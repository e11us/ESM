package ellus.ESM.pinnable.LF;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class SideShadeAutoHide extends pinSS implements AbleSMXConfig, AbleHoverHighlight {
	private SManXElm				inp;
	private ArrayList <pinnable>	cont;
	private Color					bg1HC, bg2HC, edHC, bg1C, bg2C;
	private Polygon					pg;
	private Rectangle				rg;
	private boolean					ishover;
	private Paint					gpH, gp;
	private int						extS;

	// inactive, draw rectangle, active draw polygon.
	public SideShadeAutoHide( SManXElm inp, ESMPS PS, ArrayList <pinnable> cont, Polygon pg, Rectangle rg, int extS ) {
		this.inp= inp;
		this.cont= cont;
		this.pg= pg;
		this.rg= rg;
		this.extS= extS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		super.setXY( rg.x, rg.x + rg.width, rg.y, rg.y + rg.height );
		this.bg1HC= inp.getAttr( SManXAttr.AttrType._color, "ActiveBackgroundColor1" ).getColor();
		this.bg2HC= inp.getAttr( SManXAttr.AttrType._color, "ActiveBackgroundColor2" ).getColor();
		this.edHC= inp.getAttr( SManXAttr.AttrType._color, "ActiveEdgeColor" ).getColor();
		this.bg1C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor1" ).getColor();
		this.bg2C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor2" ).getColor();
		//
		gpH= new GradientPaint( ( pg.xpoints[0] ), ( pg.ypoints[0] ),
				bg1HC, ( pg.xpoints[2] ), ( pg.ypoints[0] ), bg2HC );
		gp= new GradientPaint( rg.x, rg.y, bg1C, rg.x + rg.width, rg.y, bg2C );
	}

	@Override
	public void paint( ESMPD g, ESMPS ES ) {
		//
		ES.addGUIactiveLF( this );
		//
		if( ishover && pg != null ){
			// gradient paint. the background of the button polygon.
			g.drawPolygon( pg, 1, edHC );
			g.fillPolygon( pg, gpH );
			if( cont != null & cont.size() > 0 ){
				for( pinnable pin : cont )
					pin.paint( g, ES );
			}
		}else if( !ishover && rg != null ){
			g.fillShape( rg, gp );
		}
	}

	@Override
	public void HoverHighlightOn() {
		ishover= true;
		super.setXY( rg.x, rg.x + extS, rg.y, rg.y + rg.height );
	}

	@Override
	public void HoverHighlightOff() {
		ishover= false;
		super.setXY( rg.x, rg.x + rg.width, rg.y, rg.y + rg.height );
	}
}
