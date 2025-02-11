package com.bank.entity;

public class Person {

	private long accountNum=0l;
	private double balance = 0.0;
	private String name;
	private String password;
	private String gmail;

	public String getGmail() {
		return gmail;
	}

	public void setGmail(String gmail) {
		this.gmail = gmail;
	}
	public void setAccountNum(long accountNum) {
		this.accountNum = accountNum;
	}
	public long getAccountNum() {
		return accountNum;
	}

	

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person(double balance, String name, String password, String gmail) {
		super();
		this.gmail = gmail;
//		this.accountNum = accountNum;
		this.balance = balance;
		this.name = name;
		this.password = password;
	}

	public Person(long accountNum, double balance, String name, String password, String gmail) {
		this.gmail = gmail;
		this.accountNum = accountNum;
		this.balance = balance;
		this.name = name;
		this.password = password;
	}

	public Person() {
		super();
	}

	
}
