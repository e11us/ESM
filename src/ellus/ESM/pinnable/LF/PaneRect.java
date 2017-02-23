package ellus.ESM.pinnable.LF;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
/*||----------------------------------------------------------------------------------------------
 ||| print a polygon, dose not interact with GUI input, does not do address translation.
||||--------------------------------------------------------------------------------------------*/
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.Able.AbleClick;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PaneRect extends pinLF implements AbleSMXConfig, AbleHoverHighlight, AbleClick {
	// for content list, first must be at top, last must be at bottom.
	private Color		bg1C, bg2C;
	private SManXElm	inp;
	private Paint		gp;
	private boolean		isHover			= false;
	private long		HoverTime		= 0;
	private int			HoverTimeThres	= Integer.MAX_VALUE;

	public PaneRect( SManXElm inp ) {
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
		this.bg1C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor1" ).getColor();
		this.bg2C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor2" ).getColor();
		this.HoverTimeThres= inp.getAttr( AttrType._int, "HoverTimeThres" ).getInteger();
		if( HoverTimeThres < 0 )
			HoverTimeThres= Integer.MAX_VALUE;
		gp= new GradientPaint(
				inp.getAttr( AttrType._location, "Paint1Location" ).getLocation().getX(),
				inp.getAttr( AttrType._location, "Paint1Location" ).getLocation().getY(),
				bg1C,
				inp.getAttr( AttrType._location, "Paint2Location" ).getLocation().getX(),
				inp.getAttr( AttrType._location, "Paint2Location" ).getLocation().getY(),
				bg2C );
	}

	// !!!!!!!!!!!!!!!!!!!!!!!!! no address translation for this !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// hence does not add itself to list l.
	@Override
	public void paint( ESMPD g, ESMPS ES ) {
		ES.addGUIactiveLF( this );
		g.fillRect( super.xmin, super.ymin, super.getWidth(), super.getHeight(), gp );
	}

	@Override
	public void HoverHighlightOn() {
		if( !isHover ){
			HoverTime= helper.getTimeLong();
		}else{
			if( ( helper.getTimeLong() - HoverTime ) > HoverTimeThres ){
				B1clickAction( 0, 0 );
				HoverTime= helper.getTimeLong();
			}
		}
		isHover= true;
	}

	@Override
	public void HoverHighlightOff() {
		isHover= false;
		HoverTime= 0;
	}

	@Override
	public void B1clickAction( int x, int y ) {}
}
