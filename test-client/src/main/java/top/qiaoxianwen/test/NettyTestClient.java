package top.qiaoxianwen.test;

import top.qiaoxianwen.rpc.api.ByeService;
import top.qiaoxianwen.rpc.api.HelloObject;
import top.qiaoxianwen.rpc.api.HelloService;
import top.qiaoxianwen.rpc.serializer.CommonSerializer;
import top.qiaoxianwen.rpc.transport.RpcClient;
import top.qiaoxianwen.rpc.transport.RpcClientProxy;
import top.qiaoxianwen.rpc.transport.netty.client.NettyClient;

/**
 * 测试用Netty消费者
 *
 */
public class NettyTestClient {

    public static void main(String[] args){
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }

}
