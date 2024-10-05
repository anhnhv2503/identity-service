package com.anhnhvcoder.devteria.service;

import com.anhnhvcoder.devteria.dto.request.VerifyEmailRequest;
import com.anhnhvcoder.devteria.exception.AppException;
import com.anhnhvcoder.devteria.exception.ErrorCode;
import com.anhnhvcoder.devteria.model.User;
import com.anhnhvcoder.devteria.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String systemMail;
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        // Send email with HTML
        log.info("Sending email from: " + systemMail);
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(systemMail);
        message.setRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");

        mailSender.send(message);
    }

    public SignedJWT  verifyEmailFromToken(VerifyEmailRequest request) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(request.getToken());

        String email = signedJWT.getJWTClaimsSet().getSubject();
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if(!(verified && expTime.after(new Date()))){
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setActive(true);
        userRepository.save(user);
        log.info("Email verified for user");

        return signedJWT;
    }
}
