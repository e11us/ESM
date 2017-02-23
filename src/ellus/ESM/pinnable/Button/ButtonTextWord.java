package ellus.ESM.pinnable.Button;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.HTML.HTML2TXT;
import ellus.ESM.pinnable.pin;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardInput;
import ellus.ESM.setting.SCon;
// this is similar to noteText. typically used to display a single word.
// but with many feature deleted. a modified textNote.
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;



public class ButtonTextWord extends pin implements AbleHoverHighlight, AbleClick, AbleKeyboardInput, AbleKeyboardFunInp, AbleDoubleClick {
	private int			fontI	= 0, fontS= 14, xOS= 6, yOS= 6;
	private Color		bg1C, bg2C, bgHC, txC, txHC;
	private int			line	= 0;
	private int			ind		= 0;
	//
	private Graphics	g		= null;
	private boolean		HLmode	= false;
	private boolean		XYset	= false;
	//
	protected String	word;

	public ButtonTextWord( int[] inp, Color[] co, String word, Graphics g ) {
		// int x, int y, int xOS, int yOS, int fontI, int fontS,
		// bg1C, bg2C, bgHC, txC, txHC
		this.g= g;
		this.word= word;
		super.setXY( inp[0], inp[0], inp[1], inp[1] );
		this.xOS= inp[2];
		this.yOS= inp[3];
		this.fontI= inp[4];
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		this.fontS= inp[5];
		bg1C= co[0];
		bg2C= co[1];
		bgHC= co[2];
		txC= co[3];
		txHC= co[4];
	}

	public void setContLoca( HTML2TXT bk, int line, int ind ) {
		this.line= line;
		this.ind= ind;
	}

	public int getLine() {
		return line;
	}

	public int getInd() {
		return ind;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		// add itself.
		// set XY.
		if( !XYset ){
			resetXY();
		}
		// cor conversion.
		int pan.w2bX( super.getXmin() )= p.getX();
		int pan.w2bY( super.getYmin() )= p.getY();
		if( HLmode ){
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bgHC, pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bgHC, true );
			Graphics2D g2d= (Graphics2D)g;
			g2d.setPaint( gp );
			g2d.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight() );
			//
			g.setColor( txHC );
			g.setFont( SCon.FontList.get( fontI ).deriveFont( ( (float)fontS ) ) );
			g.drawString( word, pan.w2bX( super.getXmin() ) + xOS, pan.w2bY( super.getYmin() ) + fontS - 2 );
		}else{
			// gradient paint. the background of the button polygon.
			Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), bg1C, pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ) + super.getHeight(), bg2C, true );
			Graphics2D g2d= (Graphics2D)g;
			g2d.setPaint( gp );
			g2d.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ), super.getWidth(), super.getHeight() );
			//
			g.setColor( txC );
			g.setFont( SCon.FontList.get( fontI ).deriveFont( ( (float)fontS ) ) );
			g.drawString( word, pan.w2bX( super.getXmin() ) + xOS, pan.w2bY( super.getYmin() ) + fontS - 2 );
		}
	}

	public void resetXY() {
		try{
			Font fs= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
			FontMetrics metrics= g.getFontMetrics( fs );
			super.setXY( super.getXmin(), metrics.stringWidth( word ) + super.getXmax() + xOS * 2,
					super.getYmin(), super.getYmax() + yOS * 2 + fontS );
			XYset= true;
		}catch ( Exception ee ){
			ee.printStackTrace();
		}
	}

	@Override
	public void FunInp( String inp ) {}

	@Override
	public void keyboardInp( String code ) {}

	@Override
	public void B1clickAction( MouseEvent inp ) {}

	@Override
	public void B1DoubleClickAction( MouseEvent inp ) {}

	@Override
	public void B3DoubleClickAction( MouseEvent inp ) {}

	@Override
	public void HoverHighlightOn() {
		HLmode= true;
	}

	@Override
	public void HoverHighlightOff() {
		HLmode= false;
	}
}
