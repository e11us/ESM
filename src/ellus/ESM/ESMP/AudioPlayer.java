package ellus.ESM.ESMP;

import java.awt.Color;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.media.PlayerAudio;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Able.AbleKeyboardFunInp;
import ellus.ESM.pinnable.Able.AbleKeyboardInput;
import ellus.ESM.pinnable.Button.ButtonScrollBarVerti;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelContainerScroll;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXElm;



public class AudioPlayer extends ESMPanel {
	private SManXElm				APconfig	= null;
	private PlayerAudio				pa			= PlayerAudio.getInstance( null );
	private runAtEnd				_RAE		= new runAtEnd();
	private playlist				PL			= new playlist();
	private ButtonScrollBarVerti	vlb			= null;
	private ButtonTextFS			title		= null;
	private ButtonTextFS			play		= null;
	private ButtonTextFS			prog		= null;
	private static final double		volinit		= 0.016;
	private static final double		volmin		= 0.0101;
	private static final double		volmax		= 0.21;
	protected static final String	musicFolder	= SMan.getSetting( 420 );
	protected static String			lastPlay	= SMan.getSetting( 421 );
	private boolean					musicSetted	= false;
	private boolean					random		= true;
	// 1= repeat all, 2= repeat single, 3= off.
	private int						repeatMode	= 1;

	public AudioPlayer( SManXElm config ) {
		super( config );
		super.masterSE= config;
		//
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "Audio Player" );
		super.bgPL.add( 2, titlePin );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "P_BgColor" ), PS ) );
		this.APconfig= config.getElm( "ESMPL", "AudioPlayerPan" );
		construct();
		//
		if( lastPlay != null && !lastPlay.equals( "null" ) ){
			PL.set( lastPlay );
		}
	}

	// all mouse goes to the volume control.
	protected void checkB2Press( MouseWheelEvent e ) {
		vlb.WheelRotateAction( e.getWheelRotation() );
	}

	// keyboard control.
	protected synchronized void KeyboardInp( String lk ) {
		if( lk.length() == 1 ){
			switch( lk.charAt( 0 ) ){
				case ' ' :
					play.B1clickAction( 0, 0 );
					break;
			}
		}else if( lk.length() == 2 ){
			switch( lk ){
				case "AU" :
					vlb.WheelRotateAction( -1 );
					break;
				case "AD" :
					vlb.WheelRotateAction( 1 );
			}
		}
	}

	private void construct() {
		ESMPL pl= new ESMPL();
		super.Subpls.add( pl );
		//
		if( vlb == null )
			vlb= new ButtonScrollBarVerti(
					APconfig.getElm( "ButtonScrollBarVerti", "volumeBar" ), volmin, volmax, volinit, 1.5 ) {
				@Override
				public void WheelRotateAction( int rot ) {
					super.WheelRotateAction( rot );
					pa.setVolume( super.getValue() );
				}
			};
		pl.add( 4, vlb );
		if( title == null )
			title= new ButtonTextFS( APconfig.getElm( "ButtonTextFS", "musicTitle" ), "Play Music" ) {
				@Override
				public void B1clickAction( int x, int y ) {
					try{
						DeskTop.getDeskTop().open( new File( PL.getCurrentMusicFolder() ) );
					}catch ( Exception e ){}
				}
			};
		title.setUseMsgRoll( true );
		//
		pl.add( 3, title );
		if( prog == null )
			prog= new ButtonTextFS( APconfig.getElm( "ButtonTextFS", "musicProgress" ), " 0:0 / 0:0 " );
		pl.add( 3, prog );
		//
		if( play == null )
			play= new ButtonTextFS(
					APconfig.getElm( "ButtonTextFS", "play" ), "Play" ) {
				@Override
				public void B1clickAction( int x, int y ) {
					if( this.msg.equals( "Play" ) ){
						if( musicSetted ){
							pa.play();
							this.msg= "Pause";
						}else{
							String nxt;
							if( !random )
								nxt= PL.getCur();
							else nxt= PL.NextRand();
							//
							if( nxt != null ){
								//
								title.setMsg( PL.getCurrentMusicTitle() );
								display.println( this.getClass().toString(),
										" music: " + PL.getCurrentMusicTitle() + " is selected to play" );
								pa.set( nxt, _RAE );
								pa.setVolume( vlb.getValue() );
								pa.play();
								musicSetted= true;
								this.msg= "Pause";
								//
								pa.setProgressUpdater( prog );
							}else{
								// no music set.
							}
						}
					}else{
						if( pa != null ){
							pa.pause();
						}
						this.msg= "Play";
					}
				}
			};
		pl.add( 3, play );
		//
		// functional key.
		ArrayList <pinnable> dfp= new ArrayList <>();
		ButtonTextFS func;
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Previous" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				String nxt;
				if( random )
					nxt= PL.NextRand();
				else nxt= PL.Previous();
				if( nxt != null ){
					pa.pause();
					pa.stop();
					pa.dispose();
					title.setMsg( PL.getCurrentMusicTitle() );
					display.println( this.getClass().toString(),
							" music: " + PL.getCurrentMusicTitle() + " is selected to play" );
					pa.set( nxt, _RAE );
					pa.setVolume( vlb.getValue() );
					pa.play();
					//
					pa.setProgressUpdater( prog );
					musicSetted= true;
					play.setMsg( "Pause" );
				}
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Next" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				String nxt;
				if( random )
					nxt= PL.NextRand();
				else nxt= PL.Next();
				if( nxt != null ){
					display.println( this.getClass().toString(), "Next 1" );
					pa.pause();
					pa.stop();
					display.println( this.getClass().toString(), "Next 2" );
					pa.dispose();
					display.println( this.getClass().toString(), "Next 3" );
					title.setMsg( PL.getCurrentMusicTitle() );
					display.println( this.getClass().toString(),
							" music: " + PL.getCurrentMusicTitle() + " is selected to play" );
					pa.set( nxt, _RAE );
					display.println( this.getClass().toString(), "Next 4" );
					pa.setVolume( vlb.getValue() );
					display.println( this.getClass().toString(), "Next 5" );
					pa.play();
					pa.setProgressUpdater( prog );
					musicSetted= true;
					display.println( this.getClass().toString(), "Next completed" );
					play.setMsg( "Pause" );
				}
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Random on" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( this.msg.equals( "Random on" ) ){
					this.msg= "Random off";
					random= false;
				}else{
					this.msg= "Random on";
					random= true;
				}
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Repeat all" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( this.msg.equals( "Repeat single" ) ){
					this.msg= "Repeat all";
					repeatMode= 1;
				}else if( this.msg.equals( "Repeat all" ) ){
					this.msg= "Repeat off";
					repeatMode= 3;
				}else{
					this.msg= "Repeat single";
					repeatMode= 2;
				}
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Stop" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				if( pa != null ){
					pa.pause();
					pa.stop();
					pa.dispose();
					play.setMsg( "Play" );
				}
				musicSetted= false;
				title.setMsg( "Play Music" );
				prog.setMsg( " 0:0 / 0:0 " );
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Select file" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				pa.pause();
				display.println( this.getClass().toString(), "stop 1" );
				pa.stop();
				display.println( this.getClass().toString(), "stop 2" );
				pa.dispose();
				display.println( this.getClass().toString(), "stop 3" );
				play.setMsg( "Play" );
				prog.setMsg( " 0:0 / 0:0 " );
				musicSetted= false;
				display.println( this.getClass().toString(), "stop 4" );
				PL.set();
				display.println( this.getClass().toString(), "stop 5" );
				title.setMsg( PL.title );
				display.println( this.getClass().toString(), "stop completed" );
			}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Playlist" ) {
			@Override
			public void B1clickAction( int x, int y ) {}
		};
		dfp.add( func );
		func= new ButtonTextFS(
				APconfig.getElm( "ButtonTextFS", "functionalButton" ), "Rate song" ) {
			@Override
			public void B1clickAction( int x, int y ) {}
		};
		dfp.add( func );
		//
		PanelContainerScroll fp= new PanelContainerScroll( APconfig.getElm( "PanelContainer", "functionalPan" ), dfp );
		pl.add( 3, fp );
	}

	@Override
	public void closePanel() {
		if( pa != null ){
			pa.pause();
			pa.stop();
			pa.dispose();
			PlayerAudio.returnInstance( pa );
		}
		SMan.changeSetting( 412, lastPlay.replace( '\\', '/' ) );
		super.closePanel();
	}

	private void musicEnded() {
		String nxt;
		if( repeatMode == 1 ){
			if( random ){
				nxt= PL.NextRand();
			}else{
				nxt= PL.Next();
			}
			if( nxt != null ){
				title.setMsg( PL.getCurrentMusicTitle() );
				display.println( this.getClass().toString(),
						" music: " + PL.getCurrentMusicTitle() + " is selected to play" );
				pa.set( nxt, _RAE );
				pa.play();
				pa.setProgressUpdater( prog );
				musicSetted= true;
			}
		}else if( repeatMode == 2 ){
			nxt= PL.getCur();
			if( nxt != null ){
				display.println( this.getClass().toString(),
						" music: " + PL.getCurrentMusicTitle() + " is selected to play" );
				pa.rewind();
				pa.play();
				pa.setProgressUpdater( prog );
				musicSetted= true;
			}
		}else{
			play.setMsg( "Play" );
			musicSetted= false;
			title.setMsg( "Play Music" );
		}
	}

	private class runAtEnd implements Runnable {
		@Override
		public void run() {
			musicEnded();
		}
	}
}

class playlist {
	ArrayList <String>	music		= new ArrayList <>();
	//
	String				folderpath	= null;
	String				musicpath	= null;
	//
	String				title		= null;
	int					curr		= 0;

	void set( String path ) {
		setInt( new File( path ) );
	}

	boolean set() {
		File selFil= helper.selectDirAndFile( new File( AudioPlayer.musicFolder ) );
		if( selFil == null )
			return false;
		return setInt( selFil );
	}

	private boolean setInt( File selFil ) {
		if( selFil.isDirectory() ){
			//
			Thread fullLoader= new Thread() {
				public void run() {
					boolean first= true;
					//
					ArrayList <String> sub= helper.getAllFile( selFil.getAbsolutePath(), SCon.AudioExtList );
					if( sub == null || sub.size() == 0 )
						return;
					for( int i= 0; i < sub.size(); i++ ){
						if( PlayerAudio.testFilePath( sub.get( i ) ) ){
							if( first ){
								first= false;
								music= new ArrayList <>();
								folderpath= selFil.getAbsolutePath();
								AudioPlayer.lastPlay= folderpath;
								music.add( sub.get( i ) );
							}else music.add( sub.get( i ) );
							//
							if( i % 1000 == 0 )
								display.println( this.getClass().toString(), " Loading Music to index: " + ( i + 1 ) );
						}
					}
				}
			};
			title= helper.getFilePathName( selFil.toString() );
			fullLoader.start();
			//
		}else{
			if( !PlayerAudio.canRun( selFil.getAbsolutePath() ) )
				return false;
			musicpath= selFil.getAbsolutePath();
			title= helper.getFileName( helper.getFilePathName( folderpath ) );
			music= new ArrayList <String>();
			music.add( musicpath );
			AudioPlayer.lastPlay= musicpath;
		}
		//
		curr= 0;
		return true;
	}

	String getCurrentMusicFolder() {
		if( music != null && music.size() > 0 ){
			if( curr >= music.size() )
				curr= 0;
			return new File( music.get( curr ) ).getParent();
		}
		return null;
	}

	String getCurrentMusicTitle() {
		if( music != null && music.size() > 0 ){
			if( curr >= music.size() )
				curr= 0;
			return "[" + ( curr + 1 ) + "/" + ( music.size() ) + "]  "
					+ helper.getFileName( helper.getFilePathName( music.get( curr ) ) );
		}else return null;
	}

	String getCur() {
		if( music != null && music.size() > 0 ){
			if( curr >= music.size() )
				curr= 0;
			while( !PlayerAudio.canRun( music.get( curr ) ) ){
				music.remove( curr-- );
				if( curr == -1 )
					return null;
			}
			return music.get( curr );
		}
		return null;
	}

	String Next() {
		if( music != null && music.size() > 0 ){
			if( ++curr >= music.size() )
				curr= 0;
			while( !PlayerAudio.canRun( music.get( curr ) ) ){
				music.remove( curr-- );
				if( curr == -1 )
					return null;
			}
			return music.get( curr );
		}
		return null;
	}

	String Previous() {
		if( music != null && music.size() > 0 ){
			if( --curr < 0 )
				curr= music.size() - 1;
			while( !PlayerAudio.canRun( music.get( curr ) ) ){
				music.remove( curr-- );
				if( curr == -1 )
					return null;
			}
			return music.get( curr );
		}
		return null;
	}

	String NextRand() {
		if( music != null && music.size() > 0 ){
			curr= (int) ( Math.random() * music.size() );
			while( !PlayerAudio.canRun( music.get( curr ) ) ){
				music.remove( curr-- );
				if( curr == -1 )
					return null;
			}
			return music.get( curr );
		}
		return null;
	}
}