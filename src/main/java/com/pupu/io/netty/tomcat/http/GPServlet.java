package com.pupu.io.netty.tomcat.http;



/** GPServlet
 * @author : lipu
 * @since : 2020-08-29 23:37
 */
public abstract class GPServlet {

    /**
     * 1.service()
     * @param request request
     * @param response response
     */
    public void service(GPRequest request, GPResponse response)throws Exception{
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }

    /**
     * 2.doPost
     * @param request request
     * @param response response
     */
    protected abstract void doPost(GPRequest request, GPResponse response)throws Exception;

    /**
     * 3.doGet
     * @param request request
     * @param response response
     */
    protected abstract void doGet(GPRequest request, GPResponse response)throws Exception;
}
