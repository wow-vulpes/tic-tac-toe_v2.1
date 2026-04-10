package tictactoe.security.filter;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tictactoe.datasource.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import tictactoe.security.jwt.JwtAuthentication;
import tictactoe.security.jwt.JwtProvider;
import tictactoe.security.jwt.JwtUtil;
import tictactoe.web.model.jwt.JwtResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class AuthFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        try {
            if (!(requestURI.equals("/auth/sign-up") || requestURI.equals("/auth/sign-in") || requestURI.equals("/auth/access-token"))){
                String authHeader = httpRequest.getHeader("Authorization");
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new IllegalArgumentException("Invalid header");
                }

                String accessToken = authHeader.substring("Bearer ".length());

                if (!jwtProvider.validateAccessToken(accessToken)){
                    throw new IllegalArgumentException("Invalid access token");
                }

                Claims claims = jwtProvider.getClaims(accessToken);
                JwtAuthentication jwtAuthentication = jwtUtil.createJwtAuthentication(claims);

                SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
            }
        } catch (IllegalArgumentException ex){
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized");
            return;
        }

        chain.doFilter(request, response);
    }
}
