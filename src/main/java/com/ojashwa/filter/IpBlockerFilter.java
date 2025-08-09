package com.ojashwa.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Map;

/**
 * This is just a random practice custom filter that has been written for learning purpose only
 */

public class IpBlockerFilter implements Filter {
    /**
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // The aim is to block any request ip address starting with 192.168.5...
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String remoteAddr = req.getRemoteAddr().trim();
        if (remoteAddr == null || remoteAddr.equals("")) {
            throw new ServletException("Remote address is empty");
        }
        if (remoteAddr.startsWith("127.0.0.1")){
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> body = Map.of(
                    "status", HttpStatus.FORBIDDEN.value(),
                    "error", HttpStatus.FORBIDDEN.getReasonPhrase(),
                    "message", "Access from your IP range is not allowed",
                    "path", req.getRequestURI()
            );
            res.getWriter().write(mapper.writeValueAsString(body));
            return;
        }
        chain.doFilter(request, response);
    }
}
