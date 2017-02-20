package ellus.ESM.setting;

import java.awt.Color;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;



public class SManXAttr implements Comparable <SManXAttr>{
	//
	public enum AttrType {
		_string, _int, _double, _color, _location, _boolean
	};

	/*||----------------------------------------------------------------------------------------------
	 ||| get name
	||||--------------------------------------------------------------------------------------------*/
	public String getName() {
		return this.name;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get type.
	||||--------------------------------------------------------------------------------------------*/
	public AttrType getType() {
		return this.type;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| validate a setting before set.
	||||--------------------------------------------------------------------------------------------*/
	public void setVal( String inp ) {
		switch( type ){
			case _string :
				this.val= inp;
			case _int :
				try{
					this.val= Integer.parseInt( inp ) + "";
				}catch ( Exception ee ){}
				break;
			case _double :
				try{
					this.val= Double.parseDouble( inp ) + "";
				}catch ( Exception ee ){}
				break;
			case _color :
				java.awt.Color t= helper.getColor( inp );
				if( t != null )
					this.val= helper.color2Str( t );
				break;
			case _location :
				cor2D c= helper.getLocation( inp );
				if( c != null )
					this.val= c.toString();
				break;
			case _boolean :
				try{
					this.val= Boolean.parseBoolean( inp ) + "";
				}catch ( Exception ee ){}
				break;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| return val, if calling the right func. val will be returned, otherwise, null or 0.
	||||--------------------------------------------------------------------------------------------*/
	public String getString() {
		if( this.type == AttrType._string )
			return this.val;
		return null;
	}

	public int getInteger() {
		if( this.type == AttrType._int )
			return Integer.parseInt( this.val );
		return 0;
	}

	public double getDouble() {
		if( this.type == AttrType._double )
			return Double.parseDouble( this.val );
		return 0.0;
	}

	public Color getColor() {
		if( this.type == AttrType._color )
			return helper.getColor( this.val );
		return null;
	}

	public cor2D getLocation() {
		if( this.type == AttrType._location )
			return helper.getLocation( this.val );
		return null;
	}

	public boolean getBoolean() {
		if( this.type == AttrType._boolean ){
			return Boolean.parseBoolean( this.val );
		}
		return false;
	}

	public String getVal() {
		return this.val;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create a attribute. ( given value )
	||||--------------------------------------------------------------------------------------------*/
	protected SManXAttr( String type, String name, String val ) {
		this.name= name.replace( ' ', '_' ).replace( '\t', '_' );
		this.val= val;
		switch( type ){
			case "string" :
				this.type= AttrType._string;
				break;
			case "int" :
				this.type= AttrType._int;
				break;
			case "double" :
				this.type= AttrType._double;
				break;
			case "color" :
				this.type= AttrType._color;
				break;
			case "location" :
				this.type= AttrType._location;
				break;
			case "boolean" :
				this.type= AttrType._boolean;
				break;
			default :
				this.type= AttrType._string;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create a attribute. ( empty )
	||||--------------------------------------------------------------------------------------------*/
	protected SManXAttr( AttrType type, String name ) {
		this.type= type;
		this.name= name.replace( ' ', '_' ).replace( '\t', '_' );
		switch( type ){
			case _string :
				// def string should be empty. otherwise cause problem.
				this.val= "-";
				break;
			case _int :
				this.val= "0";
				break;
			case _double :
				this.val= "0.0";
				break;
			case _color :
				this.val= "(0,0,0)";
				break;
			case _location :
				this.val= "(0,0)";
				break;
			case _boolean :
				this.val= "false";
				break;
		}
	}
	/*||----------------------------------------------------------------------------------------------
	 ||| get type as string.
	||||--------------------------------------------------------------------------------------------*/

	/*||----------------------------------------------------------------------------------------------
	 ||| return attribute as a line.
	||||--------------------------------------------------------------------------------------------*/
	protected String writeAttri() {
		switch( type ){
			case _string :
				return "type= string name= " + this.name + " val= " + this.val + " ";
			case _int :
				return "type= int name= " + this.name + " val= " + this.val + " ";
			case _double :
				return "type= double name= " + this.name + " val= " + this.val + " ";
			case _color :
				return "type= color name= " + this.name + " val= " + this.val + " ";
			case _location :
				return "type= location name= " + this.name + " val= " + this.val + " ";
			case _boolean :
				return "type= boolean name= " + this.name + " val= " + this.val + " ";
		}
		return null;
	}

	protected String writeAttriID() {
		switch( type ){
			case _string :
				return "ID: " + ID + " val= " + this.val + " ";
			case _int :
				return "ID: " + ID + " val= " + this.val + " ";
			case _double :
				return "ID: " + ID + " val= " + this.val + " ";
			case _color :
				return "ID: " + ID + " val= " + this.val + " ";
			case _location :
				return "ID: " + ID + " val= " + this.val + " ";
			case _boolean :
				return "ID: " + ID + " val= " + this.val + " ";
		}
		return null;
	}

	protected AttrType		type= AttrType._string;
	protected String		name= null;
	protected String		val	= null;
	protected final String	ID	= helper.getCurrentTimeStamp() + helper.rand32AN().substring( 0, 5 );
	
	@Override
	public int compareTo( SManXAttr o ) {
		return this.type.toString().compareTo( o.type.toString() );
	}
}
