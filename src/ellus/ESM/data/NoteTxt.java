package ellus.ESM.data;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.sys.UseLogger;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class NoteTxt {
	private Color		bg1C, bg2C, txC, edC, bg1CH, bg2CH, edCH, txCH, dgC, inpCurC= Color.red;
	private String		filePath	= null;
	private Object[]	para;
	private String		parentFolder= null;

	public NoteTxt( String filePath, Object[] inp ) {
		this.filePath= filePath;
		this.parentFolder= ( new File( filePath ) ).getParent();
		this.para= inp;
		if( new File( filePath ).exists() )
			readFromDisk();
		else{
			para[3]= new ArrayList <>();// set def msg.
			( (ArrayList <String>)para[3] ).add( "@#_NewNote_@#" );
			helper.writeFile( filePath, false, ( (ArrayList <String>)para[3] ) );
			readFromDisk();
		}
	}

	public void update( SManXElm elm ) {
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextMsgColor" ).getColor();
		bg1CH= elm.getAttr( AttrType._color, "BackgroundColorHL1" ).getColor();
		bg2CH= elm.getAttr( AttrType._color, "BackgroundColorHL2" ).getColor();
		edCH= elm.getAttr( AttrType._color, "EdgeColorHL" ).getColor();
		txCH= elm.getAttr( AttrType._color, "TextMsgColorHL" ).getColor();
		dgC= elm.getAttr( AttrType._color, "DragBgColor" ).getColor();
		inpCurC= elm.getAttr( AttrType._color, "InputCursorColor" ).getColor();
		para[5]= bg1C;
		para[6]= bg2C;
		para[7]= edC;
		para[8]= txC;
		para[9]= bg1CH;
		para[10]= bg2CH;
		para[11]= edCH;
		para[12]= txCH;
		para[13]= dgC;
		para[14]= inpCurC;
	}

	public String getFileFolderPath() {
		return filePath;
	}

	public void storeNewChange() {
		f.f( "store" );
		// delete previous. & log it.
		delete();
		// update filepath.
		filePath= parentFolder + "/" + (String)para[0] + " " +
				(int)para[1] + " " + (int)para[2] + " " + SCon.Extpinnable;
		// write new.
		if( para[4] != null ){
			ArrayList <String> write= new ArrayList <>();
			write.addAll( (ArrayList <String>)para[3] );
			write.add( 0, ( (char)SCon.noteTxtWebLinkUrlSignal ) + (String)para[4] );
			helper.writeFile( filePath, true, write );
		}else helper.writeFile( filePath, true, ( (ArrayList <String>)para[3] ) );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| load a Note Text from the disk: file path of the note, and graphic for measure text note size
	||||--------------------------------------------------------------------------------------------*/
	private void readFromDisk() {
		File path= new File( filePath );
		if( path.isFile() ){
			try{
				Scanner rdr= new Scanner( path.getName() );
				para[0]= rdr.next();
				para[1]= rdr.nextInt();
				para[2]= rdr.nextInt();
				// display.println( "["+ msg.size() + "] " + super.getLocation().toString() );
				rdr.close();
				para[3]= new ArrayList <>();
				rdr= new Scanner( path );
				if( rdr.hasNextLine() ){
					String tmp= rdr.nextLine();
					// check first line for url link.
					if( tmp.length() > 0 && ( tmp.charAt( 0 ) ) == SCon.noteTxtWebLinkUrlSignal )
						para[4]= tmp.substring( 1, tmp.length() );
					else ( (ArrayList <String>)para[3] ).add( tmp );
				}
				while( rdr.hasNextLine() ){
					( (ArrayList <String>)para[3] ).add( rdr.nextLine() );
				}
				rdr.close();
				// reset bg.
			}catch ( FileNotFoundException e ){
				e.printStackTrace();
			}
		}
	}

	public void delete() {
		File path= new File( filePath );
		if( path.exists() && path.isFile() ){
			ArrayList <String> prev= helper.readFile( filePath );
			prev.add( 0, "Deleted NoteText atPath: " + filePath + " atTime: " + helper.getCurrentDateStamp() );
			UseLogger.log( prev );
			path.delete();
		}
	}
}
