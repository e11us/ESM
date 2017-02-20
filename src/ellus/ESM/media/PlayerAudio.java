package ellus.ESM.media;

import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;



public class PlayerAudio extends JFXPanel {
	private String		name= null;
	private MediaPlayer	mediaPlayer;

	public PlayerAudio( String audioFile, String name ) {
		this.name= name;
		//
		Media media= new Media( new File( audioFile ).toURI().toString() );
		mediaPlayer= new MediaPlayer( media );
		mediaPlayer.setAutoPlay( false );
	}

	public PlayerAudio( String audioFile ) {
		//
		Media media= new Media( new File( audioFile ).toURI().toString() );
		mediaPlayer= new MediaPlayer( media );
		mediaPlayer.setAutoPlay( false );
	}

	public void setVolume( double vol ) {
		if( vol > 1 )
			vol= 1;
		else if( vol < 0 )
			vol= 0;
		mediaPlayer.setVolume( vol );
	}

	public void play() {
		mediaPlayer.seek( new Duration( 0.0 ) );
		mediaPlayer.play();
	}

	@Override
	public String getName() {
		return name;
	}

	public void playAt( double val ) {
		Duration dur= new Duration( val );
		mediaPlayer.seek( dur );
	}
}
