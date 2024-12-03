package com.sparta.msa_exam.auth.controller;


import com.sparta.msa_exam.auth.dto.request.SignInRequestDto;
import com.sparta.msa_exam.auth.dto.response.SignInResponseDto;
import com.sparta.msa_exam.auth.dto.response.SignUpResponseDto;
import com.sparta.msa_exam.auth.service.AuthService;
import com.sparta.msa_exam.auth.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-in")
    public ResponseEntity<SignInResponseDto> createAuthenticationToken(@RequestBody SignInRequestDto signInRequest) {
        SignInResponseDto response = authService.signIn(signInRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/auth/sign-up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody User user) {
        SignUpResponseDto response = authService.signUp(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}




