package ellus.ESM.Machine;

import java.util.ArrayList;
import java.util.Collections;



public class BArray <E extends Comparable> {
	private ArrayList <E> list= null;

	public BArray() {
		list= new ArrayList <>();
	}

	public synchronized boolean add( E newObj ) {
		return insert( newObj );
	}

	public boolean addAll( ArrayList <E> inp ) {
		this.list= new ArrayList <>();
		for( E tmp : inp ){
			insert( tmp );
		}
		return true;
	}

	public E get( int i ) {
		if( i > list.size() - 1 ){
			return null;
		}
		return list.get( i );
	}

	public void remove( int i ) {
		if( i > list.size() - 1 ){
			return;
		}
		list.remove( i );
		return;
	}

	public boolean remove( E obj ) {
		return this.removeI( obj );
	}

	public void printAll() {
		for( E tmp : list ){
			System.out.println( tmp.toString() );
		}
		System.out.println( "---------------------------" );
	}

	public ArrayList <E> getAll() {
		return (ArrayList <E>)list.clone();
	}

	private boolean insert( E obj ) {
		if( list.size() == 0 ){
			list.add( obj );
			return true;
		}else if( list.size() == 1 ){
			if( list.get( 0 ).compareTo( obj ) > 0 ){
				list.add( 0, obj );
				return true;
			}else{
				list.add( 1, obj );
				return true;
			}
		}else{
			int sInd= 0;
			int eInd= list.size() - 1;
			//
			while( ( eInd - sInd ) != 1 ){
				if( ( eInd - sInd ) % 2 == 0 ){
					if( list.get( ( eInd - sInd ) / 2 + sInd ).compareTo( obj ) > 0 ){
						eInd= ( eInd - sInd ) / 2 + sInd;
					}else{
						sInd= ( eInd - sInd ) / 2 + sInd;
					}
				}else{
					if( list.get( eInd ).compareTo( obj ) > 0 ){
						eInd-- ;
					}else{
						list.add( eInd + 1, obj );
						return true;
					}
				}
				//System.out.println( "inwhile " + sInd + " " + eInd );
			}
			//
			if( list.get( sInd ).compareTo( obj ) > 0 ){
				list.add( sInd, obj );
				return true;
			}else if( list.get( eInd ).compareTo( obj ) > 0 ){
				list.add( eInd, obj );
				return true;
			}else{
				list.add( eInd + 1, obj );
				return true;
			}
		}
	}

	private boolean removeI( E obj ) {
		if( list.size() == 0 ){
			return false;
		}else if( list.size() == 1 ){
			if( list.get( 0 ).compareTo( obj ) == 0 ){
				list.remove( 0 );
				return true;
			}else return false;
		}else{
			int sInd= 0;
			int eInd= list.size() - 1;
			//
			while( ( eInd - sInd ) != 1 ){
				if( ( eInd - sInd ) % 2 == 0 ){
					if( list.get( ( eInd - sInd ) / 2 + sInd ).compareTo( obj ) > 0 ){
						eInd= ( eInd - sInd ) / 2 + sInd;
					}else if( list.get( ( eInd - sInd ) / 2 + sInd ).compareTo( obj ) == 0 ){
						list.remove( list.get( ( eInd - sInd ) / 2 + sInd ) );
						return true;
					}else{
						sInd= ( eInd - sInd ) / 2 + sInd;
					}
				}else{
					if( list.get( eInd ).compareTo( obj ) > 0 ){
						eInd-- ;
					}else if( list.get( eInd ).compareTo( obj ) == 0 ){
						list.remove( eInd );
						return true;
					}else if( list.get( eInd ).compareTo( obj ) < 0 ){
						return false;
					}
				}
				//System.out.println( "inwhile " + sInd + " " + eInd );
			}
			//
			if( list.get( eInd ).compareTo( obj ) == 0 ){
				list.remove( eInd );
				return true;
			}
			if( list.get( sInd ).compareTo( obj ) == 0 ){
				list.remove( sInd );
				return true;
			}
			return false;
		}
	}

	// a test for the valid and correctness of the Barray.
	public static void main( String[] arg ) {
		BArray <String> a= new BArray <>();
		ArrayList <String> b= new ArrayList <>();
		//
		int totalLines= 900000;
		String tmp;
		System.out.println( "starting." );
		for( int i= 0; i < totalLines; i++ ){
			tmp= helper.rand40AN().substring( 0, 5 );
			b.add( tmp );
		}
		long bs= helper.getTimeLong();
		Collections.sort( b );
		System.out.println( "b sort completed (" + ( helper.getTimeLong() - bs ) + ")ms" );
		//
		long as= helper.getTimeLong();
		a.addAll( b );
		System.out.println( "a sort completed (" + ( helper.getTimeLong() - as ) + ")ms" );
		//
		for( int i= 0; i < totalLines; i++ ){
			if( a.get( i ).compareTo( b.get( i ) ) != 0 )
				System.out.println( "error: " + ( i + 1 ) + " - " + a.get( i ).compareTo( b.get( i ) ) );
		}
		System.out.println( "all done. completed." );
	}
}
