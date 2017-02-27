package ellus.ESM.media;

import java.io.File;
import java.util.ArrayList;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.Able.AbleSetMsg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;



public class PlayerAudio extends JFXPanel {
	private String							name				= "PlayerAudio - "
			+ helper.rand32AN().substring( 0, 5 );
	private MediaPlayer						mediaPlayer			= null;
	private long							updateProgDelay		= 500;
	private long							updateProgLastTime	= 0;
	private double							vol					= 0.03;
	//
	private static ArrayList <PlayerAudio>	PAS					= new ArrayList <>();
	// current open file upto 150 mb.
	private static int						fileSizeMax			= 150;

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public static PlayerAudio getInstance( String name ) {
		if( PAS.size() == 0 ){
			PlayerAudio pa= new PlayerAudio( name );
			return pa;
		}
		if( name != null )
			PAS.get( 0 ).name= name;
		return PAS.get( 0 );
	}

	public static void returnInstance( PlayerAudio pa ) {
		PAS.add( pa );
	}

	public static boolean testFilePath( String inp ) {
		try{
			if( ( ( new File( inp ) ).length() / 1024 / 1024 ) > fileSizeMax )
				return false;
			new File( inp ).toURI().toString();
		}catch ( Exception ee ){
			return false;
		}
		return true;
	}

	public static boolean canRun( String inp ) {
		try{
			if( ( ( new File( inp ) ).length() / 1024 / 1024 ) > fileSizeMax )
				return false;
			String fr= new File( inp ).toURI().toString();
			Media media= new Media( fr );
			media= null;
		}catch ( Exception ee ){
			return false;
		}
		return true;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public boolean set( String AF, Runnable rae ) {
		try{
			Media media= new Media( new File( AF ).toURI().toString() );
			mediaPlayer= new MediaPlayer( media );
			mediaPlayer.setAutoPlay( false );
			mediaPlayer.setOnEndOfMedia( rae );
			//
		}catch ( Exception ee ){
			return false;
		}
		return true;
	}

	public void setProgressUpdater( AbleSetMsg inp ) {
		mediaPlayer.currentTimeProperty().addListener( new ChangeListener() {
			@Override
			public void changed( ObservableValue arg0, Object arg1, Object arg2 ) {
				if( helper.getTimeLong() - updateProgLastTime > updateProgDelay ){
					updateProgLastTime= helper.getTimeLong();
					Duration currentTime= mediaPlayer.getCurrentTime();
					Duration duration= mediaPlayer.getTotalDuration();
					inp.setMsg( "[" + format( currentTime.toMillis() ) + " / " + format( duration.toMillis() ) + "]" );
				}
			}
		} );
		mediaPlayer.setOnReady( new Runnable() {
			@Override
			public void run() {
				inp.setMsg( "[0:0 / " + format( mediaPlayer.getTotalDuration().toMillis() ) + "]" );
			}
		} );
	}

	public void setVolume( double vol ) {
		if( mediaPlayer != null ){
			if( vol > 1 )
				vol= 1;
			else if( vol < 0 )
				vol= 0;
			mediaPlayer.setVolume( vol );
			this.vol= vol;
		}
	}

	public void rewind() {
		mediaPlayer.setVolume( vol );
		if( mediaPlayer != null )
			mediaPlayer.seek( new Duration( 0.0 ) );
	}

	public void pause() {
		if( mediaPlayer != null )
			mediaPlayer.pause();
	}

	public void stop() {
		if( mediaPlayer != null ){
			mediaPlayer.stop();
		}
	}

	public void play() {
		mediaPlayer.setVolume( vol );
		if( mediaPlayer != null ){
			mediaPlayer.play();
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public void playAt( double val ) {
		mediaPlayer.setVolume( vol );
		if( mediaPlayer != null ){
			Duration dur= new Duration( val );
			mediaPlayer.seek( dur );
		}
	}

	public void dispose() {
		if( mediaPlayer != null )
			mediaPlayer.dispose();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected PlayerAudio( String name ) {
		if( name != null )
			this.name= name;
	}

	private String format( double millis ) {
		StringBuffer buf= new StringBuffer();
		int hours= (int) ( millis / ( 1000 * 60 * 60 ) );
		int minutes= (int) ( ( millis % ( 1000 * 60 * 60 ) ) / ( 1000 * 60 ) );
		int seconds= (int) ( ( ( millis % ( 1000 * 60 * 60 ) ) % ( 1000 * 60 ) ) / 1000 );
		buf
				.append( String.format( "%02d", hours ) )
				.append( ":" )
				.append( String.format( "%02d", minutes ) )
				.append( ":" )
				.append( String.format( "%02d", seconds ) );
		return buf.toString();
	}
}
