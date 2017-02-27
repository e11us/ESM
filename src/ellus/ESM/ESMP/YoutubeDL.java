package ellus.ESM.ESMP;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.BArray;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelContainerScroll;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.roboSys.ExcPython;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXElm;



public class YoutubeDL extends ESMPanel {
	private SManXElm					youtubeDL	= null;
	protected static YoutubeDL			ytdl		= null;
	private boolean						DLall		= false;
	private ArrayList <YTDLer>			dler		= new ArrayList <>();
	private ArrayList <ButtonTextFS>	dlerbut		= new ArrayList <>();
	private BArray <String>				urlDling	= new BArray <>();

	// get the instance.
	public static YoutubeDL getInstance( SManXElm config ) {
		if( ytdl != null )
			return null;
		ytdl= new YoutubeDL( config );
		return ytdl;
	}

	YoutubeDL( SManXElm config ) {
		super( config );
		this.masterSE= config;
		this.youtubeDL= config.getElm( "ESMPL", "youtubeDL" );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "BackgroundColor" ), PS ) );
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "Youtube DLer" );
		super.bgPL.add( 2, titlePin );
		//
		construct();
		YTLinkchecker.startChecker();
	}

	protected void construct() {
		//
		ESMPL pl= new ESMPL();
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( pl );
		//
		ArrayList <pinnable> todl= new ArrayList <>();
		for( String str : helper.readFile( SMan.getSetting( 0 ) + SMan.getSetting( 213 ) ) ){
			if( !str.contains( SCon.youtubeUrlHead ) ){
				deleteTodo( str );
			}else{
				if( urlDling.contain( str ) )
					continue;
				todl.add( new ButtonTextFS( youtubeDL.getElm( "ButtonTextFS", "Functional" ),
						str.substring( SCon.youtubeUrlHead.length(), str.length() ) ) {
					@Override
					public void B1clickAction( int x, int y ) {
						if( !dlerbut.contains( this ) ){
							YTDLer ytdl= new YTDLer( str, this );
							dlerbut.add( this );
							dler.add( ytdl );
							urlDling.add( str );
						}
					}

					@Override
					public void B3clickAction( int x, int y ) {
						if( !dlerbut.contains( this ) ){
							deleteTodo( str );
							construct();
						}else this.setFeedBack( "DLing, can not delete", Color.red, 1200 );
					}
				} );
			}
		}
		//
		if( DLall ){
			for( pinnable pin : todl )
				( (ButtonTextFS)pin ).B1clickAction( 0, 0 );
			todl= new ArrayList <>();
			todl.addAll( dlerbut );
		}else{
			todl.addAll( dlerbut );
		}
		DLall= false;
		//
		PanelContainerScroll todlpan= new PanelContainerScroll( youtubeDL.getElm( "PanelContainer", "urls" ), todl );
		pl.add( 4, todlpan );
		//
		// functional key.
		ArrayList <pinnable> dfp= new ArrayList <>();
		dfp.add( new ButtonTextFS( youtubeDL.getElm( "ButtonTextFS", "Functional" ), "DownLoad all" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				DLall= true;
				construct();
			}
		} );
		dfp.add( new ButtonTextFS( youtubeDL.getElm( "ButtonTextFS", "Functional" ), "Rename folder" ) {
			@Override
			public void B1clickAction( int x, int y ) {}
		} );
		dfp.add( new ButtonTextFS( youtubeDL.getElm( "ButtonTextFS", "Functional" ), "Open DL folder" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				try{
					DeskTop.getDeskTop().open( new File( SMan.getSetting( 0 ) + SMan.getSetting( 211 ) ) );
				}catch ( IOException e ){}
			}
		} );
		dfp.add( new ButtonTextFS( youtubeDL.getElm( "ButtonTextFS", "Functional" ), "Exit YTDL" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( dler.size() > 0 ){
					this.setFeedBack( "DL in progress, please wait", Color.red, 1300 );
					return;
				}else closePanel();
			}
		} );
		PanelContainerScroll dfpc= new PanelContainerScroll( youtubeDL.getElm( "PanelContainer", "FunctionalPanel" ),
				dfp );
		pl.add( 4, dfpc );
	}

	/*||----------------------------------------------------------------------------------------------
	||| delete the todo item in the list.
	||||--------------------------------------------------------------------------------------------*/
	protected synchronized void deleteTodo( String inp ) {
		ArrayList <String> todo= helper.getAllText( SMan.getSetting( 0 ) + SMan.getSetting( 213 ) );
		for( int i= 0; i < todo.size(); i++ ){
			if( todo.get( i ).equals( inp ) ){
				todo.remove( i );
				break;
			}
		}
		PrintWriter pw;
		try{
			pw= new PrintWriter( SMan.getSetting( 0 ) + SMan.getSetting( 213 ) );
			for( String tmp : todo ){
				pw.println( tmp );
			}
			pw.close();
		}catch ( FileNotFoundException e ){
			e.printStackTrace();
			return;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	||| close
	||||--------------------------------------------------------------------------------------------*/
	@Override
	protected void closePanel() {
		if( dler.size() > 0 )
			return;
		YTLinkchecker.stopChecker();
		ytdl= null;
		super.closePanel();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| a bridge between the py execu and the ESM, the button inside.
	 ||| given a button and YT path, dl the vid or playlist, and update the button.
	||||--------------------------------------------------------------------------------------------*/
	private class YTDLer implements Runnable {
		private YTDLer			handler	= this;
		private String			path;
		private ExcPython		pyex	= null;
		private ButtonTextFS	butt;
		File					tmpPY;

		public YTDLer( String path, ButtonTextFS button ) {
			this.path= path;
			butt= button;
			// assemble a py script for this path.
			File pyDL= new File( SMan.getSetting( 0 ) + SMan.getSetting( 214 ) );
			if( !pyDL.exists() || !pyDL.isFile() || path.length() < 43 ){
				//
				// delete error todo.
				if( path.length() < 43 )
					deleteTodo( this.path );
				dler.remove( handler );
				dlerbut.remove( butt );
				//
				display.println( handler.getClass().toString(),
						"YTDL: " + path.substring( 32, 43 ) + " Error, exiting..." );
				construct();
				return;
			}
			// first create folder.
			tmpPY= new File( SMan.getSetting( 0 ) + SMan.getSetting( 4 ) );
			if( !tmpPY.exists() )
				tmpPY.mkdirs();
			tmpPY= new File( SMan.getSetting( 0 ) + SMan.getSetting( 4 ) + "/" +
					path.substring( 32, 43 ) + " " + helper.getCurrentTimeStamp() + ".py" );
			try{
				//
				// make a py file.
				ArrayList <String> py= helper.getAllText( pyDL.getAbsolutePath() );
				if( py.size() < 20 )
					return;
				//
				// YTDL main folder.
				py.add( 8, py.get( 7 ) + " \"" + SMan.getSetting( 0 ) + SMan.getSetting( 212 )
						+ "\"" );
				py.remove( 7 );
				//
				// tmp folder ( line 9 )
				File tmpF= new File( SMan.getSetting( 0 ) + SMan.getSetting( 215 ) );
				if( !tmpF.exists() )
					tmpF.mkdirs();
				py.add( 9, py.get( 8 ) + " \"" + SMan.getSetting( 0 ) + SMan.getSetting( 215 )
						+ "\"" );
				py.remove( 8 );
				//
				// if list, dl in a folder. ( line 10 )
				if( path.length() > 43 && path.contains( "&list=" ) ){
					int loc= path.indexOf( "&list=" );
					py.add( 10, py.get( 9 ) + " \"" + SMan.getSetting( 0 ) + SMan.getSetting( 211 )
							+ "/" + path.substring( loc + 6, path.length() ) + "\"" );
				}else py.add( 10, py.get( 9 ) + " \"" + SMan.getSetting( 0 )
						+ SMan.getSetting( 211 ) + "\"" );
				py.remove( 9 );
				//
				// YT address line ( 20 ).
				py.add( 20, py.get( 19 ) + path + "'])" );
				py.remove( 19 );
				PrintWriter pw= new PrintWriter( tmpPY.getAbsolutePath() );
				for( String tmp : py ){
					pw.println( tmp );
				}
				pw.close();
				//
				// exec py
				pyex= new ExcPython( tmpPY.getAbsolutePath(), path.substring( 32, 43 ) + "DL" );
				// run thread to wait for exec py.
				Thread tt= new Thread( handler, path.substring( 32, 43 ) + "YTDLer" );
				tt.start();
			}catch ( IOException e ){
				e.printStackTrace();
				return;
			}
		}

		public String getPath() {
			return path;
		}

		@Override
		public void run() {
			if( pyex != null ){
				while( !pyex.ended ){
					if( pyex.msg != null ){
						if( pyex.msg.length() > 40 )
							butt.setMsg( pyex.msg.substring( 0, 40 ) );
						else butt.setMsg( pyex.msg );
					}
					display.println( handler.getClass().toString(),
							"YTDL: " + path.substring( 32, 43 ) + " " + pyex.msg );
					try{
						Thread.sleep( 100 );
					}catch ( InterruptedException e ){
						e.printStackTrace();
					}
				}
				butt.setMsg( path.substring( 32, 43 ) + " is completed." );
				tmpPY.delete();
				tmpPY= new File( SMan.getSetting( 0 ) + SMan.getSetting( 4 ) + "cookie" );
				if( tmpPY.exists() && tmpPY.isFile() )
					tmpPY.delete();
				display.println( handler.getClass().toString(),
						"YTDL: " + path.substring( 32, 43 ) + " dl is completed." );
				//
				// delete todo.
				deleteTodo( this.path );
				//
				dler.remove( handler );
				dlerbut.remove( butt );
				urlDling.remove( path );
				construct();
			}
		}
	}
	// youtube link.
	// len= 43. substring( 32, 43 ) = ID.
}

/*||----------------------------------------------------------------------------------------------
||| check if link presents.
||||--------------------------------------------------------------------------------------------*/
class YTLinkchecker {
	private static checker	ck		= null;
	static boolean			running	= false;
	static BArray <String>	pasturl	= new BArray <>();

	public boolean getStatus() {
		return running;
	}

	public static void startChecker() {
		if( ck == null )
			ck= new checker( "YTLinkchecker" );
		running= true;
		ck.start();
	}

	public static void stopChecker() {
		running= false;
		ck= null;
	}
}

class checker extends Thread {
	public checker( String inp ) {
		super( inp );
	}

	@Override
	public void run() {
		String inp;
		while( YTLinkchecker.running ){
			inp= clipBoard.getString();
			if( inp.length() >= 43 && inp.contains( SCon.youtubeUrlHead ) && !YTLinkchecker.pasturl.contain( inp ) ){
				YTLinkchecker.pasturl.add( inp );
				clipBoard.setString( "-" );
				//
				// append to toDL.txt
				FileWriter fw;
				try{
					fw= new FileWriter( SMan.getSetting( 0 ) + SMan.getSetting( 213 ), true );
					BufferedWriter bw= new BufferedWriter( fw );
					bw.write( inp + "\n" );
					bw.close();
					fw.close();
					//
					if( YoutubeDL.ytdl != null ){
						YoutubeDL.ytdl.construct();
					}
				}catch ( IOException e ){
					e.printStackTrace();
				}
			}
			try{
				this.sleep( 700 );
			}catch ( InterruptedException e ){}
		}
	}
}
