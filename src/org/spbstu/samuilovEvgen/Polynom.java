package org.spbstu.samuilovEvgen;

import java.util.*;

public class Polynom {
    private static final String LOOK_AROUND = "(?=%s)";
    private static final String VARIABLE = "x";

    private TreeMap<Integer, Integer> coefficients = new TreeMap<>(Comparator.reverseOrder());

    /**
     *
     parses a string in the format
     * cxd
     x -variable
     c-coefficient
     d-degree
     * @param statement
     */
    public Polynom(String statement) {
        String[] rawMembers = statement.split(String.format(LOOK_AROUND, "[+-]"));
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
     *Adding to the current polynomial a given
     * @param other
     * @return new polynom
     */

    public Polynom add(Polynom other) {
        Polynom result = new Polynom(this);
        other.coefficients.forEach((p, c) -> result.coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
        return result;
    }


    /**
     Subtraction from the current polynomial of the given
     * @param other
     * @return new polynom
     */
    public Polynom subtract(Polynom other) {
        Polynom result = new Polynom(this);
        other.coefficients.forEach((p, c) -> result.coefficients.merge(p, c, (newC, oldC) -> newC - oldC));
        return result;
    }

    /**
     *
     calculates the value for a given x
     * @param x
     * @return number
     */
    public int Calculate(int x) {
        int result = 0;
        for (Iterator<Map.Entry<Integer, Integer>> it = coefficients.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, Integer> entry = it.next();
            result += Math.pow(x, entry.getKey()) * entry.getValue();
        }
        return result;
    }
    /**
     *Multiplication of a polynomial on a polynomial
     * @param other
     * @return new  polynom
     */
    public Polynom multiply(Polynom other) {
        Polynom result = new Polynom("");
        coefficients.forEach((p1, c1) -> other.coefficients.forEach((p2, c2) ->
                result.coefficients.merge(p1 + p2, c1 * c2, (oldC, newC) -> oldC + newC)));
        return result;
    }

    /**
     *Divide the current polynomial by a given
     * @param other
     * @return new polynom
     */
    public Polynom divide(Polynom other) {
        Polynom dividend = new Polynom(this);
        Polynom divisor;
        Polynom multiplier;
        Polynom result = new Polynom("");

        while (dividend.coefficients.size() != 0
                && dividend.coefficients.firstKey() != 0
                && dividend.coefficients.firstKey() >= other.coefficients.firstKey()) {

            int coefficient = dividend.coefficients.get(dividend.coefficients.firstKey()) /
                    other.coefficients.get(other.coefficients.firstKey());
            int degree = dividend.coefficients.firstKey() - other.coefficients.firstKey();

            multiplier = new Polynom(coefficient + VARIABLE + degree);
            result = result.add(multiplier);
            divisor = other.multiply(multiplier);
            dividend = dividend.subtract(divisor);

            for (Iterator<Map.Entry<Integer, Integer>> it = dividend.coefficients.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Integer> entry = it.next();
                if (entry.getValue() == 0) {
                    it.remove();
                }
            }
        }

        return result;
    }
    private Polynom(Polynom other) {
        coefficients = new TreeMap<Integer, Integer>(Comparator.reverseOrder());
        other.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
    }

    public Polynom copy() {
        Polynom copy = new Polynom("");
        copy.add(this);
        return copy;
    }

    /**
     *getting the remainder from dividing one polynomial to another
     * @param other
     * @return new polynom polynom
     */
    public int modulo(Polynom other) {
        TreeMap<Integer, Integer> newCoefficients = new TreeMap(Comparator.reverseOrder());
        while (coefficients.size() != 0 && coefficients.firstKey() != 0) {
            if (other.coefficients.firstKey() <= coefficients.firstKey()) {
                Polynom divisor = other.copy();
                int coefficient = coefficients.get(coefficients.firstKey()) /
                        divisor.coefficients.get(divisor.coefficients.firstKey());
                int degree = coefficients.firstKey() - divisor.coefficients.firstKey();
                newCoefficients.merge(degree, coefficient, (oldC, newC) -> oldC + newC);
                divisor.multiply(new Polynom(coefficient + VARIABLE + degree));
                this.subtract(divisor);
                for (Iterator<Map.Entry<Integer, Integer>>it = coefficients.entrySet().iterator();
                     it.hasNext(); ){
                    Map.Entry<Integer, Integer> entry = it.next();
                    if (entry.getValue().equals(0)) {
                        it.remove();
                    }
                }
            } else break;
        }
        int result = coefficients.size() == 1 ?
                coefficients.get(coefficients.firstKey()) : 0;

        return result;
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
