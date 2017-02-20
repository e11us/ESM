package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardInput;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;



public class ButtonInputFS extends pin implements AbleClickHighlight, AbleKeyboardInput, AbleKeyboardFunInp, AbleDoubleClick {
	private Color		bg1C, bg2C, txC, bg1CH, bg2CH, txCH;
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
			g.drawString( defMsg, pan.w2bX( super.getXmin() ) + 5 + xOS,
					pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, txC, fon );
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
						pan.w2bX( super.getXmin() ) + 3 + xOS,
						pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, txCH, fon );
			}else{
				cutLen= -1;
				g.drawString( inputMsg, pan.w2bX( super.getXmin() ) + 3 + xOS,
						pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, txCH, fon );
			}
			// draw cursor.
			if( inpModeCurBlinkCount++ == inpModeCurBlinkThre ){
				inpModeCurBlinkCount= 0;
				inpModeCurblinkShow= !inpModeCurblinkShow;
			}
			if( inpModeCurblinkShow ){
				if( cutLen != -1 ){
					int LX= g.getTxtWid( inputMsg.substring( inputMsg.length() - cutLen, inputMsg.length() ), fon )
							+ pan.w2bX( super.getXmin() ) + 5 + xOS;
					g.drawLine( LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS - fontS,
							LX, pan.w2bY( super.getYmin() ) - 5 + super.getHeight() - yOS, 3, txCH );
				}else{
					int LX= g.getTxtWid( inputMsg, fon ) + pan.w2bX( super.getXmin() ) + 5 + xOS;
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

	public String getInputMsg() {
		return inputMsg.substring( 3, inputMsg.length() );
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
