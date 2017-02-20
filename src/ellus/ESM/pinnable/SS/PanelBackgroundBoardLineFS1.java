package ellus.ESM.pinnable.SS;

import java.awt.Color;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.pinnable.pinSS;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.setting.SManXAttr;
import ellus.ESM.setting.SManXElm;



public class PanelBackgroundBoardLineFS1 extends pinSS implements AbleSMXConfig {
	private SManXElm	inp				= null;
	private ESMPS		PS;
	//
	private int			horiLineXOS		= 0;
	private int			horiLineYOS		= 0;
	private int			vertiLineXOS	= 0;
	private int			vertiLineYOS	= 0;
	private int			horiLineThic	= 0;
	private int			vertiLineThic	= 0;
	private Color		hlC, vlC;
	private int[]		polyUpX;
	private int[]		polyUpY;
	private int[]		polyDownX;
	private int[]		polyDownY;

	public PanelBackgroundBoardLineFS1( SManXElm inp, ESMPS PS ) {
		this.inp= inp;
		this.PS= PS;
		inp.setPin( this );
		reset();
	}

	@Override
	public void reset() {
		this.hlC= inp.getAttr( SManXAttr.AttrType._color, "HorizontalLineColor" ).getColor();
		this.vlC= inp.getAttr( SManXAttr.AttrType._color, "VerticalLineColor" ).getColor();
		this.horiLineXOS= inp.getAttr( SManXAttr.AttrType._int, "HorizontalLineXOS" ).getInteger();
		this.horiLineYOS= inp.getAttr( SManXAttr.AttrType._int, "HorizontalLineYOS" ).getInteger();
		this.horiLineThic= inp.getAttr( SManXAttr.AttrType._int, "HoriLineThickness" ).getInteger();
		this.vertiLineThic= inp.getAttr( SManXAttr.AttrType._int, "VertiLineThickness" ).getInteger();
		this.vertiLineXOS= inp.getAttr( SManXAttr.AttrType._int, "VerticalLineXOS" ).getInteger();
		this.vertiLineYOS= inp.getAttr( SManXAttr.AttrType._int, "VerticalLineYOS" ).getInteger();
		//
		polyUpX= new int[4];
		polyUpY= new int[4];
		polyDownX= new int[4];
		polyDownY= new int[4];
		//
		// up.
		polyUpX[0]= 0;
		polyUpX[1]= horiLineXOS;
		polyUpX[2]= PS.getWidth() - horiLineXOS;
		polyUpX[3]= PS.getWidth();
		polyUpY[0]= 0;
		polyUpY[1]= horiLineYOS;
		polyUpY[2]= horiLineYOS;
		polyUpY[3]= 0;
		// down.
		polyDownX[0]= 0;
		polyDownX[1]= horiLineXOS;
		polyDownX[2]= PS.getWidth() - horiLineXOS;
		polyDownX[3]= PS.getWidth();
		polyDownY[0]= PS.getHeight();
		polyDownY[1]= PS.getHeight() - horiLineYOS;
		polyDownY[2]= PS.getHeight() - horiLineYOS;
		polyDownY[3]= PS.getHeight();
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		g.drawPolyLine( polyUpX, polyUpY, horiLineThic, hlC );
		g.drawPolyLine( polyDownX, polyDownY, horiLineThic, hlC );
		g.drawLine( vertiLineXOS, vertiLineYOS, vertiLineXOS, pan.getHeight() - vertiLineYOS,
				vertiLineThic, vlC );
		g.drawLine( pan.getWidth() - vertiLineXOS, vertiLineYOS, pan.getWidth() - vertiLineXOS,
				pan.getHeight() - vertiLineYOS, vertiLineThic, vlC );
	}
}
