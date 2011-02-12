// Name of the package
package com.ewhoxford.android.bloodpressure;

//Import resources
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.ewhoxford.android.bloodpressure.R;

//Import Android stuff
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

//Class Measure : activity that pops when the user wants to start taking blood pressure
public class Measure extends Activity implements OnClickListener
{
	int[] values;
	int valuesEnd;
	Random rand;
	int d;
	GraphView graph;
	
	
	
	// To be performed on the creation
    public void onCreate(Bundle savedInstanceState)
    {
    	// Parent's method
        super.onCreate(savedInstanceState);
        
        // Layout
        setContentView(R.layout.measure);

		// #### Set up click listeners for all the buttons
		
		// Help button
		View HelpButton = findViewById(R.id.button_help);
		HelpButton.setOnClickListener(this);

		// #### End of Set up click listeners for all the buttons

		// Simulate fake BP acquisition
		values = new int[60];
		rand = new Random();
		d = 4;
		graph = (GraphView) findViewById(R.id.graph);
		
		values[0] = 40;
		valuesEnd = 0;

		addValue();
	}
    
    protected void addValue()
    {
    	valuesEnd = valuesEnd + 1;
    	int i = valuesEnd;
		values[i] = values[i-1] + rand.nextInt(d+1) - d/2;
		
		graph.plotValues(values, valuesEnd);
    	
		if (i<59)
		{
    	
        final Handler handler = new Handler(); 
        Timer t = new Timer(); 
        t.schedule(new TimerTask() { 
                public void run() { 
                        handler.post(new Runnable() { 
                                public void run() { 
                                	addValue();
                                } 
                        }); 
                } 
        }, 80); 
        
		}
    }
    
    
	// event : click on something
	public void onClick(View V)
	{
		// let's find what has been clicked
		switch (V.getId())
		{
			// Start button
			case R.id.button_help:
				finish(); // kill this activity, so going back to the previous one
				break;

		}
	}
}