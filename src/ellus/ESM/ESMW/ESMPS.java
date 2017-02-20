package ellus.ESM.ESMW;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JPanel;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ESMPS {
	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	/*
	public boolean isFullScreen() {
		return this.FullScreen;
	}

	public boolean isTitleDecorated() {
		return this.titleDecorated;
	}

	public boolean isTranslucentPanel() {
		return this.translucentPane;
	}
	*/
	public int b2wX( int x ) {
		return ViewOrignX + x;
	}

	public int b2wY( int y ) {
		return ViewOrignY + y;
	}

	public int w2bX( int x ) {
		return x - ViewOrignX;
	}

	public int w2bY( int y ) {
		return y - ViewOrignY;
	}

	public int getWidth() {
		return this.PanelW;
	}

	public int getHeight() {
		return this.PanelH;
	}

	public int ViewOriginX() {
		return ViewOrignX;
	}

	public int ViewOriginY() {
		return ViewOrignY;
	}
	
	public void changeViewCenterP( int x, int y ) {
		changeViewCenter( x, y , null );
	}

	public int mouseCurrentX() {
		return this.MouseLastPositionX;
	}

	public int mouseCurrentY() {
		return this.MouseLastPositionY;
	}

	public int getImageWidth( Image image ) {
		if( panel == null )
			return 0;
		return image.getWidth( panel );
	}

	public int getImageHeight( Image image ) {
		if( panel == null )
			return 0;
		return image.getHeight( panel );
	}

	public String printScreenViewSetting() {
		return "ViewCenter: (" + ViewCenterX + "," + ViewCenterY +
				") - ViewOrigin: (" + ViewOrignX + "," + ViewOrignY + ")";
	}

	public void addGUIactive( pinnable pin ) {
		pinsGUIinteractive.add( pin );
	}

	public ArrayList <pinnable> getGUIactive() {
		ArrayList <pinnable> ret= new ArrayList <>();
		// reverse first so lastest print at top get input test first.
		if( pinsGUIinteractive != null ){
			for( int i= pinsGUIinteractive.size() - 1; i >= 0; i-- ){
				ret.add( pinsGUIinteractive.get( i ) );
			}
		}
		return ret;
	}

	public void addGUIactiveLF( pinnable pin ) {
		pinsGUIinteractiveLF.add( pin );
	}

	public ArrayList <pinnable> getGUIactiveLF() {
		ArrayList <pinnable> ret= new ArrayList <>();
		// reverse first so lastest print at top get input test first.
		if( pinsGUIinteractiveLF != null ){
			for( int i= pinsGUIinteractiveLF.size() - 1; i >= 0; i-- ){
				ret.add( pinsGUIinteractiveLF.get( i ) );
			}
		}
		return ret;
	}

	public void resetGUIactiveLF() {
		pinsGUIinteractiveLF= new ArrayList <>();
	}

	public void resetGUIactive() {
		pinsGUIinteractive= new ArrayList <>();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected ESMPS( int w, int h, JPanel jp ) {
		PanelW= w;
		PanelH= h;
		this.panel= jp;
		this.setViewCenter( 0, 0 );
		pinsGUIinteractive= new ArrayList <>();
		pinsGUIinteractiveLF= new ArrayList <>();
		this.lastMouseInputTime= helper.getTimeLong();
		this.lastKeyboardInputTime= helper.getTimeLong();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected ESMPS( SManXElm config, JPanel jp ) {
		this.config= config;
		this.panel= jp;
		pinsGUIinteractive= new ArrayList <>();
		pinsGUIinteractiveLF= new ArrayList <>();
		this.lastMouseInputTime= helper.getTimeLong();
		this.lastKeyboardInputTime= helper.getTimeLong();
		//
		name= config.getName();
		type= config.getType();
		//
		PanelW= config.getAttr( AttrType._int, "PanelWidth" ).getInteger();
		PanelH= config.getAttr( AttrType._int, "PanelHeight" ).getInteger();
		PanelX= config.getAttr( AttrType._int, "PanelX" ).getInteger();
		PanelY= config.getAttr( AttrType._int, "PanelY" ).getInteger();
		PanelLevel= config.getAttr( AttrType._int, "PanelLevel" ).getInteger();
		//
		if( PanelW == 0 && PanelH == 0 ){
			Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
			PanelW= (int) ( screenSize.width / 2.0 );
			PanelH= (int) ( screenSize.height / 2.0 );
			config.getAttr( AttrType._int, "PanelWidth" ).setVal( PanelW + "" );
			config.getAttr( AttrType._int, "PanelHeight" ).setVal( PanelH + "" );
		}
		//
		ViewCenterX= config.getAttr( AttrType._int, "InitialViewCenterX" ).getInteger();
		ViewCenterY= config.getAttr( AttrType._int, "InitialViewCenterY" ).getInteger();
		contLocationViewChangeLockEnable= config.getAttr( AttrType._boolean, "InitialContLocationViewChangeLockEnable" )
				.getBoolean();
		contLocationViewChangeLockDis= config.getAttr( AttrType._int, "InitialContLocationViewChangeLockDis" )
				.getInteger();
		this.changeViewCenter( this.ViewCenterX, this.ViewCenterY, null );
		//
		mousewheelNavEnable= config.getAttr( AttrType._boolean, "InitialMousewheelNavEnable" ).getBoolean();
		keyboardNavEnable= config.getAttr( AttrType._boolean, "InitialKeyboardNavEnable" ).getBoolean();
		mouseWheelNavSpeed= config.getAttr( AttrType._int, "InitialMouseWheelNavSpeed" ).getInteger();
		keyboardNavSpeed= config.getAttr( AttrType._int, "InitialKeyboardNavSpeed" ).getInteger();
		mouseDragNavEnable= config.getAttr( AttrType._boolean, "InitialMouseDragNavEnable" ).getBoolean();
		//
		this.useAlignment= config.getAttr( AttrType._boolean, "UsePinnableAlignment" ).getBoolean();
		this.alignmentX= config.getAttr( AttrType._int, "AlignmentGridX" ).getInteger();
		this.alignmentY= config.getAttr( AttrType._int, "AlignmentGridY" ).getInteger();
		//
		this.windowDragByTitleEnable= config.getAttr( AttrType._boolean, "InitialWindowDragEnable" ).getBoolean();
		this.windowTitleHeight= config.getAttr( AttrType._int, "WindowTitleHeight" ).getInteger();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	protected void reWrite() {
		config.getAttr( AttrType._int, "PanelWidth" ).setVal( this.PanelW + "" );
		config.getAttr( AttrType._int, "PanelHeight" ).setVal( this.PanelH + "" );
		config.getAttr( AttrType._int, "PanelX" ).setVal( this.PanelX + "" );
		config.getAttr( AttrType._int, "PanelY" ).setVal( this.PanelY + "" );
		config.getAttr( AttrType._int, "PanelLevel" ).setVal( this.PanelLevel + "" );
	}

	// =========================================.must set.===================================
	//
	// name and type of this.
	protected String				name							= "-";
	protected String				type							= "-";
	// panel inside frame locaion. and level.
	protected int					PanelX							= 0;
	protected int					PanelY							= 0;
	protected int					PanelW							= 0;
	protected int					PanelH							= 0;
	protected int					PanelLevel;
	protected float					transparentRate					= 1f;
	// view and origin.
	protected int					ViewCenterX						= 0;
	protected int					ViewCenterY						= 0;
	protected int					ViewOrignX						= 0;
	protected int					ViewOrignY						= 0;
	// content lock. ( max content xy bound )
	protected boolean				contLocationViewChangeLockEnable= false;
	protected int					contLocationViewChangeLockDis	= 5;
	// UI setting related.
	protected boolean				mousewheelNavEnable				= false;
	protected int					mouseWheelNavSpeed				= 35;
	protected boolean				keyboardNavEnable				= false;
	protected int					keyboardNavSpeed				= 5;
	protected boolean				mouseDragNavEnable				= false;
	// window panel related.
	protected boolean				windowDragByTitleEnable= false;
	protected int 					windowTitleHeight= 0;
	//
	// =======================================.optional set.=================================
	//
	// view related.
	protected int					contLocationViewChangeLockXmin	= 0;
	protected int					contLocationViewChangeLockXmax	= 0;
	protected int					contLocationViewChangeLockYmin	= 0;
	protected int					contLocationViewChangeLockYmax	= 0;
	protected int					ViewXmin						= 0;
	protected int					ViewXmax						= 0;
	protected int					ViewYmin						= 0;
	protected int					ViewYmax						= 0;
	// msg output.
	protected boolean				MsgOutputTest					= false;
	protected boolean				MsgOutputNorm					= false;
	protected boolean				MsgOutputErro					= false;
	//
	protected boolean				useAlignment					= false;
	protected int					alignmentX						= 0;
	protected int					alignmentY						= 0;
	//
	//=======================================.GUI event.=================================
	//
	protected long					lastMouseInputTime				= helper.getTimeLong();
	protected long					lastKeyboardInputTime			= helper.getTimeLong();
	protected String				LastKey							= null;
	protected int					MouseLastPositionX				= 0;
	protected int					MouseLastPositionY				= 0;
	protected AbleMouseDrag			MouseB1_IteamDraged				= null;
	protected int					MouseB1PressX					= 0;
	protected int					MouseB1PressY					= 0;
	protected int					MouseB3PressX					= 0;
	protected int					MouseB3PressY					= 0;
	//
	//=======================================.Run Time.=================================
	//
	protected boolean				closeWindow						= false;
	//
	private SManXElm				config;
	private ArrayList <pinnable>	pinsGUIinteractive				= new ArrayList <>();
	private ArrayList <pinnable>	pinsGUIinteractiveLF			= new ArrayList <>();
	private JPanel					panel							= null;

	//
	/*||----------------------------------------------------------------------------------------------
	 |||  change view.
	||||--------------------------------------------------------------------------------------------*/
	protected void setViewCenter( int x, int y ) {
		ViewCenterX= y;
		ViewCenterY= x;
		setViewOrign();
	}

	protected void changeViewCenter( int x, int y, String source ) {
		if( source != null )
			switch( source ){
				case "mouseWheel" :
					if( this.mousewheelNavEnable )
						y*= this.mouseWheelNavSpeed;
					else return;
					break;
				case "keyboard" :
					if( this.keyboardNavEnable )
						y*= this.keyboardNavSpeed;
					else return;
					break;
			}
		ViewCenterY+= y;
		ViewCenterX+= x;
		setViewOrign();
		// lock only work for view change.
		// when no lock, change view freely.
		// when lock enable, only change view if there are parts not seen, and change view with lock so that there is always some part seen.
		//
		// and if that axis is changed.
		if( y != 0 && contLocationViewChangeLockEnable ){
			// test if scroll is needed.
			if( contLocationViewChangeLockYmax - contLocationViewChangeLockYmin
					+ contLocationViewChangeLockDis * 2 < PanelH ){
				ViewCenterY-= y;
				setViewOrign();
			}else{
				if( ViewYmin + contLocationViewChangeLockDis < contLocationViewChangeLockYmin )
					changeViewCenter( 0, ( contLocationViewChangeLockYmin
							- ( ViewYmin + contLocationViewChangeLockDis ) ), null );
				if( ViewYmax - contLocationViewChangeLockDis > contLocationViewChangeLockYmax )
					changeViewCenter( 0,
							- ( ViewYmax - contLocationViewChangeLockDis - contLocationViewChangeLockYmax ),
							null );
			}
		}
		//  and if that axis is changed.
		if( x != 0 && contLocationViewChangeLockEnable ){
			// test is scroll is needed.
			if( contLocationViewChangeLockXmax - contLocationViewChangeLockXmin
					+ contLocationViewChangeLockDis * 2 < PanelW ){
				ViewCenterX-= x;
				setViewOrign();
			}else{
				if( ViewXmin + contLocationViewChangeLockDis < contLocationViewChangeLockXmin )
					changeViewCenter(
							contLocationViewChangeLockXmin - ( ViewXmin + contLocationViewChangeLockDis ), 0,
							null );
				if( ViewXmax - contLocationViewChangeLockDis > contLocationViewChangeLockXmax )
					changeViewCenter(
							- ( ViewXmax - contLocationViewChangeLockDis - contLocationViewChangeLockXmax ),
							0, null );
			}
		}
	}

	private void setViewOrign() {
		ViewOrignX= (int) ( PanelW / 2.0 ) * -1 + ViewCenterX;
		ViewOrignY= (int) ( PanelH / 2.0 ) * -1 + ViewCenterY;
		ViewYmin= (int) ( PanelH / 2.0 ) * -1 + ViewCenterY;
		ViewYmax= (int) ( PanelH / 2.0 ) + ViewCenterY;
		ViewXmin= (int) ( PanelW / 2.0 ) * -1 + ViewCenterX;
		ViewXmax= (int) ( PanelW / 2.0 ) + ViewCenterX;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public String getAllSetting() {
		String ret= "";
		/*
		ret+= "Visable: " + Visable + "\n";
		ret+= "transparentRate: " + TransparentRate+ "\n";
		ret+= "titleDecorated: " + titleDecorated+ "\n";
		ret+= "translucentPane: " + translucentPane+ "\n";
		ret+= "FullScreen: " + FullScreen+ "\n";
		ret+= "WindowedModeWidth: " + WindowedModeWidth+ "\n";
		ret+= "WindowedModeHeight: " + WindowedModeHeight+ "\n";
		*/
		ret+= "PanelWidth: " + PanelW + "\n";
		ret+= "PanelHeight: " + PanelH + "\n";
		ret+= "ViewCenterX: " + ViewCenterX + "\n";
		ret+= "ViewCenterY: " + ViewCenterY + "\n";
		ret+= "ViewOrignX: " + ViewOrignX + "\n";
		ret+= "ViewOrignY: " + ViewOrignY + "\n";
		ret+= "contLocationViewChangeLockEnable: " + contLocationViewChangeLockEnable + "\n";
		ret+= "contLocationViewChangeLockDis: " + contLocationViewChangeLockDis + "\n";
		ret+= "contLocationViewChangeLockXmin: " + contLocationViewChangeLockXmin + "\n";
		ret+= "contLocationViewChangeLockXmax: " + contLocationViewChangeLockXmax + "\n";
		ret+= "contLocationViewChangeLockYmin: " + contLocationViewChangeLockYmin + "\n";
		ret+= "contLocationViewChangeLockYmax: " + contLocationViewChangeLockYmax + "\n";
		ret+= "ViewXmin: " + ViewXmin + "\n";
		ret+= "ViewXmax: " + ViewXmax + "\n";
		ret+= "ViewYmin: " + ViewYmin + "\n";
		ret+= "ViewYmax: " + ViewYmax + "\n";
		ret+= "MsgOutputTest: " + MsgOutputTest + "\n";
		ret+= "MsgOutputNorm: " + MsgOutputNorm + "\n";
		ret+= "MsgOutputErro: " + MsgOutputErro + "\n";
		ret+= "mousewheelNavEnable: " + mousewheelNavEnable + "\n";
		ret+= "mouseWheelNavSpeed: " + mouseWheelNavSpeed + "\n";
		ret+= "keyboardNavEnable: " + keyboardNavEnable + "\n";
		ret+= "keyboardNavSpeed: " + keyboardNavSpeed + "\n";
		ret+= "lastMouseInputTime: " + lastMouseInputTime + "\n";
		ret+= "lastKeyboardInputTime: " + lastKeyboardInputTime + "\n";
		ret+= "LastKey: " + LastKey + "\n";
		return ret;
	}
}
