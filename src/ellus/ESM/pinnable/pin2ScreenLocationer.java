package ellus.ESM.pinnable;

import ellus.ESM.ESMW.ESMPS;



public class pin2ScreenLocationer {
	public static pinnable centerThisX( pinnable pin, ESMPS PS ) {
		if( pin == null || PS == null )
			return null;
		pin.setXY( PS.getWidth() / 2 - pin.getWidth() / 2,
				PS.getWidth() / 2 + pin.getWidth() / 2,
				pin.getYmin(), pin.getYmax() );
		return pin;
	}

	public static pinnable bottomLeft( pinnable pin, ESMPS PS ) {
		if( pin == null || PS == null )
			return null;
		pin.setXY( 10, pin.getWidth(),
				PS.getHeight() - pin.getHeight() - 10,
				PS.getHeight() - 10 );
		return pin;
	}
}
