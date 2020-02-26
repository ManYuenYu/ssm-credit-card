package com.ioioh.ssm.license.filter;

import com.ioioh.ssm.license.util.LicenseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-25 3:45 PM
 */
public class LicenseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TestFilter");
        

    }

    @Override
    public void destroy() {

    }

    /**
     *  授权过滤器处理返回结果
     * @param response
     * @return
     */
    private boolean licenseHandle(ServletResponse response){


        if(LicenseUtil.resultPass){

            return true;

        }else{


            return false;
        }


    }
}
