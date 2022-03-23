package com.secondcommit.forum.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Methods to generate and validate JWT tokens
 */
@Component
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${app.jwt.secret}")
    private String JWT_SECRET;
    @Value("${app.jwt.expiration-ms}")
    private int JWT_EXPIRATION_MS;
    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    /**
     * Method that generates the token
     * @param authentication type.
     * @return JWT token.
     */
    public String generateJwtToken(Authentication authentication, Boolean remember) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //If remember is true, no expiration date is set
        if (remember){
            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .claim(AUTHORITIES_KEY, authorities)
                    .setIssuedAt(new Date())
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } else {
            return Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .claim(AUTHORITIES_KEY, authorities)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION_MS))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        }
    }

    /**
     * Method that extracts the username from the token
     * @param token jwt
     * @return username included in the token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Method that validates the JWT token
     * @param authToken JWT token
     * @return boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    /**
     * Method that returns a token with the user data (username and password), plus roles
     * @param token jwt token
     * @param existingAuth authentication type
     * @param userDetails user data
     * @return updated token
     */
    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(JWT_SECRET);

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
