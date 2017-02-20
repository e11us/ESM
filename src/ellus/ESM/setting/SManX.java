package ellus.ESM.setting;

import java.util.ArrayList;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;



//type + name combination must be unique in the same level.
public class SManX {
	// is inited.
	private static boolean				inited	= false;
	// a list of root elements.
	private static ArrayList <SManXElm>	rootElm;

	/*||----------------------------------------------------------------------------------------------
	 ||| get element on root level. if not exists, create empty one and return.
	||||--------------------------------------------------------------------------------------------*/
	public static SManXElm get( String type, String name ) {
		if( !inited )
			init();
		//
		for( SManXElm el : rootElm ){
			if( el.name.equals( name ) && el.type.equals( type ) ){
				return el;
			}
		}
		SManXElm ret= new SManXElm( type, name, 0 );
		rootElm.add( ret );
		return ret;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| exit, write back all setting.
	||||---------------------------------------------------------------------------------------------*/
	public static void exit() {
		writeElm();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| first initial
	||||--------------------------------------------------------------------------------------------*/
	protected static void init() {
		helper.createFile( SCon.PathXMLsetting );
		rootElm= new ArrayList <>();
		loadElm();
		inited= true;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| load file.
	||||--------------------------------------------------------------------------------------------*/
	private static void loadElm() {
		ArrayList <String> lines= helper.readFile( SCon.PathXMLsetting );
		ArrayList <String> elm= null;
		SManXElm newE;
		int ind= 1;
		boolean inflag= false;
		for( String tmp : lines ){
			if( tmp.length() == 0 )
				continue;
			if( tmp.charAt( 0 ) == '<' ){
				if( !inflag ){
					elm= new ArrayList <>();
					inflag= true;
				}else{
					newE= new SManXElm( elm, 0 );
					rootElm.add( newE );
					inflag= false;
				}
			}
			if( inflag ){
				elm.add( tmp );
			}
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| write all back to file.
	||||--------------------------------------------------------------------------------------------*/
	private static void writeElm() {
		ArrayList <String> out= new ArrayList <>();
		for( SManXElm el : rootElm ){
			out.addAll( el.writeAllLines() );
		}
		helper.writeFile( SCon.PathXMLsetting, true, out );
		display.println( "setting.SManX", "all setting is wrote to the disk" );
	}
}
