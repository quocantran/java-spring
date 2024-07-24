package com.example.test.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.UnauthorizeException;
import com.example.test.domain.User;
import com.example.test.domain.request.RequestLoginDTO;
import com.example.test.domain.response.ResponseLoginDTO;
import com.example.test.domain.response.ResponseUserDTO;
import com.example.test.service.JwtService;
import com.example.test.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/auth")

public class AuthController {

        private final JwtService jwtService;

        private final AuthenticationManagerBuilder authenticationManagerBuilder;

        private final UserService userService;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, JwtService jwtService,
                        UserService userService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.jwtService = jwtService;
                this.userService = userService;

        }

        @PostMapping("/login")
        public ResponseEntity<ResponseLoginDTO> login(@Valid @RequestBody RequestLoginDTO loginDto) {
                // Implement login logic here

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword());
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // Set authentication to Security Context (similar req.user in Express)
                SecurityContextHolder.getContext().setAuthentication(authentication);

                User current = this.userService.getUserByUsername(loginDto.getUsername());

                String accessToken = this.jwtService.createAccessToken(current.getEmail(), current);

                ResponseLoginDTO response = new ResponseLoginDTO(accessToken, current.convertUserDto());

                // create refresh token

                String refreshToken = this.jwtService.createRefreshToken(current.getEmail());
                this.userService.updateUserToken(loginDto.getUsername(), refreshToken);

                ResponseCookie resCookie = ResponseCookie.from("refresh_token", refreshToken).httpOnly(true).path("/")
                                .maxAge(60 * 60 * 24 * 30).build();

                return ResponseEntity

                                .ok().header(HttpHeaders.SET_COOKIE, resCookie.toString())
                                .body(response);
        }

        @GetMapping("/account")
        public ResponseEntity<ResponseLoginDTO.GetAccount> getAccountUser() {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User current = this.userService.getUserByUsername(email);
                ResponseLoginDTO.GetAccount response = new ResponseLoginDTO.GetAccount();
                response.setUser(current.convertUserDto());
                return ResponseEntity.ok().body(response);
        }

        @GetMapping("/refresh")
        public ResponseEntity<ResponseLoginDTO> refreshToken(@CookieValue(name = "refresh_token") String refreshToken)
                        throws UnauthorizeException {
                Jwt decoded = this.jwtService.checkRefreshToken(refreshToken);
                String email = decoded.getSubject();
                User current = this.userService.getUserByUsername(email);

                if (current == null) {
                        throw new UnauthorizeException("Invalid refresh token");
                }

                if (current.getRefreshToken().equals(refreshToken) == false) {
                        throw new UnauthorizeException("Invalid refresh token");
                }

                String accessToken = this.jwtService.createAccessToken(email, current);

                ResponseLoginDTO response = new ResponseLoginDTO(accessToken, current.convertUserDto());

                // create refresh token

                String newRefreshToken = this.jwtService.createRefreshToken(email);
                this.userService.updateUserToken(email, newRefreshToken);

                ResponseCookie resCookie = ResponseCookie.from("refresh_token", newRefreshToken).httpOnly(true)
                                .path("/")
                                .maxAge(60 * 60 * 24 * 30).build();

                return ResponseEntity

                                .ok().header(HttpHeaders.SET_COOKIE, resCookie.toString())
                                .body(response);
        }

        @PostMapping("/logout")
        public ResponseEntity<Void> postMethodName() {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                this.userService.updateUserToken(email, null);

                ResponseCookie resCookie = ResponseCookie.from("refresh_token", null).httpOnly(true).path("/")
                                .maxAge(0).build();

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, resCookie.toString()).body(null);

        }

}
