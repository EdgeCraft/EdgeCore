package net.edgecraft.edgecraft.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.edgecraft.edgecraft.EdgeCraft;

public class DatabaseHandler {
	
	public static String host;
	public static String user;
	public static String pw;
	public static String db;
	
	private String unset = "default";
	
	public Connection connection;
	
	/**
	 * Erstellt eine Datenbank-Verbindung mit den in der Konfiguration angegebenen Werten
	 */
	public synchronized void loadConnection() {
		try {
			
			EdgeCraft.log.info("[EdgeCraft] Baue Datenbankverbindung auf..");
			
			if ((host.equals(this.unset)) || (user.equals(this.unset)) || (pw.equals(this.unset)) || (db.equals(this.unset))) {
				EdgeCraft.log.severe("[EdgeCraft] Fehlerhafte Werte! Kein Datenbankverbindung!");
				return;
			}
			
			if (!isAvailable()) closeConnection();
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?user=" + user + "&password=" + pw);
			
			EdgeCraft.log.info("[EdgeCraft] Erfolgreich mit Datenbank '" + db + "' verbunden!");
			
		} catch(Exception e) {
			EdgeCraft.log.severe("[EdgeCraft] Schwerer Fehler beim Vebrinden mit Datenbank '" + db + "'!");
			e.printStackTrace();
		}
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
			this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?user=" + user + "&password=" + pw);
			
			EdgeCraft.log.info("[EdgeCraft] Erfolgreich mit Datenbank '" + db + "' verbunden!");
			
		} catch(Exception e) {
			EdgeCraft.log.severe("[EdgeCraft] Schwerer Fehler beim Vebrinden mit Datenbank '" + db + "'!");
			e.printStackTrace();
		}		
	}
	
	/**
	 * Schließt die Datenbank-Verbindung
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if (this.connection != null) {
			this.connection.close();
		}
	}
	
	/**
	 * Prüft, ob die Datenbank-Verbindung aufrecht steht
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isAvailable() throws Exception {
		return this.connection == null;
	}
	
	/**
	 * Führt einen Query-Befehl in der Datenbank aus
	 * @param sql
	 * @throws Exception
	 */
	public synchronized void executeQuery(String sql) throws Exception {
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.executeQuery();
	}
	
	/**
	 * Führt einen Update-Befehl in der Datenbank aus
	 * @param sql
	 * @throws Exception
	 */
	public synchronized void executeUpdate(String sql) throws Exception {
		PreparedStatement statement = this.connection.prepareStatement(sql);
		statement.executeUpdate();
	}
	
	/**
	 * Gibt alle Datenbanken vom verwendeten Host aus
	 * @return String
	 * @throws Exception
	 */
	public synchronized String getDatabases() throws Exception {
		
		StringBuilder sb =  new StringBuilder();
		
		DatabaseMetaData metaData = this.connection.getMetaData();
		ResultSet rs = metaData.getCatalogs();
		
		while (rs.next()) {
			if (sb.length() > 0) sb.append(", ");
			if (rs.getString(1) != null) sb.append(rs.getString(1));
		}
		
		return sb.toString();
	}
	
	/**
	 * Prüft, ob angegebene Datenbank existiert
	 * @param db
	 * @return true/false
	 * @throws Exception
	 */
	public synchronized boolean existsDatabase(String db) throws Exception {
		
		DatabaseMetaData metaData = this.connection.getMetaData();
		ResultSet rs = metaData.getCatalogs();
		
		while (rs.next()) {
			if (rs.getString(1).equalsIgnoreCase(db)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Prüft, ob angegebene Tabelle existiert
	 * @param table
	 * @return true/false
	 * @throws Exception
	 */
	public synchronized boolean existsTable(String table) throws Exception {
		
		DatabaseMetaData metaData = this.connection.getMetaData();
		ResultSet tables = metaData.getTables(null, null, table, null);
		
		if (!tables.next()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Gibt alle Objekte in einer 'List<Map<String, Object>>' zurück, welche vom SQL-Befehl gefunden wurden
	 * @param sql
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public synchronized List<Map<String, Object>> getResults(String sql) throws Exception {
		
		if (this.connection == null) throw new Exception("No active database connection found!\n");
		
		List<Map<String, Object>> columns = new ArrayList<>();
		
		PreparedStatement statement = this.connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
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
