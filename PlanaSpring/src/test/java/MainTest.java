import com.richuff.PlanaSpringContext;
import com.richuff.appconfig.AppConfig;
import com.richuff.service.UserService;

public class MainTest {
    public static void main(String[] args) {
        PlanaSpringContext applicationContext = new PlanaSpringContext(AppConfig.class);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
