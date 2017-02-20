package ellus.ESM.pinnable;

import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;



public interface pinnable {
	// paint the pin.
	public void paint( ESMPD g, ESMPS pan );

	// basic getter.
	public String getID();

	public cor2D getLocation();

	public int getXmin();

	public int getYmin();

	public int getXmax();

	public int getYmax();

	public cor2D getCenter();

	public int getWidth();

	public int getHeight();

	public String getXYMM();

	public boolean isWithIn( int x, int y );

	public boolean removeMe();

	// basic setter.
	public void setID( String inp );

	public void setXY( int x1, int x2, int y1, int y2 );

	public void delete();
}
