package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import java.util.Date;
import org.joda.time.DateTime;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleClick;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXElm;
import ellus.ESM.setting.SManXAttr.AttrType;



  	this is untested///
  	
  	

public class PanelTimerFS1 extends pin implements AbleSMXConfig, AbleClick {
	private int		hour;
	private int		minute;
	private int		second;
	private int edsize, TimerEdgeSpikRate, fontS, fontI ;
	private Font font;
	private DateTime	sTime;
	private DateTime	eTime;
	private double	timeGone= 0;
	private Color	tmC, bgC, bgC2, edC, spC;
	private SManXElm				elm;
	private boolean SSmode;
	private long lastupdate= 0;
	private String time= null;
	private int lastTimeWidth= 0;

	
	public PanelTimerFS1( SManXElm elm, int x, int y, int tot ) {
		this.elm= elm;
		super.setXY( x, x + 1, y, y + 1 );
		elm.setPin( this );
		sTime= new DateTime();
		eTime= sTime.plusSeconds( tot );
		reset();
	}

	public PanelTimerFS1( SManXElm elm, int tot ) {
		this.elm= elm;
		elm.setPin( this );
		sTime= new DateTime();
		eTime= sTime.plusSeconds( tot );
		SSmode= true;
		reset();
	}
	
	@Override
	public void reset() {
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
		edsize= elm.getAttr( AttrType._int, "EdgeSize" ).getInteger();
		TimerEdgeSpikRate= elm.getAttr( AttrType._int, "TimerEdgeSpikRate" ).getInteger();
		//
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		spC= elm.getAttr( AttrType._color, "SpikeColor" ).getColor();
		bgC= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bgC2= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		tmC= elm.getAttr( AttrType._color, "TimerColor" ).getColor();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float )fontS );
	}

	@Override
	public void paint(  ESMPD g, ESMPS pan ) {
		pan.getGUIactive().add( this );
		//
		// gradient paint. the background paint.
		Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bgC,
				pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bgC2, true );
		//
		g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), gp );
		g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), edsize, edC );
		// draw 4 edge spike
		g.drawLine( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				(int) ( pan.w2bX( super.getXmin() ) + TimerEdgeSpikRate * super.getWidth() ),
				(int) ( pan.w2bY( super.getYmin() ) + TimerEdgeSpikRate * super.getHeight() ),
				edsize, spC );
		g.drawLine( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(),
				(int) ( pan.w2bX( super.getXmin() ) + TimerEdgeSpikRate * super.getWidth() ),
				(int) ( pan.w2bY( super.getYmin() ) + super.getHeight() - TimerEdgeSpikRate * super.getHeight() ),
				edsize, spC );
		g.drawLine( pan.w2bX( super.getXmin() ) + super.getWidth(), pan.w2bY( super.getYmin() ),
				(int) ( pan.w2bX( super.getXmin() ) + super.getWidth()- TimerEdgeSpikRate * super.getWidth() ),
				(int) ( pan.w2bY( super.getYmin() ) + TimerEdgeSpikRate * super.getHeight() ),
				edsize, spC );
		g.drawLine( pan.w2bX( super.getXmin() ) + super.getWidth(), pan.w2bY( super.getYmin() ) + super.getHeight(),
				(int) ( pan.w2bX( super.getXmin() ) + super.getWidth() - TimerEdgeSpikRate * super.getWidth() ),
				(int) ( pan.w2bY( super.getYmin() ) + super.getHeight() - TimerEdgeSpikRate * super.getHeight() ),
				edsize, spC );
		// draw time.
		if( helper.getTimeLong() - lastupdate > 300 ) {
			lastupdate= helper.getTimeLong();
			//
			int timeT= (int) ( ( eTime.getMillis() - ( new DateTime() ).getMillis() ) / 1000 );
			hour= (int) ( timeT / 3600.0 );
			minute= (int) ( ( timeT % 3600 ) / 60.0 );
			second= ( timeT % 3600 ) % 60;
			time= hour + " : " + minute + " : " + second;
			//
			lastTimeWidth= g.getTxtWid( time, font );
		}
		g.drawString( time, pan.w2bX( super.getXmin() ) + super.getWidth() / 2 - lastTimeWidth / 2,
				pan.w2bY( super.getYmin() ) + super.getHeight() / 2 + fontS / 2, tmC, font );
		/*
		// draw progress bar.
		timeGone= (double) ( ( new Date() ).getTime() - sTime.getTime() ) /
				( eTime.getTime() - sTime.getTime() );
		if( timeGone > 1 ){
			timeGone= 1;
			g.setColor( Color.red );
		}else g.setColor( GCSV.TimerColorBar );
		g.fillRect( pan.w2bX( super.getXmin() ) + GCSV.TimerBarOffSetX, pan.w2bY( super.getYmin() ) + GCSV.TimerBarOffSetY,
				(int) ( ( GCSV.TimerWidth - GCSV.TimerBarOffSetX * 2 ) * timeGone ),
				GCSV.TimerBarHeight );
				*/
	}

	@Override
	public void B1clickAction( int x, int y ) {
		
	}

}
