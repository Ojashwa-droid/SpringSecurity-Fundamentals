package com.ojashwa.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This custom filter {@link CsrfCookieFilter} is written to read the csrf-token manually in order for the
 * CookieCsrfTokenRepository (which is lazily initialized) to generate the token.
 * Because, using csrfTokenRepository(CookieCsrfTokeRepository.withHttpOnlyFalse()) generates the token lazily meaning
 * it will not generate the token unless and until one requests to read it.
 */
public class CsrfCookieFilter extends OncePerRequestFilter {
    /**
     *   @param request
     *   @param response
     *   @param filterChain
     *   @throws ServletException
     *   @throws IOException
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // To read the token, we have to first get the token
      CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
      // Render the token value to a cookie by causing the deferred token to be loaded
      csrfToken.getToken(); // During the execution of this piece of code, the actual token will be generated.
      filterChain.doFilter(request, response);
    }
}