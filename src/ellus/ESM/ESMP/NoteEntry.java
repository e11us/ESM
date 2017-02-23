package ellus.ESM.ESMP;

import java.awt.Color;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.ESMW.ESMW_SMXConfig;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.pinnable.Button.ButtonScrollWT;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelTextFieldPin;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class NoteEntry extends ESMPanel {
	private NoteEntry				handler		= this;
	private SManXElm				logWindow	= null;
	private ButtonTextFS			sizC		= null;
	private PanelTextFieldPin		txp			= null;
	private ButtonScrollWT			pgs			= null;
	private ButtonTextFS			newPS		= null;
	private int						curEntry	= 0;
	private final static String[]	entries		= { "keyword", "content", "webLink", "comment", "situation",
			"locLink", "endDate", "urgency", "importancy" };
	private final static String[]	entriesSize	= { "1000", "25000", "2083", "25000", "5000",
			"2083", "20", "1", "1" };
	private int						orgW, orgH, expW, expH;
	private boolean					expanded	= false;
	private ArrayList <String>		entryCont	= new ArrayList <>();
	// only allow one instance.
	private static NoteEntry		ne			= null;

	// get the instance.
	public static NoteEntry getInstance( SManXElm config ) {
		if( ne != null )
			return null;
		ne= new NoteEntry( config );
		return ne;
	}

	// close and reset.
	public static void close() {
		if( ne != null )
			ne.closePanel();
	}

	NoteEntry( SManXElm config ) {
		super( config );
		this.masterSE= config;
		this.logWindow= config.getElm( "ESMPL", "LoggerWindow" );
		orgW= PS.getWidth();
		orgH= PS.getHeight();
		expW= config.getAttr( AttrType._int, "WindowExpandedWidth" ).getInteger();
		expH= config.getAttr( AttrType._int, "WindowExpandedHeight" ).getInteger();
		//
		for( int i= 0; i < entries.length; i++ ){
			entryCont.add( "   " );
		}
		constructWindow();
	}

	private void constructWindow() {
		super.bgPL= new ESMPL();
		super.bgPL.add( 1, new PanelBackgroundSC( masterSE.getElm( "PanelBackgroundSC", "BackgroundColor" ), PS ) );
		super.titlePin= new PanelBackgroundTitleFS1(
				masterSE.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "NoteEntry" );
		super.bgPL.add( 2, titlePin );
		//
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( createLogWindow( logWindow ) );
	}

	private ESMPL createLogWindow( SManXElm config ) {
		ESMPL pl= new ESMPL();
		//
		int xS= PS.ViewOriginX();
		int yS= PS.ViewOriginY();
		//
		if( pgs == null ){
			pgs= new ButtonScrollWT( config.getElm( "ButtonScroll", "EntryItemScroll" ),
					entries.length, entries[curEntry] + " - size:" + entriesSize[curEntry] ) {
				@Override
				public void WheelRotateAction( int rot ) {
					entryCont.remove( curEntry );
					entryCont.add( curEntry, txp.getTxt() );
					//
					super.WheelRotateAction( rot );
					curEntry+= rot;
					if( curEntry < 0 )
						curEntry= 0;
					else if( curEntry > entries.length - 1 )
						curEntry= entries.length - 1;
					this.setMsg( entries[curEntry] + " - size:" + entriesSize[curEntry] );
					//
					txp.setText( entryCont.get( curEntry ) );
				}

				@Override
				public void B1clickAction( int x, int y ) {
					//
				}
			};
		}
		pl.add( 4, pgs );
		//
		if( newPS == null ){
			newPS= new ButtonTextFS( config.getElm( "ButtonTextFS", "Confirm" ),
					"Confirm" ) {
				@Override
				public void B1clickAction( int x, int y ) {
					//
					entryCont.remove( curEntry );
					entryCont.add( curEntry, txp.getTxt() );
					//
					sendQuery();
				}
			};
		}
		pl.add( 4, newPS );
		//
		ButtonTextFS exit= new ButtonTextFS( config.getElm( "ButtonTextFS", "Exit" ),
				"Exit NoteEntry" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				closePanel();
			}
		};
		pl.add( 4, exit );
		//
		ButtonTextFS easeA= new ButtonTextFS( config.getElm( "ButtonTextFS", "Erase" ),
				"Erase All" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				entryCont= new ArrayList <>();
				for( int i= 0; i < entries.length; i++ ){
					entryCont.add( "   " );
				}
				curEntry= 0;
				txp.setText( " " );
				pgs.setInde( 0 );
			}
		};
		pl.add( 4, easeA );
		//
		if( sizC == null ){
			sizC= new ButtonTextFS( config.getElm( "ButtonTextFS", "SizeChange" ),
					"Expand" ) {
				@Override
				public void B1clickAction( int x, int y ) {
					if( !expanded ){
						handler.resize( expW, expH );
						PS.changeViewCenterP( 0, - ( expH - orgH ) / 2 );
						expanded= true;
						sizC.setMsg( "Collapse" );
						//
						int dif= ( expH - orgH );
						txp.setXY( txp.getXmin(), txp.getXmax(), txp.getYmin() - dif, txp.getYmax() );
					}else{
						handler.resize( orgW, orgH );
						PS.changeViewCenterP( 0, ( expH - orgH ) / 2 );
						expanded= false;
						sizC.setMsg( "Expand" );
						//
						int dif= ( expH - orgH );
						txp.setXY( txp.getXmin(), txp.getXmax(), txp.getYmin() + dif, txp.getYmax() );
					}
				}
			};
		}
		pl.add( 4, sizC );
		//
		if( txp == null ){
			txp= new PanelTextFieldPin( config.getElm( "PanelTextFieldPin", "EntryContentEdit" ),
					helper.str2ALstr( entryCont.get( curEntry ) ) ) {
				@Override
				public void B3clickAction( int x, int y ) {
					closePanel();
				}
			};
		}
		pl.add( 4, txp );
		highlighted= txp;
		//
		return pl;
	}

	private void signalGoodInsert() {
		newPS.setFeedBack( "Good Insert", Color.green, 1800 );
	}

	private void signalBadInsert() {
		newPS.setFeedBack( "Bad Insert", Color.red, 1900 );
	}

	private void sendQuery() {
		ArrayList <String> name= new ArrayList <>();
		ArrayList <String> cont= new ArrayList <>();
		boolean empty= true;
		for( int i= 0; i < entries.length; i++ ){
			name.add( entries[i] );
			cont.add( entryCont.get( i ) );
			if( helper.containsAlphNum( entryCont.get( i ) ) )
				empty= false;
		}
		name.add( "functional" );
		cont.add( "NoteEntry" );
		if( !empty ){
			if( mySQLportal.insert( name, cont ) )
				signalGoodInsert();
			else signalBadInsert();
		}else signalBadInsert();
		//
		entryCont= new ArrayList <>();
		for( int i= 0; i < entries.length; i++ ){
			entryCont.add( "   " );
		}
		curEntry= 0;
		txp.setText( entryCont.get( 0 ) );
		pgs.setInde( 0 );
	}

	@Override
	protected void closePanel() {
		if( expanded ){
			handler.resize( orgW, orgH );
		}
		ne= null;
		super.closePanel();
	}

	@Override
	protected synchronized void KeyboardInpNGE( String lk ) {
		//
		if( lk.length() == 1 ){
			switch( lk.charAt( 0 ) ){
			}
		}else if( lk.length() == 2 ){
			switch( lk ){
				case "F8" :
					new ESMW_SMXConfig( masterSE );
					break;
			}
		}
	}
}
//
/*
private JTextArea txtPan;
**
SManXElm		inptWdw= logWindow.getElm( "ButtonInputMultiLine", "EntryContentEdit" );
txtPan= new JTextArea( );
txtPan.setBackground( inptWdw.getAttr( AttrType._color, "BackgroundColor1" ).getColor() );
txtPan.setForeground( inptWdw.getAttr( AttrType._color, "TextColor" ).getColor() );
txtPan.setLineWrap(true);
txtPan.setWrapStyleWord(true);
Border  raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
txtPan.setBorder( raisedetched );
txtPan.setFont(
		SCon.FontList.get( inptWdw.getAttr( AttrType._int, "FontIndex" ).getInteger() )
		.deriveFont( (float)inptWdw.getAttr( AttrType._int, "FontSize" ).getInteger() ) );
//
JScrollPane sp = new JScrollPane( txtPan );
sp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_NEVER);
sp.setBackground( Color.black );
int spx= logWindow.getAttr( AttrType._location, "TextInputWindow" ).getLocation().getX();
int spy= logWindow.getAttr( AttrType._location, "TextInputWindow" ).getLocation().getY();
sp.setBounds( spx, spy, PS.getWidth() - spx*2, PS.getHeight() - 250 );
super.setLayout( null );
super.add( sp );
*/
