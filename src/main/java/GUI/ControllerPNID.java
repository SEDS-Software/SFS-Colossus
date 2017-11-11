package GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.Scanner;


public class ControllerPNID{

	@FXML private Fun_Rect pBar786;
	@FXML private Fun_Rect pBar707;
	private double currFunLevel1;
	private double currFunLevel2;
	private double prevFunLevel1;
	private double prevFunLevel2;

	@FXML private Rectangle botN21;
	@FXML private ImageView open252;
	@FXML private ImageView open352;
	@FXML private ImageView open350;
	@FXML private ImageView open250;
	@FXML private ImageView open351;
	@FXML private ImageView open251;
	@FXML private ImageView open353;
	@FXML private ImageView open253;
	@FXML private ImageView open150;
	@FXML private ImageView open151;
	@FXML private ImageView openTop;
	@FXML private ImageView closed252;
	@FXML private ImageView closed352;
	@FXML private ImageView closed350;
	@FXML private ImageView closed250;
	@FXML private ImageView closed351;
	@FXML private ImageView closed251;
	@FXML private ImageView closed353;
	@FXML private ImageView closed253;
	@FXML private ImageView closed150;
	@FXML private ImageView closed151;
	@FXML private ImageView closedTop;

	@FXML private Label PT120;
	@FXML private Label PT121;
	@FXML private Label PT1221;
	@FXML private Label PT1222;
	@FXML private Label PT123;
	@FXML private Label PT220;
	@FXML private Label PT221;
	@FXML private Label PT222;
	@FXML private Label PT223;
	@FXML private Label PT321;
	@FXML private Label PT322;
	@FXML private Label PT323;
	@FXML private Label PT420;
	@FXML private Label PT421;

	@FXML private Label T290;
	@FXML private Label T291;
	@FXML private Label T292;
	@FXML private Label T293;
	@FXML private Label T391;
	@FXML private Label T390;
	@FXML private Label T392;
	@FXML private Label T393;

	Label [] psysLabs = {PT120, PT121, PT1221, PT1222, PT123, PT220, PT221, PT222, PT223, PT321, PT322, PT323,
		PT420, PT421};
	Label [] tempLabs = {T290, T291, T292, T293, T390, T391, T392, T393};
	private double[] psysVals = new double[14];
	private double[] tempVals = new double [8];

	private String[] fileNames = {"Val150", "Val151", "Val250", "Val251", "Val252", "Val253", "Val350", "Val351", "Val352", "Val353"};
	private String[] tempFiles = {"T290", "T291", "T292", "T293", "T390", "T391", "T392", "T393"};
	private ImageView[] prevStates = {open150, open151, open250, open251, open252, open253, open350, open351, open352, open353};
	private boolean[] currState = {true, true, true, true, true, true, true, true, true, true};



	//These are going to be used for a possible debug mode
	/*
	private boolean botN21Hovered = false;
	private boolean botN22Hovered= false;
	private boolean botN23Hovered = false;
	private boolean botN24Hovered = false;
	*/

	@FXML
	private void initialize() {

		Label [] psysLabs = {PT120, PT121, PT1221, PT1222, PT123, PT220, PT221, PT222, PT223, PT321, PT322, PT323,
				PT420, PT421};
		Label [] tempLabs = {T290, T291, T292, T293, T390, T391, T392, T393};

		for( int i=0; i<psysLabs.length; i++ ){
			psysLabs[i].setText("0");
		}
		for( int i=0; i< tempLabs.length; i++ ) {
			tempLabs[i].setText("0");
		}

		pBar707.setPrefWidth(65);
		pBar707.setPrefHeight(123);
		pBar707.setLayoutX(1384);
		pBar707.setLayoutY(557);

		pBar786.setPrefWidth(65);
		pBar786.setPrefHeight(123);
		pBar786.setLayoutX(315);
		pBar786.setLayoutY(557);

		new Thread(() -> {
			while (true) {
				updateVal();
				updateState();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println("sleep error");
				}
			}
		}).start();

	}

	private void getVal() {
		for(int i = 1; i<=psysVals.length; i++) {
			try{
				File file = new File("PT" + i);
				Scanner scanner = new Scanner(file);

				double retVal = scanner.nextDouble();
				scanner.close();

				psysVals[i-1] = retVal;
			}catch(Exception e) {
				//do nothing
			}
		}
		for(int i = 1; i<=tempVals.length; i++) {
			try{
				File file = new File("" + tempFiles[i]);
				Scanner scanner = new Scanner(file);

				double retVal = scanner.nextDouble();
				scanner.close();

				tempVals[i-1] = retVal;
			}catch(Exception e) {
				//do nothing
			}
		}

		try{
			File file = new File("Tank_Fuel_1");
			Scanner scanner = new Scanner(file);
			currFunLevel2 = scanner.nextDouble()/100;
			if(currFunLevel2 != prevFunLevel2) {
				pBar707.setLevel(currFunLevel2);
				prevFunLevel2 = currFunLevel2;
			}
			scanner.close();

			file = new File("Tank_Fuel_2");
			scanner = new Scanner(file);
			currFunLevel1 = scanner.nextDouble()/100;
			if(currFunLevel1 != prevFunLevel1) {
				pBar786.setLevel(currFunLevel1);
				prevFunLevel1 = currFunLevel1;
			}
			scanner.close();
		} catch (Exception e){
			//Do Nothing
		}
	}

	private void updateVal(){
		getVal();
		Label [] psysLabs = {PT120, PT121, PT1221, PT1222, PT123, PT220, PT221, PT222, PT223, PT321, PT322, PT323, PT420, PT421};
		Label [] tempLabs = {T290, T291, T292, T293, T390, T391, T392, T393};

		for(int i=0; i<psysLabs.length; i++){
			final int j = i;
			Platform.runLater(new Runnable() {
				public void run() {
					psysLabs[j].setText(""+(int)psysVals[j]);
				}
			});
		}

		for(int i=0; i<tempLabs.length; i++){
			final int j = i;
			Platform.runLater(new Runnable() {
				public void run() {
					tempLabs[j].setText(""+(int)tempVals[j]);
				}
			});
		}
	}

	private void getState(){
		for(int i = 0; i<fileNames.length; i++) {
			try{
				File file = new File("" + fileNames[i]);
				Scanner scanner = new Scanner(file);

				currState[i]= (scanner.nextDouble() > 0);
				scanner.close();

			}catch(Exception e) {
				//do nothing
			}
		}
	}

	private void updateState(){
		ImageView[] prevStates = {open150, open151, open250, open251, open252, open253, open350, open351, open352, open353};
		getState();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for( int i=0; i<currState.length; i++ ){
					if( prevStates[i].isVisible() != currState[i] ){
						switch (i){
							case 0:
								valve150();
								break;
							case 1:
								valve151();
								break;
							case 2:
								valve250();
								break;
							case 3:
								valve251();
								break;
							case 4:
								valve252();
								break;
							case 5:
								valve253();
								break;
							case 6:
								valve350();
								break;
							case 7:
								valve351();
								break;
							case 8:
								valve352();
								break;
							case 9:
								valve353();
								break;
						}
					}
				}
			}
		});

	}

	public void valve252(){
		boolean boo = closed252.isVisible();
		closed252.setVisible(!boo);
		open252.setVisible(boo);
	}
	public void valve352(){
		boolean boo = closed352.isVisible();
		closed352.setVisible(!boo);
		open352.setVisible(boo);
	}
	public void valve350(){
		boolean boo = closed350.isVisible();
		closed350.setVisible(!boo);
		open350.setVisible(boo);
	}
	public void valve250(){
		boolean boo = closed250.isVisible();
		closed250.setVisible(!boo);
		open250.setVisible(boo);
	}
	public void valve351(){
		boolean boo = closed351.isVisible();
		closed351.setVisible(!boo);
		open351.setVisible(boo);
	}
	public void valve251(){
		boolean boo = closed251.isVisible();
		closed251.setVisible(!boo);
		open251.setVisible(boo);
	}
	public void valve353(){
		boolean boo = closed353.isVisible();
		closed353.setVisible(!boo);
		open353.setVisible(boo);
	}
	public void valve253(){
		boolean boo = closed253.isVisible();
		closed253.setVisible(!boo);
		open253.setVisible(boo);
	}
	public void valve150(){
		boolean boo = closed150.isVisible();
		closed150.setVisible(!boo);
		open150.setVisible(boo);
	}
	public void valve151(){
		boolean boo = closed151.isVisible();
		closed151.setVisible(!boo);
		open151.setVisible(boo);
	}
	public void valveTop(){
		boolean boo = closedTop.isVisible();
		closedTop.setVisible(!boo);
		openTop.setVisible(boo);
	}



}
