package calculator;


import java.util.ArrayDeque;
import java.util.Deque;

public class NotationConverter {

    public enum Target {POSTFIX, INFIX}


    String input;
    Target target;


    /*____________CONSTRUCTORS_____________*/
    public NotationConverter(String input, Target t) {
        this.input = input;
        this.target = t;
    }

    public NotationConverter(String input) {
        this.input = input;
        this.target = Target.POSTFIX;
    }
    /*_____________________________________*/

    public String convert() {
        switch (target) {
            case POSTFIX:
                return convertToPostfix();
            case INFIX:
                return convertToInfix();
            default:
                return null;
        }
    }

    private String convertToPostfix() {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {

            /*--- Rule 1: add operands to result as they arrive ---*/
            if (Character.toString(c).matches("[0-9a-zA-Z]")) {
                if (!result.toString().equals("") && result.substring(result.length()-1).matches("[-+*/^]")) {
                    result.append(" ");
                }
                result.append(c);
            }
            /*--- Check if we have an operator next ---*/
            else if (Character.toString(c).matches("[-+*/^]")) {
                //do not remove, needed to have spaces between numbers!
                result.append(" ");
                /*--- Rule 2: Push the incoming operator on the stack if empty or stack has left parenthesis on top ---*/
                if (stack.isEmpty() || stack.peek().equals('(')) {
                    stack.push(c);
                }
                /*--- Rule 3: If incoming operator has higher precedence than top stack, put it on stack. ---*/
                else if (hasHigherPrecedence(c, stack.peek())) {
                    stack.push(c);
                }
                /*--- Rule 4: If incoming operator has lower or equal precedence ... ---*/
                else {
                    result.append(stack.pop());
                    while (stack.peek() != null && (stack.peek() != '(' || getPrecedence(stack.peek()) >= getPrecedence(c))) {
                        result.append(" ").append(stack.pop());
                    }
                    stack.push(c);
                }
            }
            /*--- Rule 5: Push left parenthesis on the stack ---*/
            else if (c == '(') {
                stack.push(c);
            }
            /*--- Rule 6: Process stack for right parenthesis ---*/
            else if (c == ')') {
                while (stack.peek() != null && stack.peek() != '(') {
                    result.append(" ").append(stack.pop());
                }
                stack.pop();
            }
        }
        while (stack.peek() != null) {
            if (stack.peek() == '(' || stack.peek() == ')') {
                System.out.println("Syntax error. Found parenthese in stack at the end of processing!!");
            }
            result.append(" ").append(stack.pop());
        }
        return result.toString();
    }

    private String convertToInfix() {
        Deque<String> stack = new ArrayDeque<>();
        System.out.println("convertToInfix not implemented yet");
        return "";
    }

    private boolean hasHigherPrecedence(char higher, char lower) {
         int precedenceHigher = getPrecedence(higher);
         int precedenceLower = getPrecedence(lower);
         return precedenceHigher > precedenceLower;
    }

    private int getPrecedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        System.out.printf("unknown operator %c passed, getPrecedence returns -1", operator);
        return -1;
    }
}
