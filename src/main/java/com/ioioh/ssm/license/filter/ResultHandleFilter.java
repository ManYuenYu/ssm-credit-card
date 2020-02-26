package com.ioioh.ssm.license.filter;

import com.ioioh.ssm.license.util.LicenseUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author YuWenYuen
 * @version 1.0
 * @date 2020-02-25 3:45 PM
 */
public class ResultHandleFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("TestFilter");

        // 转换成代理类
        ResponseWrapper wrapperResponse = new ResponseWrapper((HttpServletResponse)response);
        // 这里只拦截返回，直接让请求过去，如果在请求前有处理，可以在这里处理
        filterChain.doFilter(request, wrapperResponse);
        // 获取返回值
        byte[] content = wrapperResponse.getContent();
        //判断是否有值
        if (content.length > 0) {

            String responseStr = new String(content, "UTF-8");
            System.out.println("返回值:" + responseStr);
            String resultText = null;

            try {
                //......根据需要处理返回值
                resultText = licenseHandle(responseStr);

            } catch (Exception e) {
                e.printStackTrace();
            }
            //把返回值输出到客户端
            ServletOutputStream out = response.getOutputStream();
            out.write(resultText.getBytes());
            out.flush();
        }

    }

    @Override
    public void destroy() {

    }

    /**
     *  授权过滤器处理返回结果
     * @param responseStr
     * @return
     */
    private String licenseHandle(String responseStr){

        LicenseUtil.resultPass = false;
        if(LicenseUtil.resultPass){

            return responseStr;

        }else{

            return "{\"errorMessage\":\"服务授权时间已过期\"";
        }

    }
}
