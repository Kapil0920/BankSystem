package com.entity.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import com.entity.Person;
import java.util.Random;

public class Operations implements OperationInterface {
	Random random = new Random();
	long accountNum = 0l;
	Scanner scan = new Scanner(System.in);
	Person person = new Person();

	static Connection conn;
	static {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "root");
		} catch (SQLException e) {
			// TODO: handle exception
			System.err.println("Error Occured");
			e.printStackTrace();
		}
	}

	@Override
	public void signUp(String name, String password, String gmail) {
		System.out.println("Enter Your Name to 'SIGN UP'");
		name = scan.nextLine();
		System.out.println("Enter your password");
		password = scan.nextLine();
		System.out.println("Enter your gmail id");
		gmail = scan.nextLine();

		this.accountNum = random.nextLong(1000000000000000L, 9999999999999999L);
		if (accountNum != 0) {
			System.out.println("Account number generated: " + accountNum);
		}

		try (PreparedStatement ps = conn
				.prepareStatement("insert into bankuser (accountNum,name,password,gmail) values(?,?,?,?)")) {
			ps.setLong(1, accountNum);
			ps.setString(2, name);
			ps.setString(3, password);
			ps.setString(4, gmail);
			ps.executeUpdate();

			System.out.println("Your account num is: " + this.accountNum);

		} catch (Exception e) {
			System.err.println("Mail id is already register");
			e.printStackTrace();
		}

	}

	@Override
	public Person signIn(String name, String password, String gmail) {
		System.out.println("Enter your name ");
		name = scan.nextLine();
		System.out.println("Enter your password");
		password = scan.nextLine();
		System.out.println("Enter Your gmail");
		gmail = scan.nextLine();

		try (PreparedStatement ps = conn
				.prepareStatement("select * from bankuser where name=? and password=? and gmail=?")) {
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setString(3, gmail);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return new Person(rs.getLong("accountNum"), rs.getDouble("balance"), rs.getString("name"),
						rs.getString("password"), rs.getString("gmail"));
			}
		} catch (SQLException e) {
			System.err.println("Error Occured");
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public double withdraw(Person person, double balance) {
		try (PreparedStatement ps = conn.prepareStatement("select balance from bankuser where accountNum=?")) {
			System.out.println("Enter your account number for withdraw: ");
			long accountNum = scan.nextLong();
			ps.setLong(1, accountNum);
			person.setAccountNum(accountNum);

			ResultSet rs = ps.executeQuery();

			// Check if the account number exists
			if (rs.next()) {
				double currentBalance = rs.getDouble("balance"); // Fetch the balance from the database
				System.out.println("Account found. Current balance: " + currentBalance);

				System.out.println("Enter withdraw ammount: ");
				balance = scan.nextDouble();
				// Check if withdrawal amount is greater than current balance
				if (balance > currentBalance) {
					System.out.println("Oops! It looks like you don't have enough funds for this withdrawal. ");
					return person.getBalance();
				}

				// Proceed with withdrawal if enough balance
				PreparedStatement ps1 = conn
						.prepareStatement("update bankuser set balance = balance - ? where accountNum = ?");
				ps1.setDouble(1, balance);
				ps1.setLong(2, person.getAccountNum());
				int rowsAffected = ps1.executeUpdate();

				// If the update was successful, update the person's balance
				if (rowsAffected > 0) {
					person.setBalance(person.getBalance() - balance);
					System.out.println("Withdrawn: " + balance);
				} else {
					System.out.println("Error: Withdrawal could not be completed.");
				}
			} else {
				System.err.println(
						"It looks like the account number you entered is incorrect. Please double-check and try again.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Server error, sorry!!");
		}

		return person.getBalance();
	}

	@Override
	public void checkBalance(long accountNumber) {
		// TODO Auto-generated method stub

	}

	public double deposit(Person per, double balance) {
		try (PreparedStatement ps = conn
				.prepareStatement("update bankuser set balance =balance + ? where accountNum=? ")) {
			System.out.println("Enter account number");
			long newDepositAccount = scan.nextLong();
			ps.setLong(2, newDepositAccount);

			System.out.println("Enter deposit ammount: ");
			balance = scan.nextDouble();
			person.setBalance(balance);
			ps.setDouble(1, balance);
			ps.executeUpdate();
			person.setBalance(person.getBalance() + balance);
			System.out.println("You've succesfully deposit your balance ");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return balance;

	}

}
