package de.htwberlin.webtech.expensetracker.security;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.persistence.Column;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@NoArgsConstructor
public class JWTRequestFIlter extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public JWTRequestFIlter(JWTUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /*get token from reqheader and username from token*/
    /*validate token */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /*get header*/
        final String requestTokenHeader = request.getHeader(AUTHORIZATION);
        String jwtToken = null;
        String userNameFromJwtToken = null;

        /*parse header in order to retrieve token*/
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            jwtToken = requestTokenHeader.substring(7);


        /*get username from token*/
        try{
          userNameFromJwtToken = jwtUtil.getUsernameFromToken(jwtToken);
        }catch(IllegalArgumentException e){
            throw new RuntimeException("No Username was retrieved from Token");
        }catch(ExpiredJwtException ex){
            throw new RuntimeException("Token has expired");
        }
    }

        /*validate token*/
        if(userNameFromJwtToken != null && SecurityContextHolder.getContext().getAuthentication() ==null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userNameFromJwtToken);
            /*validation with the userDetails*/
            if(jwtUtil.validateToken(jwtToken, userDetails)){
                /*create an instance of auth object, set details and send to context */
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }


}
