package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.AuthenticationRequest;
import com.anhnhvcoder.devteria.dto.request.LogOutRequest;
import com.anhnhvcoder.devteria.dto.request.RefreshRequest;
import com.anhnhvcoder.devteria.dto.response.AuthenticationResponse;
import com.anhnhvcoder.devteria.dto.request.IntrospectRequest;
import com.anhnhvcoder.devteria.dto.response.IntrospectResponse;
import com.anhnhvcoder.devteria.exception.AppException;
import com.anhnhvcoder.devteria.exception.ErrorCode;
import com.anhnhvcoder.devteria.model.InvalidToken;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.InvalidTokenRepository;
import com.anhnhvcoder.devteria.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;
    InvalidTokenRepository invalidTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.validDuration}")
    protected Long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshableDuration}")
    protected Long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            SignedJWT signedJWT = verifyToken(token);

        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if(!user.isActive()) throw new AppException(ErrorCode.USER_NOT_ACTIVATED);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        User thisUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(thisUser.getUsername())
                .issuer(thisUser.getFirstName() + " " + thisUser.getLastName())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope", buildScope(thisUser))
                .claim("id", thisUser.getId())
                .claim("username", thisUser.getUsername())
                .jwtID(String.valueOf(UUID.randomUUID()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Could not sign JWT", e);
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {

        var signedJwt = verifyRefreshToken(request.getToken());

        var jid = signedJwt.getJWTClaimsSet().getJWTID();
        var user = signedJwt.getJWTClaimsSet().getSubject();
        var expTime = signedJwt.getJWTClaimsSet().getExpirationTime();

        InvalidToken invalidToken = InvalidToken.builder()
                .id(jid)
                .expirationTime(expTime)
                .build();

        invalidTokenRepository.save(invalidToken);

        User refreshUser = userRepository.findByUsername(user)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var token = generateToken(refreshUser);

        return AuthenticationResponse.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    private String buildScope(User user){
        StringJoiner joiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> joiner.add("PERMISSION_" + permission.getName()));
                }
            });
        }
        return joiner.toString();
    }

    public void logOut(LogOutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyRefreshToken(request.getToken());

        String tokenId = signedToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();

        invalidTokenRepository.save(new InvalidToken(tokenId, expirationTime));

    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {


        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();

        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidTokenRepository.findById(tokenId).isPresent()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    private SignedJWT verifyRefreshToken(String refreshToken) throws JOSEException, ParseException {


        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(refreshToken);

        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();

        Date expTime = new Date(signedJWT.getJWTClaimsSet()
                .getIssueTime().toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli());

        var verified = signedJWT.verify(verifier);

        if(!(verified && expTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(invalidTokenRepository.findById(tokenId).isPresent()){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Scheduled(fixedRate = 60000)
    private void clearExpiredTokens(){
        List<InvalidToken> invalidTokens = invalidTokenRepository.findAll();
        invalidTokens.forEach(invalidToken -> {
            if(invalidToken.getExpirationTime().before(new Date())){
                invalidTokenRepository.delete(invalidToken);
            }
        });

    }

}
