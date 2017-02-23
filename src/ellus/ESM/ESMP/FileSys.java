package ellus.ESM.ESMP;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelContainerScroll;
import ellus.ESM.roboSys.DeskTop;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class FileSys extends ESMPanel {
	private String		curDir		= SMan.getSetting( 500 );
	private SManXElm	folderConfig= null;

	/*||----------------------------------------------------------------------------------------------
	 ||| constructor of class
	||||--------------------------------------------------------------------------------------------*/
	public FileSys( SManXElm config ) {
		super( config );
		//
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "FileSystem" );
		super.bgPL.add( 2, titlePin );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "P_HomeBackgroundColor" ), PS ) );
		//
		folderConfig= config.getElm( "ESMPL", "folderDisplay" );
		constructFolder();
	}

	@Override
	protected synchronized void CheckB3ClickNGE( MouseEvent e ) {
		if( curDir.equals( "###ListRoot###" ) ){
			super.CheckB3ClickNGE( e );
			return;
		}
		File cf= new File( curDir );
		if( cf.getParentFile() != null && cf.getParentFile().exists() ){
			curDir= cf.getParent();
			constructFolder();
		}else{
			curDir= "###ListRoot###";
			constructFolder();
		}
	}

	private void constructFolder() {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( createFolder( folderConfig ) );
	}

	private ESMPL createFolder( SManXElm config ) {
		ESMPL pl= new ESMPL();
		File p= new File( curDir );
		if( !p.isDirectory() && !curDir.equals( "###ListRoot###" ) )
			return pl;
		//
		int xS= PS.ViewOriginX();
		int yS= PS.ViewOriginY();
		//
		ButtonTextFS ffb;
		String pathC= null;
		if( curDir.equals( "###ListRoot###" ) )
			pathC= "#root";
		else{
			try{
				pathC= p.getCanonicalPath();
			}catch ( IOException e1 ){}
		}
		ffb= new ButtonTextFS( config.getElm( "ButtonTextFS", "CurrentPath." ),
				"Current Path: " + pathC );
		pl.add( 4, ffb );
		//
		ArrayList <String> item;
		if( curDir.equals( "###ListRoot###" ) ){
			item= new ArrayList <>();
			for( File tmp : File.listRoots() ){
				item.add( tmp.getAbsolutePath() );
			}
		}else{
			item= helper.getDirFF( curDir );
		}
		ArrayList <pinnable> ff= new ArrayList <>();
		//
		if( curDir.equals( "###ListRoot###" ) ){
			for( String path : item ){
				ffb= new ButtonTextFS( config.getElm( "ButtonTextFS", "FFItemButton." ), path ) {
					@Override
					public void B1clickAction( int x, int y ) {
						File pp= new File( path );
						if( pp.isFile() ){
							try{
								DeskTop.getDeskTop().open( pp );
							}catch ( IOException e ){}
						}else if( pp.isDirectory() ){
							curDir= pp.getAbsolutePath();
							constructFolder();
						}
					}
				};
				ff.add( ffb );
			}
		}else{
			for( String path : item ){
				ffb= new ButtonTextFS( config.getElm( "ButtonTextFS", "FFItemButton." ),
						helper.getFilePathName( path ) ) {
					@Override
					public void B1clickAction( int x, int y ) {
						File pp= new File( path );
						if( pp.isFile() ){
							try{
								DeskTop.getDeskTop().open( pp );
							}catch ( IOException e ){}
						}else if( pp.isDirectory() ){
							curDir= pp.getAbsolutePath();
							constructFolder();
						}
					}
				};
				ff.add( ffb );
			}
		}
		//
		PanelContainerScroll ffg= new PanelContainerScroll( config.getElm( "PanelContainer", "FFItemGroup" ),
				xS + config.getAttr( AttrType._location, "FFItemGroup" ).getLocation().getX(),
				yS + config.getAttr( AttrType._location, "FFItemGroup" ).getLocation().getY(), ff );
		pl.add( 4, ffg );
		//
		return pl;
	}
}
