package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequestDto;
import org.example.dto.JwtResponseTokenDto;
import org.example.repository.UserRepository;
import org.example.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

//    @Transactional
//    public JwtResponseTokenDto createToken(LoginRequestDto requestDto) {
//        String userName = requestDto.getUserName();
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, requestDto.getPassword()));
//    //    String token = jwtTokenProvider.createToken(userName, getRoleList(authenticate));
//
//   //     JwtResponseTokenDto jwtResponseTokenDto = new JwtResponseTokenDto();
//    //    jwtResponseTokenDto.setToken(token);
//        Long id = userRepository.findByUserName(userName).get().getId();
//     //   saveTokenByUserId(id, token);
//        return jwtResponseTokenDto;
//    }
}
