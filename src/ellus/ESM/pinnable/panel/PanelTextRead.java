package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.f;
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
 * display all text. 
 * support line wrap. ( wrap will only work when input english. )
 *
 */
public class PanelTextRead extends pinLF implements AbleSMXConfig, AbleClickR, AbleClickHighlight, AbleClick, AbleMouseWheel, AbleDoubleClick, AbleMouseDrag, AbleHoverHighlight  {
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
	private printOut PO= new printOut();
	private int[] charWidth= null;
	private char lineWrapSig= (char)1;

	public PanelTextRead( SManXElm elm, ArrayList <String> tx ) {
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
	public void paint( ESMPD g, ESMPS pan ) {
		// add to non LF instead so it can consume all the input.
		pan.addGUIactive( this );
		// add itself.
		//
		if( charWidth == null ) {
			setCharWidth( g );
			return;
		}
		Paint gp= new GradientPaint( xo, yo, bg1C, xo, pan.getHeight() - yo, bg2C );
		g.fillRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, gp );
		g.drawRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, 1, edC );
		//
		ArrayList<String> curLine= PO.print( g, pan );
		//
		// draw the strings.
		for( int i= 0; i < curLine.size(); i++ ){
			if( yo + 15 + ( LineSep + fontS ) * ( i + 2 ) > pan.getHeight() - yo )
				break;	
			g.drawString( curLine.get( i ), xo + 15, yo + 15 + ( LineSep + fontS ) * ( i - indS + 1 ),
					txC, font );
		}
	}
	
	
	
	/*||----------------------------------------------------------------------------------------------
	 ||| 
	||||--------------------------------------------------------------------------------------------*/
	class printOut {
		int indStart= 0;
		int firstLineTot= 0;
		int indSubStart= 0;
		int indEnd= 0;
		ArrayList<String> lines;
		ArrayList<Integer> lineInd;
		//
		int ArrX= 0, ArrY= 0;
		
		ArrayList<String> print( ESMPD g, ESMPS pan ){
			//
			lines= new ArrayList<>();
			lineInd= new ArrayList<>();
			for( int i= indStart; i < tx.size(); i++ ){
				// if total lines - first lines is limit, break.
				if( ( LineSep + fontS ) * ( lines.size() - firstLineTot ) > pan.getHeight() - yo * 2 )
					break;	
				//
				lines.addAll( wrapLine( new String( tx.get( i ) ), pan.getWidth() - xo * 3 ) );
				while( lineInd.size() < lines.size() )
					lineInd.add( new Integer( i ) );
				//
				if( i == indStart ) 
					firstLineTot= lines.size();
				indEnd= i;
			}
			//
			if( indSubStart == -1 ) {
				indSubStart= firstLineTot - 1;
			}
			for( int i= 0; i < indSubStart; i++ ) {
				lines.remove( 0 );
			}
			//
			while( ( LineSep + fontS ) * ( lines.size()  ) > pan.getHeight() - yo * 2 ) {
				lines.remove( lines.size() - 1 );
			}
			return lines;
		}

		void lookUp(){
			if( indSubStart == 0 && indStart > 0) {
				indStart--;
				indSubStart= -1; 
			}else if( indSubStart > 0 )
				indSubStart--;
		}
		
		void lookDown() {
			if( ArrY + 1 > lines.size() - 3 ) {
				ArrY--;
			}
			//
			if( indSubStart >= ( firstLineTot - 1 ) ){
				if( indStart < tx.size() - 1 ) {
					indStart++;
					indSubStart= 0;
				}else {
					indSubStart= 	( firstLineTot - 1 );
				}
			}else {
				indSubStart++;
			}
		}
	}
	
	private ArrayList<String> wrapLine( String line, int wid ) {
		int tot= 0;
		char cha;
		int lastSpace= -1;
		ArrayList<String> ret= new ArrayList<String>();
		boolean end= false;
		//
		while( !end ) {
			lastSpace= -1;
			tot= 0;
			end= true;
			for( int i= 0; i < line.length(); i++ ) {
				cha= line.charAt( i );
				if( cha == ' ' )
					lastSpace= i;
				if( cha >= 32 && cha <= 126 ) {
					tot+= charWidth[cha-32];
				}else
					tot+= charWidth[0]*2;
				if( tot > wid ) {
					if( lastSpace != -1 ) {
						ret.add( line.substring( 0, lastSpace ) );
						line= line.substring( lastSpace, line.length() );
						end= false;
						break;
					}else {
						ret.add( line.substring( 0, i - 1 ) );
						line= line.substring( i - 1, line.length() );
						end= false;
						break;
					}
				}
			}
		}
		ret.add(  line  );
		return ret;
	}
	
	private void setCharWidth( ESMPD g ) {
		charWidth= new int[126-32+1];
		for( int i= 0; i < charWidth.length; i++ ) {
			charWidth[i]= g.getTxtWid( "" + ((char)(i+32)), font );
		}
	}
	
	/*||----------------------------------------------------------------------------------------------
	 ||| 
	||||--------------------------------------------------------------------------------------------*/
	@Override
	public void WheelRotateAction( int rot ) {
		if( rot > 0 )
			PO.lookDown();
		else
			PO.lookUp();
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






















