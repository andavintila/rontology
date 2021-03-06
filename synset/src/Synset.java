import java.sql.*;

// This program receives 4 arguments: server address, username, password and word to be searched for;
// and returns the synset for the word and it's definition
public class Synset {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String username = args[1];
		String password = args[2];
		//<connection-url>jdbc:mysql://localhost:3306/cmobservatorio?useUnicode=true&amp;characterEncoding=U TF-8</connection-url>
		String url = "jdbc:mysql://";
		url = url.concat(args[0]).concat("/rontology?autoReconnect=true&useUnicode=true&characterEncoding=utf8");
	
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, username, password);
		Statement stmt = con.createStatement();
		String query = "SELECT * FROM Synset WHERE words LIKE '% ".concat(args[3]).concat(",%' OR words LIKE '").concat(args[3]).concat(", %' OR words LIKE '").concat(args[3]).concat("' OR words LIKE '% ").concat(args[3]).concat("';");
		ResultSet rs = stmt.executeQuery(query);
		while(rs.next()){
			System.out.println(rs.getString(4));
		}
		
	}

}
