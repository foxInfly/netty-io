package com.pupu.io.bio.tomcat.servlet;

import com.pupu.io.bio.tomcat.http.GPResponse;
import com.pupu.io.bio.tomcat.http.GPServlet;
import com.pupu.io.bio.tomcat.http.GpRequest;

/**
 * @author : lipu
 * @since : 2020-08-29 11:10
 */
public class FirstServlet extends GPServlet {
    @Override
    protected void doPost(GpRequest request, GPResponse response) throws Exception{
        this.doPost(request,response);
    }

    @Override
    protected void doGet(GpRequest request, GPResponse response) throws Exception{
        response.write("This is First Servlet");
    }
}
