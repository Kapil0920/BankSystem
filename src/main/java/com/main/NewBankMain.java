package com.main;

import java.util.Scanner;

import com.entity.Person;
import com.entity.dao.Operations;
import com.exception.InvalidInputByUser;
import com.exception.NoUserFoundsException;

public class NewBankMain {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Person per = new Person();
		String name = "null", password = "null", gmail = "null";
		double balance = 0.0d;
		long accountNum = 0l;
		Operations operations = new Operations();

		/*
		 * System.out.println("Do you want to sign up"); String ans = sc.nextLine();
		 * 
		 * if (ans.equalsIgnoreCase("yes")) { op.signUp(name, password, gmail); }
		 * System.out.println("Thanks to using");
		 * 
		 * System.out.println("Do you want to s"); per = op.signIn(name, password,
		 * gmail); if (per != null) { System.out.println("Welocome back " +
		 * per.getName()); } else { System.err.println("No User Found"); }
		 * 
		 * System.out.println("Do you want to WITHDRAW ammount"); String withdrawAns =
		 * sc.nextLine(); if (withdrawAns.equalsIgnoreCase("yes")) { op.withdraw(per,
		 * 0);
		 * 
		 * }
		 * 
		 * System.out.println("Do you want to deposit cash"); String depAns=
		 * sc.nextLine(); if(depAns.equalsIgnoreCase("yes")) { op.deposit(per, balance);
		 * }
		 */
		
		
		
		/*
		System.out.println("What would you like to do SIGN IN or SIGN UP \n\nJust type 'In' for \"SIGN IN\" or 'Up' for \"SIGN UP\"");
		String signIn_SignUp = sc.next();
		
		if(signIn_SignUp.equalsIgnoreCase("In")) {
			per = operations.signIn(name, password, gmail);
			
			if(per!=null) {
				System.out.println("Welcome: "+per.getName());
				
				System.out.println("\nWe’re here to help! What service or activity would you like to try today?\n");
				
				System.out.println("DEPOSIT/ WITHDRAWAL or CHECK BALANCE \nIf you want to \"CHECK BALANCE\" than type just 'check'");
				sc.nextLine();
				//taking the input to execute the head of 
				String requestQuery= sc.nextLine();
				
				if(requestQuery.equalsIgnoreCase("deposit")|| requestQuery.equalsIgnoreCase("deposit")) {
					operations.deposit(per, balance);
					
				}
				else if(requestQuery.equalsIgnoreCase("withdrawal")) {
					operations.withdraw(per, balance);
				}
				else if(requestQuery.equalsIgnoreCase("check")) {
					operations.checkBalance(accountNum);
				} 
				else {
					try {
					throw new InvalidInputByUser("InvalidUseInputException\n"); 
					}catch(InvalidInputByUser e) {
						System.err.println("Please Enter the valid input to run the application");
						e.printStackTrace();
						
					}
				}
			}
			
			
			else {
				try {
				throw new NoUserFoundsException(" Invalid User Name/ Password OR gmail");
				}
				catch (NoUserFoundsException e) {
					System.err.println("No User found please check the details");
					e.printStackTrace();
				}
			}
			
		}
		else if(signIn_SignUp.equalsIgnoreCase("Up")){
			System.out.println("Else if for sign up");
			operations.signUp(name, password, gmail);
		
		}
		else {
			try {  
			throw new InvalidInputByUser("InvalidUserInputException\n"); 
			}
			catch (InvalidInputByUser e) {
				System.err.println("Please Enter the valid input to run the application");
				e.printStackTrace();
			}
			
		}
		*/
		
		
		
		
		
		
		// Prompt the user to choose between signing in or signing up
		System.out.println("What would you like to do SIGN IN or SIGN UP \n\nJust type 'In' for \"SIGN IN\" or 'Up' for \"SIGN UP\"");
		String signIn_SignUp = sc.next(); // Read user input for sign-in or sign-up

		// Check if the user wants to sign in
		if(signIn_SignUp.equalsIgnoreCase("In")) {
		    // Attempt to sign in the user with provided credentials
		    per = operations.signIn(name, password, gmail);
		    
		    // Check if the sign-in was successful
		    if(per != null) {
		        // Welcome the user if sign-in is successful
		        System.out.println("Welcome: " + per.getName());
		        
		        // Prompt the user for the service they would like to use
		        System.out.println("\nWe’re here to help! What service or activity would you like to try today?\n");
		        System.out.println("DEPOSIT/ WITHDRAWAL or CHECK BALANCE \nIf you want to \"CHECK BALANCE\" than type just 'check'");
		        
		        // Consume the newline character left by next() and read the next line for the request
		        sc.nextLine();
		        String requestQuery = sc.nextLine(); // Read user input for the requested service
		        
		        // Check if the user wants to deposit money
		        if(requestQuery.equalsIgnoreCase("deposit")) {
		            operations.deposit(per, balance); // Call the deposit method
		            
		        // Check if the user wants to withdraw money
		        } else if(requestQuery.equalsIgnoreCase("withdrawal")) {
		            operations.withdraw(per, balance); // Call the withdrawal method
		            
		        // Check if the user wants to check their balance
		        } else if(requestQuery.equalsIgnoreCase("check")) {
		            operations.checkBalance(per,accountNum); // Call the check balance method
		            
		        } else {
		            // If the input is invalid, throw an exception
		            try {
		                throw new InvalidInputByUser ("InvalidUseInputException\n"); 
		            } catch(InvalidInputByUser  e) {
		                // Handle the exception and inform the user
		                System.err.println("Please Enter the valid input to run the application");
		                e.printStackTrace();
		            }
		        }
		    } else {
		        // If sign-in fails, throw an exception
		        try {
		            throw new NoUserFoundsException("Invalid User Name/ Password OR gmail");
		        } catch (NoUserFoundsException e) {
		            // Handle the exception and inform the user
		            System.err.println("No User found please check the details");
		            e.printStackTrace();
		        }
		    }
		    
		// Check if the user wants to sign up
		} else if(signIn_SignUp.equalsIgnoreCase("Up")) {
		    System.out.println("Else if for sign up");
		    operations.signUp(name, password, gmail); // Call the sign-up method

		} else {
		    // If the input is neither "In" nor "Up", throw an exception
		    try {  
		        throw new InvalidInputByUser ("InvalidUser InputException\n"); 
		    } catch (InvalidInputByUser  e) {
		        // Handle the exception and inform the user
		        System.err.println("Please Enter the valid input to run the application");
		        e.printStackTrace();
		    }
		}
		
	}
	
	
	
	
	
	
	
	
	

}
