package ellus.ESM.data.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;



public class mySQLportal {
	public static final String	tableName	= "ESM_table";
	public static final String id1= "_ID_Date";
	public static final String id2= "_ID_Time";
	//
	private static Connection	con			= null;
	private static ArrayList<String> cols= null;
	private static ArrayList<String> colsType= new ArrayList<String>();;
	//

	public static void createMasterTable() {
		createESMtable();
	}

	public static void addCol( String name, String type  ) {
		ExecUpdate( "ALTER TABLE " + tableName + " ADD " + name + " " + type +";" );
	}
	
	public static ArrayList <String> getAllCol() {
		if( cols == null )
			refreshAllCol();
		return (ArrayList <String>)cols.clone();
	}
	
	public static boolean insert( ArrayList<String> name, ArrayList<String> val ) {
		if( name == null || val == null || name.size() != val.size() || name.size() == 0 )
			return false;
		//
		if( cols == null )
			refreshAllCol();
		//
		try{
			String time= helper.getCurrentTime() + "" +  
					helper.getCurrentTimeMS()  + "" + (int)(Math.random() * 10)
					+ (int)(Math.random() * 10 ) +""+ (int)(Math.random() * 10);
			//
			String newAC= "INSERT INTO " + tableName + " ( " + id1 + "," + id2 + ",";
			String newVa= ") VALUES ( " + helper.getCurrentDate() + "," + time + ",";
			boolean last= false;
			for( int i= 0; i < name.size(); i++ ) {
				// check if colume exists, or if this is id.
				if( !cols.contains( name.get( i ) ) || name.get( i ).equals( id1 ) || name.get( i ).equals( id2 )
						|| val.get( i ).length() == 0 || helper.AllEmptySpace( val.get( i ) ) ) 
					continue;
				//
				if( last ) {
					newAC+= ",";
					newVa+= ",";
					last= false;
				}
				//
				newAC += name.get( i );
				// store all val as string.
				newVa += "'" + val.get( i ).replace( '\'', '_' ) + "'" ;
				last= true;
			}
			// remove end ,
			newAC.substring( 0, newAC.length() - 1  );
			newVa.substring( 0, newVa.length() - 1  );
			ExecUpdate( newAC + newVa + " );" );
		}catch ( Exception e ){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void getByFunc( String func, int lim ) {
		try{
			ResultSet rsmd= ExecQuery("SELECT * FROM " + tableName + 
					" WHERE functional" + " = '" + func + "' LIMIT " + lim);
			while( rsmd.next() ) {
				//
				
				
				
				
				
				
				
				
				
			}
		}catch ( SQLException e ){}	
	}
	
	public static void connect() {
		try{
			if( con == null || !con.isValid( 5 ) )
				connectTo();
		}catch ( SQLException e ){
			e.printStackTrace();
		}
	}

	/*
	 * 
	 * ---------------------------------------------------------------------
	 * 
	 */
	
	private static String filterIllegalChar( String inp ) {
		//if( inp == null || inp.length() == 0 )
			return null;
	}
	private static void connectTo() {
		display.println( "data.mySQLportal", "connectTo()" );
		try{
			Class.forName( "com.mysql.jdbc.Driver" );
			//
			String userName= "root";
			String password= "nvkD93Ix03Lzib43sQ"; //"nvkD93sfeIx03K34URLzib43sQ";
			String url= "jdbc:mysql://localhost:3306/esmsql";
			//
			con= DriverManager.getConnection( url, userName, password );
			display.println( "data.mySQLportal", "connection established successfully." );
		}catch ( SQLException ex ){
			System.out.println( "SQLException: " + ex.getMessage() );
			System.out.println( "SQLState: " + ex.getSQLState() );
			System.out.println( "VendorError: " + ex.getErrorCode() );
		}catch ( ClassNotFoundException e ){
			e.printStackTrace();
		}
		//
		createESMtable();
	}

	private static void createESMtable() {
		display.println( "data.mySQLportal", "createESMtable()" );
		connect();
		//
		String createString= "create table IF NOT EXISTS " + tableName + " (" +
				id1 + " INT, " +
				id2 + " BIGINT, " +
				"functional varchar(16), " +
				"keyword varchar(1000), " +
				//
				"content TEXT(25000), " +
				"comment TEXT(25000), " +
				"situation TEXT(5000), " +
				//
				"webLink varchar(2083), " +
				"locLink varchar(2083), " +
				"startDate varchar(20), " +
				"endDate varchar(20), " +
				//
				"urgency char(1), " +
				"importancy char(1), " +
				"ratingPoint char(1), " +
				"PRIMARY KEY ("+id1+","+id2+") " +
				");";
		Statement stmt= null;
		try{
			stmt= con.createStatement();
			stmt.executeUpdate( createString );
			stmt.close();
			display.println( "data.mySQLportal", "master table is refreshed." );
		}catch ( SQLException e ){
			e.printStackTrace();
		}
		//
	}
	
	public static void refreshAllCol() {
		display.println( "data.mySQLportal", "refreshAllCol()" );
		try{
			if( con == null || !con.isValid( 5 ) )
				connectTo();
		}catch ( SQLException e1 ){}
		//
		colsType= new ArrayList<>();
		ArrayList <String> col= new ArrayList <>();
		try{
			ResultSetMetaData rsmd= ExecQuery("SELECT * FROM " + tableName + " LIMIT 1" ).getMetaData();
			for( int i= 1; i <= rsmd.getColumnCount(); i++ ) {
				col.add(  rsmd.getColumnName( i ) );
				colsType.add( rsmd.getColumnTypeName( i )  );
			}
		}catch ( SQLException e ){}
		// refresh all the cols.
		cols= col;
		/*
		for( int i= 0; i < cols.size(); i++ ) {
			display.println( "data.mySQLportal", "column(" + (i+1) + "): <" 
					+  cols.get( i ) + ">" + " <" + colsType.get( i ) + ">" );
		}
		*/
	} 
	
	private static void ExecUpdate( String inp ) {
		display.println( "data.mySQLportal", "ExecUpdate()" );
		connect();
		display.println( "data.mySQLportal", "ExecUpdate: " + inp );
		//
		Statement stmt= null;
		try{
			stmt= con.createStatement();
			stmt.executeUpdate( inp );
			stmt.close();
			display.println( "data.mySQLportal", "master table is refreshed." );
		}catch ( SQLException e ){
			e.printStackTrace();
		}
	}

	private static ResultSet ExecQuery( String inp ) {
		connect();
		display.println( "data.mySQLportal", "ExecQuery: " + inp );
		//
		try{
			Statement stmt= con.createStatement();
			ResultSet rs= stmt.executeQuery( inp );
			return rs;
		}catch ( SQLException e ){
			e.printStackTrace();
		}
		return null;
	}
}
