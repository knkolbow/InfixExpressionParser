package infixParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;


public class Main {
	public static void main(String[] args) throws IOException {
		FileInputStream inputFile = new FileInputStream("testCase2.txt");
		Scanner scanner = new Scanner(inputFile);
		int count = 1;
		System.out.println("Infix Expression Parser");
		System.out.println();
		while (scanner.hasNextLine()) {
			String expression = scanner.nextLine().trim().replaceAll(" ", "");
			
			Parser eval = new Parser();
			
			if (!expression.isEmpty()) {
				System.out.print(count + ") " + expression + " = ");
				int result = eval.evaluateInfix(expression);
				//System.out.print(expression + " = ");
				System.out.println(result); 
				count++;
			}
		}	
		scanner.close();
		inputFile.close();
	}
}
