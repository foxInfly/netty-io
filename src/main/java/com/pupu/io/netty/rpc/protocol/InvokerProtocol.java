package com.pupu.io.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : lipu
 * @since : 2020-08-30 22:35
 */
@Data
public class InvokerProtocol implements Serializable {
    private static final long serialVersionUID = -505469366440495528L;

    private String className;//服务名
    private String methodName;//方法名，具体的逻辑
    private Class<?>[] parames;//形参列表
    private Object[] values;//实参列表



}
