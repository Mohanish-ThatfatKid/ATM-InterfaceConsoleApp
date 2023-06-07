package in.proj.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtil {
	
	private JdbcUtil() {
		// TODO Auto-generated constructor stub
	}
	
	static {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws IOException, SQLException {

		FileInputStream fis = new FileInputStream("src\\in\\proj\\properties\\db.properties");
		Properties prop = new Properties();
		prop.load(fis);
		String url = prop.getProperty("url");
		String username = prop.getProperty("user");
		String password = prop.getProperty("password");
		
		Connection connection = DriverManager.getConnection(url,username,password);
		return connection;
	}
	
	public static void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

}
