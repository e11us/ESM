package ellus.ESM.ESMW;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.WindowConstants;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.Source.SourceDouble;
import ellus.ESM.data.sys.UseLogger;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ESMW {
	// frame and ani driver
	protected JFrame				frame			= null;
	protected String				name			= null;
	protected boolean				Visable			= false;
	protected Color					bgColor			= null;
	protected int					w, h;
	protected Thread				animator		= null;
	// background panel  ( JPanel has higher FPS than JFXPanel.!!!!!! )
	protected ESMPanel				bgPanel			= null;
	protected ArrayList <ESMPanel>	subPanels		= new ArrayList <>();
	protected int					top				= 2;
	// some additional stat.
	private boolean					undecorated		= true;
	private boolean					fullScreen		= true;
	private boolean					autoClose		= false;
	private int						autoCloseTime	= 0;
	// fps counter.
	private int						renderWaitTime	= 10;																																																												// so dont init stall.
	private int						fps;
	private long					fpsTestLast		= helper.getTimeLong();
	protected int					fpsMax, fpsMin;
	protected SourceDouble			fpsSource		= null;
	// others.
	private boolean					renderGo		= true;

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor only for class extending this.
	||||--------------------------------------------------------------------------------------------*/
	public void init( SManXElm config ) {
		fpsMax= config.getAttr( AttrType._int, "WindowFPS_Max" ).getInteger();
		fpsMin= config.getAttr( AttrType._int, "WindowFPS_Min" ).getInteger();
		if( fpsMax > 200 || fpsMax < 10 ){
			fpsMax= 99;
			config.getAttr( AttrType._int, "WindowFPS_Max" ).setVal( fpsMax + "" );
		}
		if( fpsMin > 200 || fpsMin < 1 ){
			fpsMin= 66;
			config.getAttr( AttrType._int, "WindowFPS_Min" ).setVal( fpsMin + "" );
		}
		//
		if( config.getAttr( AttrType._boolean, "FullScreenMode" ).getBoolean() ){
			Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
			w= screenSize.width;
			h= screenSize.height;
			fullScreen= true;
		}else{
			w= config.getAttr( AttrType._int, "WindowWideth" ).getInteger();
			h= config.getAttr( AttrType._int, "WindowHeight" ).getInteger();
			fullScreen= false;
		}
		undecorated= config.getAttr( AttrType._boolean, "Undecorated" ).getBoolean();
		setFrame();
		//
		name= config.getAttr( AttrType._string, "Name" ).getString();
		bgColor= config.getAttr( AttrType._color, "BackgroundColor" ).getColor();
		autoClose= config.getAttr( AttrType._boolean, "WindowAutoCloseEnabled" ).getBoolean();
		autoCloseTime= config.getAttr( AttrType._int, "WindowAutoCloseTime(s)" ).getInteger();
		//
		frame.setVisible( true );
		startRender();
		//	protected boolean			autoScreenSaver					= false;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected void addPanel( ESMPanel comp ) {
		if( comp == null )
			return;
		//
		ESMPS PS= comp.PS;
		display.println( this.getClass().toString(), "new Component added: @" +
				PS.PanelX + "," + PS.PanelY + " - " + PS.PanelW + "x" + PS.PanelH +
				" level: " + PS.PanelLevel );
		subPanels.add( comp );
		//
		comp.setBounds( PS.PanelX, PS.PanelY, PS.PanelW, PS.PanelH );
		frame.getLayeredPane().add( comp, new Integer( ++top ) );
		//
		// add the frame. same time as add the panel.
		comp.frame= this;
		// request focus immediately.
		comp.requestFocus();
	}

	protected void bringMe2Top( ESMPanel comp ) {
		JLayeredPane JL= frame.getLayeredPane();
		Component[] LP= JL.getComponents();
		for( int i= 0; i < LP.length; i++ ){
			if( comp == LP[i] ){
				JL.remove( LP[i] );
				JL.add( comp, new Integer( ( ++top ) ) );
				break;
			}
		}
	}

	protected void hideAllSubPanel() {
		JLayeredPane JL= frame.getLayeredPane();
		Component[] LP= JL.getComponents();
		for( int i= 0; i < LP.length; i++ ){
			if( subPanels.contains( LP[i] ) )
				JL.remove( LP[i] );
		}
	}

	protected void showAllSubPanel() {
		JLayeredPane JL= frame.getLayeredPane();
		for( ESMPanel pan : subPanels ){
			JL.add( pan, new Integer( ++top ) );
		}
	}

	protected int getRenderWaitTime() {
		return renderWaitTime;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected void closeFrame() {
		UseLogger.log( name + " - closeFrame()" );
		stopRender();
		frame.setVisible( false );
		frame.dispatchEvent( new WindowEvent( frame,
				WindowEvent.WINDOW_CLOSING ) );
		frame= null;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public void miniFrame() {
		stopRender();
		UseLogger.log( name + " - miniFrame()" );
		frame.setExtendedState( Frame.ICONIFIED );
	}

	public void hideFrame() {
		frame.setVisible( false );
		stopRender();
	}

	public void maxFrame() {
		frame.setVisible( true );
		startRender();
		//
		UseLogger.log( name + " - maxFrame()" );
		frame.setExtendedState( Frame.MAXIMIZED_BOTH );
		frame.toFront();
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| create paint frame.
	||||--------------------------------------------------------------------------------------------*/
	private void setFrame() {
		this.frame= new JFrame();
		// set basic
		frame= new JFrame();
		// addd bgpanel.
		JLayeredPane LP= frame.getLayeredPane();
		LP.setPreferredSize( new Dimension( w, h ) );
		bgPanel.setBounds( 0, 0, w, h );
		LP.add( bgPanel, new Integer( 0 ) );
		//
		frame.setBackground( bgColor );
		frame.setSize( w, h );
		frame.setPreferredSize( new Dimension( 0, 0 ) );
		frame.setLocation( 0, 0 );
		// full screen.
		if( fullScreen )
			frame.setExtendedState( Frame.MAXIMIZED_BOTH );
		// untitled.
		if( undecorated ){
			frame.setUndecorated( undecorated );
		}
		frame.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		//frame.setExtendedState( frame.getExtendedState() | JFrame.MAXIMIZED_BOTH );// go into full screen.
		frame.setVisible( false );
		//
		// set mouse cursor.
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg;
		try{
			cursorImg= ImageIO
					.read( new File( SMan.getSetting( 0 ) + SMan.getSetting( 102 ) + "/C2s.png" ) );
			Cursor blankCursor= Toolkit.getDefaultToolkit().createCustomCursor(
					cursorImg, new Point( 0, 0 ), "def cursor" );
			bgPanel.setCursor( blankCursor );
		}catch ( IOException e ){
			e.printStackTrace();
		}
		//
		frame.addWindowListener( new WindowListener() {
			@Override
			public void windowActivated( WindowEvent arg0 ) {}

			@Override
			public void windowClosed( WindowEvent e ) {}

			@Override
			public void windowClosing( WindowEvent e ) {}

			@Override
			public void windowDeactivated( WindowEvent e ) {}

			@Override
			public void windowDeiconified( WindowEvent e ) {
				startRender();
			}

			@Override
			public void windowIconified( WindowEvent e ) {}

			@Override
			public void windowOpened( WindowEvent e ) {}
		} );
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| pull control.
	||||--------------------------------------------------------------------------------------------*/
	private void startRender() {
		renderGo= true;
		setAnimator();
		animator.start();
	}

	private void stopRender() {
		renderGo= false;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| if frame is visable, call child board to see changes, the GUI_input().
	||||--------------------------------------------------------------------------------------------*/
	private void setAnimator() {
		animator= new Thread( "animator-" + helper.rand32AN().substring( 0, 5 ) ) {
			@Override
			public void run() {
				while( renderGo ){
					try{
						// if board visable, call GUI to check any change in pinlist, then print it.
						if( frame != null ){
							//
							bgPanel.repaint();
							for( ESMPanel con : subPanels ){
								if( con.PS.closeWindow ){
									frame.getLayeredPane().remove( con );
								}else{
									con.repaint();
								}
							}
							//
							// fps counter.
							if( helper.getTimeLong() - fpsTestLast > 3000 ){
								fps= (int)fpsSource.getDouble();
								if( fps > fpsMax )
									renderWaitTime++ ;
								if( fps < fpsMin && renderWaitTime / 2 > 2 )
									renderWaitTime/= 2;
								else renderWaitTime= 2;
								fpsTestLast= helper.getTimeLong();
							}
							//
							if( autoClose && ( ( helper.getTimeLong() - bgPanel.PS.lastKeyboardInputTime )
									/ 1000.0 > autoCloseTime ) &&
									( ( helper.getTimeLong() - bgPanel.PS.lastMouseInputTime )
											/ 1000.0 > autoCloseTime ) ){
								closeFrame();
							}
							//
							sleep( renderWaitTime );
							//
							//   f.f( "rendering" );
						}
					}catch ( Exception ee ){}
				}
			}
		};
	}
}
