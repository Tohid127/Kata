import java.util.List;
import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        String expression = scanner.nextLine();
        System.out.println("Результат вычисления: " + calc(expression));
    }

    public static String calc(String input) throws Exception {
        int arabicNumber1 = 0;
        int arabicNumber2 = 0;
        int romanNumber1 = 0;
        int romanNumber2 = 0;
        boolean isRoman = false;

        String[] operands = input.split("[+\\-*/]");
        try {
            operands[0] = operands[0].trim();
            operands[1] = operands[1].trim();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка: строка не является математической операцией.");
            System.exit(1);
        }

        for (int i = 0; i < operands.length; i++) {
            if (operands[i] == "") {
                throw new DigitRangeException("Ошибка: числа не входят в диапазон.");
            }
        }
        if (operands.length != 2)
            throw new Exception("Ошибка! Неподдерживаемая математическая операция или операнда не два.");
        String operation = operationDefinition(input);

        try {
            arabicNumber1 = Integer.parseInt(operands[0]);
            arabicNumber2 = Integer.parseInt(operands[1]);
            if (arabicNumber1 < 1 || arabicNumber1 > 10 || arabicNumber2 < 1 || arabicNumber2 > 10) {
                throw new DigitRangeException("Ошибка: числа не входят в диапазон.");
            }
        } catch (DigitRangeException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            try {
                romanNumber1 = romanToArabic(operands[0]);
                romanNumber2 = romanToArabic(operands[1]);

                if (romanNumber1 < 1 || romanNumber1 > 10 || romanNumber2 < 1 || romanNumber2 > 10) {
                    throw new DigitRangeException("Ошибка: числа не входят в диапазон.");
                }
                if (operation == "/" && romanNumber2 > romanNumber1) {
                    throw new RomanResultException("Ошибка: в римской системе нет отрицательных чисел.");
                }
                if (operation == "-" && romanNumber1 <= romanNumber2) {
                    throw new RomanResultException("Ошибка: в римской системе нет отрицательных чисел.");
                }
            } catch (DigitRangeException s) {
                System.out.println(s.getMessage());
                System.exit(1);
            } catch (RomanResultException s) {
                System.out.println(s.getMessage());
                System.exit(1);
            } catch (IllegalArgumentException s) {
                System.out.println(s.getMessage());
                System.exit(1);
            }
            isRoman = true;
        }


        if (isRoman) {
            return arabicToRoman(calculation(romanNumber1, romanNumber2, operation));
        }
        return calculation(arabicNumber1, arabicNumber2, operation) + "";
    }


    static String operationDefinition(String input) {
        if (input.contains("+")) return "+";
        else if (input.contains("-")) return "-";
        else if (input.contains("*")) return "*";
        else return "/";
    }

    static int calculation(int num1, int num2, String oper) {
        if (oper.equals("+")) return num1 + num2;
        else if (oper.equals("-")) return num1 - num2;
        else if (oper.equals("*")) return num1 * num2;
        else return num1 / num2;
    }

    static int romanToArabic(String input) {
        String romanNumeral = input.toUpperCase();
        int result = 0;

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;

        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }

        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException("Ошибка: некорректный ввод операндов.");
        }

        return result;
    }

    static String arabicToRoman(int number) {
        if ((number <= 0) || (number > 4000)) {
            throw new IllegalArgumentException(number + " is not in range (0,4000]");
        }

        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();

        int i = 0;
        StringBuilder sb = new StringBuilder();

        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }

        return sb.toString();
    }
}

