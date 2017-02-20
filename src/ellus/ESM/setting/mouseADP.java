package ellus.ESM.setting;

import java.awt.event.MouseAdapter;



// a simple wraper for MA.
public class mouseADP extends MouseAdapter {
	protected Object	store;
	protected int		ind;

	public mouseADP( Object inp, int ind ) {
		store= inp;
		this.ind= ind;
	}
}
