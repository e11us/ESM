package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Image;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.Able.AbleClick;
import ellus.ESM.pinnable.Able.AbleDoubleClick;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.setting.SCon;



public class ButtonImg extends pin implements AbleHoverHighlight, AbleClick, AbleDoubleClick {
	private Color	bgC, bgC2, bgCH;
	private Image	img;

	public ButtonImg( int[] list, Color c1, Color c2, Color c3, Image img ) {
		this.img= img;
		super.setXY( list[0], list[0] + list[2], list[1], list[1] + list[3] );
		bgC= c1;
		bgC2= c2;
		bgCH= c3;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// add itself.
		pan.getGUIactive().add( this );
		// cor conversion.
		if( img == null ){
			g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight(),
					bgC );
			g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), 1,
					super.getHeight(), bgCH );
			g.drawString( "No Image.", pan.w2bX( super.getXmin() ) + 4, pan.w2bY( super.getYmin() ) + 18,
					Color.black, SCon.FontList.get( 1 ).deriveFont( (float)14 ) );
			return;
		}else{
			if( !isHovered ){
				g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
						pan.getImageWidth( img ) + 6, pan.getImageHeight( img ) + 6, bgC );
				g.fillRect( pan.w2bX( super.getXmin() ) + 1, pan.w2bY( super.getYmin() ) + 1,
						pan.getImageWidth( img ) + 4, pan.getImageHeight( img ) + 4, bgC2 );
				g.drawImage( img, pan.w2bX( super.getXmin() ) + 3, pan.w2bY( super.getYmin() ) + 3 );
			}else{
				g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
						pan.getImageWidth( img ) + 6, pan.getImageHeight( img ) + 6, bgC );
				g.fillRect( pan.w2bX( super.getXmin() ) + 1, pan.w2bY( super.getYmin() ) + 1,
						pan.getImageWidth( img ) + 4, pan.getImageHeight( img ) + 4, bgCH );
				g.drawImage( img, pan.w2bX( super.getXmin() ) + 3, pan.w2bY( super.getYmin() ) + 3 );
			}
		}
	}

	@Override
	public void HoverHighlightOn() {
		isHovered= true;
	}

	@Override
	public void HoverHighlightOff() {
		isHovered= false;
	}

	@Override
	public void B1DoubleClickAction( int x, int y ) {}

	@Override
	public void B1clickAction( int x, int y ) {}
}
