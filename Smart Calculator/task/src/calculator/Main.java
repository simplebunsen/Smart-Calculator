package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static Map<String, Long> variableMap = new HashMap<>();
    public static String regexOperatorDigit = "[-+ ]*([0-9]|[a-zA-Z])+";
    public static Pattern pattern = Pattern.compile(regexOperatorDigit);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            NotationConverter nc = new NotationConverter(scanner.nextLine());
            String[] postfixExpression = nc.convert().split(" ");
            Deque<Long> calcStack = new ArrayDeque<>();

            for (String element : postfixExpression) {
                if (element.matches("-?[0-9]+"))
                    calcStack.push(Long.parseLong(element));
                else if (element.matches("-?[a-zA-Z]"))
                    calcStack.push(getVariableNumValue(element));
                else if (element.matches("[-+*/^]")) {
                    long num1 = calcStack.pop();
                    long num2 = calcStack.pop();
                    calcStack.push(calculate(element, num1, num2));
                }
            }
            System.out.println(calcStack.pop());
        }

    }

    private static long calculate(String operator, long num1, long num2) {
        switch (operator) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            case "^":
                return (long) Math.pow(num1, num2);
            default:
                System.out.println("something wrong in calculate");
                return Long.MIN_VALUE;

        }
    }

    public static void temp () {
        Scanner scanner = new Scanner(System.in);

        while(true){
            String inputString = scanner.nextLine();

            if (inputString.isEmpty()) continue;

            /* COMMANDS */
            if (inputString.matches("/.*")) {
                if (inputString.equals("/exit")) {
                    break;
                } else if (inputString.equals("/help")) {
                    System.out.println("The program calculates a term of numbers. + and - operators supported");
                    continue;
                } else {
                    System.out.println("Unknown command");
                    continue;
                }
            }

            /* VARIABLE ASSIGNMENTS */
            if (inputString.matches(".* *(= *.*)")){
                saveVariable(inputString);
                continue;
            }

//            /* VARIABLE PRINTING TODO: might be useless due to normal calculator logic getting the var automatically*/
//            if (inputString.matches("[a-zA-Z]*") && variableMap.containsKey(inputString)){
//                System.out.println("varprtint");
//                System.out.println(variableMap.get(inputString));
//                continue;
//            }

            /* CATCH ALL FOR INVALID INPUT */
            if(!inputString.matches("[ +-]*(([0-9]+|[a-zA-Z]+)[ ]*[+-][ +-]*)*([0-9]+|[a-zA-Z]+)")){
                System.out.println("Invalid expression: Catch All");
                continue;
            }


            Matcher m = pattern.matcher(inputString);
            List<String> list = getElementsArrayList(m);
            if (list.contains("?")){
                System.out.println("Invalid expression: ? found in list");
                continue;
            }

            //System.out.println("list of elements:" + list);

            if(list.get(0).matches("[+-]")){
                list.add(0, "0");
            }

            long result;
            if(list.get(0).matches("[+-]*[a-zA-Z]+")){
                result = getVariableNumValue(list.get(0));
            } else if(list.get(0).matches("[+-]*[0-9]+")){
                result = Integer.parseInt(list.get(0));
            } else {
                System.out.printf("Elements list returned unexpected value %s, aborting\n", list.get(0));
                continue;
            }

            for (int i = 1; i < list.size(); i = i+2) {
                String operator = list.get(i);
                long n;
                if(list.get(i+1).matches("[+-]*[a-zA-Z]+")){
                    n = getVariableNumValue(list.get(i+1));
                } else if(list.get(i+1).matches("[+-]*[0-9]+")){
                    n = Integer.parseInt(list.get(i+1));
                } else {
                    System.out.printf("Elements list returned unexpected value %s, aborting\n", list.get(i+1));
                    continue;
                }
                switch (operator) {
                    case "+":
                        result += n;
                        break;
                    case "-":
                        result -= n;
                        break;
                    case "*":
                        System.out.println("* not implemented");
                        break;
                    case "/":
                        System.out.println("/ not implemented");
                        break;
                }

            }
            //TODO: Hacky way to not print invalid variable
            if (result != Long.MIN_VALUE) {
                System.out.println(result);
            }
        }
        System.out.println("Bye!");
    }

    private static long getVariableNumValue(String s) {
        char operator = '?';
        if(String.valueOf(s.charAt(0)).matches("[-+]")) {
            operator = s.charAt(0);
            s = s.substring(1);
        }

        long output = Long.MIN_VALUE;
        if(variableMap.containsKey(s)) {
            output = variableMap.get(s);
        } else {
            System.out.println("Unknown variable");
            //System.out.printf("Unknown variable: %s not defined yet or cannot be found", s);
            //TODO: Proper error handling, just returns min value of long as is, to tell that something is deeply wrong
            return output;
        }

        if (operator == '-') {
            return output * -1;
        }
        return output;
    }

    private static void saveVariable(String inputString) {
        if (inputString.matches("=")) {
            System.out.println("Invalid assignment: input is nothing but an equals sign, you kek");
            return;
        }
        String[] inputElements = inputString.split("=");

        if (varAssignmentHasMistake(inputElements)) return;
        inputElements[1] = condenseSigns(inputElements[1].trim());
        long number;
        if(inputElements[1].matches("[+-]*[a-zA-Z]+")){
            number = getVariableNumValue(inputElements[1]);
        } else if(inputElements[1].matches("[+-]*[0-9]+")){
            number = Integer.parseInt(inputElements[1]);
        } else {
            System.out.printf("Assignment of var to unexpected value %s, aborting\n", inputElements[1]);
            number = Long.MIN_VALUE;
        }
        //System.out.printf("we put %s as %d\n", inputElements[0].trim(), number);
        variableMap.put(inputElements[0].trim(), number);
    }

    private static boolean varAssignmentHasMistake(String[] inputElements) {
        if (inputElements.length < 2) {
            System.out.println("Invalid assignment");
            //System.out.println("Invalid assignment: one side doesn't have input!");
            return true;
        }
        if (inputElements.length > 2) {
            System.out.println("Invalid assignment");
            //System.out.println("Invalid assignment: more than 1 equals sign!");
            return true;
        }
        if (!inputElements[0].trim().matches("[a-zA-Z]+")){
            System.out.println("Invalid identifier");
            //System.out.println("Invalid identifier: You can only use latin letters as variable name!");
            return true;
        }
        if (!inputElements[1].trim().matches("[-+ ]*([0-9]+|[a-zA-Z]+)")){ //TODO: remember fractions later!
            System.out.println("Invalid assignment");
            //System.out.println("Invalid assignment: Value has symbols other than numbers and + -");
            return true;
        }
        return false;
    }

    private static List<String> getElementsArrayList(Matcher m) {
        List<String> list = new ArrayList<>();

        while(m.find()){
            String substring = m.group();
            if(substring.isEmpty()) continue;
            String[] result = cleanUpDigit(substring);
            String operator = result[0];
            String cleanNumber = result[1];
            if(!operator.isEmpty()) {
                list.add(operator);
            }
            if(!cleanNumber.isEmpty()) {
                list.add(cleanNumber);
            }

        }
        list.remove("?");
        return list;
    }

    private static String[] cleanUpDigit(String substring) {
        substring = substring.replaceAll("\\s", "");

        char operator = '?';
        if(String.valueOf(substring.charAt(0)).matches("[-+*/]")) {
            operator = substring.charAt(0);
            substring = substring.substring(1);
        }
        String cleanNumber = condenseSigns(substring);
        return new String[] {String.valueOf(operator), cleanNumber};
    }

    private static String condenseSigns(String input) {
        //while current substring is bigger than the digits of the substring and 1 sign
        while (input.length() > input.replaceAll("\\W", "").length() + 1) {
            input = input.replaceAll("\\+-|-\\+", "-")
                                    .replaceAll("\\++", "")
                                    .replaceAll("--", "+");
        }
        //System.out.println("output is now " + input);
        return input;
    }
}
