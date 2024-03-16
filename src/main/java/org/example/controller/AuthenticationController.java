package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.JwtResponseTokenDto;
import org.example.dto.LoginRequestDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/gtwcl")
@RequiredArgsConstructor
public class AuthenticationController {

//    private final AuthenticationService authenticationService;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;


    // test16
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public void save(@Valid @RequestBody User userDto) {
        String password = userDto.getPassword();
        passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        userDto.setPassword(hashedPassword);

        userRepository.save(userDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public JwtResponseTokenDto createToken(@RequestBody LoginRequestDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getName(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new JwtResponseTokenDto(jwtTokenProvider.generateToken(authentication));
    }
}
