package com.pupu.io.netty.tomcat.servlet;

import com.pupu.io.netty.tomcat.http.GPRequest;
import com.pupu.io.netty.tomcat.http.GPResponse;
import com.pupu.io.netty.tomcat.http.GPServlet;

/**
 * @author : lipu
 * @since : 2020-08-29 23:37
 */
public class SecondServlet extends GPServlet {

    @Override
    protected void doPost(GPRequest request, GPResponse response) throws Exception{
        this.doPost(request,response);
    }

    @Override
    protected void doGet(GPRequest request, GPResponse response) throws Exception{
        response.write("This is First Servlet");
    }
}
