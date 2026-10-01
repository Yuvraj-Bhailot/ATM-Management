package com.atm;

public class InvalidChoiceException extends RuntimeException{
	@Override
	public String getMessage() {
		return "Invalid choice selected, Choose a proper option from the list";
	}
}
