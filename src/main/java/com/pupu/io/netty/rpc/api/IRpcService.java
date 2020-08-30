package com.pupu.io.netty.rpc.api;

/**
 * @author : lipu
 * @since : 2020-08-30 14:41
 */
public interface IRpcService {

    //加
    int add(int a, int b);
    //减
    int sub(int a, int b);
    //乘
    int mult(int a, int b);
    //除
    int div(int a, int b);
}
