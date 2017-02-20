package ellus.ESM.data.HTML;

import java.util.ArrayList;
import java.util.Scanner;



public class bookLine {
	//
	// for text lines.
	protected String	cont= null;
	protected String	link= null;

	public ArrayList <String> getAllWords() {
		ArrayList <String> res= new ArrayList <>();
		if( cont != null ){
			String contT= cont;
			Scanner rdr= new Scanner( contT );
			while( rdr.hasNext() ){
				res.add( rdr.next() );
			}
		}
		return res;
	}

	//
	// for image.
	protected String	imgPath	= null;
	protected String	imgLink	= null;

	public String getImgPath() {
		return imgPath;
	}
}
