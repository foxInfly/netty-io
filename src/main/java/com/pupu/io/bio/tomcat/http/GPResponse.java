package com.pupu.io.bio.tomcat.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author : lipu
 * @since : 2020-08-29 11:15
 */
public class GPResponse {

    private OutputStream out;

    public GPResponse(OutputStream out){
        this.out = out;
    }


    public void write(String s) throws IOException {

        //用的http协议，返回应该遵循http规则
        //如状态码等
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type:text/html;\n")
                .append("\r\n")
                .append(s);
        System.out.println(sb.toString());
        out.write(sb.toString().getBytes());


    }
}
