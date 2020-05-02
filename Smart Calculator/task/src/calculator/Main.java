package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            String input = scanner.nextLine();

            //matching when both numbers are entered (+-xxx +-yyyy)
            if (input.matches("-?[0-9]+ -?[0-9]+")){
                int a = Integer.parseInt(input.split(" ")[0]);
                int b = Integer.parseInt(input.split(" ")[1]);
                System.out.println(a+b);
            //matching when only one number is entered (+-zzzzz)
            } else if (input.matches("-?[0-9]+")){
                System.out.println(Integer.parseInt(input));
            }
            //matching when we have "/exit" input
            else if (input.equals("/exit")){
                running = false;
            }
        }
        System.out.println("Bye!");
    }
}
