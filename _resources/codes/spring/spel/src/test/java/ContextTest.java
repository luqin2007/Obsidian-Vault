import com.example.mybank.Simple;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ContextTest {

    public static final ExpressionParser parser = new SpelExpressionParser();

    @Test
    public void testRootObject() {
        // 设置上下文
        Calendar calendar = Calendar.getInstance();
        EvaluationContext context = new StandardEvaluationContext(calendar);

        Expression exp = parser.parseExpression("time");
        assertEquals(calendar.getTime(), exp.getValue(context));
    }

    @Test
    public void testConvert() {
        Simple simple = new Simple();
        simple.booleanList.add(true);
        StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);
        Expression exp = parser.parseExpression("booleanList[0]");
        exp.setValue(simpleContext, false);
        assertFalse(simple.booleanList.get(0));
    }
}
