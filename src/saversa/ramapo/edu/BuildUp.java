package saversa.ramapo.edu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.Vector;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff;

public class BuildUp extends ActionBarActivity {
	
	
	Boneyard boneyard = new Boneyard();
	int move_hand = -1,move_stack = -1;
	int pass = 0;
	int cScore = 0, hScore = 0, cWins = 0, hWins = 0;
	Vector <Domino> hHand = new Vector <Domino>();
	Vector <Domino> cHand = new Vector <Domino>();
	Vector <Domino> checkHHand = new Vector <Domino>();
	Vector <Domino> checkCHand = new Vector <Domino>();
	Vector <Domino> stack = new Vector <Domino>();
	ImageView img;
	String p_turn = "";
	String [] state,load;
	
	
	/** 
	Creation of the Build Up game activity.
	@param Bundle savedInstanceState - Bundle type that allows you to leave and come back to the function without loss of information.
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_build_up);
		
		if ( getIntent().hasExtra("CWINS") && getIntent().hasExtra("HWINS")){
		cWins =  getIntent().getExtras().getInt("CWINS");
    	hWins =  getIntent().getExtras().getInt("HWINS");
		}
		
		if (getIntent().hasExtra("LOAD")){
			load = getIntent().getStringArrayExtra("LOAD");
			boneyard.computerBoneyard.clear();
			boneyard.playerBoneyard.clear();
			parseString(load);
			if (hHand.isEmpty()){
				dealHands();
			}
			drawHandsStack();
			setText();

		}else{
			
			setText();
			Intent passed = getIntent();
			p_turn = passed.getStringExtra("TURN");
			boneyard = (Boneyard)passed.getSerializableExtra("BONEYARD");
			dealHands();
			dealStack();
			
			if (p_turn.equals("computer")){
				findMove("w",cHand,stack);
			}
		}
	
	}
	
	/** 
	Creation of a basic alertbox that is used to display information.
	@param final String title - A string representing the title of the message box being invoked.
	@param String mymessage - A string representing the message being displayed to you in the box.
	*/
	protected void alertbox(final String title, String mymessage)
	   {
	   new AlertDialog.Builder(this)
	      .setMessage(mymessage)
	      .setTitle(title)
	      .setCancelable(true)
	      .setNeutralButton(android.R.string.ok,
	         new DialogInterface.OnClickListener() {
	         public void onClick(DialogInterface dialog, int whichButton){
	      	   if (title.equals("Game Over")){
	    		   exitApp();
	    	   }
	         }
	         })
	      .show();
	   

	   }
	
	/** 
	Creation of a more complex alertbox that is used to display information and offer buttons that provide options.
	@param Activity activity -An activity used by the alert dialog builder to make and display the message and options.
	@param String title - A string representing the title of the message box.
	@param CharSequence message - A sequence of characters that make up the message being displayed in the alert box.
	*/
	public void showDialog(Activity activity, String title, CharSequence message) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(activity);

	    if (title != null) builder.setTitle(title);

	    builder.setMessage(message);
	    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	 
            public void onClick(DialogInterface dialog, int which){
            	Intent startNewGame = new Intent(BuildUp.this, DrawFirst.class);
            	startNewGame.putExtra( "HWINS", hWins);
            	startNewGame.putExtra( "CWINS", cWins);
            	startActivity(startNewGame);
            }
	    });
	    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	    	 
            public void onClick(DialogInterface dialog, int which){
            	String winner;
            	if (hWins < cWins){
            		winner = "Computer";
            	}else if (hWins < cWins){
            		winner = "Player";
            	}else{
            		winner = "Nobody";
            	}
            	alertbox("Game Over", "Your Wins: " + hWins + "\nComputer Wins: " + cWins +"\n" + winner + " wins!");
            }
	    });
	    builder.show();
	    
	    
	}
	
	/** 
	Function that deals the correct number of dominos depending on the turn to each player.
	*/
	public void dealHands(){
		hHand.clear();
		cHand.clear();
		checkHHand.clear();
		checkCHand.clear();
		String name = "";
		String dom = "";
		
		
		if (boneyard.playerBoneyard.size() > 6){
			for (int i = 0; i < 6; i++){
				name = "hand" + i;
				dom = boneyard.playerBoneyard.get(0).name.toLowerCase(Locale.ENGLISH);
				int change = getResources().getIdentifier(dom, "drawable", getPackageName());
				int changeId = getResources().getIdentifier(name, "id", getPackageName());
				img= (ImageView) findViewById(changeId);
				View b = findViewById(changeId);
				img.setImageDrawable(getResources().getDrawable(change));
				b.setVisibility(View.VISIBLE);
				Domino hDom = boneyard.playerBoneyard.get(0);
				Domino cDom = boneyard.computerBoneyard.get(0);
				hHand.add(hDom);
				cHand.add(cDom);
				checkHHand.add(hDom);
				checkCHand.add(cDom);
				boneyard.playerBoneyard.remove(0);
				boneyard.computerBoneyard.remove(0);
				}
			}else if (!boneyard.playerBoneyard.isEmpty()){
				for (int i = 0; i < 4; i++){
					name = "hand" + i;
					dom = boneyard.playerBoneyard.get(0).name.toLowerCase(Locale.ENGLISH);
					int change = getResources().getIdentifier(dom, "drawable", getPackageName());
					int changeId = getResources().getIdentifier(name, "id", getPackageName());
					img= (ImageView) findViewById(changeId);
					View b = findViewById(changeId);
					img.setImageDrawable(getResources().getDrawable(change));
					b.setVisibility(View.VISIBLE);
					Domino hDom = boneyard.playerBoneyard.get(0);
					Domino cDom = boneyard.computerBoneyard.get(0);
					hHand.add(hDom);
					cHand.add(cDom);
					checkHHand.add(hDom);
					checkCHand.add(cDom);
					boneyard.playerBoneyard.remove(0);
					boneyard.computerBoneyard.remove(0);
				}	
		}else{
			endRound();
		}
	}
	
	/** 
	Function that deals out the stacks for the players to play on.
	*/
	public void dealStack(){
		for (int i = 0; i < 6; i++){
			String name = "dom" + i;
			String dom = boneyard.playerBoneyard.get(i).name.toLowerCase(Locale.ENGLISH);
			int change = getResources().getIdentifier(dom, "drawable", getPackageName());
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			img.setImageDrawable(getResources().getDrawable(change));
			Domino hDom = boneyard.playerBoneyard.get(i);
			stack.add(hDom);
			boneyard.playerBoneyard.remove(i);
		}
		for (int i = 0; i < 6; i++){
			String name = "dom" + (i+6);
			String dom = boneyard.computerBoneyard.get(i).name.toLowerCase((Locale.ENGLISH));
			int change = getResources().getIdentifier(dom, "drawable", getPackageName());
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			img.setImageDrawable(getResources().getDrawable(change));
			Domino cDom = boneyard.computerBoneyard.get(i);
			stack.add(cDom);
			boneyard.computerBoneyard.remove(i);
		}
	}
	
	/** 
	Turns all highlighted dominos to not highlighted in the hand.
	*/
	public void wipeChooseHand(){
		for (int i = 0; i < 6; i++){
			String name = "hand" + i;
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			img.setColorFilter(null);
		}
	}
	
	/** 
	Turns all highlighted dominos to not highlighted in the stack.
	*/
	public void wipeChooseStack(){
		for (int i = 0; i < 12; i++){
			String name = "dom" + i;
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			img.setColorFilter(null);
		}
	}
	
	/** 
	Switch statement that gets the id of the image in the hand to be changed to highlighted.
	@param final View v - View that represents the image.
	*/
	public void chooseHand( View v ) {
    switch (v.getId()) {
	    case (R.id.hand0):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand0);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 0;
	    break;
	    case (R.id.hand1):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand1);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 1;
	    break;
	    case (R.id.hand2):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand2);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 2;
	    break;
	    case (R.id.hand3):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand3);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 3;
	    break;
	    case (R.id.hand4):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand4);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 4;
	    break;
	    case (R.id.hand5):
	    	wipeChooseHand();
	    	img = (ImageView) findViewById(R.id.hand5);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_hand = 5;
	    break;
	    }
	}
	
	/** 
	Switch statement that gets the id of the image in the stack to be changed to highlighted.
	@param final View v - View that represents the image.
	*/
	public void chooseStack( View v ) {
		
	    switch (v.getId()) {
	    case (R.id.dom0):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom0);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 0;
	    break;
	    case (R.id.dom1):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom1);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 1;
	    break;
	    case (R.id.dom2):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom2);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 2;
	    break;
	    case (R.id.dom3):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom3);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 3;
	    break;
	    case (R.id.dom4):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom4);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 4;
	    break;
	    case (R.id.dom5):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom5);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 5;
	    break;
	    case (R.id.dom6):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom6);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 6;
	    break;
	    case (R.id.dom7):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom7);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 7;
	    break;
	    case (R.id.dom8):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom8);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 8;
	    break;
	    case (R.id.dom9):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom9);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 9;
	    break;
	    case (R.id.dom10):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom10);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 10;
	    break;
	    case (R.id.dom11):
	    	wipeChooseStack();
	    	img = (ImageView) findViewById(R.id.dom11);
			img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
			move_stack = 11;
	    break;
	    }
		
	}
	
	/** 
	Takes the move that is highlighted by the player.
	@param final View v - View that represents the image.
	*/
	public void takeMove( View v ){
		if (move_stack != -1 && move_hand != -1){
			Domino hand = hHand.get(move_hand);
			Domino move = stack.get(move_stack);
			if (checkHumanMove(hand,move) == true){
				wipeChooseStack();
		    	wipeChooseHand();
				move = hand;
				checkHHand.remove(hand);
				stack.set(move_stack, move);
				int change_hand = getResources().getIdentifier((hand.name).toLowerCase(Locale.ENGLISH), "drawable", getPackageName());
				int delete_hand = getResources().getIdentifier("hand"+move_hand, "id", getPackageName());
				int change_stack = getResources().getIdentifier("dom"+move_stack, "id", getPackageName());
				img= (ImageView) findViewById(change_stack);
				img.setImageDrawable(getResources().getDrawable(change_hand));
				View b = findViewById(delete_hand);
				b.setVisibility(View.INVISIBLE);
				move_stack =-1;
				move_hand = -1;
				pass = 0;
				p_turn = "computer";
				findMove("w",cHand,stack);
				}
		}
	}
	
	/** 
	Takes the move for the computer's turn
	@param Vector <Domino> hand - A vector representing the hand of a player being passed in.
	@param Vector <Domino> stack - A vector representing the stack.
	@param Domino move - A domino that is the move to be made.
	@param int stackID - The id of the location on the stack that the domino is going to replace.
	*/
	public void takeCMove(Vector <Domino> hand, Vector <Domino> stack,Domino move, int stackID){
		int rem = -1;
		for (int i = 0; i < hand.size(); i++){
			if (hand.get(i) == move){
				rem = i;
			}
		}
		if (rem != -1){
			hand.remove(rem);
		}
	Domino temp = stack.get(stackID);
	TextView talk = (TextView)findViewById(R.id.computer_sp);
	talk.setTextColor(Color.parseColor("#FFFFFF"));
	if (temp.col.equals(move.col)){
		talk.setText("I took " + move.title + " on " + temp.title + "." + "\n" + "This allows me to get a domino out of my hand and gain " + (move.total - temp.total) + " points!");
	}else{
		talk.setText("I took " + move.title + " on " + temp.title + "." + "\n" + "This allows me to collectively gain a " + (move.total + temp.total) + " point advantage!");
	}
	wipeChooseStack();
	wipeChooseHand();

	stack.set(stackID, move);
	int change_hand = getResources().getIdentifier((move.name).toLowerCase(Locale.ENGLISH), "drawable", getPackageName());
	int change_stack = getResources().getIdentifier("dom"+stackID, "id", getPackageName());
	img= (ImageView) findViewById(change_stack);
	img.setImageDrawable(getResources().getDrawable(change_hand));
	img.setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY );
	checkCHand.remove(move);
	pass = 0;
	
	if (checkCHand.isEmpty()){
		pass = 1;
		if (checkHHand.isEmpty()){
			tallyScores();
			}
		}
	}
	
	/** 
	Checks the move the human wants to make to make sure it is a vaild move.
	@param Domino hand - The domino being played from the hand.
	@param Domino stack - The domino being played onto in the stack.
	@return boolean - Returns true or false if the move can or cannot be made.
	*/
	public boolean checkHumanMove(Domino hand, Domino stack){
		
		if (hand.top == hand.bot){
				if (stack.bot == stack.top){
					if (stack.total < hand.total){
						return true;
					}
					alertbox("Invalid Move", "You cannot make that move, try another.");
					return false;
				}else{
					return true;
				}
			}
			else if (stack.total <= hand.total){
				return true;
			}
 
		alertbox("Invalid Move", "You cannot make that move, try another.");
		return false;
	}
	
	/** 
	The findMove function used by both the computer and player to find the best move.
	@param String color - A string representing the color of the player trying to play.
	@param Vector <Domino> hand - A vector representing the hand.
	@param Vector <Domino> stack - A vector representing the string.
	@return Domino - Returns the domino being played from the hand.
	*/
	public Domino findMove(String color, Vector <Domino> hand,Vector <Domino> stack){
		TextView talk = (TextView)findViewById(R.id.computer_sp);
		Domino ret;
		int pStack = -1,pHand = 0;
		ret = new Domino();

		int max = 0,sum = 0;
		//Find the first non double tile
		ret = findNonDouble(hand,stack);
		// Make sure it exists
		if (ret.name != null){
			for (int i = 0; i < stack.size();i ++){
				Domino comp = stack.get(i);
				
					if (ret.total >= comp.total){
						if (comp.col.toLowerCase(Locale.ENGLISH).equals(color.toLowerCase(Locale.ENGLISH))){
							sum = ret.total - comp.total;
						}else{
							sum = ret.total + comp.total;
						}
					}
				
				if (sum > max){
					//Set the new max
					max = sum;
					//The index of the stack the domino is at.
					pStack = i;
				}
			}
			
		}
		if (pStack == -1){
			//Try same with a double from the hand.
			ret = findDouble(hand,stack);
			if (ret.name != null){
				for (int i = 0; i < stack.size();i ++){
					Domino comp = stack.get(i);
					
					if (comp.bot == comp.top){
						if (ret.total > comp.total){
							if (!comp.col.equals(color)){
							sum = ret.total + comp.total;
								}else{
									sum = ret.total - comp.total;
								}
						}
					}else{
						if (!comp.col.equals(color)){
							sum = ret.total + comp.total;
						}else{
							sum = ret.total - comp.total;
						}
					}
					if (sum > max){
						max = sum;
						pStack = i;
					}
				}
			}
			
		}
		
		for (int i = 0; i < hHand.size();i++){
			if (ret == hHand.get(i)){
				pHand = i;
			}
		}
		
		if (p_turn.equals("computer")){
			if (ret.name != null){
				takeCMove(hand,stack,ret,pStack);
				p_turn = "human";
			}else{
				if (hand.isEmpty()){
					talk.setText("No more dominos in my hand!");
					pass = 1;
				}else{
					talk.setText("No available moves, I pass!");
					pass = 1;
				}
				p_turn = "human";
			}
		}else{
			if (pStack != -1){
				int change_stack = getResources().getIdentifier("dom"+pStack, "id", getPackageName());
				int change_hand = getResources().getIdentifier("hand"+pHand, "id", getPackageName());
				img= (ImageView) findViewById(change_stack);
				img.setColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY );
				img= (ImageView) findViewById(change_hand);
				img.setColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY );
				talk.setTextColor(Color.parseColor("#FFFFFF"));
				talk.setText("Take " + ret.title + " on " + stack.get(pStack).title + "." + "\nThis will allow you to gain a " + (ret.total + stack.get(pStack).total) + " point advantage!" );
			}else{
				talk.setText("Pass your turn.");
			}
		}
	
		return ret;
	}
	
	/** 
	Find the best move that can be made with a piece that is not a double
	@param Vector <Domino> hand - The vector representing the hand to be looked through.
	@param Vector <Domino> stack - A string representing the stack.
	@return Domino - Returns the domino that is the best move and is not a double.
	*/
	public Domino findNonDouble(Vector <Domino> hand,Vector <Domino> stack){
		Domino ret;
		ret = new Domino();
		ret.total = -1;
		for (int i = 0; i < hand.size(); i++){
			Domino temp = hand.get(i);
			if (temp.bot != temp.top){
				if (temp.total >= ret.total){
					ret = temp;
				}
			}
		}
		return ret;
	}

	/** 
	Find the best move that can be made with a piece that is a double
	@param Vector <Domino> hand - The vector representing the hand to be looked through.
	@param Vector <Domino> stack - A string representing the stack.
	@return Domino - Returns the domino that is the best move and is a double.
	*/
	public Domino findDouble(Vector <Domino> hand,Vector <Domino> stack){
		Domino ret;
		ret = new Domino();
		ret.total = -1;
		for (int i = 0; i < hand.size(); i++){
			Domino temp = hand.get(i);
			if (temp.bot == temp.top){
				if (temp.total > ret.total){
					ret = temp;
				}
			}
		}
		return ret;
	}
	
	/** 
	Checks to see if the player has a move or if he has to pass.
	@return boolean - Returns true or false if the player can or cannot play a piece.
	*/
	public boolean checkPass(){

		if (!(checkHHand.isEmpty())){
			for(int i = 0; i < checkHHand.size(); i++){
				for(int j = 0; j < stack.size(); j++){
					if (checkHHand.get(i).top == checkHHand.get(i).bot){
						if (stack.get(i).top == stack.get(i).bot){
							if (checkHHand.get(i).total > stack.get(i).total){
								alertbox("Invalid Move", "You can still make another move.");
								return false;
							}
						}
					}else if (checkHHand.get(i).total > stack.get(i).total){
						alertbox("Invalid Move", "You can still make another move.");
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/** 
	Passes the turn.
	*/
	public void pass(){
		
		if (checkPass()){
			//If someone already passed.
			if (pass == 1){
				tallyScores();
			}else{
				pass = 1;
				p_turn = "computer";
				findMove("w",cHand,stack);
			}
		}
	}
	
	/** 
	Counts up the pips on the dominos on the stack and subtracts the ones in hand.
	*/
	public void tallyScores(){
		
		for (int i = 0; i < hHand.size();i++){
		int delete_hand = getResources().getIdentifier("hand"+i, "id", getPackageName());
		View b = findViewById(delete_hand);
		b.setVisibility(View.INVISIBLE);
		}
		
		
		TextView c_score = (TextView)findViewById(R.id.computer_score);
		TextView h_score = (TextView)findViewById(R.id.player_score);
		
		for (int i = 0; i < stack.size();i++){
			if (stack.get(i).col.equals("w") || stack.get(i).col.equals("W")){
				cScore += stack.get(i).total;
			}else{
				hScore += stack.get(i).total;
			}
		}
		for (int i = 0; i < checkHHand.size();i++){
			hScore -= checkHHand.get(i).total;
		}
		for (int i = 0; i < checkCHand.size();i++){
			cScore -= checkCHand.get(i).total;
		}
		if (!boneyard.playerBoneyard.isEmpty()){
		alertbox("Hand Over", "Your Score: " + hScore + "\nComputer Score: " + cScore);
		}
		
		c_score.setTextColor(Color.parseColor("#FFFFFF"));
		h_score.setTextColor(Color.parseColor("#FFFFFF"));
		c_score.setText("Computer Score: " + cScore);
		h_score.setText("Your Score: " + hScore);
		dealHands();
	}
	
	/** 
	Calls findMove with the player's color.
	@param View v - View to represent the images being highlighted.
	*/
	public void getHelp(View v){
		wipeChooseHand();
		wipeChooseStack();
		findMove("b",checkHHand,stack);
	}
	
	/** 
	End the round, calculate the winner, display, and offer another game.
	*/
	public void endRound(){

		String winner = "";
		if (hScore > cScore){
			winner = "player";
			hWins++;
		}else if (hScore < cScore){
			winner = "computer";
			cWins++;
		}else{
			winner = "nobody";
		}
		
		showDialog(this, "Round Over,", "Your Score: " + hScore + "\nComputer Score: " + cScore + "\nThe " + winner + " wins!\nWould you like to play again?");
		
		
	}

	/** 
	Creates an alert box that asks the user for text input to name a save file.
	@param View firstView - A view being used for the alert box.
	*/
	public void askSave(View firstView){
	final AlertDialog.Builder alert = new AlertDialog.Builder(this);
    final EditText input = new EditText(this);
    alert.setView(input);
    alert.setTitle("Save Game");
    alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            String value = input.getText().toString().trim();
            value += ".txt";
            saveGame(value);
            exitApp();
        }
    });

    alert.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
    alert.show();
}

	/** 
	Writes the information to a file and tells the user the game is saved.
	@param String name - A string representing the name of the save file.
	*/
	public void saveGame(String name){

			writeToFile(state, name);
			Context context = getApplicationContext();
			CharSequence text = "Game Saved";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();

		
	}
	
	/** 
	Opens internal storage and writes the information in the form of a text file.
	@param String[] a_output - The output state for the file.
	@param String a_fileName - A string representing the file being opened.
	*/
	public void writeToFile(String[] a_output, String a_fileName)
	{
		boolean mExternalStorageAvailable = false;
		@SuppressWarnings("unused")
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state))
		{
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} 
		else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) 
		{
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} 
		else 
		{
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		if (mExternalStorageAvailable)
		{
			File dir =new File(android.os.Environment.getExternalStorageDirectory(),"NMM Files");
		    if(!dir.exists())
		    {
		           dir.mkdirs();
		    }    
		    try
		    {
			    File f = new File(dir+File.separator+a_fileName);
	
			    FileOutputStream fOut = new FileOutputStream(f);
			    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

			    myOutWriter.append("Computer:\n");
			    myOutWriter.append("Stacks:\n");
			    for (int i = 0; i < 6; i++){
			    	String s_stack = stack.get(i).col.toUpperCase(Locale.ENGLISH) +  stack.get(i).bot +  stack.get(i).top +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Boneyard:\n");
			    for (int i = 0; i < boneyard.computerBoneyard.size(); i++){
			    	String s_stack = boneyard.computerBoneyard.get(i).col.toUpperCase(Locale.ENGLISH) +  boneyard.computerBoneyard.get(i).bot +  boneyard.computerBoneyard.get(i).top +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Hand:\n");
			    for (int i = 0; i < checkCHand.size(); i++){
			    	String s_stack = checkCHand.get(i).col.toUpperCase(Locale.ENGLISH) +  checkCHand.get(i).top + checkCHand.get(i).bot +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Score:\n");
			    myOutWriter.append(cScore +"\n");
			    myOutWriter.append("Rounds Won:\n");
			    myOutWriter.append(cWins+"\n");
			    
			    myOutWriter.append("Human:\n");
			    myOutWriter.append("Stacks:\n");
			    for (int i = 6; i < 12; i++){
			    	String s_stack = stack.get(i).col.toUpperCase(Locale.ENGLISH) +  stack.get(i).bot +  stack.get(i).top +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Boneyard:\n");
			    for (int i = 0; i < boneyard.playerBoneyard.size(); i++){
			    	String s_stack = boneyard.playerBoneyard.get(i).col.toUpperCase(Locale.ENGLISH) +  boneyard.playerBoneyard.get(i).bot +  boneyard.playerBoneyard.get(i).top +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Hand:\n");
			    for (int i = 0; i < checkHHand.size(); i++){
			    	String s_stack = checkHHand.get(i).col.toUpperCase(Locale.ENGLISH) +  checkHHand.get(i).bot + checkHHand.get(i).top +"\n";
			    	myOutWriter.append(s_stack);
			    }
			    myOutWriter.append("Score:\n");
			    myOutWriter.append(hScore +"\n");
			    myOutWriter.append("Rounds Won:\n");
			    myOutWriter.append(hWins+"\n");
			    myOutWriter.append("Turn:\n");
			    myOutWriter.append(p_turn);
			    
			    
			    myOutWriter.close();
			    fOut.close();
		    }
		    catch(Exception e)
		    {
		        e.printStackTrace();
		        
		    }
		}
	}
	
	/** 
	Parses a save string to get all of the essential game objects.
	@param String[] load_name - String array holding the unparsed load information.
	*/
    public void parseString (String[] load_name){
		String name, color = "";
		int state = 0, player = 1, top = 0,bot = 0;
	
		for (int i = 0; i < load.length; i ++){
			name = load[i];
			if (name.length() > 2){
			color = Character.toString(load[i].charAt(0));
			bot = Character.getNumericValue(load[i].charAt(1));
			top = Character.getNumericValue(load[i].charAt(2));
			}
				if (name == "Computer:"){
					continue;
				}
				//Computer - Stacks in file
				else if (name.equals("Stacks:")){
					state = 1;
					continue;
				}
				//Computer - Boneyard in file
				else if (name.equals("Boneyard:")){
					state = 2;
					continue;
				}
				//Computer - Hand in file
				else if (name.equals("Hand:")){
					state = 3;
					continue;
				}
				//Computer - score in file
				else if (name.equals("Score:")){
					state = 4;
					continue;
				}
				//Computer - Rounds in file
				else if (name.equals("Rounds Won:") || name.equals("Rounds") || name.equals("Won:")){
					state = 5;
					continue;
				}
				//Turn
				else if (name.equals("Turn:")){
					state = 6;
					continue;
				}
				else if (name.equals("Human:")){
					player = 2;
					state = 0;
					continue;
				}
				else if (name.equals("End")){
					break;
				}
				Domino t1;
				if (state == 1){
					t1 = new Domino(color, bot, top);
					stack.add(t1);
				}
				
				if (player == 1){
					switch (state){
					case 2:
						t1 = new Domino(color, bot, top);
						boneyard.computerBoneyard.add(t1);
						break;
					case 3:
						t1 = new Domino(color, bot, top);
						cHand.add(t1);
						checkCHand.add(t1);
						break;
					case 4:
						//Convert to number for the score
						cScore = Integer.parseInt(load[i]);
						break;
					case 5:
						cWins = Integer.parseInt(load[i]);
						break;
						}
					}
				
				else if (player == 2){
					switch (state){
					case 2:
						t1 = new Domino(color, bot, top);
						boneyard.playerBoneyard.add(t1);
						break;
					case 3:
						t1 = new Domino(color, bot, top);
						hHand.add(t1);
						checkHHand.add(t1);
						break;
					case 4:
						hScore = Integer.parseInt(load[i]);
						break;
					case 5:
						hWins = Integer.parseInt(load[i]);
						break;
						}
				if (state == 6){
					p_turn = load[i];
					state = -1;
				}
			}
		}			
	}	
    
	/** 
	Draws the hands, stack, and screen provided the user has not started a new game.
	*/
    public void drawHandsStack(){
    	for (int i = 0; i < hHand.size();i++){
    		String name = "hand" + i;
    		String uh = (hHand.get(i).name).toLowerCase(Locale.ENGLISH);
			int change = getResources().getIdentifier(uh, "drawable",getPackageName());
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			View b = findViewById(changeId);
			img.setImageDrawable(getResources().getDrawable(change));
			b.setVisibility(View.VISIBLE);
    	}
    	for (int i = 0; i < stack.size();i++){
    		String name = "dom" + i;
    		String uh = (stack.get(i).name).toLowerCase(Locale.ENGLISH);
			int change = getResources().getIdentifier(uh, "drawable",getPackageName());
			int changeId = getResources().getIdentifier(name, "id", getPackageName());
			img= (ImageView) findViewById(changeId);
			View b = findViewById(changeId);
			img.setImageDrawable(getResources().getDrawable(change));
			b.setVisibility(View.VISIBLE);
    	}

    	for (int i = hHand.size(); i < 6; i++){
    		String name = "hand" + i;
    		int changeId = getResources().getIdentifier(name, "id", getPackageName());
    		View b = findViewById(changeId);
    		b.setVisibility(View.INVISIBLE);
    	}
    }
    
	/** 
	Sets all of the text color and initial information.
	*/
    public void setText(){
		TextView c_wins = (TextView)findViewById(R.id.computer_wins);
		TextView h_wins = (TextView)findViewById(R.id.player_wins);
		c_wins.setText("Computer Wins: " + cWins);
		h_wins.setText("Your Wins: " + hWins);
		
		TextView c_score = (TextView)findViewById(R.id.computer_score);
		TextView h_score = (TextView)findViewById(R.id.player_score);
		c_score.setTextColor(Color.parseColor("#FFFFFF"));
		h_score.setTextColor(Color.parseColor("#FFFFFF"));
		c_score.setText("Computer Score: " + cScore);
		h_score.setText("Your Score: " + hScore);
    }
    
	/** 
	Exits the application.
	*/
    public void exitApp(){    	
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    	
    }
    
}


