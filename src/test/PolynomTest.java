package test;

import org.junit.jupiter.api.Test;
import org.spbstu.samuilovEvgen.Polynom;

import static org.junit.jupiter.api.Assertions.*;

import org.spbstu.samuilovEvgen.Polynom;

import java.math.BigInteger;

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
        polynom = new Polynom("x+1");
        assertEquals(new Polynom("x2+x"), polynom.multiply(new Polynom("x")));
    }

    @Test
    public void multiply2() {
        polynom = new Polynom("x3+2x");
        assertEquals(new Polynom("1x6+4x4+4x2"), polynom.multiply(new Polynom("x3+2x")));
    }

    @Test
    public void add1() {
        polynom = new Polynom("x");
        assertEquals(new Polynom("3x"), polynom.add(new Polynom("2x")));
    }

    @Test
    public void add2() {
        polynom = new Polynom("3x2+x");
        assertEquals(new Polynom("4x3+8x2+3x"), polynom.add(new Polynom("4x3+5x2+2x")));
    }

    @Test
    public void subtract1() {
        polynom = new Polynom("x2+1");
        assertEquals(new Polynom("x2+2x+1"), polynom.subtract(new Polynom("-2x")));
    }

    @Test
    public void subtract2() {
        polynom = new Polynom("2x2+2x-1");
        assertEquals(new Polynom("1x2+1x-1"), polynom.subtract(new Polynom("1x2+x")));
    }

    @Test
    public void divide1() {
        polynom = new Polynom("6x");
        assertEquals(new Polynom("3"), polynom.divide(new Polynom("2x")));
    }

    @Test
    public void divide() {
        polynom = new Polynom("x2+2x+1");
        assertEquals(new Polynom("1x+1"), polynom.divide(new Polynom("x+1")));
    }

    @Test
    public void divide2() {
        polynom = new Polynom("x3+3x2+4x+1");
        assertEquals(new Polynom("1x2+2x+2"), polynom.divide(new Polynom("x+1")));
    }

    @Test
    public void modulo1() {
        polynom = new Polynom("x2+2x+2");
        assertEquals("1", polynom.modulo(new Polynom("x+1")).toString());
    }

    @Test
    public void modulo2() {
        polynom = new Polynom("x3+3x2+4x+2");
        assertEquals("1x+1", polynom.modulo(new Polynom("x2+2x+1")).toString());
    }

    @Test
    public void calculate() {
        polynom = new Polynom("x2");
        assertEquals(new BigInteger("10000"), polynom.calculate(new BigInteger("100")));
    }

    @Test
    public void calculate2() {
        polynom = new Polynom("x100");
        assertEquals(new BigInteger("1000000000000000000000000000000000000000000000" +
                "000000000000000000000000000000000000000000000000000" +
                "0000"), polynom.calculate(new BigInteger("10")));
    }

}