package com.sparta.msa_exam.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "p_users")
public class User {
    @Id
    private String userId;
    private String userName;
    private String password;
    private String role;
}
