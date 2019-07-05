import com.xzp.model.User;
import com.xzp.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = {"classpath:Spring-Mybatis.xml","classpath:Spring-MVC.xml"})
public class TestTransaction extends AbstractJUnit4SpringContextTests {
    @Autowired
    private UserService userService;

    @Test
    public void testSave(){
        User user = new User();
        user.setNickName("封剑主-叹希奇");
        user.setEmail("123456@qq.com");
        userService.regist(user);
    }
}