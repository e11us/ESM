package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundStripSnake extends pinSS implements AbleSMXConfig {
	private int			xTED, yTED;
	private Color		C;
	private snake		sk;
	private int			dirChangeMaxTime;
	private long		lastRFtime	= 0;
	private SManXElm	inp			= null;
	private ESMPS		PS;
	private int			framCount	= 0;
	private int			framePerMove;

	public PanelBackgroundStripSnake( SManXElm inp, ESMPS PS ) {
		//int xt, int yt, int size, int len, double chanC, Color c, JPanel p ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.C= inp.getAttr( SManXAttr.AttrType._color, "SneakColor" ).getColor();
		//
		sk= new snake( inp.getAttr( SManXAttr.AttrType._int, "SneakSize" ).getInteger(),
				inp.getAttr( SManXAttr.AttrType._int, "SneakLength" ).getInteger() );
		//
		this.framePerMove= inp.getAttr( SManXAttr.AttrType._int, "Frame#toMove" ).getInteger();
		this.xTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceX" ).getInteger();
		this.yTED= inp.getAttr( SManXAttr.AttrType._int, "ToEdgeDistanceY" ).getInteger();
		this.dirChangeMaxTime= inp.getAttr( SManXAttr.AttrType._int, "SneakChangeDirectionMaxTime(s)" ).getInteger();
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		if( helper.getTimeLong() - lastRFtime > dirChangeMaxTime * 1000.0 * Math.random() ){
			lastRFtime= helper.getTimeLong();
			sk.changeDirection();
		}
		framCount++ ;
		if( framCount >= framePerMove ){
			sk.move();
			framCount= 0;
		}
		for( cor2D cor : sk.body ){
			g.fillRect( cor.getX() - sk.size / 2, cor.getY() - sk.size / 2, sk.size, sk.size, C );
		}
	}

	private enum dira {
		up, down, left, right
	};

	private class snake {
		dira				direction	= dira.up;
		ArrayList <cor2D>	body		= new ArrayList <>();
		int					x, y, size, len;
		ESMPS				pan;

		public snake( int s, int l ) {
			this.size= s;
			this.len= l;
			this.pan= PS;
			init();
		}

		void init() {
			x= (int) ( pan.getWidth() * Math.random() );
			y= (int) ( pan.getHeight() * Math.random() );
			body.add( new cor2D( x, y ) );
			double rand;
			rand= Math.random();
			if( rand > 0.5 ){
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.right;
					for( int i= 0; i < len; i++ ){
						body.add( new cor2D( x - size, y ) );
					}
				}else{
					direction= dira.left;
					for( int i= 0; i < len; i++ ){
						body.add( new cor2D( x + size, y ) );
					}
				}
			}else{
				rand= Math.random();
				if( rand > 0.5 ){
					direction= dira.up;
					for( int i= 0; i < len; i++ ){
						body.add( new cor2D( x, y + size ) );
					}
				}else{
					direction= dira.down;
					for( int i= 0; i < len; i++ ){
						body.add( new cor2D( x, y - size ) );
					}
				}
			}
		}

		void move() {
			switch( direction ){
				case up :
					y-= size;
					if( y - size / 2 < yTED ){
						y= pan.getHeight() - yTED;
					}
					body.add( 0, new cor2D( x, y ) );
					body.remove( body.size() - 1 );
					break;
				case down :
					y+= size;
					if( y + size / 2 > pan.getHeight() - yTED ){
						y= yTED;
					}
					body.add( 0, new cor2D( x, y ) );
					body.remove( body.size() - 1 );
					break;
				case left :
					x-= size;
					if( x - size / 2 < xTED ){
						x= pan.getWidth() - xTED;
					}
					body.add( 0, new cor2D( x, y ) );
					body.remove( body.size() - 1 );
					break;
				case right :
					x+= size;
					if( x + size / 2 > pan.getWidth() - xTED ){
						x= xTED;
					}
					body.add( 0, new cor2D( x, y ) );
					body.remove( body.size() - 1 );
					break;
			}
		}

		void changeDirection() {
			switch( direction ){
				case up :
					if( Math.random() > 0.5 )
						direction= dira.left;
					else direction= dira.right;
					break;
				case down :
					if( Math.random() > 0.5 )
						direction= dira.left;
					else direction= dira.right;
					break;
				case left :
					if( Math.random() > 0.5 )
						direction= dira.up;
					else direction= dira.down;
					break;
				case right :
					if( Math.random() > 0.5 )
						direction= dira.up;
					else direction= dira.down;
					break;
			}
		}
	}
}
