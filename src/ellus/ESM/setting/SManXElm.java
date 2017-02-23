package ellus.ESM.setting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr.AttrType;



//type + name combination must be unique in the same level.
public class SManXElm implements Comparable <SManXElm> {
	private final String				ID							= helper.getCurrentTimeStamp()
			+ helper.rand32AN().substring( 0, 5 );
	private int							ind							= 0;
	private int							nestedLevel					= 0;
	private ArrayList <SManXElm>		childs						= new ArrayList <>();
	private ArrayList <SManXAttr>		attributes					= new ArrayList <>();
	private static final String			whileSpace					= "                                                                                            ";
	private ArrayList <AbleSMXConfig>	pin							= new ArrayList <>();
	//
	protected String					type						= null;
	protected String					name						= null;
	//
	public static final int				standardIndentationPerTab	= 4;
	public static final String			empty						= "                                                    ";

	/*||----------------------------------------------------------------------------------------------
	 ||| get attribute of this elm. if not exists, create empty one and return.
	||||--------------------------------------------------------------------------------------------*/
	public SManXAttr getAttr( AttrType type, String name ) {
		for( SManXAttr atr : attributes ){
			if( atr.name.equals( name ) && atr.type == type )
				return atr;
		}
		SManXAttr ret= new SManXAttr( type, name );
		attributes.add( ret );
		return ret;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get direct child elm of this elm. if not exists, create empty one and return.
	||||--------------------------------------------------------------------------------------------*/
	public SManXElm getElm( String type, String name ) {
		for( SManXElm el : childs ){
			if( el.name.equals( name ) && el.type.equals( type ) )
				return el;
		}
		SManXElm ret= new SManXElm( type, name, nestedLevel + 1 );
		childs.add( ret );
		return ret;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get name
	||||--------------------------------------------------------------------------------------------*/
	public String getName() {
		return this.name;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get type.
	||||--------------------------------------------------------------------------------------------*/
	public String getType() {
		return this.type;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all the elements.
	||||--------------------------------------------------------------------------------------------*/
	public ArrayList <SManXElm> getElmAll() {
		return (ArrayList <SManXElm>)childs.clone();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all the attributes.
	||||--------------------------------------------------------------------------------------------*/
	public ArrayList <SManXAttr> getAttrAll() {
		return (ArrayList <SManXAttr>)attributes.clone();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| set the pin.
	||||--------------------------------------------------------------------------------------------*/
	public void setPin( AbleSMXConfig pin ) {
		this.pin.add( pin );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| flush all the change for the pin.
	||||--------------------------------------------------------------------------------------------*/
	public void flushChange() {
		if( pin == null || pin.size() == 0 )
			return;
		for( AbleSMXConfig p : pin ){
			if( p != null )
				p.reset();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| print cont and id.
	||||--------------------------------------------------------------------------------------------*/
	public void printSpec() {
		int i= 1;
		display.println( this.getClass().toString(), this.getType() + " " + this.getName() + " " +
				ID + " got--------------------------------" );
		for( SManXAttr attribute : attributes ){
			display.println( this.getClass().toString(), "Attr Ind:" + ( i++ ) + "-" + attribute.writeAttriID() );
		}
		display.println( this.getClass().toString(), this.getType() + " " + this.getName() + " " +
				ID + " end--------------------------------" );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create a element.
	||||--------------------------------------------------------------------------------------------*/
	protected SManXElm( ArrayList <String> lines, int level ) {
		//
		this.ind= lines.get( 0 ).indexOf( "<" );
		loadAttri( lines.get( 0 ) );
		lines.remove( 0 );
		this.nestedLevel= level;
		//
		ArrayList <String> elm= null;
		boolean inflag= false;
		for( String tmp : lines ){
			if( tmp.charAt( ind + standardIndentationPerTab ) == '<' ){
				if( !inflag ){
					elm= new ArrayList <>();
					inflag= true;
				}else{
					childs.add( new SManXElm( elm, level + 1 ) );
					inflag= false;
				}
			}
			if( inflag ){
				elm.add( tmp );
			}
		}
		Collections.sort( childs );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create a element.
	||||--------------------------------------------------------------------------------------------*/
	protected SManXElm( String type2, String name2, int ind2 ) {
		this.name= name2.replace( ' ', '_' ).replace( '\t', '_' );
		this.type= type2.replace( ' ', '_' ).replace( '\t', '_' );
		this.nestedLevel= ind2;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all the content of this and its child.
	||||--------------------------------------------------------------------------------------------*/
	protected ArrayList <String> writeAllLines() {
		ArrayList <String> ret= new ArrayList <>();
		//
		String attr= whileSpace.substring( 0, nestedLevel * standardIndentationPerTab )
				+ "<" + this.type + " Name= " + this.name + " ";
		for( SManXAttr attribute : attributes ){
			attr+= attribute.writeAttri();
		}
		attr+= ">";
		ret.add( attr );
		for( SManXElm child : childs ){
			ret.addAll( child.writeAllLines() );
		}
		ret.add( whileSpace.substring( 0, nestedLevel * standardIndentationPerTab ) + "</" + this.type + ">" );
		return ret;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| load the type and name and all attributes of this element.
	||||--------------------------------------------------------------------------------------------*/
	private void loadAttri( String inp ) {
		Scanner rdr= new Scanner( inp.substring( ind + 1, inp.length() ) );
		this.type= rdr.next();
		rdr.next();
		this.name= rdr.next();
		//
		SManXAttr tAtr;
		String n, t, v, tok;
		while( rdr.hasNext() ){
			tok= rdr.next();
			if( tok.equals( ">" ) )
				break;
			t= rdr.next();
			rdr.next();
			n= rdr.next();
			rdr.next();
			v= rdr.next();
			tAtr= new SManXAttr( t, n, v );
			///
			attributes.add( tAtr );
		}
		Collections.sort( attributes );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| for sorting.
	||||--------------------------------------------------------------------------------------------*/
	@Override
	public int compareTo( SManXElm o ) {
		return this.name.compareTo( o.name );
	}
}