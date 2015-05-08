
     /************************************************************
     * Name:  Stephen Aversa                                    *
     * Project:  Java Android						            *
     * Class:  OPL						                        *
     * Date:  11/15/2014		 	                            *
     ************************************************************/

package saversa.ramapo.edu;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Vector;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	Vector <Domino> playerBoneyard = new Vector <Domino>();
	Vector <Domino> computerBoneyard = new Vector <Domino>();
	Vector <Domino> hHand = new Vector <Domino>();
	Vector <Domino> cHand = new Vector <Domino>();
	Vector <Domino> stack = new Vector <Domino>();
	int cScore,hScore,cWins,hWins;
	String turn;
	String[] load;
	ImageView img1,img2,img3,img4,img5;

	/** 
	Creation of the Build Up game activity.
	@param Bundle savedInstanceState - Bundle type that allows you to leave and come back to the function without loss of information.
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAnims();

    }
	
    /** 
	Creation of the Build Up game activity.
	@param View firstView - View that starts the activity.
	*/
    public void startGame(View firstView){
    	Intent startGame = new Intent(MainActivity.this, DrawFirst.class);
    	startActivity(startGame);
    	
    }
    
    /** 
	Creates an alert box asking for the user to input the name of a file to load.
	@param View firstView - View that starts the alert box.
	*/
    public void askLoad(View firstView){
    	final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setTitle("Load Game");
        alert.setPositiveButton("Load", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString().trim();
                //Check to see if the user wants to load any of the files that were preloaded in.
                if ((value.equals("first")) || (value.equals("second")) || (value.equals("third")) || (value.equals("fourth")) || (value.equals("fifth"))){
                	loadTxt(value);
                	//Otherwise check load:
                }else{
                value += ".txt";
                
                if (loadGame(value)){
            		Intent startLoad = new Intent(MainActivity.this, BuildUp.class);
            		startLoad.putExtra("LOAD", load);
        		    startActivity(startLoad);
                	}			
                }
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
	Check to see if the game can be loaded from the provided file name.
	@param String load_game - A string representing the name of the file.
	@return boolean - Returns whether or not the file could be properly opened.
	*/
    public boolean loadGame(String load_game){
    	@SuppressWarnings("unused")
		boolean mExternalStorageAvailable = false;
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
		
		if (mExternalStorageWriteable)
		{
			File dir =new File(android.os.Environment.getExternalStorageDirectory(),"NMM Files");
		    if(!dir.exists())
		    {
		           dir.mkdirs();
		    }    
		    try{
			    File f = new File(dir+File.separator+load_game);
	
			    FileInputStream fIn = new FileInputStream(f);
			    InputStreamReader myInReader = new InputStreamReader(fIn);
			    
			    char[] buffer = new char[1000];
			    myInReader.read(buffer);
			    splitString(new String(buffer));
			    
			    fIn.close();
			    myInReader.close();
			    return true;
		    }
			catch(Exception e)
			{
				 Toast.makeText(getApplicationContext(), "File cannot be found.",
		                    Toast.LENGTH_SHORT).show();
			    return false;
			}
		}
		return false;
	}
    
	/** 
	Split the string if it was made by the applications save.
	@param String load_game - A string representing the name of the file.
	*/
    public void splitString (String load_name){
		load = load_name.split("\n");
		}
    
	/** 
	Split the string if it was made by the provided serialization files.
	@param String load_game - A string representing the name of the file.
	*/
    public void splitSaveString (String load_name){
    	String [] temp = new String[110];
    	load = load_name.split(" ");
    	load = load_name.split("\r");
    	load = load_name.split(" ");
    	
    	int j = 0;
    	for (int i = 0; i < load.length; i ++){
			if (load[i].equals("\r\n\r\nHuman:")){
				temp[j] = "Human:";
				j++;
				continue;
			}else if (load[i].equals("\r\n\r\nTurn:")){
				temp[j] = "Turn:";
				j++;
				continue;	
			}
    		if (!load[i].equals("")){
    			if (!load[i].equals("\r\n")){
    				temp[j] = load[i];
    				j++;

    			}
    		}
    	}
    	temp[j] = "End";
    	load = temp;
    	String [] temp2 = new String[j];
    	for (int i = 0; i < j;i++){
    		temp2[i] = temp[i];
    	}
    	load = temp2;
		}
    
	/** 
	Split the string if it was made by the provided serialization files.
	@param View view - A view made for the animated domino.
	@param int speed - The speed at which the domino falls.
	*/
    private void animate(View view, int speed){

    DisplayMetrics dm = new DisplayMetrics();
    this.getWindowManager().getDefaultDisplay().getMetrics( dm );


    int originalPos[] = new int[2];
    view.getLocationOnScreen( originalPos );
    int yDest = dm.heightPixels;

    TranslateAnimation anim = new TranslateAnimation( 0, originalPos[0] , -yDest, yDest );
    anim.setRepeatCount(Animation.INFINITE);
    anim.setDuration(speed);
    anim.setFillAfter( true );
    view.startAnimation(anim);
}

	/** 
	Create the animated Dominos.
	*/
    private void createAnims(){
    ImageView img1 = (ImageView) findViewById(R.id.imageView3);
    ImageView img2 = (ImageView) findViewById(R.id.imageView2);
    ImageView img3 = (ImageView) findViewById(R.id.imageView4);
    ImageView img4 = (ImageView) findViewById(R.id.imageView5);
    ImageView img5 = (ImageView) findViewById(R.id.imageView6);
    animate(img1, 6000);
    animate(img2, 4000);
    animate(img3, 5000);
    animate(img4, 4000);
    animate(img5, 4000);
}

	/** 
	Load in the text to get the String array to pass between the game provided it was a preloaded save.
	@param String filename - The name of the file being loaded.
	*/
    private void loadTxt(String filename){
	String txtHelp;
	try {

		Resources res = getResources();
		int open = getResources().getIdentifier(filename, "raw", getPackageName());
        InputStream in_s = res.openRawResource(open);

        byte[] b = new byte[in_s.available()];
        in_s.read(b);
        txtHelp = (new String(b));
        
        splitSaveString(txtHelp);
        String com = load[load.length-1];
        
        if (com.equals("Computer") || (com.equals("Human"))){
	        Intent startLoad = new Intent(MainActivity.this, BuildUp.class);
			startLoad.putExtra("LOAD", load);
		    startActivity(startLoad);
        }else{
            Intent startLoad = new Intent(MainActivity.this, DrawFirst.class);
    		startLoad.putExtra("LOAD", load);
    	    startActivity(startLoad);

        }

    } catch (Exception e) {
        txtHelp = ("Error: can't show help.");
    }
}


    
    
}
