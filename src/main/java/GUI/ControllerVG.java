package GUI;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

import java.util.*;
import java.io.*;

/**
 * Controls the VG screen.
 */
public class ControllerVG {

	// initializes the psys gauges (incl. the tank psys gauges at #13/14)
	private Set<Gauge> psysGauges;
	@FXML private Label title;
	@FXML private Gauge psys1;
	@FXML private Gauge psys2;
	@FXML private Gauge psys3;
	@FXML private Gauge psys4;
	@FXML private Gauge psys5;
	@FXML private Gauge psys6;
	@FXML private Gauge psys7;
	@FXML private Gauge psys8;
	@FXML private Gauge psys9;
	@FXML private Gauge psys10;
	@FXML private Gauge psys11;
	@FXML private Gauge psys12;
	@FXML private Gauge psys13;
	@FXML private Gauge psys14;

	@FXML private FunLevel Tank_Fuel_1;
	@FXML private Gauge Tank_Temp_1;
	@FXML private FunLevel Tank_Fuel_2;
	@FXML private Gauge Tank_Temp_2;
	@FXML private Gauge battery;
	@FXML private Gauge Thrust_Gauge;

	// tracks the thrust value
	private double thrustVal;

	// thread sleep time (in milliseconds)
	private final int SLEEP_TIME = 100;

	@FXML private LineChart <String, Number> chart1;
	@FXML private LineChart <String, Number> chart2;
	@FXML private LineChart <String, Number> chartThrust;
	private XYChart.Series<String, Number> series1 = new XYChart.Series<>();
	private XYChart.Series<String, Number> series2 = new XYChart.Series<>();
	private XYChart.Series<String, Number> seriesThrust = new XYChart.Series<>();

	// the charts' x-positions
	private int chartCount = 0;


	/**
	 * Initializes the necessary variables and runs the VG in its own thread indefinitely.
	 */
	@FXML
	private void initialize() {

		// initializes the gauge set--must be done here because of how the FXML initializes the gauges
		psysGauges = new LinkedHashSet<>();
		Gauge[] psys = {psys1, psys2, psys3, psys4, psys5, psys6, psys7, psys8, psys9, psys10, psys11, psys12,
				psys13, psys14};
		psysGauges.addAll(Arrays.asList(psys));

		List<Stop> stops = new LinkedList<>();
		stops.add(new Stop(0.00, Color.RED));
		stops.add(new Stop(0.50, Color.YELLOW));
		stops.add(new Stop(1.00, Color.GREEN));

/*
		Tank_Fuel_1.setGradientBarEnabled(true);
		Tank_Fuel_1.setGradientBarStops(stops);
		Tank_Fuel_2.setGradientBarEnabled(true);
		Tank_Fuel_2.setGradientBarStops(stops);
*/

		// battery in the top right corner
		battery.setGradientBarEnabled(true);
		battery.setGradientBarStops(stops);

		List<Section> secs = new LinkedList<>();
		secs.add(new Section(0.0, 100.0, Color.RED));
		secs.add(new Section(100.0, 1300.0, Color.GREEN));
		secs.add(new Section(1300.0, 1440.0, Color.ORANGE));
		secs.add(new Section(1440.0, 1600.0, Color.RED));
		for (Gauge psysGauge : psysGauges) {
			psysGauge.setSections(secs);
		}

//		Tank_PT_1.setSections(secs);
//		Tank_PT_2.setSections(secs);

		LinkedList<Section> temp = new LinkedList<>();
		temp.add(new Section(0.0, 700.0, Color.GREEN));
		temp.add(new Section(700.0, 800.0, Color.ORANGE));
		temp.add(new Section(800.0, 1000.0, Color.RED));
		Tank_Temp_1.setSections(temp);
		Tank_Temp_2.setSections(temp);

		for (Gauge psysGauge : psysGauges) {
			psysGauge.setValueColor(Color.WHITE);
			psysGauge.setUnitColor(Color.WHITE);
		}

//		Tank_PT_1.setValueColor(Color.WHITE);
//		Tank_PT_2.setValueColor(Color.WHITE);

		Tank_Temp_1.setUnitColor(Color.WHITE);
		Tank_Temp_2.setUnitColor(Color.WHITE);

		chart1.getYAxis().setAutoRanging(false);
		chart1.getYAxis().setTickLength(10.0);
		chart1.getData().clear();
		series1 = new XYChart.Series<>();
		for (int i = 1; i <= psysGauges.size(); i++) {
			series1.getData().add(new XYChart.Data<>(Integer.toString(i), 0));
		}
		chart1.getData().add(series1);

		chart2.getYAxis().setAutoRanging(false);
		chart2.getYAxis().setTickLength(10.0);
		chart2.getData().clear();
		series2 = new XYChart.Series<>();
		for (int i = 1; i <= psysGauges.size(); i++) {
			series2.getData().add(new XYChart.Data<>(Integer.toString(i), 0));
		}
		chart2.getData().add(series2);

		chartThrust.getYAxis().setAutoRanging(false);
		chartThrust.getYAxis().setTickLength(10.0);
		chartThrust.getData().clear();
		seriesThrust = new XYChart.Series<>();
		for (int i = 1; i <= psysGauges.size(); i++) {
			seriesThrust.getData().add(new XYChart.Data<>(Integer.toString(i), 0));
		}
		chartThrust.getData().add(seriesThrust);

		new Thread(() -> {
			while (true) {
				updateVal();
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					System.out.println("Process interrupted.");
				}
			}
		}).start();
	}

	/**
	 * Updates the gauge values and chart.
	 */
	@FXML
	private void updateVal() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				// iterates through and updates values for psys gauges
				int i = 1;
				for (Gauge psysGauge : psysGauges) {
					File file = new File("PT" + i++);
					try {
						Scanner scanner = new Scanner(file);
						psysGauge.setValue(Math.round(scanner.nextDouble()));
						scanner.close();
					} catch (FileNotFoundException e) {
						System.out.println("File not found: " + file.getName());
					} catch (NoSuchElementException e) {

						// can be caused by the file being written to while attempting to read
						System.out.println("Unable to read file: " + file.getName());

					}
				}

				// iterates through and updates values for misc gauges
				File file = null;
				Scanner scanner = null;
				try {

					file = new File("Tank_Fuel_1");
					scanner = new Scanner(file);
					Tank_Fuel_2.setLevel(scanner.nextDouble() / 100);
					scanner.close();

					file = new File("Tank_Fuel_2");
					scanner = new Scanner(file);
					Tank_Fuel_1.setLevel(scanner.nextDouble() / 100);
					scanner.close();

					file = new File("Tank_Temp_1");
					scanner = new Scanner(file);
					Tank_Temp_1.setValue(scanner.nextDouble());
					scanner.close();

					file = new File("Tank_Temp_2");
					scanner = new Scanner(file);
					Tank_Temp_2.setValue(scanner.nextDouble());
					scanner.close();

					file = new File("Battery");
					scanner = new Scanner(file);
					battery.setValue(scanner.nextDouble());
					scanner.close();

					file = new File("Thrust");
					scanner = new Scanner(file);
					thrustVal = scanner.nextDouble();
					Thrust_Gauge.setValue(thrustVal);
					scanner.close();
				} catch (FileNotFoundException e) {
					System.out.println("File not found: " + file.getName());
				} catch (NoSuchElementException e) {

					// can be caused by the file being written to while attempting to read
					System.out.println("Unable to read file: " + file.getName());

				}

				// updates charts every second
				chartCount++;
				if (chartCount % 10 == 0) {
					int chartTime = (chartCount / 10) - 1;
					int nthLast = 0;
					Gauge[] arr = psysGauges.toArray(new Gauge[0]);

					double tank2Y = arr[arr.length - ++nthLast].getValue();
					series2.getData().remove(0);
					series2.getData().add(new XYChart.Data<>(Integer.toString(chartTime), tank2Y));

					double tank1Y = arr[arr.length - ++nthLast].getValue();
					series1.getData().remove(0);
					series1.getData().add(new XYChart.Data<>(Integer.toString(chartTime), tank1Y));

					seriesThrust.getData().remove(0);
					seriesThrust.getData().add(new XYChart.Data<>(Integer.toString(chartTime), Math.round(thrustVal)));
				}
			}
		});

	}

}
