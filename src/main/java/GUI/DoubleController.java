package GUI;


import java.io.File;
import java.util.Scanner;


public class DoubleController {
	private double [] psysVals = new double [14];
	private ControllerVG contVG;
	private ControllerPNID contPNID;

	public DoubleController( ControllerVG contVG, ControllerPNID contPNID ){
		this.contVG = contVG;
		this.contPNID = contPNID;
	}


	public void getValues(){
		for(int i = 1; i<=psysVals.length; i++) {
			try{
				File file = new File("C:\\Data\\PT" + i + ".txt");
				Scanner scanner = new Scanner(file);

				double retVal = scanner.nextDouble();
				scanner.close();

				psysVals[i-1] = retVal;

			}catch(Exception e) {
				//do nothing
			}
		}

		//contVG.updateVal(psysVals);
		//contPNID.updateVal(psysVals);
	}
}
