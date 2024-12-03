package com.sparta.msa_exam.auth.service;

import com.sparta.msa_exam.auth.dto.request.SignInRequestDto;
import com.sparta.msa_exam.auth.dto.response.SignInResponseDto;
import com.sparta.msa_exam.auth.dto.response.SignUpResponseDto;
import com.sparta.msa_exam.auth.repository.UserRepository;
import com.sparta.msa_exam.auth.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * AuthService 생성자.
     * Base64 URL 인코딩된 비밀 키를 디코딩하여 HMAC-SHA 알고리즘에 적합한 SecretKey 객체를 생성합니다.
     *
     * @param secretKey Base64 URL 인코딩된 비밀 키
     */
    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 사용자 ID를 받아 JWT 액세스 토큰을 생성합니다.
     *
     * @param userId 사용자 ID
     * @return 생성된 JWT 액세스 토큰
     */
    public String createAccessToken(String userId, String role) {
        return Jwts.builder()
                .claim("user_id", userId)
                .claim("role", role)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, io.jsonwebtoken.SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 사용자 등록
     *
     * @param user 사용자 정보
     * @return 저장된 사용자
     */
    public SignUpResponseDto signUp(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        checkDuplicatedUserId(user.getUserId());

        return new SignUpResponseDto(userRepository.save(user).getUserId());
    }

    /**
     * 사용자 인증
     * @return JWT 액세스 토큰
     */
    public SignInResponseDto signIn(SignInRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid user ID or password");
        }

        return new SignInResponseDto(createAccessToken(user.getUserId(), user.getRole()));
    }


    private void checkDuplicatedUserId(String userId) {
        if(userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("이미 가입된 아이디입니다.");
        }
    }
}