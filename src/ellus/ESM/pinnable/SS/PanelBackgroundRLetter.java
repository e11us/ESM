package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundRLetter extends pinSS implements AbleSMXConfig {
	private int			xTED, yTED;
	private int			lb, lp;
	private int			fontS			= 10;
	private double		dir				= 0;
	private Font		font;
	private Color		txC;
	private String		msg				= "";
	private int			fontI, fontSb, fontSp;
	private int			timeX			= 0;
	private int			timeY			= 0;
	private int			TextDirection	= 0;
	private SManXElm	inp;
	private int			refreshThres	= 1000;
	private long		lastRFtime		= 0;

	// display msg, and change at random time.
	public PanelBackgroundRLetter( SManXElm inp ) {
		//int fontI, int fontS, int xt, int yt, double cR, String msg, Color C ) {
		this.inp= inp;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.TextDirection= inp.getAttr( AttrType._int, "TextDirection" ).getInteger();
		this.fontI= inp.getAttr( AttrType._int, "FontIndex" ).getInteger();
		this.fontSb= inp.getAttr( AttrType._int, "FontSizeBase" ).getInteger();
		this.fontSp= inp.getAttr( AttrType._int, "FontSizePluse" ).getInteger();
		this.xTED= inp.getAttr( AttrType._int, "ToEdgeDistanceX" ).getInteger();
		this.yTED= inp.getAttr( AttrType._int, "ToEdgeDistanceY" ).getInteger();
		this.lb= inp.getAttr( AttrType._int, "RandomStringLengthBase" ).getInteger();
		this.lp= inp.getAttr( AttrType._int, "RandomStringLengthPlus" ).getInteger();
		this.refreshThres= inp.getAttr( AttrType._int, "RefreshWaitMax(s)" ).getInteger();
		//
		this.txC= inp.getAttr( AttrType._color, "TextColor" ).getColor();
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		if( helper.getTimeLong() - lastRFtime > refreshThres * 1000.0 * Math.random() ){
			lastRFtime= helper.getTimeLong();
			msg= helper.rand32AN().substring( 0, (int) ( Math.random() * lb + lp ) );
			fontS= (int) ( Math.random() * fontSb + fontSp );
			timeX= (int) ( Math.random() * ( pan.getWidth() - xTED * 2 ) + xTED );
			timeY= (int) ( Math.random() * ( pan.getHeight() - fontS - yTED * 2 ) + yTED );
			if( TextDirection == 0 ){
				dir= Math.random();
			}else if( TextDirection > 0 ){
				dir= 0;
			}else{
				dir= 1;
			}
		}
		if( dir > 0.5 ){
			AffineTransform orig= new AffineTransform();
			orig.rotate( Math.toRadians( 90 ), 0, 0 );
			font= ( SCon.FontList.get( fontI ).deriveFont( (float)fontS ) ).deriveFont( orig );
			g.drawString( msg, timeX, timeY, txC, font );
		}else{
			font= ( SCon.FontList.get( fontI ).deriveFont( (float)fontS ) );
			g.drawString( msg, timeX, timeY, txC, font );
		}
	}
}
