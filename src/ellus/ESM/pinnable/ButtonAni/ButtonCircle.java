package ellus.ESM.pinnable.ButtonAni;

import java.awt.Color;
import java.awt.geom.Arc2D;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonCircle extends pinLF implements AbleHoverHighlight, AbleClick, AbleSMXConfig {
	private int			circlNum		= 0;
	private int			circlThic		= 10;
	private int			circlDia;
	private int			circlSep;
	private Color		CirC;
	private Arc2D[]		arcs;
	private Arc2D		ark;
	private boolean		isHover			= false;
	private long		HoverTime		= 0;
	private int			HoverTimeThres	= Integer.MAX_VALUE;
	//
	private int			xOS, yOS;
	private SManXElm	inp;

	public ButtonCircle( SManXElm inp, ESMPS PS ) {
		this.inp= inp;
		inp.setPin( this );
		// x, y, size, cirl#, cirl thic, cirl sep // bg1C, bg2C, edC, csC, txC
		reset();
	}

	@Override
	public void reset() {
		super.setXY( inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getX() + xOS +
						inp.getAttr( AttrType._int, "Diameter" ).getInteger(),
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS,
				inp.getAttr( AttrType._location, "location" ).getLocation().getY() + yOS +
						inp.getAttr( AttrType._int, "Diameter" ).getInteger() );
		//
		circlNum= inp.getAttr( AttrType._int, "CircleNumber" ).getInteger();
		circlDia= inp.getAttr( AttrType._int, "Diameter" ).getInteger();
		circlThic= inp.getAttr( AttrType._int, "Thickness" ).getInteger();
		circlSep= inp.getAttr( AttrType._int, "CircleSeperation" ).getInteger();
		HoverTimeThres= inp.getAttr( AttrType._int, "HoverTimeThres" ).getInteger();
		CirC= inp.getAttr( AttrType._color, "CircleColor" ).getColor();
		//
		arcs= new Arc2D[circlNum];
		for( int i= 0; i < arcs.length; i++ ){
			ark= new Arc2D.Float( Arc2D.OPEN );
			ark.setAngleStart( Math.random() * 360 );
			ark.setAngleExtent( 5 );
			arcs[i]= ark;
		}
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		pan.addGUIactiveLF( this );
		//
		Color circleC= CirC;
		//
		if( isHover ){
			for( int i= 0; i < arcs.length; i++ ){
				circleC= circleC.brighter().brighter();
				ark= arcs[i];
				ark.setFrame( super.getXmin() + i * circlSep, super.getYmin() + i * circlSep,
						circlDia - i * circlSep * 2,
						circlDia - i * circlSep * 2 );
				ark.setAngleStart( ark.getAngleStart() + ( i + 1 ) * 7.5 + Math.random() * 2 );
				g.drawShape( ark, circlThic, circleC );
			}
		}else{
			for( int i= 0; i < arcs.length; i++ ){
				circleC= circleC.brighter();
				ark= arcs[i];
				ark.setFrame( super.getXmin() + i * circlSep, super.getYmin() + i * circlSep,
						circlDia - i * circlSep * 2,
						circlDia - i * circlSep * 2 );
				ark.setAngleStart( ark.getAngleStart() + ( i + 1 ) * 3.24 + Math.random() * 2 );
				g.drawShape( ark, circlThic, circleC );
			}
		}
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
