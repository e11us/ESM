package ellus.ESM.pinnable.SS;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundStripCenterFlow extends pinSS implements AbleSMXConfig {
	private double		bandRait	= 0.13;
	private int			revDirExt	= 300;
	private train[]		trains;
	private Color		stripC;
	// 0=both, 1= vertical only, -1= horizontal only.
	private int			dir			= 0;
	private SManXElm	inp			= null;
	private ESMPS		PS;

	public PanelBackgroundStripCenterFlow( SManXElm inp, ESMPS pan ) {
		// train#, speedBasic, speedPlus, bandRate, stp Color, jpan
		this.inp= inp;
		this.PS= pan;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		int trainTot= inp.getAttr( SManXAttr.AttrType._int, "train#" ).getInteger();
		int sB= inp.getAttr( SManXAttr.AttrType._int, "speedBase" ).getInteger();
		int sP= inp.getAttr( SManXAttr.AttrType._int, "speedPlus" ).getInteger();
		dir= inp.getAttr( SManXAttr.AttrType._int, "trainDirection" ).getInteger();
		bandRait= inp.getAttr( SManXAttr.AttrType._double, "bandRate" ).getDouble();
		stripC= inp.getAttr( SManXAttr.AttrType._color, "stripColor" ).getColor();
		trains= new train[trainTot];
		for( int i= 0; i < trains.length; i++ ){
			trains[i]= new train( sB, sP, PS );
		}
	}

	@Override
	public void paint( ESMPD drawer, ESMPS pan ) {
		for( int i= 0; i < trains.length; i++ ){
			trains[i].paint( drawer, pan );
			trains[i].move();
		}
	}

	private enum dira {
		up, down, left, right
	};

	private class train {
		int		x, y, w, h, speed, xBand, yBand, speedBase, speedPlus;
		dira	direction	= dira.right;
		ESMPS	pan;

		//
		public train( int speed, int speedPlus, ESMPS pa ) {
			this.pan= pa;
			this.speedBase= speed;
			this.speedPlus= speedPlus;
			this.speed= (int) ( speedPlus * Math.random() ) + speedBase;
			xBand= (int) ( pan.getWidth() * bandRait );
			yBand= (int) ( pan.getHeight() * bandRait );
			init();
		}

		//
		void init() {
			double rand;
			if( dir != 0 ){
				if( dir == 1 )
					rand= 1;
				else rand= 0;
			}else rand= Math.random();
			if( rand > 0.5 ){
				w= 1600 + (int) ( Math.random() * 800 );
				h= (int) ( 35 + Math.random() * 10 );
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.right;
					x= (int) ( pan.getWidth() * Math.random() );
					y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 - 20 );
				}else{
					direction= dira.left;
					x= (int) ( pan.getWidth() * Math.random() );
					y= (int) ( Math.random() * yBand + pan.getHeight() / 2 - yBand / 2 - 20 );
				}
			}else{
				w= (int) ( 35 + Math.random() * 10 );
				h= 1600 + (int) ( Math.random() * 800 );
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.up;
					y= (int) ( pan.getHeight() * Math.random() );
					// -30 compensate.
					x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 - 30 );
				}else{
					direction= dira.down;
					y= (int) ( pan.getHeight() * Math.random() );
					x= (int) ( Math.random() * xBand + pan.getWidth() / 2 - xBand / 2 - 30 );
				}
			}
			//f.f( pan.getWidth() + " " + pan.getHeight() + " - " + x + " " + y + " " + w+ " " + h );
		}

		//
		void paint( ESMPD drawer, ESMPS pan ) {
			drawer.fillRect( x - w / 2, y - h / 2, w, h, stripC );
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

		private dira getRandDirection() {
			if( Math.random() > 0.5 ){
				if( Math.random() > 0.5 )
					return dira.up;
				else return dira.down;
			}else{
				if( Math.random() > 0.5 )
					return dira.left;
				else return dira.right;
			}
		}
	}
}
/*
public PanelBackgroundStripCenterFlow( int trainTot, int sB, int sP, double bandR, Color stpC, PanState pan ) {
	// train#, speedBasic, speedPlus, bandRate, stp Color, jpan
	trains= new train[trainTot];
	this.bandRait= bandR;
	this.stripC= stpC;
	for( int i= 0; i < trains.length; i++ ){
		trains[i]= new train( sB, sP, pan );
	}
}
public PanelBackgroundStripCenterFlow( int trainTot, int sB, int sP, double bandR, Color stpC, int dir,
		PanState pan ) {
	trains= new train[trainTot];
	this.bandRait= bandR;
	this.stripC= stpC;
	this.dir= dir;
	for( int i= 0; i < trains.length; i++ ){
		trains[i]= new train( sB, sP, pan );
	}
}
*/