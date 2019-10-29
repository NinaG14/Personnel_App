package com.app.commline;

import java.sql.SQLException;
import java.util.Scanner;

import com.db.operations.Query;

public class App {

	public static void main(String[] args) throws SQLException {
		// Create new table whenever application is started
		Query.createTable();
		Scanner in = new Scanner(System.in);
		int choice,val;
		do {
			// Display the menu
			System.out.println("1\t Add");
			System.out.println("2\t Edit");
			System.out.println("3\t Delete");
			System.out.println("4\t Display");
			System.out.println("5\t Count");
			System.out.println("6\t Add person from XML");
			System.out.println("7\t Exit");

			System.out.println("Please enter your choice:");
			
			do {
				while (!in.hasNextInt()) {
					System.out.println("Not a valid id!");
					System.out.println("Please enter a valid Id");
					in.next();
				}
				val = in.nextInt();
			} while (val <= 0);
			// Get user's choice
			choice = val;
			switch (choice) {
			case 1:
				Query.add();
				break;
			case 2:
				Query.edit();
				break;
			case 3:
				Query.delete();
				break;
			case 4:
				Query.display();
				break;
			case 5:
				Query.count();
				break;
			case 6:
				Query.addPersonXML("rec.xml");		
				break;
			case 7:
				System.exit(0);
				break;

			default:
				System.out.println("Invalid choice");
				continue;
			}// end of switch
		} while (choice != 7);
		in.close();
	}
}
