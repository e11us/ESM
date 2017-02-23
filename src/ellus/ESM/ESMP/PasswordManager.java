package ellus.ESM.ESMP;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import java.util.Timer;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.data.SQL.sqlResult;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.pinnable.Button.ButtonScrollWT;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundMatx;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelContainerScroll;
import ellus.ESM.pinnable.panel.PanelTextFieldPin;
import ellus.ESM.pinnable.panel.PanelTextRead;
import ellus.ESM.roboSys.keyboard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PasswordManager extends ESMPanel implements AbleSMXConfig {
	private PasswordManager				handler		= this;
	private SManXElm					showPWconfig= null;
	private ButtonScrollWT				pgs			= null;
	private ButtonScrollWT				ags			= null;
	private ButtonTextFS				newPS		= null;
	private PanelContainerScroll		pwp			= null;
	private int							curAction	= 0;
	private static ArrayList <String>	sqlName;
	private static final String			sqlfuncName	= "passwordManager";
	private static final String			startSig	= "@#$%^&*()*&^%$#@!";
	//
	private static final String			key			= SMan.getSetting( 2020 );

	private enum action {
		Delay_Type, Display_PW, Edit_PW
	};

	// only allow one instance.
	private static PasswordManager pw= null;

	// get the instance.
	public static PasswordManager getInstance( SManXElm config ) {
		if( pw != null )
			return null;
		pw= new PasswordManager( config );
		return pw;
	}

	PasswordManager( SManXElm config ) {
		super( config );
		this.masterSE= config;
		this.showPWconfig= config.getElm( "ESMPL", "showPassword" );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "BackgroundColor" ), PS ) );
		super.bgPL.add( 0, new PanelBackgroundMatx( config.getElm( "PanelBackgroundMatx", "bgMatrix" ), PS ) );
		//
		constructShowPS();
		//
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "PasswordManager" );
		super.bgPL.add( 2, titlePin );
		//
		showPWconfig.setPin( this );
	}

	private void constructShowPS() {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( createShowPW( showPWconfig ) );
	}

	private void showPSI( ESMPL sw ) {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( sw );
	}

	private ESMPL createShowPW( SManXElm config ) {
		ESMPL pl= new ESMPL();
		//
		int xS= PS.ViewOriginX();
		int yS= PS.ViewOriginY();
		//
		if( pgs == null ){
			ArrayList <String> cato= helper
					.getDirSubFolder( SMan.getSetting( 0 ) + SMan.getSetting( 5 ) );
			pgs= new ButtonScrollWT( config.getElm( "ButtonScroll", "PasswordGroupScroll" ),
					cato.size(), "Current Group: " ) {
				@Override
				public void WheelRotateAction( int rot ) {
					super.WheelRotateAction( rot );
					constructShowPS();
				}
			};
		}
		pl.add( 4, pgs );
		//
		if( ags == null ){
			ags= new ButtonScrollWT( config.getElm( "ButtonScroll", "PasswordActionScroll" ),
					action.values().length, "Action: " + action.values()[curAction].toString() ) {
				@Override
				public void WheelRotateAction( int rot ) {
					super.WheelRotateAction( rot );
					super.setMsg( "Action: " + action.values()[super.getIndex()].toString() );
					curAction= this.getIndex();
				}
			};
		}
		pl.add( 4, ags );
		//
		if( newPS == null ){
			newPS= new ButtonTextFS( config.getElm( "ButtonTextFS", "NewPasswordButton" ),
					"New Password" ) {
				@Override
				public void B1clickAction( int x, int y ) {
					//
				}
			};
			newPS.setXY( xS + config.getAttr( AttrType._location, "NewPasswordButton" ).getLocation().getX(),
					xS + config.getAttr( AttrType._location, "NewPasswordButton" ).getLocation().getX() +
							newPS.getWidth(),
					yS + config.getAttr( AttrType._location, "NewPasswordButton" ).getLocation().getY(),
					yS + config.getAttr( AttrType._location, "NewPasswordButton" ).getLocation().getY() +
							newPS.getHeight() );
		}
		pl.add( 4, newPS );
		//
		ArrayList <pinnable> pwsP= new ArrayList <>();
		pwsP.add( new ButtonTextFS( showPWconfig.getElm( "ButtonTextFS", "PasswordButton" ),
				"loading All Passwords..." ) );
		if( pwp == null )
			pwp= new PanelContainerScroll( config.getElm( "PanelContainer", "PasswordGroupShow" ),
					xS + config.getAttr( AttrType._location, "PasswordGroupContainer" ).getLocation().getX(),
					yS + config.getAttr( AttrType._location, "PasswordGroupContainer" ).getLocation().getY(),
					pwsP );
		pl.add( 4, pwp );
		//
		new pwloader();
		return pl;
	}

	@Override
	protected void closePanel() {
		super.closePanel();
		pw= null;
	}

	@Override
	public void reset() {
		pgs= null;
		ags= null;
		newPS= null;
		constructShowPS();
	}

	private class pwgetter extends Thread {
		PanelTextRead	ptr;
		String			pw;

		public pwgetter( String pw, PanelTextRead ptr ) {
			this.ptr= ptr;
			this.pw= pw;
			this.start();
		}

		public void run() {
			ptr.setTxt( getPW( pw, key ) );
		}
	}

	private class pwtyper extends Thread {
		String	pw;
		String	key;

		public pwtyper( String pw, String key ) {
			this.pw= pw;
			this.key= key;
			this.start();
		}

		public void run() {
			String res= getPW( pw, key );
			Scanner rdr= new Scanner( res );
			while( rdr.hasNext() ){
				if( rdr.next().toLowerCase().equals( "key:" ) ){
					String resKey= rdr.next();
					rdr.close();
					try{
						// sleep around 3 second for delay type password.
						this.sleep( 2780 );
						keyboard.type( resKey );
					}catch ( Exception e ){
						e.printStackTrace();
					}
					return;
				}
			}
		}
	}

	private class pwloader extends Thread {
		public pwloader() {
			this.start();
		}

		public void run() {
			ArrayList <pinnable> pwsP= new ArrayList <>();
			SManXElm config= showPWconfig;
			//
			for( String pw : getAllPwName() ){
				pwsP.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "PasswordButton" ), pw ) {
					@Override
					public void B1clickAction( int x, int y ) {
						switch( curAction ){
							case 0 :
								new pwtyper( pw, key );
								break;
							case 1 :
								ESMPL pws= new ESMPL();
								PanelTextRead dsp= new PanelTextRead(
										config.getElm( "PanelTextRead", "PasswordShowPanel" ),
										helper.str2ALstr( "Loading password for: " + pw ) ) {
									@Override
									public void B3clickAction( int x, int y ) {
										this.removeMe= true;
										constructShowPS();
									}
								};
								new pwgetter( pw, dsp );
								pws.add( 1, dsp );
								highlighted= dsp;
								showPSI( pws );
								break;
							case 2 :
								PanelTextFieldPin editPan= new PanelTextFieldPin(
										config.getElm( "PanelTextFieldPin", "PasswordEditPanel" ),
										helper.str2ALstr( getPW( pw, key ) ) );
								highlighted= editPan;
								break;
							default :
								break;
						}
					}
				} );
			}
			pwp.resetCont( pwsP );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get a pw from the db.
	||||--------------------------------------------------------------------------------------------*/
	private static String getPW( String name, String key ) {
		ArrayList <String> sqlresname= new ArrayList <>();
		ArrayList <String> sqlresval= new ArrayList <>();
		sqlresname.add( "keyword" );
		sqlresval.add( name );
		//
		ArrayList <sqlResult> res= mySQLportal.getByFunc( sqlfuncName, 1, sqlresname, sqlresval );
		if( res.size() > 0 ){
			return removePedding( decrypt( key, res.get( 0 ).val.get( 4 ).toString() ) );
		}
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| get all pw name from db.
	||||--------------------------------------------------------------------------------------------*/
	private static ArrayList <String> getAllPwName() {
		ArrayList <sqlResult> res= mySQLportal.getByFunc( sqlfuncName, -1, null, null );
		ArrayList <String> names= new ArrayList <String>();
		if( res.size() > 0 ){
			for( sqlResult resI : res )
				names.add( resI.val.get( 3 ).toString() );
			return names;
		}
		return null;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| insert a local file into the sql db with the key. ( name is filename without path & .txt )
	||||--------------------------------------------------------------------------------------------*/
	private static void insertPW( String path, String key ) {
		String ps= helper.ALstr2str( helper.readFile( path ) );
		if( ps == null )
			return;
		//
		if( sqlName == null ){
			sqlName= new ArrayList <>();
			sqlName.add( "functional" );
			sqlName.add( "keyword" );
			sqlName.add( "content" );
		}
		//
		ArrayList <String> sqlVal= new ArrayList <>();
		sqlVal.add( sqlfuncName );
		sqlVal.add( helper.getFileName( helper.getFilePathName( path ) ) );
		sqlVal.add( encrypt( key, addPedding( ps ) ) );
		mySQLportal.insert( sqlName, sqlVal );
	}

	private static String encrypt( String pwK, String value ) {
		if( pwK == null || pwK.length() != 32 || value == null || value.length() == 0 )
			return null;
		String key= pwK.substring( 0, 16 );
		String initVector= pwK.substring( 16, 32 );
		try{
			IvParameterSpec iv= new IvParameterSpec( initVector.getBytes( "UTF-8" ) );
			SecretKeySpec skeySpec= new SecretKeySpec( key.getBytes( "UTF-8" ), "AES" );
			Cipher cipher= Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
			cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv );
			byte[] enc= cipher.doFinal( value.getBytes() );
			String ret= Base64.getEncoder().encodeToString( enc );
			return ret;
		}catch ( Exception ex ){
			ex.printStackTrace();
		}
		return null;
	}

	private static String addPedding( String inp ) {
		StringBuilder str= new StringBuilder( helper.randAN( (int) ( 3000 * Math.random() + 1000 ) ) );
		str.append( " " + startSig + " " + inp + " " + startSig + " " );
		str.append( helper.randAN( (int) ( 3000 * Math.random() + 1000 ) ) );
		return str.toString();
	}

	private static String removePedding( String inp ) {
		int ind= inp.indexOf( startSig );
		if( ind == -1 )
			return null;
		inp= inp.substring( ind + startSig.length() + 1, inp.length() );
		ind= inp.indexOf( startSig );
		if( ind == -1 )
			return null;
		return inp.substring( 0, ind - 1 );
	}

	private static String decrypt( String pwK, String encrypted ) {
		if( pwK == null || pwK.length() != 32 || encrypted == null || encrypted.length() == 0 )
			return null;
		String key= pwK.substring( 0, 16 );
		String initVector= pwK.substring( 16, 32 );
		try{
			IvParameterSpec iv= new IvParameterSpec( initVector.getBytes( "UTF-8" ) );
			SecretKeySpec skeySpec= new SecretKeySpec( key.getBytes( "UTF-8" ), "AES" );
			Cipher cipher= Cipher.getInstance( "AES/CBC/PKCS5PADDING" );
			cipher.init( Cipher.DECRYPT_MODE, skeySpec, iv );
			String org= new String( cipher.doFinal( Base64.getDecoder().decode( encrypted ) ) );
			return org;
		}catch ( Exception ex ){
			ex.printStackTrace();
		}
		return null;
	}
}
