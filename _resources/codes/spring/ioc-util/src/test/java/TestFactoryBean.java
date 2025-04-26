import com.example.mybank.ExampleFactoryBean;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFactoryBean {

    @Test
    public void testFactoryBean() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        assertEquals("hello world", context.getBean("example-factory"));
        assertEquals(new ExampleFactoryBean(), context.getBean("&example-factory"));
    }
}
