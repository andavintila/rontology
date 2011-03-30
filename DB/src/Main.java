import java.io.*;
import java.sql.*;
public class Main {

	/**
	 * @param args
	 */
	public static String querySelect(String tablename, String fieldname, String where, String like){
		return "SELECT " + fieldname + " FROM " + tablename + " WHERE " + where + " LIKE "+ "'" + like + "'" + "AND type IS NULL "+" LIMIT 0,100;";
	}
	public static String update(String tablename, String fieldname1, String value1, String fieldname2, String value2){
		return "UPDATE " + tablename + " SET " + fieldname1 + " = " + "'" + value1 + "'" + " WHERE " + fieldname2 + " = " + "\"" + value2 + "\" ;";
	} 
	
	public static void changeType(String type, String id, Statement stmt) throws SQLException{
		String update = null;
		if (type.contains("adv.")) 
			update = update("rontology.Def", "type", "adv.", "id", id);
		else if (type.contains("vb."))
			update = update("rontology.Def", "type", "vb.", "id", id);
		else if (type.contains("adj."))
			update = update("rontology.Def", "type", "adj", "id", id);
		else if (type.contains("s.m."))
			update = update("rontology.Def", "type", "s.m.", "id", id);
		else if (type.contains("s.n."))
			update = update("rontology.Def", "type", "s.n.", "id", id);
		else if (type.contains("s.f."))
			update = update("rontology.Def", "type", "s.f.", "id", id);
		else if (type.contains("subst.")||type.contains("s."))
			update = update("rontology.Def", "type", "s.", "id", id);
		if (update != null){
			//Statement stmt1 = con.createStatement();
			System.out.println("update");
			stmt.executeUpdate(update);
		}
	}
	
	public static void changeType2 (String type, String id, Statement stmt) throws SQLException{
		String update = null;
		if (type.contains("adv.")) 
			update = update("rontology.Def", "type", "adj. adv.", "id", id);
		else if (type.contains("vb."))
			update = update("rontology.Def", "type", "adj. vb.", "id", id);
		/*else if (type.contains("adj."))
			update = update("rontology.Def", "type", "adv. adj.", "id", id);*/
		else if (type.contains("subst.")||type.contains("s."))
			update = update("rontology.Def", "type", "adj. s.", "id", id);
		if (update != null){
			//Statement stmt1 = con.createStatement();
			System.out.println("update");
			stmt.executeUpdate(update);
		}
	}
	
	public static String prelCar(String s, String c){
		
			return s;
	}
	
	public static void prelucrareFis() throws IOException, SQLException, ClassNotFoundException{
		String username = "root";
		String password = "ceva";
	//<connection-url>jdbc:mysql://localhost:3306/cmobservatorio?useUnicode=true&amp;characterEncoding=U TF-8</connection-url>
		String url = "jdbc:mysql://127.0.0.1/rontology?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, username, password);
		BufferedReader br = new BufferedReader( new FileReader("bd.txt"));
		int i = 0, id = 386857, nrW = 1;
		String sql = null, type = null, fw = null, wordDef = null;
		Statement stmt = con.createStatement();
		ResultSet rs;
		String linie=null;
		int cont = 0;
		while(cont!=1315070){
			cont++;
			linie = br.readLine();
		}
		System.out.println(linie);
		String line = br.readLine();
		System.out.println(line);
		
		while (line != null){
			String[] word = line.split(" ~~ ");
			line = br.readLine();
			if (line.isEmpty() != true){
				while (line.isEmpty() != true){
					//System.out.println(line);
					String longWord = null;
					
					String[] sfw = line.split(" ");
					if (sfw.length > 2){
						type = sfw[sfw.length-1];
						longWord = sfw[0];
						for (i = 1; i < sfw.length - 1; i++){
							longWord = longWord.concat(" ");
							longWord = longWord.concat(sfw[i]);
						}
					}
					else { 
						if (sfw.length == 2)
							type = sfw[1];
					    fw = sfw[0].toLowerCase();
					}
					line = br.readLine(); 
					while (line.isEmpty() != true){
						//System.out.println(line);
						String def = null;
						String[] words = line.split("\\. {1,}+");
						if (words.length >= 2){
						if (words[1].contains(",")){
							String[] wordsAux = words[1].split(",");
							nrW += wordsAux.length;
							if (wordsAux[0].startsWith("a ")) {
								String[] wd = wordsAux[0].split(" ");
								wordDef = wd[wd.length - 1];
							}
							else wordDef = wordsAux[0];
						}
						else { 
							nrW++;
							if (words[1].startsWith("a ")){
								String[] wordsA = words[1].split(" ");
								wordDef = wordsA[wordsA.length -1];
							}
							else wordDef = words[1];
							}
						
				//		System.out.println(wordDef);
					//	else wordDef = words[1].split("{3} ")[1];
						
						Statement stmt1 = con.createStatement();
						ResultSet rs1 = stmt1.executeQuery("SELECT * FROM rontology.Def WHERE lexicon LIKE '" + wordDef +"'AND type LIKE '%"+ type +"%';");
						String wds = null;
						if (longWord != null)
							wds = longWord.toLowerCase() + ", " + words[1].split("   ")[0];
						else wds = fw.toLowerCase() + ", " + words[1].split("   ")[0];
						if (rs1.next()) def = rs1.getString(4);
						if (def != null && def.contains("'")){
							String[] spl = def.split("'");
						//	System.out.println(spl[0]);
							def = spl[0];
							for (i = 1; i < spl.length; i++){
								if(def.endsWith("\\"))
									def = def.concat("\\''").concat(spl[i]);
								else def = def.concat("''").concat(spl[i]);
							}
						}
						sql = "insert into rontology.Synset (id, type, nrWords, words, definition) values(" + id + ", '" + type + "', " + nrW + ", '" + wds + "', '"+ def +"');";
						Statement stmt3 = con.createStatement();
						ResultSet rs3 = stmt3.executeQuery("SELECT * FROM rontology.Synset WHERE words LIKE '" + wds + "';");
						if (rs3.next() == false){
							System.out.println(sql);
							stmt.executeUpdate(sql);
						}
						rs1.close();
						stmt1.close();
						rs3.close();
						stmt3.close();
						}
						//= stmt.executeUpdate(sql);
						line = br.readLine();
						id++;
						nrW = 1;
						
					}
					//br.readLine();
					line = br.readLine();
					//fw = line;
					if (line.isEmpty()!= true) line = br.readLine();
				}
				 
			}
			else {
				String def = null;
				Statement stmt2 = con.createStatement();
				if (word.length >= 2){
				ResultSet rs2 = stmt2.executeQuery("SELECT * FROM rontology.Def WHERE lexicon LIKE '" + word[1] +"';");
				if (rs2.next()){
					type = rs2.getString(7);
					def = rs2.getString(4);
					if (def != null && def.contains("'")){
						String[] spl = def.split("'");
					//	System.out.println(spl[0]);
						def = spl[0];
						for (i = 1; i < spl.length; i++){
							if(def.endsWith("\\"))
								def = def.concat("\\''").concat(spl[i]);
							else def = def.concat("''").concat(spl[i]);
						}
					}
					
				}
				sql = "insert into rontology.Synset (id, type, nrWords, words, definition) values(" + id + ", '" + type + "', " + nrW + ", '" + word[1] + "', '" + def +"');";
				Statement stmt4 = con.createStatement();
				ResultSet rs4 = stmt4.executeQuery("SELECT * FROM rontology.Synset WHERE words LIKE '" + word[1] + "';");
				if (rs4.next() == false){
					System.out.println(sql);
					stmt.executeUpdate(sql);
				}
				rs2.close();
				rs4.close();
				stmt2.close();
				stmt4.close();
				}
				//System.out.println(word[1]);
				//System.out.println(sql);
				//System.out.println("(" + id + ", '" + type + "', " + nrW + ", '" + word[1]+"', '"+ def +"');");
				id++;
				
			}
			br.readLine();
			line = br.readLine();
			i++;
		}
		stmt.close();
		con.close();
	}
	
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
	//	System.out.println("ceva");
		String username = "root";
		String password = "ceva";
	//<connection-url>jdbc:mysql://localhost:3306/cmobservatorio?useUnicode=true&amp;characterEncoding=U TF-8</connection-url>
		String url = "jdbc:mysql://127.0.0.1/rontology?autoReconnect=true&useUnicode=true&characterEncoding=utf8";
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection(url, username, password);
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("");
		
		//tip adj si adv
	//	String query = querySelect("SELECT *  FROM rontology.Def WHERE htmlRep LIKE '%adj.%' AND htmlRep LIKE '%adv.%';");
	/*	ResultSet rs = stmt.executeQuery("SELECT * FROM rontology.Def WHERE id <= 534282 AND id >= 500000;");
		while (rs.next()){
			//System.out.print(rs.getString(3) + ", ");
			Statement stmt1 = con.createStatement();
			ResultSet rs1 = stmt1.executeQuery("SELECT * FROM dex.LexemDefinitionMap WHERE definitionId = '" + rs.getString(1) + "' ;" );
			if (rs1.next()){
				//"SELECT * FROM dex.InflectedForm WHERE lexemId = '" + rs.getString(1) + "' ;" );
				//System.out.println(rs.getString(1) + "    " + rs1.getString(2));
				
				Statement stmt3 = con.createStatement();
				ResultSet rs3 = stmt3.executeQuery("SELECT * FROM dex.InflectedForm WHERE lexemId = '" + rs1.getString(2) + "' ;" );
				if (rs3.next()){
				int infl = Integer.parseInt(rs3.getString(6));
			//	System.out.println("infl" + infl);
				String update = null;
				if (infl <= 8) 
					update = update("rontology.Def", "type", "s.m.", "id", rs.getString(1));
				else if (infl <= 16)
					update = update("rontology.Def", "type", "s.f.", "id", rs.getString(1));
				else if (infl <= 24)
					update = update("rontology.Def", "type", "s.n.", "id", rs.getString(1));
				else if (infl <= 40)
					update = update("rontology.Def", "type", "adj.", "id", rs.getString(1));
				else if (infl < 84 && infl >= 49)
					update = update("rontology.Def", "type", "vb.", "id", rs.getString(1));
				else if (infl == 84)
					update = update("rontology.Def", "type", "inv.", "id", rs.getString(1));
				Statement stmt2 = con.createStatement();
				if (update != null){
				//	System.out.println(update);
					stmt2.executeUpdate(update);
				}
			}}
		}
		
	/*	//tip adv 
		String query = querySelect("rontology.Def", "*", "htmlRep", "%adv.%" );
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()){
			System.out.print(rs.getString(3) + ", ");
			String update = update("rontology.Def", "type", "adv", "id", rs.getString(1)); 
			Statement stmt2 = con.createStatement();
			System.out.println(stmt2.executeUpdate(update));
		}*/
	/*	//SELECT * FROM `Def` WHERE type like 'adj. vb.' and internalRep not like '%adj.%'
		ResultSet rs = stmt.executeQuery("SELECT * FROM rontology.Def WHERE type like 'adj. vb.' and internalRep not like '%adj.%'");
	//	System.out.println(rs.getString(1));
		while (rs.next()){
			System.out.println(rs.getString(1));
			Statement stmt1 = con.createStatement();
	//		String update = update("rontology.Def", "type", "vb.", "id", rs.getString(1));
		//	stmt1.executeUpdate(update);
		//	System.out.println(rs.getString(4));
		//	String[] type = rs.getString(4).split("@");
		//	if (type.length >= 3){
			//	System.out.println(type[2]);
				type = type[2].split(" ");
				if (type.length >= 5){
			 		System.out.println(type[4]);
			 	//	changeType2(type[3], rs.getString(1), stmt1);
					}
			}
		//	for (int i = 0; i < type.length; i++)
			//	changeType(type[i], rs.getString(1), stmt1);
			
			
		//	update = update("rontology.Def", "type", type[0], "id", rs.getString(1));*/
	//	Statement stmt = con.createStatement();
	/*	ResultSet rs = stmt.executeQuery("select * from Def where lexicon like 'deține';");
		while (rs.next()){
	//		System.out.println(rs.getString(4));
		}*/
		//prelucrareFis();
//		stmt.close();
	//	con.close();
		}
	//metacaracter
	//@dexonline
}

