package com.lapakpedia.storefront.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtService {

    private static final Long EXPIRATION_TIME = 30 * 60 * 1000l;
    public static final String KEY = "UjXn2r5u8x!A%D*G-KaPdSgVkYp3s6v9y$B?E(H+MbQeThWmZq4t7w!z%C*F)J@N";

    public String generateToken(String userName) {
        Date exp = new Date(System.currentTimeMillis() + (30 * 60 * 1000));
        Key key = Keys.hmacShaKeyFor(KEY.getBytes());
        Claims claims = Jwts.claims().setSubject(userName);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(exp).compact();
    }

    public Claims getClaimFromToken(String token){
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token)
                .getSubject();
    }

    public Boolean isTokenExpired(String token){
        return getClaimFromToken(token)
                .getExpiration()
                .before(new Date());
    }
}
