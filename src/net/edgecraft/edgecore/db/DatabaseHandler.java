package net.edgecraft.edgecore.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.edgecraft.edgecore.EdgeCore;

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
	
	private static final DatabaseHandler instance = new DatabaseHandler();
	private Connection connection;
	
	// Constructors:
	protected DatabaseHandler() {/* ... */}

	public static DatabaseHandler getInstance() {
		return instance;
	}
	
	/**
	 * Establishes a database-connection with the given config.
	 * 
	 * @param host
	 * @param user
	 * @param pw
	 * @param db
	 */
	public synchronized void loadConnection(String host, String user, String pw, String db) {
		if (host.equals(unset) || user.equals(unset) || pw.equals(unset) || db.equals(unset)) throw new IllegalArgumentException("Given Database Parameters are set to default!");
		
		try {
			
			EdgeCore.log.info(EdgeCore.edgebanner + " Baue Datenbankverbindung auf..");
			
			if (!isAvailable()) closeConnection();
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			setConnection( DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db + "?user=" + user + "&password=" + pw) );
			
			EdgeCore.log.info(EdgeCore.edgebanner + " Erfolgreich mit Datenbank '" + db + "' verbunden!");
			
		} catch(Exception e) {
			EdgeCore.log.severe(EdgeCore.edgebanner + " Schwerer Fehler beim Vebrinden mit Datenbank '" + db + "'!");
			e.printStackTrace();
		}		
	}
		
	/**
	 * Establishes a database-connection with the local config.
	 * 
	 */
	public synchronized void loadConnection() {
		loadConnection( getHost(), getUser(), getPW(), getDB() );
	}
		
	/**
	 * Closes the database-connection.
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		if ( isAvailable() ) {
			getConnection().close();
			loadConnection();
		}
	}
	
	/**
	 * Checks whether the database-connection is already established.
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean isAvailable() {
		return getConnection() != null;
	}
	
	/**
	 * Executes a query-command.
	 * 
	 * @param sql
	 * @throws Exception
	 */
	@Deprecated
	public synchronized PreparedStatement prepareQuery( String sql ) throws Exception {
		return getConnection().prepareStatement(sql);
	}
	
	/**
	 * Executes an update-command.
	 * 
	 * @param sql
	 * @throws Exception
	 */
	@Deprecated
	public synchronized PreparedStatement prepareUpdate( String sql ) throws Exception {
		return getConnection().prepareStatement(sql);
	}
	
	/**
	 * Prepares a statement for the given command
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public synchronized PreparedStatement prepareStatement( String sql ) throws Exception {
		return getConnection().prepareStatement(sql);
	}

	/**
	 * Returns all databases of the host.
	 * 
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
	 * Checks whether the given database already exists.
	 * 
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
	 * Checks whether the given table already exists.
	 * 
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
	 * Returns all returned values of the given sql-command.
	 * 
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

	/**
	 * Returns the host.
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the user.
	 * 
	 * @return
	 */
	public static String getUser() {
		return user;
	}

	/**
	 * Returns the password.
	 * 
	 * @return
	 */
	public static String getPW() {
		return pw;
	}

	/**
	 * Returns the database.
	 * 
	 * @return
	 */
	public static String getDB() {
		return db;
	}

	/**
	 * Returns the connection.
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}
	

	/**
	 * Sets the host.
	 * 
	 * @param host
	 */

	public static void setHost( String host ) {
		if( host != null )
			DatabaseHandler.host = host;
	}

	/**
	 * Sets the user.
	 * 
	 * @param user
	 */
	public static void setUser( String user ) {
		if( user != null )
			DatabaseHandler.user = user;
	}

	/**
	 * Sets the password.
	 * 
	 * @param pw
	 */
	public static void setPW( String pw ) {
		if( pw != null )
			DatabaseHandler.pw = pw;
	}

	/**
	 * Sets the database.
	 * 
	 * @param db
	 */
	public static void setDB ( String db ) {
		if( db != null ) 
			DatabaseHandler.db = db;
	}

	/**
	 * Sets the connection.
	 * 
	 * @param connection
	 */
	public void setConnection( Connection connection ) {
		if( connection != null )
			this.connection = connection;
	}
	
}