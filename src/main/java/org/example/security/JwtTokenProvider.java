package org.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.example.exception.BlogAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${key}")
    private String secret;
    @Value("${app.jwt-expiration-milliseconds}")
    private int jwtExpirationInMs;

    // generate token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }


    // get username from the token
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }


//    @Transactional
//    @Override
//    public JwtResponseTokenDto createToken(LoginRequestDto requestDto) {
//        String userName = requestDto.getUserName();
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, requestDto.getPassword()));
//        String token = jwtTokenProvider.createToken(userName, getRoleList(authenticate));
//        JwtResponseTokenDto jwtResponseTokenDto = new JwtResponseTokenDto();
//        jwtResponseTokenDto.setToken(token);
//        Long id = userRepository.findByUserName(userName).get().getId();
//        saveTokenByUserId(id, token);
//        return jwtResponseTokenDto;
//    }
//
//    private List<Role> getRoleList(Authentication authentication) {
//        return authentication
//                .getAuthorities()
//                .stream()
//                .map(entry -> entry.getAuthority().replaceAll("ROLE_", ""))
//                .map(roleRepository::findByName).collect(Collectors.toList());
//    }
//
//    private void saveTokenByUserId(Long id, String token) {
//        Token tokenFromDb = Optional.ofNullable(tokenRepository.findByUserId(id)).orElse(new Token());
//        tokenFromDb.setTokenValue(token);
//        tokenFromDb.setUserId(id);
//        tokenRepository.save(tokenFromDb);
//    }

}