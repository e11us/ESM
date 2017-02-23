package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pin2ScreenLocationer;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelDateTimeInfo extends pinSS implements AbleHoverHighlight, AbleSMXConfig {
	private int			fontI, fontS;
	private Color		txC;
	private String		time		= "";
	private SManXElm	inp;
	private int			refreshThres= 1000;
	private long		lastRFtime	= helper.getTimeLong();
	private Font		font		= null;
	// this always remain center of screen.
	private boolean		centered	= false;

	public PanelDateTimeInfo( SManXElm inp ) {
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
		//
		refreshThres= inp.getAttr( AttrType._int, "RefreshWait(ms)" ).getInteger();
		fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
		fontS= inp.getAttr( AttrType._int, "FontSize" ).getInteger();
		//
		this.txC= inp.getAttr( AttrType._color, "TextColor" ).getColor();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		if( helper.getTimeLong() - lastRFtime > refreshThres ){
			LocalDate date= LocalDate.now();
			DateTimeFormatter fmt= DateTimeFormat.forPattern( "EEEE  /  dd MMMM  /  yyyy" );
			DateTime dt= new DateTime();
			DateTimeFormatter fmt2= DateTimeFormat.forPattern( "kk : mm : ss" );
			time= "Today: " + date.toString( fmt ) + "   Time: " + dt.toString( fmt2 );
			lastRFtime= helper.getTimeLong();
			//
			if( !centered ){
				this.setXY( 0, g.getTxtWid( time, font ), super.ymin, super.ymax );
				pin2ScreenLocationer.centerThisX( this, pan );
				centered= true;
			}
		}
		g.drawString( time, super.xmin, super.ymin + fontS - 2, txC, font );
	}

	@Override
	public void HoverHighlightOn() {}

	@Override
	public void HoverHighlightOff() {}
}
