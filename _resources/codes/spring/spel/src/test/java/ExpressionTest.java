import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExpressionTest {

    @Test
    public void test() {
        ExpressionParser parser = new SpelExpressionParser();
        // 这里在 @Value 中为 @Value("#{'Hello World'}")
        Expression exp = parser.parseExpression("'Hello World'");
        assertEquals("Hello World", exp.getValue(String.class));
        // null
        exp = parser.parseExpression("null");
        assertNull(exp.getValue());
    }
}
