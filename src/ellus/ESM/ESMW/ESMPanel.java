package ellus.ESM.ESMW;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MouseInputAdapter;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.display;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleClickR;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardInput;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;
import ellus.ESM.pinnable.able_Interface.AbleMouseWheel;
import ellus.ESM.pinnable.able_Interface.AblePanelTitle;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ESMPanel extends JPanel {
	// basic.
	private ESMPanel			handler		= this;
	private JPanel				panel		= this;
	private SManXElm			config;
	//
	protected ESMPL				bgPL		= null;
	protected ESMPL				menuPL		= null;
	protected ArrayList <ESMPL>	Subpls		= new ArrayList <>();
	//
	protected ESMPS				PS			= null;
	protected ESMPD				drawer		= null;
	protected pinnable			highlighted	= null;
	protected pinnable 			titlePin= null;
	// draw this panel. ( change location. )
	protected boolean			dragPanelOn	= false;
	protected int				dragPanelOSX= 0;
	protected int				dragPanelOSY= 0;
	// for config uses.
	protected SManXElm			masterSE	= null;
	// others// print PL?
	protected boolean			paintPL		= true;
	protected ESMW frame= null;

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public ESMPanel( SManXElm config ) {
		// if no config, then dont create a PS.
		if( config != null ){
			this.config= config;
			PS= new ESMPS( this.config, panel );
			PS.setViewCenter( PS.ViewCenterX, PS.ViewCenterY );
		}
		bgPL= new ESMPL();
		drawer= new ESMPD();
		setBasePanel();
		setDefaultCursor();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public ESMPanel( int x, int y ) {
		// if no config, then dont create a PS.
		PS= new ESMPS( x, y, panel );
		bgPL= new ESMPL();
		drawer= new ESMPD();
		setBasePanel();
		setDefaultCursor();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public void resize( int x, int y ) {
		super.setBounds( PS.PanelX, PS.PanelY, x, y );
		PS.PanelH= y;
		PS.PanelW= x;
	}
	
	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public void bring2Top() {
		if( frame != null ) {
			frame.bringMe2Top( this );
		}
	}
	
	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	@Override
	protected void paintComponent( Graphics g ) {
		if( !paintPL )
			return;
		//
		// use this to clear color from layer below this panel.
		g.setColor( Color.black );
		g.fillRect( 0, 0, PS.getWidth(), PS.getHeight() );
		// reset all GUI interactive pin.
		PS.resetGUIactive();
		PS.resetGUIactiveLF();
		//  call layer pin to print all pins.
		drawer.setGraphic( g, panel );
		bgPL.printAllLayers( drawer, PS );
		if( Subpls.size() > 0 ){
			for( int i= 0; i < Subpls.size(); i++ ){
				ESMPL pl= Subpls.get( i );
				pl.printAllLayers( drawer, PS );
				// do this so no concurrent modification.error.
				if( !paintPL )
					return;
			}
		}
		// at last print menu PL if it exists. ( at top ).
		if( menuPL != null )
			menuPL.printAllLayers( drawer, PS );
		//
		// check other no-direct GUI input by pulling.
		HoverHighlight();
		if( PS.LastKey != null ){
			KeyboardInp( PS.LastKey );
			PS.LastKey= null;
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void setBasePanel() {
		//
		// ********************* mouse click *********************
		//
		panel.addMouseListener( new MouseInputAdapter() {
			@Override
			public void mouseClicked( MouseEvent e ) {
				PS.lastMouseInputTime= helper.getTimeLong();
				if( PS.MsgOutputTest )
					display.println( this.getClass().toString(), PS.name + " is focused" );
				 // request focus to the window upon mouse enter. && bring to top.
				panel.requestFocus();
				bring2Top();
				//
				if( e.getClickCount() == 2 ){
					switch( e.getButton() ){
						case 1 :
							CheckB1DoubleClick( e );
							break;
						case 3 :
							CheckB3DoubleClick( e );
							break;
					}
				}
			}

			@Override
			public void mouseEntered( MouseEvent e ) {
				display.println( this.getClass().toString(), PS.type + " " + PS.name + " is focused" );
				//panel.requestFocus(); // request focus to the window upon mouse enter.
				PS.lastMouseInputTime= helper.getTimeLong();
				PS.MouseLastPositionX= e.getX();
				PS.MouseLastPositionY= e.getY();
			}

			@Override
			public void mousePressed( MouseEvent e ) {
				PS.lastMouseInputTime= helper.getTimeLong();
					switch( e.getButton() ){
						case 1 :
							if( !windowDrag( e ) ){
								CheckB1Press( e );
							}
							break;
						case 3 :
							CheckB3Press( e );
							break;
					}
			}

			@Override
			public void mouseReleased( MouseEvent e ) {
				PS.lastMouseInputTime= helper.getTimeLong();
				switch( e.getButton() ){
					case 1 :
						if( handler.dragPanelOn ){
							PS.PanelX= e.getXOnScreen() + dragPanelOSX;
							PS.PanelY= e.getYOnScreen() + dragPanelOSY;
							handler.dragPanelOn= false;
							setDefaultCursor();
							// give the title input.
							windowTitleInput( e.getX(), e.getY() );
						}else{
							CheckB1Click( e );
						}
						break;
					case 3 :
						CheckB3Click( e );
						break;
				}
			}
		} );
		//
		// ********************* mouse motion *********************
		//
		panel.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved( MouseEvent e ) {
				PS.lastMouseInputTime= helper.getTimeLong();
				PS.MouseLastPositionX= e.getX();
				PS.MouseLastPositionY= e.getY();
				HoverHighlight();
			}

			@Override
			public void mouseDragged( MouseEvent e ) {
				 // request focus to the window upon mouse enter. && bring to top.
				panel.requestFocus();
				bring2Top();
				//
				PS.lastMouseInputTime= helper.getTimeLong();
				PS.MouseLastPositionX= e.getX();
				PS.MouseLastPositionY= e.getY();
				//
				if( handler.dragPanelOn ){
					panel.setBounds( dragPanelOSX + e.getXOnScreen(),
							dragPanelOSY + e.getYOnScreen(), PS.PanelW, PS.PanelH );
				}
			}
		} );
		// ********************* mouse wheel *********************
		//
		panel.addMouseWheelListener( new MouseWheelListener() {
			@Override
			public void mouseWheelMoved( MouseWheelEvent e ) {
				PS.lastMouseInputTime= helper.getTimeLong();
				checkB2Press( e );
			}
		} );
		//
		// *********************  window  *********************
		//
		panel.addComponentListener( new ComponentListener() {
			// repaint the center and content if the window is resized.
			@Override
			public void componentResized( ComponentEvent arg0 ) {}

			@Override
			public void componentMoved( ComponentEvent arg0 ) {}

			@Override
			public void componentShown( ComponentEvent arg0 ) {}

			@Override
			public void componentHidden( ComponentEvent arg0 ) {}
		} );
		panel.addFocusListener( new FocusListener() {
			@Override
			public void focusGained( FocusEvent arg0 ) {}

			@Override
			public void focusLost( FocusEvent e ) {}
		} );
		//
		// ********************* arrow key bind *********************
		//
		Action VK_LEFTk= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "AL";
				PS.lastKeyboardInputTime= helper.getTimeLong();
				PS.changeViewCenter( -PS.keyboardNavSpeed, 0, "keyboard" );
			}
		};
		Action VK_RIGHTk= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "AR";
				PS.lastKeyboardInputTime= helper.getTimeLong();
				PS.changeViewCenter( PS.keyboardNavSpeed, 0, "keyboard" );
			}
		};
		Action VK_UPk= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "AU";
				PS.lastKeyboardInputTime= helper.getTimeLong();
				PS.changeViewCenter( 0, -PS.keyboardNavSpeed, "keyboard" );
			}
		};
		Action VK_DOWNk= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "AD";
				PS.lastKeyboardInputTime= helper.getTimeLong();
				PS.changeViewCenter( 0, PS.keyboardNavSpeed, "keyboard" );
			}
		};
		//
		// ********************* keyboard general bind *********************
		//
		Action ActivateAction= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				if( ( e.getActionCommand().charAt( 0 ) ) == 27 ){}
				PS.LastKey= e.getActionCommand();
				PS.lastKeyboardInputTime= helper.getTimeLong();
				return;
			}
		};
		// all control press with key. start with C. ex: CA, CB, CQ, CW
		Action inpC= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "C" + (char) ( e.getActionCommand().charAt( 0 ) + 64 );
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		//
		// ********************* Function key bind (F1-F12) *********************
		//
		Action VK_F1= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F1";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F2= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F2";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F3= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F3";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F4= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F4";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F5= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F5";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F6= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F6";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F7= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F7";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F8= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F8";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F9= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F9";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F10= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F10";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F11= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F11";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_F12= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "F12";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		//
		// ********************* other special key *********************
		//
		Action VK_Home= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VHO";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_End= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VEN";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_PAGE_UP= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VPU";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_PAGE_DOWN= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VPD";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_DELETE= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VDE";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_TAB= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VTA";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_CAPS_LOCK= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VCL";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		Action VK_CONTEXT_MENU= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				PS.LastKey= "VCM";
				PS.lastKeyboardInputTime= helper.getTimeLong();
			}
		};
		//
		// ********************* all key bind hook with GUI event *********************
		//
		// this keyboard hook all printable char.
		for( int i= 33; i < 127; i++ ){
			panel.getInputMap().put(
					KeyStroke.getKeyStroke( (char)i ), i + "key" );
			panel.getActionMap().put( i + "key", ActivateAction );
		}
		// this hook all keyboard with ctrl pressed.
		for( int i= 65; i <= ( 64 + 26 ); i++ ){
			panel.getInputMap().put(
					KeyStroke.getKeyStroke( i, InputEvent.CTRL_MASK ), "control_I" + i );
			panel.getActionMap().put( "control_I" + i, inpC );
		}
		// space key.
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, 0 ), "space-" );
		panel.getActionMap().put( "space-", ActivateAction );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_SPACE, InputEvent.SHIFT_MASK ), "space-" );
		panel.getActionMap().put( "space-", ActivateAction );
		// this the key hook for esc key.
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "VK_ESCAPE" );
		panel.getActionMap().put( "VK_ESCAPE", ActivateAction );
		// this the key hook for enter
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_ENTER, 0 ), "VK_ENTER" );
		panel.getActionMap().put( "VK_ENTER", ActivateAction );
		// this the key hook for back space ( delete )
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), "VK_BACK_SPACE" );
		panel.getActionMap().put( "VK_BACK_SPACE", ActivateAction );
		// this the key hook for arrow key.
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, 0 ), "VK_LEFT" );
		panel.getActionMap().put( "VK_LEFT", VK_LEFTk );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, 0 ), "VK_RIGHT" );
		panel.getActionMap().put( "VK_RIGHT", VK_RIGHTk );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0 ), "VK_UP" );
		panel.getActionMap().put( "VK_UP", VK_UPk );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0 ), "VK_DOWN" );
		panel.getActionMap().put( "VK_DOWN", VK_DOWNk );
		// this hook the functionkey. F1-12.
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F1, 0 ), "VK_F1" );
		panel.getActionMap().put( "VK_F1", VK_F1 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F2, 0 ), "VK_F2" );
		panel.getActionMap().put( "VK_F2", VK_F2 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F3, 0 ), "VK_F3" );
		panel.getActionMap().put( "VK_F3", VK_F3 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F4, 0 ), "VK_F4" );
		panel.getActionMap().put( "VK_F4", VK_F4 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F5, 0 ), "VK_F5" );
		panel.getActionMap().put( "VK_F5", VK_F5 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F6, 0 ), "VK_F6" );
		panel.getActionMap().put( "VK_F6", VK_F6 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F7, 0 ), "VK_F7" );
		panel.getActionMap().put( "VK_F7", VK_F7 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F8, 0 ), "VK_F8" );
		panel.getActionMap().put( "VK_F8", VK_F8 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F9, 0 ), "VK_F9" );
		panel.getActionMap().put( "VK_F9", VK_F9 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F10, 0 ), "VK_F10" );
		panel.getActionMap().put( "VK_F10", VK_F10 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F11, 0 ), "VK_F11" );
		panel.getActionMap().put( "VK_F11", VK_F11 );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_F12, 0 ), "VK_F12" );
		panel.getActionMap().put( "VK_F12", VK_F12 );
		// other special key.
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_HOME, 0 ), "VK_Home" );
		panel.getActionMap().put( "VK_Home", VK_Home );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_END, 0 ), "VK_End" );
		panel.getActionMap().put( "VK_End", VK_End );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_UP, 0 ), "VK_PAGE_UP" );
		panel.getActionMap().put( "VK_PAGE_UP", VK_PAGE_UP );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_PAGE_DOWN, 0 ), "VK_PAGE_DOWN" );
		panel.getActionMap().put( "VK_PAGE_DOWN", VK_PAGE_DOWN );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_DELETE, 0 ), "VK_DELETE" );
		panel.getActionMap().put( "VK_DELETE", VK_DELETE );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_TAB, 0 ), "VK_TAB" );
		panel.getActionMap().put( "VK_TAB", VK_TAB );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_CAPS_LOCK, 0 ), "VK_CAPS_LOCK" );
		panel.getActionMap().put( "VK_CAPS_LOCK", VK_CAPS_LOCK );
		panel.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_CONTEXT_MENU, 0 ), "VK_CONTEXT_MENU" );
		panel.getActionMap().put( "VK_CONTEXT_MENU", VK_CONTEXT_MENU );
		//
		// make this board ( JPanel ) focusable.
		panel.setFocusable( true );
		//
		return;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| GUI related.
	||||--------------------------------------------------------------------------------------------*/
	protected synchronized void checkB2Press( MouseWheelEvent e ) {
		int x, y;
		x= PS.b2wX( e.getX() );
		y= PS.b2wY( e.getY() );
		for( pinnable pin : PS.getGUIactive() ){
			if( pin.isWithIn( x, y ) && pin instanceof AbleMouseWheel ){
				( (AbleMouseWheel)pin ).WheelRotateAction( e.getWheelRotation() );
				return;
			}
		}
		checkB2PressNGE( e );
	}

	protected void checkB2PressNGE( MouseWheelEvent e ) {}

	protected synchronized void CheckB1Press( MouseEvent e ) {
		PS.MouseB1PressX= e.getX();
		PS.MouseB1PressY= e.getY();
		int x, y;
		x= PS.b2wX( e.getX() );
		y= PS.b2wY( e.getY() );
		boolean clearAll= false;
		for( pinnable pin : PS.getGUIactive() ){
			if( pin.isWithIn( x, y ) && pin instanceof AbleMouseDrag ){
				( (AbleMouseDrag)pin ).MouseDragOn( new cor2D( PS.b2wX( e.getX() ), PS.b2wY( e.getY() ) ) );
				PS.MouseB1_IteamDraged= ( (AbleMouseDrag)pin );
				clearAll= true;
				break;
			}
		}
		if( clearAll ){
			// unhighlight all others.
			ArrayList <pinnable> pins= (ArrayList <pinnable>)PS.getGUIactive().clone();
			ArrayList <pinnable> pinsLF= (ArrayList <pinnable>)PS.getGUIactiveLF().clone();
			for( pinnable pin : pins )
				if( pin instanceof AbleClickHighlight )
					( (AbleClickHighlight)pin ).B1clickHighlightOff( 0, 0 );
			for( pinnable pin : pinsLF )
				if( pin instanceof AbleClickHighlight )
					( (AbleClickHighlight)pin ).B1clickHighlightOff( 0, 0 );
		}else CheckB1PressNGE( e );
	}

	protected synchronized void CheckB1PressNGE( MouseEvent e ) {}

	protected synchronized void CheckB1Click( MouseEvent e ) {
		//
		int x, y, xx, yy;
		x= PS.b2wX( PS.MouseB1PressX );
		y= PS.b2wY( PS.MouseB1PressY );
		xx= PS.b2wX( e.getX() );
		yy= PS.b2wY( e.getY() );
		ArrayList <pinnable> pins= (ArrayList <pinnable>)PS.getGUIactive().clone();
		ArrayList <pinnable> pinsLF= (ArrayList <pinnable>)PS.getGUIactiveLF().clone();
		// test drag first.
		if( PS.MouseB1_IteamDraged != null ){
			PS.MouseB1_IteamDraged.MouseDragOff( new cor2D( xx, yy ) );
			// threshold for click, and drag.
			if( Math.abs( ( x - xx ) * ( y - yy ) ) < 10
					&& PS.MouseB1_IteamDraged instanceof AbleClickHighlight ){
				highlighted= (pinnable)PS.MouseB1_IteamDraged;
				( (AbleClickHighlight)PS.MouseB1_IteamDraged ).B1clickHighlightOn( xx, yy );
			}
			PS.MouseB1_IteamDraged= null;
			return;
		}
		// threshold for click press and release distance difference.
		if( Math.abs( ( x - xx ) * ( y - yy ) ) > 10 )
			return;
		// reset highlighted.
		highlighted= null;
		boolean checkClick= true, checkHighL= true;
		// check regular GUI.
		for( pinnable pin : pins ){
			if( ( checkClick || checkHighL ) && pin.isWithIn( xx, yy ) ){
				if( checkClick && pin instanceof AbleClick ){
					( (AbleClick)pin ).B1clickAction( xx, yy );
					checkClick= false;;
				}
				if( checkHighL && pin instanceof AbleClickHighlight ){
					( (AbleClickHighlight)pin ).B1clickHighlightOn( xx, yy );
					highlighted= pin;
					checkHighL= false;
				}
			}else if( pin instanceof AbleClickHighlight ){
				( (AbleClickHighlight)pin ).B1clickHighlightOff( 0, 0 );
			}
		}
		// always check LF last.
		x= PS.MouseB1PressX;
		y= PS.MouseB1PressY;
		xx= e.getX();
		yy= e.getY();
		for( pinnable pin : pinsLF ){
			if( ( checkClick || checkHighL ) && pin.isWithIn( xx, yy ) ){
				// click only click first one. and highlight first one.
				if( checkClick && pin instanceof AbleClick ){
					( (AbleClick)pin ).B1clickAction( xx, yy );
					checkClick= false;;
				}
				if( checkHighL && pin instanceof AbleClickHighlight ){
					( (AbleClickHighlight)pin ).B1clickHighlightOn( xx, yy );
					highlighted= pin;
					checkHighL= false;
				}else if( pin instanceof AbleClickHighlight )
					( (AbleClickHighlight)pin ).B1clickHighlightOff( 0, 0 );
			}else if( pin instanceof AbleClickHighlight )
				( (AbleClickHighlight)pin ).B1clickHighlightOff( 0, 0 );
		}
		//
		// always clear menu at end of click check.
		menuPL= null;
		//
		if( checkClick && checkHighL ){
			CheckB1ClickNGE( e );
		}
		//f.f( "ML hl: " + highlighted );
	}

	protected synchronized void CheckB1ClickNGE( MouseEvent e ) {}

	protected synchronized void CheckB3Press( MouseEvent e ) {
		PS.MouseB3PressX= e.getX();
		PS.MouseB3PressY= e.getY();
		//
		if( PS.mouseDragNavEnable ){}
		//
	}

	protected synchronized void CheckB3Click( MouseEvent e ) {
		int x, y, xx, yy;
		x= PS.b2wX( PS.MouseB3PressX );
		y= PS.b2wY( PS.MouseB3PressY );
		xx= PS.b2wX( e.getX() );
		yy= PS.b2wY( e.getY() );
		ArrayList <pinnable> pins= (ArrayList <pinnable>)PS.getGUIactive().clone();
		ArrayList <pinnable> pinsLF= (ArrayList <pinnable>)PS.getGUIactiveLF().clone();
		// threshold for click press and release distance difference.
		if( Math.abs( ( x - xx ) * ( y - yy ) ) > 10 )
			return;
		boolean checkClick= true;
		// check regular GUI.
		for( pinnable pin : pins ){
			if( checkClick && pin.isWithIn( x, y ) && pin.isWithIn( xx, yy ) && pin instanceof AbleClickR ){
				// click only click first one. and highlight first one.
				( (AbleClickR)pin ).B3clickAction( xx, yy );
				checkClick= false;
				break;
			}
		}
		// always check LF last.
		x= PS.MouseB1PressX;
		y= PS.MouseB1PressY;
		xx= e.getX();
		yy= e.getY();
		for( pinnable pin : pinsLF ){
			if( checkClick && pin.isWithIn( x, y ) && pin.isWithIn( xx, yy ) && pin instanceof AbleClickR ){
				// click only click first one. and highlight first one.
				( (AbleClickR)pin ).B3clickAction( xx, yy );
				checkClick= false;;
			}
		}
		//
		// always clear menu at end of click check.
		menuPL= null;
		//
		if( checkClick ){
			CheckB3ClickNGE( e );
		}
	}

	protected synchronized void CheckB3ClickNGE( MouseEvent e ) {
		// def action is close window.
		closePanel();
	}

	protected synchronized void CheckB1DoubleClick( MouseEvent e ) {
		int xx= PS.b2wX( e.getX() );
		int yy= PS.b2wY( e.getY() );
		ArrayList <pinnable> pins= (ArrayList <pinnable>)PS.getGUIactive().clone();
		ArrayList <pinnable> pinsLF= (ArrayList <pinnable>)PS.getGUIactiveLF().clone();
		// check regular GUI.
		for( pinnable pin : pins ){
			if( pin.isWithIn( xx, yy ) && pin instanceof AbleDoubleClick ){
				// click only click first one. and highlight first one.
				( (AbleDoubleClick)pin ).B1DoubleClickAction( xx, yy );
				return;
			}
		}
		// always check LF last.
		for( pinnable pin : pinsLF ){
			if( pin.isWithIn( xx, yy ) && pin instanceof AbleDoubleClick ){
				// click only click first one. and highlight first one.
				( (AbleDoubleClick)pin ).B1DoubleClickAction( xx, yy );
				return;
			}
		}
	}

	protected synchronized void CheckB3DoubleClick( MouseEvent e ) {
		// this should be empty.
	}

	protected synchronized void HoverHighlight() {
		int xx, yy;
		xx= PS.b2wX( PS.MouseLastPositionX );
		yy= PS.b2wY( PS.MouseLastPositionY );
		for( pinnable pin : PS.getGUIactive() ){
			if( pin instanceof AbleHoverHighlight ){
				if( pin.isWithIn( xx, yy ) )
					( (AbleHoverHighlight)pin ).HoverHighlightOn();
				else ( (AbleHoverHighlight)pin ).HoverHighlightOff();
			}
		}
		// now check for LF pin.
		xx= PS.MouseLastPositionX;
		yy= PS.MouseLastPositionY;
		for( pinnable pin : PS.getGUIactiveLF() ){
			if( pin instanceof AbleHoverHighlight ){
				if( pin.isWithIn( xx, yy ) )
					( (AbleHoverHighlight)pin ).HoverHighlightOn();
				else ( (AbleHoverHighlight)pin ).HoverHighlightOff();
			}
		}
	}

	protected boolean windowDrag( MouseEvent e ) {
		// dont have config. dont have title.
		if( config == null || !PS.windowDragByTitleEnable )
			return false;
		// test it.
		if( e.getY() < PS.windowTitleHeight ){
			this.dragPanelOn= true;
			this.dragPanelOSX= PS.PanelX - e.getXOnScreen();
			this.dragPanelOSY= PS.PanelY - e.getYOnScreen();
			setBlankCursor();
			return true;
		}else return false;
	}
	
	protected void windowTitleInput( int x, int y ) {
		if( titlePin != null && titlePin instanceof  AblePanelTitle ) {
			if( ( (AblePanelTitle)titlePin ).isCloseClicked( x, y ) ){
				closePanel();
			}
		}
	}

	// should always be overridden.
	protected synchronized void KeyboardInp( String lk ) {
		/*
		f.f( "--KE hl: " + highlighted );
		//
		still have highlight turnned off problem..
		*/
		if( highlighted != null ){
			// always check esc first.
			//if( lk.length() == 1 && (int)lk.charAt( 0 ) == 27 ){
			// if there is highlighted, check keyboard for that.
			if( highlighted instanceof AbleKeyboardInput && lk.length() == 1 ){
				( (AbleKeyboardInput)highlighted ).keyboardInp( lk );
			}else if( highlighted instanceof AbleKeyboardFunInp && lk.length() > 1 ){
				( (AbleKeyboardFunInp)highlighted ).FunInp( lk );
			}
		}else{
			KeyboardInpNGE( lk );
		}
	}

	protected synchronized void KeyboardInpNGE( String lk ) {
		if( lk.length() == 1 ){
			switch( lk.charAt( 0 ) ){
				case 27 :
					closePanel();
					break;
			}
		}
	}

	protected void setBlankCursor() {
		BufferedImage cursorImg= new BufferedImage( 16, 16, BufferedImage.TYPE_INT_ARGB );
		Cursor blankCursor= Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new Point( 0, 0 ), "blank cursor" );
		panel.setCursor( blankCursor );
	}

	protected void setDefaultCursor() {
		BufferedImage cursorImg;
		try{
			cursorImg= ImageIO
					.read( new File( SMan.getSetting( 0 ) + SMan.getSetting( 102 ) + "/C2s.png" ) );
			Cursor blankCursor= Toolkit.getDefaultToolkit().createCustomCursor(
					cursorImg, new Point( 0, 0 ), "def cursor" );
			panel.setCursor( blankCursor );
		}catch ( IOException e ){}
	}

	protected void closePanel() {
		PS.closeWindow= true;
		PS.reWrite();
	}
}
/*||----------------------------------------------------------------------------------------------
||| the default reload.
||||--------------------------------------------------------------------------------------------*/