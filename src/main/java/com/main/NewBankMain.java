package com.main;

import java.util.Scanner;
import com.entity.Person;
import com.entity.dao.Operations;

public class NewBankMain {
	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		Person per = new Person();
		String name = "null", password = "null", gmail = "null";
		double balance = 0.0d;
		long accountNum = 0l;
		Operations op = new Operations();

	/*	System.out.println("Do you want to sign up");
		String ans = sc.nextLine();

		if (ans.equalsIgnoreCase("yes")) {
			op.signUp(name, password, gmail);
		}
		System.out.println("Thanks to using");

		System.out.println("Do you want to s");
		per = op.signIn(name, password, gmail);
		if (per != null) {
			System.out.println("Welocome back " + per.getName());
		} else {
			System.err.println("No User Found");
		}

		System.out.println("Do you want to WITHDRAW ammount");
		String withdrawAns = sc.nextLine();
		if (withdrawAns.equalsIgnoreCase("yes")) {
			op.withdraw(per, 0);

		}
		
		System.out.println("Do you want to deposit cash");
		String depAns= sc.nextLine();
		if(depAns.equalsIgnoreCase("yes")) {
			op.deposit(per, balance);
		}
		*/

		
	
		System.out.println("Which option will select SIGN IN/SIGN UP");
		String signInOrSignUp=sc.nextLine();
		
		if(signInOrSignUp.equalsIgnoreCase("sign in")) {
			op.signIn(name, password, gmail);
			if(per!=null) {
				System.out.println("Welcome Back '"+per.getName()+"'");
				
				System.out.println("What do you like to do DEPOSIT CASH/ WITHDRAWAL CASH/ CHECK ACCOUNT BALANCE");
				String requestAns = sc.nextLine();
				if(requestAns.equalsIgnoreCase("deposit cash"))
				{
					op.deposit(per, balance);
				}
				else if(requestAns.equalsIgnoreCase("withdrawal cash"))
				{
					op.withdraw(per, balance);
				}
				
			}
		}
		else {
			System.err.println("User name OR password is incorrect ");
		}
		
	}

}
