package com.bank.entity.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import  org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.bank.entity.Person;
import com.bank.entity.PersonRowMapper;
import com.bank.exception.AccountNumberNotMatchException;
@Component
public class Operations implements OperationInterface {
	// Random object to generate random account numbers
	Random random = new Random();
	long accountNum = 0L; // Variable to hold the account number
	Scanner scan = new Scanner(System.in); // Scanner for user input
	Person person = new Person(); // Person object to hold user details
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Operations() {
		super();
	}

	@Autowired
	public Operations(DataSource data) {
		jdbcTemplate = new JdbcTemplate(data);
	}

	// Static block to establish a database connection
	static Connection conn;
	static {
		try {
			// Establishing connection to the MySQL database
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking_system", "root", "root");
		} catch (SQLException e) {
			// Handle SQL exceptions
			System.err.println("Error Occurred");
			e.printStackTrace();
		}
	}

	@Override
	public void signUp(Person per) {
		System.out.println("Enter name: ");
		String name = scan.nextLine();
		per.setName(name);

		while (true) {
			System.out.println("Enter your password");
			String password = scan.nextLine();
			per.setPassword(password);
			// Validate password length
			if (passwordLength(password)) {
				break; // Exit loop if password is valid
			} else {
				System.out.println("Password should at least contain 8 digits");
			}
		}

		System.out.println("Enter your gmail id");
		String gmail = scan.nextLine();

		while (!gmail.contains("@") || !gmail.contains("gmail.com")) {
			System.out.println("Please enter the valid Email id");
			gmail = scan.nextLine();
			per.setGmail(gmail);
		}

		this.accountNum = random.nextLong(1000000000000000l, 9999999999999999l);
		per.setAccountNum(accountNum);
		
			per.setGmail(gmail);

			if(gmail==person.getGmail()) {
				throw new DuplicateKeyException("DuplicateKeyException");
			}
		
			try{

		String query = "insert into bankuser (accountNum,balance,name,password,gmail ) values ('" + per.getAccountNum()
		+ "','" + per.getBalance() + "','" + per.getName() + "','" + per.getPassword() + "','" + per.getGmail()
		+ "')";
		
		
			jdbcTemplate.update(query);
			System.out.println("Your Account Number is: " + accountNum);

			}
			catch(DuplicateKeyException e) {
				System.err.println("Mail Id is already register");
			}
		
		
	}

	// This is the sign-in method for existing users
	@Override
	public Person signIn(String name, String password, String gmail) {
		System.out.println("Enter your name ");
		name = scan.nextLine();
		System.out.println("Enter your password");
		password = scan.nextLine();
		System.out.println("Enter Your gmail");
		gmail = scan.nextLine();

		String query = "select * from bankuser where name=? and password=? and gmail=?";

		RowMapper<Person> rowMap = new PersonRowMapper();
		Person obj = jdbcTemplate.queryForObject(query, rowMap, name, password, gmail);
		System.out.println("Got details");
		return obj;

	}

	// Method to withdraw money from the user's account
	@Override
	public double withdraw(Person person, double balance) {
		try (PreparedStatement ps = conn.prepareStatement("select balance from bankuser where accountNum=?")) {
			System.out.println("Enter your account number for withdrawal: ");
			long accountNum = scan.nextLong();
			ps.setLong(1, accountNum); // Set the account number in the query
			ResultSet rs = ps.executeQuery(); // Execute the query

			// Check if the account number matches the user's account number
			if (person.getAccountNum() != accountNum) {
				try {
					throw new AccountNumberNotMatchException(
							"Account number is not correct, please check the account number first");
				} catch (AccountNumberNotMatchException e) {
					e.printStackTrace(); // Print stack trace for debugging
				}
			} else {
				if (rs.next()) {
					double currentBalance = rs.getDouble("balance"); // Fetch the balance from the database
					System.out.println("Account found. Current balance: " + currentBalance);
					System.out.println();

					// Check if the balance is too low for a transaction
					if (currentBalance < 100) {
						System.out.println("Your balance is too low; you cannot do a transaction");
					} else {
						System.out.println("Enter withdrawal amount: ");
						balance = scan.nextDouble();

						// Check if withdrawal amount is greater than current balance
						if (balance > currentBalance) {
							System.out.println("Oops! It looks like you don't have enough funds for this withdrawal.");
							return person.getBalance(); // Return current balance if withdrawal fails
						}

						// Proceed with withdrawal if enough balance
						PreparedStatement ps1 = conn
								.prepareStatement("update bankuser set balance = balance - ? where accountNum = ?");
						ps1.setDouble(1, balance);
						ps1.setLong(2, person.getAccountNum());
						int rowsAffected = ps1.executeUpdate(); // Execute the update operation

						// If the update was successful, update the person's balance
						if (rowsAffected > 0) {
							person.setBalance(person.getBalance() - balance); // Update the person's balance
							System.out.println("Withdrawn: " + balance);
						} else {
							System.out.println("Error: Withdrawal could not be completed.");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Print stack trace for debugging
			System.err.println("Server error, sorry!!");
		}
		return person.getBalance(); // Return the updated balance
	}

	// Method to check the user's account balance
	@Override
	public void checkBalance(Person per, long accountNumber) {
		try (PreparedStatement ps = conn.prepareStatement("select balance from bankuser where accountNum=?")) {
			System.out.println("Enter the account number ");
			accountNumber = scan.nextLong(); // Get account number from user

			ps.setLong(1, accountNumber); // Set the account number in the query
			ResultSet rs = ps.executeQuery(); // Execute the query

			// Check if the account number matches the user's account number
			if (per.getAccountNum() == accountNumber && rs.next()) {
				System.out.println("Account found");
				System.out.println("Your account balance is: " + rs.getDouble("Balance")); // Display balance
			} else {
				try {
					throw new AccountNumberNotMatchException("Account number doesn't match user data");
				} catch (AccountNumberNotMatchException e) {
					e.printStackTrace(); // Print stack trace for debugging
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Print stack trace for debugging
		}
	}

	// Method to deposit money into the user's account
	public void deposit(Person per, double balance) {
		String query = "update bankuser set balance = balance + ? where accountNum=?";
		String accountNumQuery = "select * from bankuser where accountNum =? ";
				System.out.println("Enter account number");
			long newDepositAccount = scan.nextLong(); // Get account number for deposit
			
			RowMapper< Person> rowMap = new PersonRowMapper();
			jdbcTemplate.queryForObject(accountNumQuery,rowMap, newDepositAccount);
			

			// Verify that the user who has signed in must have their account number to
			// deposit money
//			jdbcTemplate.queryForList(query,balance,per.getAccountNum());
			
			int d=jdbcTemplate.update(query,500,5072420263423990l);
			
			
			if (per.getAccountNum() != newDepositAccount) {
				try {
					throw new AccountNumberNotMatchException(
							"Account number is incorrect; please check the account number");
				} catch (AccountNumberNotMatchException e) {
					e.printStackTrace(); // Print stack trace for debugging
				}
			} else if (per.getAccountNum() == newDepositAccount) {
				System.out.println("Enter deposit amount: ");
				balance = scan.nextDouble(); // Get deposit amount from user
				person.setBalance(balance); // Update person's balance
				person.setBalance(person.getBalance() + balance); // Update the person's balance
				System.out.println("You've successfully deposited your balance ");
			}
	}

	// Method to check if the password length is valid
	public boolean passwordLength(String passString) {
		return passString.length() > 7; // Return true if the password length is greater than 7 characters
	}

	public void with(double balance) {
		String query = "select * from bankuser where accountNum=?";
		System.out.println("Account Number: ");
		this.accountNum = scan.nextLong();

		RowMapper<Person> rowMap = new PersonRowMapper();
		Person obj = jdbcTemplate.queryForObject(query, rowMap);

	}

}
