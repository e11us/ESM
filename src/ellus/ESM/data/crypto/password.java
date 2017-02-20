package ellus.ESM.data.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SCon;



/*
 * usage example.
 *
 password pw= new password();
 pw.newPW("es_mail", "acc1", null, null, null );	// create pw.
 pw.store( GCSV.folderPWM, "masterkey1" );			// store it on disk.
 pw.get( GCSV.folderPWM, "es_mail", "masterkey1" );	// retrive from disk.
 */
public class password {
	//
	private String	path	= null;
	//
	// must have.
	//
	// key word for this PW.
	private String	nameTag	= "";
	// username.
	private String	user	= "";
	//
	// auto generation.
	private String	key		= null;
	private String	date	= "$";
	//
	// additional info.
	private String	weburl	= "$";
	private String	discpt	= "$";

	public password() {}

	public password( String path ) {
		this.path= path;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create new pw. ( currently not used )
	||||--------------------------------------------------------------------------------------------*/
	public void newPW( String inp ) {
		if( inp == null )
			return;
		Scanner rdr= new Scanner( inp );
		if( rdr.hasNextLine() ){
			nameTag= helper.getValidName( rdr.nextLine().replace( ' ', '_' ) );
		}
		if( rdr.hasNextLine() ){
			user= helper.getValidName( rdr.nextLine().replace( ' ', '_' ) );
		}
		if( rdr.hasNextLine() ){
			key= rdr.nextLine().replace( ' ', '_' );
		}
		if( rdr.hasNextLine() ){
			weburl= rdr.nextLine().replace( ' ', '_' );
		}
		discpt= "";
		while( rdr.hasNextLine() ){
			discpt+= rdr.nextLine() + "\n";
		}
		if( weburl.length() == 0 ){
			weburl= "$";
		}
		if( key == null || key.length() == 0 ){
			key= helper.rand32AN();
		}
		if( discpt.length() == 0 ){
			discpt= "$";
		}
		date= helper.getCurrentTimeStamp();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| reset the pw. delete existing one.
	||||--------------------------------------------------------------------------------------------*/
	public boolean resetAll( String inp ) {
		String origName= nameTag;
		try{
			if( inp == null )
				return false;
			//
			Scanner rdr= new Scanner( inp );
			String tmp;
			if( rdr.hasNextLine() ){
				tmp= rdr.nextLine();
				tmp= tmp.substring( tmp.indexOf( ':' ) + 1, tmp.length() );
				while( tmp.charAt( 0 ) == ' ' )
					tmp= tmp.substring( 1, tmp.length() );
				nameTag= helper.getValidName( tmp.replace( ' ', '_' ) );
			}
			if( rdr.hasNextLine() ){
				tmp= rdr.nextLine();
				tmp= tmp.substring( tmp.indexOf( ':' ) + 1, tmp.length() );
				while( tmp.charAt( 0 ) == ' ' )
					tmp= tmp.substring( 1, tmp.length() );
				user= tmp.replace( ' ', '_' );
			}
			if( rdr.hasNextLine() ){
				tmp= rdr.nextLine();
				tmp= tmp.substring( tmp.indexOf( ':' ) + 1, tmp.length() );
				while( tmp.charAt( 0 ) == ' ' )
					tmp= tmp.substring( 1, tmp.length() );
				key= tmp.replace( ' ', '_' );
			}
			if( rdr.hasNextLine() ){
				tmp= rdr.nextLine();
				tmp= tmp.substring( tmp.indexOf( ':' ) + 1, tmp.length() );
				while( tmp.charAt( 0 ) == ' ' )
					tmp= tmp.substring( 1, tmp.length() );
				weburl= tmp.replace( ' ', '_' );
			}
			discpt= "";
			while( rdr.hasNextLine() ){
				if( discpt.length() == 0 )
					discpt= rdr.nextLine();
				else discpt+= "\n" + rdr.nextLine();
			}
			if( weburl.length() == 0 ){
				weburl= "$";
			}
			if( key == null || key.length() == 0 || key.toLowerCase().equals( "null" ) ){
				key= helper.rand32AN();
			}
			if( discpt.length() == 0 ){
				discpt= "$";
			}
			date= helper.getCurrentTimeStamp();
		}catch ( Exception ee ){
			ee.printStackTrace();
			display.printErr( this.getClass().toString(), "error reseting the password: " + nameTag );
			return false;
		}
		//
		File ext= new File( path + "/" + origName + SCon.ExtPWM );
		if( origName != null )
			ext.delete();
		//
		return true;
	}

	public void setName( String inp ) {
		if( inp != null && helper.getValidName( inp ) != null ){
			this.nameTag= helper.getValidName( inp );
		}
	}

	public boolean store( String key ) {
		if( path == null )
			return false;
		return store( path, key );
	}

	public boolean store( String path, String key ) {
		// check
		if( nameTag == null || user == null || key == null )
			return false;
		//display.println( "___________" + this.toString() + " " + key + "___________" );
		// make sure path exists and unique.
		File spath= new File( path );
		if( !spath.exists() )
			spath.mkdir();
		if( helper.getValidName( nameTag ) == null ){
			display.println( ( (Object)this ).getClass().toString(), " || bad name." );
			return false;
		}
		File pw= new File( path + "/" + helper.getValidName( nameTag )
				+ SCon.ExtPWM );
		if( pw.exists() ){
			display.println( ( (Object)this ).getClass().toString(), " || PW exists, please try diff name." );
			return false;
		}
		// get all info.
		String info= getPedding();
		info+= " @#$%^&* " + toString() + " @#$%^&* ";
		info+= getPedding();
		info= crypter.encryption( key, info );
		// store.
		FileOutputStream fos;
		ObjectOutputStream oos;
		try{
			fos= new FileOutputStream( pw );
			oos= new ObjectOutputStream( fos );
			oos.writeObject( info );
			oos.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
			return false;
		}catch ( IOException e ){
			e.printStackTrace();
			return false;
		}
		erase();
		display.println( ( (Object)this ).getClass().toString(), " || PW is stored." );
		return true;
	}

	public String get( String path, String name, String key ) {
		if( getPW( path, name, key ) ){
			this.path= path;
			return this.toStringN();
		}
		return null;
	}

	public String getI( String path, String name, String key ) {
		if( getPW( path, name, key ) ){
			return this.toStringI();
		}
		return null;
	}

	private boolean getPW( String path, String name, String key ) {
		try{
			File spath= new File( path + "/" + name + SCon.ExtPWM );
			//System.out.println( path + "/" + name + SCon.ExtPWM + " " + key);
			if( !spath.exists() || !spath.isFile() )
				return false;
			FileInputStream fileIn;
			ObjectInputStream in;
			String tmp;
			try{
				fileIn= new FileInputStream( spath );
				in= new ObjectInputStream( fileIn );
				tmp= (String)in.readObject();
				in.close();
				fileIn.close();
			}catch ( IOException ie ){
				ie.printStackTrace();
				return false;
			}catch ( ClassNotFoundException ce ){
				ce.printStackTrace();
				return false;
			}
			tmp= crypter.decryption( key, tmp );
			Scanner rdr= new Scanner( tmp );
			while( rdr.hasNext() ){
				if( rdr.next().equals( "@#$%^&*" ) ){
					rdr.next();
					this.nameTag= rdr.next();
					rdr.next();
					this.date= rdr.next();
					rdr.next();
					this.user= rdr.next();
					rdr.next();
					this.key= rdr.next();
					rdr.next();
					this.weburl= rdr.next();
					rdr.next();
					this.discpt= "";
					String tmpSS;
					while( rdr.hasNext() ){
						tmpSS= rdr.next();
						if( tmpSS.equals( "@#$%^&*" ) )
							break;
						discpt+= tmpSS + " ";
					}
					discpt= discpt.replace( '~', '\n' );
					display.println( ( (Object)this ).getClass().toString(), " || PW is retrieved." );
					return true;
				}
			}
			display.println( ( (Object)this ).getClass().toString(), " || PW is not retrieved." );
			return false;
		}catch ( Exception ee ){
			ee.printStackTrace();
			return false;
		}
	}

	public void erase() {
		nameTag= user= key= date= weburl= discpt= helper.rand32AN();
		nameTag= user= key= date= weburl= discpt= null;
	}

	@Override
	public String toString() {
		return new String() + " Name: " + nameTag + " Date_created: " + date
				+ " user: " + user + " key: " + key + " url: " + weburl
				+ " discription: " + discpt.replace( '\n', '~' );
	}

	public String toStringN() {
		return new String() + "\nName: " + nameTag + "\nDate_created: " + date
				+ "\nuser: " + user + "\nkey: " + key + "\nurl: " + weburl
				+ "\n----------discription----------\n" + discpt + " ";
	}

	public ArrayList <String> toStringAL() {
		ArrayList <String> ret= new ArrayList <>();
		ret.add( "Date_created: " + date );
		ret.add( "\n" );
		ret.add( "Name: " + nameTag );
		ret.add( "user: " + user );
		ret.add( "key: " + key );
		ret.add( "url: " + weburl );
		ret.add( "\n" );
		ret.add( "\n----------discription----------\n" );
		String tmp= new String( discpt );
		while( tmp.contains( "\n" ) ){
			ret.add( ">> " + tmp.substring( 0, tmp.indexOf( '\n' ) ) );
			if( tmp.indexOf( '\n' ) != tmp.length() - 1 )
				tmp= tmp.substring( tmp.indexOf( '\n' ) + 1, tmp.length() );
			else{
				tmp= "";
				break;
			}
		}
		if( tmp.length() > 0 )
			ret.add( ">> " + tmp );
		return ret;
	}

	public ArrayList <String> toStringALwd() {
		ArrayList <String> ret= new ArrayList <>();
		ret.add( "Name: " + nameTag );
		ret.add( "user: " + user );
		ret.add( "key: " + key );
		ret.add( "url: " + weburl );
		String tmp= new String( discpt );
		while( tmp.contains( "\n" ) ){
			ret.add( tmp.substring( 0, tmp.indexOf( '\n' ) ) );
			if( tmp.indexOf( '\n' ) != tmp.length() - 1 )
				tmp= tmp.substring( tmp.indexOf( '\n' ) + 1, tmp.length() );
			else{
				tmp= "";
				break;
			}
		}
		if( tmp.length() > 0 )
			ret.add( tmp );
		return ret;
		// + "\n----------discription----------\n" + discpt + " ";
	}

	public String toStringI() {
		return new String() + nameTag + "\n" + user + "\n" + key + "\n"
				+ weburl + "\n" + discpt + " ";
	}

	public String getPW() {
		return key;
	}

	private String getPedding() {
		ArrayList <String> pedding= new ArrayList <>();
		int itor= (int) ( Math.random() * 10 + 100 );
		for( int i= 0; i < itor; i++ ){
			pedding.add( helper.randAN( ( (int)Math.random() * 50 ) + 50 ) + " " );
		}
		String res= "";
		for( String tmp : pedding ){
			res+= tmp;
		}
		return res;
		/*
		int itor= (int) ( Math.random() * 10 + 100 );
		char[] pedding= new char[ itor ];
		for( int i= 0; i < itor; i++ ){
			pedding[i]= helper.randAN( ( (int)Math.random() * 50 ) + 50 ).charAt( 0 );
			pedding+= " ";
		}
		return pedding;
		*/
	}
}
