package net.edgecraft.edgecraft.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.edgecraft.edgecraft.EdgeCraft;

public class DatabaseHandler {
	
	public static final String DatabaseHost = "Database.Host";
	public static final String DatabaseUser = "Database.User";
	public static final String DatabasePW = "Database.Password";
	public static final String DatabaseDB = "Database.Database";


	private static String host;
	private static String user;
	private static String pw;
	private static String db;
	
	public static final String unset = "default";
	
	private Connection connection;
	


	public String getHost() {
		return host;
	}

	public static String getUser() {
		return user;
	}

	public static String getPW() {
		return pw;
	}

	public static String getDB() {
		return db;
	}

	public Connection getConnection() {
		return connection;
	}
	


	public static void setHost( String host ) {
		if( host != null )
			DatabaseHandler.host = host;
	}

	public static void setUser( String user ) {
		if( user != null )
			DatabaseHandler.user = user;
	}

	public static void setPW( String pw ) {
		if( pw != null )
			DatabaseHandler.pw = pw;
	}

	public static void setDB ( String db ) {
		if( db != null ) 
			DatabaseHandler.db = db;
	}


	public void setConnection( Connection connection ) {
		if( connection != null )
			this.connection = connection;
	}

	public DatabaseHandler() {} // default


	public DatabaseHandler( String host, String user, String pw, String db ) {

		setHost( host );
		setUser( user );
		setPW( pw );
		setDB( db );
	}


	/**
	 * Erstellt eine Datenbank-Verbindung mit den in der Konfiguration angegebenen Werten
	 */
	public synchronized void loadConnection() {
		loadConnection( getHost(), getUser(), getPW(), getDB() );
	}
	
	/**
	 * Erstellt eine Datenbank-Verbindung mit den angegebenen Werten
	 * @param host
	 * @param user
	 * @param pw
	 * @param db
	 */
	public synchronized void loadConnection(String host, String user, String pw, String db) {
		try {
			
			EdgeCraft.log.info("[EdgeCraft] Baue Datenbankverbindung auf..");
			
			if (!isAvailable()) closeConnection();
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			setConnection( DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?user=" + user + "&password=" + pw) );
			
			EdgeCraft.log.info("[EdgeCraft] Erfolgreich mit Datenbank '" + db + "' verbunden!");
			
		} catch(Exception e) {
			EdgeCraft.log.severe("[EdgeCraft] Schwerer Fehler beim Vebrinden mit Datenbank '" + db + "'!");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Schlie�t die Datenbank-Verbindung
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if ( isAvailable() ) {
			getConnection().close();
		}
	}
	
	/**
	 * Pr�ft, ob die Datenbank-Verbindung aufrecht steht
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isAvailable() {
		return getConnection() != null;
	}
	
	/**
	 * F�hrt einen Query-Befehl in der Datenbank aus
	 * @param sql
	 * @throws Exception
	 */
	public synchronized void executeQuery( String sql ) throws Exception {
		getConnection().prepareStatement(sql).executeQuery();
	}
	
	/**
	 * F�hrt einen Update-Befehl in der Datenbank aus
	 * @param sql
	 * @throws Exception
	 */
	public synchronized void executeUpdate( String sql ) throws Exception {
		getConnection().prepareStatement(sql).executeUpdate();
	}
	

	/**
	 * Gibt alle Datenbanken vom verwendeten Host aus
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getDatabases() throws Exception {
		
		StringBuilder sb =  new StringBuilder();

		ResultSet rs = getConnection().getMetaData().getCatalogs();
		
		while (rs.next()) {
			if (sb.length() > 0) sb.append(", ");
			if (rs.getString(1) != null) sb.append(rs.getString(1));
		}
		
		return sb.toString();
	}
	
	/**
	 * Pr�ft, ob angegebene Datenbank existiert
	 * @param db
	 * @return true/false
	 * @throws Exception
	 */
	public synchronized boolean existsDatabase( String db ) throws Exception {
		
		ResultSet rs = getConnection().getMetaData().getCatalogs();
		

		while (rs.next()) {
			if (rs.getString(1).equalsIgnoreCase(db)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Pr�ft, ob angegebene Tabelle existiert
	 * @param table
	 * @return true/false
	 * @throws Exception
	 */
	public synchronized boolean existsTable( String table ) throws Exception {
		
		ResultSet tables = getConnection().getMetaData().getTables( null, null, table, null );
		
		if (!tables.next()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gibt alle Objekte in einer 'List<Map<String, Object>>' zur�ck, welche vom SQL-Befehl gefunden wurden
	 * @param sql
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public synchronized List<Map<String, Object>> getResults( String sql ) throws Exception {
		
		if ( !isAvailable() ) throw new Exception("No active database connection found!\n");
		
		List<Map<String, Object>> columns = new ArrayList<>();
		
		ResultSet resultSet = getConnection().prepareStatement(sql).executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		
		int columnCount = metaData.getColumnCount();
		
		while (resultSet.next()) {
			
			Map<String, Object> column = new LinkedHashMap<>(columnCount);
			
			for (int i = 1; i <= columnCount; i++) {
				column.put(metaData.getColumnName(i), resultSet.getObject(i));
			}
			
			columns.add(column);
		}
		
		return columns;
	}

	
}
