package richuff.service.Impl;

import com.richuff.anno.Component;
import richuff.service.UserService;

@Component("userService")
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("test");
    }
}
