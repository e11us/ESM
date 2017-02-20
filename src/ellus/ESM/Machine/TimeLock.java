package ellus.ESM.Machine;

public class TimeLock {
	private int		lockTime= 1;
	private boolean	Locked	= false;

	public TimeLock( int lockTime ) {
		this.lockTime= lockTime;
	}

	public void Lock() {
		Thread tt= new Thread( "TimeLock-" + helper.rand32AN().substring( 0, 5 ) ) {
			@Override
			public void run() {
				try{
					this.sleep( lockTime );
				}catch ( Exception ee ){}
				Locked= false;
			}
		};
		tt.start();
		Locked= true;
	}

	public boolean isLocked() {
		return Locked;
	}
}
