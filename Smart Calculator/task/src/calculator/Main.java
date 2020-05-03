package calculator;

import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while(true){
            String inputString = scanner.nextLine();

            if (inputString.isEmpty()) continue;
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
            if(!inputString.matches("([-+ ]*[0-9])*")){
                System.out.println("Invalid expression");
                continue;
            }

            String regexOperatorDigit = "[-+ ]*[0-9]*";
            Pattern pattern = Pattern.compile(regexOperatorDigit);
            Matcher m = pattern.matcher(inputString);
            List<String> list = getElementsArrayList(m);
            if (list.contains("?")){
                System.out.println("Invalid expression");
                continue;
            }

            //System.out.println("list of elements:" + list);

            if(list.get(0).matches("[+-]")){
                list.add(0, "0");
            }
            int result = Integer.parseInt(list.get(0));
            for (int i = 1; i < list.size(); i= i+2) {
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
        String cleanNumber = substring;
        //while current substring is bigger than the digits of the substring and 1 operator
        while (cleanNumber.length() > cleanNumber.replaceAll("\\D", "").length() + 1) {
            cleanNumber = cleanNumber.replaceAll("\\+-|-\\+", "-")
                                    .replaceAll("\\++", "")
                                    .replaceAll("--", "+");
        }
        return new String[] {String.valueOf(operator), cleanNumber};
    }
}
