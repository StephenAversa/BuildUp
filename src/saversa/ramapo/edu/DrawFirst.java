
package saversa.ramapo.edu;

import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawFirst extends ActionBarActivity  {
	
	String [] load;
	Boneyard boneyard = new Boneyard();
	String turn = "";
	int cWins = 0, hWins = 0;
	boolean l = false;
	
	
	/** 
	Creation that checks what is and is not loaded to determine where to go next.
	@param Bundle savedInstanceState - Bundle type that allows you to leave and come back to the function without loss of information.
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_first);
		
		if ( getIntent().hasExtra("CWINS") && getIntent().hasExtra("HWINS")){
		cWins =  getIntent().getExtras().getInt("CWINS");
    	hWins =  getIntent().getExtras().getInt("HWINS");
		}
		
		if (getIntent().hasExtra("LOAD")){
			load = getIntent().getStringArrayExtra("LOAD");
			l = true;
			int boneCount = 0;
			for (int i = 9; i < 40;i++){
				if (load[i].equals("Hand:")){
					break;
				}
				boneCount++;
			}

			boneyard.playerBoneyard.clear();
			boneyard.computerBoneyard.clear();
			//Read in from the first position of the boneyard to the end.
			for (int i = 9; i < boneCount; i++){
				String name, color = "";
				int top = 0,bot = 0;
				color = Character.toString(load[i].charAt(0));
				top = Character.getNumericValue(load[i].charAt(1));
				bot = Character.getNumericValue(load[i].charAt(2));
				Domino t1;
				t1 = new Domino(color, top, bot);
				boneyard.computerBoneyard.add(t1);
			}
			//Read in from the second position of the boneyard to the end.
			for (int i = 46; i < (46 + boneCount); i++){
				String name, color = "";
				int top = 0,bot = 0;
				color = Character.toString(load[i].charAt(0));
				top = Character.getNumericValue(load[i].charAt(1));
				bot = Character.getNumericValue(load[i].charAt(2));
				Domino t1;
				t1 = new Domino(color, top, bot);
				boneyard.playerBoneyard.add(t1);
			}
			
		}

	}
    
	/** 
	Draws the flip of the dominos and the change of the button
	@param View firstView - View that is used to change the images and buttons.
	*/
    public void Draw(View firstView){

    	int num1 = boneyard.playerBoneyard.get(0).total;
    	int num2 = boneyard.computerBoneyard.get(0).total;
    	int change1 = getResources().getIdentifier(boneyard.playerBoneyard.get(0).name.toLowerCase(Locale.ENGLISH), "drawable", getPackageName());
    	ImageView img= (ImageView) findViewById(R.id.temp1);
    	img.setImageResource(change1);
    	int change2 = getResources().getIdentifier(boneyard.computerBoneyard.get(0).name.toLowerCase(Locale.ENGLISH), "drawable", getPackageName());
    	ImageView img2= (ImageView) findViewById(R.id.temp2);
    	img2.setImageResource(change2);
    	Button button = (Button)findViewById(R.id.start);

    	TextView txt = (TextView)findViewById(R.id.result);
    	
    	
    	if (num1 == num2){
    		boneyard.shuffle();	
    		txt.setText("TIE");
    	}
    	//Provided we are continuing, change the button to continue.
    	else if (num1 > num2){
        	button.setText("Continue");
        	turn = "human";
    		txt.setText("Player First");
    	}
    	else if (num1 < num2){
        	button.setText("Continue");
        	turn = "computer";
    		txt.setText("Computer First");
    	}
   
    	if (button.getText() == "Continue"){
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent StartHands = new Intent(DrawFirst.this, BuildUp.class);
                //Store everything, all of it.
                StartHands.putExtra( "TURN", turn);
                StartHands.putExtra( "HWINS", hWins);
                StartHands.putExtra( "CWINS", cWins);
                if (l==true){
                	StartHands.putExtra("LOAD", load);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("BONEYARD", boneyard);
                StartHands.putExtras(bundle);
                startActivity(StartHands);
            }
    	});
        
    	}
    	
    }
    
    
	
}
