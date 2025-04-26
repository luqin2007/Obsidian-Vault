import com.example.mybank.ReplacedMethodBean;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public class TestReplacedMethod {

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        assertSame(context.getBean("test"), context.getBean(ReplacedMethodBean.class).getByBean("test"));
    }
}
