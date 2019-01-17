package com.baidu.call.config;

import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by chenshouqin on 2016-11-06 15:57.
 * Contact: BaiduHI : garychenqin, QQ:1175340090
 */

//@Configuration
//@ConfigurationProperties(prefix = "cas", ignoreUnknownFields = true)
public class CasConfig {

    private String casServerUrlPrefix;  // sso验证地址
    private String casServerLoginUrl;   // sso登录地址
    private String serverName;  // sso验证或者登陆后返回的地址
    private String urlPatterns; // sso过滤匹配
    private String encoding;    // sso编码

    /**
     * 注册CAS SSO 验证用户是否存在Filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean cas20ValidateRegistration() {
        FilterRegistrationBean cas20 = new FilterRegistrationBean();
        cas20.setFilter(new Cas20ProxyReceivingTicketValidationFilter());
        cas20.addUrlPatterns(urlPatterns);
        cas20.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        cas20.addInitParameter("serverName", serverName);
        cas20.addInitParameter("encoding", encoding);
        return cas20;
    }

    /**
     * 注册CAS SSO 用户登录Filter
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean cas20LoginRegistration() {
        FilterRegistrationBean cas20 = new FilterRegistrationBean();
        cas20.setFilter(new AuthenticationFilter());
        cas20.addUrlPatterns(urlPatterns);
        cas20.addInitParameter("casServerLoginUrl", casServerLoginUrl);
        cas20.addInitParameter("serverName", serverName);
        cas20.addInitParameter("encoding", encoding);
        return cas20;
    }

    public String getCasServerUrlPrefix() {
        return casServerUrlPrefix;
    }

    public void setCasServerUrlPrefix(String casServerUrlPrefix) {
        this.casServerUrlPrefix = casServerUrlPrefix;
    }

    public String getCasServerLoginUrl() {
        return casServerLoginUrl;
    }

    public void setCasServerLoginUrl(String casServerLoginUrl) {
        this.casServerLoginUrl = casServerLoginUrl;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUrlPatterns() {
        return urlPatterns;
    }

    public void setUrlPatterns(String urlPatterns) {
        this.urlPatterns = urlPatterns;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
