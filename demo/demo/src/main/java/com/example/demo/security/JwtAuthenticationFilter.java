package com.example.demo.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        System.out.println(requestTokenHeader);
        String username = null;
        String token = null;
        UserDetails userDetails = null;

        if (requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer")) {
            token = requestTokenHeader.substring(7);
            try {
                username = jwtTokenHelper.getUsernameFromToken(token);
            }catch (IllegalAccessError e){
                System.out.println("Unable to get jwt token.");
            }catch (ExpiredJwtException e){
                System.out.println("JWT Expired.");
            }catch (MalformedJwtException e){
                System.out.println("JWT is not correct.");
            }


            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtTokenHelper.validateToken(token,userDetails)){

                }
            }else{
                System.out.println("Invalid token.");
            }

        }else{
            System.out.println("JWT Token does not begin with Bearer");
        }

        //once we get the token, we will validate.
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            if (jwtTokenHelper.validateToken(token, userDetailsService.loadUserByUsername(username))){

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else{
                System.out.println("Invalid JWT Token.");
            }
        }else{
            System.out.println("Username is null or context is not null.");
        }
        filterChain.doFilter(request,response);
    }
}
