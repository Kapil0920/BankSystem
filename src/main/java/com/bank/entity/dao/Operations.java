package com.bank.entity.dao;

//This is a Spring Boot component that handles banking operations
import java.util.Random;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.TransientDataAccessException;
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
	private JdbcTemplate jdbcTemplate; // JdbcTemplate for database operations

	// Getter for JdbcTemplate
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	// Setter for JdbcTemplate
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Default constructor
	public Operations() {
		super();
	}

	// Constructor that initializes JdbcTemplate with a DataSource
	@Autowired
	public Operations(DataSource data) {
		jdbcTemplate = new JdbcTemplate(data);
	}

	// Method for user sign-up
	@Override
	public void signUp(Person per) {
		System.out.println("Enter name: ");
		String name = scan.nextLine();
		per.setName(name); // Set the name in the Person object

		// Loop to ensure valid password length
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

		// Validate the email format
		while (!gmail.contains("@") || !gmail.contains("gmail.com")) {
			System.out.println("Please enter the valid Email id");
			gmail = scan.nextLine();
			per.setGmail(gmail);
		}

		this.accountNum = random.nextLong(1000000000000000l, 9999999999999999l);
		per.setAccountNum(accountNum);

		per.setGmail(gmail);

		if (gmail == person.getGmail()) {
			throw new DuplicateKeyException("DuplicateKeyException");
		}

		try {
			// SQL query to insert a new user into the database
			String query = "INSERT INTO bankuser (accountNum, balance, name, password, gmail) VALUES ('"
					+ per.getAccountNum() + "','" + per.getBalance() + "','" + per.getName() + "','" + per.getPassword()
					+ "','" + per.getGmail() + "')";

			// Execute the insert query
			jdbcTemplate.update(query);
			System.out.println("Your Account Number is: " + accountNum);

		} catch (DuplicateKeyException e) {
			System.err.println("Mail Id is already registered");
		}
	}

	// Method for user sign-in
	@Override
	public Person signIn(String name, String password, String gmail) {
		Person obj = null; // Object to hold the retrieved user
		System.out.println("Enter your name ");
		name = scan.nextLine();
		System.out.println("Enter your password");
		password = scan.nextLine();
		System.out.println("Enter Your gmail");
		gmail = scan.nextLine();
		try {
			// SQL query to find the user by name, password, and email
			String query = "SELECT * FROM bankuser WHERE name=? AND password=? AND gmail=?";
			RowMapper<Person> rowMap = new PersonRowMapper(); // RowMapper to map the result set to Person object
			obj = jdbcTemplate.queryForObject(query, rowMap, name, password, gmail); // Execute the query
			System.out.println("Got details");

			return obj; // Return the retrieved user object

		} catch (EmptyResultDataAccessException e) {
			System.err.println("No User Found");
		}
		return obj; // Return null if no user is found
	}

	// Method to withdraw money from the user's account
	@Override
	public double withdraw(Person person, double balance) {
		String queryForShowBalance = "SELECT balance FROM bankuser WHERE accountNum=?";
		String queryForUpdateBalance = "UPDATE bankuser SET balance = balance - ? WHERE accountNum = ?";
		String queryForCheckAccountNumber = "SELECT accountNum FROM bankuser WHERE name =? AND password=? AND gmail=?";

		System.out.println("Enter Account Number For Withdrawal");
		long accountNum = scan.nextLong(); // Get the account number for withdrawal

		try {
			// Retrieve the account number from the database
			Long isAccountNumberPresent = jdbcTemplate.queryForObject(queryForCheckAccountNumber, Long.class,
					person.getName(), person.getPassword(), person.getGmail());

			// Check if the provided account number matches the retrieved account number
			if (isAccountNumberPresent.equals(accountNum)) {
				System.out.println("Account Number Match:");

				// Retrieve the current balance for the account
				Double showBalance = jdbcTemplate.queryForObject(queryForShowBalance, Double.class, accountNum);

				// Check if the balance is sufficient for withdrawal
				if (showBalance > 1000) {
					System.out.println("You have " + showBalance);

					System.out.println("Enter Amount For WITHDRAWAL");
					balance = scan.nextDouble(); // Get the withdrawal amount from user

					// Check if the withdrawal amount exceeds the available balance
					if (balance > showBalance) {
						System.err.println("InsufficientWithdrawRequestException");
						System.out.println("Your Balance is lower than you want to withdraw");
						return balance; // Return the balance without making a withdrawal
					}

					// Update the balance in the database
					jdbcTemplate.update(queryForUpdateBalance, balance, accountNum);
					person.setBalance(showBalance - balance); // Update the person's balance

					System.out.println("Your remaining amount is: " + person.getBalance());
					System.out.println("You've successfully completed your transaction");
				} else {
					System.err.println("Oops! Your Balance is too low: " + showBalance);
					return balance; // Return the balance if insufficient
				}
			} else {
				// Throw a custom exception if the account number does not match
				throw new AccountNumberNotMatchException("AccountNumberNotMatchException");
			}
		} catch (TransientDataAccessException e) {
			e.printStackTrace(); // Handle transient data access exceptions
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace(); // Handle cases where no result is found
		} catch (AccountNumberNotMatchException e) {
			e.printStackTrace(); // Handle custom account number mismatch exceptions
		}

		return balance; // Return the balance at the end of the method
	}

	// Method to check the user's account balance
	@Override
	public void checkBalance(Person per, long accountNumber) {
		String query = "SELECT balance FROM bankuser WHERE accountNum=?";
		String queryForAccountNum = "SELECT accountNum FROM bankuser WHERE name = ? AND password = ? AND gmail = ?";

		System.out.println("Enter Account Number For Check Balance");
		accountNumber = scan.nextLong(); // Get the account number for balance check

		try {
			// Retrieve the account number from the database
			Long checkAccountNumber = jdbcTemplate.queryForObject(queryForAccountNum, Long.class, per.getName(),
					per.getPassword(), per.getGmail());

			// Retrieve the balance for the provided account number
			Double checkBalance = jdbcTemplate.queryForObject(query, Double.class, accountNumber);

			// Check if the retrieved account number matches the provided account number
			if (checkAccountNumber.equals(accountNumber)) {
				System.out.println("Account Number Found.");
				System.out.println("You have: " + checkBalance); // Display the balance
			} else {
				// Throw a custom exception if the account number does not match
				throw new AccountNumberNotMatchException("AccountNumberNotMatchException");
			}
		} catch (TransientDataAccessException e) {
			e.printStackTrace(); // Handle transient data access exceptions
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace(); // Handle cases where no result is found
		} catch (AccountNumberNotMatchException e) {
			e.printStackTrace(); // Handle custom account number mismatch exceptions
		} catch (Exception e) {
			e.printStackTrace(); // Handle any other exceptions
		}
	}

	// Method to deposit money into the user's account
	@Override
	public void deposit(Person per, double balance) {
		String query = "UPDATE bankuser SET balance = balance + ? WHERE accountNum = ?";
		String select = "SELECT accountNum FROM bankuser WHERE name = ? AND password = ? AND gmail = ?";

		System.out.println("Enter account number");
		long newAccount = scan.nextLong(); // Get the account number for deposit

		try {
			// Retrieve the account number from the database
			Long accountNum = jdbcTemplate.queryForObject(select, Long.class, per.getName(), per.getPassword(),
					per.getGmail());
			System.out.println("Account Number is: " + accountNum);
			// Check if the account number matches the one provided
			if (accountNum == newAccount) {
				System.out.println("Account Found.");
				System.out.println("Enter Deposit amount: ");
				balance = scan.nextDouble(); // Get deposit amount from user

				// Update the balance in the database
				jdbcTemplate.update(query, balance, accountNum);
				System.out.println("Successfully deposited " + balance + " into account number " + accountNum);
			} else {
				System.err.println("Account number does not match or does not exist.");
			}
		} catch (EmptyResultDataAccessException e) {
			System.err.println("No matching account found for the provided credentials.");
		} catch (DataAccessException e) {
			System.err.println("An error occurred while accessing the database: " + e.getMessage());
		}
	}

	// Method to check if the password length is valid
	public boolean passwordLength(String passString) {
		return passString.length() > 7; // Return true if the password length is greater than 7 characters
	}
}
