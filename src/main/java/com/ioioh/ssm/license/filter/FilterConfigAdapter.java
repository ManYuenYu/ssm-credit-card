package com.ioioh.ssm.license.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-25 3:52 PM
 */
@Configuration
public class FilterConfigAdapter {

    @Bean
    public FilterRegistrationBean licenseFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        ResultHandleFilter resultHandleFilter = new ResultHandleFilter();
        registrationBean.setFilter(resultHandleFilter);
        registrationBean.setName("resultHandleFilter");
        registrationBean.setOrder(1);
        List<String> urlList = new ArrayList<String>();
        urlList.add("/*");
        registrationBean.addInitParameter("paramName", "paramValue");
        registrationBean.setUrlPatterns(urlList);
        return registrationBean;
    }
}
