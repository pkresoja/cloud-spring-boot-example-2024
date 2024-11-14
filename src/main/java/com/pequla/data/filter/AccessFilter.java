package com.pequla.data.filter;

import com.pequla.data.service.AccessService;
import jakarta.servlet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AccessFilter implements Filter {

    private final AccessService service;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        service.saveAccess(servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
