package com.pupu.io.netty.tomcat.http;



/**
 * @author : lipu
 * @since : 2020-08-29 23:37
 */
public abstract class GPServlet {
    public void service(GPRequest request, GPResponse response)throws Exception{
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }

    protected abstract void doPost(GPRequest request, GPResponse response)throws Exception;

    protected abstract void doGet(GPRequest request, GPResponse response)throws Exception;
}
