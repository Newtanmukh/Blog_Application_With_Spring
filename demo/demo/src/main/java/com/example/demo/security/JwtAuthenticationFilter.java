package com.example.demo.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username = null;
        String token = null;

        if (request!=null && requestTokenHeader.startsWith("Bearer")) {
            token = requestTokenHeader.substring(7);
            try {
                username = jwtTokenHelper.getUsernameFromToken(token);
            }catch (IllegalAccessError e){
                e.printStackTrace();
                System.out.println("Unable to get jwt token.");
            }catch (ExpiredJwtException e){
                System.out.println("JWT Expired.");
            }catch (MalformedJwtException e){

            }


            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtTokenHelper.validateToken(token,userDetails)){

                }
            }else{
                System.out.println("Invalid token.");
            }

        }else{
            System.out.println("JWT Token does not begin with Bearer");
        }

    }
}
