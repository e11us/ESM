package ellus.ESM.ESMW;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import ellus.ESM.ESMP.Calendar;
import ellus.ESM.ESMP.FileSys;
import ellus.ESM.ESMP.NoteEntry;
import ellus.ESM.ESMP.PasswordManager;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.Source.SourceDouble;
import ellus.ESM.data.dictionary.dictDB;
import ellus.ESM.data.sys.BackupEr;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonInputFS;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.ButtonAni.ButtonCircle;
import ellus.ESM.pinnable.LF.PaneRect;
import ellus.ESM.pinnable.LF.SideShadeAutoHide;
import ellus.ESM.pinnable.Note.NoteText;
import ellus.ESM.pinnable.SS.PanelBackgroundCoreCirc;
import ellus.ESM.pinnable.SS.PanelBackgroundCoreRaid;
import ellus.ESM.pinnable.SS.PanelBackgroundDotCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundDotGrid;
import ellus.ESM.pinnable.SS.PanelBackgroundPicSuff;
import ellus.ESM.pinnable.SS.PanelBackgroundRLetter;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundStripCenterFlow;
import ellus.ESM.pinnable.SS.PanelBackgroundStripSnake;
import ellus.ESM.pinnable.SS.PanelCupInfo;
import ellus.ESM.pinnable.SS.PanelDateTimeInfo;
import ellus.ESM.pinnable.SS.PanelFpsInfo;
import ellus.ESM.pinnable.SS.PanelMemInfo;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr.AttrType;
import notUsed.ESMP.NoteWall;
import notUsed.button.ButtonNew;
import notUsed.panel.PanelMenu;
import ellus.ESM.setting.SManXElm;



public class ESMW_Home extends ESMW {
	// homePanel for bg and PL for use.
	private ESMW_Home		handler			= this;
	private ESMPanel		homePanel		= null;
	private ESMPL			homeRegular		= null;
	private ESMPL			homeNote		= null;
	private ESMPL			homeBgNote		= null;
	private ESMPL			homeBgRegular	= null;
	private ESMPL			homeBgLink		= null;
	private ESMPL			homeLink		= null;
	private ESMPL			homeBgNWSB		= null;
	private ESMPL			homeNWSB		= null;
	//
	// setting config.
	private SManXElm		calSE		= SManX.get( "ESM", "_the_ESM_" )
		.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "Calendar" );
	private SManXElm		fileSysSE		= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "FileSystem" );
	private SManXElm		pswordSE		= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "PasswordManager" );
	private SManXElm 		notEntSE =  SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name ).getElm( "ESMPanel", "NoteEntry" );
	private SManXElm		SE				= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_Home_SManX_Name );
	private HomeOption		HO				= HomeOption.regular;	
	// fps panel for all bg.
	private PanelFpsInfo	fpsPan			= null;

	private enum HomeOption {
		regular, note, link, noteWallSelect
	};

	public ESMW_Home() {
		// setup fps.
		fpsPan= setupFpsMA( SE );
		// homePanel SetUp.
		homePanel= createESMPanelHome();
		homeBgNote= SetHomePanelBGNote( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Note" ) );
		homeBgRegular= SetHomePanelBGReg( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Regular" ) );
		homeBgLink= SetHomePanelBGLink( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "Link" ) );
		homeRegular= setHomePanelRegu( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeRegular" ) );
		homeNote= setHomePanelNote( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" ) );
		homeLink= setHomePanelLink( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeLink" ) );
		//
		homeBgNWSB= SetHomePanelBGLink( homePanel.PS,
				SE.getElm( "ESMPanel", "HomeBgPanel" ).getElm( "ESMPL", "NoteWallSB" ) );
		homeNWSB= setHomePanelNWSB( homePanel.PS,
				SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNoteWallSB" ) );
		//
		homePanel.bgPL= homeBgRegular;
		homePanel.Subpls.add( homeRegular );
		homePanel.masterSE= SE;
		//  Frame SetUp. (set it to frame bg. )
		super.bgPanel= homePanel;
		super.init( SE.getElm( "ESMW", "HomeFrame" ) );
		//super.frame.setAlwaysOnTop( true );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPanel createESMPanelHome() {
		//( home panel always starts in full screen. & create setting package. )
		Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
		ESMPanel pp= new ESMPanel( screenSize.width, screenSize.height ) {
			/*
			 *
			 *
			 *careful of adding multiple listenser to ESMPanel.
			 *double load,
			 *
			 *
			 */
			@Override
			protected synchronized void KeyboardInpNGE( String lk ) {
				// always clear menu.
				homePanel.menuPL= null;
				//
				if( lk.length() == 1 ){
					switch( lk.charAt( 0 ) ){
						case 27 :// esc
							if( HO == HomeOption.regular ){
								handler.miniFrame();
								this.menuPL= null;
								break;
							}else if( HO == HomeOption.note ){
								HO= HomeOption.regular;
								this.Subpls.remove( homeNote );
								this.Subpls.add( homeRegular );
								this.bgPL= homeBgRegular;
								handler.showAllSubPanel();
								break;
							}else if( HO == HomeOption.link ){
								HO= HomeOption.regular;
								this.Subpls.remove( homeLink );
								this.Subpls.add( homeRegular );
								this.bgPL= homeBgRegular;
								handler.showAllSubPanel();
								break;
							}
						case 10 :// enter.
							break;
						default :
							break;
					}
				}else if( lk.length() == 2 ){
					switch( lk ){
						case "F8" :
							new ESMW_SMXConfig( masterSE );
							break;
					}
				}
			}

			@Override
			protected synchronized void CheckB3ClickNGE( MouseEvent e ) {
				// create a new right click panel, and add it.
				menuPL= ( getRightClickMenu( homePanel.PS, SE.getElm( "ESMPanel", "HomePanel" ).getElm(
						"Setting", "HomePanel_RightClickMenu" ) ) );
			}
		};
		return pp;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGNote( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGLink( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL SetHomePanelBGReg( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		for( SManXElm elm : config.getElmAll() ){
			switch( elm.getType() ){
				case "PanelBackgroundDotCenterFlow" :
					bgPL.add( 3, new PanelBackgroundDotCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundDotGrid" :
					bgPL.add( 1, new PanelBackgroundDotGrid( elm, PS ) );
					break;
				case "PanelBackgroundStripCenterFlow" :
					bgPL.add( 2, new PanelBackgroundStripCenterFlow( elm, PS ) );
					break;
				case "PanelBackgroundRLetter" :
					bgPL.add( 4, new PanelBackgroundRLetter( elm ) );
					break;
				case "PanelBackgroundStripSnake" :
					bgPL.add( 2, new PanelBackgroundStripSnake( elm, PS ) );
					break;
				case "PanelBackgroundSC" :
					bgPL.add( 0, new PanelBackgroundSC( elm, PS ) );
					break;
			}
		}
		//
		ArrayList <String> imgPath= helper.getDirFile( SMan.getSetting( 0 )
				+ SMan.getSetting( 106 ) + SMan.getSetting( 2110 ), "jpg" );
		Image[] img= new Image[imgPath.size()];
		for( int i= 0; i < imgPath.size(); i++ ){
			img[i]= helper.getImage( imgPath.get( i ) );
		}
		bgPL.add( 0, new PanelBackgroundPicSuff( img, 5 ) );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelRegu( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		SManXElm SE= config;
		//
		// the 3 option pan circle.button.
		ArrayList <pinnable> homeCB= new ArrayList <>();
		// show DeskTopLink
		ButtonCircle dtnotes= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopLink" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( HO != HomeOption.link ){
					HO= HomeOption.link;
					homePanel.Subpls.remove( homeRegular );
					homePanel.Subpls.add( homeLink );
					homePanel.bgPL= homeBgLink;
					handler.hideAllSubPanel();
				}
			}
		};
		homeCB.add( dtnotes );
		// show desktop note.
		ButtonCircle dtnotes1= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopNote" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( HO != HomeOption.note ){
					HO= HomeOption.note;
					homePanel.Subpls.remove( homeRegular );
					homePanel.Subpls.add( homeNote );
					homePanel.bgPL= homeBgNote;
					handler.hideAllSubPanel();
				}
			}
		};
		homeCB.add( dtnotes1 );
		// show console output.
		ButtonCircle dtnotes3= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopNoteWallSB" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( HO != HomeOption.noteWallSelect ){
					HO= HomeOption.noteWallSelect;
					
				}
			}
		};
		homeCB.add( dtnotes3 );
		// exit ESM
		ButtonCircle dtnotes4= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopExit" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				closeFrame();
				exitESM();
			}
		};
		homeCB.add( dtnotes4 );
		//
		// right option for all the app.
		ArrayList <pinnable> homeCB2= new ArrayList <>();
		//
		ButtonCircle dtpw= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopPassWord" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				//
				PasswordManager pwm= PasswordManager.getInstance( pswordSE );
				if( pwm != null )
					handler.addPanel( pwm );
			}
		};
		homeCB2.add( dtpw );
		//
		ButtonCircle notEnt= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopNoteEntry" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				//
				NoteEntry net= NoteEntry.getInstance( notEntSE );
				if( net != null )
					handler.addPanel( net );
			}
		};
		homeCB2.add( notEnt );
		// file sys
		ButtonCircle dtnotes5= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopFileSys" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				//
				// file sys panl.
				FileSys fip= new FileSys( fileSysSE );
				handler.addPanel( fip );
			}
		};
		homeCB2.add( dtnotes5 );
		// file sys
		ButtonCircle bucal= new ButtonCircle( SE.getElm( "ButtonCircle", "DeskTopCalendar" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				//
				handler.addPanel( Calendar.getInstance( calSE ) );
			}
		};
		homeCB2.add( bucal );
		//
		// set up sideshade hide and expand.
		int homeSSYOS= SE.getAttr( AttrType._int, "HomeSideShadeYOS" ).getInteger();
		int homeSSHS= SE.getAttr( AttrType._int, "HomeSideShadeHideSize" ).getInteger();
		int homeSSES= SE.getAttr( AttrType._int, "HomeSideShadeExpandSize" ).getInteger();
		// left SS.
		Polygon polyLeft= new Polygon();
		polyLeft.addPoint( 0, homeSSYOS );
		polyLeft.addPoint( homeSSES, homeSSES + homeSSYOS );
		polyLeft.addPoint( homeSSES, PS.PanelH - homeSSES - homeSSYOS );
		polyLeft.addPoint( 0, PS.PanelH - homeSSYOS );
		Rectangle rectangleLeft= new Rectangle();
		rectangleLeft.x= -1;// add to -1 so that mouse most left still active
		rectangleLeft.y= homeSSYOS;
		rectangleLeft.width= homeSSHS;
		rectangleLeft.height= PS.PanelH - homeSSYOS * 2;
		//SManXElm inp, ESMPS PS, ArrayList<pinnable> cont, Polygon pg, Rectangle rg  ){
		SideShadeAutoHide homeLSS= new SideShadeAutoHide(
				SE.getElm( "SideShadeAutoHide", "SideShadeLeft" ), PS,
				homeCB, polyLeft, rectangleLeft, homeSSES );
		bgPL.add( 4, homeLSS );
		// right SS.
		Polygon polyRigth= new Polygon();
		polyRigth.addPoint( PS.PanelW, homeSSYOS );
		polyRigth.addPoint( PS.PanelW - homeSSES, homeSSES + homeSSYOS );
		polyRigth.addPoint( PS.PanelW - homeSSES, PS.PanelH - homeSSES - homeSSYOS );
		polyRigth.addPoint( PS.PanelW, PS.PanelH - homeSSYOS );
		Rectangle rectangleRight= new Rectangle();
		rectangleRight.x= PS.PanelW - homeSSHS;
		rectangleRight.y= homeSSYOS;
		rectangleRight.width= homeSSHS;
		rectangleRight.height= PS.PanelH - homeSSYOS * 2;
		//SManXElm inp, ESMPS PS, ArrayList<pinnable> cont, Polygon pg, Rectangle rg  ){
		SideShadeAutoHide homeRSS= new SideShadeAutoHide(
				SE.getElm( "SideShadeAutoHide", "SideShadeRight" ), PS,
				homeCB2, polyRigth, rectangleRight, homeSSES );
		bgPL.add( 4, homeRSS );
		//
		// add TFP
		bgPL.add( 4, new PanelCupInfo( SE.getElm( "PanelCupInfo", "TFP_CPUpanel" ) ) );
		bgPL.add( 4, new PanelDateTimeInfo( SE.getElm( "PanelDateTimeInfo", "TFP_DTpanel" ) ) );
		bgPL.add( 4, fpsPan );
		bgPL.add( 4, new PanelMemInfo( SE.getElm( "PanelMemInfo", "TFP_MEMpanel" ) ) );
		bgPL.add( 3, new PaneRect( SE.getElm( "PaneRect", "TFP_BgPanel" ) ) );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| FPS panel for all bg.
	||||--------------------------------------------------------------------------------------------*/
	private PanelFpsInfo setupFpsMA( SManXElm config ) {
		PanelFpsInfo pp= new PanelFpsInfo( config.getElm( "pinnable.Panel", "FPSpanel" ),
				new SourceDouble() {
					@Override
					public double getDouble() {
						return handler.getRenderWaitTime();
					}
				} );
		// also set for frame fps source.
		super.fpsSource= new SourceDouble() {
			@Override
			public double getDouble() {
				return pp.getFPS();
			}
		};
		return pp;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelNote( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		// add all note text.
		for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
				SMan.getSetting( 10 ), SCon.Extpinnable2 ) ){
			bgPL.add( 1, new NoteText( config.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ), tmp ) );
		}
		// exit to regular.
		ButtonCircle dtnotes4= new ButtonCircle( config.getElm( "ButtonCircle", "DeskTopRegular" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				// click highlight off if needed.
				if( homePanel.highlighted != null && homePanel.highlighted instanceof AbleClickHighlight )
					( (AbleClickHighlight)homePanel.highlighted ).B1clickHighlightOff( 0, 0 );
				// simulate, as if pressed esc.
				homePanel.KeyboardInpNGE( (char)27 + "" );
			}
		};
		bgPL.add( 4, fpsPan );
		bgPL.add( 4, dtnotes4 );
		return bgPL;
	}
	
	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelNWSB( ESMPS PS, SManXElm config ) {	//
		
		
		return null;
		/*
		// start layout postion of component.
		int xS= PS.ViewOrignX;
		int yS= PS.ViewOrignY;
		// set up colors.
		Color[] sideShadeColor= {
				helper.getColor( SMan.getSetting( 2210 ) ),
				helper.getColor( SMan.getSetting( 2211 ) ),
				helper.getColor( SMan.getSetting( 2212 ) )
		};
		Color[] NWButColor= {
				helper.getColor( SMan.getSetting( 2213 ) ),
				helper.getColor( SMan.getSetting( 2214 ) ),
				helper.getColor( SMan.getSetting( 2215 ) ),
				helper.getColor( SMan.getSetting( 2216 ) ),
				helper.getColor( SMan.getSetting( 2217 ) )
		};
		Color[] funButtColor= {
				helper.getColor( SMan.getSetting( 2223 ) ),
				helper.getColor( SMan.getSetting( 2224 ) ),
				helper.getColor( SMan.getSetting( 2225 ) ),
				helper.getColor( SMan.getSetting( 2226 ) ),
				helper.getColor( SMan.getSetting( 2227 ) )
		};
		Color[] NWButColorH= {
				helper.getColor( SMan.getSetting( 2218 ) ),
				helper.getColor( SMan.getSetting( 2219 ) ),
				helper.getColor( SMan.getSetting( 2220 ) ),
				helper.getColor( SMan.getSetting( 2221 ) ),
				helper.getColor( SMan.getSetting( 2222 ) )
		};
		// get button setup.
		int[] NWButton= { 0, 0,
				SCon.SB_NW_ButtonW, SCon.SB_NW_ButtonH,
				SCon.SB_NW_ButtonWOS, SCon.SB_NW_ButtonHOS };
		int fontInd= Integer.parseInt( SMan.getSetting( 2250 ) );
		int fontSize= Integer.parseInt( SMan.getSetting( 2251 ) );
		//
		// do the new cato button.
		NWButton[0]= xS + SCon.SB_NW_NewCatoButtonX;
		NWButton[1]= yS + SCon.SB_NW_NewCatoButtonY;
		ButtonNew newCato= new ButtonNew( NWButton, funButtColor, NWButColorH ) {
			@Override
			public void B1clickAction( MouseEvent inp ) {
				NWButton[2]= SCon.SB_NW_ButtonW * 3;
				ButtonInputFS name= new ButtonInputFS( NWButton, NWButColor, NWButColorH,
						helper.getFileName( helper.getFilePathName( "New Folder Name" ) ), 2,
						(int) ( fontSize * 2.2 ) );
				ButtonTextFS ok= new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						helper.getFileName( helper.getFilePathName( "confirm" ) ), 2, fontSize * 3 ) {
					@Override
					public void B1clickAction( int x, int y ) {
						File newF= new File( SMan.getSetting( 0 ) + SMan.getSetting( 1 ) + "/"
								+ name.getInputMsg() );
						newF.mkdirs();
						construct();
					}
				};
				ArrayList <pinnable> menuList= new ArrayList <>();
				menuList.add( name );
				menuList.add( ok );
				highlighted= new PanelMenu( menuList, 1, 25, 25, new Color( 20, 22, 22, 170 ) );
				pinListLayered.get( 4 ).add( highlighted );
			}
		};
		NWButton[0]+= SCon.SB_NW_ButtonW + SCon.SB_NW_ButtonGroupDisX;
		pinListLayered.get( 1 ).add( newCato );
		//
		// setup button again.
		NWButton[0]= xS + SCon.SB_NW_ButtonGroupX;
		NWButton[1]= yS + SCon.SB_NW_ButtonGroupY;
		NWButton[2]= SCon.SB_NW_ButtonW;
		// get all catergory.
		ArrayList <String> cato= helper
				.getDirSubFolder( SMan.getSetting( 0 ) + SMan.getSetting( 1 ) );
		if( cato != null ){
			// for each cato.
			for( String tmp : cato ){
				ButtonNew newsub= new ButtonNew( NWButton, funButtColor, NWButColorH ) {
					@Override
					// do the new board in the cato.
					public void B1clickAction( MouseEvent inp ) {
						NWButton[2]= SCon.SB_NW_ButtonW * 3;
						ButtonInputFS name= new ButtonInputFS( NWButton, NWButColor, NWButColorH,
								helper.getFileName( helper.getFilePathName( "New Board Name" ) ), 2,
								(int) ( fontSize * 2.2 ) );
						ButtonTextFS ok= new ButtonTextFS( NWButton, NWButColor, NWButColorH,
								helper.getFileName( helper.getFilePathName( "confirm" ) ), 2, fontSize * 3 ) {
							@Override
							public void B1clickAction( MouseEvent inp ) {
								File newF= new File( tmp + "/" + name.getInputMsg() );
								newF.mkdirs();
								construct();
							}
						};
						ArrayList <pinnable> menuList= new ArrayList <>();
						menuList.add( name );
						menuList.add( ok );
						highlighted= new PanelMenu( menuList, 1, 25, 25, new Color( 20, 22, 22, 170 ) );
						pinListLayered.get( 4 ).add( highlighted );
					}
				};
				pinListLayered.get( 1 ).add( newsub );
				NWButton[2]= (int) ( SCon.SB_NW_ButtonW * 3.5 );
				NWButton[3]= SCon.SB_NW_ButtonH;
				NWButton[0]+= SCon.SB_NW_ButtonGroupDisX + SCon.SB_NW_ButtonW;
				String name= "Group <" + ( new File( tmp ) ).getName() + ">";
				ButtonTextFS catoName= new ButtonTextFS( NWButton, funButtColor, NWButColorH, name, fontInd,
						fontSize * 2 ) {
					@Override
					public void B1clickAction( MouseEvent inp ) {
						try{
							DeskTop.getDeskTop().open( new File( tmp ) );
						}catch ( IOException e ){
							e.printStackTrace();
						}
					}
				};
				pinListLayered.get( 1 ).add( catoName );
				// reset.
				NWButton[2]= SCon.SB_NW_ButtonW;
				NWButton[3]= SCon.SB_NW_ButtonH;
				NWButton[0]= xS + SCon.SB_NW_ButtonGroupX + SCon.SB_NW_ButtonW
						+ SCon.SB_NW_ButtonGroupDisX;
				// get all sub board.
				ArrayList <String> catonb= helper.getDirSubFolder( tmp );
				if( catonb != null && catonb.size() > 0 ){
					// for each sub board.
					NWButton[1]+= SCon.SB_NW_ButtonGroupDisY + SCon.SB_NW_ButtonH;
					for( String tmp2 : catonb ){
						if( NWButton[0] + SCon.SB_NW_ButtonGroupTEX + SCon.SB_NW_ButtonW > xS + wid ){
							NWButton[0]= xS + SCon.SB_NW_ButtonGroupX + SCon.SB_NW_ButtonW
									+ SCon.SB_NW_ButtonGroupDisX;
							NWButton[1]+= SCon.SB_NW_ButtonGroupDisY + SCon.SB_NW_ButtonH;
						}
						ButtonTextFS catnbsub= new ButtonTextFS( NWButton, NWButColor, NWButColorH,
								helper.getFilePathName( tmp2 ), fontInd, fontSize ) {
							// get into sub board.
							@Override
							public void B1clickAction( MouseEvent inp ) {
								NoteWall nw1= new NoteWall( helper.getFilePathName( tmp2 ), tmp2, handler );
							}
						};
						NWButton[0]+= SCon.SB_NW_ButtonGroupDisX + SCon.SB_NW_ButtonW;
						pinListLayered.get( 1 ).add( catnbsub );
					}
				}
				// inc for each cato.
				NWButton[0]= xS + SCon.SB_NW_ButtonGroupX;
				NWButton[1]+= SCon.SB_NW_ButtonGroupDisY + SCon.SB_NW_ButtonH;
			}
		}*/
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL setHomePanelLink( ESMPS PS, SManXElm config ) {
		ESMPL bgPL= new ESMPL();
		// add all note text.
		for( String tmp : helper.getAllFile( SMan.getSetting( 0 ) +
				SMan.getSetting( 14 ), SCon.Extpinnable2 ) ){
			bgPL.add( 1, new NoteText( config.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ), tmp ) );
		}
		// exit to regular.
		ButtonCircle dtnotes4= new ButtonCircle( config.getElm( "ButtonCircle", "DeskTopRegular" ), PS ) {
			@Override
			public void B1clickAction( int x, int y ) {
				// click highlight off if needed.
				if( homePanel.highlighted != null && homePanel.highlighted instanceof AbleClickHighlight )
					( (AbleClickHighlight)homePanel.highlighted ).B1clickHighlightOff( 0, 0 );
				// simulate, as if pressed esc.
				homePanel.KeyboardInpNGE( (char)27 + "" );
			}
		};
		bgPL.add( 4, fpsPan );
		bgPL.add( 4, dtnotes4 );
		return bgPL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL getRightClickMenu( ESMPS PS, SManXElm config ) {
		ESMPL PL= new ESMPL();
		Color[] NWButColor= {
				config.getAttr( AttrType._color, "ButtonColor1" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor2" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor3" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor4" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColor5" ).getColor()
		};
		Color[] NWButColorH= {
				config.getAttr( AttrType._color, "ButtonColorH1" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH2" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH3" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH4" ).getColor(),
				config.getAttr( AttrType._color, "ButtonColorH5" ).getColor()
		};
		int[] NWButton= {
				PS.b2wX( PS.MouseLastPositionX ), PS.b2wY( PS.MouseLastPositionY ),
				config.getAttr( AttrType._int, "ButtonWidth" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonHeight" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonXOS" ).getInteger(),
				config.getAttr( AttrType._int, "ButtonYOS" ).getInteger(), };
		int sepa= config.getAttr( AttrType._int, "BoardItemSeperation" ).getInteger();
		int fontInd= config.getAttr( AttrType._int, "BoardFontInd" ).getInteger();
		int fontSize= config.getAttr( AttrType._int, "BoardFontSize" ).getInteger();
		switch( HO ){
			case regular :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"menu Test", fontInd, fontSize ) {} );
				break;
			case note :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new TextNote", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						// create a new blank note. and reload all home pins.
						NoteText newNote= new NoteText(
								SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" )
										.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ),
								SMan.getSetting( 0 ) + SMan.getSetting( 10 ) + "/ " + helper.getCurrentDateStamp() +
										helper.rand32AN().substring( 0, 5 ) + " " + x + " " + y + " ."
										+ SCon.Extpinnable2 );
						homeNote.add( 1, newNote );
					}
				} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"Align All to Grid", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						int xxx, yyy;
						int alignmentX= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentX" )
								.getInteger();
						int alignmentY= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentY" )
								.getInteger();
						for( pinnable pin : homeNote.getAll() ){
							xxx= pin.getXmin();
							yyy= pin.getYmin();
							if( pin.getXmin() % alignmentX > alignmentX / 2 )
								xxx+= -pin.getXmin() % alignmentX + alignmentX;
							else xxx-= pin.getXmin() % alignmentX;
							if( pin.getYmin() % alignmentY > alignmentY / 2 )
								yyy+= -pin.getYmin() % alignmentY + alignmentY;
							else yyy-= pin.getYmin() % alignmentY;
							pin.setXY( xxx, xxx + pin.getWidth(), yyy, yyy + pin.getHeight() );
							// store new location.
							if( pin instanceof NoteText )
								( (NoteText)pin ).B1clickHighlightOff( 0, 0 );
						}
					}
				} );
				break;
			case link :
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new LocalLink", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						// create a new blank note. and reload all home pins.
						NoteText newNote= new NoteText(
								SE.getElm( "ESMPanel", "HomePanel" ).getElm( "ESMPL", "homeNote" )
										.getElm( "NoteTextConfig", "HomeNote_NoteConfig" ),
								SMan.getSetting( 0 ) + SMan.getSetting( 14 ) + "/ " + helper.getCurrentDateStamp() +
										helper.rand32AN().substring( 0, 5 ) + " " + x + " " + y + " ."
										+ SCon.Extpinnable2 );
						// ask for a link first.
						newNote.B3clickActionSec( SMan.getSetting( 500 ) );
						//
						if( newNote.hasLink() ){
							homeLink.add( 1, newNote );
						}else newNote.FunInp( "F2" );// if can get one, erase this note.
						// close menu.
						homePanel.menuPL= null;
					}
				} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"new WebLink", fontInd, fontSize ) {} );
				NWButton[1]+= config.getAttr( AttrType._int, "ButtonHeight" ).getInteger() + 5;
				PL.add( 1, new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"Align All to Grid", fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						int xxx, yyy;
						int alignmentX= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentX" )
								.getInteger();
						int alignmentY= SE.getElm( "ESMPanel", "HomeBgPanel" ).getAttr( AttrType._int, "AlignmentY" )
								.getInteger();
						for( pinnable pin : homeLink.getAll() ){
							xxx= pin.getXmin();
							yyy= pin.getYmin();
							if( pin.getXmin() % alignmentX > alignmentX / 2 )
								xxx+= -pin.getXmin() % alignmentX + alignmentX;
							else xxx-= pin.getXmin() % alignmentX;
							if( pin.getYmin() % alignmentY > alignmentY / 2 )
								yyy+= -pin.getYmin() % alignmentY + alignmentY;
							else yyy-= pin.getYmin() % alignmentY;
							pin.setXY( xxx, xxx + pin.getWidth(), yyy, yyy + pin.getHeight() );
							// store new location.
							if( pin instanceof NoteText )
								( (NoteText)pin ).B1clickHighlightOff( 0, 0 );
						}
					}
				} );
				break;
		}
		return PL;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void exitESM() {
		//
		display.println( this.getClass().toString(), "ESM is getting ready to exit..." );
		//
		// start by exit all sub panel if needed.
		NoteEntry.close();
		//
		// close dict db update if in process.
		dictDB.updateDB_Stop();
		//
		// check if need backup.
		BackupEr.backupIfNeeded();
		//
		// close and write all the SManX setting.
		SManX.exit();
		// exit
		display.println( this.getClass().toString(), "ESM will exit now, Good Bye" );
		System.exit( 1 );
	}
}

/*
 * 
 * private void HomeConstructRegular() {
		//
		if( bgLayer12 == null ){
			// cirl#, cirl thic, cirl sep, int corSize // cirC
			int[] corRai= { 9, 5, 0, 26, 210, 0, 82, 1, 990, 500 };
			bgLayer12= new PanelBackgroundCoreRaid( corRai );
		}
		if( bgLayer13 == null ){
			// cirl#, cirl thic, cirl sep, cirl Size // cirC
			int[] corCir1= { 3, 3, 4, 56, 990, 500 };
			bgLayer13= new PanelBackgroundCoreCirc( corCir1, new Color( 217, 255, 204, 95 ) );
		}
		//
		PanelBackgroundRLetter signiture= new PanelBackgroundRLetter( 5, 23, 2315, 940, -1, "E _ESM #3.74",
				new Color( 255, 255, 153, 200 ) );
	}
	
	
	
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
						
						
						
						
						// top edge.
		Shapes sp1= new Shapes();
		line2D l1= new line2D();
		l1.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX1, GCSV.HomeShadeEdgeOffSetY1 );
		l1.p2= new cor2D( GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l1.thick= 1;
		l1.c= Color.WHITE;
		sp1.add( l1 );
		line2D l2= new line2D();
		l2.p1= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX1, GCSV.HomeShadeEdgeOffSetY1 );
		l2.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l2.thick= 1;
		l2.c= Color.WHITE;
		sp1.add( l2 );
		line2D l3= new line2D();
		l3.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l3.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, GCSV.HomeShadeEdgeOffSetY2 );
		l3.thick= 1;
		l3.c= Color.WHITE;
		sp1.add( l3 );
		pins.add( sp1 );
		// bottom edge.
		sp1= new Shapes();
		l1= new line2D();
		l1.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX1, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY1 );
		l1.p2= new cor2D( GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY2 );
		l1.thick= 1;
		l1.c= Color.WHITE;
		sp1.add( l1 );
		l2= new line2D();
		l2.p1= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX1, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY1 );
		l2.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY2 );
		l2.thick= 1;
		l2.c= Color.WHITE;
		sp1.add( l2 );
		l3= new line2D();
		l3.p1= new cor2D( GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight() - GCSV.HomeShadeEdgeOffSetY2 );
		l3.p2= new cor2D( paintBoard.getWidth() - GCSV.HomeShadeEdgeOffSetX2, paintBoard.getHeight()
				- GCSV.HomeShadeEdgeOffSetY2 );
		l3.thick= 1;
		l3.c= Color.WHITE;
		sp1.add( l3 );
		pins.add( sp1 );
		//
		// time display.
		xpos= paintBoard.getWidth() / 2 - 270;
		ypos= paintBoard.getHeight() / -2 + 40;
		pins.add( new Gadget_TimeDisplay( xpos, ypos ) );
		//
		// cpu usage display.
		xpos= paintBoard.getWidth() / -2 + 96;
		ypos= paintBoard.getHeight() / -2 + 20;
		pins.add( new Gadget_CPU( xpos, ypos, 400, 20 ) );
		//
		// set all.
		super.resetPinlistS( pins );

	*/
