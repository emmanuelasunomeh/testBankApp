package com.example.bank.application.security.jwt;

import com.example.bank.application.security.UserAccess;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    private String jwtSecret= "BankApplication";
    private int jwtExpiration = 86400;

    /**
     * @param authentication
     * @param loginRquestId
     * @return
     */
    public String generateJwtToken(Authentication authentication, Long loginRquestId) {

        UserAccess userAccess = (UserAccess) authentication.getPrincipal();
        
        Claims claims = Jwts.claims().setSubject(userAccess.getUsername());
        claims.put("userId", userAccess.getUser().getId() + "");
//        claims.put("role", userAccess.getAuthorities());
        claims.put("loginRquestId", loginRquestId);
        claims.put("userAccess", userAccess);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * @param authToken
     * @return 
     */
    public Claims getClaims(String authToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken)
                    .getBody();

            return claims;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
        }
        return null;
    }
    
    /**
     * @param request
     * @return 
     */
    public String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }
        return null;
    }
        
//    /**
//     * @param httpRequest
//     * @return
//     */
//    public Long getLoginRquestId(HttpServletRequest httpRequest){
//        String authToken = this.getJwt(httpRequest);
//        Claims claims = getClaims(authToken);
//        Object loginRquestId = claims.get("loginRquestId");
//        return loginRquestId!=null ? Long.parseLong(loginRquestId+"") : 0L;
//    }
    
    /**
     * @param httpRequest
     */
    public void clearClaims(HttpServletRequest httpRequest){
        String authToken = this.getJwt(httpRequest);
        Claims claims = getClaims(authToken);
        claims.clear();
    }

    /**
     * @param authToken
     * @return 
     */
    public boolean validateJwtToken(String authToken) {
        Claims claims = this.getClaims(authToken);
        return claims!=null;
    }

    /**
     * @param token
     * @return 
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
