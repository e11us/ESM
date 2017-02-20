package ellus.ESM.roboSys;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;



/* -----------------------------------------------------------------------------
 * get content form system clipboard.
 * use:
 * 		first new a instant of this.
 * 		then use the static getString() to get current contant from sys clipboard.
 * -----------------------------------------------------------------------------
 */
public class clipBoard extends TimerTask {
	private static Clipboard	tk	= null;
	private static Timer		tm	= null;
	private static String		data= null;

	public clipBoard() {
		if( tk == null ){
			tk= Toolkit.getDefaultToolkit().getSystemClipboard();
			tm= new Timer( "ClipBoard Timer" );
			tm.schedule( this, 0, 333 );
		}
	}

	public static String getString() {
		if( tk == null )
			new clipBoard();
		return data;
	}

	public static void setString( String inp ) {
		try{
			StringSelection stringSelection= new StringSelection( inp );
			tk.setContents( stringSelection, null );
		}catch ( Exception ee ){
			ee.printStackTrace();
		}
	}

	@Override
	public void run() {
		try{
			data= (String)tk.getData( DataFlavor.stringFlavor );
		}catch ( UnsupportedFlavorException e ){}catch ( IOException e ){}
	}
}
