package ellus.ESM.Machine;

import java.util.ArrayList;
import ellus.ESM.ESMP.PasswordManager;
import ellus.ESM.ESMW.ESMW_Home;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;



public class mainDriver {
	public static void main( String[] args ) {
		//
		//
		//
		try{
			//----------------------- ESM ----------------------------
			//
			boolean run= true;
			if( run ){
				//
				// load all setting. ( must be done before runs. )
				new SMan();
				ESMW_Home eh= new ESMW_Home();
				//
			}
			//----------------------- ESM end -------------------------
		}catch ( Exception e ){
			e.printStackTrace();
			System.exit( 111 );
		}
	}

	private static void wait( int time ) {
		try{
			Thread.sleep( time );
		}catch ( InterruptedException e ){
			e.printStackTrace();
		}
	}
}