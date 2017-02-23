package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pin2ScreenLocationer;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.roboSys.MemInfo;
import ellus.ESM.roboSys.cpuInfo;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelMemInfo extends pinSS implements AbleHoverHighlight, AbleSMXConfig {
	private double		useRate		= 0;
	private int			total		= 0;
	private int			fontI, fontS, barYOS;
	private long		lastRFtime	= helper.getTimeLong();
	private Color		txC, br1C, br2C, edC;
	private int			refreshThres= 1000;
	private Font		font		= null;
	private SManXElm	inp;
	//
	private boolean		centered	= false;
	private int			x1, x2;

	public PanelMemInfo( SManXElm inp ) {
		// x,y,w,h, fontI, fontS, barXOS, barYOS, frameOSX | txC, br1C,br2C, edC;
		this.inp= inp;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		super.setXY(
				inp.getAttr( AttrType._location, "location" ).getLocation().getX(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() +
						inp.getAttr( AttrType._int, "Width" ).getInteger(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() +
						inp.getAttr( AttrType._int, "Height" ).getInteger() );
		fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
		fontS= inp.getAttr( AttrType._int, "FontSize" ).getInteger();
		refreshThres= inp.getAttr( AttrType._int, "RefreshWait(ms)" ).getInteger();
		barYOS= inp.getAttr( AttrType._int, "CPUBarYOS" ).getInteger();
		//
		this.txC= inp.getAttr( AttrType._color, "TextColor" ).getColor();
		this.br1C= inp.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		this.br2C= inp.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		this.edC= inp.getAttr( AttrType._color, "EdgeColor" ).getColor();
		//
		total= ( (int) ( MemInfo.getTotal() / 1024 / 1024 / 1024 ) );
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		if( helper.getTimeLong() - lastRFtime > refreshThres ){
			new updater();
			lastRFtime= helper.getTimeLong();
		}
		// draw cpu. msg.
		g.drawString( "Mem ", super.getXmin() - x1 - 10, super.getYmax() - 2, txC, font );
		// draw rate.
		Paint gp= new GradientPaint( super.xmin, super.ymin, br1C,
				super.xmin + super.getWidth(), super.ymin, br2C, true );
		int tot= super.getWidth();
		g.fillRect( super.xmin, super.ymin + barYOS, (int) ( tot * useRate ),
				super.getHeight() - barYOS * 2, gp );
		// draw frame.
		g.drawLine( super.xmin, super.ymin + super.getHeight(),
				super.xmin + super.getWidth(), super.ymin + super.getHeight(), 1, edC );
		g.drawLine( super.xmin + super.getWidth(), super.ymin + super.getHeight(),
				super.xmin + super.getWidth(), super.ymin, 1, edC );
		g.drawString( ( (int) ( useRate * 100 ) ) + "%",
				super.xmax + 10, super.getYmax() - 2, txC, font );
		if( !centered ){
			x1= g.getTxtWid( "Men", font );
			x2= g.getTxtWid( "%50", font );
			int wid= x1 + x2 + super.getWidth() + 20;
			super.setXY( 0, wid, super.ymin, super.ymax );
			centered= true;
			pin2ScreenLocationer.centerThisX( this, pan );
		}
	}

	// use extra thread to eliminate lag.
	private class updater extends Thread {
		public updater() {
			this.start();
		}

		public void run() {
			useRate= MemInfo.getUsed() / 100.0;
		}
	}

	@Override
	public void HoverHighlightOn() {}

	@Override
	public void HoverHighlightOff() {}
}
