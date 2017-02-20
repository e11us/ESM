package ellus.ESM.pinnable.SS;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundDotCenterFlow extends pinSS implements AbleSMXConfig {
	private double		bandRait	= 0.23;
	private double		edgeRtio	= 0.88;
	private int			revDirExt	= 666;
	private train[]		trains;
	private Color		dC			= null;
	private SManXElm	inp			= null;
	private ESMPS		PS;
	private int			colorbase	= 0;
	private int			colorplus	= 0;

	public PanelBackgroundDotCenterFlow( SManXElm inp, ESMPS PS ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.dC= inp.getAttr( SManXAttr.AttrType._color, "FixColor" ).getColor();
		this.bandRait= inp.getAttr( SManXAttr.AttrType._double, "BandRaito" ).getDouble();
		this.edgeRtio= inp.getAttr( SManXAttr.AttrType._double, "EdgeDotRatio" ).getDouble();
		this.revDirExt= inp.getAttr( SManXAttr.AttrType._int, "ReverseDirectionFromScreenDistance" ).getInteger();
		this.colorbase= inp.getAttr( SManXAttr.AttrType._int, "ColorBase" ).getInteger();
		this.colorplus= inp.getAttr( SManXAttr.AttrType._int, "ColorPlus" ).getInteger();
		//
		trains= new train[inp.getAttr( SManXAttr.AttrType._int, "TotalTrain#" ).getInteger()];
		if( trains.length == 0 )
			trains= new train[1];
		for( int i= 0; i < trains.length; i++ ){
			trains[i]= new train( inp.getAttr( SManXAttr.AttrType._int, "DotSize" ).getInteger(),
					inp.getAttr( SManXAttr.AttrType._int, "SeperationX" ).getInteger(),
					inp.getAttr( SManXAttr.AttrType._int, "SeperationY" ).getInteger(),
					inp.getAttr( SManXAttr.AttrType._int, "SpeedBase" ).getInteger(),
					inp.getAttr( SManXAttr.AttrType._int, "SpeedPlus" ).getInteger(), PS );
			//xSep, ySep, 9, 37, pan );
		}
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		for( int i= 0; i < trains.length; i++ ){
			trains[i].paint( g, pan );
			trains[i].move();
		}
	}

	private enum dotType {
		edge, solid
	};

	private enum dira {
		up, down, left, right
	};

	private class dot {
		Color	c;
		dotType	type= dotType.solid;
	}

	private class train {
		int		x, y, w, h, size, xSep, ySep, speed, xBand, yBand, speedBase, speedPlus;
		dot[]	cont;
		dira	direction	= dira.right;
		ESMPS	pan;

		//
		public train( int si, int xs, int ys, int speed, int speedPlus, ESMPS pa ) {
			this.size= si;
			this.xSep= xs;
			this.ySep= ys;
			this.pan= pa;
			this.speedBase= speed;
			this.speedPlus= speedPlus;
			this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
			xBand= (int) ( PS.getWidth() * bandRait );
			yBand= (int) ( pan.getHeight() * bandRait );
			init();
		}

		//
		void init() {
			double rand;
			//rand= Math.random() * ( xBand + yBand );
			//if( rand > xBand ){
			rand= Math.random();
			if( rand > 0.5 ){
				w= 20 + (int) ( Math.random() * 300 );
				h= (int) ( 2 + Math.random() * 3 );
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.right;
					x= (int) ( pan.getWidth() * Math.random() );
					y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 );
				}else{
					direction= dira.left;
					x= (int) ( pan.getWidth() * Math.random() );
					y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 );
				}
			}else{
				w= (int) ( 2 + Math.random() * 3 );
				h= 20 + (int) ( Math.random() * 300 );
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.up;
					y= (int) ( pan.getHeight() * Math.random() );
					x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 );
				}else{
					direction= dira.down;
					y= (int) ( pan.getHeight() * Math.random() );
					x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 );
				}
			}
			cont= new dot[ ( w + 1 ) * ( h + 1 )];
			for( int i= 0; i < cont.length; i++ ){
				cont[i]= new dot();
				//
				if( Math.random() > edgeRtio )
					cont[i].type= dotType.edge;
				//
				int intentC= (int) ( Math.random() * colorbase + colorplus );
				if( dC.getRed() == 0 && dC.getGreen() == 0 && dC.getBlue() == 0 ){
					if( intentC > 255 )
						intentC= 255;
					if( intentC < 0 )
						intentC= 0;
					dC= new Color( intentC, intentC, intentC, intentC );
					cont[i].c= dC;
				}else{
					dC= new Color( dC.getRed(), dC.getGreen(), dC.getBlue(), intentC );
					cont[i].c= dC;
				}
			}
		}

		//
		void move() {
			switch( direction ){
				case up :
					y-= speed;
					if( y + h < 0 - revDirExt ){
						direction= dira.down;
						this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
						x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 );
					}
					break;
				case down :
					y+= speed;
					if( y > pan.getHeight() + revDirExt ){
						direction= dira.up;
						this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
						x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 );
					}
					break;
				case left :
					x-= speed;
					if( x + w < 0 - revDirExt ){
						direction= dira.right;
						this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
						y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 );
					}
					break;
				case right :
					x+= speed;
					if( x > pan.getWidth() + revDirExt ){
						direction= dira.left;
						this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
						y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 );
					}
					break;
			}
		}

		//
		void paint( ESMPD g, ESMPS pan ) {
			int q= 0;
			for( int i= 0; i < h; i++ ){
				for( int e= 0; e < w; e++ ){
					if( cont[q].type == dotType.edge ){
						g.drawRect( x + ( e - w / 2 ) * ( size + xSep ), y + ( i - h / 2 ) * ( size + ySep ), size,
								size, 1, cont[q].c );
					}else if( cont[q].type == dotType.solid ){
						g.fillRect( x + ( e - w / 2 ) * ( size + xSep ), y + ( i - h / 2 ) * ( size + ySep ), size,
								size, cont[q].c );
					}
					q++ ;
				}
			}
		}
	}
}
