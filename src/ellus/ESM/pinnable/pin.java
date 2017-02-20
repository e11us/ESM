package ellus.ESM.pinnable;

import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.helper;



public class pin implements pinnable {
	public static final int	layerMax	= 5;
	//
	// basic property of all pinned item.
	private String			ID			= helper.getCurrentTimeStamp() + "_" + helper.randAN( 5 );
	protected cor2D			location	= new cor2D();
	protected int			xmin		= 0;
	protected int			xmax		= 0;
	protected int			ymin		= 0;
	protected int			ymax		= 0;
	//
	// print property.
	protected boolean		isHovered	= false;
	// print layer 0 = most bottom, higher number, more on top. ( range 0-4 );
	protected int			printLayer	= 0;
	// should remove this pin?
	protected boolean		removeMe;

	// basic getter function.
	@Override
	public String getID() {
		return ID;
	}

	@Override
	public cor2D getLocation() {
		return location.clone();
	}

	@Override
	public int getWidth() {
		return xmax - xmin;
	}

	@Override
	public int getHeight() {
		return ymax - ymin;
	}

	@Override
	public int getXmin() {
		return xmin;
	}

	@Override
	public int getYmin() {
		return ymin;
	}

	@Override
	public int getXmax() {
		return xmax;
	}

	@Override
	public int getYmax() {
		return ymax;
	}

	@Override
	public String getXYMM() {
		return xmin + " " + xmax + " " + ymin + " " + ymax;
	}

	@Override
	public boolean removeMe() {
		return removeMe;
	}

	@Override
	public boolean isWithIn( int x, int y ) {
		if( x > xmin && x < xmax && y > ymin && y < ymax ){
			return true;
		}else{
			//System.out.println( x + " " + y + " " + xmin + " " +
			//		xmax + " " + ymin + " " + ymax );
			return false;
		}
	}

	// basic setter.
	@Override
	public void setID( String inp ) {
		this.ID= inp;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| location x min, x max, y min, y max.
	||||--------------------------------------------------------------------------------------------*/
	@Override
	public void setXY( int x1, int x2, int y1, int y2 ) {
		this.location= new cor2D( x1, y1 );
		this.xmin= x1;
		this.xmax= x2;
		this.ymin= y1;
		this.ymax= y2;
	}

	// function to be implemented by inherited class.
	@Override
	public pinnable clone() {
		return null;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {}

	@Override
	public void delete() {
		this.removeMe= true;
	}

	@Override
	public cor2D getCenter() {
		return new cor2D( ( xmax - xmin ) / 2 + xmin, ( ymax - ymin ) / 2 + ymin );
	}
}
