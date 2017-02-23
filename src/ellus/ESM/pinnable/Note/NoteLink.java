package ellus.ESM.pinnable.Note;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.GCSV;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.display;
import ellus.ESM.data.NoteLnk;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.setting.SCon;



public class NoteLink extends pinSS {
	// id, l1 id, l2 id, xmin, xmax, ymin, ymax, p1, p2
	Object	para[]	= { null, null, null, null, null, null, null, null, null };
	NoteLnk	data;

	public NoteLink( String path, ArrayList <pinnable> pins ) {
		data= new NoteLnk( path, pins, para );
		data.load();
		// remove if not loaded correctly.
		if( para[3] == null || para[4] == null || para[5] == null || para[6] == null ){
			super.removeMe= true;
			display.printErr( this.getClass().toGenericString(), "error loding a note link: " + path );
		}else super.setXY( (int)para[3], (int)para[4], (int)para[5], (int)para[6] );
	}

	public NoteLink( String id1, String id2, String path ) {
		File spath= new File( path );
		if( spath.exists() )
			spath.mkdirs();
		spath= new File( path + "/" + super.getID() + " " + SCon.ExtpinLink );
		try{
			PrintWriter inp= new PrintWriter( spath );
			inp.println( id1 );
			inp.println( id2 );
			inp.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		data.refreshCenter();
		cor2D p1= (cor2D)para[7];
		cor2D p2= (cor2D)para[8];
		//
		g.drawLine( pan.w2bX( p1.getX() ), pan.w2bY( p1.getY() ),
				pan.w2bX( p2.getX() ), pan.w2bY( p2.getY() ),
				GCSV.pinLinkLinkThic, GCSV.pinLinkColor );
	}
}
