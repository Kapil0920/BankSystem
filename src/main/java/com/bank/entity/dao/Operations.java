package com.bank.entity.dao;
// This is Spring Boot

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import  org.springframework.dao.DuplicateKeyException;
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
		Person obj=null;
		System.out.println("Enter your name ");
		name = scan.nextLine();
		System.out.println("Enter your password");
		password = scan.nextLine();
		System.out.println("Enter Your gmail");
		gmail = scan.nextLine();
		try {
		String query = "select * from bankuser where name=? and password=? and gmail=?";
		RowMapper<Person> rowMap = new PersonRowMapper();
		obj = jdbcTemplate.queryForObject(query, rowMap, name, password, gmail);
		System.out.println("Got details");

		return obj;

		}
		catch(EmptyResultDataAccessException e) {
			System.err.println("No User Found");
			
		}
		return obj;
	}

	// Method to withdraw money from the user's account
	@Override
	public double withdraw(Person person, double balance) {
		String queryForShowBalance = "select balance from bankuser where accountNum=?";
		String queryForUpdateBalance = "update bankuser set balance = balance - ? where accountNum = ?";
		String queryForCheckAccountNumber = "select accountNum from bankuser where name =? And password=? And gmail=?";
		
		System.out.println("Enter Account Number For Withdrawal");
		long accountNum = scan.nextLong();
		
		try {
			
		// For retrieving account number from database
		Long IsAccountNumberPresent= jdbcTemplate.queryForObject(queryForCheckAccountNumber, Long.class,person.getName(),person.getPassword(),person.getGmail());
		
		if(IsAccountNumberPresent.equals(accountNum)) {
			System.out.println("Account Number Match:");
			
			Double showBalance  = jdbcTemplate.queryForObject(queryForShowBalance, Double.class, accountNum);
			
				if(showBalance>1000) {
					
					System.out.println("You have "+showBalance);
					
					System.out.println("Enter Amount For WITHDRAW balance");
					balance = scan.nextInt();
					
					if(balance>showBalance) {
						System.err.println("InsufficientWithdrawRequestException");
						System.out.println("Your Balance is lower than you want to withdraw");
						return balance;
					}
					
					jdbcTemplate.update(queryForUpdateBalance,balance,accountNum);
					person.setBalance(showBalance-balance);
	
					System.out.println("Your left ammount is: "+person.getBalance());
					
					System.out.println("You've successfully done your transaction");
				}
				else {
					System.err.println("Oops! Your Balance is too low: "+showBalance);
					return balance;
				}
			}
			else {
				throw new AccountNumberNotMatchException("AccountNumberNotMatchException");
			}
		}
		catch(TransientDataAccessException e) {
			e.printStackTrace();
		}
		catch(EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		catch(AccountNumberNotMatchException e) {
			e.printStackTrace();
		}
		
		
		
		return balance;
		
	}

	
	
	
	// Method to check the user's account balance
	@Override
	public void checkBalance(Person per, long accountNumber) {
		
		String query = "select balance from bankuser where accountNum=?";
		String queryForAccountNum = "SELECT accountNum FROM bankuser WHERE name = ? AND password = ? AND gmail = ?";

		System.out.println("Enter Account Number For Check Balance");
		accountNumber = scan.nextLong();
		


		try {
			
			Long checkAccountNumber  = jdbcTemplate.queryForObject(queryForAccountNum, Long.class, per.getName(),per.getPassword(),per.getGmail());
		
			Double checkBalance=jdbcTemplate.queryForObject(query, Double.class, accountNumber);
		
		if(checkAccountNumber.equals( accountNumber)) {
			System.out.println("Account Number Found.");
			System.out.println("You have: "+checkBalance);
			
		}
		else {
			
			throw new AccountNumberNotMatchException("AccountNumberNotMatchException");
		}
		

		}
		catch(TransientDataAccessException e) {
			e.printStackTrace();
		}
		catch(EmptyResultDataAccessException e ) {
			e.printStackTrace();
		}
		catch(AccountNumberNotMatchException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	@Override
	public void deposit(Person per, double balance) {
	    String query = "UPDATE bankuser SET balance = balance + ? WHERE accountNum = ?";
	    String select = "SELECT accountNum FROM bankuser WHERE name = ? AND password = ? AND gmail = ?";

	    System.out.println("Enter account number");
	    long newAccount = scan.nextLong(); // Get the account number for deposit

	    try {
	        // Use parameterized queries to prevent SQL injection
	        @SuppressWarnings("deprecation")
			Long accountNum = jdbcTemplate.queryForObject(select, new Object[]{per.getName(), per.getPassword(), per.getGmail()}, Long.class);
	        System.out.println("Account Number is: "+accountNum);
	        // Check if the account number matches the one provided
	        	if(accountNum ==newAccount) {
	            System.out.println("Account Found.");
	            System.out.println("Enter Deposit amount: ");
	            balance = scan.nextDouble(); // Get deposit amount from user

	            // Update the balance in the database
	            jdbcTemplate.update(query, balance, accountNum);
	            System.out.println("Successfully deposited " + balance + " into account number " + accountNum);
	        } 
	        	else {
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
