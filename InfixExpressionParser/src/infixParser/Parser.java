package infixParser;

import java.util.Scanner;
import java.util.Stack;

public class Parser {
	// Data fields
		private String opArr[] = {"*", "/", "%", "^", "+", "-", "<", ">", "<=", ">=", "==", "!=", "&&", "||"};
		private Stack<Integer> numHold;
		private Stack<String> operators;
		
		// Constructor
		public Parser() {
			numHold = new Stack<>();
			operators = new Stack<>();
		}
		
	    /** Determines the precedence of an operator
	    	@param operator: the operator to find its precedence
	    	@return: the precedence of the operator
	    	@throws IllegalArgumentException: operator is not supported.
	     */
		private int precedence(String operator) {
			if (operator.equals("^")) { return 7; }
			if (operator.equals("*") || operator.equals("/") || operator.equals("%")) { return 6; }
			if (operator.equals("+") || operator.equals("-")) { return 5; }
			if (operator.equals(">") || operator.equals("<") || operator.equals(">=") || operator.equals("<=")) { return 4; }
			if (operator.equals("==") || operator.equals("!=")) { return 3; }
			if (operator.equals("&&")) { return 2; }
			if (operator.equals("||")) { return 1; }
			throw new IllegalArgumentException(String.format("Operator %s is not supported.", operator));
		}
		/**
		 * Solves the expression
		 * @param num1: the first number in the expression 
		 * @param num2: the second number in the expression
		 * @param sign: the operator between the two numbers
		 * @return
		 */
		private int resultOfOperation(int num1, int num2, String sign) {
			if(sign.equals("+")) { return num1 + num2; }
			else if(sign.equals("-")) { return num1 - num2; }
			else if(sign.equals("*")) { return num1 * num2; }
			else if(sign.equals("/")) {
				try {
					int result = num1 / num2;
					return result;
				}
				catch (ArithmeticException e) {
					System.out.print("Error: Divide by zero - ");
				}
			}
			else if(sign.equals("%")) { return num1 % num2; }
			else if(sign.equals("^")) { return (int)Math.pow(num1, num2); }
			else if(sign.equals(">")) {
				if (num1 > num2) { return 1; }
			}
			else if(sign.equals(">=")) {
				if (num1 >= num2) { return 1; }
			}
			else if(sign.equals("<")) {
				if (num1 < num2) { return 1; }
			}
			else if(sign.equals("<=")) {
				if (num1 <= num2) { return 1; }
			}
			else if(sign.equals("==")) {
				if (num1 == num2) { return 1; }
			}
			else if(sign.equals("!=")) {
				if (num1 != num2) { return 1; }
			}
			else if(sign.equals("&&")) {
				if(num1 == num2) { return 1; }
			}
			else if(sign.equals("||")) {
				if(num1 != 0 || num2 != 0) { return 1; }
			}
			return 0;
		}
		/**
		 * Determines if the string is an operator
		 * @param token: the string to check
		 * @return: true if it is an operator, false otherwise
		 */
		private boolean operatorChecker(String token) {
			for (int i = 0; i < opArr.length; i++) {
				if (token.compareTo(opArr[i]) == 0) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * Evaluates an infix equation
		 * @param expression: the infix equation to solve
		 * @return: the result
		 */
		public int evaluateInfix(String expression) {
			expression = expression.replace("", " ");
			//copies expression
			String copy = expression.trim();
			//stores characters from expression into string array
			//used for getting operations with two characters
			String chars[] = copy.split(" ");
			Scanner scnr = new Scanner(expression);
			//temporary string variable to store digits
			String temp = "";
			//count variable used to access chars[] array index
			//stays one index ahead of scanner in the expression
			int count = 0;
			
			while(scnr.hasNext()) {
				String token = scnr.next();
				count++;

				//grabs numbers from expression and puts them into a string
				//allows storing of numbers more than single digits
				if(Character.isDigit(token.charAt(0))) {
					while (Character.isDigit(token.charAt(0)) && scnr.hasNext()) {
						temp = temp + token.charAt(0);
						if (scnr.hasNext()) {
							token = scnr.next();
							count++;
						}
					}
					
					//pushes int value of temp to numHold stack as long as temp is not empty
					if (!temp.equals("")) {
						numHold.push(Integer.valueOf(temp));
						temp = "";
					}
					else if(Character.isDigit(token.charAt(0))) {
						numHold.push(Integer.valueOf(token));
					}
				}

				//if token is equal to an operations character that has two characters, append second character to token
				if(token.equals("&") || token.equals("|") || token.equals("=") || token.equals("!")) {
					token = token + chars[count];
				}
				//if token is equal to > or < check to see if the next character in the expression is "=", append to token if it is
				else if(token.equals(">") || token.equals("<")) {
					if(chars[count].equals("=")) {
						token = token + chars[count];
					}
				}
				
				if(token.equals("(")) { operators.push(token); }
				// solve the expression inside the parentheses
				else if (token.equals(")")) {
					while (!operators.isEmpty() && !operators.peek().equals("(")) {
						int num2 = numHold.pop();
						int num1 = numHold.pop();
						String sign = operators.pop();
						numHold.push(resultOfOperation(num1, num2, sign));
					}
					operators.pop(); // removes "("
				}
				
				else if (operatorChecker(token)) {
					while (!operators.isEmpty() && !(operators.peek().equals("(")) && (precedence(token) <= precedence(operators.peek()))) {
						int num2 = numHold.pop();
						int num1 = numHold.pop();
						String sign = operators.pop();
						numHold.push(resultOfOperation(num1, num2, sign));
					}
					if (!operators.isEmpty() && !(operators.peek().equals("(")) && precedence(token) >= precedence(operators.peek())) { operators.push(token); }
					else { operators.push(token); }

				}
			}
			scnr.close();
			
			while (!operators.isEmpty()) { 
				int num2 = numHold.pop();
				int num1 = numHold.pop();
				String sign = operators.pop();
				numHold.push(resultOfOperation(num1, num2, sign));
			}
			return numHold.pop();
		}
}
