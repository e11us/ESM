package ellus.ESM.ESMW;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import ellus.ESM.pinnable.Able.AbleClickHighlight;
import ellus.ESM.pinnable.Able.AbleKeyboardFunInp;
import ellus.ESM.pinnable.Able.AbleKeyboardInput;
import ellus.ESM.pinnable.Button.ButtonInputFS;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ESMW_SMXConfig extends ESMW {
	private ESMPanel				homePanel		= null;
	private ArrayList <SManXElm>	currentElmNest	= new ArrayList <>();
	private SManXElm				boardSetting	= null;
	private SManXElm				SE				= SManX.get( "ESM", "_the_ESM_" )
			.getElm( "Window", SCon.SB_SMXCnfg_SManX_Name );

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor of class
	||||--------------------------------------------------------------------------------------------*/
	public ESMW_SMXConfig( SManXElm elm ) {
		// copy elm.
		currentElmNest.add( elm );
		// homePanel SetUp. ( home panel always starts in full screen. & create setting package. )
		homePanel= setUpHomePanel( SE );
		boardSetting= SE.getElm( "ESMPanel", "ConfigHome" ).getElm( "Setting", "GeneralSetting" );
		// set it to frame bg.
		super.bgPanel= homePanel;
		// Frame SetUp.
		SE.getElm( "ESMW", "configFrame" ).getAttr( AttrType._int, "WindowWideth" ).setVal(
				SE.getElm( "ESMPanel", "ConfigHome" ).getAttr( AttrType._int, "PanelWidth" ) + "" );
		SE.getElm( "ESMW", "configFrame" ).getAttr( AttrType._int, "WindowHeight" ).setVal(
				SE.getElm( "ESMPanel", "ConfigHome" ).getAttr( AttrType._int, "PanelHeight" ) + "" );
		super.init( SE.getElm( "ESMW", "configFrame" ) );
		super.frame.setResizable( false );
		super.frame.setLocationRelativeTo( null );
		construct();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| init all the part at first.
	||||--------------------------------------------------------------------------------------------*/
	private void construct() {
		ESMPL PL= new ESMPL();
		homePanel.bgPL= PL;
		//
		// set the title as the current element.
		String name= super.name + " - Current Element: .";
		for( SManXElm elm : currentElmNest ){
			name+= "/" + elm.getName();
		}
		super.frame.setTitle( name );
		//
		int xS= homePanel.PS.ViewOriginX();
		int yS= homePanel.PS.ViewOriginY();
		int sepa= boardSetting.getAttr( AttrType._int, "BoardItemSeperation" ).getInteger();
		int fontInd= boardSetting.getAttr( AttrType._int, "BoardFontInd" ).getInteger();
		int fontSize= boardSetting.getAttr( AttrType._int, "BoardFontSize" ).getInteger();
		//
		Color[] NWButColor= {
				boardSetting.getAttr( AttrType._color, "ButtonColor1" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColor2" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColor3" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColor4" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColor5" ).getColor()
		};
		Color[] NWButColorH= {
				boardSetting.getAttr( AttrType._color, "ButtonColorH1" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColorH2" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColorH3" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColorH4" ).getColor(),
				boardSetting.getAttr( AttrType._color, "ButtonColorH5" ).getColor()
		};
		int[] NWButton= {
				xS + sepa, yS + sepa,
				boardSetting.getAttr( AttrType._int, "ButtonWidth" ).getInteger(),
				boardSetting.getAttr( AttrType._int, "ButtonHeight" ).getInteger(),
				boardSetting.getAttr( AttrType._int, "ButtonXOS" ).getInteger(),
				boardSetting.getAttr( AttrType._int, "ButtonYOS" ).getInteger(), };
		//
		// for all child elm
		ButtonTextFS dbut;
		ButtonInputFS ipbut;
		ArrayList <SManXElm> childs= currentElmNest.get( currentElmNest.size() - 1 ).getElmAll();
		if( childs != null ){
			for( SManXElm child : childs ){
				NWButton[0]= sepa + xS;
				dbut= new ButtonTextFS( NWButton, NWButColor, NWButColor,
						"Element type: " + child.getType(), fontInd, fontSize );
				//dbut= new ButtonTextFS( SE.getElm( "ButtonTextFS", "ElementChilds_TypeInfo" ), "Element type: " + child.getType() );
				PL.add( 1, dbut );
				NWButton[0]+= sepa + boardSetting.getAttr( AttrType._int, "ButtonWidth" ).getInteger();
				dbut= new ButtonTextFS( NWButton, NWButColor, NWButColorH,
						"Element name: " + child.getName(), fontInd, fontSize ) {
					@Override
					public void B1clickAction( int x, int y ) {
						currentElmNest.add( child );
						construct();
					}
				};
				PL.add( 1, dbut );
				NWButton[1]+= sepa + boardSetting.getAttr( AttrType._int, "ButtonHeight" ).getInteger();
			}
		}
		//
		// for all attributes.
		ArrayList <SManXAttr> attributes= currentElmNest.get( currentElmNest.size() - 1 ).getAttrAll();
		if( attributes != null ){
			for( SManXAttr attribute : attributes ){
				NWButton[0]= xS + sepa;
				dbut= new ButtonTextFS( NWButton, NWButColor, NWButColor,
						"Attribute name:   " + attribute.getName(), fontInd, fontSize );
				PL.add( 1, dbut );
				NWButton[0]+= sepa + boardSetting.getAttr( AttrType._int, "ButtonWidth" ).getInteger();
				dbut= new ButtonTextFS( NWButton, NWButColor, NWButColor,
						"Attribute type: " + attribute.getType(), fontInd, fontSize );
				PL.add( 1, dbut );
				NWButton[1]+= sepa + boardSetting.getAttr( AttrType._int, "ButtonHeight" ).getInteger();
				ipbut= new ButtonInputFS( NWButton, NWButColor, NWButColorH, attribute.getVal(),
						fontInd, fontSize ) {
					@Override
					public void keyboardInp( String code ) {
						if( code.length() == 1 ){
							if( code.charAt( 0 ) == 10 ){
								// reset the config for the given element.
								attribute.setVal( inputMsg );
								currentElmNest.get( currentElmNest.size() - 1 ).flushChange();
								this.defMsg= attribute.getVal();
								super.B1clickHighlightOff( 0, 0 );
							}
							// for char input.
							if( code.charAt( 0 ) >= 32 && code.charAt( 0 ) <= 126 ){
								// regular char.;
								inputMsg= inputMsg + code.charAt( 0 );
								return;
							}else if( ( code.charAt( 0 ) ) == 8 ){
								// back space key for delete char
								if( inputMsg.length() > 0 )
									inputMsg= inputMsg.substring( 0, inputMsg.length() - 1 );
							}
						}
					}

					@Override
					public void B1clickHighlightOn( int x, int y ) {
						inputMode= true;
						inputMsg= defMsg;
					}

					@Override
					public void B1clickHighlightOff( int x, int y ) {
						inputMode= false;
					}

					@Override
					public void B1DoubleClickAction( int x, int y ) {
						inputMode= true;
						inputMsg= "";
					}
				};
				PL.add( 1, ipbut );
				NWButton[1]+= sepa * 2 + boardSetting.getAttr( AttrType._int, "ButtonHeight" ).getInteger();
			}
		}
	}

	private ESMPanel setUpHomePanel( SManXElm config ) {
		ESMPanel pan= new ESMPanel( config.getElm( "ESMPanel", "ConfigHome" ) ) {
			@Override
			protected synchronized void CheckB3Click( MouseEvent e ) {
				if( currentElmNest.size() > 1 ){
					currentElmNest.remove( currentElmNest.size() - 1 );
					construct();
				}else{
					closeFrame();
				}
			}

			@Override
			protected synchronized void KeyboardInp( String lk ) {
				//
				// always check esc first.
				if( ( lk.charAt( 0 ) ) == 27 ){
					if( highlighted != null ){
						( (AbleClickHighlight)highlighted ).B1clickHighlightOff( 0, 0 );
						highlighted= null;
					}else{
						closeFrame();
					}
				}else if( highlighted != null ){
					// if there is highlighted, check keyboard for that.
					if( highlighted instanceof AbleKeyboardInput && lk.length() == 1 ){
						( (AbleKeyboardInput)highlighted ).keyboardInp( lk );
					}else if( highlighted instanceof AbleKeyboardFunInp && lk.length() == 2 ){
						( (AbleKeyboardFunInp)highlighted ).FunInp( lk );
					}
				}else if( lk.length() == 1 ){
					// otherwise, check if system want keyboard input.
					int code= ( lk.charAt( 0 ) );
					switch( code ){
						default :
							break;
					}
				}else if( lk.length() == 2 ){
					switch( lk ){
						default :
					}
				}else if( lk.length() == 3 ){
					switch( lk ){
					}
				}
			}
		};
		return pan;
	}
}
