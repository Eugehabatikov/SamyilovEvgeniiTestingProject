import java.util.*;

public class Polynom {
    private static final String LOOK_AROUND = "(?=%s)";
    private static final String CHECKER = "([+-])([0-9])?%s";
    private static final String VARIABLE = "x";

    private Map<Integer, Integer> coefficients = new TreeMap<>(Comparator.reverseOrder());

    public Polynom(String statement) {
        checkString();
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

    private void checkString() {
//TODO
    }

    public Polynom add(Polynom polynomial) {
        polynomial.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
        return this;
    }

    public Polynom subtract(Polynom polynomial) {
        polynomial.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC - oldC));
        return this;
    }

    public Polynom multiply(Polynom polynomial) {
        Map<Integer, Integer> newCoefficients = new TreeMap<>(Comparator.reverseOrder());
        coefficients.forEach((p1, c1) -> polynomial.coefficients.forEach((p2, c2) -> newCoefficients
                .merge(p1 * p2, c1 * c2, (oldC, newC) -> oldC + newC)));
        coefficients = newCoefficients;
        return this;
    }

    public Polynom divide(Polynom polynomial) {
        polynomial.coefficients.forEach((p1, c1) -> coefficients.computeIfPresent(p1, (oldC, newC) -> oldC / newC));
        return this;
    }

    public Polynom modulo(Polynom polynomial) {
        polynomial.coefficients.forEach((p1, c1) -> coefficients.computeIfPresent(p1, (oldC, newC) -> oldC % newC));
        return this;
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
            if (c > 0) builder.append("+" + c).append(VARIABLE).append(p);
            else   builder.append(c).append(VARIABLE).append(p);
                });

        return builder.toString();
    }
}
