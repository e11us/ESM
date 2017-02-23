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
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundSC extends pinSS implements AbleSMXConfig {
	private Color		c1	= null, c2= null;
	private ESMPS		PS;
	private SManXElm	inp;
	private Paint		gp	= null;

	public PanelBackgroundSC( SManXElm inp, ESMPS PS ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	public PanelBackgroundSC( Color a, Color b ) {
		c1= a;
		c2= b;
	}

	@Override
	public void reset() {
		c1= inp.getAttr( AttrType._color, "backgroundColor1" ).getColor();
		c2= inp.getAttr( AttrType._color, "backgroundColor2" ).getColor();
		gp= new GradientPaint( 0, 0, c1, 0, PS.getHeight(), c2, true );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( gp == null ){
			if( c1 != null && c2 != null )
				gp= new GradientPaint( 0, 0, c1, 0, pan.getHeight(), c2, true );
		}
		g.fillRect( 0, 0, pan.getWidth(), pan.getHeight(), gp );
	}
}
