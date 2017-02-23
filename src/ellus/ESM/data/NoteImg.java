package ellus.ESM.data;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.helper;
import ellus.ESM.setting.SCon;



public class NoteImg {
	private String	filePath= null;
	Object[]		para;
	//para= { id, x, y, image, w, h }

	public NoteImg( String filePath, Object[] inp ) {
		this.filePath= filePath;
		this.para= inp;
	}

	public void init( String id, String folder, int x, int y ) {
		para[0]= id;
		para[1]= x;
		para[2]= y;
		para[3]= helper.getImage( filePath );
		// dup file.
		try{
			Files.copy( new File( filePath ).toPath(), new File( folder + "/" + id + " " + x
					+ " " + y + " " + SCon.ExtpinImg ).toPath() );
		}catch ( IOException e ){
			e.printStackTrace();
			display.printErr( this.getClass().toGenericString(), "error copy file" );
			return;
		}
		filePath= folder + "/" + id + " " + x
				+ " " + y + " " + SCon.ExtpinImg;
	}

	public void load() {
		readFromDisk();
	}

	public void store() {
		new File( filePath ).renameTo( new File( ( new File( filePath ) ).getParent() + "/" +
				(String)para[0] + " " + (int)para[1] + " " + (int)para[2] + " " + SCon.ExtpinImg ) );
		filePath= ( new File( filePath ) ).getParent() + "/" +
				(String)para[0] + " " + (int)para[1] + " " + (int)para[2] + " " + SCon.ExtpinImg;
	}

	private void readFromDisk() {
		try{
			para[3]= ImageIO.read( new File( filePath ) );
			Scanner rdr= new Scanner( helper.getFileName( new File( filePath ).getName() ) );
			if( rdr.hasNext() )
				para[0]= rdr.next();
			else return;
			if( rdr.hasNextInt() )
				para[1]= new Integer( rdr.nextInt() );
			else return;
			if( rdr.hasNextInt() )
				para[2]= new Integer( rdr.nextInt() );
			else return;
		}catch ( IOException e ){
			e.printStackTrace();
			return;
		}
		para[4]= ( (Image)para[3] ).getWidth( new JPanel() );
		para[5]= ( (Image)para[3] ).getHeight( new JPanel() );
	}

	public void delete() {
		if( filePath != null )
			;
		File ff= new File( filePath );
		if( ff.exists() && ff.isFile() )
			ff.delete();
	}
}
