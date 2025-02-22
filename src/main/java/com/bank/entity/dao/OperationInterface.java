package com.bank.entity.dao;

import com.bank.entity.Person;

public interface OperationInterface {
	void signUp(Person per);
	double withdraw(Person per, double balance);
	void checkBalance(Person per,long accountNumber);
	double deposit (Person per,double balance);
	public Person signIn(String name,String password,String gmail);
}
