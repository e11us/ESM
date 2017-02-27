package ellus.ESM.pinnable.Others;

import ellus.ESM.pinnable.pin;

public class CircleProgressBar extends pin {
	
	@Override
	public void paint( Graphics g, cor2D orign, JPanel pan ) {
		if( bad )
			return;
		// calc days left first.
		if( !daysLeftset ){
			try{
				daysLeft( g );
				daysLeftset= true;
			}catch ( Exception ee ){
				bad= true;
				return;
			}
		}
		// the location related.
		cor2D p= helper.cor2Dw2b( orign, super.getLocation() );
		Graphics2D g2= (Graphics2D)g;
		// draw days gone.
		Arc2D.Float arc2= new Arc2D.Float( Arc2D.OPEN );
		arc2.setFrame( p.getX() + GCSV.DCDcircleOffSetX,
				p.getY() + GCSV.DCDcircleOffSetY, GCSV.DCDcircleDiameter,
				GCSV.DCDcircleDiameter );
		arc2.setAngleStart( 360 * daysLeftRatio );
		arc2.setAngleExtent( 360 * ( 1 - daysLeftRatio ) );
		g2.setColor( GCSV.DCDcircleGoneColor );
		g2.setStroke( new BasicStroke( 2 ) );
		g2.draw( arc2 );
		// Draw circle. days left.
		Arc2D.Float arc= new Arc2D.Float( Arc2D.OPEN );
		arc.setFrame( p.getX() + GCSV.DCDcircleOffSetX,
				p.getY() + GCSV.DCDcircleOffSetY, GCSV.DCDcircleDiameter,
				GCSV.DCDcircleDiameter );
		arc.setAngleStart( 0 );
		arc.setAngleExtent( 360 * daysLeftRatio );
		g2.setColor( crC );
		g2.setStroke( new BasicStroke( GCSV.DCDcircleThick ) );
		g2.draw( arc );
		g2.setStroke( new BasicStroke( 1 ) );
		// draw the txt.
		g.setColor( GCSV.DCDtxtColor );
		Font fon= GCSV.DCDFont.deriveFont( (float)GCSV.DCDtxtSiz );
		g.setFont( fon );
		g.drawString( msg, p.getX() + GCSV.DCDtxtOffSetX, p.getY() +
				GCSV.DCDtxtOffSetY + GCSV.DCDtxtSiz - 2 );
		// draw days left.
		g.setColor( GCSV.DCDtxtColor );
		Font fon2= GCSV.timeDisplayFont.deriveFont( (float)GCSV.DCDtxtDayNumSiz );
		g.setFont( fon2 );
		g.drawString( daysLeft + "", p.getX() + daysLeftNumOffSetX, p.getY() +
				daysLeftNumOffSetY );
	}
}
