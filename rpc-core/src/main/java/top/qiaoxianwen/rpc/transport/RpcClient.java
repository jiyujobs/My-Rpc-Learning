package top.qiaoxianwen.rpc.transport;

import top.qiaoxianwen.rpc.entity.RpcRequset;
import top.qiaoxianwen.rpc.serializer.CommonSerializer;

/**
 * 客户端类通用接口
 *
 */
public interface RpcClient {

    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequset rpcRequset);

}
