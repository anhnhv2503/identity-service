package com.anhnhvcoder.devteria.controller;

import com.anhnhvcoder.devteria.dto.request.VerifyEmailRequest;
import com.anhnhvcoder.devteria.dto.response.ApiResponse;
import com.anhnhvcoder.devteria.service.EmailService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestBody VerifyEmailRequest request) throws ParseException, JOSEException {

            var signedJWT = emailService.verifyEmailFromToken(request);
            log.info("Email validate success");
            return ResponseEntity.ok(ApiResponse.builder()
                    .code(1000)
                    .message("Email validate success")
                    .result(null)
                    .build());

    }

}
