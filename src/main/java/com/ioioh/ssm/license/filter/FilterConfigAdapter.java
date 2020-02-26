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
public class LicenseFilterConfigAdapter {

    @Bean
    public FilterRegistrationBean licenseFilterRegistration(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        LicenseFilter licenseFilter = new LicenseFilter();
        registrationBean.setFilter(licenseFilter);
        registrationBean.setName("licenseFilter");
        registrationBean.setOrder(1);
        List<String> urlList = new ArrayList<String>();
        urlList.add("/*");
        registrationBean.addInitParameter("paramName", "paramValue");
        registrationBean.setUrlPatterns(urlList);
        return registrationBean;
    }
}
