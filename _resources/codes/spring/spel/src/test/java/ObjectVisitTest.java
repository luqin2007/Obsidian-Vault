import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectVisitTest {

    private static final ExpressionParser parser = new SpelExpressionParser();

    @Test
    public void testMemberVisit() {
        // String 的 bytes 属性，即 getBytes() 方法
        Expression exp = parser.parseExpression("'Hello World'.bytes");
        assertArrayEquals("Hello World".getBytes(), exp.getValue(byte[].class));
        // byte[] 的 length 公共字段
        exp = parser.parseExpression("'Hello World'.bytes.length");
        assertEquals("Hello World".getBytes().length, exp.getValue());
        // String 的 length() 方法
        exp = parser.parseExpression("'Hello World'.length()");
        assertEquals("Hello World".length(), exp.getValue(int.class));
        // String 的 length() 方法
        exp = parser.parseExpression("'Hello World'.length()");
        assertEquals("Hello World".length(), exp.getValue(int.class));
        // String 的 charAt() 方法
        exp = parser.parseExpression("'Hello World'.charAt(3)");
        assertEquals('l', exp.getValue(char.class));
    }

    @Test
    public void testCreateCollection() {
        Expression exp = parser.parseExpression("{1,2,3,4,5}");
        System.out.println(exp.getValueType());
        assertEquals(List.of(1,2,3,4,5), exp.getValue(List.class));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, exp.getValue(int[].class));
    }

    @Test
    public void testByIndex() {
        int[] array = new int[]{0, 1, 2, 3, 4, 5};
        Expression exp = parser.parseExpression("[3]");
        assertEquals(3, exp.getValue(array));

        List<String> list = List.of("a", "b", "c", "d", "e");
        exp = parser.parseExpression("[3]");
        assertEquals("d", exp.getValue(list));

        Map<String, String> map = Map.of("a", "A", "b", "B", "c", "C", "d", "D");
        exp = parser.parseExpression("['d']");
        assertEquals("D", exp.getValue(map));

        Properties properties = new Properties();
        properties.put("a", "A");
        properties.put("b", "B");
        properties.put("c", "C");
        exp = parser.parseExpression("['c']");
        assertEquals("C", exp.getValue(properties));
    }
}
