package com.main;

import java.util.Scanner;

import com.entity.Person;
import com.entity.dao.Operations;
import com.exception.InvalidRequestException;

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

		
		System.out.println("What would you like to do SIGN IN or SIGN UP \nJust type 'In' for \"SIGN IN\" or 'Up' for \"SIGN UP\"");
		String signIn_SignUp = sc.next();
		
		if(signIn_SignUp.equalsIgnoreCase("In")) {
			per = operations.signIn(name, password, gmail);
			if(per!=null) {
				System.out.println("Welcome: "+per.getName());
				System.out.println("We’re here to help! What service or activity would you like to try today?");
				System.out.println("DEPOSIT/ WITHDRAWAL or CHECK BALANCE \nIf you want to \"CHECK BALANCE\" than type just 'check'");
				sc.next();
				String requestQuery= sc.nextLine();
				
				if(requestQuery.equalsIgnoreCase("Deposit")) {
					operations.deposit(per, balance);
				}
				else if(requestQuery.equalsIgnoreCase("withdrawal")) {
					operations.withdraw(per, balance);
				}
				else if(requestQuery.equalsIgnoreCase("check")) {
					operations.checkBalance(accountNum);
				}
				else {
//					try {
//					throw new InvalidRequestException();
//					}catch (InvalidRequestException e) {
//						e.printStackTrace();
//					}
					InvalidRequestException in = new InvalidRequestException();
					in.printException();
				}
			}
			else {
				InvalidUserDetails 
			}
		}
	}

}
