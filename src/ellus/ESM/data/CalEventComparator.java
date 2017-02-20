package ellus.ESM.data;

import java.util.ArrayList;



public class CalEventComparator {
	public static void sortA( ArrayList <CalendarEvent> inp ) {
		long max= 0;
		int maxI= 0;
		CalendarEvent tmp;
		for( int e= 0; e < inp.size(); e++ ){
			max= Long.MIN_VALUE;
			maxI= 0;
			for( int i= 0; i < inp.size() - e; i++ ){
				if( inp.get( i ).getSecLeft() > max ){
					max= inp.get( i ).getSecLeft();
					maxI= i;
				}
			}
			tmp= inp.get( maxI );
			inp.remove( maxI );
			inp.add( tmp );
		}
	}

	public static void sortD( ArrayList <CalendarEvent> inp ) {
		long max= 0;
		int maxI= 0;
		CalendarEvent tmp;
		for( int e= 0; e < inp.size(); e++ ){
			max= Long.MIN_VALUE;
			maxI= 0;
			for( int i= e; i < inp.size(); i++ ){
				if( inp.get( i ).getSecLeft() > max ){
					max= inp.get( i ).getSecLeft();
					maxI= i;
				}
			}
			tmp= inp.get( maxI );
			inp.remove( maxI );
			inp.add( 0, tmp );
		}
	}
}
