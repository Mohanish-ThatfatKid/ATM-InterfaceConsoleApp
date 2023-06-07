package in.proj.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {
	private IdGenerator() {
		// TODO Auto-generated constructor stub
	}
	
	private static Connection con = null;
	private static PreparedStatement pstmt = null;
	private static ResultSet rs = null;
	private static String url="jdbc:mysql:///bankatm";
	private static String username = "root";
	private static String password = "mohanishroot";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 con = DriverManager.getConnection(url,username,password);
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String genrateUserId(){
		String uniqueId = "user0";
		Integer idValue = 0;
		
		try {
			if (con!= null) {
				String selectQuery = "select max(genid) from studidgen";
				String updaetQuery = "update studidgen set genid=genid+1";
				
				pstmt=con.prepareStatement(selectQuery);
				rs = pstmt.executeQuery();
				if (rs.next()) {
					 idValue = rs.getInt(1);
				} 
				
				uniqueId = uniqueId+idValue;
				
				pstmt = con.prepareStatement(updaetQuery);
				
				pstmt.executeUpdate();
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			uniqueId = null;
			e.printStackTrace();
		}
		finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return uniqueId;
	}
	


}
