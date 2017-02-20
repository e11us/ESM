package ellus.ESM.ESMP;

import java.util.ArrayList;
import java.util.Timer;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.f;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.crypto.password;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonScrollWT;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundMatx;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.able_Interface.AbleSMXConfig;
import ellus.ESM.pinnable.panel.PanelMultiLineInput;
import ellus.ESM.pinnable.panel.PanelTextFieldPin;
import ellus.ESM.pinnable.panel.PanelTextRead;
import ellus.ESM.pinnable.panel.PanelContainer;
import ellus.ESM.pinnable.panel.PanelDisplay;
import ellus.ESM.roboSys.keyboard;
import ellus.ESM.setting.SCon;
import ellus.ESM.setting.SMan;
import ellus.ESM.setting.SManX;
import ellus.ESM.setting.SManXAttr.AttrType;
import ellus.ESM.setting.SManXElm;



public class Calendar extends ESMPanel {
	private SManXElm		CalendarPL= null;
	
	private ButtonTextFS	newPS		= null;
	private int				curAction	= 0;

	// only allow one instance.
	private static Calendar cal= null;
	
	// get the instance.
	public static Calendar getInstance(  SManXElm config  ) {
		if( cal != null )
			return null;
		cal= new Calendar( config );
		return cal;
	}		
		
	Calendar( SManXElm config ) {
		super( config );
		this.masterSE= config;
		this.CalendarPL= config.getElm( "ESMPL", "Calendar" );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "BackgroundColor" ), PS ) );
		super.titlePin= new PanelBackgroundTitleFS1( 
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border"), PS, "Calendar" );
		super.bgPL.add( 2, titlePin );
		//
		constructShow();
	}
	
	private void constructShow() {
		super.Subpls.removeAll( Subpls );
		super.Subpls.add( createShowPW( CalendarPL ) );
	}

	private ESMPL createShowPW( SManXElm config ) {
		ESMPL pl= new ESMPL();
		//
		// week day title.
		ArrayList <pinnable> dayTitle= new ArrayList <>();
		for( int i= 0; i < 7; i++ ) {
			dayTitle.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDayName" ),
				helper.getWeekDayName( i+1 ) )  );
		}
		PanelContainer pwp= new PanelContainer( config.getElm( "PanelContainer", "WeekDayName" ), dayTitle );
		pl.add( 4, pwp );
		// week day 
		DateTime dt= new DateTime();
		dt= dt.minusDays( dt.getDayOfWeek() - 1);
		DateTime today= new DateTime();
		ArrayList <pinnable> days= new ArrayList <>();
		int dayNum= 0;
		for( int i= 0; i < 356; i++ ) {
			dayNum= dt.getDayOfMonth();
			if( dt.equals( today ) ) {
				days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Today" ), dayNum + ""));
				dt= dt.plusDays( 1 );
				continue;
			}
			if( i == 0 || dayNum == 1 ) {
				days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ),	
						dayNum + " " + helper.getMonthName( dt.getMonthOfYear() -1 ).substring( 0,3 ) ) );
			}else {
				days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ),	dayNum + ""  ) );
			}
			dt= dt.plusDays( 1 );
		}
		PanelContainer pwp2= new PanelContainer( config.getElm( "PanelContainer", "WeekDay" ), days );
		pl.add( 4, pwp2 );
		//
		// functional key.
		ArrayList <pinnable> dfp= new ArrayList <>();
		dfp.add(  new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "New RecurrentEvent" ));
		dfp.add(  new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "New One-Time Event" ));
		dfp.add(  new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "Display All Events" ));
		dfp.add(  new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "Exit Calendar" ));
		PanelContainer dfpc= new PanelContainer( config.getElm( "PanelContainer", "FunctionalPanel" ), dfp );
		pl.add( 4, dfpc );		
		//
		return pl;
	}

	@Override
	protected void closePanel() {
		super.closePanel();
		cal= null;
	}
}
