package saversa.ramapo.edu;

import java.io.Serializable;

public class Domino implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	String col, name, title;
	int top,bot,total;

public Domino(){
	
}
	/** 
	Sets all of the necessary information about the Domino.
	@param String c - A string reprsenting the domino color.
	@param int b - An int representing the bottom of a domino.
	@param int c - An int representing the top of a domino.
	@param int t - An int representing the domino total.
	*/
public Domino(String c, int b, int t){
		col = c;
		top = t;
		bot = b;
		total = top + bot;
		name = col + "_" + bot + "_" + top;
		title = col.toUpperCase() + " " + bot + "-" + top;
		
		
	}
}
