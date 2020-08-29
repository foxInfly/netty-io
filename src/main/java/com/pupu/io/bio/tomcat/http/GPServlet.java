package com.pupu.io.bio.tomcat.http;

/**
 * @author : lipu
 * @since : 2020-08-29 11:13
 */
public abstract class GPServlet {

    public void service(GpRequest request,GPResponse response)throws Exception{
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }

    protected abstract void doPost(GpRequest request, GPResponse response)throws Exception;

    protected abstract void doGet(GpRequest request, GPResponse response)throws Exception;
}
