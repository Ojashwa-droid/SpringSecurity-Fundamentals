package com.ojashwa.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class RequestValidationBeforeFilter implements Filter {
    /**
     * <p>
     * This custom filter {@link RequestValidationBeforeFilter} is used to validate the request before it is processed by
     * the Spring Security filter chain. It checks to see if the credential's email contains the "test" string, then send a
     * <code>BadRequest</code> in the httpResponse status.<br>
     * If the request is invalid, it will throw a
     * {@link org.springframework.security.authentication.BadCredentialsException}
     * </p>
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response
            , FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            header = header.trim();
            if (StringUtils.startsWithIgnoreCase(header, "Basic ")) {
                byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decodedToken;
                try{
                    decodedToken = Base64.getDecoder().decode(base64Token);
                    String token = new String(decodedToken, StandardCharsets.UTF_8); // username:password
                    int delim = token.indexOf(":");
                    if (delim == -1) {
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }

                    String email = token.substring(0, delim);
                    if (email.contains("test")) {
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
/*                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        // Construct the JSON response
                        ObjectMapper mapper = new ObjectMapper();
                        Map<String, Object> body = Map.of(
                                "status", HttpStatus.BAD_REQUEST.value(),
                                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                                "message", "Input username contains string 'test'",
                                "path", req.getRequestURI()
                        );
                        res.getWriter().write(mapper.writeValueAsString(body));*/
                        return;
                    }
                } catch (IllegalArgumentException e){
                    throw new IllegalArgumentException("Failed to parse basic authentication token");
                }
            }
        }
        chain.doFilter(request, response);
    }
}