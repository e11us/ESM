package ellus.ESM.roboSys;

import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import ellus.ESM.setting.SCon;



public class NetLocInfo {
	static Sigar sig= null;

	public static String getIP() {
		if( SCon._levelTesting_useNativeLib ){
			if( sig == null )
				init();
			try{
				return sig.getNetInterfaceConfig().getNetmask();
			}catch ( SigarException e ){
				e.printStackTrace();
			}
			return "0.0.0.0";
		}else return "0.0.0.0";
	}

	private static void init() {
		sig= new Sigar();
	}
}