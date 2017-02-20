package ellus.ESM.data;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.setting.SCon;



public class WebLinkAddress {
	private String		name	= null;
	private String		addr	= null;
	private String[]	tags	= null;
	private String		previewS= null;
	private String		previewL= null;

	public WebLinkAddress( String n, String add, String[] tag, String ps, String pl, String path ) {
		this.name= n;
		this.addr= add;
		this.tags= tag;
		this.previewS= ps;
		this.previewL= pl;
		store( path );
	}

	public WebLinkAddress( String name, String path ) {
		this.name= name;
		load( path );
	}

	public String getLink() {
		return addr;
	}

	public String[] getTags() {
		return tags;
	}

	public String getTagsAsStr() {
		if( tags != null && tags.length > 0 ){
			String ret= "";
			for( String tmp : tags ){
				ret+= tmp + " ";
			}
			return ret;
		}
		return null;
	}

	private void load( String path ) {
		try{
			File ff= new File( path + "/" + name + SCon.ExtWebLink );
			Scanner rdr= new Scanner( ff );
			//
			this.addr= rdr.nextLine();
			if( rdr.hasNextLine() )
				this.previewS= rdr.nextLine();
			else return;
			//
			if( rdr.hasNextLine() )
				this.previewL= rdr.nextLine();
			else return;
			//
			ArrayList <String> tag= new ArrayList <>();
			while( rdr.hasNextLine() ){
				tag.add( rdr.nextLine() );
			}
			if( tag.size() > 0 ){
				int i= 0;
				tags= new String[tag.size()];
				for( String tmp : tag ){
					tags[i++ ]= tmp;
				}
			}
		}catch ( Exception ee ){
			ee.printStackTrace();
			display.printErr( this.getClass().toString(), "Error loading a weblink." );
		}
	}

	private void store( String path ) {
		try{
			File st= new File( path + "/" + name + SCon.ExtWebLink );
			if( st.exists() && st.isFile() ){
				display.printErr( this.getClass().toString(), "Error creating the new weblink, it already exists." );
				return;
			}
			PrintWriter pw= new PrintWriter( st );
			pw.println( addr );
			pw.println( previewS );
			pw.println( previewL );
			if( tags != null ){
				for( String tmp : tags ){
					pw.println( tmp );
				}
			}
			pw.close();
		}catch ( Exception ee ){
			ee.printStackTrace();
			display.printErr( this.getClass().toString(), "Error creating the new weblink" );
		}
		display.println( this.getClass().toString(), "weblink: " + name + " is created" );
	}
}
