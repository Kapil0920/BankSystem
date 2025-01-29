package com.exception;


class InvalidUserDetails extends Exception{
	
	public void printExcption() {
		System.err.println("Invalid User Details\nUser Not Found ");
	}
}

public class InvalidRequestException extends Exception{
	
	public void printException() {
		System.out.println( "InvalidInputException\nYou entered the wrong input please enter the valid input");
	}
}

