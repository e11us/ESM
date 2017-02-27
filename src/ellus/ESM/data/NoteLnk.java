package ellus.ESM.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleLocalLink;



public class NoteLnk {
	private String					path;
	private ArrayList <pinnable>	pL;
	private Object[]				inp;																																																																												 // id, l1 id, l2 id, xmin, xmax, ymin, ymax, p1, p2
	private pinnable				pp1, pp2;

	public NoteLnk( String path, ArrayList <pinnable> pins, Object[] inp ) {
		this.path= path;
		this.pL= pins;
		this.inp= inp;
	}

	public void load() {
		readFromDisk();
	}

	private void readFromDisk() {
		if( pL == null || path == null )
			return;
		String p1, p2;
		pp1= pp2= null;
		p1= p2= null;
		try{
			Scanner rdr= new Scanner( ( new File( path ) ).getName() );
			inp[0]= rdr.next();
			rdr.close();
			rdr= new Scanner( new File( path ) );
			p1= rdr.nextLine();
			p2= rdr.nextLine();
			rdr.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
			return;
		}
		if( p1 == null || p2 == null )
			return;
		//
		inp[1]= p1;
		inp[2]= p2;
		for( pinnable p : pL ){
			if( p.getID().equals( p1 ) )
				pp1= p;
			if( p.getID().equals( p2 ) )
				pp2= p;
		}
		// not not both end good, dont add it.
		if( pp1 != null && pp2 != null ){
			if( pp1 instanceof AbleLocalLink ){
				( (AbleLocalLink)pp1 ).setLinker( pp2 );
			}
			if( pp2 instanceof AbleLocalLink ){
				( (AbleLocalLink)pp2 ).setLinker( pp1 );
			}
			setupLink();
		}else{
			//
			// a bad link, delete it.
			( new File( path ) ).delete();
		}
	}

	private void setupLink() {
		//display.println( ((pinnable)pp1).getCenter().toString() + " " + ((pinnable)pp2).getCenter().toString() );
		if( pp1.getCenter().getX() > pp2.getCenter().getX() ){
			inp[3]= pp2.getCenter().getX();
			inp[4]= pp1.getCenter().getX();
		}else{
			inp[3]= pp1.getCenter().getX();
			inp[4]= pp2.getCenter().getX();
		}
		if( pp1.getCenter().getY() > pp2.getCenter().getY() ){
			inp[5]= pp2.getCenter().getY();
			inp[6]= pp1.getCenter().getY();
		}else{
			inp[5]= pp1.getCenter().getY();
			inp[6]= pp2.getCenter().getY();
		}
		inp[7]= pp1.getCenter();
		inp[8]= pp2.getCenter();
	}

	public void refreshCenter() {
		setupLink();
	}
}
