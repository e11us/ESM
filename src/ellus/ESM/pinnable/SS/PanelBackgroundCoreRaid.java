package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.geom.Arc2D;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;



public class PanelBackgroundCoreRaid extends pinSS {
	private int		circlThic	= 10;
	private int		circlSep;
	private int		coreSiz;
	private int		rr;
	private int		gg;
	private int		bb;
	private Arc2D[]	arcs;
	private Arc2D	ark;
	private int		centOSX, centOSY;
	//
	private int		coloGlowDir	= -1;
	private int		coloGlow	= 3;
	private int		colorFloor	= 111;

	public PanelBackgroundCoreRaid( int[] list ) {
		// cirl#, cirl thic, cirl sep, int corSize // cirC
		circlThic= list[1];
		circlSep= list[2];
		coreSiz= list[3];
		rr= list[4];
		gg= list[5];
		bb= list[6];
		this.coloGlow= list[7];
		this.centOSX= list[8];
		this.centOSY= list[9];
		//
		//
		arcs= new Arc2D[list[0]];
		for( int i= 0; i < arcs.length; i++ ){
			ark= new Arc2D.Float( Arc2D.OPEN );
			ark.setAngleStart( 0 );
			ark.setAngleExtent( 360 );
			arcs[i]= ark;
		}
	}

	@Override
	public void paint( ESMPD drawer, ESMPS pan ) {
		if( coloGlowDir > 0 ){
			if( rr + ( coloGlow * coloGlowDir ) > 255 ||
					gg + ( coloGlow * coloGlowDir ) > 255 ||
					bb + ( coloGlow * coloGlowDir ) > 255 ){
				coloGlowDir= -1;
			}else{
				rr+= coloGlow * coloGlowDir;
				gg+= coloGlow * coloGlowDir;
				bb+= coloGlow * coloGlowDir;
			}
		}else{
			if( rr + ( coloGlow * coloGlowDir ) < colorFloor &&
					gg + ( coloGlow * coloGlowDir ) < colorFloor &&
					bb + ( coloGlow * coloGlowDir ) < colorFloor ){
				coloGlowDir= 1;
			}else{
				rr+= coloGlow * coloGlowDir;
				gg+= coloGlow * coloGlowDir;
				bb+= coloGlow * coloGlowDir;
			}
		}
		//
		int colorDec= 18;
		int rrr= rr, ggg= gg, bbb= bb;
		//
		if( rrr > 255 )
			rrr= 255;
		if( ggg > 255 )
			ggg= 255;
		if( bbb > 255 )
			bbb= 255;
		//
		if( rrr < 0 )
			rrr= 0;
		if( ggg < 0 )
			ggg= 0;
		if( bbb < 0 )
			bbb= 0;
		//
		//int rrr=C.getRed(), ggg= C.getBlue(), bbb= C.getBlue();
		//
		drawer.fillOval( centOSX + pan.getWidth() / 2 - coreSiz, centOSY + pan.getHeight() / 2 - coreSiz,
				coreSiz * 2, coreSiz * 2, new Color( rrr, ggg, bbb ) );
		for( int i= 0; i < arcs.length; i++ ){
			if( ( rrr - colorDec ) >= 0 ){
				rrr-= colorDec;
			}
			if( ( ggg - colorDec ) >= 0 ){
				ggg-= colorDec;
			}
			if( ( bbb - colorDec ) >= 0 ){
				bbb-= colorDec;
			}
			//
			ark= arcs[i];
			ark.setFrame( centOSX + pan.getWidth() / 2 - coreSiz - ( circlThic + circlSep ) * i,
					centOSY + pan.getHeight() / 2 - coreSiz - ( circlThic + circlSep ) * i,
					( ( circlThic + circlSep ) * i + coreSiz ) * 2,
					( ( circlThic + circlSep ) * i + coreSiz ) * 2 );
			drawer.drawShape( ark, circlThic, new Color( rrr, ggg, bbb ) );
		}
	}
}
