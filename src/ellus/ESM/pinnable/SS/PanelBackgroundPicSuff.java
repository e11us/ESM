package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Image;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;



public class PanelBackgroundPicSuff extends pinSS {
	private Image[]	img					= null;
	private Image	image				= null;
	private long	timeLastChange		= helper.getTimeLong();
	private int		timeChangeLocaDelay	= 0;

	public PanelBackgroundPicSuff( Image[] img, int changeTime ) {
		this.img= img;
		timeChangeLocaDelay= changeTime;
		image= this.img[(int) ( Math.random() * this.img.length )];
	}
	
	public PanelBackgroundPicSuff( Image imge ) {
		image= imge;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		int x= 0;
		int y= 0;
		if( img != null &&  ( helper.getTimeLong() - timeLastChange ) / 1000 > timeChangeLocaDelay ){
			timeLastChange= helper.getTimeLong();
			image= this.img[(int) ( Math.random() * this.img.length )];
		}
		//
		int wid= pan.getImageWidth( image );
		int hei= pan.getImageHeight( image );
		if( wid < pan.getWidth() )
			x= ( pan.getWidth() - wid ) / 2;
		if( hei < pan.getHeight() )
			y= ( pan.getHeight() - hei ) / 2;
		if( x != 0 && y != 0 ){
			// draw in middle.
			g.drawImage( image, x, y );
		}else{
			g.drawImage( image, x, y );
		}
	}
}
