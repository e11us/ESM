package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.Font;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;



// similar to matx bg. just have one more thing, the time display.
public class PanelBackgroundMatx2 extends pinSS {
	private Color	tx1C;
	private Font[]	fonts				= null;
	// font size variant.
	private int		FontSizeVar			= 16;
	// for time.
	private int		timeFontI			= Integer.parseInt( SMan.getSetting( 1101 ) );
	private int		timeFontS			= Integer.parseInt( SMan.getSetting( 1102 ) );
	private Font	timeFont			= SCon.FontList.get( timeFontI ).deriveFont( (float)timeFontS );
	private int		timeChangeLocaDelay	= Integer.parseInt( SMan.getSetting( 1103 ) );
	private long	timeLastChange		= helper.getTimeLong();

	public PanelBackgroundMatx2( int fontI, int fontS, Color[] co ) {
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		tx1C= co[2];
		fonts= new Font[FontSizeVar];
		for( int i= 0; i < FontSizeVar; i++ ){
			fonts[i]= SCon.FontList.get( fontI ).deriveFont( (float) ( fontS - FontSizeVar / 2.0 + i ) );
		}
		tx1C= tx1C.darker().darker().darker().darker();
	}
	/*
	@Override public void paint( ESMPD drawer, ESMPS pan ) {
		// see if need to set rand array.
		if( randNum == null ){
			randNum= new int[(int) ( ( (double)pan.getWidth() / fontS + 1 )
					* ( (double)pan.getHeight() / fontS + 1 ) )];
			randCol= new Color[(int) ( ( (double)pan.getWidth() / fontS + 1 )
					* ( (double)pan.getHeight() / fontS + 1 ) )];
			for( int i= 0; i < randNum.length; i++ ){
				randNum[i]= helper.randAlphcapNum();
				randCol[i]= new Color( 0, (int) ( Math.random() * 50 ), 0, 200 );
			}
		}
		// check if # mutate.
		if( Math.random() * 1000 <= NumMutaThres ){
			for( int i= 0; i < randNum.length; i++ ){
				randNum[i]= helper.randAlphcapNum();
			}
		}
		// paint background first.
		Paint gp= new GradientPaint( 0, 0, bg1C, 0, pan.getHeight(), bg2C, true );
		Graphics2D g2d= (Graphics2D)g;
		g2d.setPaint( gp );
		g2d.fillRect( 0, 0, pan.getWidth(), pan.getHeight() );
		// draw matrix.
		int x= 0, y= 0, ind= 0;
		while( ind < randNum.length ){
			if( y > pan.getHeight() ){
				y= 0;
				x+= fontS;
			}
			// check if color mutate.
			if( Math.random() * 1000 <= ColorMutaThres ){
				randCol[ind]= tx1C;
			}else if( Math.random() * 1000 <= ColorMutaThres2 ){
				randCol[ind]= randCol[ind].darker();
			}
			g.setFont( fonts[(int) ( Math.random() * FontSizeVar )] );
			g.setColor( randCol[ind] );
			g.drawString( ( (char)randNum[ind++ ] ) + "", x, y );
			y+= fontS + Math.random() * FontSizeVar - FontSizeVar / 2.0;
		}
		// draw the time.
		Calendar cal= Calendar.getInstance();
		SimpleDateFormat sdf= new SimpleDateFormat( "HH : mm : ss" );
		// draw time.
		g.setColor( tx1C );
		g.setFont( timeFont );
		String time= sdf.format( cal.getTime() );
		int tw= getTimeFontWH( time, g );
		if( ( helper.getTimeLong() - timeLastChange ) / 1000 > timeChangeLocaDelay ){
			timeLastChange= helper.getTimeLong();
			timeX= (int) ( Math.random() * ( pan.getWidth() - tw - 100 ) + 50 );
			timeY= (int) ( timeFontS + Math.random() * ( pan.getHeight() - timeFontS - 100 ) + 50 );
		}
		g.drawString( time, timeX, timeY );
	}
	*/
}
