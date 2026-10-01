package com.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AtmImplementation implements IAtm {

	Scanner scanner = new Scanner(System.in);

	public Connection createConnection() {
		Connection connection = null;

		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("POSTGRESQL_DATABASE_URL", "USER",
					"YOUR-PASSWORD");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	@Override
	public void createAccount() {
		Connection connection = createConnection();

		try {
			PreparedStatement insertPeparedStatement = connection
					.prepareStatement("INSERT INTO account VALUES (?,?,?,?,?,?,?)");

			System.out.println("ENTER ACCOUNT NUMBER: ");
			String accountNumber = scanner.next();
			insertPeparedStatement.setString(1, accountNumber);
			System.out.println("ENTER NAME: ");
			insertPeparedStatement.setString(2, scanner.next());
			System.out.println("ENTER PHONE NUMBER: ");
			insertPeparedStatement.setLong(3, scanner.nextLong());
			System.out.println("ENTER BANK NAME: ");
			insertPeparedStatement.setString(4, scanner.next());
			System.out.println("ENTER EMAIL: ");
			insertPeparedStatement.setString(5, scanner.next());
			System.out.println("ENTER PIN: ");
			insertPeparedStatement.setInt(6, scanner.nextInt());
			insertPeparedStatement.setDouble(7, 0.0);

			int rows = insertPeparedStatement.executeUpdate();
			if (rows > 0) {
				System.out.println("ACCOUNT CREATED SUCCESSFULLY\nYOUR ACCOUNT NUMBER: " + accountNumber);
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void depositAmount() {
		Connection connection = createConnection();

		System.out.println("ENTER ACCOUNT NUMBER:");
		String accountNumber = scanner.next();
		System.out.println("ENTER PIN:");
		int pin = scanner.nextInt();
		System.out.println("ENTER DEPOSIT AMOUNT:");
		double depositAmount = scanner.nextDouble();

		try {
			PreparedStatement depositPreparedStatement = connection
					.prepareStatement("UPDATE account SET balance=balance+? WHERE account_number=? AND pin=?");

			depositPreparedStatement.setDouble(1, depositAmount);
			depositPreparedStatement.setString(2, accountNumber);
			depositPreparedStatement.setInt(3, pin);

			int rows = depositPreparedStatement.executeUpdate();
			if (rows > 0) {
				System.out.println("AMOUNT DEPOSITED SUCCESSFULLY.");
			} else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void withdrawAmount() {
		Connection connection = createConnection();

		System.out.println("ENTER ACCOUNT NUMBER:");
		String accountNumber = scanner.next();
		System.out.println("ENTER PIN:");
		int pin = scanner.nextInt();
		System.out.println("ENTER WITHDRAW AMOUNT:");
		double withdrawAmount = scanner.nextDouble();

		try {
			PreparedStatement balancePreparedStatement = connection
					.prepareStatement("SELECT balance FROM account WHERE account_number=? AND pin=?");

			balancePreparedStatement.setString(1, accountNumber);
			balancePreparedStatement.setInt(2, pin);

			balancePreparedStatement.execute();

			double currentBalance = 0;

			ResultSet balanceResultSet = balancePreparedStatement.getResultSet();
			if (balanceResultSet.next()) {
				currentBalance = balanceResultSet.getDouble(1);
			} else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				return;
			}

			if (withdrawAmount <= currentBalance && withdrawAmount > 0) {
				PreparedStatement withdrawPreparedStatement = connection
						.prepareStatement("UPDATE account SET balance=balance-? WHERE account_number=? AND pin=?");

				withdrawPreparedStatement.setDouble(1, withdrawAmount);
				withdrawPreparedStatement.setString(2, accountNumber);
				withdrawPreparedStatement.setInt(3, pin);

				withdrawPreparedStatement.execute();
				System.out.println("AMOUNT WITHDRAW SUCCESSFULLY.");
			} else {
				System.out.println("INSUFFICIENT BALANCE.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkBalance() {
		Connection connection = createConnection();

		try {
			PreparedStatement balancePreparedStatement = connection
					.prepareStatement("SELECT balance FROM account WHERE account_number=? AND pin=?");

			System.out.println("ENTER ACCOUNT NUMBER:");
			balancePreparedStatement.setString(1, scanner.next());
			System.out.println("ENTER PIN:");
			balancePreparedStatement.setInt(2, scanner.nextInt());

			balancePreparedStatement.execute();

			ResultSet balanceResultSet = balancePreparedStatement.getResultSet();
			if (balanceResultSet.next()) {
				double currentBalance = balanceResultSet.getDouble(1);
				System.out.println("YOUR BALANCE: " + currentBalance);
			} else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayDetails() {
		Connection connection = createConnection();

		try {
			PreparedStatement detailsPreparedStatement = connection
					.prepareStatement("SELECT * FROM account WHERE account_number=? AND pin=?");

			System.out.println("ENTER ACCOUNT NUMBER:");
			detailsPreparedStatement.setString(1, scanner.next());
			System.out.println("ENTER PIN:");
			detailsPreparedStatement.setInt(2, scanner.nextInt());

			detailsPreparedStatement.execute();

			ResultSet detailsResultSet = detailsPreparedStatement.getResultSet();
			if (detailsResultSet.next()) {
				System.out.println("ACCOUNT NUMBER: " + detailsResultSet.getString(1) + "\n" + "NAME: "
						+ detailsResultSet.getString(2) + "\n" + "PHONE NUMBER: " + detailsResultSet.getLong(3) + "\n"
						+ "BANK NAME: " + detailsResultSet.getString(4) + "\n" + "EMAIL: "
						+ detailsResultSet.getString(5) + "\n" + "PIN: " + detailsResultSet.getInt(6) + "\n"
						+ "BALANCE: " + detailsResultSet.getDouble(7));
			} else {
				System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAccount() {
		Connection connection = createConnection();

		try {
			PreparedStatement deletePreparedStatement = connection
					.prepareStatement("DELETE FROM account WHERE account_number=? AND pin=?");

			System.out.println("ENTER ACCOUNT NUMBER:");
			deletePreparedStatement.setString(1, scanner.next());
			System.out.println("ENTER PIN:");
			deletePreparedStatement.setInt(2, scanner.nextInt());

			System.out.println("CONFIRM DO YOU WANT TO DELETE YOUR ACCOUNT:\n1=>YES | 2=>NO");
			int choice = scanner.nextInt();
			if (choice == 1) {
				int rows = deletePreparedStatement.executeUpdate();
				if (rows > 0) {
					System.out.println("YOUR ACCOUNT IS DELETED.");
				} else {
					System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				}
			} else {
				System.out.println("ACCOUNT DELETION CANCELED.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDetails() {
		Connection connection = createConnection();

		System.out.println("ENTER ACCOUNT NUMBER: ");
		String accountNumber = scanner.next();
		System.out.println("ENTER PIN: ");
		int pin = scanner.nextInt();

		int rows = 0;

		System.out.println("ENTER AN OPTION TO UPDATE:\n1.NAME\n2.PHONE NUMBER\n3.EMAIL\n4.PIN");
		int choice = scanner.nextInt();

		try {
			switch (choice) {
			case 1:
				PreparedStatement nameUpdatePreparedStatement = connection
						.prepareStatement("UPDATE account SET name=? WHERE account_number=? AND pin=?");

				System.out.println("ENTER NEW NAME:");
				nameUpdatePreparedStatement.setString(1, scanner.next());
				nameUpdatePreparedStatement.setString(2, accountNumber);
				nameUpdatePreparedStatement.setInt(3, pin);

				rows = nameUpdatePreparedStatement.executeUpdate();
				if (rows > 0) {
					System.out.println("NAME UPDATED SUCCESSFULLY.");
				} else {
					System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				}
				break;

			case 2:
				PreparedStatement phoneNumberUpdatePreparedStatement = connection
						.prepareStatement("UPDATE account SET phone_number=? WHERE account_number=? AND pin=?");

				System.out.println("ENTER NEW PHONE NUMBER:");
				phoneNumberUpdatePreparedStatement.setLong(1, scanner.nextLong());
				phoneNumberUpdatePreparedStatement.setString(2, accountNumber);
				phoneNumberUpdatePreparedStatement.setInt(3, pin);

				rows = phoneNumberUpdatePreparedStatement.executeUpdate();
				if (rows > 0) {
					System.out.println("PHONE NUMBER UPDATED SUCCESSFULLY.");
				} else {
					System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				}
				break;

			case 3:
				PreparedStatement emailUpdatePreparedStatement = connection
						.prepareStatement("UPDATE account SET email=? WHERE account_number=? AND pin=?");

				System.out.println("ENTER NEW EMAIL:");
				emailUpdatePreparedStatement.setString(1, scanner.next());
				emailUpdatePreparedStatement.setString(2, accountNumber);
				emailUpdatePreparedStatement.setInt(3, pin);

				rows = emailUpdatePreparedStatement.executeUpdate();
				if (rows > 0) {
					System.out.println("EMAIL UPDATED SUCCESSFULLY.");
				} else {
					System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				}
				break;

			case 4:
				PreparedStatement pinUpdatePreparedStatement = connection
						.prepareStatement("UPDATE account SET pin=? WHERE account_number=? AND pin=?");

				System.out.println("ENTER NEW PIN:");
				pinUpdatePreparedStatement.setInt(1, scanner.nextInt());
				pinUpdatePreparedStatement.setString(2, accountNumber);
				pinUpdatePreparedStatement.setInt(3, pin);

				rows = pinUpdatePreparedStatement.executeUpdate();
				if (rows > 0) {
					System.out.println("PIN UPDATED SUCCESSFULLY.");
				} else {
					System.out.println("INVALID ACCOUNT NUMBER OR PIN.");
				}
				break;

			default:
				System.out.println("INVALID OPTION SELECTED");
				break;
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void transferAmount() {
		Connection connection = createConnection();

		System.out.println("ENTER SENDER ACCOUNT NUMBER:");
		String senderAccountNumber = scanner.next();
		System.out.println("ENTER SENDER PIN:");
		int senderPin = scanner.nextInt();
		System.out.println("ENTER TRANSFER AMOUNT:");
		double transferAmount = scanner.nextDouble();
		System.out.println("ENTER RECEIVER ACCOUNT NUMBER:");
		String receiverAccountNumber = scanner.next();

		try {
			PreparedStatement balancePreparedStatement = connection
					.prepareStatement("SELECT balance FROM account WHERE account_number=? AND pin=?");

			balancePreparedStatement.setString(1, senderAccountNumber);
			balancePreparedStatement.setInt(2, senderPin);

			balancePreparedStatement.execute();

			ResultSet balanceResultSet = balancePreparedStatement.getResultSet();
			double currentBalance = 0;
			if (balanceResultSet.next()) {
				currentBalance = balanceResultSet.getDouble(1);
			} else {
				System.out.println("INVALID SENDER ACCOUNT NUMBER OR PIN.");
				return;
			}

			if (transferAmount <= currentBalance && transferAmount > 0) {
				PreparedStatement depositPreparedStatement = connection
						.prepareStatement("UPDATE account SET balance=balance+? WHERE account_number=?");

				depositPreparedStatement.setDouble(1, transferAmount);
				depositPreparedStatement.setString(2, receiverAccountNumber);

				int rows = depositPreparedStatement.executeUpdate();
				if (rows > 0) {
					PreparedStatement withdrawPreparedStatement = connection
							.prepareStatement("UPDATE account SET balance=balance-? WHERE account_number=? AND pin=?");

					withdrawPreparedStatement.setDouble(1, transferAmount);
					withdrawPreparedStatement.setString(2, senderAccountNumber);
					withdrawPreparedStatement.setInt(3, senderPin);

					withdrawPreparedStatement.execute();

					System.out.println("AMOUNT TRANSFERED SUCCESSFULLY.");
				} else {
					System.out.println("INVALID RECEIVER ACCOUNT NUMBER.");
				}
			} else {
				System.out.println("INSUFFICIENT BALANCE.");
			}

			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
