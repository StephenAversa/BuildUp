package saversa.ramapo.edu;

import java.io.Serializable;
import java.util.Collections;
import java.util.Vector;

public class Boneyard implements Serializable{
		
	private static final long serialVersionUID = 1L;
	
	public Vector <Domino> playerBoneyard = new Vector <Domino>();
	public Vector <Domino> computerBoneyard = new Vector <Domino>();

	/** 
	Default Constructor for Boneyard
	*/
	public Boneyard(){
		createBoneyards();
		shuffle();
		
	}
	
	/** 
	Creates the two players respective boneyards.
	*/
	public void createBoneyards(){

		if ((playerBoneyard.size() < 28) && (computerBoneyard.size() < 28)){
			Domino p1, c1;
			int first = 0;
			int second = 0;
			
			for (int i = 0; i < 7; i++){
				for (int j = first; j < 7; j++){
					p1 = new Domino("b", first, second);
					c1 = new Domino("w", first, second);
					playerBoneyard.add(p1);
					computerBoneyard.add(c1);
					second++;
				}
				first++;
				second = first;
			}
		}
	}
	
	/** 
	Shuffles the two boneyards before play.
	*/
	public void shuffle(){
		Collections.shuffle(playerBoneyard);
		Collections.shuffle(computerBoneyard);
	}
	

}
