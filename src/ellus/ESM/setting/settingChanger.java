package ellus.ESM.setting;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;



/*
 *
 *
 * this use java window instead of custom window.
 * this is NO Longer used.
 *
 *
 */
public class settingChanger extends JFrame {
	private int						locax				= -1;
	private int						locay				= -1;
	private int						si, ei;
	private ArrayList <Integer>		ind					= new ArrayList <>();
	private ArrayList <JTextField>	jtl					= new ArrayList <>();
	private ArrayList <JLabel>		jll					= new ArrayList <>();
	private JTextField				jt;
	private JLabel					jl;
	private SMan					SM					= null;
	//
	private JFrame					handler				= this;
	private JPanel					pan					= new JPanel();
	//
	public static final int			txtInputWindowWidth	= 950;
	public static final int			txtInputMsgSiz		= 18;

	public settingChanger( int x, int y, int w, int h, int sInd, int eInd, SMan sm ) {
		this.locax= x;
		this.locay= y;
		si= sInd;
		ei= eInd;
		this.SM= sm;
		//
		init();
		if( w != -1 && h != -1 )
			this.setSize( w, h );
		else this.setSize( txtInputWindowWidth, ind.size() * ( txtInputMsgSiz + 12 ) + 20 );
		if( locax != -1 && locay != -1 )
			this.setLocation( locax, locay );
		else this.setLocationRelativeTo( null );
		this.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
		this.setVisible( true );
		init();
	}

	private void init() {
		this.setVisible( false );
		//
		pan= new JPanel();
		this.setContentPane( pan );
		//
		pan.setLayout( new GridBagLayout() );
		GridBagConstraints c= new GridBagConstraints();
		cK ckey;
		//
		Action exit= new AbstractAction() {
			@Override
			public void actionPerformed( ActionEvent e ) {
				handler.setVisible( false );
				handler.dispatchEvent( new WindowEvent( handler,
						WindowEvent.WINDOW_CLOSING ) );
				return;
			}
		};
		//
		for( int i= si; i <= ei; i++ ){
			if( SM == null ){
				ckey= SMan.getCK( i );
				if( ckey == null )
					continue;
				else{
					jt= new JTextField( ckey.cont );
					jl= new JLabel( "[" + ckey.type + "]\t" + ckey.comment );
					Font ff= jl.getFont().deriveFont( Font.PLAIN, txtInputMsgSiz );
					jt.setFont( ff );
					jl.setFont( ff );
					jtl.add( jt );
					jll.add( jl );
					if( ckey.type.equals( "int" ) ){
						jt.addMouseListener( new mouseADP( jt, i ) {
							@Override
							public void mouseEntered( MouseEvent arg0 ) {
								( (JTextField)store ).requestFocus();
							}

							@Override
							public void mouseClicked( MouseEvent arg ) {
								try{
									if( arg.getButton() == 1 ){
										// rewrite setting to file.
										SMan.changeSetting( ind,
												( Integer.parseInt( SMan.getCK( ind ).cont ) - 1 ) + "" );
										// call for reconstruct.
									}
									if( arg.getButton() == 3 ){
										SMan.changeSetting( ind,
												( Integer.parseInt( SMan.getCK( ind ).cont ) + 1 ) + "" );
									}
									init();
								}catch ( Exception ee ){
									ee.printStackTrace();
								}
							}
						} );
					}else if( ckey.type.equals( "Color" ) ){
						jt.addMouseListener( new mouseADP( jt, i ) {
							@Override
							public void mouseEntered( MouseEvent arg0 ) {
								( (JTextField)store ).requestFocus();
							}

							@Override
							public void mouseClicked( MouseEvent arg ) {
								if( arg.getButton() == 1 ){
									( (JTextField)store ).setText( "" );
								}
								if( arg.getButton() == 3 ){
									SMan.changeSetting( ind,
											// replace comma in the line if present.
											( ( (JTextField)store ).getText().replace( ',', ' ' ) ) );
									init();
								}
							}
						} );
					}
					jt.getInputMap().put(
							KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "VK_ESCAPE" );
					jt.getActionMap().put( "VK_ESCAPE", exit );
					//
					c.fill= GridBagConstraints.HORIZONTAL;
					c.weightx= 0.29;
					c.gridx= 0;
					c.gridy= ind.size();
					//c.gridheight= txtInputMsgSiz + 2;
					pan.add( jt, c );
					//
					c.fill= GridBagConstraints.HORIZONTAL;
					c.weightx= 0.71;
					c.gridx= 1;
					c.gridy= ind.size();
					//c.gridheight= txtInputMsgSiz + 2;
					pan.add( jl, c );
					//
					ind.add( new Integer( i ) );
				}
			}else{
				ckey= SM.getCKInd( i );
				if( ckey == null )
					continue;
				else{
					jt= new JTextField( ckey.cont );
					jl= new JLabel( "[" + ckey.type + "]\t" + ckey.comment );
					Font ff= jl.getFont().deriveFont( Font.PLAIN, txtInputMsgSiz );
					jt.setFont( ff );
					jl.setFont( ff );
					jtl.add( jt );
					jll.add( jl );
					if( ckey.type.equals( "int" ) ){
						jt.addMouseListener( new mouseADP( jt, i ) {
							@Override
							public void mouseEntered( MouseEvent arg0 ) {
								( (JTextField)store ).requestFocus();
							}

							@Override
							public void mouseClicked( MouseEvent arg ) {
								try{
									if( arg.getButton() == 1 ){
										// rewrite setting to file.
										SM.changeSettingInd( ind,
												( Integer.parseInt( SM.getCKInd( ind ).cont ) - 1 ) + "" );
										// call for reconstruct.
									}
									if( arg.getButton() == 3 ){
										SM.changeSettingInd( ind,
												( Integer.parseInt( SM.getCKInd( ind ).cont ) + 1 ) + "" );
									}
									init();
								}catch ( Exception ee ){
									ee.printStackTrace();
								}
							}
						} );
					}else if( ckey.type.equals( "Color" ) ){
						jt.addMouseListener( new mouseADP( jt, i ) {
							@Override
							public void mouseEntered( MouseEvent arg0 ) {
								( (JTextField)store ).requestFocus();
							}

							@Override
							public void mouseClicked( MouseEvent arg ) {
								if( arg.getButton() == 1 ){
									( (JTextField)store ).setText( "" );
								}
								if( arg.getButton() == 3 ){
									SM.changeSettingInd( ind,
											( ( (JTextField)store ).getText() ) );
									init();
								}
							}
						} );
					}
					jt.getInputMap().put(
							KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "VK_ESCAPE" );
					jt.getActionMap().put( "VK_ESCAPE", exit );
					//
					c.fill= GridBagConstraints.HORIZONTAL;
					c.weightx= 0.29;
					c.gridx= 0;
					c.gridy= ind.size();
					//c.gridheight= txtInputMsgSiz + 2;
					pan.add( jt, c );
					//
					c.fill= GridBagConstraints.HORIZONTAL;
					c.weightx= 0.71;
					c.gridx= 1;
					c.gridy= ind.size();
					//c.gridheight= txtInputMsgSiz + 2;
					pan.add( jl, c );
					//
					ind.add( new Integer( i ) );
				}
			}
		}
		//
		pan.getInputMap().put(
				KeyStroke.getKeyStroke( KeyEvent.VK_ESCAPE, 0 ), "VK_ESCAPE" );
		pan.getActionMap().put( "VK_ESCAPE", exit );
		//
		this.setVisible( true );
	}
}
