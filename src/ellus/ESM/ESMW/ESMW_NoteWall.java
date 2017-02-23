package ellus.ESM.ESMW;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.Note.NoteText;
import ellus.ESM.pinnable.SS.PanelBackgroundDotCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundDotGrid;
import ellus.ESM.pinnable.SS.PanelBackgroundRLetter;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundStripCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundStripSnake;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS2;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ESMW_NoteWall extends ESMW {
	private ESMW		handler			= this;
	private ESMPanel	noteWallPanel	= null;
	private ESMPL		noteWallbg		= null;
	private ESMPL		noteWall		= null;
	private SManXElm	SE				= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_NoteWall_SManX_Name );
	//
	private String		path;
	private ESMW		parent;
	private cor2D		initView;
	private SMan		SM;

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor of class
	||||--------------------------------------------------------------------------------------------*/
	public ESMW_NoteWall( ESMW parent, String path ) {
		try{
			this.path= path;
			this.parent= parent;
			//
			// get setting for this notewall. ( get all setting newly each time when call construct.
			SM= new SMan( path );
			if( SM.getSettingInd( 0 ) == null ){
				// if setting not exitst, create new form default. !!!!!!!!!! delete bad file if needed !!!!!!!!!!!
				File badsetting= new File( path + "/" + SCon.ExtSetting );
				if( badsetting.exists() && badsetting.isFile() )
					badsetting.delete();
				File defsetting= new File( SMan.getSetting( 0 ) + SMan.getSetting( 401 ) );
				try{
					Files.copy( defsetting.toPath(), new File( path + "/" + SCon.ExtSetting ).toPath() );
				}catch ( IOException e ){
					e.printStackTrace();
				}
				SM= new SMan( path );
			}
			// set it to frame bg.
			setUpNoteWallPanel();
			setUpNoteWall();
			setUpNWBG();
			super.bgPanel= noteWallPanel;
			//
			noteWallPanel.PS.keyboardNavSpeed= Integer.parseInt( SM.getSettingInd( 3 ) );
			noteWallPanel.PS.mouseWheelNavSpeed= Integer.parseInt( SM.getSettingInd( 3 ) );
			initView= helper.getLocation( SM.getSettingInd( 0 ) );
			if( initView != null ){
				noteWallPanel.PS.setViewCenter( initView.getX(), initView.getY() );
			}
			noteWallPanel.PS.mousewheelNavEnable= true;
			noteWallPanel.PS.keyboardNavEnable= true;
			noteWallPanel.PS.keyboardNavSpeed= Integer.parseInt( SM.getSettingInd( 3 ) );
			// Frame SetUp.
			super.init( SE.getElm( "ESMW", "NoteWallFrame" ) );
			// bring it to front.
			frame.toFront();
		}catch ( Exception ee ){
			parent.maxFrame();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void construct() {}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void setUpNoteWallPanel() {
		Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
		noteWallPanel= new ESMPanel( screenSize.width, screenSize.height ) {
			@Override
			protected synchronized void KeyboardInpNGE( String lk ) {
				// always clear menu.
				noteWallPanel.menuPL= null;
				//
				if( lk.length() == 1 ){
					switch( lk.charAt( 0 ) ){
						case 27 :
							parent.maxFrame();
							this.closePanel();
							handler.closeFrame();
					}
				}
			}
		};
		noteWallPanel.masterSE= SE;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void setUpNoteWall() {
		ESMPS PS= noteWallPanel.PS;
		SManXElm config= SE.getElm( "ESMPL", "notewall" );
		SManXElm ntc= config.getElm( "NoteTextConfig", "HomeNote_NoteConfig" );
		noteWall= new ESMPL();
		//
		for( String tmp : helper.getAllFile( path, SCon.Extpinnable2 ) ){
			//
			// ntc
			// this config is shared by all notewall. its just a temp holder.
			// real config comes from SM.
			ntc.getAttr( AttrType._int, "TextXOS" ).setVal( SM.getSettingInd( 13 ) );
			ntc.getAttr( AttrType._int, "TextYOS" ).setVal( SM.getSettingInd( 14 ) );
			ntc.getAttr( AttrType._int, "TextLineSeperation" ).setVal( SM.getSettingInd( 15 ) );
			ntc.getAttr( AttrType._int, "FontIndex" ).setVal( 23 + "" );
			ntc.getAttr( AttrType._int, "FontSize" ).setVal( 20 + "" );
			noteWall.add( 1, new NoteText( ntc, tmp ) );
		}
		/*
		// add all note link.
		for( String tmp : helper.getAllFile( path, SCon.ExtpinLink2 ) ){
			noteWall.add( 2,  new NoteLink( tmp, noteWall.getAll( 1 ) ) );
		}
		
			// add all note image.
			for( String tmp : helper.getAllFile( path, SCon.ExtpinImg2 ) ){
				pinListLayered.get( 2 ).add( new NoteImage( tmp ) );
			}
		
		
		 */
		//
		noteWallPanel.Subpls.removeAll( noteWallPanel.Subpls );
		noteWallPanel.Subpls.add( noteWall );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void setUpNWBG() {
		ESMPS PS= noteWallPanel.PS;
		SManXElm config= SE.getElm( "ESMPL", "notewallBG" );
		noteWallbg= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					noteWallbg.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					noteWallbg.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					noteWallbg.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					noteWallbg.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					noteWallbg.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundTitleFS2" :
					noteWallbg.add( 6, new PanelBackgroundTitleFS2( elm, PS, "NoteWall Selector" ) );
			}
		}
		noteWallbg.add( 0, new PanelBackgroundSC( helper.getColor( SM.getSettingInd( 1 ) ),
				helper.getColor( SM.getSettingInd( 2 ) ) ) );
		noteWallPanel.bgPL= noteWallbg;
	}

	public void loadAll( String a ) {
		return;
	}
}
/*
 *
 * 	if( highlighted == null ){
						ArrayList <String> f1panTxt= new ArrayList <>();
						f1panTxt.add( "F1 help menu----------------------------------------------" );
						f1panTxt.add( "" );
						f1panTxt.add( "01. right mouse on empty space for menu." );
						f1panTxt.add( "" );
						f1panTxt.add( "02. left mouse on item to highlight." );
						f1panTxt.add( "" );
						f1panTxt.add( "03. double left mouse on item to open link." );
						f1panTxt.add( "" );
						f1panTxt.add( "04. right mouse for drag item." );
						f1panTxt.add( "" );
						f1panTxt.add( "05. double right mouse for setting a file link." );
						f1panTxt.add( "" );
						f1panTxt.add( "06. highlight a item, and ctrl+v to paste text inside." );
						f1panTxt.add( "" );
						f1panTxt.add(
								"07. if pasted text is a web link, it will set web link of the highlighted item." );
						f1panTxt.add( "" );
						f1panTxt.add(
								"08. when new item link is selected. click on the first item, then the second." );
						f1panTxt.add( "" );
						f1panTxt.add( "09. F2 to delete the highlighted item." );
						f1panTxt.add( "" );
						f1panTxt.add( "10. F5 to refresh the folders." );
						Color[] f1panC= {
								helper.getColor( SMan.getSetting( 1012 ) ),
								helper.getColor( SMan.getSetting( 1013 ) ),
								helper.getColor( SMan.getSetting( 1014 ) ),
								helper.getColor( SMan.getSetting( 1015 ) )
						};
						//display.println( helper.getColor( SMan.getSetting( 1012 ) ).toString() );
						PanelDisplay help= new PanelDisplay(
								Integer.parseInt( SMan.getSetting( 1010 ) ),
								Integer.parseInt( SMan.getSetting( 1011 ) ),
								Integer.parseInt( SMan.getSetting( 2250 ) ),
								f1panC, f1panTxt );
						super.pinListLayered.get( 4 ).add( help );
						highlighted= help;
					}


					// color for note text.
		Color[] NTC= {
				helper.getColor( SM.getSettingInd( 101 ) ),
				helper.getColor( SM.getSettingInd( 102 ) ),
				helper.getColor( SM.getSettingInd( 103 ) ),
				helper.getColor( SM.getSettingInd( 104 ) ),
				helper.getColor( SM.getSettingInd( 105 ) ),
				helper.getColor( SM.getSettingInd( 106 ) )
		};
		// add all note text.
		for( String tmp : helper.getAllFile( path, SCon.Extpinnable2 ) ){
			pinListLayered.get( 2 ).add( new NoteText(
					Integer.parseInt( SM.getSettingInd( 11 ) ),
					Integer.parseInt( SM.getSettingInd( 12 ) ),
					Integer.parseInt( SM.getSettingInd( 13 ) ),
					Integer.parseInt( SM.getSettingInd( 14 ) ),
					Integer.parseInt( SM.getSettingInd( 15 ) ),
					NTC, paintBoard.getGraphics(), tmp ) );


					*/
