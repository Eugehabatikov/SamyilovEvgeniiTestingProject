import java.util.*;

public class Polynom {
    private static final String LOOK_AROUND = "(?=%s)";
    private static final String CHECKER = "([+-])([0-9])?%s";
    private static final String VARIABLE = "x";

    private TreeMap<Integer, Integer> coefficients = new TreeMap<>(Comparator.reverseOrder());

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
     * Добавление к текущему полиному заданного
     * @param polynomial
     * @return
     */

    public Polynom add(Polynom polynomial) {
        polynomial.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC + oldC));
        return this;
    }

    /**
     *Вычитание из текущего полинома заданного
     * @param polynomial
     * @return
     */
    public Polynom subtract(Polynom polynomial) {
        polynomial.coefficients.forEach((p, c) -> coefficients.merge(p, c, (newC, oldC) -> newC - oldC));
        return this;
    }

    /**
     * Умножение полинома на полином
     * @param polynomial
     * @return
     */
    public Polynom multiply(Polynom polynomial) {
        TreeMap<Integer, Integer> newCoefficients = new TreeMap<>(Comparator.reverseOrder());
        coefficients.forEach((p1, c1) -> polynomial.coefficients.forEach((p2, c2) -> newCoefficients
                .merge(p1 + p2, c1 * c2, (oldC, newC) -> oldC + newC)));
        coefficients= newCoefficients;
        return this;
    }

    /**
     * Деление текущего полинома на заданный
     * @param other
     * @return
     */
    public Polynom divide(Polynom other) {
        TreeMap<Integer, Integer> newCoefficients = new TreeMap(Comparator.reverseOrder());
        while (coefficients.size() != 0 && coefficients.firstKey() != 0) { // выполняю пока не уйдет Х
            if (other.coefficients.firstKey() <= coefficients.firstKey()) { // выполняю если старшая степень делителя <= моей
                Polynom divisor = other.copy();
                int coefficient = coefficients.get(coefficients.firstKey()) /
                        divisor.coefficients.get(divisor.coefficients.firstKey());
                int degree = coefficients.firstKey() - divisor.coefficients.firstKey();
                newCoefficients.merge(degree, coefficient, (oldC, newC) -> oldC + newC);
                divisor.multiply(new Polynom(coefficient + VARIABLE + degree));
                this.subtract(divisor);
//ниже написан итератор, который удаляет все ячейки с нулевыми занчениями(важно!!)
                for (Iterator<Map.Entry<Integer, Integer>> it = coefficients.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    if (entry.getValue().equals(0)) {
                        it.remove();
                    }
                }
//^ вот это итератор
            } else break;
        }
        coefficients = newCoefficients;
        return this;
    }

    public Polynom copy() {
        Polynom copy = new Polynom("");
        copy.add(this);
        return copy;
    }

    /**
     * получение остатка от деления одного полинома на другой
     *
     * @param other
     * @return
     */
    public int modulo(Polynom other) {
        TreeMap<Integer, Integer> newCoefficients = new TreeMap(Comparator.reverseOrder());
        while (coefficients.size() != 0 && coefficients.firstKey() != 0) { // выполняю пока не уйдет Х
            if (other.coefficients.firstKey() <= coefficients.firstKey()) { // выполняю если старшая степень делителя <= моей
                Polynom divisor = other.copy();
                int coefficient = coefficients.get(coefficients.firstKey()) /
                        divisor.coefficients.get(divisor.coefficients.firstKey());
                int degree = coefficients.firstKey() - divisor.coefficients.firstKey();
                newCoefficients.merge(degree, coefficient, (oldC, newC) -> oldC + newC);
                divisor.multiply(new Polynom(coefficient + VARIABLE + degree));
                this.subtract(divisor);
//ниже написан итератор, который удаляет все ячейки с нулевыми занчениями(важно!!)
                for (Iterator<Map.Entry<Integer, Integer>>it = coefficients.entrySet().iterator();
                     it.hasNext(); ){
                    Map.Entry<Integer, Integer> entry = it.next();
                    if (entry.getValue().equals(0)) {
                        it.remove();
                    }
                }
//^ вот это итератор
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
