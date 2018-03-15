import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolynomTest {
    Polynom polynom;

    @Test
    public void create(){
        String statement = "7x4+3x3-6x2+x-8";
        polynom = new Polynom(statement);
        assertEquals("+7x4+3x3-6x2+1x1-8x0",polynom.toString());
        assertFalse("+7x4+3x3-9x2+1x1-8x0" == polynom.toString());
    }


}
