package top.qiaoxianwen.rpc.transport.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.qiaoxianwen.rpc.entity.RpcRequset;
import top.qiaoxianwen.rpc.entity.RpcResponse;
import top.qiaoxianwen.rpc.enumeration.ResponseCode;
import top.qiaoxianwen.rpc.enumeration.RpcError;
import top.qiaoxianwen.rpc.exception.RpcException;
import top.qiaoxianwen.rpc.loadbalancer.LoadBalancer;
import top.qiaoxianwen.rpc.loadbalancer.RandomLoadBalancer;
import top.qiaoxianwen.rpc.registry.NacosServiceDiscovery;
import top.qiaoxianwen.rpc.registry.ServiceDiscovery;
import top.qiaoxianwen.rpc.serializer.CommonSerializer;
import top.qiaoxianwen.rpc.transport.RpcClient;
import top.qiaoxianwen.rpc.transport.socket.util.ObjectReader;
import top.qiaoxianwen.rpc.transport.socket.util.ObjectWriter;
import top.qiaoxianwen.rpc.util.RpcMessageChecker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket方式远程方法调用的消费者(客户端)
 *
 *
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    public SocketClient() { this(DEFAULT_SERIALIZER, new RandomLoadBalancer());}
    public SocketClient(LoadBalancer loadBalancer) { this(DEFAULT_SERIALIZER, loadBalancer);}
    public SocketClient(Integer serializer) { this(serializer, new RandomLoadBalancer());}

    public SocketClient(Integer serializer, LoadBalancer loadBalancer){
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
    }

    @Override
    public Object sendRequest(RpcRequset rpcRequset) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequset.getInterfaceName());
        try (Socket socket = new Socket()){
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequset, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if(rpcResponse == null){
                logger.error("服务调用失败，service:{}", rpcRequset.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, "service:" + rpcRequset.getInterfaceName());
            }
            if(rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("服务调用失败,service:{},response:{}", rpcRequset.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequset.getInterfaceName());
            }
            RpcMessageChecker.check(rpcRequset, rpcResponse);
            return rpcResponse;
        } catch (IOException e){
            logger.error("调用时有错误发生：", e);
            throw new RpcException("调用服务失败:", e);
        }
    }

}
