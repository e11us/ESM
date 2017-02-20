package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Paint;
import java.awt.Polygon;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.media.PlayerAudio;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonTextFS extends pin implements AbleHoverHighlight, AbleClick, AbleSMXConfig {
	private int				WidOffSet	= 0, HeiOffSet= 0;
	private int				fontI		= 0;
	private int				fontS		= 16;
	private int				txOSX		= 5, txOSY= 10;
	private String			secMsg		= null;
	private Color			bg1C, bg2C, edC, txC, bg1CH, bg2CH, edCH, txCH;
	private SManXElm		elm;
	//
	protected PlayerAudio	sound		= null;
	protected String		msg			= null;
	private Font			fon;
	//
	private String defMsg;
	private Color fC= null;
	private int delay;
	private long delayS= 0;

	public ButtonTextFS( int[] list, Color[] colors, Color[] colors2, String msg, int fontInd, int fontsiz ) {
		// x,y,w,h,wos,hof // bg1C, bg2C, edC, csC, txC
		if( list.length != 6 || colors.length != 5 )
			return;
		this.msg= msg;
		super.setXY( list[0], list[0] + list[2], list[1], list[1] + list[3] );
		WidOffSet= list[4];
		HeiOffSet= list[5];
		bg1C= colors[0];
		bg2C= colors[1];
		edC= colors[2];
		txC= colors[4];
		bg1CH= colors2[0];
		bg2CH= colors2[1];
		edCH= colors2[2];
		txCH= colors2[4];
		//
		fontI= fontInd;
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		fontS= fontsiz;
		fon= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	public ButtonTextFS( SManXElm elm, String msg ) {
		// x,y,w,h,wos,hof // bg1C, bg2C, edC, csC, txC
		this.elm= elm;
		this.msg= msg;
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
		WidOffSet= elm.getAttr( AttrType._int, "WidthOffSet" ).getInteger();
		HeiOffSet= elm.getAttr( AttrType._int, "HeightOffSet" ).getInteger();
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
		pan.addGUIactive( this );
		//
		if( fC != null ) {
			if( helper.getTimeLong() - delayS > delay ) {
				fC= null;
				this.msg= defMsg;
				return;
			}
			// create polygon
			Polygon pg= new Polygon();
			pg.addPoint( pan.w2bX( super.getXmin() ) + WidOffSet, pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(), pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(),
					pan.w2bY( super.getYmin() ) + super.getHeight() - HeiOffSet );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth() - WidOffSet,
					pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + HeiOffSet );
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), fC,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), fC, true );
			g.fillPolygon( pg, gp );
			// draw polygon outline of the button.
			g.drawPolygon( pg, 3, edC );
			// draw String now.
			Font fon= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
			g.drawString( msg, pan.w2bX( super.getXmin() ) + txOSX,
					pan.w2bY( super.getYmin() ) - txOSY + super.getHeight(), txC, fon );
			//
			if( Math.random() > 0.9 )
				fC= fC.darker();
			return;
		}
		//
		if( !isHovered ){
			// create polygon
			Polygon pg= new Polygon();
			pg.addPoint( pan.w2bX( super.getXmin() ) + WidOffSet, pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(), pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(),
					pan.w2bY( super.getYmin() ) + super.getHeight() - HeiOffSet );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth() - WidOffSet,
					pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + HeiOffSet );
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2C, true );
			g.fillPolygon( pg, gp );
			// draw polygon outline of the button.
			g.drawPolygon( pg, 3, edC );
			// draw String now.
			Font fon= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
			g.drawString( msg, pan.w2bX( super.getXmin() ) + txOSX,
					pan.w2bY( super.getYmin() ) - txOSY + super.getHeight(), txC, fon );
			//secondPrint( g, pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) );
		}else{
			// create polygon
			Polygon pg= new Polygon();
			pg.addPoint( pan.w2bX( super.getXmin() ) + WidOffSet, pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(), pan.w2bY( super.getYmin() ) );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth(),
					pan.w2bY( super.getYmin() ) + super.getHeight() - HeiOffSet );
			pg.addPoint( pan.w2bX( super.getXmin() ) + super.getWidth() - WidOffSet,
					pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight() );
			pg.addPoint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + HeiOffSet );
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1CH,
					pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2CH, true );
			g.fillPolygon( pg, gp );
			// draw polygon outline of the button.
			g.drawPolygon( pg, 3, edCH );
			// draw String now.
			if( secMsg != null )
				g.drawString( secMsg, pan.w2bX( super.getXmin() ) + txOSX,
						pan.w2bY( super.getYmin() ) - txOSY + super.getHeight(), txCH, fon );
			else g.drawString( msg, pan.w2bX( super.getXmin() ) + txOSX,
					pan.w2bY( super.getYmin() ) - txOSY + super.getHeight(), txCH, fon );
		}
	}

	public void setMsg( String msg ) {
		this.msg= msg;
	}
	
	public void setFeedBack( String msg, Color C, int delay ) {
		fC= C;
		defMsg= this.msg;
		this.msg= msg;
		this.delay= delay;
		delayS= helper.getTimeLong();
	}

	public void secondPrint( Graphics g, int x, int y ) {
		// to be overriden.
	}

	public String getMsg() {
		return msg;
	}

	public void addSeconMsg( String inp ) {
		this.secMsg= inp;
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
	public void B1clickAction( int x, int y ) {
		// TODO Auto-generated method stub
	}
}
