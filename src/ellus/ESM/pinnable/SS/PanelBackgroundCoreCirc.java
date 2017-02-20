package ellus.ESM.pinnable.SS;

import java.awt.Color;
import java.awt.geom.Arc2D;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;



public class PanelBackgroundCoreCirc extends pinSS {
	private int		circlThic	= 10;
	private int		circlSep;
	private int		circlSiz;
	private Color	c;
	private Arc2D[]	arcs;
	private Arc2D	ark;
	private int		centOSX, centOSY;

	public PanelBackgroundCoreCirc( int[] list, Color c ) {
		// cirl#, cirl thic, cirl sep, cirl Size // cirC
		circlSiz= list[3];
		circlThic= list[1];
		circlSep= list[2];
		this.centOSX= list[4];
		this.centOSY= list[5];
		this.c= c;
		//
		//
		arcs= new Arc2D[list[0]];
		for( int i= 0; i < arcs.length; i++ ){
			ark= new Arc2D.Float( Arc2D.CHORD );
			ark.setAngleStart( Math.random() * 360 );
			ark.setAngleExtent( 25 * Math.random() + 310 );
			arcs[i]= ark;
		}
	}

	@Override
	public void paint( ESMPD drawer, ESMPS pan ) {
		for( int i= 0; i < arcs.length; i++ ){
			ark= arcs[i];
			ark.setAngleStart( ark.getAngleStart() - 2.7 * ( arcs.length - i ) + 1 );
			ark.setFrame( centOSX + pan.getWidth() / 2 - circlSiz - ( circlThic + circlSep ) * i,
					centOSY + pan.getHeight() / 2 - circlSiz - ( circlThic + circlSep ) * i,
					( ( circlThic + circlSep ) * i + circlSiz ) * 2,
					( ( circlThic + circlSep ) * i + circlSiz ) * 2 );
			drawer.drawShape( ark, circlThic, c );
		}
	}
}
