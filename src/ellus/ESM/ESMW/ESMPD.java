package ellus.ESM.ESMW;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.JPanel;



public class ESMPD {
	/*||----------------------------------------------------------------------------------------------
	 ||| reset the graphics object. ( must set, for drawing, and anti alising. )
	||||--------------------------------------------------------------------------------------------*/
	public void setGraphic( Graphics GG, JPanel jp ) {
		this.jp= jp;
		G= GG;
		g2= (Graphics2D)G;
		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON );
		g2.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
	}

	public int getTxtWid( String msg, Font F ) {
		if( msg == null )
			return 0;
		FontMetrics metrics= G.getFontMetrics( F );
		return metrics.stringWidth( msg );
	}

	public void drawString( String msg, int i, int j, Color C, Font F ) {
		refreshG();
		G.setColor( C );
		G.setFont( F );
		G.drawString( msg, i, j );
	}

	public void fillRect( int i, int j, int width, int height, Color C ) {
		refreshG();
		G.setColor( C );
		G.fillRect( i, j, width, height );
	}

	public void fillRect( int i, int j, int width, int height, Paint C ) {
		refreshG();
		g2.setPaint( C );
		g2.fillRect( i, j, width, height );
	}

	public void drawRect( int i, int j, int width, int height, int thic, Color C ) {
		refreshG();
		g2.setStroke( new BasicStroke( thic ) );
		G.setColor( C );
		G.drawRect( i, j, width, height );
	}

	public void drawLine( int i1, int j1, int i2, int j2, int thic, Color C ) {
		if( thic == 0 )
			return;
		refreshG();
		g2.setColor( C );
		g2.setStroke( new BasicStroke( thic ) );
		g2.drawLine( i1, j1, i2, j2 );
	}

	public void drawLine( int i1, int j1, int i2, int j2, int thic, Paint C ) {
		if( thic == 0 )
			return;
		refreshG();
		g2.setPaint( C );
		g2.setStroke( new BasicStroke( thic ) );
		g2.drawLine( i1, j1, i2, j2 );
	}

	public void drawLineRJ( int i1, int j1, int i2, int j2, int thic, Color C ) {
		if( thic == 0 )
			return;
		refreshG();
		g2.setColor( C );
		g2.setStroke( new BasicStroke( thic, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
		g2.drawLine( i1, j1, i2, j2 );
	}

	public void drawPolygon( Polygon pg, int thic, Color C ) {
		refreshG();
		g2.setColor( C );
		g2.setStroke( new BasicStroke( thic, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
		g2.drawPolygon( pg );
	}

	public void drawPolyLine( int[] xPoints, int[] yPoints, int thic, Color C ) {
		refreshG();
		g2.setColor( C );
		g2.setStroke( new BasicStroke( thic, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND ) );
		g2.drawPolyline( xPoints, yPoints, xPoints.length );
	}

	public void fillPolygon( Polygon pg, Color C ) {
		refreshG();
		g2.setColor( C );
		g2.fillPolygon( pg );
	}

	public void fillPolygon( Polygon pg, Paint C ) {
		refreshG();
		g2.setPaint( C );
		g2.fillPolygon( pg );
	}

	public void drawShape( Shape geo, int thic, Color C ) {
		refreshG();
		g2.setStroke( new BasicStroke( thic ) );
		g2.setColor( C );
		g2.draw( geo );
	}

	public void fillShape( Shape geo, Paint C ) {
		refreshG();
		g2.setPaint( C );
		g2.fill( geo );
	}

	public void fillShape( Shape geo, Color C ) {
		refreshG();
		g2.setColor( C );
		g2.fill( geo );
	}

	public void fillOval( int i, int j, int k, int l, Paint C ) {
		refreshG();
		g2.setPaint( C );
		g2.fillOval( i, j, k, l );
	}

	public void fillOval( int i, int j, int k, int l, Color C ) {
		refreshG();
		g2.setColor( C );
		g2.fillOval( i, j, k, l );
	}

	public void drawOval( int i, int j, int k, int l, int thic, Color C ) {
		refreshG();
		g2.setStroke( new BasicStroke( thic ) );
		g2.setColor( C );
		g2.drawOval( i, j, k, l );
	}

	public FontMetrics getFontMetrics( Font fs ) {
		refreshG();
		return G.getFontMetrics();
	}

	public void drawImage( Image img, int i, int j ) {
		refreshG();
		G.drawImage( img, i, j, jp );
		G.finalize();
	}

	private Graphics	G	= null;
	private Graphics2D	g2	= null;
	private JPanel		jp	= null;

	private void refreshG() {
		this.g2= (Graphics2D)G;
	}
}
