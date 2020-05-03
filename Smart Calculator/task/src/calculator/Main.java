package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running){
            String inputString = scanner.nextLine();

            if (inputString.isEmpty()) continue;
            else if (inputString.equals("/exit")) {
                running = false;
                break;
            } else if (inputString.equals("/help")) {
                System.out.println("The program calculates a term of numbers. + and - operators supported");
                continue;
            }
            if(!inputString.matches("([-+ ]*[0-9])*")){
                System.out.println("detected illegal format, probably be able to error correct and ignore the mistake.");
            }

            String regexOperatorDigit = "[-+ ]*[0-9]*";
            Pattern pattern = Pattern.compile(regexOperatorDigit);
            Matcher m = pattern.matcher(inputString);
            List<String> list = getElementsArrayList(m);

            int result = 0;
            for (String s : list) {
                int n = Integer.parseInt(s);
                result += n;
            }

            System.out.println(result);
        }
        System.out.println("Bye!");
    }

    private static List<String> getElementsArrayList(Matcher m) {
        List<String> list = new ArrayList<>();

        while(m.find()){
            String substring = m.group();
            substring = cleanUpDigit(substring);

            if(!substring.isEmpty()) {
                list.add(substring);
            }

        }
        return list;
    }

    private static String cleanUpDigit(String substring) {
        substring = substring.replaceAll("\\s", "");

        //while current substring is bigger than the digits of the substring and 1 operator
        while (substring.length() > substring.replaceAll("\\D", "").length() + 1) {
            substring = substring.replaceAll("\\+-|-\\+", "-")
                                 .replaceAll("\\++", "")
                                 .replaceAll("--", "+");
        }
        return substring;
    }
}
