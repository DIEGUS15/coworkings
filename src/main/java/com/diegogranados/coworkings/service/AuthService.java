package com.diegogranados.coworkings.service;

import com.diegogranados.coworkings.dto.AuthResponse;
import com.diegogranados.coworkings.dto.LoginRequest;
import com.diegogranados.coworkings.dto.RegisterRequest;
import com.diegogranados.coworkings.entity.Role;
import com.diegogranados.coworkings.entity.User;
import com.diegogranados.coworkings.exception.EmailAlreadyExistsException;
import com.diegogranados.coworkings.repository.UserRepository;
import com.diegogranados.coworkings.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

                User user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow();

                String token = jwtService.generateToken(user);

                return AuthResponse.builder()
                                .token(token)
                                .role(user.getRole().name())
                                .build();
        }

        public AuthResponse register(RegisterRequest request) {
                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new EmailAlreadyExistsException(request.getEmail());
                }

                User newUser = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.OPERADOR)
                                .build();

                userRepository.save(newUser);

                String token = jwtService.generateToken(newUser);

                return AuthResponse.builder()
                                .token(token)
                                .role(newUser.getRole().name())
                                .build();
        }
}
