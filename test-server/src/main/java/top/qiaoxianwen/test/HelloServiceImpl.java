package top.qiaoxianwen.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.qiaoxianwen.rpc.annotation.Service;
import top.qiaoxianwen.rpc.api.HelloObject;
import top.qiaoxianwen.rpc.api.HelloService;

/**
 * @author xianwen
 */
@Service
public class HelloServiceImpl implements HelloService {

    protected static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object){
        logger.info("接收到消息:{}", object.getMessage());
        return "这是Impl1方法";
    }
}