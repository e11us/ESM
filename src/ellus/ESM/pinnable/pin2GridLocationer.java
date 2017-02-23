package ellus.ESM.pinnable;

import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class pin2GridLocationer implements AbleSMXConfig {
	private int			x, y, w;
	private SManXElm	elm;
	private ESMPS		PS;
	private int			xo, yo, xsep, ysep;
	int					xcur, ycur;
	int					lastH	= 0;

	public pin2GridLocationer( SManXElm elm, ESMPS PS, int x, int y ) {
		this.x= x;
		this.y= y;
		this.elm= elm;
		this.PS= PS;
		elm.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		xo= elm.getAttr( AttrType._int, "ItemToEdgeDistanceX" ).getInteger();
		yo= elm.getAttr( AttrType._int, "ItemToEdgeDistanceY" ).getInteger();
		xsep= elm.getAttr( AttrType._int, "ItemSeparationX" ).getInteger();
		ysep= elm.getAttr( AttrType._int, "ItemSeparationY" ).getInteger();
		w= elm.getAttr( AttrType._int, "TotalWidth" ).getInteger();
		//
		if( w == 0 )
			w= PS.getWidth();
		//
		xcur= x + xo;
		ycur= y + yo;
	}

	public cor2D next( pinnable pin ) {
		if( pin.getWidth() > w - xo * 2 ){
			// pin 2 large, return null.
			pin.setXY( Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE );
			return null;
		}
		if( xcur + pin.getWidth() + xo > x + w ){
			ycur+= lastH + ysep;
			xcur= x + xo;
			//
			cor2D ret= new cor2D( xcur, ycur );
			xcur+= pin.getWidth() + xsep;
			lastH= pin.getHeight();
			pin.setXY( ret.getX(), ret.getX() + pin.getWidth(), ret.getY(), ret.getY() + pin.getHeight() );
			return ret;
		}
		//
		cor2D ret= new cor2D( xcur, ycur );
		xcur+= pin.getWidth() + xsep;
		lastH= pin.getHeight();
		pin.setXY( ret.getX(), ret.getX() + pin.getWidth(), ret.getY(), ret.getY() + pin.getHeight() );
		return ret;
	}

	public cor2D fullRow( pinnable pin ) {
		pin.setXY( x + xo, x + w - xo, ycur, ycur + pin.getHeight() );
		cor2D ret= new cor2D( x + xo, ycur );
		xcur= x + xo;
		ycur+= pin.getHeight() + ysep;
		return ret;
	}

	public cor2D nextRow( pinnable pin ) {
		ycur+= lastH + ysep;
		xcur= x + xo;
		cor2D ret= new cor2D( xcur, ycur );
		xcur+= pin.getWidth() + xsep;
		lastH= pin.getHeight();
		pin.setXY( ret.getX(), ret.getX() + pin.getWidth(), ret.getY(), ret.getY() + pin.getHeight() );
		return ret;
	}

	public void nextRow() {
		ycur+= lastH + ysep;
		xcur= x + xo;
	}
}
