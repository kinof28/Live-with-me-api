package com.Abdelwahab.Live.With.ME.filter;

import com.Abdelwahab.Live.With.ME.exceptions.BadCredentialsException;
import com.Abdelwahab.Live.With.ME.services.AdminDetailsServiceImp;
import com.Abdelwahab.Live.With.ME.services.UserDetailsServiceImp;
import com.Abdelwahab.Live.With.ME.utilities.JWTUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    JWTUtility jwtUtility;
    UserDetailsServiceImp userDetailsServiceImp;
    AdminDetailsServiceImp adminDetailsServiceImp;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authorisationHeader = httpServletRequest.getHeader("Authorization");
        UserDetails user;
        String token=null,userName=null;
        try {
            if(authorisationHeader != null && !authorisationHeader.startsWith("ad")){
                if (authorisationHeader != null && authorisationHeader.startsWith("Bearer")) {
                    token = authorisationHeader.substring(7);
                    userName = jwtUtility.extractUserName(token);

                }
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    user = this.userDetailsServiceImp.loadUserByUsername(userName);
                    if (this.jwtUtility.validateToken(token, user,false)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }else if(authorisationHeader != null && authorisationHeader.startsWith("ad")){
                if (authorisationHeader != null && authorisationHeader.startsWith("adBearer")) {
                    token = authorisationHeader.substring(9);
                    userName = jwtUtility.extractUserName(token);

                }
                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    user = this.adminDetailsServiceImp.loadUserByUsername("admin_"+userName);
                    if (this.jwtUtility.validateToken(token, user,true)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }

        }catch(Exception e ){
            System.out.println("here is this shit in JWT Filter : "+e);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
