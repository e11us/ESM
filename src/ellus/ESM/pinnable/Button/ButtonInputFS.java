package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.Able.AbleClickHighlight;
import ellus.ESM.pinnable.Able.AbleDoubleClick;
import ellus.ESM.pinnable.Able.AbleKeyboardFunInp;
import ellus.ESM.pinnable.Able.AbleKeyboardInput;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonInputFS extends pin implements AbleClickHighlight, AbleKeyboardInput, AbleKeyboardFunInp, AbleDoubleClick, AbleSMXConfig {
	private Color		bg1C, bg2C, txC, bg1CH, bg2CH, txCH, edC, edCH;
	private int			xOS					= 0, yOS= 0;
	private int			fontI				= 0;
	private int			fontS				= 16;
	//
	protected String	defMsg				= null;
	protected boolean	inputMode			= false;
	protected String	inputMsg			= ">> ";
	//
	private int			inpModeCurBlinkCount= 0;
	private final int	inpModeCurBlinkThre	= 6;
	private boolean		inpModeCurblinkShow	= false;
	private int			cutLen				= -1;
	private Font		fon;
	//
	private SManXElm	elm;
	private int			txOSX				= 5, txOSY= 5;

	public ButtonInputFS( int[] list, Color[] colors, Color[] colors2, String msg, int fontInd, int fontsiz ) {
		if( list.length != 6 || colors.length != 5 )
			return;
		this.defMsg= msg;
		// x,y,w,h,wos,hof // bg1C, bg2C, edC, csC, txC
		super.setXY( list[0], list[0] + list[2], list[1], list[1] + list[3] );
		xOS= list[4];
		yOS= list[5];
		bg1C= colors[0];
		bg2C= colors[1];
		txC= colors[4];
		bg1CH= colors2[0];
		bg2CH= colors2[1];
		txCH= colors2[4];
		//
		fontI= fontInd;
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		fontS= fontsiz;
		fon= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	public ButtonInputFS( SManXElm elm, String msg ) {
		// x,y,w,h,wos,hof // bg1C, bg2C, edC, csC, txC
		this.defMsg= msg;
		this.elm= elm;
		elm.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		super.setXY(
				elm.getAttr( AttrType._location, "location" ).getLocation().getX(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getX() +
						elm.getAttr( AttrType._int, "Width" ).getInteger(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY() +
						elm.getAttr( AttrType._int, "Height" ).getInteger() );
		xOS= elm.getAttr( AttrType._int, "WidthOffSet" ).getInteger();
		yOS= elm.getAttr( AttrType._int, "HeightOffSet" ).getInteger();
		txOSX= elm.getAttr( AttrType._int, "TextOffSetX" ).getInteger();
		txOSY= elm.getAttr( AttrType._int, "TextOffSetY" ).getInteger();
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextMsgColor" ).getColor();
		bg1CH= elm.getAttr( AttrType._color, "BackgroundColorHighlighted1" ).getColor();
		bg2CH= elm.getAttr( AttrType._color, "BackgroundColorHighlighted2" ).getColor();
		edCH= elm.getAttr( AttrType._color, "EdgeColorHighlighted" ).getColor();
		txCH= elm.getAttr( AttrType._color, "TextMsgColorHighlighted" ).getColor();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		//
		fon= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		pan.addGUIactive( this );
		//
		if( !inputMode ){
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2C, true );
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					gp );
			// draw input text underline.
			g.drawLine( pan.w2bX( super.getXmin() ) + xOS, pan.w2bY( super.getYmin() ) + super.getHeight() - yOS,
					pan.w2bX( super.getXmin() ) + super.getWidth() - xOS,
					pan.w2bY( super.getYmin() ) + super.getHeight() - yOS,
					3, txC );
			// draw String now.
			if( defMsg == null )
				defMsg= "";
			g.drawString( defMsg, pan.w2bX( super.getXmin() ) + txOSX + xOS,
					pan.w2bY( super.getYmin() ) - txOSY + super.getHeight() - yOS, txC, fon );
		}else{
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1CH,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2CH, true );
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					gp );
			// draw input text underline.
			g.drawLine( pan.w2bX( super.getXmin() ) + xOS, pan.w2bY( super.getYmin() ) + super.getHeight() - yOS,
					pan.w2bX( super.getXmin() ) + super.getWidth() - xOS,
					pan.w2bY( super.getYmin() ) + super.getHeight() - yOS, 3, txC );
			// draw String now.
			// test width.
			if( g.getTxtWid( inputMsg, fon ) > super.getWidth() - 3 + xOS * 2 ){
				if( cutLen == -1 )
					cutLen= inputMsg.length() - 1;
				g.drawString( inputMsg.substring( inputMsg.length() - cutLen, inputMsg.length() ),
						pan.w2bX( super.getXmin() ) + txOSX + xOS,
						pan.w2bY( super.getYmin() ) - txOSY + super.getHeight() - yOS, txCH, fon );
			}else{
				cutLen= -1;
				g.drawString( inputMsg, pan.w2bX( super.getXmin() ) + txOSX + xOS,
						pan.w2bY( super.getYmin() ) - txOSY + super.getHeight() - yOS, txCH, fon );
			}
			// draw cursor.
			if( inpModeCurBlinkCount++ == inpModeCurBlinkThre ){
				inpModeCurBlinkCount= 0;
				inpModeCurblinkShow= !inpModeCurblinkShow;
			}
			if( inpModeCurblinkShow ){
				if( cutLen != -1 ){
					int LX= g.getTxtWid( inputMsg.substring( inputMsg.length() - cutLen, inputMsg.length() ), fon )
							+ pan.w2bX( super.getXmin() ) + txOSX + xOS;
					g.drawLine( LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS - fontS,
							LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, 3, txCH );
				}else{
					int LX= g.getTxtWid( inputMsg, fon ) + pan.w2bX( super.getXmin() ) + txOSX + xOS;
					g.drawLine( LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS - fontS,
							LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, 3, txCH );
				}
			}
		}
	}

	@Override
	public void keyboardInp( String code ) {
		if( code.length() == 1 ){
			// for char input.
			if( code.charAt( 0 ) >= 32 && code.charAt( 0 ) <= 126 ){
				// regular char.;
				inputMsg= inputMsg + code.charAt( 0 );
				return;
			}else if( ( code.charAt( 0 ) ) == 8 ){
				// back space key for delete char
				if( inputMsg.length() > 3 )
					inputMsg= inputMsg.substring( 0, inputMsg.length() - 1 );
			}
		}
	}

	@Override
	public void B1clickHighlightOn( int x, int y ) {
		inputMode= true;
		inputMsg= ">> ";
	}

	@Override
	public void B1clickHighlightOff( int x, int y ) {
		inputMode= false;
	}

	public boolean isInputMode() {
		return inputMode;
	}

	public String getInputMsg() {
		return inputMsg.substring( 3, inputMsg.length() );
	}

	public void resetInputMsg() {
		inputMsg= ">> ";
	}

	@Override
	public void FunInp( String code ) {
		if( code.length() == 2 ){
			switch( code ){
				case "CV" :
					String newmsg= clipBoard.getString();
					if( newmsg != null && newmsg.length() > 0 )
						inputMsg= inputMsg + newmsg;
					break;
			}
		}
	}

	@Override
	public void B1DoubleClickAction( int x, int y ) {
		// TODO Auto-generated method stub
	}
}
