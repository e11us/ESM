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



public class ButtonScrollBarVerti extends pin implements AbleHoverHighlight, AbleMouseWheel, AbleSMXConfig {
	private Color		bg1C, bg2C, edC, txC, brC, bg1CH, bg2CH;
	private int			fontS, fontI, BarOS;
	private Font		font;
	private int			barSize, barSep, edsize, total;
	private double		min, max, init, step;
	private SManXElm	elm;

	public ButtonScrollBarVerti( SManXElm elm, double min, double max, double init, double step ) {
		this.elm= elm;
		elm.setPin( this );
		this.min= min;
		this.max= max;
		this.init= init;
		this.step= step;
		if( init > max )
			init= max;
		if( init < min )
			init= min;
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
		//
		barSize= elm.getAttr( AttrType._int, "BarSize" ).getInteger();
		BarOS= elm.getAttr( AttrType._int, "BarOS" ).getInteger();
		barSep= elm.getAttr( AttrType._int, "BarSeperation" ).getInteger();
		edsize= elm.getAttr( AttrType._int, "EdgeSize" ).getInteger();
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		brC= elm.getAttr( AttrType._color, "BarColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		//
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
		total= super.getHeight() - fontS - 24;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( barSep == 0 )
			return;
		pan.addGUIactive( this );
		//
		Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C,
				pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmax() ), bg2C, true );
		g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), gp );
		g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), edsize, edC );
		//
		double perce= ( init - min ) / ( max - min );
		g.drawString( ( (int) ( perce * 100 ) ) + "%", super.getWidth() / 2 -
				g.getTxtWid( ( (int) ( perce * 100 ) ) + "%", font ) / 2
				+ pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() + 8 + fontS ), txC, font );
		//
		int htot= pan.w2bY( super.getYmax() ) - (int) ( perce * total );
		for( int y= pan.w2bY( super.getYmax() ) - barSize - 8; y >= htot; y-= barSep ){
			g.fillRect( pan.w2bX( super.getXmin() ) + BarOS, y, super.getWidth() - 2 * BarOS, barSize, brC );
		}
	}

	public double getValue() {
		return init;
	}

	public void setValue( double inp ) {
		init= inp;
		if( init > max )
			init= max;
		if( init < min )
			init= min;
	}

	@Override
	public void WheelRotateAction( int rot ) {
		init-= rot * ( max - min ) / 100 * step;
		if( init > max )
			init= max;
		if( init < min )
			init= min;
	}

	@Override
	public void HoverHighlightOn() {
		// TODO Auto-generated method stub
	}

	@Override
	public void HoverHighlightOff() {
		// TODO Auto-generated method stub
	}
}
