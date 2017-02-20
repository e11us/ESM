package ellus.ESM.pinnable.Note;

import java.awt.Color;
import java.awt.Image;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.display;
import ellus.ESM.data.NoteImg;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleLocalLink;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;



public class NoteImage extends pin implements AbleMouseDrag, AbleKeyboardFunInp, AbleClickHighlight, AbleLocalLink {
	Object[]			para			= { null, null, null, null, null, null };
	NoteImg				data;
	//
	private boolean		mouseDragged	= false;
	private cor2D		mouseDragOffset	= null;
	private boolean		highlighted		= false;
	//
	// local link. managed by notelink.
	private pinnable	loclink			= null;

	// not exitst. create new.
	public NoteImage( String path, String folder, int x, int y ) {
		data= new NoteImg( path, para );
		data.init( super.getID(), folder, x, y );
		//
		data.load();
		try{
			super.setID( (String)para[0] );
			super.setXY( (int)para[1], (int)para[1] + (int)para[4],
					(int)para[2], (int)para[2] + (int)para[5] );
		}catch ( Exception ee ){
			ee.printStackTrace();
			display.printErr( this.getClass().toString(), "error loading note image." );
			return;
		}
	}

	// does exist, fetch it.
	public NoteImage( String path ) {
		data= new NoteImg( path, para );
		//
		data.load();
		try{
			super.setID( (String)para[0] );
			super.setXY( (int)para[1], (int)para[1] + (int)para[4],
					(int)para[2], (int)para[2] + (int)para[5] );
		}catch ( Exception ee ){
			ee.printStackTrace();
			display.printErr( this.getClass().toString(), "error loading note image." );
			return;
		}
	}

	@Override
	public void FunInp( String inp ) {
		if( inp.length() == 2 ){
			switch( inp ){
				case "F2" :
					this.delete();
					super.removeMe= true;
					data.delete();
					break;
			}
		}
	}

	@Override
	public void MouseDragOn( cor2D inp ) {
		mouseDragged= true;
		mouseDragOffset= inp.minu( super.getLocation() );
	}

	@Override
	public void MouseDragOff( cor2D inp ) {
		mouseDragged= false;
		inp= inp.minu( mouseDragOffset );
		super.setXY( inp.getX(), inp.getX() + super.getWidth(),
				inp.getY(), inp.getY() + super.getHeight() );
		para[1]= new Integer( inp.getX() );
		para[2]= new Integer( inp.getY() );
		data.store();
	}

	@Override
	public boolean MouseDragState() {
		return mouseDragged;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( para[3] == null )
			return;
		// add itself.
		pan.getGUIactive().add( this );
		//
		if( highlighted ){
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
					super.getWidth(), super.getHeight(), Color.white );
		}
		g.drawImage( (Image)para[3], pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) );
	}

	@Override
	public void paintByMouse( ESMPD g, ESMPS pan ) {
		if( para[3] == null )
			return;
		int x= pan.mouseCurrentX() - mouseDragOffset.getX();
		int y= pan.mouseCurrentY() - mouseDragOffset.getY();
		g.drawImage( (Image)para[3], x, y );
	}

	@Override
	public void setLinker( pinnable inp ) {
		loclink= inp;
	}

	@Override
	public cor2D getJumpLoca() {
		return loclink.getLocation();
	}

	@Override
	public void B1clickHighlightOn( int x, int y ) {
		highlighted= true;
	}

	@Override
	public void B1clickHighlightOff( int x, int y ) {
		highlighted= false;
	}
}