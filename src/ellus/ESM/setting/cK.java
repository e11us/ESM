package ellus.ESM.setting;

public class cK {
	int		ind		= 0;
	String	type	= null;
	String	cont	= null;
	String	comment	= null;

	@Override
	public String toString() {
		if( type == null )
			return "Ind: " + ind;
		if( comment == null )
			return "Ind: <" + ind + "> <" + type + "> <" + cont + ">";
		else return "Ind: <" + ind + "> <" + type + "> <" + cont + "> // " + comment;
	}
}
