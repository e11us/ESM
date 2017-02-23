package ellus.ESM.ESMW;

import java.util.ArrayList;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleMouseDrag;



public class ESMPL {
	// constant;
	public static final int						pinnableTotalLayer	= 10;
	//
	private ArrayList <ArrayList <pinnable>>	layers;
	private int[]								thisGet				= new int[pinnableTotalLayer];
	private int									thisGetLayer		= 0;

	public ESMPL() {
		reset();
	}

	protected void reset() {
		layers= new ArrayList <>();
		for( int i= 0; i < pinnableTotalLayer; i++ ){
			layers.add( new ArrayList <pinnable>() );
		}
		for( int i= 0; i < pinnableTotalLayer; i++ ){
			thisGet[i]= 0;
		}
	}

	public void add( int layerNumber, pinnable pin ) {
		if( layerNumber > ( pinnableTotalLayer - 1 ) )
			layers.get( pinnableTotalLayer - 1 ).add( pin );
		else if( layerNumber < 0 )
			layers.get( 0 ).add( pin );
		else layers.get( layerNumber ).add( pin );
	}

	// like a iterator.
	protected pinnable get() {
		pinnable pin= null;
		while( true ){
			if( this.thisGetLayer > pinnableTotalLayer - 1 ){
				this.thisGetLayer= 0;
				return null;
			}
			pin= get( this.thisGetLayer );
			if( pin != null )
				return pin;
			else this.thisGetLayer++ ;
		}
	}

	protected pinnable get( int layerNumber ) {
		if( layerNumber > ( pinnableTotalLayer - 1 ) ){
			layerNumber= pinnableTotalLayer - 1;
			if( thisGet[layerNumber] <= layers.get( layerNumber ).size() - 1 ){
				return layers.get( layerNumber ).get( thisGet[layerNumber]++ );
			}else{
				thisGet[layerNumber]= 0;
				return null;
			}
		}else if( layerNumber < 0 ){
			layerNumber= 0;
			if( thisGet[layerNumber] <= layers.get( layerNumber ).size() - 1 ){
				return layers.get( layerNumber ).get( thisGet[layerNumber]++ );
			}else{
				thisGet[layerNumber]= 0;
				return null;
			}
		}else{
			if( thisGet[layerNumber] <= layers.get( layerNumber ).size() - 1 ){
				return layers.get( layerNumber ).get( thisGet[layerNumber]++ );
			}else{
				thisGet[layerNumber]= 0;
				return null;
			}
		}
	}

	protected ArrayList <pinnable> getAll( int i ) {
		return (ArrayList <pinnable>)layers.get( i ).clone();
	}

	protected ArrayList <pinnable> getAll() {
		ArrayList <pinnable> ret= new ArrayList <>();
		for( int i= 0; i < pinnableTotalLayer; i++ ){
			ret.addAll( layers.get( i ) );
		}
		return ret;
	}

	protected void printAllLayers( ESMPD g, ESMPS PS ) {
		// reset content min max.
		int contentXmin= Integer.MAX_VALUE;
		int contentXmax= Integer.MIN_VALUE;
		int contentYmin= Integer.MAX_VALUE;
		int contentYmax= Integer.MIN_VALUE;
		// local variable.
		pinnable pin;
		ArrayList <pinnable> pins;
		ArrayList <pinnable> pinListPrinted= new ArrayList <>();
		// start testing all layers of conetent.
		for( int e= 0; e < pinnableTotalLayer; e++ ){
			pins= layers.get( e );
			// display.println( this.getClass().toString(), "Total of: " + pins.size() + " pinnables in Layer: " + (e+1) );
			if( pins == null || pins.size() == 0 )
				continue;
			for( int i= 0; i < pins.size(); i++ ){
				//
				// remove invalid pins.
				pin= pins.get( i );
				if( pin == null || pin.removeMe() ){
					pin= null;
					pins.remove( i );
					i-- ;
					continue;
				}
				//
				// print drag by mouse, return immediately.
				if( pin instanceof AbleMouseDrag && ( (AbleMouseDrag)pin ).MouseDragState() ){
					//( (AbleMouseDrag)pin ).paintAt( PS.MouseCurrent.getX(),	PS.MouseCurrent.getY(), g2d,	PS );
					( (AbleMouseDrag)pin ).paintByMouse( g, PS );
					//
					PS.addGUIactive( pin );
					//
					continue;
				}
				//
				// FL & SS always print without check if are in the viewing area. because they are always print at a location.
				if( pin instanceof pinLF || pin instanceof pinSS ){
					pin.paint( g, PS );
					continue;
				}
				//
				// if NI or regular pin, then update the the viewing area, and check if in view, if not skip.
				// set content Min Max.
				if( pin.getXmin() < contentXmin )
					contentXmin= pin.getXmin();
				if( pin.getYmin() < contentYmin )
					contentYmin= pin.getYmin();
				if( pin.getXmax() > contentXmax )
					contentXmax= pin.getXmax();
				if( pin.getYmax() > contentYmax )
					contentYmax= pin.getYmax();
				/*
				//
				// test msg.
				if( PS.MsgOutputTest ){
					f.f( PS.ViewCenterX + " " +PS.ViewCenterY + " " + PS.PanelWidth + "x" + PS.PanelHeight + " mx" +
							contentXmin + " mx" + contentXmax + " my" + contentYmin + " my" + contentYmax + " " +
							" vX" + PS.ViewXmin + " " + PS.ViewXmax + " vY" + PS.ViewYmin + " " + PS.ViewYmax );
					f.f( "pin: " + pin.getID() + " " + pin.getXYminMax() );
				}
				*/
				//
				// check if visable, yes then print.
				// diff print policy depends on if edgeviewLim is set.
				if( PS.EdgeViewLimitY != 0 || PS.EdgeViewLimitX != 0 ){
					if( pin.getXmax() < PS.ViewXmax && pin.getYmax() < PS.ViewYmax &&
							pin.getXmin() > PS.ViewXmin && pin.getYmin() > PS.ViewYmin )
						pin.paint( g, PS );
				}else{
					if( pin.getXmax() < PS.ViewXmin || pin.getYmax() < PS.ViewYmin ||
							pin.getXmin() > PS.ViewXmax || pin.getYmin() > PS.ViewYmax ){}else{
						pin.paint( g, PS );
					}
				}
			}
		}
		//
		PS.contLocationViewChangeLockXmin= contentXmin;
		PS.contLocationViewChangeLockYmin= contentYmin;
		PS.contLocationViewChangeLockXmax= contentXmax;
		PS.contLocationViewChangeLockYmax= contentYmax;
	}
}
