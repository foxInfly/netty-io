package com.pupu.io.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**GPRequest
 * @author : lipu
 * @since : 2020-08-29 23:35
 */
public class GPRequest {

    /**
     * ChannelHandlerContext
     */
    private ChannelHandlerContext ctx;

    /**
     * HttpRequest
     */
    private HttpRequest req;

    public GPRequest(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }


    /**1.getUrl
     * @return   String
     */
    public String getUrl(){
        return req.uri();
    }

    /**2.getMethod
     * @return String
     */
    public String getMethod(){return req.method().name();}

    /**3.getParameters
     *
     * @return Map
     */
    public Map<String, List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(req.uri());
        return decoder.parameters();
    }

    /**4.getParameter
     *
     * @return String
     */
    public String getParameter(String name){
        Map<String, List<String>> params = getParameters();
        List<String> param = params.get(name);
        if (null == param) {
            return null;
        }else {
            return param.get(0);
        }
    }
}
