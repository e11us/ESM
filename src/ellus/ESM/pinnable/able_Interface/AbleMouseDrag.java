package ellus.ESM.pinnable.able_Interface;

import ellus.ESM.ESMW.ESMPD;
import ellus.ESM.ESMW.ESMPS;
import ellus.ESM.Machine.cor2D;



public interface AbleMouseDrag {
	// location of the init dragged position.
	public void MouseDragOn( cor2D inp );

	// location of the final dragged position.
	public void MouseDragOff( cor2D inp );

	// state of the drag on off.
	public boolean MouseDragState();

	// paint at location needed for drag
	public void paintByMouse( ESMPD g, ESMPS pan );
}
