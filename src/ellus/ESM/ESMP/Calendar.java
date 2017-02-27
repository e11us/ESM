package ellus.ESM.ESMP;

import java.awt.Color;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ellus.ESM.ESMW.ESMPL;
import ellus.ESM.ESMW.ESMPanel;
import ellus.ESM.Machine.BArray;
import ellus.ESM.Machine.helper;
import ellus.ESM.data.SQL.mySQLportal;
import ellus.ESM.data.SQL.sqlResult;
import ellus.ESM.pinnable.pinnable;
import ellus.ESM.pinnable.Button.ButtonInputFS;
import ellus.ESM.pinnable.Button.ButtonTextFS;
import ellus.ESM.pinnable.SS.PanelBackgroundSC;
import ellus.ESM.pinnable.SS.PanelBackgroundTitleFS1;
import ellus.ESM.pinnable.panel.PanelContainerScroll;
import ellus.ESM.setting.SManXElm;



public class Calendar extends ESMPanel {
	private SManXElm				CalendarPL	= null;
	private SManXElm				newEvn		= null;
	// only allow one instance.
	private static Calendar			cal			= null;
	private static final String		sqlfuncName	= "Calendar";
	private boolean					sqlLoaded	= false;
	private BArray <evn>			events		= null;
	private ArrayList <evn>			days4Compare= null;
	private ArrayList <pinnable>	weekdaysPin	= null;
	private ArrayList <pinnable>	eventsPin	= null;
	private PanelContainerScroll	dfpc		= null;
	private PanelContainerScroll	datp		= null;
	// 0= home, 1= new event input, 2= show chosen day event, show all event, 3= show chosen event.
	private int						stage		= 0;

	// get the instance.
	public static Calendar getInstance( SManXElm config ) {
		if( cal != null )
			return null;
		cal= new Calendar( config );
		return cal;
	}

	Calendar( SManXElm config ) {
		super( config );
		this.masterSE= config;
		this.CalendarPL= config.getElm( "ESMPL", "Calendar" );
		this.newEvn= config.getElm( "ESMPL", "newEvn" );
		super.bgPL.add( 0, new PanelBackgroundSC( config.getElm( "PanelBackgroundSC", "BackgroundColor" ), PS ) );
		super.titlePin= new PanelBackgroundTitleFS1(
				config.getElm( "PanelBackgroundTitleFS1", "Title&Border" ), PS, "Calendar" );
		super.bgPL.add( 2, titlePin );
		//
		constructShow();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void constructShow() {
		stage= 0;
		super.Subpls.removeAll( Subpls );
		if( sqlLoaded ){
			super.Subpls.add( createShowPW( CalendarPL ) );
		}else{
			ESMPL pl= new ESMPL();
			ArrayList <pinnable> tt= new ArrayList <>();
			tt.add( new ButtonTextFS( newEvn.getElm( "ButtonTextFS", "confrim" ), "Loading SQL..." ) );
			PanelContainerScroll pwp= new PanelContainerScroll( CalendarPL.getElm( "PanelContainer", "WeekDayName" ),
					tt );
			pl.add( 1, pwp );
			super.Subpls.add( pl );
			loadSQL();
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private ESMPL createShowPW( SManXElm config ) {
		ESMPL pl= new ESMPL();
		//
		// week day title.
		ArrayList <pinnable> dayTitle= new ArrayList <>();
		for( int i= 0; i < 7; i++ ){
			dayTitle.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDayName" ),
					helper.getWeekDayName( i + 1 ) ) );
		}
		PanelContainerScroll pwp= new PanelContainerScroll( config.getElm( "PanelContainer", "WeekDayName" ),
				dayTitle );
		pl.add( 4, pwp );
		// week day
		DateTime dt= new DateTime();
		int offset= dt.getDayOfWeek() - 1;
		dt= dt.minusDays( dt.getDayOfWeek() - 1 );
		DateTime today= new DateTime();
		ArrayList <pinnable> days= new ArrayList <>();
		int dayNum= 0;
		int totEvent= 0;
		days4Compare= new ArrayList <>();
		for( int i= 0; i < 356; i++ ){
			dayNum= dt.getDayOfMonth();
			if( dt.equals( today ) ){
				//
				days4Compare.add( new evn( dt ) );
				totEvent= days4Compare.get( days4Compare.size() - 1 ).allONandBefore( events );
				//
				if( i == 0 || dayNum == 1 ){
					if( totEvent > 0 )
						days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Today" ),
								dayNum + " " + helper.getMonthName( dt.getMonthOfYear() - 1 ).substring( 0, 3 ),
								"x" + totEvent, days4Compare.get( days4Compare.size() - 1 ).sd ) {
							@Override
							public void B1clickAction( int x, int y ) {
								showEventofDay( super.getmsgID() );
							}
						} );
					else days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Today" ),
							dayNum + " " + helper.getMonthName( dt.getMonthOfYear() - 1 ).substring( 0, 3 ) ) );
				}else{
					if( totEvent > 0 )
						days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Today" ), dayNum + "",
								"x" + totEvent, days4Compare.get( days4Compare.size() - 1 ).sd ) {
							@Override
							public void B1clickAction( int x, int y ) {
								showEventofDay( super.getmsgID() );
							}
						} );
					else days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Today" ), dayNum + "" ) );
				}
				dt= dt.plusDays( 1 );
				continue;
			}
			//
			days4Compare.add( new evn( dt ) );
			totEvent= days4Compare.get( days4Compare.size() - 1 ).allOnDay( events );
			//
			if( i % 21 == 0 || dayNum == 1 ){
				if( totEvent > 0 && i > offset )
					days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ),
							dayNum + " " + helper.getMonthName( dt.getMonthOfYear() - 1 ).substring( 0, 3 ),
							"x" + totEvent, days4Compare.get( days4Compare.size() - 1 ).sd ) {
						@Override
						public void B1clickAction( int x, int y ) {
							showEventofDay( super.getmsgID() );
						}
					} );
				else days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ),
						dayNum + " " + helper.getMonthName( dt.getMonthOfYear() - 1 ).substring( 0, 3 ) ) );
			}else{
				if( totEvent > 0 && i > offset )
					days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ), dayNum + "",
							"x" + totEvent, days4Compare.get( days4Compare.size() - 1 ).sd ) {
						@Override
						public void B1clickAction( int x, int y ) {
							showEventofDay( super.getmsgID() );
						}
					} );
				else days.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "WeekDay" ), dayNum + "" ) );
			}
			dt= dt.plusDays( 1 );
		}
		if( datp == null )
			datp= new PanelContainerScroll( config.getElm( "PanelContainer", "WeekDay" ), null );
		datp.resetCont( days );
		weekdaysPin= days;
		pl.add( 4, datp );
		//
		// functional key.
		ArrayList <pinnable> dfp= new ArrayList <>();
		dfp.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "New RecurrentEvent" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				newrecurEventInputWindow();
			}
		} );
		dfp.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "New One-Time Event" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				newOneTimeEventInputWindow();
			}
		} );
		dfp.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "Display All Events" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				showAllEvent();
			}
		} );
		dfp.add( new ButtonTextFS( config.getElm( "ButtonTextFS", "Functional" ), "Exit Calendar" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				stage= 0;
				closePanel();
			}
		} );
		if( dfpc == null )
			dfpc= new PanelContainerScroll( config.getElm( "PanelContainer", "FunctionalPanel" ), null );
		dfpc.resetCont( dfp );
		pl.add( 4, dfpc );
		//
		return pl;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void newrecurEventInputWindow() {
		stage= 1;
		//
		ArrayList <pinnable> rec= new ArrayList <>();
		ButtonInputFS keyword= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ), "* Event Name " );
		ButtonInputFS comm= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ), "Event comment" );
		ButtonInputFS dateIP= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
				"Next Event Date, ex: 2017 1 1 or (blank for today)" );
		ButtonInputFS intervalIP= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
				"* Event Interval, or every xth of month/week = m1/w1/y1" );
		ButtonInputFS imp= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
				"Event Importance" );
		ButtonTextFS confi= new ButtonTextFS( newEvn.getElm( "ButtonTextFS", "confrim" ), "Confirm" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				String kw= keyword.getInputMsg();
				if( kw == null || kw.length() == 0 ){
					this.setFeedBack( "keyword empty", Color.red, 1000 );
					return;
				}
				String com= comm.getInputMsg();
				String date= dateIP.getInputMsg();
				if( date != null && date.length() > 0 ){
					date= helper.parseDate( date );
					if( date == null ){
						this.setFeedBack( "bad start date", Color.red, 1000 );
						return;
					}
				}else{
					DateTime dt= new DateTime();
					date= dt.getYear() + " " + dt.getMonthOfYear() + " " + dt.getDayOfMonth();
				}
				String intv= intervalIP.getInputMsg();
				if( intv == null || intv.length() == 0 ){
					this.setFeedBack( "bad interval", Color.red, 1000 );
					return;
				}
				int intvN= 0;
				switch( intv.charAt( 0 ) ){
					case 'm' :
					case 'M' :
					case 'w' :
					case 'W' :
					case 'y' :
					case 'Y' :
						try{
							intvN= Integer.parseInt( intv.substring( 1, intv.length() ) );
						}catch ( Exception ee ){
							this.setFeedBack( "bad interval", Color.red, 1000 );
							return;
						}
						break;
					default :
						try{
							intvN= Integer.parseInt( intv );
						}catch ( Exception ee ){
							this.setFeedBack( "bad interval", Color.red, 1000 );
							return;
						}
				}
				String impN= imp.getInputMsg();
				if( impN != null && impN.length() > 0 ){
					try{
						Integer.parseInt( impN );
					}catch ( Exception ee ){
						this.setFeedBack( "bad importance value", Color.red, 1000 );
						return;
					}
				}
				if( insert( kw, com, date, intv, impN ) ){
					sqlLoaded= false;
					constructShow();
				}else this.setFeedBack( "bad SQL insert", Color.red, 1000 );
				return;
			}
		};
		rec.add( keyword );
		rec.add( comm );
		rec.add( dateIP );
		rec.add( intervalIP );
		rec.add( imp );
		rec.add( confi );
		datp.resetCont( rec );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void newOneTimeEventInputWindow() {
		stage= 1;
		//
		ArrayList <pinnable> rec= new ArrayList <>();
		ButtonInputFS keyword= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ), "* Event Name " );
		ButtonInputFS comm= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ), "Event comment" );
		ButtonInputFS dateIP= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
				"Event Date, ex: 2017 1 1 or (blank for today)" );
		// event interval is "NA"
		ButtonInputFS imp= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
				"Event Importance" );
		ButtonTextFS confi= new ButtonTextFS( newEvn.getElm( "ButtonTextFS", "confrim" ), "Confirm" ) {
			@Override
			public void B1clickAction( int x, int y ) {
				String kw= keyword.getInputMsg();
				if( kw == null || kw.length() == 0 ){
					this.setFeedBack( "keyword empty", Color.red, 1000 );
					return;
				}
				String com= comm.getInputMsg();
				String date= dateIP.getInputMsg();
				if( date != null && date.length() > 0 ){
					date= helper.parseDate( date );
					if( date == null ){
						this.setFeedBack( "bad start date", Color.red, 1000 );
						return;
					}
				}else{
					DateTime dt= new DateTime();
					date= dt.getYear() + " " + dt.getMonthOfYear() + " " + dt.getDayOfMonth();
				}
				String impN= imp.getInputMsg();
				if( impN != null && impN.length() > 0 ){
					try{
						Integer.parseInt( impN );
					}catch ( Exception ee ){
						this.setFeedBack( "bad importance value", Color.red, 1000 );
						return;
					}
				}
				if( insert( kw, com, date, "NA", impN ) ){
					sqlLoaded= false;
					constructShow();
				}else this.setFeedBack( "bad SQL insert", Color.red, 1000 );
				return;
			}
		};
		rec.add( keyword );
		rec.add( comm );
		rec.add( dateIP );
		rec.add( imp );
		rec.add( confi );
		datp.resetCont( rec );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void showEventofDay( String date ) {
		stage= 2;
		DateTime dt= new DateTime();
		if( date.equals( dt.getYear() + " " + dt.getMonthOfYear() + " " + dt.getDayOfMonth() ) ){
			evn en= new evn( date );
			ArrayList <pinnable> butt= new ArrayList <>();
			for( evn curEvn : en.allONandBeforeEvn( events ) ){
				butt.add( new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
						curEvn.name + " - <" + curEvn.intv + ">", curEvn.timeLeft(), curEvn.ID ) {
					@Override
					public void B1clickAction( int x, int y ) {
						showOptionForEvent( this.getmsgID() );
					}
				} );
			}
			eventsPin= butt;
			datp.resetCont( butt );
		}else{
			evn en= new evn( date );
			ArrayList <pinnable> butt= new ArrayList <>();
			for( evn curEvn : en.allOnDayEvn( events ) ){
				butt.add( new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
						curEvn.name + " - <" + curEvn.intv + ">", curEvn.timeLeft(), curEvn.ID ) {
					@Override
					public void B1clickAction( int x, int y ) {
						showOptionForEvent( this.getmsgID() );
					}
				} );
			}
			eventsPin= butt;
			datp.resetCont( butt );
		}
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void showAllEvent() {
		stage= 2;
		ArrayList <pinnable> butt= new ArrayList <>();
		evn curEvn= null;
		for( int i= 0; i < events.size(); i++ ){
			curEvn= events.get( i );
			butt.add( new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
					curEvn.name + " - <" + curEvn.intv + ">", curEvn.timeLeft(), curEvn.ID ) {
				@Override
				public void B1clickAction( int x, int y ) {
					showOptionForEvent( this.getmsgID() );
				}
			} );
		}
		eventsPin= butt;
		datp.resetCont( butt );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void showOptionForEvent( String ID ) {
		stage= 3;
		ArrayList <pinnable> butt= new ArrayList <>();
		ButtonTextFS opt;
		evn curEvn= null;
		for( int i= 0; i < events.size(); i++ ){
			if( ID.equals( events.get( i ).ID ) ){
				curEvn= events.get( i );
				opt= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ), curEvn.name );
				butt.add( opt );
				if( curEvn.comm != null ){
					opt= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ), curEvn.comm );
					butt.add( opt );
				}
				//
				opt= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ), "Event Done, Log & Exit" ) {
					@Override
					public void B1clickAction( int x, int y ) {
						updateIntv( ID, false, null );
						if( updateSQL( ID ) )
							constructShow();
						else{
							this.setFeedBack( "bad SQL update, please check server", Color.red, 1000 );
						}
					}
				};
				butt.add( opt );
				if( !curEvn.intv.equals( "NA" ) && curEvn.isIntervalDayType() ){
					opt= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
							"Event Done, Log & reset NextDate" ) {
						@Override
						public void B1clickAction( int x, int y ) {
							updateIntv( ID, true, null );
							if( updateSQL( ID ) )
								constructShow();
							else{
								this.setFeedBack( "bad SQL update, please check server", Color.red, 1000 );
							}
						}
					};
					butt.add( opt );
					//
					ButtonInputFS intervalIP= new ButtonInputFS( newEvn.getElm( "ButtonInputFS", "inputField" ),
							"Event Interval, or every xth of month/week = m1/w1/y1" );
					opt= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
							"Event Done, Log & reset Interval with value below" ) {
						@Override
						public void B1clickAction( int x, int y ) {
							if( updateIntv( ID, true, intervalIP.getInputMsg() ) ){
								if( updateSQL( ID ) )
									constructShow();
								else{
									this.setFeedBack( "bad SQL update, please check server", Color.red, 1000 );
								}
							}else this.setFeedBack( "bad new interval", Color.red, 1000 );
						}
					};
					butt.add( opt );
					butt.add( intervalIP );
				}
				//
				ButtonTextFS e1= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
						" " );
				butt.add( e1 );
				butt.add( e1 );
				butt.add( e1 );
				butt.add( e1 );
				ButtonTextFS dele= new ButtonTextFS( CalendarPL.getElm( "ButtonTextFS", "eventShow" ),
						"delete Event" ) {
					@Override
					public void B1clickAction( int x, int y ) {
						if( deleteID( ID ) ){
							sqlLoaded= false;
							constructShow();
						}else{
							this.setFeedBack( "bad SQL delete, please check server", Color.red, 1000 );
						}
					}
				};
				butt.add( dele );
				break;
			}
		}
		datp.resetCont( butt );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private void loadSQL() {
		Thread tt= new Thread() {
			@Override
			public void run() {
				events= new BArray <>();
				//
				ArrayList <sqlResult> res= mySQLportal.getByFunc( sqlfuncName, 0, null, null );
				if( res != null && res.size() > 0 ){
					for( sqlResult rs : res ){
						evn eventI= new evn();
						eventI.name= (String)rs.val.get( 3 );
						eventI.comm= (String)rs.val.get( 5 );
						eventI.intv= (String)rs.val.get( 4 );
						eventI.imp= (String)rs.val.get( 12 );
						eventI.sd= (String)rs.val.get( 9 );
						eventI.ID= (String)rs.val.get( 0 ) + (String)rs.val.get( 1 );
						eventI.idA= (String)rs.val.get( 0 );
						eventI.idB= (String)rs.val.get( 1 );
						events.add( eventI );
					}
				}
				sqlLoaded= true;
				constructShow();
			}
		};
		tt.start();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private boolean deleteID( String ID ) {
		evn curEvn= null;
		for( int i= 0; i < events.size(); i++ ){
			if( ID.equals( events.get( i ).ID ) ){
				curEvn= events.get( i );
				return mySQLportal.delete( curEvn.idA, curEvn.idB );
			}
		}
		return false;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	public boolean updateIntv( String ID, boolean resetSD, String newi ) {
		evn curEvn= null;
		for( int i= 0; i < events.size(); i++ ){
			if( ID.equals( events.get( i ).ID ) ){
				curEvn= events.get( i );
				if( curEvn.isIntervalDayType() ){
					if( !resetSD ){
						curEvn.incInterval();
						return true;
					}else{
						if( newi != null ){
							try{
								Integer.parseInt( newi );
							}catch ( Exception ee ){
								return false;
							}
							curEvn.intv= newi;
							curEvn.resetSD();
							curEvn.incInterval();
							return true;
						}else{
							curEvn.resetSD();
							curEvn.incInterval();
							return true;
						}
					}
				}else{
					curEvn.incInterval();
					return true;
				}
			}
		}
		return false;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private boolean updateSQL( String ID ) {
		evn curEvn= null;
		for( int i= 0; i < events.size(); i++ ){
			if( ID.equals( events.get( i ).ID ) ){
				curEvn= events.get( i );
				//
				ArrayList <String> sqlName= new ArrayList <>();
				sqlName.add( "functional" );
				sqlName.add( "keyword" );
				sqlName.add( "content" );
				sqlName.add( "comment" );
				sqlName.add( "startDate" );
				sqlName.add( "importancy" );
				//
				ArrayList <String> sqlVal= new ArrayList <>();
				sqlVal.add( sqlfuncName );
				sqlVal.add( curEvn.name );
				sqlVal.add( curEvn.intv );
				sqlVal.add( curEvn.comm );
				sqlVal.add( curEvn.sd );
				sqlVal.add( curEvn.imp );
				if( mySQLportal.update( curEvn.idA, curEvn.idB, sqlName, sqlVal ) ){
					//
					sqlLoaded= false;
					return true;
				}else return false;
			}
		}
		return false;
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private boolean insert( String keyword, String comm, String sd, String intv, String imp ) {
		ArrayList <String> sqlName= new ArrayList <>();
		sqlName.add( "functional" );
		sqlName.add( "keyword" );
		sqlName.add( "content" );
		sqlName.add( "comment" );
		sqlName.add( "startDate" );
		sqlName.add( "importancy" );
		//
		ArrayList <String> sqlVal= new ArrayList <>();
		sqlVal.add( sqlfuncName );
		sqlVal.add( keyword );
		sqlVal.add( intv );
		sqlVal.add( comm );
		sqlVal.add( sd );
		sqlVal.add( imp );
		//
		sqlLoaded= false;
		//
		return mySQLportal.insert( sqlName, sqlVal );
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	@Override
	protected void closePanel() {
		switch( stage ){
			case 0 :
				cal= null;
				super.closePanel();
				break;
			case 1 :
			case 2 :
				datp.resetCont( weekdaysPin );
				stage= 0;
				break;
			case 3 :
				datp.resetCont( eventsPin );
				stage= 2;
				break;
		}
	}

	@Override
	protected void windowClosebyTitleInput() {
		stage= 0;
		closePanel();
	}

	/*||----------------------------------------------------------------------------------------------
	 |||
	||||--------------------------------------------------------------------------------------------*/
	private class evn implements Comparable <evn> {
		String	name;
		String	intv;
		String	comm;
		String	sd;
		String	imp;
		String	ID, idA, idB;

		//
		public evn() {}

		public void resetSD() {
			DateTimeFormatter formatter= DateTimeFormat.forPattern( "yyyy MM dd" );
			DateTime dt= new DateTime();
			sd= dt.toString( formatter );
		}

		public evn( DateTime dt ) {
			this.sd= dt.getYear() + " " + dt.getMonthOfYear() + " " + dt.getDayOfMonth();
		}

		public evn( String dt ) {
			this.sd= dt;
		}

		public String timeLeft() {
			if( sd == null )
				return null;
			//
			DateTimeFormatter formatter= DateTimeFormat.forPattern( "yyyy MM dd" );
			DateTime dt= formatter.parseDateTime( sd );
			DateTime now= new DateTime();
			int day= (int) ( ( dt.getMillis() - now.getMillis() ) / 1000.0 / 3600 / 24 );
			if( day > 1 )
				return day + " days left";
			else if( day == 1 )
				return "1 day left";
			else if( day == 0 ){
				int hour= (int) ( ( dt.getMillis() - now.getMillis() ) / 1000.0 / 3600 );
				if( hour > 1 )
					return hour + " hours left";
				else if( hour == 1 )
					return "1 hour left";
				else return "due now";
			}else if( day == -1 ){
				return "past due 1 day";
			}else{
				return "past due " + ( -1 * day ) + " day";
			}
		}

		public int allONandBefore( BArray <evn> inp ) {
			int tot= 0;
			for( int i= 0; i < inp.size(); i++ ){
				if( inp.get( i ).compareDate( this ) <= 0 )
					tot++ ;
			}
			return tot;
		}

		public ArrayList <evn> allONandBeforeEvn( BArray <evn> inp ) {
			ArrayList <evn> ret= new ArrayList <>();
			for( int i= 0; i < inp.size(); i++ ){
				if( inp.get( i ).compareDate( this ) <= 0 )
					ret.add( inp.get( i ) );
			}
			return ret;
		}

		public int allOnDay( BArray <evn> inp ) {
			int tot= 0;
			for( int i= 0; i < inp.size(); i++ ){
				if( inp.get( i ).compareDate( this ) == 0 )
					tot++ ;
			}
			return tot;
		}

		public void incInterval() {
			if( sd == null || intv == null )
				return;
			DateTimeFormatter formatter= DateTimeFormat.forPattern( "yyyy MM dd" );
			DateTime dt= formatter.parseDateTime( sd );
			if( isIntervalDayType() ){
				dt= dt.plusDays( Integer.parseInt( intv ) );
				sd= dt.toString( formatter );
				return;
			}else{
				DateTime now= new DateTime();
				switch( intv.charAt( 0 ) ){
					case 'w' :
					case 'W' :
						dt= dt.withWeekOfWeekyear( dt.getWeekOfWeekyear() ).withDayOfWeek( 1 ).plusWeeks( 1 );
						dt= dt.plusDays( Integer.parseInt( intv.substring( 1, intv.length() ) ) - 1 );
						sd= dt.toString( formatter );
						break;
					case 'M' :
					case 'm' :
						dt= dt.withMonthOfYear( dt.getMonthOfYear() ).withDayOfMonth( 1 ).plusMonths( 1 );
						dt= dt.plusDays( Integer.parseInt( intv.substring( 1, intv.length() ) ) - 1 );
						sd= dt.toString( formatter );
						break;
					case 'y' :
					case 'Y' :
						dt= dt.withYear( dt.getYear() ).withDayOfYear( 1 ).plusYears( 1 );
						dt= dt.plusDays( Integer.parseInt( intv.substring( 1, intv.length() ) ) - 1 );
						sd= dt.toString( formatter );
						break;
				}
			}
		}

		public boolean isIntervalDayType() {
			if( intv != null ){
				switch( intv.charAt( 0 ) ){
					case 'w' :
					case 'W' :
					case 'M' :
					case 'm' :
					case 'y' :
					case 'Y' :
						return false;
					default :
						return true;
				}
			}
			return true;
		}

		public ArrayList <evn> allOnDayEvn( BArray <evn> inp ) {
			ArrayList <evn> ret= new ArrayList <>();
			for( int i= 0; i < inp.size(); i++ ){
				if( inp.get( i ).compareDate( this ) == 0 )
					ret.add( inp.get( i ) );
			}
			return ret;
		}

		public int compareDate( evn o ) {
			return helper.parseDate2Int( sd ) - helper.parseDate2Int( o.sd );
		}

		@Override
		public int compareTo( evn o ) {
			return helper.parseDate2Int( sd ) - helper.parseDate2Int( o.sd );
		}

		public String print() {
			return name + " " + comm + " " + sd + " " + imp + " " + intv;
		}
	}
}
