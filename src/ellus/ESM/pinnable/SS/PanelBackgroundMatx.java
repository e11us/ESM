package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundMatx extends pinSS implements AbleSMXConfig {
	private Color		bg1C, bg2C, tx1C;
	private int			xTED, yTED, xTED2, yTED2;
	//
	private int[]		randNum			= null;
	private Color[]		randCol			= null;
	private int[]		fontInd			= null;
	private Font[]		fonts			= null;
	// thres, 1-1000, higher the #, higher changes of change.
	private int			mutTime			= 1000;
	private double		mutSizChance	= 0;
	private long		mutLastTime		= 0;
	private double		mutChance		= 0;
	private double		mutColor1Chance	= 0;
	private double		mutColor2Chance	= 0;
	// font size variant.
	private int			fontSizeBase	= 0;
	private int			fontSizePlus	= 0;
	private int			fontI			= 0;
	//
	private SManXElm	inp				= null;

	public PanelBackgroundMatx( SManXElm inp, ESMPS PS ) {
		//int fontI, int fontS, Color[] co ) {
		this.inp= inp;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.bg1C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor1" ).getColor();
		this.bg2C= inp.getAttr( SManXAttr.AttrType._color, "BackgroundColor2" ).getColor();
		this.tx1C= inp.getAttr( SManXAttr.AttrType._color, "TextColor" ).getColor();
		this.fontI= inp.getAttr( SManXAttr.AttrType._int, "FontIndex" ).getInteger();
		this.fontSizeBase= inp.getAttr( SManXAttr.AttrType._int, "FontSizeBase" ).getInteger();
		this.fontSizePlus= inp.getAttr( SManXAttr.AttrType._int, "FontSizePlus" ).getInteger();
		this.mutTime= inp.getAttr( SManXAttr.AttrType._int, "FontMutationTime" ).getInteger();
		this.mutSizChance= inp.getAttr( SManXAttr.AttrType._int, "FontMutationSizeChance" ).getDouble();
		this.mutChance= inp.getAttr( SManXAttr.AttrType._double, "FontMutationChance" ).getDouble();
		this.mutColor1Chance= inp.getAttr( SManXAttr.AttrType._double, "FontMutationColor1Chance" ).getDouble();
		this.mutColor2Chance= inp.getAttr( SManXAttr.AttrType._double, "FontMutationColor2Chance" ).getDouble();
		this.xTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceX" ).getInteger();
		this.yTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceY" ).getInteger();
		this.xTED2= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceX2" ).getInteger();
		this.yTED2= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceY2" ).getInteger();
		//
		fonts= new Font[fontSizePlus];
		for( int i= 0; i < fontSizePlus; i++ ){
			fonts[i]= SCon.FontList.get( fontI ).deriveFont( (float) ( fontSizeBase + i ) );
		}
	}

	@Override
	public void paint( ESMPD drawer, ESMPS pan ) {
		// must have font size.
		if( fontSizeBase == 0 )
			return;
		// see if need to set rand array.
		if( randNum == null ){
			int tot= (int) ( ( (double)pan.getWidth() / ( fontSizePlus + fontSizeBase ) )
					* ( (double)pan.getHeight() / ( fontSizePlus + fontSizeBase ) ) );
			randNum= new int[tot];
			randCol= new Color[tot];
			fontInd= new int[tot];
			for( int i= 0; i < randNum.length; i++ ){
				randNum[i]= helper.randAlphcapNum();
				randCol[i]= tx1C;
				fontInd[i]= (int) ( Math.random() * fontSizePlus );
			}
		}
		// check if mutate.
		if( helper.getTimeLong() - mutLastTime > mutTime ){
			for( int i= 0; i < randNum.length; i++ ){
				if( Math.random() > mutChance ){
					randNum[i]= helper.randAlphcapNum();
				}
				if( Math.random() > mutSizChance ){
					fontInd[i]= (int) ( Math.random() * fontSizePlus );
				}
				if( Math.random() > mutColor1Chance ){
					randCol[i]= randCol[i].brighter();
				}else if( Math.random() > mutColor2Chance ){
					randCol[i]= tx1C;
				}
			}
			mutLastTime= helper.getTimeLong();
		}
		// paint background first.
		Paint gp= new GradientPaint( 0, 0, bg1C, 0, pan.getHeight(), bg2C, true );
		drawer.fillRect( 0, 0, pan.getWidth(), pan.getHeight(), gp );
		// draw matrix.
		int x= xTED, y= (int) ( yTED + Math.random() * fontSizePlus * 2 ), ind= 0;
		while( ind < randNum.length ){
			if( y + yTED2 > pan.getHeight() ){
				y= (int) ( yTED + Math.random() * fontSizePlus * 2 );
				x+= fontSizeBase * 2 + fontSizePlus;
				if( x + xTED2 > pan.getWidth() )
					return;
			}
			drawer.drawString( ( (char)randNum[ind] ) + "", x, y,
					randCol[ind], fonts[fontInd[ind]] );
			ind++ ;
			y+= Math.random() * fontSizePlus * 2 + fontSizeBase * 2;
		}
	}
}
