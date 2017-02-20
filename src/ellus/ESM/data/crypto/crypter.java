package ellus.ESM.data.crypto;

//change to long!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
/* -----------------------------------------------------------------------------
 *
 * -----------------------------------------------------------------------------
 */
public class crypter {
	// random rotaiontional shitfting cipher.
	// valid string and key are asci 32 to 126. all chars.
	// 32 to 126 -> 1 to 95 minus 31.
	/* --------------------------------------------------------------------------
	 * ---
	 * --------------------------------------------------------------------------
	 * --- */
	public static String encryption( String key, String msg ) {
		return encryptS( key, encryptS( key, encryptS( key, msg ) ) );
	}

	/* --------------------------------------------------------------------------
	 * ---
	 * --------------------------------------------------------------------------
	 * --- */
	public static String decryption( String key, String cip ) {
		return decryptS( key, decryptS( key, decryptS( key, cip ) ) );
	}

	/* --------------------------------------------------------------------------
	 * ---
	 * --------------------------------------------------------------------------
	 * --- */
	private static String encryptS( String key, String msg ) {
		char[] ret= new char[msg.length()];
		int KInd= 0, MInd= 0;
		int max= key.length();
		int m, k, c;
		int count= 0;
		// all ret init is null.
		for( int i= 0; i < msg.length(); i++ ){
			ret[i]= (char)0;
		}
		while( count++ < msg.length() ){
			if( msg.charAt( MInd ) == '\n' ){
				ret[MInd]= '\n';
				MInd++ ;
				if( MInd == ret.length )
					MInd= 0;
				continue;
			}
			while( ret[MInd] != (char)0 ){
				MInd++ ;
				if( MInd == ret.length )
					MInd= 0;
			}
			// System.out.println( "si: " + MInd );
			m= msg.charAt( MInd ) - 31;
			k= key.charAt( KInd ) - 31;
			c= m + k;
			if( c > 95 ){
				ret[MInd]= (char) ( c - 95 + 31 );
				MInd+= 1 * Math.abs( m - k );
			}else{
				ret[MInd]= (char) ( c + 31 );
				MInd+= -1 * Math.abs( m - k );
			}
			// System.out.println( m + " " + k + " " + c );
			while( MInd >= msg.length() )
				MInd-= msg.length();
			while( MInd < 0 )
				MInd+= msg.length();
			// System.out.println( "si: " + MInd );
			if( ++KInd == key.length() )
				KInd= 0;
		}
		String retS= new String();
		for( int i= 0; i < ret.length; i++ ){
			retS+= "" + ret[i];
		}
		// System.out.println( "Encryption finished." );
		return retS;
	}

	/* --------------------------------------------------------------------------
	 * ---
	 * --------------------------------------------------------------------------
	 * --- */
	private static String decryptS( String key, String cip ) {
		try{
			char[] ret= new char[cip.length()];
			int KInd= 0, CInd= 0;
			int max= key.length();
			int m, k, c;
			int count= 0;
			// all ret init is null.
			for( int i= 0; i < cip.length(); i++ ){
				ret[i]= (char)0;
			}
			while( count++ < cip.length() ){
				if( cip.charAt( CInd ) == '\n' ){
					ret[CInd]= '\n';
					CInd++ ;
					if( CInd == ret.length )
						CInd= 0;
					continue;
				}
				while( ret[CInd] != (char)0 ){
					CInd++ ;
					if( CInd == ret.length )
						CInd= 0;
				}
				// System.out.println( "si: " + CInd );
				c= cip.charAt( CInd ) - 31;
				k= key.charAt( KInd ) - 31;
				m= c - k;
				if( m < 1 ){
					ret[CInd]= (char) ( m + 95 + 31 );
					m= m + 95;
					CInd+= 1 * Math.abs( m - k );
				}else{
					ret[CInd]= (char) ( m + 31 );
					CInd+= -1 * Math.abs( m - k );
				}
				// System.out.println( m + " " + k + " " + c );
				while( CInd >= cip.length() )
					CInd-= cip.length();
				while( CInd < 0 )
					CInd+= cip.length();
				// System.out.println( "si: " + CInd );
				if( ++KInd == key.length() )
					KInd= 0;
			}
			String retS= new String();
			for( int i= 0; i < ret.length; i++ ){
				retS+= "" + ret[i];
			}
			// System.out.println( "Decryption finished." );
			return retS;
		}catch ( Exception ee ){
			ee.printStackTrace();
		}finally{
			;
		}
		return null;
	}
}
