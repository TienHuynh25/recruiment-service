package vn.unigap.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.unigap.api.dto.in.LoginRequestDTO;
import vn.unigap.api.dto.in.RegisterRequestDTO;
import vn.unigap.api.dto.out.AuthResponseDTO;
import vn.unigap.api.entity.UserEntity;
import vn.unigap.api.exception.DuplicateResourceException;
import vn.unigap.api.exception.ValidationException;
import vn.unigap.api.repository.EmployerRepository;
import vn.unigap.api.repository.SeekerRepository;
import vn.unigap.api.repository.UserRepository;
import vn.unigap.api.security.JwtTokenProvider;

import java.util.Date;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private SeekerRepository seekerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public AuthResponseDTO register(RegisterRequestDTO registerRequest) {
        // Check if username already exists
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + registerRequest.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + registerRequest.getEmail());
        }

        // Validate roleEntityId if provided
        if (registerRequest.getRoleEntityId() != null) {
            if (registerRequest.getRole() == UserEntity.UserRole.EMPLOYER) {
                if (!employerRepository.existsById(registerRequest.getRoleEntityId())) {
                    throw new ValidationException("Employer not found with id: " + registerRequest.getRoleEntityId());
                }
            } else if (registerRequest.getRole() == UserEntity.UserRole.SEEKER) {
                if (!seekerRepository.existsById(registerRequest.getRoleEntityId())) {
                    throw new ValidationException("Seeker not found with id: " + registerRequest.getRoleEntityId());
                }
            }
        }

        // Create new user
        UserEntity user = UserEntity.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(registerRequest.getRole())
                .roleEntityId(registerRequest.getRoleEntityId())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        userRepository.save(user);

        // Generate JWT token
        String token = tokenProvider.generateTokenFromUsername(user.getUsername());

        return new AuthResponseDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getRoleEntityId()
        );
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        UserEntity user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ValidationException("User not found"));

        return new AuthResponseDTO(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getRoleEntityId()
        );
    }
}
