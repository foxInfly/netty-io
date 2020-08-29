package com.pupu.io.bio.tomcat.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author : lipu
 * @since : 2020-08-29 11:14
 */
public class GpRequest {

    private String method;
    private String url;

    public GpRequest(InputStream in){
        try {
            String content = "";
            byte[] buffer = new byte[1024];
            int len = 0;
            if ((len = in.read(buffer)) > 0) {
                content = new String(buffer,0,len);
            }
            System.out.println(content);
            String line = content.split("\\n")[0];
            System.out.println("line: "+line);
            // \s 是匹配所有空白符，包括换行，\S 非空白符，包括换行。
            String[] arr = line.split("\\s");

            this.method=arr[0];
            //根据？截取没有参数的部分
            this.url=arr[1].split("\\?")[0];

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getUrl() {

        return url;
    }

    public String getMethod() {
        return method;
    }
}
