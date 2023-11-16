package prometheus.Controllers;

import java.awt.Color;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import prometheus.Models.Rectangle;

/**
 * Class to read/write rectangles to SQLite database
 * 
 * @author Kenechukwu Okoye
 * @version 2023.11.15
 */
public class SQLHandler {

	private ArrayList<Rectangle> drawnRectangles = new ArrayList<Rectangle>();
	private Connection connection;
	private Statement stmt;
	private String directoryName;

	/**
	 * Constructor for SQLHandler class
	 * 
	 * This constructor is for loading rectangles from a previous design which
	 * already exist in an SQLite database
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @param directoryName file path to the project database
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public SQLHandler(String directoryName) throws SQLException, ClassNotFoundException {
		this.directoryName = directoryName;
		readRectanglesFromDatabase();
	}

	/**
	 * Constructor for SQLHandler class
	 * 
	 * This constructor is for writing rectangles to an SQLite database
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @param drawnRectangles information about components being converted to source
	 *                        code
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public SQLHandler(ArrayList<Rectangle> drawnRectangles) throws SQLException, ClassNotFoundException {
		// Create SQLite database based on label given to first rectangle (jframe) and
		// write all rectangle information
		// to the database
		this.drawnRectangles = drawnRectangles;

		String delimeter = "\\";
		String fileName = "";
		String filePath = new File(fileName).getAbsolutePath();
		if (drawnRectangles.get(0).getLabel() != null && drawnRectangles.get(0).getLabel().length() > 0) {
			fileName = drawnRectangles.get(0).getLabel();
		} else {
			fileName = "Prometheus";
		}
		String directory = filePath + delimeter + "Flames" + delimeter + fileName + delimeter;
		directoryName = directory + removeSpacesInString(fileName);

		writeRectanglesToDatabase();
	}

	/**
	 * Function to write all drawn rectangles to the SQLite database
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void writeRectanglesToDatabase() throws SQLException, ClassNotFoundException {
		connection = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager
				.getConnection(String.format("jdbc:sqlite:%s.db", removeSpacesInString(directoryName)));
		connection.setAutoCommit(true);

		stmt = connection.createStatement();

		String sql = "DROP TABLE IF EXISTS PROMETHEUS;";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS PROMETHEUS(COMPONENT_TYPE TEXT, COMPONENT_LABEL TEXT, RED INT, GREEN INT, BLUE INT, ALPHA INT, WIDTH INT, HEIGHT INT, X INT, Y INT);";
		stmt.executeUpdate(sql);

		for (Rectangle rect : drawnRectangles) {
			sql = String.format(
					"INSERT INTO PROMETHEUS VALUES('%s', '%s', %d, %d, %d, %d, %d, %d, %d, %d);",
					rect.getComponent(), rect.getLabel(), rect.getColor().getRed(), rect.getColor().getGreen(),
					rect.getColor().getBlue(), rect.getColor().getAlpha(), rect.getWidth(), rect.getHeight(),
					rect.getX(), rect.getY());
			stmt.executeUpdate(sql);
		}
		stmt.close();
		connection.close();
	}

	/**
	 * Function to read all rectangles from an existing SQLite database
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void readRectanglesFromDatabase() throws SQLException, ClassNotFoundException {
		connection = null;
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager
				.getConnection(String.format("jdbc:sqlite:%s.db", removeSpacesInString(directoryName)));

		stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM PROMETHEUS;");

		while (rs.next()) {
			Rectangle rect = new Rectangle(
					rs.getString("COMPONENT_TYPE"),
					rs.getInt("X"), rs.getInt("Y"),
					rs.getInt("X") + rs.getInt("WIDTH"),
					rs.getInt("Y") + rs.getInt("HEIGHT"));
			Color color = new Color(
					rs.getInt("RED"), rs.getInt("GREEN"),
					rs.getInt("BLUE"), rs.getInt("ALPHA"));
			rect.setColor(color);
			rect.setLabel(rs.getString("COMPONENT_LABEL"));
			drawnRectangles.add(rect);
		}
		rs.close();
		stmt.close();
		connection.close();
	}

	/**
	 * Function to replace all spaces in a string with underscores
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @param name
	 * @return String
	 */
	private String removeSpacesInString(String name) {
		return name.replaceAll(" ", "_");
	}

	/**
	 * Getter method for drawnRectangles
	 * 
	 * @author Kenechukwu Okoye
	 * @version 2023.11.15
	 * 
	 * @return all rectangles that have been passed to
	 *         the SQLHandler instance
	 */
	public ArrayList<Rectangle> getRectangles() {
		return drawnRectangles;
	}
}