package ellus.ESM.Machine;

import java.io.Serializable;



/* -----------------------------------------------------------------------------
 *
 * -----------------------------------------------------------------------------
 */
public class cor2D implements Serializable {
	private int	x	= 0;
	private int	y	= 0;

	/* --------------------------------------------------------------------------
	 * ---
	 * --------------------------------------------------------------------------
	 * --- */
	public cor2D() {
		x= 0;
		y= 0;
	}

	public cor2D( int xi, int yi ) {
		x= xi;
		y= yi;
	}

	public cor2D( cor2D inp ) {
		x= inp.getX();
		y= inp.getY();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX( int xi ) {
		x= xi;
	}

	public void setY( int yi ) {
		y= yi;
	}

	@Override
	public cor2D clone() {
		return new cor2D( x, y );
	}

	public cor2D plus( int xi, int yi ) {
		return new cor2D( x + xi, y + yi );
	}

	public cor2D plus( cor2D inp ) {
		return new cor2D( x + inp.getX(), y + inp.getY() );
	}

	public cor2D minu( cor2D inp ) {
		return new cor2D( x - inp.getX(), y - inp.getY() );
	}

	@Override
	public String toString() {
		return new String( "(" + x + "," + y + ")" );
	}

	public int distance2( cor2D inp ) {
		return (int)Math.round( Math.sqrt( Math.abs( inp.x - this.x ) ^ 2
				+ Math.abs( inp.y - this.y ) ^ 2 ) );
	}
}
