package com.pupu.io.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.nio.charset.StandardCharsets;

/** GPResponse
 * @author : lipu
 * @since : 2020-08-29 23:36
 */
public class GPResponse {
    /**
     * SocketShannel的封装
     */
    private ChannelHandlerContext ctx;

    /**
     * HttpRequest
     */
    private HttpRequest req;

    public GPResponse(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    /**
     * write content to http
     * @param out out
     */
    public void write(String out){

        try {
            if (out == null || out.length() == 0) {
                return;
            }
            //设置http协议及请求头信息
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,//设置http版本1.1
                    HttpResponseStatus.OK,//设置响应状态码
                    Unpooled.wrappedBuffer(out.getBytes(StandardCharsets.UTF_8))//将输出值写出 编码为UTF-8
            );
            response.headers().set("Content-Type","text/html");

            ctx.write(response);

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            System.out.println(req.toString());
            ctx.flush();
            ctx.close();
        }
    }
}
