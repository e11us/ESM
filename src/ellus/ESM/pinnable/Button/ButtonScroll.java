package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleMouseWheel;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonScroll extends pin implements AbleHoverHighlight, AbleMouseWheel, AbleSMXConfig {
	private Color		bg1C, bg2C, edC, txC, brC, bg1CH, bg2CH;
	private int			fontS, fontI;
	private Font		font;
	private int			barSize, barNum, barSep, edsize;
	private SManXElm	elm;
	private boolean		isHovered	= false;
	private int			ind			= 0;
	private int			indTot		= 0;
	private int			xOS, xOS2;

	public ButtonScroll( SManXElm elm, int x, int y, int indTot ) {
		this.elm= elm;
		super.setXY( x, x + 1, y, y + 1 );
		elm.setPin( this );
		this.indTot= indTot;
		reset();
	}

	@Override
	public void reset() {
		super.setXY(
				super.getXmin(), super.getXmin() +
						elm.getAttr( AttrType._int, "Width" ).getInteger(),
				super.getYmin(), super.getYmin() +
						elm.getAttr( AttrType._int, "Height" ).getInteger() );
		barNum= elm.getAttr( AttrType._int, "BarNumberTotal" ).getInteger();
		barSize= elm.getAttr( AttrType._int, "BarSize" ).getInteger();
		edsize= elm.getAttr( AttrType._int, "EdgeSize" ).getInteger();
		barSep= elm.getAttr( AttrType._int, "BarSeperation" ).getInteger();
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		brC= elm.getAttr( AttrType._color, "BarColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		xOS= elm.getAttr( AttrType._int, "FontXOS" ).getInteger();
		xOS2= elm.getAttr( AttrType._int, "FontXOS2" ).getInteger();
		bg1CH= elm.getAttr( AttrType._color, "BackgroundColorHighlighted1" ).getColor();
		bg2CH= elm.getAttr( AttrType._color, "BackgroundColorHighlighted2" ).getColor();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		//
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( fontS == 0 )
			return;
		pan.addGUIactive( this );
		//
		Paint gp;
		if( !isHovered ){
			gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2C, true );
		}else{
			gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1CH,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2CH, true );
		}
		//
		g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), gp );
		g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), edsize, edC );
		for( int i= 0; i < barNum; i++ ){
			g.drawLine( pan.w2bX( super.getXmin() ) + barSep * ( i + 1 ),
					pan.w2bY( super.getYmin() ) + edsize,
					pan.w2bX( super.getXmin() ) + edsize,
					pan.w2bY( super.getYmin() ) + barSep * ( i + 1 ),
					barSize, brC );
			g.drawLine( pan.w2bX( super.getXmax() ) - barSep * ( i + 1 ),
					pan.w2bY( super.getYmax() ) - edsize,
					pan.w2bX( super.getXmax() ) - edsize,
					pan.w2bY( super.getYmax() ) - barSep * ( i + 1 ),
					barSize, brC );
		}
		//
		g.drawLine( pan.w2bX( super.getXmin() ) + super.getWidth() / 2 + super.getHeight() / 2,
				pan.w2bY( super.getYmin() ) + edsize,
				pan.w2bX( super.getXmin() ) + super.getWidth() / 2 - super.getHeight() / 2,
				pan.w2bY( super.getYmax() ) - edsize, edsize, edC );
		g.drawString( ( 1 + ind ) + "", xOS + pan.w2bX( super.getXmin() ),
				pan.w2bY( super.getYmin() ) + super.getHeight() / 2 + fontS / 2, txC, font );
		g.drawString( indTot + "", xOS2 + pan.w2bX( super.getXmin() ),
				pan.w2bY( super.getYmin() ) + super.getHeight() / 2 + fontS / 2, txC, font );
	}

	public int getIndex() {
		return ind;
	}

	public void resetTot( int tot ) {
		this.indTot= tot;
		ind= 0;
	}

	@Override
	public void HoverHighlightOn() {
		isHovered= true;
	}

	@Override
	public void HoverHighlightOff() {
		isHovered= false;
	}

	@Override
	public void WheelRotateAction( int rot ) {
		ind+= rot;
		if( ind > indTot - 1 )
			ind= indTot - 1;
		else if( ind < 0 )
			ind= 0;
	}
}
