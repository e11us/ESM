package ellus.ESM.pinnable.ButtonAni;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.Able.AbleClick;
import ellus.ESM.pinnable.Able.AbleHoverHighlight;
import ellus.ESM.pinnable.Able.AbleSMXConfig;
import ellus.ESM.roboSys.mouse;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class ButtonSpikeHori extends pinLF implements AbleHoverHighlight, AbleClick, AbleSMXConfig {
	private int			spikeSize, spikeSep, spikeYOS;
	private double		spikeSpeed;
	private Color		spikeEdgeColor, spikeCenterColor, fontBgColor, fontColor;
	private int			fontI, fontS;
	private Font		font;
	private SManXElm	elm;
	private double[]	locaL, locaR;
	private Paint		gpL, gpR;
	private boolean		init			= false;
	private String		msg;
	private int			msgloc, msgsize;
	private boolean		ishovered		= false;
	private long		HoverTime		= 0;
	private int			HoverTimeThres	= Integer.MAX_VALUE;

	public ButtonSpikeHori( SManXElm elm, ESMPS PS, String msg ) {
		this.elm= elm;
		elm.setPin( this );
		this.msg= msg;
		reset();
	}

	@Override
	public void reset() {
		super.setXY(
				elm.getAttr( AttrType._location, "location" ).getLocation().getX(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getX() +
						elm.getAttr( AttrType._int, "Width" ).getInteger(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY(),
				elm.getAttr( AttrType._location, "location" ).getLocation().getY() +
						elm.getAttr( AttrType._int, "Height" ).getInteger() );
		spikeSize= elm.getAttr( AttrType._int, "spikeSize" ).getInteger();
		spikeSep= elm.getAttr( AttrType._int, "spikeSep" ).getInteger();
		spikeSpeed= elm.getAttr( AttrType._double, "spikeSpeedPerFrame" ).getDouble();
		spikeYOS= elm.getAttr( AttrType._int, "spikeYOS" ).getInteger();
		HoverTimeThres= elm.getAttr( AttrType._int, "HoverTimeThres" ).getInteger();
		//
		spikeEdgeColor= elm.getAttr( AttrType._color, "spikeEdgeColor" ).getColor();
		spikeCenterColor= elm.getAttr( AttrType._color, "spikeCenterColor" ).getColor();
		fontBgColor= elm.getAttr( AttrType._color, "fontBgColor" ).getColor();
		fontColor= elm.getAttr( AttrType._color, "fontColor" ).getColor();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
		//
		if( spikeSep == 0 )
			return;
		init= false;
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		if( spikeSep == 0 )
			return;
		//
		pan.addGUIactiveLF( this );
		//
		if( !init ){
			init= true;
			locaL= new double[super.getWidth() / 2 / spikeSep];
			locaR= new double[super.getWidth() / 2 / spikeSep];
			for( int i= 0; i < locaL.length; i++ ){
				locaL[i]= super.getXmin() + i * spikeSep;
				locaR[i]= super.getXmax() - i * spikeSep - spikeSize;
			}
			gpL= new GradientPaint( super.getXmin(), super.getYmin(), spikeEdgeColor,
					super.getXmin() + super.getWidth() / 2, super.getYmin(), spikeCenterColor );
			gpR= new GradientPaint( super.getXmax(), super.getYmin(), spikeEdgeColor,
					super.getXmin() + super.getWidth() / 2, super.getYmin(), spikeCenterColor );
			//
			msgsize= g.getTxtWid( msg, font ) + 10;
			msgloc= super.getXmin() + super.getWidth() / 2 - ( msgsize ) / 2;
		}
		//
		if( ishovered ){
			for( int i= 0; i < locaL.length; i++ ){
				g.fillRect( (int)locaL[i], super.getYmin() + spikeYOS, spikeSize, super.getHeight() - spikeYOS * 2,
						gpL );
				g.fillRect( (int)locaR[i], super.getYmin() + spikeYOS, spikeSize, super.getHeight() - spikeYOS * 2,
						gpR );
				locaL[i]+= spikeSpeed * 2;
				locaR[i]-= spikeSpeed * 2;
				//
				if( locaL[i] + spikeSize > super.getXmin() + super.getWidth() / 2 )
					locaL[i]= super.getXmin();
				if( locaR[i] < super.getXmin() + super.getWidth() / 2 )
					locaR[i]= super.getXmax() - spikeSize;
			}
			//
			g.fillRect( msgloc, super.getYmin(), msgsize, super.getHeight(), fontBgColor );
			g.drawString( msg, msgloc + 5, super.ymax - ( super.getHeight() - fontS ) / 2, fontColor, font );
		}else{
			for( int i= 0; i < locaL.length; i++ ){
				g.fillRect( (int)locaL[i], super.getYmin() + spikeYOS, spikeSize, super.getHeight() - spikeYOS * 2,
						gpL );
				g.fillRect( (int)locaR[i], super.getYmin() + spikeYOS, spikeSize, super.getHeight() - spikeYOS * 2,
						gpR );
				locaL[i]+= spikeSpeed;
				locaR[i]-= spikeSpeed;
				//
				if( locaL[i] + spikeSize > super.getXmin() + super.getWidth() / 2 )
					locaL[i]= super.getXmin();
				if( locaR[i] < super.getXmin() + super.getWidth() / 2 )
					locaR[i]= super.getXmax() - spikeSize;
			}
			//
			g.fillRect( msgloc, super.getYmin(), msgsize, super.getHeight(), fontBgColor );
			g.drawString( msg, msgloc + 5, super.ymax - ( super.getHeight() - fontS ), fontColor, font );
		}
	}

	@Override
	public void B1clickAction( int x, int y ) {}

	@Override
	public void HoverHighlightOn() {
		if( !ishovered ){
			HoverTime= helper.getTimeLong();
		}else{
			if( ( helper.getTimeLong() - HoverTime ) > HoverTimeThres ){
				B1clickAction( 0, 0 );
				HoverTime= helper.getTimeLong();
				mouse.moveToCenter();
			}
		}
		ishovered= true;
	}

	@Override
	public void HoverHighlightOff() {
		ishovered= false;
		HoverTime= 0;
	}
}
