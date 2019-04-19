package com.baidu.call.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by chenyafei01_sh on 2018/1/5.
 */
public class GetUuapUser {

    private static Logger logger = LogManager.getLogger(GetUuapUser.class);

    public static String GetUser() {
        String username = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            Assertion assertion = (Assertion) session.getAttribute("_const_cas_assertion_");
            logger.error("GetUuapUser --- uuap login info :" + assertion);
            if (assertion == null) {
                  //username = readConfigByPath("///usr/local/call/LoginUser.properties", "username");
//                username = readConfigByPath("D:\\dist\\LoginUser.properties", "username");
                username = "v_gaochuang";
//                username = null;
            } else {
                username = assertion.getPrincipal().getName();
            }
            logger.error("GetUuapUser --- uuap login username :" + username);
        } catch (Exception e) {
            username = null;
            logger.error(e);
        }
        return username;
    }
}
