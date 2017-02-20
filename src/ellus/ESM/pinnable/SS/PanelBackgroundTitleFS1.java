package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.f;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AblePanelTitle;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;

public class PanelBackgroundTitleFS1 extends pinSS implements AbleSMXConfig , AblePanelTitle{
	private SManXElm	inp				= null;
	private ESMPS		PS;
	//
	private int			TitleHeight		= 0;
	private int			TitleSeperation		= 0;
	private int			SquareSize		= 0;
	private int			VerticalLineThick		= 0;
	private int buttomLineThic, buttomLineSep;
	private Color		hlC, vlC, SquareColor, ButtomLineColor, txC, txbC;
	private double bounceX= 0;
	private double buttomSquareSpeedPerFrame= 0;
	private Font fon;
	private String title= null;
	private int end= 0;

	public PanelBackgroundTitleFS1( SManXElm inp, ESMPS PS, String title ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		this.title= title;
		reset();
	}

	
	@Override
	public void reset() {
		this.hlC= inp.getAttr( SManXAttr.AttrType._color, "TitleColor" ).getColor();
		this.vlC= inp.getAttr( SManXAttr.AttrType._color, "VerticalLineColor" ).getColor();
		this.SquareColor= inp.getAttr( SManXAttr.AttrType._color, "SquareColor" ).getColor();
		this.ButtomLineColor= inp.getAttr( SManXAttr.AttrType._color, "ButtomLineColor" ).getColor();
		this.TitleHeight= inp.getAttr( SManXAttr.AttrType._int, "TitleHeight" ).getInteger();
		this.TitleSeperation= inp.getAttr( SManXAttr.AttrType._int, "TitleSeperation" ).getInteger();
		this.SquareSize= inp.getAttr( SManXAttr.AttrType._int, "SquareSize" ).getInteger();
		this.VerticalLineThick= inp.getAttr( SManXAttr.AttrType._int, "VerticalLineThick" ).getInteger();
		this.buttomLineThic= inp.getAttr( SManXAttr.AttrType._int, "buttomLineThic" ).getInteger();
		this.buttomLineSep= inp.getAttr( SManXAttr.AttrType._int, "buttomLineSep" ).getInteger();
		this.buttomSquareSpeedPerFrame= inp.getAttr( SManXAttr.AttrType._double, "buttomSquareSpeedPerFrame" ).getDouble();
		if( buttomSquareSpeedPerFrame == 0 )
			buttomSquareSpeedPerFrame= 7.1;
		bounceX= SquareSize;
		if( buttomLineSep == 0 )
			buttomLineSep= 1;
		if( buttomLineThic == 0 )
			buttomLineThic= 1;
		txC= inp.getAttr( SManXAttr.AttrType._color, "FontColor" ).getColor();
		txbC= inp.getAttr( SManXAttr.AttrType._color, "FontBgColor" ).getColor();
		fon= SCon.FontList.get( inp.getAttr( SManXAttr.AttrType._int, "FontIndex" ).getInteger() )
				.deriveFont( (float )inp.getAttr( SManXAttr.AttrType._int, "FontSize" ).getInteger());
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		g.fillRect( 0, 0, pan.getWidth() , TitleHeight, hlC );
		//
		g.fillRect( pan.getWidth() - SquareSize, 0, SquareSize, SquareSize, SquareColor );
		g.fillRect( pan.getWidth() - SquareSize, TitleHeight - SquareSize, SquareSize, SquareSize, SquareColor );
		//
		if( title != null ) {
			end= g.getTxtWid( title, fon );
			g.fillRect( 0, 0, end + 2, TitleHeight, txbC );
			g.drawString( title, 2, TitleHeight - 5, txC, fon );
			g.fillRect( end - SquareSize + 2 , 0, SquareSize, SquareSize, SquareColor );
			g.fillRect( 0, 0, SquareSize, SquareSize, SquareColor );
			// exit box.
			g.drawRect( end + 5, 0, TitleHeight , TitleHeight -1 , 1, txbC );
			g.drawLine( end + 5, 0, end + 5 + TitleHeight , TitleHeight -1 , 1, txC );
			g.drawLine( end + 5 + TitleHeight , 0, end + 5 , TitleHeight  -1, 1, txC );
		}else {
			g.fillRect( 0, 0, SquareSize, SquareSize, SquareColor );
			// exit box.
			end= SquareSize + 3;
			g.drawRect( end + 5, 0, TitleHeight , TitleHeight -1 , 1, txbC );
			g.drawLine( end + 5, 0, end + 5 + TitleHeight , TitleHeight -1 , 1, txC );
			g.drawLine( end + 5 + TitleHeight , 0, end + 5 , TitleHeight  -1, 1, txC );
		}
		//
		g.drawLine( 0, TitleHeight + TitleSeperation, pan.getWidth(), TitleHeight + TitleSeperation,
				VerticalLineThick, vlC );
		g.drawLine( 0, TitleHeight + TitleSeperation, 0, pan.getHeight(),
				VerticalLineThick, vlC );
		g.drawLine( pan.getWidth()-VerticalLineThick/2, TitleHeight + TitleSeperation,
				pan.getWidth()-VerticalLineThick/2, pan.getHeight(),	VerticalLineThick, vlC );
		for( int i= SquareSize + 1; i < pan.getWidth(); i+= buttomLineSep ) {
			g.drawLine( i, pan.getHeight(), i, pan.getHeight() - SquareSize, buttomLineThic, ButtomLineColor );
		}
		g.fillRect( 0, pan.getHeight() - SquareSize, SquareSize, SquareSize, SquareColor );
		g.fillRect( pan.getWidth() - SquareSize, pan.getHeight() - SquareSize, SquareSize, SquareSize, SquareColor );
		//
		bounceX+= buttomSquareSpeedPerFrame;
		if( bounceX >= pan.getWidth() - SquareSize || bounceX < 0 )
			buttomSquareSpeedPerFrame*= -1;
		g.fillRect( (int) ( bounceX ), pan.getHeight() - SquareSize, SquareSize, SquareSize, SquareColor );
		//
	}

	public boolean isCloseClicked( int x, int y ) {
		if( x > ( end + 5 ) && x < ( end + 5 + TitleHeight ) &&
				y > 0 && y < TitleHeight )
			return true;
		else return false;
	}
}

