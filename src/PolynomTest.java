import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolynomTest {

    Polynom polynom;

    @Test
    public void create1() {
        String statement = "7x4+3x3-6x2+x-8";
        polynom = new Polynom(statement);
        assertEquals("7x4+3x3-6x2+1x-8", polynom.toString());
        assertFalse("7x4+3x3-9x2+1x-8".equals(polynom.toString()));
    }

    @Test
    public void create2() {
        String statement = "4x+7x5";
        polynom = new Polynom(statement);
        assertEquals("7x5+4x", polynom.toString());
        assertFalse("4x+7x5".equals(polynom.toString()));
    }

    @Test
    public void multiply1() {
        String statement = "x";
        polynom = new Polynom(statement);
        assertEquals("1x2", polynom.multiply(new Polynom(statement)).toString());
    }

    @Test
    public void multiply2() {
        String statement = "x3+2x";
        polynom = new Polynom(statement);
        assertEquals("1x6+4x4+4x2", polynom.multiply(new Polynom(statement)).toString());
    }

    @Test
    public void add1() {
        String statement = "x";
        polynom = new Polynom(statement);
        assertEquals("2x", polynom.add(new Polynom(statement)).toString());
    }

    @Test
    public void add2() {
        String statement = "3x2+x";
        polynom = new Polynom(statement);
        assertEquals("6x2+2x", polynom.add(new Polynom(statement)).toString());
    }

    @Test
    public void subtract1() {
        String statement = "2x";
        polynom = new Polynom(statement);
        assertEquals("1x", polynom.subtract(new Polynom("1x")).toString());
    }

    @Test
    public void subtract2() {
        String statement = "2x2+2x-1";
        polynom = new Polynom(statement);
        assertEquals("1x2+1x-1", polynom.subtract(new Polynom("1x2+x")).toString());
    }

    @Test
    public void divide1() {
        String statement = "6x";
        polynom = new Polynom(statement);
        assertEquals("3", polynom.divide(new Polynom("2x")).toString());
    }

    @Test
    public void divide2() {
        String statement = "6x2";
        polynom = new Polynom(statement);
        assertEquals("3x2", polynom.divide(new Polynom("2")).toString());
    }

    @Test
    public void modulo1() {
        String statement1 = "-6x2-7x1";
        String statement2 = "-6x2-3x1";
        polynom = new Polynom(statement2);
        assertEquals(4, polynom.modulo(new Polynom(statement1)));
    }

    @Test
    public void modulo2() {
        String statement1 = "70";
        String statement2 = "15";
        Polynom p1 = new Polynom(statement1);
        Polynom p2 = new Polynom(statement2);
        assertEquals(70, p1.modulo(new Polynom(statement2)));

    }
}