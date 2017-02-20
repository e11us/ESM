package ellus.ESM.pinnable.Button;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.TimeLock;
import ellus.ESM.Machine.display;
import ellus.ESM.media.PlayerAudio;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonTouchSS extends pin implements AbleHoverHighlight, AbleSMXConfig {
	private int				spS			= 0, spW= 0;
	private Color			spC;
	//
	protected PlayerAudio	sound		= null;
	protected String		msg			= null;
	//
	private int				xOS, yOS;
	private SManXElm		inp;
	protected TimeLock		timelocker	= new TimeLock( 1333 );

	public ButtonTouchSS( SManXElm inp, int xOS, int yOS ) {
		// x,y,w,h, spikeSep Width, spike Width, yOS //
		this.inp= inp;
		this.xOS= xOS;
		this.yOS= yOS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		display.println( this.getClass().toGenericString(), "ButtonTouchSS is reset()." );
		super.setXY(
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS +
						inp.getAttr( AttrType._int, "Width" ).getInteger(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS +
						inp.getAttr( AttrType._int, "Height" ).getInteger() );
		//
		spS= inp.getAttr( AttrType._int, "SpikeSeperation" ).getInteger();
		spW= inp.getAttr( AttrType._int, "SpikeSize" ).getInteger();
		//
		spC= inp.getAttr( AttrType._color, "SpikeColor" ).getColor();
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// add itself.
		pan.getGUIactive().add( this );
		// cor conversion.
		int xInc= 0;
		//
		// draw spikes.
		if( spS + spW != 0 )
			while( xInc <= super.getWidth() ){
				g.drawLine( pan.w2bX( super.getXmin() ) + xInc, pan.w2bY( super.getYmin() ),
						pan.w2bX( super.getXmin() ) + xInc, pan.w2bY( super.getYmin() ) + super.getHeight(), spW, spC );
				xInc+= spS + spW;
			}
	}

	@Override
	public void HoverHighlightOn() {
		isHovered= true;
	}

	@Override
	public void HoverHighlightOff() {
		isHovered= false;
	}
}
//