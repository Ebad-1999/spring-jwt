package com.tpe.security;

import com.tpe.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    /*
            in this class:
                1. Create JWT
                2. Validate JWT
                3. Extract UserName from JWT
     */
    //we neet to have secret
    private String jwtSecretKey = "sboot";

    //jwt token life cycle: 24 hours
    private long jwtExpiration = 86400000;   //24*60*60*1000


    //*********** CREATE JW TOKEN ************

    /*
        to create JWT we need 3 things
            1. userName
            2. expire date
            3. secret key with encode type
     */


    public String createToken(Authentication authentication){
        //to get userdetails of currently logged in user in the format of userdetails which is recognised by security
         UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

         return Jwts.builder().
                 setSubject(userDetails.getUsername()).
                 setIssuedAt(new Date()).  // created Time
                 setExpiration(new Date(new Date().getTime()+jwtExpiration)).  //setting life cycle
                 signWith(SignatureAlgorithm.HS512, jwtSecretKey). //
                 compact(); //compacting the provided data
    }


    //*********** VALIDATE TOKEN ************

    public boolean validateToken(String token){
        //by providing secret key and token we are validating the token
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return false;
    }


    //**********EXTRACT USERNAME FROM JW TOKEN*************

    public String getUserNameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecretKey).
                parseClaimsJws(token).
                getBody().
                getSubject();
    }


}
