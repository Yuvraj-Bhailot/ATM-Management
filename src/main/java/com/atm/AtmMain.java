package com.atm;

import java.util.Scanner;

public class AtmMain {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		IAtm atm = new AtmImplementation();
		
		System.out.println("================================================");
		System.out.println("|                   A T M                      | ");
		System.out.println("================================================");
		System.out.println("|              ________________                |");
		System.out.println("|             |                |               |");
		System.out.println("|             |     A T M      |               |");
		System.out.println("|             |----------------|               |");
		System.out.println("|             |   __________   |               |");
		System.out.println("|             |  |          |  |               |");
		System.out.println("|             |  | WELCOME  |  |               |");
		System.out.println("|             |  |__________|  |               |");
		System.out.println("|             |  \\          \\  |               |");
		System.out.println("|             |   [1] [2] [3]  |               |");
		System.out.println("|             |   [4] [5] [6]  |               |");
		System.out.println("|             |   [7] [8] [9]  |               |");
		System.out.println("|             |   [*] [0] [#]  |               |");
		System.out.println("|             |________________|               |");
		System.out.println("================================================");
		
		menu:
		while(true) {
			System.out.println("WELCOME ALL");
			System.out.println("1.CREATE AN ACCOUNT\n"
							 + "2.DEPOSIT\n"
							 + "3.WITHDRAW\n"
							 + "4.CHECK BALANCE\n"
							 + "5.DISPLAY DETAILS\n"
							 + "6.DELETE ACCOUNT\n"
							 + "7.UPDATION\n"
							 + "8.TRANSFER AMOUNT\n"
							 + "10.EXIT");
			System.out.println("SELECT ONE OPTION: ");
			
			switch (scanner.nextInt()) {
			case 1:
				atm.createAccount();
				break;
				
			case 2:
				atm.depositAmount();
				break;
				
			case 3:
				atm.withdrawAmount();
				break;
				
			case 4:
				atm.checkBalance();
				break;
				
			case 5:
				atm.displayDetails();
				break;
				
			case 6:
				atm.deleteAccount();
				break;
				
			case 7:
				atm.updateDetails();
				break;
				
			case 8:
				atm.transferAmount();
				break;
				
			case 10:
				System.out.println("THANK YOU");
				break menu;

			default:
				try {
					throw new InvalidChoiceException();
				} catch (InvalidChoiceException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		
		scanner.close();
		
	}
	
}
