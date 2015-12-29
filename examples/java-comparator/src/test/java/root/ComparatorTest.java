package root;

import comparator.Comparator;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ComparatorTest {

    private static final Map<String, Pattern> ALIASES = new HashMap<String, Pattern>() {
        {
            put("digit", Pattern.compile("\\d+"));
        }
    };

    private static Comparator createComparator() {
        return Comparator.java().strict(ALIASES);
    }

    @Test
    public void matches() throws Exception {
        String expected = "{\"count\":\"p(digit)\"}";
        String actual = "{\"count\":\"120\"}";

        createComparator().compare(expected, actual);
    }
}
