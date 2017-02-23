package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleMouseWheel;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelContainerScroll extends pin implements AbleHoverHighlight, AbleMouseWheel, AbleSMXConfig {
	private Color					bg1C, bg2C, edC;
	private int						edsize;
	private SManXElm				elm;
	private int						ind			= 0;
	private int						xOS, yOS, xSep, ySep;
	private ArrayList <pinnable>	cont;
	private int						maxPerline	= 0;
	private int						maxLineTot	= 0;
	private boolean					SSmode		= false;

	public PanelContainerScroll( SManXElm elm, int x, int y, ArrayList <pinnable> cont ) {
		this.elm= elm;
		super.setXY( x, x + 1, y, y + 1 );
		this.cont= cont;
		elm.setPin( this );
		reset();
	}

	public PanelContainerScroll( SManXElm elm, ArrayList <pinnable> cont ) {
		this.elm= elm;
		this.cont= cont;
		elm.setPin( this );
		SSmode= true;
		reset();
	}

	@Override
	public void reset() {
		if( cont == null || cont.size() == 0 )
			return;
		if( SSmode ){
			super.setXY(
					elm.getAttr( AttrType._location, "Location" ).getLocation().getX(),
					elm.getAttr( AttrType._location, "Location" ).getLocation().getX() +
							elm.getAttr( AttrType._int, "Width" ).getInteger(),
					elm.getAttr( AttrType._location, "Location" ).getLocation().getY(),
					elm.getAttr( AttrType._location, "Location" ).getLocation().getY() +
							elm.getAttr( AttrType._int, "Height" ).getInteger() );
		}else{
			super.setXY(
					super.getXmin(), super.getXmin() +
							elm.getAttr( AttrType._int, "Width" ).getInteger(),
					super.getYmin(), super.getYmin() +
							elm.getAttr( AttrType._int, "Height" ).getInteger() );
		}
		//
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		edsize= elm.getAttr( AttrType._int, "EdgeSize" ).getInteger();
		//
		xSep= elm.getAttr( AttrType._int, "ItemSeparationX" ).getInteger();
		ySep= elm.getAttr( AttrType._int, "ItemSeparationY" ).getInteger();
		xOS= elm.getAttr( AttrType._int, "ToEdgeDistanceX" ).getInteger();
		yOS= elm.getAttr( AttrType._int, "ToEdgeDistanceY" ).getInteger();
		//
		if( cont.get( 0 ).getWidth() == 0 )
			return;
		maxPerline= ( super.getWidth() - xOS * 2 + xSep ) / ( cont.get( 0 ).getWidth() + xSep );
		maxLineTot= ( super.getHeight() - yOS * 2 + ySep ) / ( cont.get( 0 ).getHeight() + ySep );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( edsize == 0 || cont == null || cont.size() == 0 || maxPerline == 0 )
			return;
		pan.addGUIactive( this );
		//
		Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C,
				pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2C, true );
		//
		g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), gp );
		g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), edsize, edC );
		//
		int xS= ( super.getXmin() + xOS );
		int yS= ( super.getYmin() + yOS );
		pinnable tmp;
		int itemPerlin= 0;
		// for each pin.
		for( int i= ind; i < cont.size(); i++ ){
			tmp= cont.get( i );
			if( itemPerlin >= maxPerline ){
				xS= ( super.getXmin() + xOS );
				yS+= ySep + tmp.getHeight();
				itemPerlin= 0;
				if( yS > super.getYmax() - yOS - tmp.getHeight() )
					break;
			}
			tmp.setXY( xS, xS + tmp.getWidth(), yS, yS + tmp.getHeight() );
			tmp.paint( g, pan );
			xS+= xSep + tmp.getWidth();
			itemPerlin++ ;
		}
	}

	public void resetCont( ArrayList <pinnable> cont ) {
		this.cont= cont;
		reset();
	}

	@Override
	public void WheelRotateAction( int rot ) {
		if( ind + maxLineTot * rot * maxPerline < cont.size() )
			ind+= maxLineTot * rot * maxPerline;
		if( ind < 0 )
			ind= 0;
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
