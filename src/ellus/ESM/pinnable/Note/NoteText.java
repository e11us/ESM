package ellus.ESM.pinnable.Note;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.NoteTxt;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleClickHighlight;
import ellus.ESM.pinnable.Able.AbleClickR;
import ellus.ESM.pinnable.Able.AbleDoubleClick;
import ellus.ESM.pinnable.Able.AbleKeyboardFunInp;
import ellus.ESM.pinnable.Able.AbleKeyboardInput;
import ellus.ESM.pinnable.Able.AbleLocalLink;
import ellus.ESM.pinnable.Able.AbleMouseDrag;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class NoteText extends pin implements AbleSMXConfig, AbleClickHighlight, AbleKeyboardInput, AbleMouseDrag, AbleKeyboardFunInp, AbleClickR, AbleDoubleClick, AbleLocalLink {
	private int			fontS, xOS= 6, yOS= 6, lineSep= 7;
	private Font		font;
	//
	private boolean		InpMode				= false;
	private int			inpModeX			= 0;
	private int			inpModeY			= 0;
	private int			inpModeCurBlinkCount= 0;
	private final int	inpModeCurBlinkThre	= 6;
	private boolean		inpModeCurblinkShow	= false;
	//
	private boolean		mouseDragOn			= false;
	private cor2D		mouseDragOffset		= null;
	//
	private NoteTxt		data;
	// private Object[] para= { ID, x, y, msg, link,
	//	 	bg1C, bg2C, txC, edC, bg1CH, bg2CH, edCH,txCH, dgC, inpCurC
	private Object[]	para				= { null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null };
	//
	// local link. managed by notelink.
	private pinnable	loclink				= null;
	//
	private SManXElm	elm;
	private ESMPD		drawer				= null;
	private boolean		SetXYDone			= false;

	// create new noteText, if file not exists, create new one.
	public NoteText( SManXElm inp, String filepath ) {
		//int fontI, int fontS, int xOS, int yOS, int lineSep, Color[] co, String filepath ) {
		// bg1C, bg2C, edC, txC, bg1CH, bg2CH, edCH, txCH;
		this.elm= inp;
		inp.setPin( this );
		data= new NoteTxt( filepath, para );
		super.setID( (String)para[0] );
		super.setXY( (int)para[1], (int)para[1] + 2, (int)para[2], (int)para[2] + 2 );
		reset();
	}

	@Override
	public void reset() {
		// reset color.
		data.update( elm );
		//
		xOS= elm.getAttr( AttrType._int, "TextXOS" ).getInteger();
		yOS= elm.getAttr( AttrType._int, "TextYOS" ).getInteger();
		lineSep= elm.getAttr( AttrType._int, "TextLineSeperation" ).getInteger();
		int fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
		SetXYDone= false;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( drawer == null )
			drawer= g;
		if( !SetXYDone ){
			this.resetWH();
			SetXYDone= true;
		}
		// check msg.
		if( ( (ArrayList <String>)para[3] ) == null ){
			display.println( this.getClass().toGenericString(), "Error, msg of NoteText is null." );
			return;
		}
		ArrayList <String> msg= ( (ArrayList <String>)para[3] );
		// add itself.
		pan.addGUIactive( this );
		//
		if( InpMode ){
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), (Color)para[9],
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), (Color)para[10],
					true );
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					gp );
			g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					1, (Color)para[13] );
			//
			if( msg == null )
				return;
			for( int i= 0; i < msg.size(); i++ ){
				g.drawString( msg.get( i ), pan.w2bX( super.getXmin() ) + xOS,
						pan.w2bY( super.getYmin() ) + ( lineSep + fontS ) * ( i + 1 ) + yOS,
						(Color)para[12], font );
				// draw cursor.
				if( inpModeCurBlinkCount++ == inpModeCurBlinkThre ){
					inpModeCurBlinkCount= 0;
					inpModeCurblinkShow= !inpModeCurblinkShow;
				}
				if( inpModeCurblinkShow && inpModeY == i ){
					int LX= g.getTxtWid( msg.get( inpModeY ).substring( 0, inpModeX ), font )
							+ pan.w2bX( super.getXmin() ) + xOS;
					g.drawLine( LX, pan.w2bY( super.getYmin() ) + ( lineSep + fontS ) * ( i + 1 ) + yOS,
							LX, pan.w2bY( super.getYmin() ) + ( lineSep + fontS ) * ( i ) + yOS, 3, (Color)para[14] );
				}
			}
		}else{
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), (Color)para[5],
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), (Color)para[6],
					true );
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					gp );
			g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					1, (Color)para[7] );
			//
			if( msg == null )
				return;
			for( int i= 0; i < msg.size(); i++ ){
				g.drawString( msg.get( i ), pan.w2bX( super.getXmin() ) + xOS, pan.w2bY( super.getYmin() ) +
						( lineSep + fontS ) * ( i + 1 ) + yOS, (Color)para[8], font );
			}
		}
		if( ( (String)para[4] ) != null ){
			g.fillOval( pan.w2bX( super.getXmin() ) - 3 + super.getWidth(),
					pan.w2bY( super.getYmin() ) - 1, 4, 4, (Color)para[8] );
		}
	}

	@Override
	public void paintByMouse( ESMPD g, ESMPS pan ) {
		if( ( (ArrayList <String>)para[3] ) == null )
			return;
		//
		ArrayList <String> msg= ( (ArrayList <String>)para[3] );
		int x= pan.mouseCurrentX() - mouseDragOffset.getX();
		int y= pan.mouseCurrentY() - mouseDragOffset.getY();
		// gradient paint. the background of the button polygon.
		Paint gp= new GradientPaint( x, y, (Color)para[13],
				x, y + super.getHeight(), (Color)para[13], true );
		g.fillRect( x, y, super.getWidth(), super.getHeight(), gp );
		//
		if( msg == null )
			return;
		for( int i= 0; i < msg.size(); i++ ){
			g.drawString( msg.get( i ), x + xOS, y + ( lineSep + fontS ) * ( i + 1 ) + yOS,
					(Color)para[8], font );
		}
	}

	@Override
	public void B1clickHighlightOn( int x, int y ) {
		// if first init clear it.
		if( ( (ArrayList <String>)para[3] ).get( 0 ).equals( "@#_NewNote_@#" ) ){
			InpMode= true;
			( (ArrayList <String>)para[3] ).remove( 0 );
			( (ArrayList <String>)para[3] ).add( "" );
			inpModeY= 0;
			inpModeX= 0;
		}else{
			InpMode= true;
			inpModeY= ( (ArrayList <String>)para[3] ).size() - 1;
			inpModeX= ( (ArrayList <String>)para[3] ).get( inpModeY ).length();
		}
	}

	@Override
	public void B1clickHighlightOff( int x, int y ) {
		InpMode= false;
		// reset x and y by that.
		para[1]= super.getXmin();
		para[2]= super.getYmin();
		data.storeNewChange();
	}

	@Override
	public void B1DoubleClickAction( int x, int y ) {
		String link= ( (String)para[4] );
		B1clickHighlightOff( 0, 0 );
		//
		if( link != null ){
			if( link.contains( SCon.webUrlHeader ) ){
				display.println( this.getClass().toString(), "opening web link: " + link );
				try{
					Desktop dt= DeskTop.getDeskTop();
					if( dt != null ){
						dt.browse( new URI( link ) );
					}
				}catch ( IOException e ){
					e.printStackTrace();
					return;
				}catch ( URISyntaxException e ){
					e.printStackTrace();
					return;
				}catch ( Exception e ){
					e.printStackTrace();
					return;
				}
			}else{
				display.println( this.getClass().toString(), "opening local file link: " + link );
				File lik= new File( link );
				if( lik.exists() ){
					try{
						Desktop dt= DeskTop.getDeskTop();
						if( dt != null ){
							dt.open( lik );
						}
					}catch ( IOException e ){
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void B3clickAction( int x, int y ) {
		File selFil= helper.selectDirAndFile( new File( data.getFileFolderPath() ) );
		if( selFil == null )
			return;
		para[4]= selFil.getAbsolutePath();
		data.storeNewChange();
	}

	@Override
	public void FunInp( String inp ) {
		if( ( (ArrayList <String>)para[3] ) == null )
			return;
		ArrayList <String> msg= ( (ArrayList <String>)para[3] );
		if( inp.length() == 2 ){
			switch( inp ){
				case "F2" :
					this.delete();
					data.delete();
					super.removeMe= true;
					break;
				// for contrl pressed.
				case "CC" :
					String res= "";
					for( String tmp : msg ){
						res+= tmp + "\n";
					}
					clipBoard.setString( res );
					break;
				case "CV" :
					String newmsg= clipBoard.getString();
					if( newmsg.contains( SCon.webUrlHeader ) ){
						para[4]= newmsg;
						this.B1clickHighlightOff( 0, 0 );
						break;
					}
					String pre= msg.get( inpModeY ).substring( 0, inpModeX );
					String post= msg.get( inpModeY ).substring( inpModeX, msg.get( inpModeY ).length() );
					msg.remove( inpModeY );
					if( newmsg.contains( "\n" ) ){
						while( newmsg.contains( "\n" ) ){
							if( pre != null ){
								msg.add( inpModeY++ , pre + newmsg.substring( 0, newmsg.indexOf( '\n' ) ) );
								pre= null;
							}else msg.add( inpModeY++ , newmsg.substring( 0, newmsg.indexOf( '\n' ) ) );
							if( newmsg.indexOf( '\n' ) != newmsg.length() - 1 )
								newmsg= newmsg.substring( newmsg.indexOf( '\n' ) + 1, newmsg.length() );
							else{
								newmsg= "";
								break;
							}
						}
						if( newmsg.length() > 0 )
							msg.add( inpModeY++ , newmsg );
						inpModeY-- ;
						msg.add( inpModeY, msg.get( inpModeY ) + post );
						inpModeX= msg.get( inpModeY + 1 ).length();
						msg.remove( inpModeY + 1 );
						resetWH();
					}else{
						msg.add( inpModeY, pre + newmsg + post );
						inpModeX= ( pre + newmsg ).length();
						resetWH();
					}
					break;
				//
				// for arrow key.
				case "AU" :
					if( inpModeY > 0 ){
						inpModeY-- ;
						if( msg.get( inpModeY ).length() < inpModeX ){
							inpModeX= msg.get( inpModeY ).length();
						}
					}
					break;
				case "AD" :
					if( inpModeY < msg.size() - 1 ){
						inpModeY++ ;
						if( msg.get( inpModeY ).length() < inpModeX ){
							inpModeX= msg.get( inpModeY ).length();
						}
					}
					break;
				case "AL" :
					if( inpModeX > 0 )
						inpModeX-- ;
					break;
				case "AR" :
					if( inpModeX < msg.get( inpModeY ).length() )
						inpModeX++ ;
					break;
			}
		}else if( inp.length() == 3 ){
			switch( inp ){
				case "VHO" :
					if( InpMode ){
						inpModeX= 0;
					}
					break;
				case "VEN" :
					if( InpMode ){
						inpModeX= msg.get( inpModeY ).length();
					}
					break;
				case "VDE" :
					if( InpMode ){
						if( inpModeX == msg.get( inpModeY ).length() ){
							if( inpModeY < msg.size() - 1 ){
								String tmp= msg.get( inpModeY ) + msg.get( inpModeY + 1 );
								msg.remove( inpModeY );
								msg.remove( inpModeY );
								msg.add( inpModeY, tmp );
								resetWH();
								break;
							}
						}else{
							String tmp= msg.get( inpModeY );
							tmp= tmp.substring( 0, inpModeX ) + tmp.substring( inpModeX + 1, tmp.length() );
							msg.remove( inpModeY );
							msg.add( inpModeY, tmp );
							resetWH();
							break;
						}
					}
					break;
				default :
					break;
			}
		}
	}

	@Override
	public void keyboardInp( String code ) {
		if( ( (ArrayList <String>)para[3] ) == null )
			return;
		ArrayList <String> msg= ( (ArrayList <String>)para[3] );
		//
		if( code.length() == 1 ){
			if( code.charAt( 0 ) == 27 ){
				this.B1clickHighlightOff( 0, 0 );
				return;
			}
			// for char input. firstly, remove the current line.
			String tmp= msg.get( inpModeY );
			msg.remove( inpModeY );
			//
			if( code.charAt( 0 ) >= 32 && code.charAt( 0 ) <= 126 ){
				// regular char.;
				msg.add( inpModeY, tmp.substring( 0, inpModeX ) + code.charAt( 0 ) +
						tmp.substring( inpModeX, tmp.length() ) );
				inpModeX++ ;
				resetWH();
				return;
			}else if( ( code.charAt( 0 ) ) == 8 ){
				// back space key for delete char
				if( inpModeX > 0 ){
					msg.add( inpModeY, tmp.substring( 0, inpModeX - 1 ) + tmp.substring( inpModeX, tmp.length() ) );
					resetWH();
					inpModeX-- ;
					return;
				}else{
					if( inpModeY > 0 ){
						inpModeY-- ;
						inpModeX= msg.get( inpModeY ).length();
						tmp= msg.get( inpModeY ) + tmp;
						msg.remove( inpModeY );
						msg.add( inpModeY, tmp );
						resetWH();
						return;
					}else{
						msg.add( 0, tmp );
						return;
					}
				}
			}else if( ( code.charAt( 0 ) ) == 10 ){
				// new line.
				msg.add( inpModeY, tmp.substring( 0, inpModeX ) );
				msg.add( inpModeY + 1, tmp.substring( inpModeX, tmp.length() ) );
				inpModeY++ ;
				inpModeX= 0;
				resetWH();
				return;
			}
		}
	}

	@Override
	public void MouseDragOn( cor2D inp ) {
		mouseDragOn= true;
		mouseDragOffset= inp.minu( super.getLocation() );
	}

	@Override
	public void MouseDragOff( cor2D inp ) {
		mouseDragOn= false;
		// reset x,y after drag off.
		inp= inp.minu( mouseDragOffset );
		super.setXY( inp.getX(), inp.getX() + super.getWidth(),
				inp.getY(), inp.getY() + super.getHeight() );
		para[1]= new Integer( inp.getX() );
		para[2]= new Integer( inp.getY() );
		data.storeNewChange();
		//f.f( "drag off: " + para[1] + " " + para[2] + " - " + inp.toString() + " OS " + mouseDragOffset.toString() );
	}

	@Override
	public boolean MouseDragState() {
		return this.mouseDragOn;
	}

	private void resetWH() {
		if( ( (ArrayList <String>)para[3] ) == null || drawer == null )
			return;
		ArrayList <String> msg= ( (ArrayList <String>)para[3] );
		//
		if( msg == null )
			return;
		int w= 0;
		int leng= 0;
		for( String t : msg ){
			leng= drawer.getTxtWid( t, font );
			if( leng > w )
				w= leng;
		}
		// set xy
		super.setXY( (int)para[1], (int)para[1] + w + xOS * 2 + 2, (int)para[2],
				(int)para[2] + msg.size() * ( fontS + lineSep ) + yOS * 4 );
	}

	@Override
	public void setLinker( pinnable inp ) {
		loclink= inp;
	}

	@Override
	public cor2D getJumpLoca() {
		if( loclink == null )
			return null;
		//display.println( this.getClass().toString(), "opening local pinnable link: " + loclink.getID() );
		return loclink.getCenter();
	}

	// for using noteText as a link only.
	public void B3clickActionSec( String path ) {
		File spath= new File( path );
		if( !spath.exists() || !spath.isDirectory() )
			return;
		File selFil= helper.selectDirAndFile( spath );
		if( selFil == null )
			return;
		para[4]= selFil.getAbsolutePath();
		// reset name as well.
		ArrayList <String> ret= new ArrayList <>();
		ret.add( helper.getFileName( selFil.getName() ) );
		para[3]= ret;
		data.storeNewChange();
	}

	public boolean hasLink() {
		if( para[4] != null )
			return true;
		else return false;
	}
}
/*
// use this when readFromDisk() is used after.
public NoteText(  SManXElm inp, int xOS, int yOS, Graphics g, String filepath ) {
	// bg1C, bg2C, txC, bg1CH, bg2CH, txCH;
	this.g= g;
	this.inp= inp;
	this.xOS= xOS;
	this.yOS= yOS;
	this.filepathII= filepath;
	inp.setPin( this );
	reset();
	// try loading the data.
	try{
		data.load();
		super.setID( (String)para[0] );
		super.setXY( (int)para[1], (int)para[1] + 2, (int)para[2], (int)para[2] + 2 );
		resetWH();
	}catch ( Exception ee ){
		ee.printStackTrace();
		display.printErr( this.getClass().toString(), "error loading a NoteText" );
		return;
	}
}

@Override
public void reset() {
	display.println( this.getClass().toGenericString(), "NoteText is reset()." );
	this.fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
	if( fontI >= SCon.FontList.size() )
		fontI= 0;
	this.fontS= inp.getAttr( AttrType._int, "FontSize" ).getInteger();
	this.lineSep= inp.getAttr( AttrType._int, "LineSeperation" ).getInteger();
	//
	Color[] co={ inp.getAttr( AttrType._color, "TextColor" ).getColor(),
			inp.getAttr( AttrType._color, "BackgroundColor1" ).getColor(),
			inp.getAttr( AttrType._color, "BackgroundColor2" ).getColor(),
			inp.getAttr( AttrType._color, "TextHighlightColor" ).getColor(),
			inp.getAttr( AttrType._color, "BackgroundHighlightColor1" ).getColor(),
			inp.getAttr( AttrType._color, "BackgroundHighlightColor2" ).getColor() };
	//
	???

	data= new NoteTxt( filepathII, co, para );
}
*/