package ellus.ESM.WebInteract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import ellus.ESM.Machine.helper;



/* -----------------------------------------------------------------------------
 example:
 	new DL( "./folder/folder", "abc.txt", "http://exp.com" );

 example:
 	DL dler= new DL( ".", "out.mp3", "http://abc.mp3" );
 	while( dler.isAlive() ){}
 	System.out.println(  dler.isSucc() );

 will create new file if already exists.
 * -----------------------------------------------------------------------------
 */
public class DL implements Runnable {
	private String	url	= null;
	private File	path= null;
	private Thread	runs= null;

	public DL( String filepathInp, String filenameInp, String urlInp, boolean threaded, boolean skipExisting ) {
		if( filepathInp == null || filenameInp == null || urlInp == null )
			return;
		// check file path.
		File filepath= new File( filepathInp );
		if( !filepath.exists() )
			filepath.mkdirs();
		// check file.
		path= new File( filepathInp + "/" + filenameInp );
		if( path.exists() && path.isFile() ){
			if( skipExisting ){
				return;
			}else{
				path= new File( helper.existingFileName( filepathInp + "/"
						+ filenameInp ) );
			}
		}
		this.url= urlInp;
		// starts to run it.
		if( threaded ){
			runs= new Thread( this, "DLerThread_" + helper.randAN( 3 ) + "_" );
			runs.start();
		}else DL();
	}

	@Override
	public void run() {
		DL();
	}

	private void DL() {
		if( url == null || path == null )
			return;
		try{
			URL website= null;
			ReadableByteChannel rbc= null;
			FileOutputStream fos= null;
			website= new URL( url );
			rbc= Channels.newChannel( website.openStream() );
			fos= new FileOutputStream( path.getAbsolutePath() );
			fos.getChannel().transferFrom( rbc, 0, Long.MAX_VALUE );
			fos.close();
			rbc.close();
		}catch ( IOException e ){
			e.printStackTrace();
		}
	}
}
//
/* // DL stop here implement
 * Runnable updatethread = new Runnable() { public void run() { try {
 * URL url = new URL(
 * "http://downloads.sourceforge.net/project/bitcoin/Bitcoin/blockchain/bitcoin_blockchain_170000.zip"
 * ); HttpURLConnection httpConnection = (HttpURLConnection)
 * (url.openConnection()); long completeFileSize =
 * httpConnection.getContentLength();
 * java.io.BufferedInputStream in = new
 * java.io.BufferedInputStream(httpConnection.getInputStream());
 * java.io.FileOutputStream fos = new java.io.FileOutputStream( "pinnable.zip");
 * java.io.BufferedOutputStream bout = new BufferedOutputStream( fos, 1024);
 * byte[] data = new byte[1024]; long downloadedFileSize = 0; int x = 0; while
 * ((x = in.read(data, 0, 1024)) >= 0) { downloadedFileSize += x;
 * // calculate progress final int currentProgress = (int)
 * ((((double)downloadedFileSize) / ((double)completeFileSize)) * 100000d);
 * // update progress bar SwingUtilities.invokeLater(new Runnable() {
 * @Override public void run() { jProgressBar.setValue(currentProgress); } });
 * bout.write(data, 0, x); } bout.close(); in.close(); } catch
 * (FileNotFoundException e) { } catch (IOException e) { } } }; */
