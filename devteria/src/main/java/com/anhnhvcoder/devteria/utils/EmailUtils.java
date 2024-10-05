package com.anhnhvcoder.devteria.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@Slf4j
public class EmailUtils {

    @Value("${client.domain}")
    private String clientUrl;
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    private String generateVerifyEmailToken(String email){
        try {
            // Create the JWT header and claims
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))  // Fix the date handling
                    .build();

            // Create the SignedJWT object (which combines header and claims)
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);

            // Sign the JWT with the provided key
            MACSigner signer = new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);

            // Return the serialized token (this is the full JWT: header, payload, signature)
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Could not sign JWT", e);
            throw new RuntimeException(e);
        }
    }

    public String subjectRegister(){
        return "Welcome to Devteria Learning Project";
    }

    public String bodyRegister(String email ,String firstName, String lastName){
        return "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "<head>\n"
                + "  <meta charset=\"utf-8\">\n"
                + "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "  <style>\n"
                + "    body {\n"
                + "      font-family: Arial, sans-serif;\n"
                + "      background-color: #f4f4f4;\n"
                + "      margin: 0;\n"
                + "      padding: 0;\n"
                + "    }\n"
                + "\n"
                + "    .email-container {\n"
                + "      max-width: 600px;\n"
                + "      margin: 20px auto;\n"
                + "      background-color: #fff;\n"
                + "      border-radius: 8px;\n"
                + "      overflow: hidden;\n"
                + "      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n"
                + "    }\n"
                + "\n"
                + "    .header {\n"
                + "      background-color: #34db74;\n"
                + "      color: #fff;\n"
                + "      padding: 20px;\n"
                + "      text-align: center;\n"
                + "    }\n"
                + "\n"
                + "    .content {\n"
                + "      padding: 20px;\n"
                + "    }\n"
                + "\n"
                + "    .button{\n" +
                "        background-color: #34db74;\n" +
                "        color: #fff;\n" +
                "        margin: 24px 24px 24px 24px;\n" +
                "        padding: 10px 20px;\n" +
                "        border: none;\n" +
                "        cursor: pointer;\n" +
                "        text-decoration: none;\n" +
                "      }\n"
                + "\n"
                + " a {\n" +
                "        color: #fff;\n" +
                "      }\n"
                + "    .footer {\n"
                + "      background-color: #34db74;\n"
                + "      color: #fff;\n"
                + "      padding: 10px;\n"
                + "      text-align: center;\n"
                + "    }\n"
                + "  </style>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <div class=\"email-container\">\n"
                + "    <div class=\"header\">\n"
                + "     <h1>Devteria Learning Project</h1>\n "
                + "      <h2>Welcome " + firstName + " "  + lastName + "</h2>\n"
                + "    </div>\n"
                + "    <div class=\"content\">\n"
                + "      <h3>Information:</h3>\n"
                + "      <p>Date : " + LocalDate.now() + "</p>\n"
                + "      <p>This email valid in 24h. Please verify your email as soon as possible</p>\n"
                + "      <p>Contact 0976652503.</p>\n"
                + "      <a class=\"button\" href=\" " + clientUrl + "verify?token=" + generateVerifyEmailToken(email) + "\">VERIFY</a>\n"
                + "      <p>We are ready support</p>\n"
                + "    </div>\n"
                + "    <div class=\"footer\">\n"
                + "      <p>Thanks for Subcribe</p>\n"
                + "    </div>\n"
                + "  </div>\n"
                + "</body>\n"
                + "</html>";
    }

    public String subjectVerifyEmail(){
        return "Verify your email";
    }

    public String bodyVerifyEmail(){
        return null;
    }
}
