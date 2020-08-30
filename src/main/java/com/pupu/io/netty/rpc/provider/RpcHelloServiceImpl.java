package com.pupu.io.netty.rpc.provider;

import com.pupu.io.netty.rpc.api.IRpcHelloService;

/**
 * @author : lipu
 * @since : 2020-08-30 22:39
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    public String hello(String name) {
        return "Hello " + name +"!";
    }
}
