package GUI;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.*;

/**
 * Controls the PNID screen.
 */
public class ControllerPNID {

	// initializes the fuel and LOX bars
	@FXML private Fun_Rect fuel786;
	@FXML private Fun_Rect lox707;

	private double prevLevel786;
	private double prevLevel707;

	@FXML private Rectangle botN21;

	// initializes the valves, each of which has an open and closed state
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

	// initializes the pressure transducer readings
	private Set<Label> psysLabels;
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

	// initializes the temperature readings
	private Set<Label> temperatureLabels;
	@FXML private Label T290;
	@FXML private Label T291;
	@FXML private Label T292;
	@FXML private Label T293;
	@FXML private Label T391;
	@FXML private Label T390;
	@FXML private Label T392;
	@FXML private Label T393;

	// files that contain the valve readings
	private String[] valveFiles = {"Val150", "Val151", "Val250", "Val251", "Val252", "Val253", "Val350", "Val351",
			"Val352", "Val353"};
	private String[] temperatureFiles = {"T290", "T291", "T292", "T293", "T390", "T391", "T392", "T393"};
	private boolean[] valveStates;

	// starting values for pressure and temperature
	private final double DEFAULT_PRESSURE = 0;
	private final double DEFAULT_TEMPERATURE = 0;

	// lox bar positioning
	private final int LOX_WIDTH = 65;
	private final int LOX_HEIGHT = 123;
	private final int LOX_X = 1384;
	private final int LOX_Y = 557;

	// fuel tank positioning
	private final int FUEL_WIDTH = LOX_WIDTH;
	private final int FUEL_HEIGHT = LOX_HEIGHT;
	private final int FUEL_X = 315;
	private final int FUEL_Y = LOX_Y;

	// thread sleep time (in milliseconds)
	private final int SLEEP_TIME = 100;

	/* to be used for a possible debug mode
	private boolean botN21Hovered = false;
	private boolean botN22Hovered= false;
	private boolean botN23Hovered = false;
	private boolean botN24Hovered = false;
	*/

	/**
	 * Initializes the necessary variables and runs the PNID in its own thread indefinitely.
	 */
	@FXML
	private void initialize() {

		// initializes the label sets--must be done here because of how the FXML initializes the labels
		psysLabels = new LinkedHashSet<>();
		Label[] PTLabels = {PT120, PT121, PT1221, PT1222, PT123, PT220, PT221, PT222, PT223, PT321, PT322, PT323,
				PT420, PT421};
		psysLabels.addAll(Arrays.asList(PTLabels));
		temperatureLabels = new LinkedHashSet<>();
		Label[] TLabels = {T290, T291, T292, T293, T390, T391, T392, T393};
		temperatureLabels.addAll(Arrays.asList(TLabels));

		// sets the pressure and temperature labels to the appopriate starting values
		for (Label label : psysLabels) {
			label.setText(Long.toString(Math.round(DEFAULT_PRESSURE)));
		}
		for (Label label : temperatureLabels) {
			label.setText(Long.toString(Math.round(DEFAULT_TEMPERATURE)));
		}

		// sets the state of all valves to true
		valveStates = new boolean[valveFiles.length];
		for (int i = 0; i < valveStates.length; i++) {
			valveStates[i] = true;
		}

		lox707.setPrefWidth(LOX_WIDTH);
		lox707.setPrefHeight(LOX_HEIGHT);
		lox707.setLayoutX(LOX_X);
		lox707.setLayoutY(LOX_Y);

		fuel786.setPrefWidth(FUEL_WIDTH);
		fuel786.setPrefHeight(FUEL_HEIGHT);
		fuel786.setLayoutX(FUEL_X);
		fuel786.setLayoutY(FUEL_Y);

		// runs the PNID indefinitely
		new Thread(() -> {
			while (true) {
				updateValues();
				updateState();
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					System.out.println("Process interrupted.");
				}
			}
		}).start();

	}

	/**
	 * Updates the values for the pressure and temperature readings as discrete values.
	 */
	private void updateValues() {

		// iterates through the PTx files and updates the pressure labels
		Platform.runLater(new Runnable() {
			public void run() {
				int i = 1;
				for (Label label : psysLabels) {
					File file = new File("PT" + i++);
					try {
						Scanner scanner = new Scanner(file);
						double readVal = scanner.nextDouble();
						double linTrans = (readVal - 0.5) * (1500.0/4.0);
						label.setText(Long.toString(Math.round(linTrans)));
						scanner.close();
					} catch (FileNotFoundException e) {
//						System.out.println("File not found: " + file.getName());
					} catch (NoSuchElementException e) {

						// can be caused by the file being written to while attempting to read
						System.out.println("Unable to read file: PT" + (i-1));

					}
				}
			}
		});

		// iterates through the Tx files and updates the temperature labels
		Platform.runLater(new Runnable() {
			public void run() {
				int i = 0;
				for (Label label : temperatureLabels) {
					File file = new File(temperatureFiles[i++]);
					try {
						Scanner scanner = new Scanner(file);
						label.setText(Long.toString(Math.round(scanner.nextDouble())));
						scanner.close();
					} catch (FileNotFoundException e) {
//						System.out.println("File not found: " + file.getName());
					} catch (NoSuchElementException e) {

						// can be caused by the file being written to while attempting to read
						System.out.println("Unable to read file: T" + (i-1));

					}
				}
			}
		});

		// iterates through the fuel tank files and updates the fuel labels
		File file = null;
		Scanner scanner = null;
		try {
			file = new File("Tank_Fuel_1");
			scanner = new Scanner(file);
			double funLevel707 = scanner.nextDouble() / 100;
			if (funLevel707 != prevLevel707) {
				lox707.setLevel(funLevel707);
				prevLevel707 = funLevel707;
			}
			scanner.close();

			file = new File("Tank_Fuel_2");
			scanner = new Scanner(file);
			double funLevel786 = scanner.nextDouble() / 100;
			if (funLevel786 != prevLevel786) {
				fuel786.setLevel(funLevel786);
				prevLevel786 = funLevel786;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
//			System.out.println("File not found: " + file.getName());
		} catch (NoSuchElementException e) {

			// can be caused by the file being written to while attempting to read
			System.out.println("Unable to read file: " + file.getName());

		}
	}

	/**
	 * Updates the internal values for the valve states. Used by updateState().
	 */
	private void getState() {
		for (int i = 0; i < valveFiles.length; i++) {
			File file = new File(valveFiles[i]);
			try {
				Scanner scanner = new Scanner(file);
				valveStates[i] = (scanner.nextDouble() > 0);
				scanner.close();
			} catch (FileNotFoundException e) {
//				System.out.println("File not found: " + file.getName());
			} catch (NoSuchElementException e) {
				System.out.println("Unable to read file: " + file.getName());
			}
		}
	}

	/**
	 * Switches valve states between open/closed if a change is detected.
	 */
	private void updateState() {
		getState();

		// pre-update states can be determined by the visibility of the open/closed images
		ImageView[] prevStates = {open150, open151, open250, open251, open252, open253, open350, open351, open352,
				open353};
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < valveStates.length; i++) {
					if (prevStates[i].isVisible() != valveStates[i]) {
						switch (i) {
							case 0:
								switchValve(closed150, open150);
								break;
							case 1:
								switchValve(closed151, open151);
								break;
							case 2:
								switchValve(closed250, open250);
								break;
							case 3:
								switchValve(closed251, open251);
								break;
							case 4:
								switchValve(closed252, open252);
								break;
							case 5:
								switchValve(closed253, open253);
								break;
							case 6:
								switchValve(closed350, open350);
								break;
							case 7:
								switchValve(closed351, open351);
								break;
							case 8:
								switchValve(closed352, open352);
								break;
							case 9:
								switchValve(closed353, open353);
								break;
						}
					}
				}
			}
		});

	}

	/**
	 * Helper method to switch between open/closed valve states.
	 * @param closed the "closed" image representation of the valve
	 * @param open the "open" image representation of the valve
	 */
	private void switchValve(ImageView closed, ImageView open) {
		boolean bool = closed.isVisible();
		closed.setVisible(!bool);
		open.setVisible(bool);
	}

}