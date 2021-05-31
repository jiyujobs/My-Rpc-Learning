package top.qiaoxianwen.test;

import top.qiaoxianwen.rpc.annotation.ServiceScan;
import top.qiaoxianwen.rpc.serializer.CommonSerializer;
import top.qiaoxianwen.rpc.transport.RpcServer;
import top.qiaoxianwen.rpc.transport.socket.server.SocketServer;

/**
 * 测试用服务提供方(服务端)
 *
 */
@ServiceScan
public class SocketTestServer {

    public static void main(String[] args){
        RpcServer server = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        server.start();
    }


}
