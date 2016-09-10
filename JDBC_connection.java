
import java.sql.*;


public class JDBC_connection {
public static void main(String[] args){
	//String url = "jdbc:oracle:thin:@10.14.4.132:1521/XE";
	String url = "jdbc:mysql://10.14.4.132 /USER12";//10.14.5.88:1521
	String user = "sripada";
	String pwd = "bhaskar";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				
			Connection conn = DriverManager.getConnection(url,user,pwd);
			System.out.println("Connection = " + conn);
			Statement stmt = conn.createStatement();
		/*	String querry = "Select * from tabl11";
			ResultSet rs = stmt.executeQuery(querry);
			while(rs.next()){
				System.out.print(rs.getInt(1) + " ");
				System.out.print(rs.getString("id")+ " ");
				//System.out.print(rs.getString("Job")+ " ");
				//System.out.println(rs.getInt("mgr")+ " ");
				System.out.println("");
			}//*/
			}catch(SQLException | ClassNotFoundException e){
				e.printStackTrace();
			}
}
}
