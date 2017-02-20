package ellus.ESM.data.sys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SMan;



public class BackupEr {
	public boolean backupIng= false;

	public static void backupIfNeeded() {
		backupThread bker= new backupThread( false );
		// non thread.
		bker.run();
	}

	public static void backupNow() {
		backupThread bker= new backupThread( true );
		// thread.
		bker.start();
	}
}

class backupThread extends Thread {
	private boolean force= false;

	public backupThread( boolean force ) {
		this.force= force;
	}

	@Override
	public void run() {
		display.println( this.getClass().toString(), "backup is started." );
		//
		File backupFolder= new File( SMan.getSetting( 0 ) + SMan.getSetting( 105 ) );
		if( !backupFolder.exists() )
			backupFolder.mkdirs();
		//
		Scanner rdr= new Scanner( SMan.getSetting( 1111 ) );
		backupZiper ziper= new backupZiper();
		File bk;
		while( rdr.hasNextInt() ){
			bk= new File( SMan.getSetting( 0 ) + SMan.getSetting( rdr.nextInt() ) );
			backupFolder= new File( SMan.getSetting( 0 ) + SMan.getSetting( 105 ) +
					"/" + helper.getCurrentDateStamp() + " - " + bk.getName() + ".zip" );
			if( !force ){
				if( !backupFolder.exists() )
					ziper.zipIt( bk.toString(), backupFolder.toString() );
				else display.println( this.getClass().toString(), bk.toString() + " backup of today exists." );
			}else{
				ziper.zipIt( bk.toString(), SMan.getSetting( 0 ) + SMan.getSetting( 105 ) +
						"/" + helper.getCurrentTimeStamp() + " - " + bk.getName() + ".zip" );
			}
		}
		display.println( this.getClass().toString(), "backup is Ended." );
	}
}

class backupZiper {
	public void zipIt( String sourc, String zipFile ) {
		//
		List <String> fileList= new ArrayList <>();
		generateFileList( new File( sourc ), fileList, sourc );
		//
		byte[] buffer= new byte[1024];
		try{
			FileOutputStream fos= new FileOutputStream( zipFile );
			ZipOutputStream zos= new ZipOutputStream( fos );
			//System.out.println( "Output to Zip : " + zipFile );
			for( String file : fileList ){
				//System.out.println( "File Added : " + file );
				ZipEntry ze= new ZipEntry( file );
				zos.putNextEntry( ze );
				FileInputStream in= new FileInputStream( sourc + File.separator + file );
				int len;
				while( ( len= in.read( buffer ) ) > 0 ){
					zos.write( buffer, 0, len );
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
		}catch ( IOException ex ){
			ex.printStackTrace();
		}
	}

	private void generateFileList( File node, List <String> fileList, String SOURCE_FOLDER ) {
		//add file only
		if( node.isFile() ){
			fileList.add( generateZipEntry( node.getAbsoluteFile().toString(), SOURCE_FOLDER ) );
		}
		if( node.isDirectory() ){
			String[] subNote= node.list();
			for( String filename : subNote ){
				generateFileList( new File( node, filename ), fileList, SOURCE_FOLDER );
			}
		}
	}

	private String generateZipEntry( String file, String SOURCE_FOLDER ) {
		return file.substring( SOURCE_FOLDER.length() + 1, file.length() );
	}
}
