package GUI;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.animation.*;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ControllerEC {

    //Initialize the current valve state labels
    @FXML private Label c1;
    @FXML private Label c2;
    @FXML private Label c3;
    @FXML private Label c4;
    @FXML private Label c5;
    @FXML private Label c6;
    @FXML private Label c7;
    @FXML private Label c8;
    @FXML private Label c9;
    @FXML private Label c10;

    //Initialize the current HotFire Sequence Stage Labels
    @FXML private Label s0;
    @FXML private Label s1;
    @FXML private Label s2;
    @FXML private Label s3;
    @FXML private Label s4;
    @FXML private Label s5;
    @FXML private Label s6;
    @FXML private Label s7;
    @FXML private Label s8;
    @FXML private Label s9;
    @FXML private Label s10;

    //Initialize the Buttons used to control the Exec Cue
    @FXML private Button abort;
    @FXML private Button next;
    @FXML private Button standByBut;
    @FXML private Button caliBut;
    @FXML private Button hotBut;

    //Initialize the Animation labels for the abort button
    @FXML private Label abort1;
    @FXML private Label abort2;
    @FXML private Label abort3;

    //Initialize the animation labels for the start button
    @FXML private Label next1;
    @FXML private Label next2;

    //Initialize the text area for the error messages
    @FXML private TextArea errorArea;

    //Initialize the Current Mode Label
    @FXML private Label currMode;

    private double next1Orig; // Used for the start button animation


    private boolean[] currState = {true,true,false,true,false,false,true,true, false, false};
    // tracks the current state of the Valves. Read of file and stored in this array

    private String[] fileNames = {"Val150", "Val151", "Val250", "Val251", "Val252", "Val253", "Val350", "Val351", "Val352", "Val353"};
    //Lists the names of the files to find the state of the valves


    Label[] currLabel={c1,c2,c3,c4,c5,c6,c7,c8,c9,c10}; //List of the current states of the Valves
    Label[] seqLabel ={s0,s1,s2,s3,s4,s5,s6,s7,s8, s9, s10};  //List of the HotFire Sequence Stages and what is happening

    private String ecState= "standby"; //Tells mode we are in, standby,preCount, countdown, testing are the only three values

    private Label currGreen = null; //The current label that is green in the seq tracking

    /* Components of the CountDown timer */
    @FXML private Label timer; //The Label for the CountDown Timer
    private Timeline timeline; //Used for the timer
    private static final Integer STARTTIME = 10; //starting time of the timer
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME); //Used for the timer

    /* Error Timer for the valves */
    private Label errorCount = new Label(""+errorSTARTTIME);
    private Timeline errortimeline; //Used for the timer
    private static final Integer errorSTARTTIME = 7; //starting time of the timer
    private IntegerProperty errortimeSeconds = new SimpleIntegerProperty(STARTTIME); //Used for the timer

    private String errorValveString = ""; //holds the errors when valves are in incorrect states


    /*
    Mapping of the Valves to their index in the array
    Valve 150 = 0
    Valve 151 = 1
    Valve 250 = 2
    Valve 251 = 3
    Valve 252 = 4
    Valve 253 = 5
    Valve 350 = 6
    Valve 351 = 7
    Valve 352 = 8
    Valve 353 = 9
    */
    private String [] valveName = {"Valve 150", "Valve 151", "Valve 250", "Valve 251", "Valve 252", "Valve 253", "Valve 350",
            "Valve 351", "Valve 352", "Valve 353"}; //Used to modify Label text with open and closed

    private int seqStage = 0; // Holds the current stage of the HotFire Sequence tracking

    // The states of all valves at each stage of the HotFire sequence
    private boolean[] stage0 = {false,false,true,false,false,false,true,false,false,false}; //250, 350 open
    private boolean[] stage1 = {false,false,true,false,false,false,true,false,false,false};
    private boolean[] stage2 = {false,false,true,false,false,true,true,false,false,true}; //253, 353 open
    private boolean[] stage3 = {false,false,true,false,false,true,true,false,false,true};
    private boolean[] stage4 = {false,false,true,false,false,true,true,false,false,true};
    private boolean[] stage5 = {false,false,true,false,false,true,true,false,false,true};
    private boolean[] stage6 = {false,false,true,false,false,true,true,false,false,true};
    private boolean[] stage7 = {false,false,true,true,false,true,true,true,false,true}; //251, 351 open
    private boolean[] stage8 = {false,false,false,true,false,false,false,true,false,false}; //250,350, 252, 353 close
    private boolean[] stage9 = {true,true,false,true,false,false,false,true,false,false}; //150, 151
    private boolean[] stage10 = {true,true,false,true,false,false,false,true,false,false}; //150, 151

    //Holds all of the valve states for each sequence stage
    private boolean [][] stageStates = {stage0, stage1, stage2, stage3, stage4, stage5, stage6, stage7, stage8, stage9, stage10};

    //error from DAQ message string
    private String errorMessage = "";
    // warning from DAQ message string
    private String warningMessage = "";

    //Calibration count
    private int calibrationInt = 0;

    //error  messages to print based on the file Error.txt
    private String[] errorMsgs = {
            "E01: Over pressurization: Propellant",
            "E02: Over pressurization: Oxidizer",
            "E03-1: Over pressurization: Injector port 1",
            "E03-2: Over pressurization: Injector port 2",
            "E03-3: Over pressurization: Injector port 3",
            "E03-4: Over pressurization: Injector port 4",
            "E03-5: Over pressurization: Injector port 5",
            "E03-6: Over pressurization: Injector port 6",
            "E03-7: Over pressurization: Injector port 7",
            "E03-8: Over pressurization: Injector port 8",
            "E03-9: Over pressurization: Injector port 9",
            "E03-10: Over pressurization: Injector port 10",
            "E03-11: Over pressurization: Injector port 11",
            "E03-12: Over pressurization: Injector port 12",
            "E04: Engine temperature anomaly",
            "E05: Oxidizer weight loss",
            "E06: Propellant weight loss",
            "E07: LOX flow rate anomaly",
            "E08: RP Flow rate anomaly",
            "E09-1: Solenoid #1 actuation failed",
            "E09-2: Solenoid #2 actuation failed",
            "E09-3: Solenoid #3 actuation failed",
            "E09-4: Solenoid #4 actuation failed",
            "E09-5: Solenoid #5 actuation failed",
            "E09-6: Solenoid #6 actuation failed",
            "E09-7: Solenoid #7 actuation failed",
            "E09-8: Solenoid #8 actuation failed",
            "E09-9: Solenoid #9 actuation failed",
            "E09-10: Solenoid #10 actuation failed",
            "E09-11: Solenoid #11 actuation failed",
            "E09-12: Solenoid #12 actuation failed",
            "E10: Power rail anomaly",
            "E11: Electronics overheating"};

    private String[] warningMsgs = {
            "W01: Over pressurization: Propellant",
            "W02: Over pressurization: Oxidizer",
            "W03-1: Over pressurization: Injector port 1",
            "W03-2: Over pressurization: Injector port 2",
            "W03-3: Over pressurization: Injector port 3",
            "W03-4: Over pressurization: Injector port 4",
            "W03-5: Over pressurization: Injector port 5",
            "W03-6: Over pressurization: Injector port 6",
            "W03-7: Over pressurization: Injector port 7",
            "W03-8: Over pressurization: Injector port 8",
            "W03-9: Over pressurization: Injector port 9",
            "W03-10: Over pressurization: Injector port 10",
            "W03-11: Over pressurization: Injector port 11",
            "W03-12: Over pressurization: Injector port 12",
            "W04: Engine temperature anomaly",
            "W05: Oxidizer weight loss",
            "W06: Propellant weight loss",
            "W07: LOX flow rate anomaly",
            "W08: RP Flow rate anomaly",
            "W09-1: Solenoid #1 actuation failed",
            "W09-2: Solenoid #2 actuation failed",
            "W09-3: Solenoid #3 actuation failed",
            "W09-4: Solenoid #4 actuation failed",
            "W09-5: Solenoid #5 actuation failed",
            "W09-6: Solenoid #6 actuation failed",
            "W09-7: Solenoid #7 actuation failed",
            "W09-8: Solenoid #8 actuation failed",
            "W09-9: Solenoid #9 actuation failed",
            "W09-10: Solenoid #10 actuation failed",
            "W09-11: Solenoid #11 actuation failed",
            "W09-12: Solenoid #12 actuation failed",
            "W10: Power rail anomaly",
            "W11: Electronics overheating"};

    public void initialize(){
        Label[] currLabel={c1,c2,c3,c4,c5,c6,c7,c8,c9,c10};
        Label[] seqLabel={s0,s1,s2,s3,s4,s5,s6,s7,s8, s9, s10};

        for (int i=0; i<currLabel.length; i++) {
            String labName = currLabel[i].getText();
            currState[i]=labName.contains("Open"); //set the boolean[] currState to the state of each valve
        }
        reset();

        next1Orig = next1.getLayoutX();

        File fnew = new File("MARCO1");
        String line1 = "0";
        try {
            FileWriter fw = new FileWriter(fnew, false);
            fw.write(line1);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        timer.textProperty().bind(timeSeconds.asString());
        timer.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (timer.getText().equals("0")) {
                    ecState= "testing";
                    next.setDisable(true);
                }
                int timerCount = Integer.parseInt(timer.getText());
            }
        });

        errorCount.textProperty().bind(errortimeSeconds.asString());
        errorCount.textProperty().addListener((new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(errorCount.getText().equals("0")){
                    displayValveErrors();
                }
            }
        }));

        new Thread(() -> {
            while (true) {
                getCurrState();
                getErrors();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("sleep error");
                }
            }
        }).start();

    }

    /**
     * resets the GUI to standby mode
     */
    public void reset() {
        Label[] currLabel={c1,c2,c3,c4,c5,c6,c7,c8,c9,c10};
        Label[] seqLabel={s0,s1,s2,s3,s4,s5,s6,s7,s8, s9, s10};

        seqStage = 0; //resets the seqStage to stage 0

        //Iterates through the valve labels and sets them all to their initial states of closed
        for (int i=0; i<currLabel.length; i++) {
            currLabel[i].setText(valveName[i] + ": Closed");
            currLabel[i].setStyle("-fx-background-color: GREY");
        }

        //Iterates through all the seqLabel and turns them GREY
        for( Label seqLab : seqLabel) {
            seqLab.setStyle("-fx-background-color: GREY");
        }

        /* start/cancel button reset */
        next.setDisable(true);
        next1.setText("Start");
        next.setStyle("-fx-color: GREEN");

        /* reset the countDown */
        timeSeconds.set(STARTTIME);
        if (timeline != null) {
            timeline.stop();
        }

        errortimeSeconds.set(errorSTARTTIME);
        if (errortimeline != null) {
            errortimeline.stop();
        }

        ecState= "standby";
        currMode.setText("Stand By");
        standByBut.setDisable(false);
        caliBut.setDisable(false);
        hotBut.setDisable(false);

    }

    /**
     * Starts the countdown, sets the start button to a "cancel" button
     */
    private void countDown(){
        ecState= "countdown";

        //Styles the next button
        next.setStyle("-fx-color: #EB3505");
        next1.setText("Cancel");

        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(STARTTIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }

    /**
     * Start the countdown when there exists a valve in an incorrect state
     */
    private void errorCountDown(){
        if (errortimeline != null) {
            errortimeline.stop();
        }
        errortimeSeconds.set(errorSTARTTIME);
        errortimeline = new Timeline();
        errortimeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(errorSTARTTIME+1),
                        new KeyValue(errortimeSeconds, 0)));
        errortimeline.playFromStart();
    }

    private void resetErrorCount(){
        errortimeSeconds.set(errorSTARTTIME);
        if (errortimeline != null) {
            errortimeline.stop();
        }
    }


    /**
     * Opens the files and grabs the current open/closed state of the valves
     */
    private void readValveState(){
        for(int i = 0; i<fileNames.length; i++) {
            try{
                File file = new File("" +fileNames[i]);
                Scanner scanner = new Scanner(file);

                currState[i]= (scanner.nextDouble() > 0);
                scanner.close();

            }catch(Exception e) {
                //do nothing
            }
        }
    }

    /**
     * Calls the readValveState method then updates the current labels
     */
    private void getCurrState(){
        readValveState();
        Label[] currLabel = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10};

        Platform.runLater(new Runnable() {
            public void run() {
                for (int i = 0; i < currState.length; i++) {

                    if (currState[i])
                        currLabel[i].setText(valveName[i] + ": Open");
                    else
                        currLabel[i].setText(valveName[i] + ": Closed");

                    if (currState[i] != stageStates[seqStage][i])
                        currLabel[i].setStyle("-fx-background-color: RED");
                    else
                        currLabel[i].setStyle("-fx-background-color: GREEN");
                }
            }
        });
    }

    /**
     * Goes into the Error.txt file and grabs the errors String
     */
    private void getErrors(){
        Platform.runLater(new Runnable() {
            public void run() {
                try {
                    errorMessage = "Errors";
                    File file = new File("Error");
                    Scanner scanner = new Scanner(file);

                    char[] errors = scanner.next().toCharArray();
                    for( int i=0; i<errors.length; i++ ){
                        if( errors[i] == '1'){
                            if( errorMessage.length() != 0 ){
                                errorMessage += "\n";
                            }
                            errorMessage += errorMsgs[i];
                        }
                    }

                    warningMessage= "\n\nWarnings";
                    char[] warnings = scanner.next().toCharArray();
                    for( int i=0; i<warnings.length; i++ ){
                        if( warnings[i] == '1' ){
                            warningMessage += "\n";
                            warningMessage += warningMsgs[i];
                        }
                    }

                    scanner.close();
                    errorArea.setText(errorMessage + warningMessage);
                } catch(Exception e) {
                    System.out.println("error reading from errors file");
                }
            }
        });
    }

    /**
     * The method is used to write to the SeqStage.txt file manually
     * Eventually this will be changed with DAQ file writing
     */
    private void manualInc(){
        int stageNum = (seqStage + 1) % 11;
        File fnew = new File("SeqStage");
        String line1 = "" + stageNum;
        try {
            FileWriter fw = new FileWriter(fnew, false);
            fw.write(line1);
            fw.close();
        } catch (IOException e) {
            System.out.println("error Writing to sequence Stage");
        }
    }

    /**
     * This method is used to get the current HotFire Sequence stage from the SeqStage.txt file
     * @return the current Hotfire Sequence stage number
     */
    private int getSeqStage(){
        int stageNum = 0; // holds the number read from the file and is returned
        try{
            File file = new File("SeqStage");
            Scanner scanner = new Scanner(file);

            stageNum = scanner.nextInt();
            scanner.close();
        }catch(Exception e) {
            System.out.println("error reading from sequence stage");
        }
        return stageNum;
    }

    /**
     * Calibrate method to signal to the DAQ to start calibration
     */
    public void calibrate(){
        File fnew = new File("Calibrate");
        String line1 = "" + calibrationInt;
        try {
            FileWriter fw = new FileWriter(fnew, false);
            fw.write(line1);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        calibrationInt = (calibrationInt == 0) ? 1 : 0;
    }

    /**
     * Controls the next button functionality.
     * If clicked during counting down, the whole process is reset
     * Otherwise we increment the stage of the Hotfire Sequence
     */
    public void nextState() {
        Label[] seqLabel = {s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10}; // holds the labels for the HotFire Sequence
        Label[] currLabel = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10}; // holds labels for current valve states

        currMode.setText("Hot Fire");
        /* the sequence of states is standby -> preCount -> countdown -> testing */
        if ( ecState.equals("countdown")) { //if the cancel button is clicked during countdown
            reset(); //reset everything
        } else if (ecState.equals("standby")) { //if the button is clicked in standby
            next.setDisable(false);
            ecState= "preCount";
        } else if (ecState.equals("preCount")) {
            seqLabel[0].setStyle("-fx-background-color: GREEN");
            currGreen = seqLabel[0];
            standByBut.setDisable(true);
            caliBut.setDisable(true);
            //TODO uncomment the timer button trigger
            hotBut.setDisable(true);
            countDown();
        } else { // countdown is over, increment through the HotFire sequence
            manualInc();
            seqStage = getSeqStage();
            if( currGreen != null ){
                currGreen.setStyle("-fx-background-color: GREY");
            }
            if (seqStage == 0) { //if the sequence is now 0, the testing is over and reset everything
                reset();
            } else {
                seqLabel[seqStage].setStyle("-fx-background-color: GREEN");
                currGreen = seqLabel[seqStage];
                getCurrState();
                boolean valveInError = false; //keeps track of any valves in the wrong state
                for (int i=0; i<currState.length; i++) {
                    if (currState[i] != stageStates[seqStage][i]) {
                        currLabel[i].setStyle("-fx-background-color: RED");
                        valveInError = true;
                    } else {
                        currLabel[i].setStyle("-fx-background-color: GREY");
                    }
                }
                if(valveInError){
                    errorCountDown();
                }
            }
        }
    }

    /**
     * This is called when the errorCount for the valves reaches 0
     * When it is called it iterates through the labels and looks for the color RED
     * and if found it calls a function to add that valve to the error text section
     */
    private void displayValveErrors(){
        Label[] currLabel = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10}; // holds labels for current valve states
        resetErrorCount();
        errorValveString = "Valves In incorrect state:\n";
        for( int i = 0; i < currLabel.length; i++){
            if(currLabel[i].getStyle().contains("RED")){
                addValveError(i);
            }
        }
        errorArea.setText(errorMessage + warningMessage + "\n\n" + errorValveString);
    }

    /**
     * This function adds errors to the errorArea
     * @param indexOfValve represents the name of the valve that has an error position
     */
    private void addValveError( int indexOfValve ){
        errorValveString +=  "Warning: " + valveName[indexOfValve] +" not in correct state\n";
    }

    public void abortEntered() {
        FadeTransition fade = new FadeTransition(Duration.millis(250), abort1);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        FadeTransition fadein = new FadeTransition(Duration.millis(250), abort2);
        fadein.setFromValue((0.0));
        fadein.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(fade, fadein);
        pt.play();
    }
    public void abortExited() {
        FadeTransition fade = new FadeTransition(Duration.millis(250), abort2);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        FadeTransition fadein = new FadeTransition(Duration.millis(250), abort1);
        fadein.setFromValue((0.0));
        fadein.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(fade, fadein);
        pt.play();
    }
    public void abortPressed() {

        File fnew = new File("MARCO1");
        int num = 1;
        String line1 = "" + num;
        try {
            FileWriter fw = new FileWriter(fnew, false);
            fw.write(line1);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        reset();

    }

    public void nextEntered() {
        double preferredPos = next1Orig - 250;

        //get this code from http://stackoverflow.com/questions/31807329/get-screen-coordinates-of-a-node-in-javafx-8
        //it prevents wild offset of the animation
        if( next.getStyle().contains("GREEN")) {
            Bounds bounds = next1.getBoundsInLocal();
            Bounds screen = next1.localToScene(bounds);
            double translate = preferredPos - screen.getMinX();

            TranslateTransition away = new TranslateTransition(Duration.millis(250), next1);
            away.setByX(translate);

            TranslateTransition to = new TranslateTransition(Duration.millis(250), next2);
            to.setByX(translate);

            ParallelTransition pt = new ParallelTransition(away, to);
            pt.play();
        }
    }
    public void nextExited() {
        double preferredPos = next1Orig;
        Bounds bounds = next1.getBoundsInLocal();
        Bounds screen = next1.localToScene(bounds);
        //System.out.println(screen.getMinX());
        double translate = preferredPos - screen.getMinX();


        TranslateTransition away = new TranslateTransition(Duration.millis(250), next1);
        away.setByX(translate);

        TranslateTransition to = new TranslateTransition(Duration.millis(250), next2);
        to.setByX(translate);

        ParallelTransition pt = new ParallelTransition(away, to);
        pt.play();
    }
}
