package com.bank.controller;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DuplicateKeyException;

import com.bank.configuration.BankConfiguration;
import com.bank.entity.Person;
import com.bank.entity.dao.Operations;
import com.bank.exception.InvalidInputByUser;
import com.bank.exception.NoUserFoundsException;

public class NewBankMain {
	static Scanner sc = new Scanner(System.in); // Scanner object to read user input
	static Person per = new Person(); // Creating a Person object
	static String name = "null", password = "null", gmail = "null"; // Variables for user details
	static double balance = 0.0d; // Variable to store balance amount
	static long accountNum = 0l; // Variable to store account number
	static ApplicationContext appContext = new AnnotationConfigApplicationContext(BankConfiguration.class);
	static Operations op = appContext.getBean(Operations.class);

	public static void main(String[] args) {

		// Prompt the user to choose between signing in or signing up
		System.out.println(
				"What would you like to do SIGN IN or SIGN UP \n\n"
				+ "Just type 'In' for \"SIGN IN\" or 'Up' for \"SIGN UP\"");
		
		String signIn_SignUp = sc.next(); // Read user input for sign-in or sign-up

		// Check if the user wants to sign in
		if (signIn_SignUp.equalsIgnoreCase("In")) {
			// Attempt to sign in the user with provided credentials
			per = op.signIn(name, password, gmail);
			// Check if the sign-in was successful
			if (per != null) {
				signingIn(per.getName());
			}
				// Check if the user wants to sign up
			} else if (signIn_SignUp.equalsIgnoreCase("Up")) {
				try {
					op.signUp(per); // Call the sign-up method

					System.out.println("Would you like to login in app\ntype \"yes\" for login otherwise \"no\"");
					String signUpToLogin = sc.next();
					if (signUpToLogin.equalsIgnoreCase("yes"))
						op.signIn(name, password, gmail);
					signingIn(per.getName());

				} catch (DuplicateKeyException e) {
					e.printStackTrace();
				}

			} else {
				// If the input is neither "In" nor "Up", throw an exception
				try {
					throw new InvalidInputByUser("InvalidUser InputException\n");
				} catch (InvalidInputByUser e) {
					// Handle the exception and inform the user
					System.err.println("Please Enter the valid input to run the application");
					e.printStackTrace();
				}
			}

			System.out.println("Program is end............");

	}

	
	
	
	
	
	
	
	public static void signingIn(String name) {
		System.out.println("Hello & Welcome Back "+name+"\n");
		System.out.println(per.getAccountNum()+"\t"+per.getBalance()+"\t"+per.getGmail()+"\t"+per.getPassword()+"\t"+per.getName());

		System.out.println("\nWe’re here to help! What service or activity would you like to try today?\n");
		System.out.println(
				"DEPOSIT/ WITHDRAWAL or CHECK BALANCE \nIf you want to \"CHECK BALANCE\" then type just 'check'\n");
		System.out.println(
				"Or if you want to \"DEPOSIT BALANCE\" type 'D' or 'd'\n\nIf you want to \"WITHDRAWAL BALANCE\" then type 'W' or 'w'");

		// Consume the newline character left by next() and read the next line for the
		// request
		sc.nextLine();
		String requestQuery = sc.nextLine(); // Read user input for the requested service

//		System.out.println("Would you like to login in app\ntype \"yes\" for login otherwise \"no\"");
//		String signUpToLogin = sc.next();

		
		if (requestQuery.equalsIgnoreCase("d")) {
			op.deposit(per, balance); // Call the deposit method

			// Check if the user wants to withdraw money
		} else if (requestQuery.equalsIgnoreCase("w")) {
			op.withdraw(per, balance); // Call the withdrawal method

			// Check if the user wants to check their balance
		} else if (requestQuery.equalsIgnoreCase("check")) {
			op.checkBalance(per, accountNum); // Call the check balance method

		} else {
			// If the input is invalid, throw an exception
			try {
				throw new InvalidInputByUser("InvalidUseInputException\n");
			} catch (InvalidInputByUser e) {
				// Handle the exception and inform the user
				System.err.println("Please Enter the valid input to run the application");
				e.printStackTrace();
			}
		}

	}
	
	
	
	
	
	
	
	
//	5072420263423990
//	enjoyKapil
//	kapil@gmail.com

}
