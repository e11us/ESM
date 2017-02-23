package ellus.ESM.ESMP;

import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.dictionary.dictDB;
import ellus.ESM.pinnable.Button.ButtonInputFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelTextFieldPin;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXElm;



public class Dictionary extends ESMPanel {
	private String				curDir				= SMan.getSetting( 500 );
	private SManXElm			dictionaryShowConfig= null;
	private PanelTextFieldPin	txp					= null;
	private ButtonInputFS		inpb				= null;
	private String				curDef				= " ";
	// only allow one instance.
	private static Dictionary	dict				= null;

	// get the instance.
	public static Dictionary getInstance( SManXElm config ) {
		if( dict != null )
			return null;
		dict= new Dictionary( config );
		return dict;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor of class
	||||--------------------------------------------------------------------------------------------*/
	Dictionary( SManXElm config ) {
		super( config );
		//
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "Dictionary" );
		super.bgPL.add( 2, titlePin );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "P_DictionaryBgColor" ), PS ) );
		//
		dictionaryShowConfig= config.getElm( "ESMPL", "dictionaryShow" );
		dictionaryShow();
		//
		super.requestFocus();
		highlighted= inpb;
	}

	private void dictionaryShow() {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( cds( dictionaryShowConfig ) );
	}

	private ESMPL cds( SManXElm config ) {
		ESMPL pl= new ESMPL();
		//
		if( inpb == null ){
			inpb= new ButtonInputFS( config.getElm( "ButtonInputFS", "inpBox" ), "Search A Word in Dictionary.." );
		}
		if( txp == null ){
			txp= new PanelTextFieldPin( config.getElm( "PanelTextFieldPin", "defShow" ),
					helper.str2ALstr( curDef ) ) {
				@Override
				public void B3clickAction( int x, int y ) {
					closePanel();
				}
			};
			txp.readOnly();
		}
		//
		pl.add( 2, inpb );
		pl.add( 1, txp );
		//
		return pl;
	}

	@Override
	protected synchronized void KeyboardInp( String lk ) {
		if( lk.length() != 1 )
			return;
		//
		// test esc first.
		if( lk.charAt( 0 ) == (char)27 ){
			if( inpb.getInputMsg().length() > 0 )
				inpb.resetInputMsg();
			else closePanel();
			return;
		}
		// test enter
		if( lk.charAt( 0 ) == 10 ){
			delayGetter dl= new delayGetter( inpb.getInputMsg(), true );
			inpb.resetInputMsg();
			return;
		}
		//
		if( !inpb.isInputMode() ){
			inpb.B1clickHighlightOn( 0, 0 );
		}
		inpb.keyboardInp( lk.charAt( 0 ) + "" );
	}

	@Override
	protected void closePanel() {
		super.closePanel();
		dict= null;
	}

	class delayGetter extends Thread {
		String	word;
		boolean	showErr	= false;

		public delayGetter( String word, boolean showErr ) {
			super( "delayGetter-" + word );
			this.word= word;
			this.start();
		}

		@Override
		public void run() {
			String def= dictDB.getWord( word );
			if( def != null )
				txp.setText( def );
			else{
				if( showErr )
					txp.setText( "Error getting definition for word: " + word );
			}
		}
	}
}
