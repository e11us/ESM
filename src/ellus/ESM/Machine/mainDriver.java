package ellus.ESM.Machine;

import ellus.ESM.ESMW.ESMW_Home;
import ellus.ESM.setting.SMan;



public class mainDriver {
	public static void main( String[] args ) {
		//
		//
		//
		try{
			//----------------------- ESM ----------------------------
			//
			//PlayerAudio pa= new PlayerAudio( "C:/_C#const/Boeing 777, Night Flight Cabin Sound.mp3" );
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