import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class randomIn{
	public static void main (String args[]){
		//Random readings for the Gauge Values
		String psysPaths[] = new String[14];
		File psysFiles[] = new File[14];
		for (int i = 1; i<=14; i++){
			psysPaths[i-1]="C:\\Data\\PT"+i;
		}

    //Random Readings for the Valve states
		String valvePaths[] = new String[10];
		File valveFiles[] = new File[10];
		String[] fileNames = {"Val150", "Val151", "Val250", "Val251", "Val252", "Val253", "Val350", "Val351", "Val352", "Val353"};
		for (int i = 0; i<10; i++){
			valvePaths[i]="C:\\Data\\" + fileNames[i];
		}

		String tempPaths[] = new String[8];
		File tempFiles[] = new File[8];
		String[] tempFilesNames = {"T290", "T291", "T292", "T293", "T390", "T391", "T392", "T393"};
		for (int i = 0; i<8; i++){
			tempPaths[i]="C:\\Data\\" + tempFilesNames[i];
		}

		//Random readings for the other gauges
		File fileTank_Fuel_1 = new File("C:\\Data\\Tank_Fuel_1");
		File fileTank_Temp_1 = new File("C:\\Data\\Tank_Temp_1");
		File fileTank_Fuel_2 = new File("C:\\Data\\Tank_Fuel_2");
		File fileTank_Temp_2 = new File("C:\\Data\\Tank_Temp_2");

		File fileBattery = new File("C:\\Data\\Battery");
		File fileThrust = new File("C:\\Data\\Thrust");


		//The while loop to update files
		try{
			for (int i = 1; i<=14; i++){
				psysFiles[i-1]=new File(psysPaths[i-1]);
			}

			for (int i = 0; i<10; i++){
				valveFiles[i]=new File(valvePaths[i]);
			}

			for (int i = 0; i<8; i++){
				tempFiles[i]=new File(tempPaths[i]);
			}



			while(true){
				for ( int i = 0; i<14; i++){
					FileWriter fw = new FileWriter (psysFiles[i].getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(""+(Math.random()*1600));
					bw.close();
				}
				for( int j=0; j<10; j++){
					FileWriter fw = new FileWriter (valveFiles[j].getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(""+(int)(Math.random()*2)); //gets a random 0 or 1
					bw.close();
				}
				for( int j=0; j<8; j++){
					FileWriter fw = new FileWriter (tempFiles[j].getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(""+(int)(Math.random()*1000)); //gets a random 0 or 1
					bw.close();
				}

				FileWriter fw = new FileWriter(fileTank_Fuel_1.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*100));
				bw.close();

				fw = new FileWriter(fileTank_Temp_1.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*1000));
				bw.close();

				fw = new FileWriter(fileTank_Fuel_2.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*100));
				bw.close();

				fw = new FileWriter(fileTank_Temp_2.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*1000));
				bw.close();

				fw = new FileWriter(fileBattery.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*100));
				bw.close();

				fw = new FileWriter(fileThrust.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				bw.write("" + (Math.random()*1000));
				bw.close();

			}

		}

		catch (Exception e){
			System.out.println("NOOOOOO");
		}
	}
}
