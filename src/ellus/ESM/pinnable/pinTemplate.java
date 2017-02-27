package ellus.ESM.pinnable;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXElm;
import ellus.ESM.setting.SManXAttr.AttrType;

public class pinTemplate  extends pin implements AbleSMXConfig{
	//
	private Color				bg1C, bg2C, edC, txC, curC;
	private int					fontI				= 4, fontS= 26;
	private Font				font;
	private SManXElm			elm;
	private String msg;
	
	public pinTemplate( SManXElm elm, ArrayList <String> tx ) {
		this.elm= elm;
		elm.setPin( this );
		reset();
	}
	
	@Override
	public void reset() {
		super.setXY(
				elm.getAttr( AttrType._location, "location" ).getLocation().getX(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getX() +
						elm.getAttr( AttrType._int, "Width" ).getInteger(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY() +
						elm.getAttr( AttrType._int, "Height" ).getInteger() );
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
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
		
	}
}
