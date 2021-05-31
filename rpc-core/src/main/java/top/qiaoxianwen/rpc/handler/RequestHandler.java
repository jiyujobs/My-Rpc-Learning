package top.qiaoxianwen.rpc.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.qiaoxianwen.rpc.entity.RpcRequset;
import top.qiaoxianwen.rpc.entity.RpcResponse;
import top.qiaoxianwen.rpc.enumeration.ResponseCode;
import top.qiaoxianwen.rpc.provider.ServiceProvider;
import top.qiaoxianwen.rpc.provider.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 进行过程通用的处理器
 *
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequset rpcRequset){
        Object service = serviceProvider.getServiceProvider(rpcRequset.getInterfaceName());
        return invokeTargetMethod(rpcRequset, service);
    }

    private Object invokeTargetMethod(RpcRequset rpcRequset, Object service){
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequset.getMethodName(), rpcRequset.getParamTypes());
            result = method.invoke(service, rpcRequset.getParameters());
            logger.info("服务：{}成功调用方法:{}", rpcRequset.getInterfaceName(), rpcRequset.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequset.getRequsetId());
        }
        return result;
    }
}
