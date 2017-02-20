package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleClickR;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardInput;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;
import ellus.ESM.pinnable.able_Interface.AbleMouseWheel;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



/*
 * very similar to panel display.
 *
 * a hybrid of notetext and panel display.
 * display things like panel display, but can edit content and navigate like note text.
 */
public class PanelMultiLineInput extends pinLF implements AbleSMXConfig, AbleClickR, AbleClickHighlight, AbleClick, AbleMouseWheel, AbleDoubleClick, AbleMouseDrag, AbleHoverHighlight, AbleKeyboardFunInp, AbleKeyboardInput {
	private Color				bg1C, bg2C, edC, txC, curC;
	private ArrayList <String>	tx;
	private int					txtOSX				= 15, txtOSY= 15;
	private int					LineSep				= 5;
	private int					xo, yo;
	//
	private int					inpModeX			= 0;
	private int					inpModeY			= 0;
	private int					inpModeCurBlinkCount= 0;
	private final int			inpModeCurBlinkThre	= 20;
	private boolean				inpModeCurblinkShow	= false;
	private int					fontI				= 4, fontS= 26;
	private int					indS;
	private int					indE;
	private Font				font;
	private SManXElm			elm;

	public PanelMultiLineInput( SManXElm elm, ArrayList <String> tx ) {
		//int xo, int yo, int fontI, Color[] co, ArrayList <String> tx, Graphics g ) {//bg1C, bg2C, edC, txC;
		this.tx= tx;
		this.elm= elm;
		elm.setPin( this );
		// take up whole possible screen.
		super.setXY( Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE );
		reset();
	}

	@Override
	public void reset() {
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		curC= elm.getAttr( AttrType._color, "CursorColor" ).getColor();
		xo= elm.getAttr( AttrType._int, "ToEdgeDistanceX" ).getInteger();
		yo= elm.getAttr( AttrType._int, "ToEdgeDistanceY" ).getInteger();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	public String getTxt() {
		String res= "";
		for( String tmp : tx ){
			if( res.length() != 0 )
				res= res + "\n" + tmp;
			else res= tmp;
		}
		return res;
	}

	@Override
	public void FunInp( String inp ) {
		if( ( tx ) == null )
			return;
		ArrayList <String> msg= tx;
		if( inp.length() == 2 ){
			switch( inp ){
				case "CV" :
					String newmsg= clipBoard.getString();
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
					}else{
						msg.add( inpModeY, pre + newmsg + post );
						inpModeX= ( pre + newmsg ).length();
					}
					break;
				case "CC" :
					String res= "";
					for( String tmp : msg ){
						res+= tmp + "\n";
					}
					clipBoard.setString( res );
					break;
				//
				// for arrow key.
				case "AU" :
					if( inpModeY > 0 ){
						inpModeY-- ;
						if( msg.get( inpModeY ).length() < inpModeX ){
							inpModeX= msg.get( inpModeY ).length();
						}
						if( inpModeY < indS )
							indS--;
					}
					break;
				case "AD" :
					if( inpModeY < msg.size() - 1 ){
						inpModeY++ ;
						if( msg.get( inpModeY ).length() < inpModeX ){
							inpModeX= msg.get( inpModeY ).length();
						}
						if( inpModeY > indE )
							indS++;
					}
					break;
				case "AL" :
					if( inpModeX > 0 )
						inpModeX-- ;
					else {
						if( inpModeY > 0 ) {
							inpModeY--;
							inpModeX= msg.get( inpModeY ).length();
							if( inpModeY < indS )
								indS--;
						}
					}
					break;
				case "AR" :
					if( inpModeX < msg.get( inpModeY ).length() )
						inpModeX++ ;
					else {
						if( inpModeY < msg.size() - 1 ) {
							inpModeY++;
							inpModeX= 0;
							if( inpModeY > indE )
								indS++;
						}
					}
					break;
			}
		}else if( inp.length() == 3 ){
			switch( inp ){
				case "VHO" :
					inpModeX= 0;
					break;
				case "VEN" :
					inpModeX= msg.get( inpModeY ).length();
					break;
				case "VDE" :
					if( inpModeX == msg.get( inpModeY ).length() ){
						if( inpModeY < msg.size() - 1 ){
							String tmp= msg.get( inpModeY ) + msg.get( inpModeY + 1 );
							msg.remove( inpModeY );
							msg.remove( inpModeY );
							msg.add( inpModeY, tmp );
							break;
						}
					}else{
						String tmp= msg.get( inpModeY );
						tmp= tmp.substring( 0, inpModeX ) + tmp.substring( inpModeX + 1, tmp.length() );
						msg.remove( inpModeY );
						msg.add( inpModeY, tmp );
						break;
					}
					break;
				default :
					break;
			}
		}
	}

	@Override
	public void keyboardInp( String code ) {
		if( ( tx ) == null )
			return;
		ArrayList <String> msg= tx;
		//
		if( code.length() == 1 ){
			// for char input. firstly, remove the current line.
			String tmp= msg.get( inpModeY );
			msg.remove( inpModeY );
			//
			if( code.charAt( 0 ) >= 32 && code.charAt( 0 ) <= 126 ){
				// regular char.;
				msg.add( inpModeY, tmp.substring( 0, inpModeX ) + code.charAt( 0 ) +
						tmp.substring( inpModeX, tmp.length() ) );
				inpModeX++ ;
				return;
			}else if( ( code.charAt( 0 ) ) == 8 ){
				// back space key for delete char
				if( inpModeX > 0 ){
					msg.add( inpModeY, tmp.substring( 0, inpModeX - 1 ) + tmp.substring( inpModeX, tmp.length() ) );
					inpModeX-- ;
					return;
				}else{
					if( inpModeY > 0 ){
						inpModeY-- ;
						inpModeX= msg.get( inpModeY ).length();
						tmp= msg.get( inpModeY ) + tmp;
						msg.remove( inpModeY );
						msg.add( inpModeY, tmp );
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
				//
				if( inpModeY + 1 == indE )
					indS++;
				//
				return;
			}
		}
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// add to non LF instead so it can consume all the input.
		pan.addGUIactive( this );
		// add itself.
		//
		Paint gp= new GradientPaint( xo, yo, bg1C, xo, pan.getHeight() - yo, bg2C );
		g.fillRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, gp );
		g.drawRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, 1, edC );
		// draw the strings.
		for( int i= indS; i < tx.size(); i++ ){
			// test if last line.
			if( yo + 15 + ( LineSep + fontS ) * ( i - indS + 2 ) > pan.getHeight() - yo )
				break;
			// draw cursor.
			if( inpModeCurBlinkCount++ == inpModeCurBlinkThre ){
				inpModeCurBlinkCount= 0;
				inpModeCurblinkShow= !inpModeCurblinkShow;
			}
			if( inpModeCurblinkShow && inpModeY == i ){
				int LX= 0;
				LX= g.getTxtWid( tx.get( inpModeY ).substring( 0, inpModeX ), font ) + xo + txtOSX;
				
					
				g.drawLine( LX, yo + txtOSY + ( LineSep + fontS ) * ( i-indS + 1 ),
						LX, yo + txtOSY + ( LineSep + fontS ) * ( i-indS ), 3, curC );
			}
			// draw string.
			g.drawString( tx.get( i ), xo + 15, yo + 15 + ( LineSep + fontS ) * ( i - indS + 1 ),
					txC, font );
			// set last.
			indE= i;
		}
		if( indE < inpModeY ) {
			inpModeY= indE-1;
			inpModeX= 0;
		}
		if( indS > inpModeY ) {
			inpModeY= indS;
			inpModeX= 0;
		}
	}

	/*
	 * 
	 * 
	 * 
	 * (non-Javadoc)
	 * @see ellus.ESM.pinnable.able_Interface.AbleMouseWheel#WheelRotateAction(int)
	 */
	@Override
	public void WheelRotateAction( int rot ) {
		indS+= rot;
		if( indS > tx.size() - 1 )
			indS= tx.size() - 1;
		if( indS < 0 )
			indS= 0;
	}

	@Override
	public boolean MouseDragState() {
		return false;
	}

	@Override
	public void B3clickAction( int x, int y ) {
		this.removeMe= true;
	}

	@Override
	public void HoverHighlightOn() {}

	@Override
	public void HoverHighlightOff() {}

	@Override
	public void MouseDragOn( cor2D inp ) {}

	@Override
	public void MouseDragOff( cor2D inp ) {}

	@Override
	public void paintByMouse( ESMPD g, ESMPS pan ) {}

	@Override
	public void B1clickAction( int x, int y ) {}

	@Override
	public void B1clickHighlightOn( int x, int y ) {}

	@Override
	public void B1clickHighlightOff( int x, int y ) {}

	@Override
	public void B1DoubleClickAction( int x, int y ) {}
}
