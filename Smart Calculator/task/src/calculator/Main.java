package calculator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static Map<String, Long> variableMap = new HashMap<>();
    public static String regexOperatorDigit = "[-+ ]*[0-9]*";
    public static Pattern pattern = Pattern.compile(regexOperatorDigit);

    public static void main(String[] args) {
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

            /* VARIABLE PRINTING TODO: might be useless due to normal calculator logic getting the var automatically*/
            if (inputString.matches("[a-zA-Z]*") && variableMap.containsKey(inputString)){
                System.out.println(variableMap.get(inputString));
                continue;
            }

            /* CATCH ALL FOR INVALID INPUT */
            if(!inputString.matches("([-+ ]*[0-9])*")){
                System.out.println("Invalid expression: Catch All");
                continue;
            }


            Matcher m = pattern.matcher(inputString);
            List<String> list = getElementsArrayList(m);
            if (list.contains("?")){
                System.out.println("Invalid expression: ? found in list");
                continue;
            }

            System.out.println("list of elements:" + list);

            if(list.get(0).matches("[+-]")){
                list.add(0, "0");
            }
            int result = Integer.parseInt(list.get(0));
            for (int i = 1; i < list.size(); i = i+2) {
                String operator = list.get(i);
                int n = Integer.parseInt(list.get(i+1));
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

            System.out.println(result);
        }
        System.out.println("Bye!");
    }

    private static void saveVariable(String inputString) {
        if (inputString.matches("=")) {
            System.out.println("Invalid assignment: input is nothing but an equals sign, you kek");
            return;
        }
        String[] inputElements = inputString.split("=");

        if (varAssignmentHasMistake(inputElements)) return;

        long number = Long.parseLong(condenseSigns(inputElements[1].trim()));
        System.out.printf("we put %s as %d\n", inputElements[0].trim(), number);
        variableMap.put(inputElements[0].trim(), number);
    }

    private static boolean varAssignmentHasMistake(String[] inputElements) {
        if (inputElements.length < 2) {
            System.out.println("Invalid assignment: one side doesn't have input!");
            return true;
        }
        if (inputElements.length > 2) {
            System.out.println("Invalid assignment: more than 1 equals sign!");
            return true;
        }
        if (!inputElements[0].trim().matches("[a-zA-Z]+")){
            System.out.println("Invalid identifier: You can only use latin letters as variable name!");
            return true;
        }
        if (!inputElements[1].trim().matches("[-+ ]*[0-9]+")){ //TODO: remember fractions later!
            System.out.println("Invalid assignment: Value has symbols other than numbers and + -");
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
        while (input.length() > input.replaceAll("\\D", "").length() + 1) {
            input = input.replaceAll("\\+-|-\\+", "-")
                                    .replaceAll("\\++", "")
                                    .replaceAll("--", "+");
        }
        //System.out.println("output is now " + input);
        return input;
    }
}
