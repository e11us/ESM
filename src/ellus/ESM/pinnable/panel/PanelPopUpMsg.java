package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import org.joda.time.DateTime;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pin2ScreenLocationer;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.Able.AbleClick;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.pinnable.Able.AbleSelfClose;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXElm;
import ellus.ESM.setting.SManXAttr.AttrType;

// a short pop up msg always displayed at middle of screen.
public class PanelPopUpMsg extends pinLF implements AbleSMXConfig, AbleClick, AbleSelfClose{
	private Color				bg1C, bg2C, edC, txC, curC, txC2;
	private int					fontI				= 4, fontS= 26;
	private int edsize;
	private Font				font, font2;
	private SManXElm			elm;
	private String msg;
	private boolean locaSet= false;
	private long startD= 0;
	private long sec= -1;
	
	public PanelPopUpMsg( SManXElm elm, String msg ) {
		this.msg= msg;
		this.elm= elm;
		elm.setPin( this );
		reset();
	}
	
	@Override
	public void reset() {
		super.setXY(0, elm.getAttr( AttrType._int, "Width" ).getInteger(),
				0, elm.getAttr( AttrType._int, "Height" ).getInteger() );
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		txC2= elm.getAttr( AttrType._color, "TextColor2" ).getColor();
		edsize= elm.getAttr( AttrType._int, "EdgeSize" ).getInteger();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
		font2= SCon.FontList.get( 5 ).deriveFont( (float)12 );
		locaSet= false;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( !locaSet ) {
			pin2ScreenLocationer.centerThisXY( this, pan );
			locaSet= true;
			return;
		}
		if( startD == 0 )
			startD= helper.getTimeLong();
		if( sec != -1 && startD + sec * 1000 < helper.getTimeLong() ) {
			this.B1clickAction( 0, 0 );
		}
		pan.addGUIactiveLF( this );
		//
		Paint gp= new GradientPaint( super.getXmin(),  super.getYmin() , bg1C,
				 super.getXmin() ,  super.getYmin()  + super.getHeight(), bg2C, true );
		//
		g.fillRect(  super.getXmin(), super.getYmin(),super.getWidth(), super.getHeight(), gp );
		g.drawRect(  super.getXmin() , super.getYmin(),
				super.getWidth(), super.getHeight(), edsize, edC );
		// draw msg in mid.
		g.drawString( msg,  super.getXmin() + super.getWidth() / 2 - g.getTxtWid( msg, font ) / 2,
				 super.getYmin() + super.getHeight() / 2 + fontS / 2, txC, font );
		//
		g.drawString( "(click on me to close)", super.getXmin() + 5 + edsize ,
				super.getYmax() - 5 - edsize, txC2, font2 );
	}
	
	@Override
	public void B1clickAction( int x, int y ) {
		this.removeMe= true;
	}

	@Override
	public void setSelfCloseTime( int sec ) {
		this.sec= sec;
	}
}
