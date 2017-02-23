package ellus.ESM.pinnable.SS;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinLF;



public class PanelSlidingStrips extends pinLF {
	private int		yos, size, sep;
	private Color	C;
	private boolean	dir;

	public PanelSlidingStrips( int x, int y, int w, int h, int yos, int size, int sep, Color C, boolean dir ) {
		this.C= C;
		this.yos= yos;
		this.size= size;
		this.sep= sep;
		super.setXY( x, x + w, y, y + h );
		this.dir= dir;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( sep == 0 )
			return;
		pan.addGUIactiveLF( this );
		//
		if( dir ){
			for( int i= super.ymin - yos; i <= super.ymax; i+= sep ){
				g.drawLineRJ( super.xmin, i, super.xmax, i + yos, size, C );
			}
		}else{
			for( int i= super.ymin - yos; i <= super.ymax; i+= sep ){
				g.drawLineRJ( super.xmax, i, super.xmin, i + yos, size, C );
			}
		}
	}
}
