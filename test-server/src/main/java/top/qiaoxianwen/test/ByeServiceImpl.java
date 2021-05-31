package top.qiaoxianwen.test;

import top.qiaoxianwen.rpc.annotation.Service;
import top.qiaoxianwen.rpc.api.ByeService;

@Service
public class ByeServiceImpl implements ByeService {


    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
