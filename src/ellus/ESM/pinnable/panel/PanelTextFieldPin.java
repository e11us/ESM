package ellus.ESM.pinnable.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.util.ArrayList;
import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.pinnable.pinLF;
import ellus.ESM.pinnable.able_Interface.AbleClick;
import ellus.ESM.pinnable.able_Interface.AbleClickHighlight;
import ellus.ESM.pinnable.able_Interface.AbleClickR;
import ellus.ESM.pinnable.able_Interface.AbleDoubleClick;
import ellus.ESM.pinnable.able_Interface.AbleHoverHighlight;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardFunInp;
import ellus.ESM.pinnable.able_Interface.AbleKeyboardInput;
import ellus.ESM.pinnable.able_Interface.AbleMouseDrag;
import ellus.ESM.pinnable.able_Interface.AbleMouseWheel;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.roboSys.clipBoard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



/*
 * very similar to panel similar to PanelTextFieldPin. but dont cover whole screen, but rather its a pin.
 * support line wrap. ( wrap will only work when input english. )
 *
 */
public class PanelTextFieldPin extends pinLF implements AbleSMXConfig, AbleClickR,AbleClickHighlight, 
AbleClick, AbleMouseWheel, AbleDoubleClick,AbleKeyboardFunInp, AbleKeyboardInput {
	private Color				bg1C, bg2C, edC, txC, curC;
	private ArrayList <String>	tx;
	private int					LineSep				= 5;
	private int					xo, yo;
	//
	private int					inpModeCurBlinkCount= 0;
	private final int			inpModeCurBlinkThre	= 25;
	private boolean				inpModeCurblinkShow	= false;
	private int					fontI				= 4, fontS= 26;
	private Font				font;
	private SManXElm			elm;
	private PanelTextFieldPin		handler				= this;
	private printOut			PO					= new printOut();
	private cursor CS= new cursor();
	private int[]				charWidth			= null;
	private aLine				head				= null, tail;
	private boolean stopPrint= false;

	public PanelTextFieldPin( SManXElm elm, ArrayList <String> tx ) {
		//int xo, int yo, int fontI, Color[] co, ArrayList <String> tx, Graphics g ) {//bg1C, bg2C, edC, txC;
		this.tx= tx;
		this.elm= elm;
		elm.setPin( this );
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
		bg1C= elm.getAttr( AttrType._color, "BackgroundColor1" ).getColor();
		bg2C= elm.getAttr( AttrType._color, "BackgroundColor2" ).getColor();
		edC= elm.getAttr( AttrType._color, "EdgeColor" ).getColor();
		txC= elm.getAttr( AttrType._color, "TextColor" ).getColor();
		curC= elm.getAttr( AttrType._color, "CursorColor" ).getColor();
		xo= elm.getAttr( AttrType._int, "ToEdgeDistanceX" ).getInteger();
		yo= elm.getAttr( AttrType._int, "ToEdgeDistanceY" ).getInteger();
		//
		fontI= elm.getAttr( AttrType._int, "FontIndex" ).getInteger();
		LineSep= elm.getAttr( AttrType._int, "LineSeperation" ).getInteger();
		if( fontI >= SCon.FontList.size() )
			fontI= 0;
		if( fontI < 0 )
			fontI= 0;
		fontS= elm.getAttr( AttrType._int, "FontSize" ).getInteger();
		font= SCon.FontList.get( fontI ).deriveFont( (float)fontS );
	}

	public String getTxt() {
		StringBuilder res= null;
		while( head != null ) {
			if( res == null )
				res= head.lines;
			else
				res.append( '\n' + head.lines.toString() );
			head= head.post;
		}
		return res.toString();
	}

	public void setText( String inp ) {
		tx= helper.str2ALstr( inp );
		//
		stopPrint= true;
		setTx();
		stopPrint= false;
	}
	
	@Override
	public void FunInp( String inp ) {
		if( ( tx ) == null )
			return;
		ArrayList <String> msg= tx;
		if( inp.length() == 2 ){
			switch( inp ){
				case "CV" :
					CS.CV( clipBoard.getString() );
					break;
				case "CC" :
					clipBoard.setString( getTxt() );
					break;
				//
				// for arrow key.
				case "AU" :
					CS.AU();
					break;
				case "AD" :
					CS.AD();
					break;
				case "AL" :
					CS.AL();
					break;
				case "AR" :
					CS.AR();
					break;
			}
		}else if( inp.length() == 3 ){
			switch( inp ){
				case "VHO" :
					CS.SD();
					break;
				case "VEN" :
					CS.ED();
					break;
				case "VDE" :
					CS.delR();
					break;
				default :
					break;
			}
		}
	}

	@Override
	public void keyboardInp( String code ) {
		//
		if( code.length() == 1 ){
			if( code.charAt( 0 ) >= 32 && code.charAt( 0 ) <= 126 ){
				CS.input( code.charAt( 0 ) );
				return;
			}else if( ( code.charAt( 0 ) ) == 8 ){
				CS.del();
			}else if( ( code.charAt( 0 ) ) == 10 ){
				CS.nwl();
			}
		}
	}

	@Override
	public void paint( ESMPD g, ESMPS pan ) {
		//
		if( stopPrint )
			return;
		// add to non LF instead so it can consume all the input.
		pan.addGUIactive( this );
		// add itself.
		//
		if( charWidth == null ){
			setCharWidth( g );
			setTx();
			return;
		}
		Paint gp= new GradientPaint( pan.w2bX( super.getXmin() ),
				pan.w2bY( super.getYmin() ), bg1C, pan.w2bX( super.getXmin() ),
				pan.w2bY( super.getYmax() ), bg2C );
		g.fillRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), gp );
		g.drawRect( pan.w2bX( super.getXmin() ), pan.w2bY( super.getYmin() ),
				super.getWidth(), super.getHeight(), 1, edC );
		//
		// draw the strings.
		ArrayList <String> curLine= PO.print( g, (int) ( super.getWidth() - xo*2.4 ) );
		//f.f( "-------------------------------------" + curLine.size() + " " );
		for( int i= 0; i < curLine.size(); i++ ){
			g.drawString( curLine.get( i ), pan.w2bX( super.getXmin() ) + xo,
					pan.w2bY( super.getYmin() ) + yo + ( LineSep + fontS ) * ( i + 1 ), 
					txC, font );
		}
		// draw cursor.
		if( inpModeCurBlinkCount++ == inpModeCurBlinkThre ){
			inpModeCurBlinkCount= 0;
			inpModeCurblinkShow= !inpModeCurblinkShow;
		}
		if( inpModeCurblinkShow ) {
			cor2D curLoc= CS.getCurPosition( g, font );
			if( curLoc == null ) return ;
			g.drawLine( pan.w2bX( super.getXmin() ) + xo + curLoc.getX(),
					pan.w2bY( super.getYmin() ) + yo  + ( LineSep + fontS ) * ( curLoc.getY()  + 1 ),
					pan.w2bX( super.getXmin() ) + xo  + curLoc.getX(),
					(int) ( pan.w2bY( super.getYmin() ) + ( LineSep + fontS ) * ( curLoc.getY() )
							+ yo + fontS*0.19  ), 3, curC );	
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| 
	||||--------------------------------------------------------------------------------------------*/
	class printOut {
		aLine				head	= handler.head;
		int					headInd	= 0;
		ArrayList <String>	lines;
		ArrayList <aLine>	linT;
		ArrayList <Integer>	linTInd;
		//
		int lineTot;
		aLine current= null;
		int currentInd= 0;
		
		void reset() {
			head= null;
			headInd= 0;
		}

		//--------------------------------------------
		ArrayList <String> print( ESMPD g, int wid ) {
			lines= new ArrayList <>();
			linT= new ArrayList <>();
			linTInd= new ArrayList <>();
			if( headInd == -1 )
				headInd= head.linePO.size() - 1;
			current= head;
			boolean first= true;
			//
			lineTot= ( handler.getHeight() - yo * 2  ) / ( LineSep + fontS );
			while( lines.size() < lineTot ){
				if( current == null )
					return lines;
				//
				linTInd.add( new Integer( lines.size() ) );
				if( first ) {
					lines.addAll( current.get( headInd, wid ) );
					first = false;
				}else
					lines.addAll( current.get( 0, wid ) );
				linT.add( current );
				//
				current= current.post;
			}
			while( lines.size() > lineTot ) {
				lines.remove( lines.size()-1 );
			}
			return lines;
		}

		void lookUp() {
			if( headInd > 0 )
				headInd --;
			else if( head.pre != null ) {
				head= head.pre;
				headInd= -1;
			}
		}

		void lookDown() {
			if( head.hasNextInd( headInd ) )
				headInd++;
			else {
				if( head.post != null ) {
					head= head.post;
					headInd= 0;
				}
			}
			
			/*
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * still need fix.
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
			

		}
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| 
	||||--------------------------------------------------------------------------------------------*/
	class cursor{
		int x= 0;
		int yind= 0;
		aLine curLinObj= null;
		String curLine= null;
		int totInx= 0;
		
		void reset() {
			x=0;
			yind= 0;
			totInx= 0;
			curLinObj= null;
			curLine= null;
		}
		
		cor2D getCurPosition( ESMPD g , Font ff ) {
			try {
				curLine= curLinObj.get( yind );
				if( PO.linT.contains( curLinObj ) ) {
					int y= PO.linTInd.get( PO.linT.indexOf( curLinObj ) ) + yind;
					return new cor2D( g.getTxtWid( curLine.substring( 0, x ), ff ), y );
				}
				return null;
			}catch( Exception ee ) {
				f.f( totInx + " " + x + " " + curLine.length() );
				ee.printStackTrace();
				System.exit( 11 );
			}
			return null;
		}
	
		
		void CV( String str ) {
			str.replace( '\n', ' ' );
			StringBuilder A= new StringBuilder( curLinObj.lines.toString().substring( 0, totInx ) );
			A.append( " " + str + " " );
			A.append( curLinObj.lines.toString().substring( totInx, curLinObj.lines.length() ) );
			curLinObj.lines= A;
			curLinObj.linePO= wrapLine( curLinObj.lines.toString(), lastWid );
			//
			totInx+= str.length() + 1;
			int tot= totInx;
			int y= 0;
			while( true ) {
				if( tot > curLinObj.linePO.get( y ).length() )
					tot-= curLinObj.linePO.get( y++ ).length();
				else {
					yind= y;
					x= tot;
					break;
				}
			}
			
		}
		
		void SD() {
			x= 0;
			totInx= x + curLinObj.getTotTil( yind );
			if( curLinObj.lines.length() > totInx &&
					curLinObj.lines.charAt( totInx ) == ' ' && x == 0) {
				totInx++;
				x++;
			}
		}
		
		void ED() {
			x= curLinObj.linePO.get( yind ).length();
			totInx= x + curLinObj.getTotTil( yind );
		}
		
		void AU() {
			if( yind > 0 ) {
				yind--;
				//
				curLine= curLinObj.get( yind );
				if( curLine.length() < x )
					x= curLine.length();
				totInx= x + curLinObj.getTotTil( yind );
			}else if( curLinObj.pre != null ) {
				curLinObj= curLinObj.pre;
				yind= curLinObj.linePO.size() - 1;
				//
				curLine= curLinObj.get( yind );
				if( curLine.length() < x ) 
					x= curLine.length();
				totInx= x + curLinObj.getTotTil( yind );
			}
			if( curLinObj.lines.length() > totInx &&
					curLinObj.lines.charAt( totInx ) == ' ' && x == 0) {
				totInx++;
				x++;
			}
		}

		void AD() {
			if( PO.linT.contains( curLinObj ) ) {
				if( PO.linTInd.get( PO.linT.indexOf( curLinObj ) ) + yind + 2 >= PO.lineTot ) {
					PO.lookDown();
					return;
				}
			}else
				return;
			//
			if( curLinObj.hasNextInd( yind ) ) {
				curLine= curLinObj.get( yind );
				yind++;
				if( totInx + curLine.length() > curLinObj.lines.length() ) {
					x= curLinObj.get( yind ).length();
					totInx= x + curLinObj.getTotTil( yind );
				}else {
					if( curLinObj.get( yind ).length() < x) 
						x= curLinObj.get( yind ).length();
					totInx= x + curLinObj.getTotTil( yind );
				}
			}else if( curLinObj.post != null ) {
				curLinObj= curLinObj.post;
				yind= 0;
				if( x > curLinObj.get( yind ).length() )
					x= curLinObj.get( yind ).length();
				totInx= x + curLinObj.getTotTil( yind );
			}
			if( curLinObj.lines.length() > totInx &&
					curLinObj.lines.charAt( totInx ) == ' ' && x == 0) {
				totInx++;
				x++;
			}
		}

		void AR() {
			if( x > curLinObj.get( yind ).length() - 1 ) {
				if(  curLinObj.hasNextInd( yind ) ) {
					yind++;
					x= 0;
					if( curLinObj.lines.charAt( totInx ) == ' ' ) {
						totInx++;
						x++;
					}
				}else if( curLinObj.post != null ) {
					curLinObj= curLinObj.post;
					totInx= 0;
					x= 0;
					yind= 0;
				}
			}else {
				x++;
				totInx++;
			}
		}

		void AL() {
			if( ( x > 0 && yind == 0 ) || ( x > 1 && yind > 0 ) ) {
				x--;
				totInx--;
			}else if( yind > 0 ) {
				yind--;
				x= curLinObj.get( yind ).length();
				totInx= x + curLinObj.getTotTil( yind );
			}else if( curLinObj.pre != null ) {
				curLinObj= curLinObj.pre;
				yind= curLinObj.linePO.size() - 1;
				x= curLinObj.get( yind ).length();
				totInx= x + curLinObj.getTotTil( yind );
			}
		}
		
		void input( char c ) {
			curLinObj.lines.insert( totInx, c );
			curLinObj.linePO= wrapLine( curLinObj.lines.toString(), lastWid );
			totInx++;
			int tot= totInx;
			int y= 0;
			while( true ) {
				if( tot > curLinObj.linePO.get( y ).length() )
					tot-= curLinObj.linePO.get( y++ ).length();
				else {
					yind= y;
					x= tot;
					break;
				}
			}
		}
		
		 void nwl() {
			aLine nxt= new aLine();
			nxt.lines=  new StringBuilder( curLinObj.lines.substring( totInx, curLinObj.lines.length() ) );
			curLinObj.lines= new StringBuilder( curLinObj.lines.substring( 0, totInx ) );
			nxt.post= curLinObj.post;
			nxt.pre= curLinObj;
			curLinObj.post= nxt;
			curLinObj= nxt;
			x= totInx= 0;
			yind= 0;
		}
		
		 void del() {
			if( totInx == 0 ) {
				if( curLinObj.pre != null ) {
					totInx= curLinObj.pre.lines.length();
					yind= curLinObj.pre.linePO.size() - 1;
					x= curLinObj.pre.linePO.get( yind ).length();
					curLinObj.pre.lines.append( curLinObj.lines );
					curLinObj.lines= null;
					curLinObj.linePO= null;
					curLinObj.pre.post= curLinObj.post;
					if( curLinObj.post != null )
						curLinObj.post.pre= curLinObj.pre;
					curLinObj= curLinObj.pre;
				}
			}else {
				String A= curLinObj.lines.toString().substring( 0, totInx-1 );
				String B= curLinObj.lines.toString().substring( totInx, curLinObj.lines.length() );
				curLinObj.lines= new StringBuilder( A + B );
				curLinObj.linePO= wrapLine( curLinObj.lines.toString(), lastWid );
				//
				int tot= --totInx;
				int y= 0;
				while( true ) {
					if( tot > curLinObj.linePO.get( y ).length() )
						tot-= curLinObj.linePO.get( y++ ).length();
					else {
						yind= y;
						x= tot;
						break;
					}
				}
			}
		}
		
		void delR(){
			if( totInx >= curLinObj.lines.length()  )
				return;
			String A= curLinObj.lines.toString().substring( 0, totInx );
			String B= curLinObj.lines.toString().substring( totInx+1, curLinObj.lines.length() );
			curLinObj.lines= new StringBuilder( A + B );
			curLinObj.linePO= wrapLine( curLinObj.lines.toString(), lastWid );
			//
			int tot= totInx;
			int y= 0;
			while( true ) {
				if( tot > curLinObj.linePO.get( y ).length() )
					tot-= curLinObj.linePO.get( y++ ).length();
				else {
					yind= y;
					x= tot;
					break;
				}
			}
		}
	}
	
	class aLine {
		ArrayList <String>	linePO	= null;
		StringBuilder		lines	= null;
		aLine				pre		= null;
		aLine				post	= null;

		ArrayList <String> get( int ind, int wid ) {
			linePO= wrapLine( lines.toString(), wid );
			if( ind > linePO.size() - 1 )
				return null;
			ArrayList <String> ret= new ArrayList <>();
			for( int i= ind; i < linePO.size(); i++ )
				ret.add( linePO.get( i ) );
			return ret;
		}
		
		String get( int ind ) {
			if( ind > linePO.size() - 1 )
				return null;
			else 
				return linePO.get( ind);
		}
		
		boolean hasNextInd( int ind ) {
			if( linePO == null )
				return false;
			if( ind + 1 > linePO.size()-1 )
				return false;
			else return true;
		}
		
		int getTotTil( int ind ) {
			if( ind > linePO.size() - 1 )
				ind= linePO.size() - 1;
			int tot= 0;
			for( int i= 0; i < ind; i++ ) {
				tot+= linePO.get( i ).length(); 
			}
			return tot;
		}
		
		void AU() {
			
		}

		void AD() {
			
		}

		public void AR() {}

		public void AL() {}
		
	}

	private int lastWid= 0;
	private ArrayList <String> wrapLine( String line, int wid ) {
		lastWid= wid;
		int tot= 0;
		char cha;
		int lastSpace= -1;
		ArrayList <String> ret= new ArrayList <String>();
		boolean end= false;
		//
		while( !end ){
			lastSpace= -1;
			tot= 0;
			end= true;
			for( int i= 0; i < line.length(); i++ ){
				cha= line.charAt( i );
				if( cha == ' ' )
					lastSpace= i;
				if( cha >= 32 && cha <= 126 ){
					tot+= charWidth[cha - 32];
				}else tot+= charWidth[0] * 2;
				if( tot > wid ){
					if( lastSpace != -1 ){
						ret.add( line.substring( 0, lastSpace ) );
						line= line.substring( lastSpace, line.length() );
						end= false;
						break;
					}else{
						ret.add( line.substring( 0, i - 1 ) );
						line= line.substring( i - 1, line.length() );
						end= false;
						break;
					}
				}
			}
		}
		ret.add( line );
		return ret;
	}

	private void setCharWidth( ESMPD g ) {
		charWidth= new int[126 - 32 + 1];
		for( int i= 0; i < charWidth.length; i++ ){
			charWidth[i]= g.getTxtWid( "" + ( (char) ( i + 32 ) ), font );
		}
	}
	
	private void setTx() {
		head= null;
		aLine l;
		aLine last= null;;
		for( String t : tx ){
			l= new aLine();
			if( head == null )
				head= l;
			l.lines= new StringBuilder( t );
			if( last != null ){
				last.post= l;
				l.pre= last;
			}
			last= l;
		}
		tail= last;
		//
		PO.reset();
		CS.reset();
		PO.head= head;
		CS.curLinObj= head;
	}

	/*||----------------------------------------------------------------------------------------------
	 ||| 
	||||--------------------------------------------------------------------------------------------*/
	@Override
	public void WheelRotateAction( int rot ) {
		if( rot > 0 )
			PO.lookDown();
		else PO.lookUp();
	}

	@Override
	public void B3clickAction( int x, int y ) {
		this.removeMe= true;
	}

	@Override
	public void B1clickAction( int x, int y ) {}

	@Override
	public void B1clickHighlightOn( int x, int y ) {}

	@Override
	public void B1clickHighlightOff( int x, int y ) {}

	@Override
	public void B1DoubleClickAction( int x, int y ) {}
}