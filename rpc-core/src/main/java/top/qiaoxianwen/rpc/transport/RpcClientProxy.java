package top.qiaoxianwen.rpc.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.qiaoxianwen.rpc.entity.RpcRequset;
import top.qiaoxianwen.rpc.entity.RpcResponse;
import top.qiaoxianwen.rpc.transport.netty.client.NettyClient;
import top.qiaoxianwen.rpc.transport.socket.client.SocketClient;
import top.qiaoxianwen.rpc.util.RpcMessageChecker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * RPC客户端动态代理
 *
 */
public class RpcClientProxy implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);

    private final RpcClient client;

    public RpcClientProxy(RpcClient client){ this.client = client; }

    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法:{}#{}", method.getDeclaringClass().getName(), method.getName());
        RpcRequset rpcRequset = new RpcRequset(UUID.randomUUID().toString(), method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes(), false);
        RpcResponse rpcResponse = null;
        if(client instanceof NettyClient){
            try {
                CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>)  client.sendRequest(rpcRequset);
                rpcResponse = completableFuture.get();
            } catch (Exception e){
                logger.error("方法调用请求发送失败", e);
                return null;
            }
        }
        if(client instanceof SocketClient){
            rpcResponse = (RpcResponse) client.sendRequest(rpcRequset);
        }
        RpcMessageChecker.check(rpcRequset, rpcResponse);
        return rpcResponse.getData();
    }
}
