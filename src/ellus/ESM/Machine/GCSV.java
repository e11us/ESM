package ellus.ESM.Machine;

import java.awt.Color;
import java.awt.Font;



/* -----------------------------------------------------------------------------
 * Global Constant Static Variable
 * -----------------------------------------------------------------------------
 */
public class GCSV {
	//
	/*
	// minimalist BG.
	public static final String[]	ImgBG_minimal				= {
			folderBgImg + "/Extra/minimalist/minimalist (1).jpg",
			folderBgImg + "/Extra/minimalist/minimalist (2).jpg",
			folderBgImg + "/Extra/minimalist/minimalist (3).jpg",
			folderBgImg + "/Extra/minimalist/minimalist (4).jpg" };
	//
	public static final String		ImgBGDictionary				= folderBgImg + "/matrix (1).jpg";
	public static final String		phantomJSbrowser			= "./sys/phantomjs/phantomjs.exe";
	public static final String		Chromebrowser				= "./sys/GoogleChrome/chromedriver.exe";
	public static final String		ChromeExtAdBlock			= "./sys/GoogleChrome/adBlock.crx";
	public static final String		DictWordFileBeg				= "Dict_";
	public static final String		NoteBoardSettingFile		= ".setting";
	public static final String		SubLimeTxtEXE				= folderSysMain
			+ "/Sublime Text 2.0.2 x64/sublime_text.exe";
	public static final String		ColorPicker					= "file:///C:/_SDK/java/ProjectSpace/ESmachine/sys/Color%20Picker/HTML%20Color%20Picker.html";
	*/
	//
	// ------------------------------ board.
	public static final int			boardAutoRefreshDefaultFPS	= 40;
	public static final int			boardStdWidth				= 1608;
	public static final int			boardStdHeight				= 1200;
	public static final Color		boardBGdefaultColor			= new Color( 0, 102, 255, 180 );
	public static final Color		BoardBG1					= new Color( 240, 255, 240, 5 );
	public static final Color		BoardBG2					= new Color( 180, 180, 180, 50 );
	public static final int			boardArrKeyNavSpeed			= 55;
	public static final int			boardMouseWheelNavSpeed		= 55;
	public static final float		boardOpaRat					= 0.965f;
	public static final float		boardBgImgOpaRat			= 0.9f;
	//
	// ------------------------------ board - Home
	//
	public static final int			HomeEdgeSpaceX				= 60;
	public static final int			HomeEdgeSpaceY				= 100;
	public static final int			HomePaneSpacing				= 22;
	//
	public static final int			HomeShadeEdgeOffSetX1		= 30;
	public static final int			HomeShadeEdgeOffSetX2		= 53;
	public static final int			HomeShadeEdgeOffSetY1		= 60;
	public static final int			HomeShadeEdgeOffSetY2		= 80;
	public static final double		HomeSideShadeTapperRat		= 0.05;
	public static final Color		HomeColorShadeEdge			= new Color( 255, 255, 255, 190 );
	public static final Color		HomeColorSideShadeTop1		= new Color( 179, 218, 255 );
	public static final Color		HomeColorSideShadeTop2		= new Color( 10, 10, 10, 90 );
	//
	public static final int			HomeSepaWidth				= 20;
	public static final int			HomeSepaHeight				= 20;
	//
	// ------------------------------ board - PWM
	public static final int			PWMboardWidth				= 1450;
	public static final int			PWMboardHeight				= 640;
	public static final int			PWMContPanX					= 40;
	public static final int			PWMContPanY					= 60;
	public static final int			PWMPaneSpacing				= 22;
	public static final int			PWMpwWidth					= 320;
	public static final int			PWMpwHeight					= 55;
	//
	// ------------------------------ board - weblink
	public static final int			weblinkWidth				= 1200;
	public static final int			weblinkboardWidth			= 1325;
	public static final int			weblinkboardHeight			= 940;
	//
	// ------------------------------ board - Dictionary
	public static final int			maxSearchResShow			= 6;
	public static final Color		DictColorBg					= new Color( 235, 235, 235, 20 );
	public static final Color		DictColorBg2				= new Color( 235, 235, 235, 60 );
	public static final int			DictBoardWidth				= 1344;
	public static final int			DictBoardHeight				= 900;
	public static final int			DictBoardContPanX			= 63;
	public static final int			DictBoardContPanY			= 80;
	public static final int			DictWordWidth				= 330;
	public static final int			DictWordHeight				= 60;
	public static final int			DictBoardContPanSpacing		= 25;
	public static final int			DictBoardSearchBoxWidth		= 500;
	public static final Color		DictColorDefFont			= new Color( 255, 255, 255, 255 );
	public static final String		DictDefSeperator			= "----------------------------------------------------------";
	public static final int			DictFontSize				= 28;
	public static final int			DictDefLineMax				= 50;
	public static Font				DictFont					= null;
	//
	// ------------------------------ board - NoteBoard
	public static final float		noteBoardFrameOpaRat		= 0.99f;
	public static final Color		noteBoardBgColor1			= new Color( 0, 191, 255, 230 );
	public static final Color		noteBoardBgColor2			= new Color( 100, 100, 100, 200 );
	//
	// ------------------------------ pinnable - Button
	public static final int			ButtonStdWidth				= 200;
	public static final int			ButtonStdHeight				= 120;
	public static final int			ButtonStdArkS				= 37;
	public static final int			ButtonStdTxtOffset			= 5;
	public static final int			ButtonTxtFontSiz			= 25;
	//
	// ------------------------------ pinnable - ButtonNew .
	public static final int			ButtonNewWidth				= 155;
	public static final int			ButtonNewHeight				= 105;
	public static final int			ButtonNewWidOffSet			= 32;
	public static final int			ButtonNewHeiOffSet			= 17;
	public static final int			ButtonNewEdgeTick			= 2;
	public static final Color		ButtonNewColorEdge			= new Color( 255, 255, 255, 248 );
	public static final Color		ButtonNewColorBG			= new Color( 235, 235, 235, 90 );
	public static final Color		ButtonNewColorEdgeHL		= new Color( 10, 10, 255, 185 );
	public static final double		ButtonNewCSratiWidth		= 0.3;
	public static final double		ButtonNewCSratiHeight		= 0.3;
	//
	// ------------------------------ pinnable - ButtonTxt1
	public static final Color		ButtonTxtFontdefColor		= Color.WHITE;
	public static final Color		ButtonTxtBGdefColor			= new Color( 77, 77, 255, 155 );
	//
	// ------------------------------ pinnable - ButtonTxt2
	//public static final int ButtonTxt2
	public static Font				ButtonTxt2Font				= null;
	public static final int			ButtonTxt2TxtSiz			= 22;
	public static final int			ButtonTxt2TxtPosOffSetX		= 15;
	public static final int			ButtonTxt2TxtPosOffSetY		= 75;
	public static final int			ButtonTxt2Width				= 220;
	public static final int			ButtonTxt2Height			= 105;
	public static final int			ButtonTxt2WidOffSet			= 32;
	public static final int			ButtonTxt2HeiOffSet			= 17;
	public static final int			ButtonTxt2EdgeTick			= 2;
	public static final Color		ButtonTxt2ColorBG1			= new Color( 235, 235, 235, 90 );
	public static final Color		ButtonTxt2ColorBG2			= new Color( 185, 185, 185, 85 );
	public static final Color		ButtonTxt2ColorEdge			= new Color( 255, 255, 255, 248 );
	public static final Color		ButtonTxt2ColorFont			= new Color( 255, 255, 255, 255 );
	public static final Color		ButtonTxt2ColorEdgeHL		= new Color( 51, 50, 255, 185 );
	// ------------------------------ pinnable - ButtonTxt3
	public static Font				ButtonTxt3Font				= null;
	public static final int			ButtonTxt3TxtSiz			= 17;
	public static final int			ButtonTxt3Width				= 250;
	public static final int			ButtonTxt3Height			= 45;
	public static final int			ButtonTxt3SideShadeWidth	= 12;
	public static final int			ButtonTxt3TxtBgPosOffSetX	= ButtonTxt3SideShadeWidth + 5;
	public static final int			ButtonTxt3TxtBgPosOffSetY	= 2;
	public static final Color		ButtonTxt3ColorBG1			= new Color( 235, 235, 235, 110 );
	public static final Color		ButtonTxt3ColorBG2			= new Color( 185, 185, 185, 20 );
	public static final Color		ButtonTxt3ColorFont			= new Color( 255, 255, 255, 230 );
	public static final Color		ButtonTxt3ColorShade		= new Color( 255, 255, 255, 248 );
	//
	// ------------------------------ pinnable - ButtonIMG
	public static final float		ButtonImgTransparentRate	= 0.61f;
	public static final float		ButtonImgTransparentRateHL	= 0.99f;
	//
	// ------------------------------ pinnable - ButtonYT
	public static final int			ButtonYTwidth				= 770;
	public static final int			ButtonYTheight				= 70;
	//
	// ------------------------------ pinnable - ButtonOO
	public static Font				ButtonOOFont				= null;
	public static final Color		ButtonOOBgColorHL			= new Color( 0, 77, 153 );
	public static final Color		ButtonOOBgColor				= new Color( 153, 231, 255, 85 );
	public static final Color		ButtonOOonColor				= Color.green;
	public static final Color		ButtonOOoffColor			= Color.WHITE;
	public static final int			ButtonOOWidth				= 250;
	public static final int			ButtonOOHeight				= 50;
	public static final int			ButtonOOlightPosX			= 225;
	public static final int			ButtonOOlightPosY			= 10;
	public static final int			ButtonOOlightSiz			= 14;
	public static final int			ButtonOOTxtOffsetX			= 9;
	public static final int			ButtonOOTxtOffsetY			= 14;
	public static final int			ButtonOOTxtSiz				= 20;
	// ------------------------------ pinnable - ButtonRaid
	public static final int			ButtonBoxWidth				= 46;
	public static final int			ButtonBoxHeight				= 46;
	public static final Color		ButtonBoxColorEdge			= new Color( 255, 255, 255, 100 );
	public static final Color		ButtonBoxColor				= new Color( 0, 0, 0, 35 );
	//
	// ------------------------------ pinnable - pinTxt (userTxt )
	public static final int			pinTxtSearchBoxWidth		= 700;
	public static final int			pinTxtHashKeyBoxWidth		= 350;
	public static final Color		pinTxtFontDefColor			= Color.WHITE;
	public static final Color		pinTxtFontInpColor			= Color.blue;
	public static final Color		pinTxtBGdefColor			= new Color( 255, 204, 0, 80 );
	public static final int			pinTxtTxtPosOffSetX			= 7;
	public static final int			pinTxtTxtPosOffSetY			= 65;
	public static final int			pinTxtFontSiz				= 30;
	//
	// ------------------------------ pinnable - UserInp
	public static final Color		userInpBGdefColor			= new Color( 255, 204, 0, 80 );
	//
	// ------------------------------ pinnable - Panel
	public static final Color		PanelScrollColorEdge		= new Color( 255, 255, 255, 248 );
	public static final Color		PanelScrollColorBg			= new Color( 235, 235, 235, 60 );
	public static final Color		PanelScrollColorBg2			= new Color( 235, 235, 235, 61 );
	public static final Color		PanelColorEdgeDef			= new Color( 255, 255, 255, 5 );
	public static final int			PanelScrollscrollSpd		= 22;
	public static final int			PanelScrollEdgeDotSiz		= 4;
	public static final Color		PanelScrollColorEdgeDot		= new Color( 0, 255, 204, 250 );
	//
	public static final Color		PanelScrollTxtColorEdgeDot	= new Color( 0, 255, 204, 250 );
	public static final Color		PanelScrollTxtColorBg		= new Color( 75, 75, 75, 100 );
	public static final Color		PanelScrollTxtColorBg2		= new Color( 55, 55, 55, 135 );
	public static final Color		PanelScrollTxtColorTx		= new Color( 255, 255, 255, 220 );
	public static Font				PaneScrollTxtFont			= null;
	public static final int			PaneScrollTxtFontSiz		= 16;
	public static final int			PaneScrollTxtTxtPosOffSetX	= 11;
	public static final int			PaneScrollTxtTxtPosOffSetY	= 5;
	public static final int			PaneScrollTxtTxtLineSepaDis	= 3;
	//
	public static final int			PanelScroll2SpacingX		= 22;
	public static final int			PanelScroll2SpacingY		= 20;
	//------------------------------ pinnable - pin line
	public static final int			pinLineHoriLineLen			= 10;
	public static final int			pinLineVertiLineLen			= 20;
	public static final Color		pinLineColorDef				= Color.white;
	//
	// ------------------------------ pinnable - pin link
	public static final Color		pinLinkColor				= new Color( 255, 255, 255, 70 );
	public static final int			pinLinkLinkThic				= 3;
	//
	// ------------------------------ Gadget - time
	public static final Color		timeDisplayColorFont		= new Color( 51, 51, 255, 255 );
	public static final int			timeDisplayFontSiz			= 26;
	public static Font				timeDisplayFont				= null;
	// ------------------------------ Gadget - date countdown.
	public static final int			DCDwidth					= 165;
	public static final int			DCDHeight					= 185;
	public static final int			DCDcircleOffSetX			= 10;
	public static final int			DCDcircleOffSetY			= 10;
	public static final int			DCDcircleDiameter			= 145;
	public static final int			DCDcircleThick				= 5;
	public static final Color		DCDcircleColor				= new Color( 51, 51, 255, 255 );
	public static final Color		DCDcircleGoneColor			= new Color( 255, 255, 255, 51 );
	public static final Color		DCDtxtColor					= new Color( 102, 204, 255, 255 );
	public static final int			DCDtxtOffSetX				= 7;
	public static final int			DCDtxtOffSetY				= 163;
	public static final int			DCDtxtSiz					= 21;
	public static final int			DCDtxtDayNumSiz				= 28;
	public static Font				DCDFont						= null;
	// ------------------------------ Gadget - Timer.
	public static final int			TimerWidth					= 250;
	public static final int			TimerHeight					= 130;
	public static final double		TimerEdgeSpikRate			= 0.05;
	public static final int			TimerNameOffSetY			= 11;
	public static final int			TimerTimeOffSetY			= 45;
	public static final int			TimerBarOffSetX				= 31;
	public static final int			TimerBarOffSetY				= 91;
	public static final int			TimerBarHeight				= 17;
	public static final int			TimerFontNameSize			= 22;
	public static final int			TimerFontTimeSize			= 28;
	public static final Color		TimerColorEdgeSpike			= Color.BLUE;
	public static final Color		TimerColorTxt				= Color.white;
	public static final Color		TimerColorBar				= new Color( 0, 179, 134, 100 );
	// ------------------------------ tracer
	public static final double		tracerSpeedMin				= -13;
	public static final double		tracerSpeedMax				= 13;
	public static final double		tracerSpeedChangeMax		= 1.3;
	// ------------------------------ Dynamic BG vertex
	public static final Color		vertexBgColorLine			= new Color( 235, 235, 235, 120 );
	public static final int			vertexBgLineTick			= 3;
	public static final int			vertexBgOffScreenDis		= 300;
	public static final double		vertexBgNodePerPix			= 0.000032;
	public static final int			vertexBgLinePerNode			= 4;
	public static final double		vertexBgColorChangeThres	= 0.34;
	// ------------------------------ Dynamic BG matrix
	public static final int			matrixBgSizeMax				= 100000;
	public static final int			matrixBgSizeMin				= 100;
	public static Font				matrixBgFont				= null;
	public static final int			matrixFontSiz				= 18;
	//
	// ------------------------------ YouTube Dler V2
	public static final String[]	YTDLV2Init					= { "python", "import pafy" };
	public static final String		YTDLV2AddrTestHead			= "vid= pafy.new( \"";
	public static final String		YTDLV2AddrTestTail			= "\" )";
	public static final String		YTDLV2AddrGetTitle			= "vid.title";
	// ------------------------------ dictionary getter
	public static final int			dictGetterGetword_retry_max	= 3;
	public static final int			dictGetterBrowser_use_max	= 470;
}
