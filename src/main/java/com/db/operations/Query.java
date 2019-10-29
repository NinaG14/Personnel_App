package com.db.operations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.app.commline.*;

public class Query {
	// Generalised SQL queries
	static String SelectQuery = "select * from PERSON";
	static String DropQuery = "Drop table PERSON";
	static String CountQuery = "SELECT COUNT(id) FROM PERSON";
	private static final String createTableSQL = "CREATE TABLE PERSON(id int primary key, firstname varchar(255),surname varchar(255))";

	// H2 Database connection variables
	private static final String DB_DRIVER = "org.h2.Driver";
	// private static final String DB_DRIVER ="jdbc:sqlite:C:/Users/naush/Documents/MyFiles/comm_java.db";
	private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
	private static final String DB_USER = "";
	private static final String DB_PASSWORD = "";

	// Establishing DB connectivity using JDBC
	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dbConnection;
	}

	// Tracing SQL exception during connection
	public static void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
				Throwable t = ex.getCause();
				while (t != null) {
					System.out.println("Cause: " + t);
					t = t.getCause();
				}
			}
		}
	}
	/* DB Queries-Create Table,Add,Delete,Edit and Display records */

	// Create Table Query
	public static void createTable() throws SQLException {
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		DatabaseMetaData dbm = connection.getMetaData();
		ResultSet tables = dbm.getTables(null, null, "PERSON", null);
		if (tables.next()) {
			// Existing table is dropped whenever application is launched.
			statement.executeUpdate(DropQuery);
		} else {
			statement.executeUpdate(createTableSQL);
			System.out.println("Table created successfully");
		}
	}

	// Add records to table
	public static void add() throws SQLException {
		Personnel per = new Personnel();
		int id;
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		Scanner in = new Scanner(System.in);
		System.out.println("Input id,first name,last name");
		// Validating id field to be a positive integer value
		do {
			while (!in.hasNextInt()) {
				System.out.println("Not a valid id!");
				System.out.println("Please enter a valid Id");
				in.next();
			}
			id = in.nextInt();
		} while (id <= 0);
		per.id = id;
		per.setFname(in.next());
		per.setLname(in.next());
		ResultSet rs = statement.executeQuery("Select * from PERSON WHERE id='" + per.id + "'");

		// Checking if record already exists in table
		if (rs.next()) {
			System.out.println("Record with id " + per.id + " already exists \n");
		} else {
			statement.executeUpdate(
					"INSERT INTO PERSON VALUES('" + per.id + "', '" + per.getFname() + "','" + per.getLname() + "')");
			// in.close();
			System.out.println("All values were inserted successfully");
		}

	}

	// Adding records from XML file
	@SuppressWarnings("restriction")
	public static void addPersonXML(String personXMLFile) throws SQLException {
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		try {
			File personFile = new File(personXMLFile);

			@SuppressWarnings("restriction")
			JAXBContext jaxbContext = JAXBContext.newInstance(Persons.class);

			@SuppressWarnings("restriction")
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Persons pers = (Persons) jaxbUnmarshaller.unmarshal(personFile);
			// Read XML
			for (Personnel per : pers.getPersons()) {

				ResultSet rs = statement.executeQuery("Select * from PERSON WHERE id='" + per.id + "'");
				if (rs.next()) {
					System.out.println("Record with id " + per.id + " already exists \n");
				} else {
					statement.executeUpdate("INSERT INTO PERSON VALUES('" + per.id + "', '" + per.getFname() + "','"
							+ per.getLname() + "')");

					System.out.println("All values were inserted successfully");

				}
			} // for loop

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	//Edit records
	public static void edit() throws SQLException {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter id of person whose record needs to be edited:");
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		Personnel per = new Personnel();
		int id;
		do {
			while (!in.hasNextInt()) {
				System.out.println("Not a valid id!");
				System.out.println("Please enter a valid Id");
				in.next();
			}
			id = in.nextInt();
		} while (id <= 0);
		per.id = id;
		ResultSet rs = statement.executeQuery("Select * from PERSON WHERE id='" + per.id + "'");
		if (rs.next()) {	
		System.out.println("Enter new first name and last name");
		per.setFname(in.next());
		per.setLname(in.next());
		statement.executeUpdate("UPDATE PERSON SET firstname=  '" + per.getFname() + "',surname='" + per.getLname()
				+ "' WHERE id='" + per.id + "'");
		System.out.println("All values were edited successfully");
		}
		else{
			
			System.out.println("No such record found ");
		}
	}
    //Delete records
	public static void delete() throws SQLException {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter id of person whose record needs to be deleted:");
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		Personnel per = new Personnel();
		int id;
		do {
			while (!in.hasNextInt()) {
				System.out.println("Not a valid id!");
				System.out.println("Please enter a valid Id");
				in.next();
			}
			id = in.nextInt();
		} while (id <= 0);
		per.id = id;
		ResultSet rs = statement.executeQuery("Select * from PERSON WHERE id='" + per.id + "'");
		if (rs.next()) {	
		while (rs.next()) {
			System.out.println("Id FirstName LastName");
			System.out.println(
					"" + rs.getInt("id") + "    " + rs.getString("firstname") + "  " + rs.getString("surname"));
		}

		System.out.println("Are you sure you want this record to be deleted (y/n):");
		String choice = in.next().trim().toLowerCase();
		if (choice.equals("y")) {
			statement.executeUpdate("Delete from PERSON WHERE id='" + per.id + "'");
			System.out.println("All values were Deleted successfully");
		} else {
			return;
		}
		}
	 else {
		System.out.println("No such record found ");
	}

	}

	//Count number of records in DB
	public static void count() throws SQLException {
		Scanner in = new Scanner(System.in);
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM PERSON");
		rs.next();
		int count = rs.getInt("rowcount");
		rs.close();
		System.out.println("There are " + count + " records in the database");
	}

    //Display records in DB	
	public static void display() throws SQLException {
		Scanner in = new Scanner(System.in);
		System.out.println("1\t Display all records");
		System.out.println("2\t Display selective records");
		Connection connection = getDBConnection();
		Statement statement = connection.createStatement();

		System.out.println("Please enter choice");
		int choice = in.nextInt();

		if (choice == 1) {
			ResultSet rs = statement.executeQuery(SelectQuery);
			System.out.println("Id FirstName LastName");
			while (rs.next()) {
				System.out.println(
						"  " + rs.getInt("id") + "  " + rs.getString("firstname") + "  " + rs.getString("surname"));
			}

			return;
		} else {
			Personnel per = new Personnel();
			System.out.println("Enter id");
			per.id = in.nextInt();
			ResultSet rs = statement.executeQuery("Select * from PERSON WHERE id='" + per.id + "'");
			if (rs.next()) {
				System.out.println("Id FirstName LastName");
				System.out.println(
						"  " + rs.getInt("id") + "  " + rs.getString("firstname") + "  " + rs.getString("surname"));
			} else {
				System.out.println("No such record found ");
			}

		}

	}

}
