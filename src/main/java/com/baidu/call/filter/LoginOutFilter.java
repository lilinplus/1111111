package com.baidu.call.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by v_chenyafei on 2017/11/21.
 */

//@WebFilter(filterName = "loginOutFilter" ,urlPatterns = "/*")
@WebFilter(filterName = "loginOutFilter" ,urlPatterns = "/api/login_out")
public class LoginOutFilter implements Filter {

    @Value("${web.uuap-ip}")
    private String uuapIp;

    @Value("${web.uuap-host}")
    private String uuapHost;

    private Logger logger= LogManager.getLogger(LoginOutFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest=(HttpServletRequest) servletRequest;
            HttpSession session=httpServletRequest.getSession();
            session.removeAttribute("_const_cas_assertion_");
            HttpServletResponse httpServletResponse=(HttpServletResponse) servletResponse;
            httpServletResponse.sendRedirect("https://" + uuapHost + "?service=http://" + uuapIp);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    @Override
    public void destroy() {

    }
}
