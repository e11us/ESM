package ellus.ESM.Machine;

public class Delayer {
	public Delayer( String name, int delay ) {
		Thread tt= new Thread( name ) {
			@Override
			public void run() {
				try{
					sleep( delay );
				}catch ( InterruptedException e ){}
				//
				DoThisWithDelay();
			}
		};
		tt.start();
	}

	public void DoThisWithDelay() {}
}
