package ellus.ESM.data.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SMan;



public class mySQLportal {
	public static final String			tableName	= "ESM_table";
	public static final String			id1			= "_ID_Date";
	public static final String			id2			= "_ID_Time";
	//
	private static Connection			con			= null;
	private static ArrayList <String>	cols		= null;
	private static ArrayList <String>	colsType	= new ArrayList <>();;
	//
	private static boolean				outputMsg	= false;

	public static boolean addCol( String name, String type ) {
		try{
			ExecUpdate( "ALTER TABLE " + tableName + " ADD " + name + " " + type + ";" );
		}catch ( SQLException e ){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static ArrayList <String> getAllCol() {
		if( cols == null )
			connect();
		return (ArrayList <String>)cols.clone();
	}

	public static boolean insert( ArrayList <String> name, ArrayList <String> val ) {
		if( name == null || val == null || name.size() != val.size() || name.size() == 0 )
			return false;
		//
		if( cols == null )
			connect();
		//
		try{
			String time= helper.getCurrentTime() + "" +
					helper.getCurrentTimeMS() + "" + (int) ( Math.random() * 10 )
					+ (int) ( Math.random() * 10 ) + "" + (int) ( Math.random() * 10 );
			//
			String newAC= "INSERT INTO " + tableName + " ( " + id1 + "," + id2 + ",";
			String newVa= ") VALUES ( " + helper.getCurrentDate() + "," + time + ",";
			boolean last= false;
			for( int i= 0; i < name.size(); i++ ){
				// check if colume exists, or if this is id.
				if( !cols.contains( name.get( i ) ) || name.get( i ).equals( id1 ) || name.get( i ).equals( id2 )
						|| val.get( i ) == null || val.get( i ).length() == 0 || helper.AllEmptySpace( val.get( i ) ) )
					continue;
				//
				if( last ){
					newAC+= ",";
					newVa+= ",";
					last= false;
				}
				//
				newAC+= name.get( i );
				// store all val as string.
				newVa+= "'" + val.get( i ).replace( '\'', '_' ) + "'";
				last= true;
			}
			// remove end ,
			newAC.substring( 0, newAC.length() - 1 );
			newVa.substring( 0, newVa.length() - 1 );
			ExecUpdate( newAC + newVa + " );" );
			//
		}catch ( Exception e ){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean update( String idA, String idB, ArrayList <String> name, ArrayList <String> val ) {
		if( idA == null || idB == null || name == null || val == null || name.size() != val.size() || name.size() == 0 )
			return false;
		//
		if( cols == null )
			connect();
		//
		try{
			String query= "UPDATE " + tableName + " SET ";
			for( int i= 0; i < name.size(); i++ ){
				// check if colume exists, or if this is id.
				if( !cols.contains( name.get( i ) ) || name.get( i ).equals( id1 ) || name.get( i ).equals( id2 )
						|| val.get( i ) == null || val.get( i ).length() == 0 || helper.AllEmptySpace( val.get( i ) ) )
					continue;
				query+= " " + name.get( i ) + " = '" + val.get( i ) + "',";
			}
			query= query.substring( 0, query.length() - 1 );
			query+= " WHERE " + id1 + " = " + idA + " AND " + id2 + " = " + idB + " ; ";
			ExecUpdate( query );
			//
		}catch ( Exception e ){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean delete( String idA, String idB ) {
		if( idA == null || idB == null )
			return false;
		//
		if( cols == null )
			connect();
		//
		try{
			String query= "DELETE FROM " + tableName + " WHERE " +
					id1 + " = " + idA + " AND " + id2 + " = " + idB + " ; ";
			ExecUpdate( query );
			//
		}catch ( Exception e ){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static ArrayList <sqlResult> getByFunc( String func, int lim, ArrayList <String> name,
			ArrayList <String> val ) {
		//
		if( cols == null )
			connect();
		//
		try{
			String query= "SELECT * FROM " + tableName +
					" WHERE functional" + " = '" + func + "' ";
			if( name != null ){
				for( int i= 0; i < name.size(); i++ ){
					// check if colume exists, or if this is id.
					if( !cols.contains( name.get( i ) ) || name.get( i ).equals( id1 ) || name.get( i ).equals( id2 )
							|| val.get( i ).length() == 0 || helper.AllEmptySpace( val.get( i ) ) )
						continue;
					query+= " AND " + name.get( i ) + " = '" + val.get( i ) + "' ";
				}
			}
			if( lim > 0 )
				query+= " LIMIT " + lim;
			ResultSet res= ExecQuery( query );
			ResultSetMetaData rsmd= res.getMetaData();
			//
			ArrayList <sqlResult> ret= new ArrayList <>();
			sqlResult rs;
			//
			while( res.next() ){
				rs= new sqlResult();
				rs.colName.add( rsmd.getColumnName( 1 ) );
				rs.type.add( rsmd.getColumnTypeName( 1 ) );
				rs.val.add( res.getInt( 1 ) + "" );
				rs.colName.add( rsmd.getColumnName( 2 ) );
				rs.type.add( rsmd.getColumnTypeName( 2 ) );
				rs.val.add( res.getBigDecimal( 2 ).toString() );
				for( int i= 2; i < rsmd.getColumnCount(); i++ ){
					rs.colName.add( rsmd.getColumnName( i + 1 ) );
					rs.type.add( rsmd.getColumnTypeName( i + 1 ) );
					rs.val.add( res.getObject( i + 1 ) );
				}
				ret.add( rs );
			}
			return ret;
		}catch ( SQLException e ){}
		return null;
	}

	public static void close() {
		try{
			if( con != null )
				con.close();
		}catch ( SQLException e ){
			e.printStackTrace();
		}
		cols= null;
	}

	public static void connect() {
		try{
			if( con == null || !con.isValid( 5 ) ){
				connectTo();
				createESMtable();
				refreshAllCol();
				//
				Thread.sleep( 10 );
			}
		}catch ( SQLException e ){
			e.printStackTrace();
		}catch ( InterruptedException e ){
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
		if( outputMsg )
			display.println( "data.mySQLportal", "connectTo()" );
		try{
			Class.forName( "com.mysql.jdbc.Driver" );
			//
			String userName= SMan.getSetting( 2010 );
			String password= SMan.getSetting( 2011 );
			String url= SMan.getSetting( 2012 );
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
	}

	private static void createESMtable() {
		if( outputMsg )
			display.println( "data.mySQLportal", "createESMtable()" );
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
				"PRIMARY KEY (" + id1 + "," + id2 + ") " +
				");";
		Statement stmt= null;
		try{
			stmt= con.createStatement();
			stmt.executeUpdate( createString );
			stmt.close();
			if( outputMsg )
				display.println( "data.mySQLportal", "master table is refreshed." );
		}catch ( SQLException e ){
			e.printStackTrace();
		}
		//
	}

	public static void refreshAllCol() {
		display.println( "data.mySQLportal", "refreshAllCol()" );
		//
		colsType= new ArrayList <>();
		ArrayList <String> col= new ArrayList <>();
		try{
			ResultSetMetaData rsmd= ExecQuery( "SELECT * FROM " + tableName + " LIMIT 1" ).getMetaData();
			for( int i= 1; i <= rsmd.getColumnCount(); i++ ){
				col.add( rsmd.getColumnName( i ) );
				colsType.add( rsmd.getColumnTypeName( i ) );
			}
		}catch ( SQLException e ){}
		// refresh all the cols.
		cols= col;
	}

	private static void ExecUpdate( String inp ) throws SQLException {
		connect();
		if( outputMsg )
			display.println( "data.mySQLportal", "ExecUpdate: " + inp );
		//
		Statement stmt= null;
		stmt= con.createStatement();
		stmt.executeUpdate( inp );
		stmt.close();
	}

	private static ResultSet ExecQuery( String inp ) {
		connect();
		if( outputMsg )
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
