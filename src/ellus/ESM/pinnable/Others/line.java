package ellus.ESM.pinnable.Others;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.pinnable.pinSS;



public class line extends pinSS {
	private cor2D	p1	= null, p2= null;
	private Color	c1	= null, c2= null;
	private int		thic;

	public line( cor2D p1, cor2D p2, Color c1, Color c2, int thic ) {
		this.p1= p1;
		this.p2= p2;
		this.c1= c1;
		this.c2= c2;
		this.thic= thic;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// gradient paint. the background of the button polygon.
		Paint gp= new GradientPaint( pan.w2bX( p1.getX() ), pan.w2bY( p1.getY() ), c1,
				pan.w2bX( p2.getX() ), pan.w2bY( p2.getY() ), c2, true );
		g.drawLine( pan.w2bX( p1.getX() ), pan.w2bY( p1.getY() ),
				pan.w2bX( p2.getX() ), pan.w2bY( p2.getY() ), thic, gp );
	}
}
