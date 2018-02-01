import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class odbcConnection {
	public static Connection getDBConnection() {
		Connection connection = null;
		try {
			Properties connectionInfo = new Properties();
			connectionInfo.put("charSet", "Cp1251");
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String db = "jdbc:ucanaccess://C://Program Files//eclipse//workspace//Ñoursework//CW.mdb";
			connection = DriverManager.getConnection(db, connectionInfo);
			return connection;
		} catch (Exception ex) {
			return null;
		}
	}
}
