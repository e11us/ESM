package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleClickR;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;
import ellus.ESM.pinnable.able_Interface.AbleMouseWheel;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



//display a msg in middle.
//cover the whole screen, consume the left click and right drag. and scroll. ( need to add this as the highlighted.)
//remove itself when off highlight.
//only way to exit is press esc.
public class PanelDisplay extends pinLF implements AbleSMXConfig, AbleClickR, AbleClickHighlight, AbleClick, AbleMouseWheel, AbleDoubleClick, AbleMouseDrag, AbleHoverHighlight {
	private Color				bg1C, bg2C, edC, txC;
	private ArrayList <String>	tx;
	private int					LineSep	= 5;
	private int					xo, yo;
	private int					fontI	= 4, fontS= 36;
	private Font				font;
	private int					indS	= 0;
	private SManXElm			elm;

	public PanelDisplay( SManXElm elm, ArrayList <String> tx ) {
		this.tx= tx;
		// take up whole possible screen.
		super.setXY( Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE );
		this.elm= elm;
		elm.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		xo= elm.getAttr( AttrType._int, "ToEdgeDistanceX" ).getInteger();
		yo= elm.getAttr( AttrType._int, "ToEdgeDistanceY" ).getInteger();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// add to non LF instead so it can consume all the input.
		pan.addGUIactive( this );
		// add itself.
		//
		Paint gp= new GradientPaint( xo, yo, bg1C, xo, pan.getHeight() - yo, bg2C );
		g.fillRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, gp );
		g.drawRect( xo, yo, pan.getWidth() - xo * 2, pan.getHeight() - yo * 2, 1, edC );
		// draw the strings.
		for( int i= indS; i < tx.size(); i++ ){
			g.drawString( tx.get( i ), xo + 15, yo + 15 + ( LineSep + fontS ) * ( i - indS + 1 ),
					txC, font );
			if( yo + 15 + ( LineSep + fontS ) * ( i - indS + 2 ) > pan.getHeight() - yo )
				break;
		}
	}

	@Override
	public void WheelRotateAction( int rot ) {
		indS+= rot;
		if( indS > tx.size() - 1 )
			indS= tx.size() - 1;
		if( indS < 0 )
			indS= 0;
	}

	@Override
	public void B3clickAction( int x, int y ) {
		this.removeMe= true;
	}

	@Override
	public boolean MouseDragState() {
		return false;
	}

	@Override
	public void B1clickHighlightOff( int x, int y ) {}

	@Override
	public void B1clickAction( int x, int y ) {}

	@Override
	public void B1clickHighlightOn( int x, int y ) {}

	@Override
	public void MouseDragOn( cor2D inp ) {}

	@Override
	public void MouseDragOff( cor2D inp ) {}

	@Override
	public void HoverHighlightOn() {}

	@Override
	public void HoverHighlightOff() {}

	@Override
	public void paintByMouse( ESMPD g, ESMPS pan ) {}

	@Override
	public void B1DoubleClickAction( int x, int y ) {
		// TODO Auto-generated method stub
	}
}
