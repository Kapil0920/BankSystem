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

		System.out.println("Do you want to sign up");
		String ans = sc.nextLine();

		if (ans.equalsIgnoreCase("yes")) {
			op.signUp(name, password, gmail);
		}
		System.out.println("Thanks to using");

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

	}

}
