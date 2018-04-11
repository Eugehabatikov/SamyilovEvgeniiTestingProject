package org.spbstu.samuilovEvgen;

import java.math.BigInteger;
import java.util.*;

public class Polynom {
    private static final String VARIABLE = "x";
    private TreeMap<Integer, Integer> coefficients = new TreeMap<>(Comparator.reverseOrder());

    /**
     * parses a string in the format
     * cxd
     * x -variable
     * c-coefficient
     * d-degree
     * example: 2x3+2x2
     * statement must be writed without spaces
     *
     * @param statement string which will be fragmentation on parts of polynoim
     */
    public Polynom(String statement) {
        String[] rawMembers = statement.split("(?=[+-])");
        for (String member : rawMembers) {
            if (member.length() == 0)
                continue;

            boolean sign = member.charAt(0) != '-';
            String unsigned = member.replace("+", "").replace("-", "");

            String[] parsedMember = unsigned.split(VARIABLE);
            int position = unsigned.indexOf(VARIABLE);

            int coefficient = 1;
            if (position > 0 || position == -1)
                coefficient = Integer.parseInt(parsedMember[0]);
            coefficient = sign ? coefficient : -coefficient;

            int power = 0;
            if (position == -1)
                power = 0;
            else if (position == unsigned.length() - 1)
                power = 1;
            else if (position != unsigned.length() - 1)
                power = Integer.parseInt(parsedMember[1]);

            coefficients.merge(power, coefficient, (newC, oldC) -> newC + oldC);
        }
    }

    /**
     * Adding to the current polynomial a given
     *
     * @param other the polynomial added to the original
     * @return new polynom
     */

    public Polynom add(Polynom other) {
        Polynom result = new Polynom(this);
        other.coefficients.forEach((p, c) -> result.coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
        result.coefficients.entrySet().removeIf(entry -> !entry.getKey().equals(0) && entry.getValue().equals(0));
        return result;
    }


    /**
     * Subtraction from the current polynomial of the given
     *
     * @param other polynomial, subtracted from the original
     * @return new polynom
     */
    public Polynom subtract(Polynom other) {
        Polynom result = new Polynom(this);
        other.coefficients.forEach((p, c) -> result.coefficients.merge(p, -c, (newC, oldC) -> newC + oldC));
        result.coefficients.entrySet().removeIf(entry -> !entry.getKey().equals(0) && entry.getValue().equals(0));
        return result;
    }

    /**
     * calculates the value for a given x
     *
     * @param x calculated in the method
     * @return number
     */
    public BigInteger calculate(BigInteger x) {
        BigInteger result = new BigInteger("0");
        if (x.equals(new BigInteger("0"))) {
            result = result.add(new BigInteger(coefficients.get(0).toString()));
            return result;
        }
        for (Map.Entry<Integer, Integer> entry : coefficients.entrySet()) {
            result = result.add(x.pow(entry.getKey()).multiply(new BigInteger(entry.getValue().toString())));
        }
        return result;
    }

    /**
     * Multiplication of a polynomial on a polynomial
     *
     * @param other polynomial, multiplied by the original
     * @return new  polynom
     */
    public Polynom multiply(Polynom other) {
        Polynom result = new Polynom("");
        coefficients.forEach((p1, c1) -> other.coefficients.forEach((p2, c2) ->
                result.coefficients.merge(p1 + p2, c1 * c2, (oldC, newC) -> oldC + newC)));
        result.coefficients.entrySet().removeIf(entry -> !entry.getKey().equals(0) && entry.getValue().equals(0));
        return result;
    }

    /**
     * Divide the current polynomial by a given
     *
     * @param other polynomial to which the original
     * @return new polynom
     */
    public Polynom divide(Polynom other) {
        Polynom dividend = new Polynom(this);
        Polynom divisor;
        Polynom multiplier;
        Polynom result = new Polynom("");
        if (other.equals(new Polynom("0"))) {
            throw new IllegalArgumentException();
        }
        if (this.equals(new Polynom("0"))) {
            return new Polynom("0");
        }
        while (dividend.coefficients.size() != 0
                && dividend.coefficients.firstKey() != 0
                && dividend.coefficients.firstKey() >= other.coefficients.firstKey()) {

            int coefficient = dividend.coefficients.firstEntry().getValue() /
                    other.coefficients.firstEntry().getValue();
            int degree = dividend.coefficients.firstKey() - other.coefficients.firstKey();

            multiplier = new Polynom(coefficient + VARIABLE + degree);
            result = result.add(multiplier);
            divisor = other.multiply(multiplier);
            dividend = dividend.subtract(divisor);
            dividend.coefficients.entrySet().removeIf(entry -> entry.getValue() == 0);
        }
        return result;
    }

    private Polynom(Polynom other) {
        coefficients = new TreeMap<Integer, Integer>(Comparator.reverseOrder());
        other.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
    }

    /**
     * getting the remainder from dividing one polynomial to another
     *
     * @param other polynomial to which the original
     * @return new polynom polynom
     */
    public Polynom modulo(Polynom other) {
        return subtract(other.multiply(divide(other)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Polynom)) return false;
        Polynom that = (Polynom) o;
        return Objects.equals(coefficients, that.coefficients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficients);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        coefficients.forEach((p, c) -> {
            if (c != 0) {
                if (c > 0 && builder.length() > 0)
                    builder.append("+");
                builder.append(c);

                if (p != 0) {
                    builder.append(VARIABLE);
                    if (p != 1)
                        builder.append(p);
                }
            }
        });

        return builder.toString();
    }
}
