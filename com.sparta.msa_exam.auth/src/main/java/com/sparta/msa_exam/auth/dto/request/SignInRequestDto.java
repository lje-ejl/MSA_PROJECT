package com.sparta.msa_exam.auth.dto.request;

import lombok.Getter;

@Getter
public class SignInRequestDto {
    private String userId;
    private String password;
}