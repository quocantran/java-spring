package com.example.test.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import com.example.test.domain.User;
import com.example.test.domain.request.RequestLoginDTO;
import com.nimbusds.jose.util.Base64;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.access-token.secret}")
    private String jwtAccessSecretKey;

    @Value("${jwt.access-token.expiration}")
    private long jwtAccessExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long jwtRefreshExpiration;

    @Value("${jwt.refresh-token.secret}")
    private String jwtRefreshSecretKey;

    private final ArrayList<String> roles = new ArrayList<String>();

    public static final MacAlgorithm MAC_ALGORITHM = MacAlgorithm.HS256;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static SecretKey getSecretKey(String secretKey) {
        byte[] keyBytes = Base64.from(secretKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, MAC_ALGORITHM.getName());

    }

    public Jwt checkRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey(jwtAccessSecretKey))
                .macAlgorithm(MAC_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            throw e;
        }
    }

    public String createAccessToken(String email, User user) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtAccessExpiration, ChronoUnit.SECONDS);
        this.roles.add("ROLE_USER_CREATE");
        this.roles.add("ROLE_USER_READ");
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", user.convertUserDto())
                .claim("permission", roles)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MAC_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public String createRefreshToken(String email) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.jwtRefreshExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("email", email)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MAC_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }
}
