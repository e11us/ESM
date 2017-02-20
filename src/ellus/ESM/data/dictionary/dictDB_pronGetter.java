package ellus.ESM.data.dictionary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.helper;
import ellus.ESM.WebInteract.DL;
import ellus.ESM.setting.SMan;



public class dictDB_pronGetter {
	public dictDB_pronGetter( File def ) {
		if( def == null || !def.exists() || !def.isFile() )
			return;
		Scanner rdr, rdr2;
		ArrayList <String> links= new ArrayList <>();
		//
		try{
			rdr= new Scanner( def );
			String line;
			String regi;
			String link;
			while( rdr.hasNextLine() ){
				line= rdr.nextLine();
				if( line.charAt( 0 ) != 'r' )
					continue;
				rdr2= new Scanner( line );
				// consume first tag.
				if( rdr2.hasNext() )
					rdr2.next();
				else continue;
				if( rdr2.hasNext() )
					regi= rdr2.next();
				else continue;
				if( line.indexOf( "http://" ) != -1 ){
					line= regi + " " + line.substring( line.indexOf( "http://" ), line.length() );
					if( !links.contains( line ) ){
						links.add( line );
					}
				}
				rdr2.close();
			}
			rdr.close();
			//
			if( links.size() > 0 ){
				for( int i= 0; i < links.size(); i++ ){
					line= links.get( i );
					rdr= new Scanner( line );
					regi= helper.getFileName( def.getName() ) + "-" + ( i + 1 ) + "- " + rdr.next() + ".mp3";
					link= rdr.next();
					new DL( SMan.getSetting( 0 ) + SMan.getSetting( 201 ),
							regi, link, false, true );
				}
			}else{
				File bad= new File(
						SMan.getSetting( 0 ) + SMan.getSetting( 203 ) + "/" + def.getName() );
				bad.createNewFile();
			}
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}
}
