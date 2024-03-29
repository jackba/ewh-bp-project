package com.ewhoxford.android.bloodpressure.signalProcessing;

/**
 * 
 * @author mpimentel
 * Convert digital in pressure units (mm Hg)
 */
public class ConvertTommHg {
	
	/**
	 * 
	 * @param valsIn
	 * @param fs
	 * @return return converted pressure signal (in mm Hg) and time series (in secs)
	 * @see valsOut
	 */
	public static TimeSeriesMod convertArrayTommHg(int[][] valsIn, int fs){
		// initialize variables
		TimeSeriesMod valsOut = new TimeSeriesMod();
		double[] arrayPressure = new double[valsIn.length];
		float[] arrayTime = new float[valsIn.length];
		float aux1 = 0;
	    float aux2 = 0;
		int valsx = 0;
		int valsy = 0;
		int i = 0;
		
		// Run the entire digital signal
		while (i < valsIn.length) {
			// convert second column of the digital signal to the appropriate integer
			valsy = Math.abs(valsIn[i][1]-255);
			if (valsIn[i][0]==1){
				valsx = (int) Math.pow(2, 8);
			}
			else if (valsIn[i][0]==2){
				valsx = (int) Math.pow(2, 9);
			}
			else if (valsIn[i][0]==3){
				valsx = (int) (Math.pow(2, 8) + Math.pow(2, 9));
			}
			else{
				valsx = 0;
			}
			
			// convert digital signal to volts 
			aux1 = (float) (valsx+valsy)/1024;
			
			// convert signal from volts to pressure units (mm Hg)
			aux2 = (float) (aux1-0.04);
			arrayPressure[i] = (double) (aux2*7.50061683/0.018);
			
			// calculate time series (based on sampling frequency, fs)
			arrayTime[i] = ((float)i/(float)fs);
			
			i++;
		}
		
		// output
		valsOut.setPressure(arrayPressure);
		valsOut.setTime(arrayTime);
		
		return valsOut;
	}

//	public static double convertTommHg(int xValue, int yValue) {
//
//		
//		float aux1 = 0, aux2 = 0;
//
//	    double pressureValue = 0;
//
//	    
//	    if (yValue != 0){
//	    	yValue = Math.abs(yValue-256);
//	    }
//	    
//	    if (xValue == 20){
//	    	xValue = 0;
//	    	yValue = 128;
//	    }
//
//	    if (xValue == 21){
//	    	xValue = 1;
//	    	yValue = 128;
//	    }
//	    
//	    if (xValue == 22){
//	    	xValue = 2;
//	    	yValue = 128;
//	    }
//	    
//	    if (xValue == 23){
//	    	xValue = 3;
//	    	yValue = 128;
//	    }
//	    
//	    aux1 = (float) ( (xValue<<8) | yValue)/1347; 
//
//	    aux2 = (float) (aux1-0.04);
//
//	    pressureValue = (double) (aux2*7.50061683/(0.018));
//
//	    return pressureValue;
//			
//	}
	
	public static double convertTommHg(int x, int y) {

		float aux1 = 0, aux2 = 0;

		double pressureValue = 0;

		if (x < 0)
			x += 256;
		if (y < 0)
			y += 256;

		aux1 = (float) ((x << 8) | y) / (1024 * 5);

		aux2 = (float) ((3.72 * aux1) - 0.04);

		pressureValue = (double) (aux2 * 7.50061683 / (0.018));

		return pressureValue;

	}

}
