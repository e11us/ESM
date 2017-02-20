package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.roboSys.MemInfo;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelMemInfo extends pinSS implements AbleHoverHighlight, AbleSMXConfig {
	private double		useRate		= 0;
	private int			total		= 0;
	private int			fontI, fontS;
	private long		lastRFtime	= helper.getTimeLong();
	private int			barXOS		= 0;
	private int			barYOS		= 0;
	private int			frmXOS		= 0;
	private Color		txC, br1C, br2C, edC;
	private int			refreshThres= 1000;
	private Font		font		= null;
	private SManXElm	inp;

	public PanelMemInfo( SManXElm inp ) {
		// x,y,w,h, fontI, fontS, barXOS, barYOS, frameOSX | txC, br1C,br2C, edC;
		this.inp= inp;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		//display.println( this.getClass().toGenericString(), "PanelMemInfo is reset()." );
		super.setXY(
				inp.getAttr( AttrType._location, "location" ).getLocation().getX(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() +
						inp.getAttr( AttrType._int, "Width" ).getInteger(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() +
						inp.getAttr( AttrType._int, "Height" ).getInteger() );
		//
		fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
		fontS= inp.getAttr( AttrType._int, "FontSize" ).getInteger();
		barXOS= inp.getAttr( AttrType._int, "CPUBarXOS" ).getInteger();
		barYOS= inp.getAttr( AttrType._int, "CPUBarYOS" ).getInteger();
		frmXOS= inp.getAttr( AttrType._int, "CPUFrameXOS" ).getInteger();
		refreshThres= inp.getAttr( AttrType._int, "RefreshWait(ms)" ).getInteger();
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
			useRate= MemInfo.getUsed() / 100.0;
			lastRFtime= helper.getTimeLong();
		}
		// draw cpu. msg.
		g.drawString( "Mem ", super.getXmin(), super.getYmin() + fontS - 2, txC, font );
		// draw rate.
		Paint gp= new GradientPaint( super.xmin + barXOS, super.ymin + barYOS, br1C,
				super.xmin + super.getWidth(), super.ymin + barXOS, br2C, true );
		int tot= super.getWidth() - ( barXOS - frmXOS ) - barXOS;
		g.fillRect( super.xmin + barXOS, super.ymin + barYOS, (int) ( tot * useRate ),
				super.getHeight() - barYOS * 2, gp );
		// draw frame.
		g.drawLine( frmXOS + super.xmin, super.ymin + super.getHeight(),
				super.xmin + super.getWidth(), super.ymin + super.getHeight(), 1, edC );
		g.drawLine( super.xmin + super.getWidth(), super.ymin + super.getHeight(),
				super.xmin + super.getWidth(), super.ymin, 1, edC );
		g.drawString( ( (int) ( useRate * 100 ) ) + "% of " + total + "GB",
				super.xmin + super.getWidth() + 5, super.ymin + fontS - 2,
				txC, font );
	}

	@Override
	public void HoverHighlightOn() {}

	@Override
	public void HoverHighlightOff() {}
}
