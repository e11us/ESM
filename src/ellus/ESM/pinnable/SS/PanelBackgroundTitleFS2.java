package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Polygon;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AblePanelTitle;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundTitleFS2 extends pinSS implements AbleSMXConfig, AblePanelTitle {
	private SManXElm			inp	= null;
	private ESMPS				PS;
	private PanelSlidingStrips	sl, sr;
	private int					titleXOS, titleXOS2, titleYOS2, titleXOS3, titleYOS3, SquareSize,
			sideStripeWidth, sideStripYOS, sideStripSize, sideStripSep, titleSize1, titleSize2;
	private Color				title1, title2, sideStrip, centerBg, titleFill;
	private Polygon				tfu, tfd;

	public PanelBackgroundTitleFS2( SManXElm inp, ESMPS PS, String title ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.title1= inp.getAttr( SManXAttr.AttrType._color, "title1" ).getColor();
		this.title2= inp.getAttr( SManXAttr.AttrType._color, "title2" ).getColor();
		this.sideStrip= inp.getAttr( SManXAttr.AttrType._color, "sideStrip" ).getColor();
		this.centerBg= inp.getAttr( SManXAttr.AttrType._color, "centerBg" ).getColor();
		this.titleFill= inp.getAttr( SManXAttr.AttrType._color, "titleFill" ).getColor();
		//
		this.titleXOS= (int) ( inp.getAttr( SManXAttr.AttrType._double, "titleXOS" ).getDouble() * PS.getWidth() );
		this.titleXOS2= (int) ( inp.getAttr( SManXAttr.AttrType._double, "titleXOS2" ).getDouble() * PS.getWidth() );
		this.titleYOS2= inp.getAttr( SManXAttr.AttrType._int, "titleYOS2" ).getInteger();
		this.titleXOS3= inp.getAttr( SManXAttr.AttrType._int, "titleXOS3" ).getInteger();
		this.titleYOS3= inp.getAttr( SManXAttr.AttrType._int, "titleYOS3" ).getInteger();
		this.SquareSize= inp.getAttr( SManXAttr.AttrType._int, "SquareSize" ).getInteger();
		this.sideStripeWidth= inp.getAttr( SManXAttr.AttrType._int, "sideStripeWidth" ).getInteger();
		this.sideStripYOS= inp.getAttr( SManXAttr.AttrType._int, "sideStripYOS" ).getInteger();
		this.sideStripSize= inp.getAttr( SManXAttr.AttrType._int, "sideStripSize" ).getInteger();
		this.sideStripSep= inp.getAttr( SManXAttr.AttrType._int, "sideStripSep" ).getInteger();
		this.titleSize1= inp.getAttr( SManXAttr.AttrType._int, "titleSize1" ).getInteger();
		this.titleSize2= inp.getAttr( SManXAttr.AttrType._int, "titleSize2" ).getInteger();
		//
		//fon= SCon.FontList.get( inp.getAttr( SManXAttr.AttrType._int, "FontIndex" ).getInteger() )	.deriveFont( (float )inp.getAttr( SManXAttr.AttrType._int, "FontSize" ).getInteger());
		//
		sl= new PanelSlidingStrips( 0, 0, sideStripeWidth, PS.getHeight(),
				sideStripYOS, sideStripSize, sideStripSep, sideStrip, true );
		sr= new PanelSlidingStrips( PS.getWidth() - sideStripeWidth,
				0, sideStripeWidth, PS.getHeight(), sideStripYOS, sideStripSize, sideStripSep, sideStrip, false );
		//
		tfu= new Polygon();
		tfu.addPoint( titleXOS, 0 );
		tfu.addPoint( titleXOS2, titleYOS2 );
		tfu.addPoint( PS.getWidth() - titleXOS2, titleYOS2 );
		tfu.addPoint( PS.getWidth() - titleXOS, 0 );
		tfd= new Polygon();
		tfd.addPoint( titleXOS, PS.getHeight() );
		tfd.addPoint( titleXOS2, PS.getHeight() - titleYOS2 );
		tfd.addPoint( PS.getWidth() - titleXOS2, PS.getHeight() - titleYOS2 );
		tfd.addPoint( PS.getWidth() - titleXOS, PS.getHeight() );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		sl.paint( g, pan );
		sr.paint( g, pan );
		//
		g.fillPolygon( tfu, titleFill );
		g.fillPolygon( tfd, titleFill );
		// top
		g.drawLineRJ( sideStripeWidth + sideStripSize * 2, titleSize1 / 2,
				titleXOS, titleSize1 / 2,
				titleSize1, title1 );
		g.drawLineRJ( titleXOS, titleSize1 / 2,
				titleXOS2, titleYOS2,
				titleSize1, title1 );
		g.drawLineRJ( titleXOS2, titleYOS2,
				pan.getWidth() - titleXOS2, titleYOS2,
				titleSize1, title1 );
		g.drawLineRJ( pan.getWidth() - titleXOS2, titleYOS2,
				pan.getWidth() - titleXOS, titleSize1 / 2,
				titleSize1, title1 );
		g.drawLineRJ( pan.getWidth() - titleXOS, titleSize1 / 2,
				pan.getWidth() - sideStripeWidth - sideStripSize * 2, titleSize1 / 2,
				titleSize1, title1 );
		// buttom.
		g.drawLineRJ( sideStripeWidth + sideStripSize * 2, pan.getHeight() - titleSize1 / 2,
				titleXOS, pan.getHeight() - titleSize1 / 2,
				titleSize1, title1 );
		g.drawLineRJ( titleXOS, pan.getHeight() - titleSize1 / 2,
				titleXOS2, pan.getHeight() - titleYOS2,
				titleSize1, title1 );
		g.drawLineRJ( titleXOS2, pan.getHeight() - titleYOS2,
				pan.getWidth() - titleXOS2, pan.getHeight() - titleYOS2,
				titleSize1, title1 );
		g.drawLineRJ( pan.getWidth() - titleXOS2, pan.getHeight() - titleYOS2,
				pan.getWidth() - titleXOS, pan.getHeight() - titleSize1 / 2,
				titleSize1, title1 );
		g.drawLineRJ( pan.getWidth() - titleXOS, pan.getHeight() - titleSize1 / 2,
				pan.getWidth() - sideStripeWidth - sideStripSize * 2, pan.getHeight() - titleSize1 / 2,
				titleSize1, title1 );
		//
		g.drawLine( titleXOS3, titleYOS3, titleXOS3, pan.getHeight() - titleYOS3, titleSize2, title2 );
		g.drawLine( pan.getWidth() - titleXOS3, pan.getHeight() - titleYOS3,
				pan.getWidth() - titleXOS3, titleYOS3, titleSize2, title2 );
		g.fillRect( titleXOS3, titleYOS3, pan.getWidth() - titleXOS3 * 2, pan.getHeight() - titleYOS3 * 2, centerBg );
		//
	}

	@Override
	public boolean isCloseClicked( int x, int y ) {
		return false;
	}
}
