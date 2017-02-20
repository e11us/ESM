package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
/*||----------------------------------------------------------------------------------------------
 ||| background print, dose not interact with GUI input, does not do address translation.
||||--------------------------------------------------------------------------------------------*/
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundSC extends pinSS implements AbleSMXConfig {
	private Color		c1	= null, c2= null;
	private ESMPS		PS;
	private SManXElm	inp;
	private Paint		gp;

	public PanelBackgroundSC( SManXElm inp, ESMPS PS ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		c1= inp.getAttr( AttrType._color, "backgroundColor1" ).getColor();
		c2= inp.getAttr( AttrType._color, "backgroundColor2" ).getColor();
		gp= new GradientPaint( 0, 0, c1, 0, PS.getHeight(), c2, true );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		g.fillRect( 0, 0, pan.getWidth(), pan.getHeight(), gp );
	}
}
