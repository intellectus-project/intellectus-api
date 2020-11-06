package com.intellectus.security;

import com.google.common.collect.Lists;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private static final List<UserToken> tokens = Lists.newArrayList();

    @Value("${spring.security.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.security.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (existsConnection(userPrincipal.getUsername())){
            for (UserToken authToken : tokens) {
                if (authToken.getUsername().equals(userPrincipal.getUsername())) {
                    authToken.connectUser();
                    return authToken.getToken();
                }
            }
        }

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        tokens.add(new UserToken(userPrincipal.getUsername(), token));

        return token;
    }


    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            if (tokens.stream().anyMatch(token -> token.getToken().equals(authToken))) {
                return true;
            }
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public boolean validateUsername(String username, String authToken) {
        return tokens.stream().anyMatch(token -> token.getUsername().equals(username) && token.getToken().equals(authToken));
    }

    private boolean existsConnection(String username){
        return tokens.stream().anyMatch(token -> token.getUsername().equals(username));
    }

    public boolean revoqueToken(String username) {
        if (existsConnection(username)) {
            for (UserToken authToken : tokens) {
                if (authToken.getUsername().equals(username)) {
                    authToken.disconnectUser();
                    if(authToken.getConnections()==0) tokens.remove(authToken);
                    return  true;
                }
            }
        }
        return false;
    }
}