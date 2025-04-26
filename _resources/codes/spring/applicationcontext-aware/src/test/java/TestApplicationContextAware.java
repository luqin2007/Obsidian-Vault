import com.example.mybank.ConsumerRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public class TestApplicationContextAware {

    @Test
    public void test() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        assertSame(context, context.getBean(ConsumerRequestService.class).getApplicationContext());
    }
}
