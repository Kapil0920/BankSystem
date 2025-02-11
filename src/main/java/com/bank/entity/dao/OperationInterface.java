package com.bank.entity.dao;

import com.bank.entity.Person;

public interface OperationInterface {
	Person signIn(String name,String password,String gmail);
	void signUp(String userName,String password,String gmail);
	double withdraw(Person per, double balance);
	void checkBalance(Person per,long accountNumber);
	double deposit (Person per,double balance);

}
