import com.example.mybank.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OperationTest {

    private static ExpressionParser parser = new SpelExpressionParser();

    @Test
    public void testRelationalOperations() {
        Expression exp = parser.parseExpression("'abc' < 'ad'");
        assertTrue(exp.getValue(boolean.class));
        // 任何值都大于 null 值
        exp = parser.parseExpression("100 > null");
        assertTrue(exp.getValue(boolean.class));
    }

    @Test
    public void testInstanceofOperation() {
        Expression exp = parser.parseExpression("'abc' instanceof T(int)");
        assertFalse(exp.getValue(boolean.class));

        exp = parser.parseExpression("'abc' instanceof T(String)");
        assertTrue(exp.getValue(boolean.class));
    }

    @Test
    public void testMatch() {
        Expression exp = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'");
        assertTrue(exp.getValue(boolean.class));

        exp = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'");
        assertFalse(exp.getValue(boolean.class));
    }

    @Test
    public void testClass() {
        // T 运算结果可以直接作为 Class 使用
        Expression exp = parser.parseExpression("T(String)");
        assertEquals(String.class, exp.getValue());
        // 也可以用于访问静态成员
        exp = parser.parseExpression("T(Boolean).TRUE");
        assertEquals(Boolean.TRUE, exp.getValue());
    }

    @Test
    public void testContextFind() throws NoSuchMethodException, IllegalAccessException {
        // 访问变量
        Calendar calendar = Calendar.getInstance();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("calendar", calendar);
        Expression exp = parser.parseExpression("#calendar.time");
        assertEquals(calendar.getTime(), exp.getValue(context));
        // 访问方法
        context = new StandardEvaluationContext();
        // String SpringUtil#reverseString(String)
        MethodHandle reverse = MethodHandles.lookup().findStatic(SpringUtil.class,
                "reverseString", MethodType.methodType(String.class, String.class));
        context.registerFunction("reverse", reverse);
        exp = parser.parseExpression("#reverse('abc')");
        assertEquals("cba", exp.getValue(context));
    }

    @Test
    public void testRoot() {
        Calendar calendar = Calendar.getInstance();
        EvaluationContext context = new StandardEvaluationContext(calendar);
        Expression exp = parser.parseExpression("#root.time");
        assertEquals(calendar.getTime(), exp.getValue(context));
    }

    @Test
    public void testThis() {
        // create an array of integers
        List<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17));

        // create parser and set variable 'primes' as the array of integers
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("primes", primes);

        // all prime numbers > 10 from the list (using selection ?{...})
        // evaluates to [11, 13, 17]
        List<Integer> primesGreaterThanTen = (List<Integer>) parser
                .parseExpression("#primes.?[#this>10]")
                .getValue(context);
    }
}
