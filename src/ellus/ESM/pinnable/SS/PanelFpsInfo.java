package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.Source.SourceDouble;
import ellus.ESM.pinnable.pin2ScreenLocationer;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelFpsInfo extends pinSS implements AbleSMXConfig {
	private int				fontI, fontS;
	private Color			txC;
	private int				xOS, yOS;
	private SManXElm		inp;
	private long			fpsTestLast	= 0;
	private int				fpsCount	= 1;
	private int				fps			= 0;
	private int				refreshThres= 1000;
	private Font			font		= null;
	private long			startTime	= helper.getTimeLong();
	private String			uptime		= "0";
	private SourceDouble	RWT			= null;
	//
	private boolean			setLoca		= false;

	public PanelFpsInfo( SManXElm inp, SourceDouble RWT ) {
		// x,y,w,h, fontI, fontS, barXOS, barYOS, frameOSX | txC, br1C,br2C, edC;
		this.inp= inp;
		this.RWT= RWT;
		inp.setPin( this );
		reset();
	}

	public double getFPS() {
		return fps;
	}

	@Override
	public void reset() {
		//display.println( this.getClass().toGenericString(), "PanelFpsInfo is reset()." );
		super.setXY(
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS +
						inp.getAttr( AttrType._int, "Width" ).getInteger(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS +
						inp.getAttr( AttrType._int, "Height" ).getInteger() );
		//
		fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
		fontS= inp.getAttr( AttrType._int, "FontSize" ).getInteger();
		refreshThres= inp.getAttr( AttrType._int, "RefreshWait(ms)" ).getInteger();
		if( refreshThres == 0 ){
			refreshThres= 2000;
		}
		//
		this.txC= inp.getAttr( AttrType._color, "TextColor" ).getColor();
		//
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// fps auto adjust.
		if( helper.getTimeLong() - fpsTestLast > refreshThres ){
			fps= (int) ( 1000.0 / ( ( helper.getTimeLong() - fpsTestLast ) / fpsCount ) );
			fpsTestLast= helper.getTimeLong();
			fpsCount= 1;
			int up= ( (int) ( ( helper.getTimeLong() - startTime ) / 1000.0 / 60 ) );
			uptime= up + "min";
			if( up >= 60 ){
				uptime= ( up / 60 ) + "hr";
			}
			if( up > ( 60 * 24 ) ){
				uptime= ( up / 60 / 24 ) + "Day";
			}
			if( !setLoca ){
				setLoca= true;
				super.setXY( 0, g.getTxtWid( "UT - " + uptime + "  FPS - " + fps + "  RWT - 2", font ), super.ymin,
						super.ymax );
				pin2ScreenLocationer.bottomLeft( this, pan );
			}
		}
		fpsCount++ ;
		//
		g.drawString( "UT - " + uptime + "  FPS - " + fps + "  RWT - " + (int)RWT.getDouble(), super.xmin,
				super.ymin + fontS - 2, txC, font );
	}
}
