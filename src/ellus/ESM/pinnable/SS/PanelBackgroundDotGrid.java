package ellus.ESM.pinnable.SS;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundDotGrid extends pinSS implements AbleSMXConfig {
	private int			xSep, ySep;
	private int			xTED		= 0, yTED= 0;
	private int			len			= 1;
	private int			size		= 0;
	private Color		C			= null;
	private int			colorbase	= 0;
	private int			colorplus	= 0;
	private SManXElm	inp			= null;

	public PanelBackgroundDotGrid( SManXElm inp, ESMPS PS ) {
		//int xSep, int ySep, int xTED, int yTED, int size, Color C
		this.inp= inp;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.C= inp.getAttr( SManXAttr.AttrType._color, "FixColor" ).getColor();
		this.colorbase= inp.getAttr( SManXAttr.AttrType._int, "ColorBase" ).getInteger();
		this.colorplus= inp.getAttr( SManXAttr.AttrType._int, "ColorPlus" ).getInteger();
		this.size= inp.getAttr( SManXAttr.AttrType._int, "GridDotSize" ).getInteger();
		this.xSep= inp.getAttr( SManXAttr.AttrType._int, "SeperationX" ).getInteger();;
		this.ySep= inp.getAttr( SManXAttr.AttrType._int, "SeperationY" ).getInteger();
		this.xTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceX" ).getInteger();
		this.yTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceY" ).getInteger();
	}

	@Override
	public void paint( ESMPD drawer, ESMPS pan ) {
		// avoid infinit loop.
		if( ySep == 0 || xSep == 0 )
			return;
		//
		if( ! ( C.getRed() == 0 && C.getGreen() == 0 && C.getBlue() == 0 ) ){
			//
			double dir= Math.random();
			for( int i= 0 + yTED; i < pan.getHeight() - yTED; i+= ySep ){
				for( int e= 0 + xTED; e < pan.getWidth() - xTED; e+= xSep ){
					if( size == 0 ){
						if( dir > 0.5 ){
							drawer.drawLine( e, i, e + len, i + len, 1, C );
						}else{
							drawer.drawLine( e + len, i, e, i + len, 1, C );
						}
					}else{
						drawer.fillRect( e, i, size, size, C );
					}
				}
			}
		}else{
			//
			int inten= 0;
			double dir= Math.random();
			for( int i= 0 + yTED; i < pan.getHeight() - yTED; i+= ySep ){
				for( int e= 0 + xTED; e < pan.getWidth() - xTED; e+= xSep ){
					inten= (int) ( Math.random() * colorbase + colorplus );
					if( size == 0 ){
						if( dir > 0.5 ){
							drawer.drawLine( e, i, e + len, i + len, 1, new Color( inten, inten, inten, inten ) );
						}else{
							drawer.drawLine( e + len, i, e, i + len, 1, new Color( inten, inten, inten, inten ) );
						}
					}else{
						drawer.fillRect( e, i, size, size, C );
					}
				}
			}
		}
	}
}