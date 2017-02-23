package ellus.ESM.data.SQL;

import java.util.ArrayList;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;



public class sqlResult {
	public ArrayList <String>	colName	= null;
	public ArrayList <String>	type	= null;
	public ArrayList <Object>	val		= null;
	public String				ID		= helper.getCurrentTime() + "_" + helper.rand32AN().substring( 0, 5 );

	public sqlResult() {
		colName= new ArrayList <>();
		type= new ArrayList <>();
		val= new ArrayList <>();
	}

	public void printAll() {
		display.println( this.getClass().toString(), "SQL resutl ID: " + ID );
		int i= 1;
		for( String str : colName ){
			display.println( this.getClass().toString(), "column: " + ( i ) + " name: "
					+ str + " type: " + type.get( i - 1 ) + " value: " + val.get( i - 1 ) );
			i++ ;
		}
	}
}
