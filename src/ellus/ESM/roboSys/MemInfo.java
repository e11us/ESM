package ellus.ESM.roboSys;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import ellus.ESM.setting.SCon;



public class MemInfo {
	static Sigar sig= null;

	public static double getUsed() {
		if( SCon._levelTesting_useNativeLib ){
			if( sig == null )
				init();
			try{
				return sig.getMem().getUsedPercent();
			}catch ( SigarException e ){
				e.printStackTrace();
			}
			return 0;
		}else return 0;
	}

	public static long getTotal() {
		if( SCon._levelTesting_useNativeLib ){
			if( sig == null )
				init();
			try{
				return sig.getMem().getTotal();
			}catch ( SigarException e ){
				e.printStackTrace();
			}
			return 0;
		}else return 0;
	}

	private static void init() {
		sig= new Sigar();
	}
}
