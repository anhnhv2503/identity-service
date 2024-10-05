package com.anhnhvcoder.devteria.controller;

import com.anhnhvcoder.devteria.dto.request.AuthenticationRequest;
import com.anhnhvcoder.devteria.dto.request.IntrospectRequest;
import com.anhnhvcoder.devteria.dto.request.LogOutRequest;
import com.anhnhvcoder.devteria.dto.request.RefreshRequest;
import com.anhnhvcoder.devteria.dto.response.ApiResponse;
import com.anhnhvcoder.devteria.dto.response.AuthenticationResponse;
import com.anhnhvcoder.devteria.dto.response.IntrospectResponse;
import com.anhnhvcoder.devteria.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);

        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticateToken(@RequestBody IntrospectRequest introspectRequest)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(introspectRequest);

        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogOutRequest request) throws ParseException, JOSEException {
        authenticationService.logOut(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
       var result = authenticationService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.builder().result(result).build());
    }


}
