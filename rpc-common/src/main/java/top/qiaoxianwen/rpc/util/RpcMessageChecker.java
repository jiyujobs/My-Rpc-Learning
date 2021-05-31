package top.qiaoxianwen.rpc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.qiaoxianwen.rpc.entity.RpcRequset;
import top.qiaoxianwen.rpc.entity.RpcResponse;
import top.qiaoxianwen.rpc.enumeration.ResponseCode;
import top.qiaoxianwen.rpc.enumeration.RpcError;
import top.qiaoxianwen.rpc.exception.RpcException;

/**
 * 检查响应与请求
 */
public class RpcMessageChecker {

    public static final String INTERFACE_NAME = "interfaceName";
    public static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    private RpcMessageChecker(){}

    public static void check(RpcRequset rpcRequset, RpcResponse rpcResponse){
        if(rpcResponse == null){
            logger.error("调用服务失败,serviceName:{}", rpcRequset.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequset.getInterfaceName());
        }

        if(!rpcRequset.getRequsetId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequset.getInterfaceName());
        }

        if(rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())){
            logger.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequset.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequset.getInterfaceName());
        }
    }
}
