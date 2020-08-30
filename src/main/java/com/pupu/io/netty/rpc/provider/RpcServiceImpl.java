package com.pupu.io.netty.rpc.provider;

import com.pupu.io.netty.rpc.api.IRpcService;

/**
 * @author : lipu
 * @since : 2020-08-30 22:40
 */
public class RpcServiceImpl implements IRpcService {
    public int add(int a, int b) {
        return a+b;
    }

    public int sub(int a, int b) {
        return a-b;
    }

    public int mult(int a, int b) {
        return a*b;
    }

    public int div(int a, int b) {
        return a/b;
    }
}
