package ellus.ESM.ESMP;

import java.util.ArrayList;
import java.util.Timer;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.crypto.password;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonScrollWT;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundMatx;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.pinnable.panel.PanelMultiLineInput;
import ellus.ESM.pinnable.panel.PanelTextFieldPin;
import ellus.ESM.pinnable.panel.PanelTextRead;
import ellus.ESM.pinnable.panel.PanelContainer;
import ellus.ESM.pinnable.panel.PanelDisplay;
import ellus.ESM.roboSys.keyboard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class PasswordManager extends ESMPanel implements AbleSMXConfig{
	private SManXElm		showPWconfig= null;
	private ButtonScrollWT	pgs			= null;
	private ButtonScrollWT	ags			= null;
	private ButtonTextFS	newPS		= null;
	private int				curAction	= 0;

	private enum action {
		Delay_Type, Display_PW, Edit_PW
	};

	// only allow one instance.
	private static PasswordManager pw= null;
	
	// get the instance.
	public static PasswordManager getInstance(  SManXElm config  ) {
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
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border"), PS, "PasswordManager" );
		super.bgPL.add( 2, titlePin );
		//
		showPWconfig.setPin( this );
	}
	
	private void constructShowPS() {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( createShowPW( showPWconfig ) );
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
		String folder= helper.getDirSubFolder( SMan.getSetting( 0 )
				+ SMan.getSetting( 5 ) ).get( pgs.getIndex() );
		ArrayList <String> pws= helper.getDirFile( folder, SCon.ExtPWM2 );
		ArrayList <pinnable> pwsP= new ArrayList <>();
		for( String path : pws ){
			pwsP.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "PasswordButton" ),
					helper.getFileName( helper.getFilePathName( path ) ) ) {
				@Override
				public void B1clickAction( int x, int y ) {
					password pwI= new password();
					pwI.get( folder, helper.getFileName( helper.getFilePathName( path ) ), "master1" );
					//
					//
					switch( curAction ){
						case 0 :
							try{
								// sleep 3.3 second for delay type password.
								Timer tt= new Timer( "password_typeDelay Timer" );
								tt.schedule( new keyboard( pwI.getPW() ), 3300 );
							}catch ( Exception e ){}
							break;
						case 1 :
							// txtRead replace display, use the same config.
							PanelTextRead dsp= new PanelTextRead( config.getElm( "PanelDisplay", "PasswordShowPanel" ),
							//PanelDisplay dsp= new PanelDisplay( config.getElm( "PanelDisplay", "PasswordShowPanel" ),
									pwI.toStringAL() );
							pl.add( 5, dsp );
							highlighted= dsp;
							break;
						case 2 :
							PanelTextFieldPin editPan= new PanelTextFieldPin(
									config.getElm( "ButtonInputMultiLine", "PasswordEditPanel" ),
									pwI.toStringALwd() ) {
							//PanelMultiLineInput editPan= new PanelMultiLineInput(
							//		config.getElm( "ButtonInputMultiLine", "PasswordEditPanel" ),pwI.toStringALwd() ) {
								@Override
								public void B3clickAction( int x, int y ) {
									this.removeMe= true;
									//
									// reset still need fix.
									//pwI.resetAll( this.getTxt() );
								}
							};
							pl.add( 5, editPan );
							highlighted= editPan;
							break;
						default :
							break;
					}
				}
			} );
		}
		//
		PanelContainer pwp= new PanelContainer( config.getElm( "PanelContainer", "PasswordGroupShow" ),
				xS + config.getAttr( AttrType._location, "PasswordGroupContainer" ).getLocation().getX(),
				yS + config.getAttr( AttrType._location, "PasswordGroupContainer" ).getLocation().getY(),
				pwsP );
		pl.add( 4, pwp );
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
}
