package GUI;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Section;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import java.util.LinkedList;
import java.util.*;
import java.io.*;


public class ControllerVG {
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
    //@FXML private Gauge Tank_Fuel_1;
    @FXML private FunLevel Tank_Fuel_1;
    //@FXML private Gauge Tank_PT_1;
    @FXML private Gauge Tank_Temp_1;
    //@FXML private Gauge Tank_Fuel_2;
    @FXML private FunLevel Tank_Fuel_2;
    //@FXML private Gauge Tank_PT_2;
    @FXML private Gauge Tank_Temp_2;
    @FXML private Gauge battery;
    @FXML private Gauge Thrust_Gauge;
    private double thrustVal;
    private double prevFunLevel1;
    private double prevFunLevel2;

    private Gauge[] psys = {psys1, psys2,psys3,psys4, psys5, psys6, psys7,psys8,psys9, psys10, psys11, psys12,psys13,psys14};
    private double [] psysVals = new double [14];


    @FXML private LineChart <String, Number> chart1;
    @FXML private LineChart <String, Number> chart2;
    @FXML private LineChart <String, Number> chartThrust;
    private XYChart.Series<String, Number> series1 = new XYChart.Series<>();
    private XYChart.Series<String, Number> series2 = new XYChart.Series<>();
    private XYChart.Series<String, Number> seriesThrust = new XYChart.Series<>();
    private int xval = 12;
    private int timeToUpdate = 0;



    @FXML
    private void initialize() {
        Gauge[] psys = {psys1, psys2,psys3,psys4, psys5, psys6, psys7,psys8,psys9, psys10, psys11, psys12,psys13,psys14};

        //Tank_Fuel_1.setGradientBarEnabled(true);
        //Tank_Fuel_2.setGradientBarEnabled(true);
        LinkedList<Stop> stops = new LinkedList<Stop>();
        stops.add(new Stop(0.0, Color.RED));
        stops.add(new Stop(.50, Color.YELLOW));
        stops.add(new Stop(1.00, Color.GREEN));
        //Tank_Fuel_1.setGradientBarStops(stops);
        //Tank_Fuel_2.setGradientBarStops(stops);

        //battery in the top right corner
        battery.setGradientBarEnabled(true);
        battery.setGradientBarStops(stops);

        LinkedList<Section> secs = new LinkedList<>();
        secs.add(new Section(0.0, 100.0, Color.RED));
        secs.add(new Section(100.0, 1300.0, Color.GREEN));
        secs.add(new Section(1300.0, 1440.0, Color.ORANGE));
        secs.add(new Section(1440.0, 1600.0, Color.RED));
        for (int i=0; i<psys.length; i++) {
            psys[i].setSections(secs);
        }

        //Tank_PT_1.setSections(secs);
        //Tank_PT_2.setSections(secs);

        LinkedList<Section> temp = new LinkedList<>();
        temp.add(new Section(0.0, 700.0, Color.GREEN));
        temp.add(new Section(700.0, 800.0, Color.ORANGE));
        temp.add(new Section(800.0, 1000.0, Color.RED));
        Tank_Temp_1.setSections(temp);
        Tank_Temp_2.setSections(temp);

        for (int i=0; i<psys.length; i++) {
            psys[i].setValueColor(Color.WHITE);
        }

        //Tank_PT_1.setValueColor(Color.WHITE);
        //Tank_PT_2.setValueColor(Color.WHITE);

        for (int i=0; i<psys.length; i++) {
            psys[i].setUnitColor(Color.WHITE);
        }

        Tank_Temp_1.setUnitColor(Color.WHITE);
        Tank_Temp_2.setUnitColor(Color.WHITE);

        chart1.getYAxis().setAutoRanging(false);
        chart1.getYAxis().setTickLength(10.0);
        chart1.getData().clear();
        series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>("1", 0));
        series1.getData().add(new XYChart.Data<>("2", 0));
        series1.getData().add(new XYChart.Data<>("3", 0));
        series1.getData().add(new XYChart.Data<>("4", 0));
        series1.getData().add(new XYChart.Data<>("5", 0));
        series1.getData().add(new XYChart.Data<>("6", 0));
        series1.getData().add(new XYChart.Data<>("7", 0));
        series1.getData().add(new XYChart.Data<>("8", 0));
        series1.getData().add(new XYChart.Data<>("9", 0));
        series1.getData().add(new XYChart.Data<>("10", 0));
        series1.getData().add(new XYChart.Data<>("11", 0));
        series1.getData().add(new XYChart.Data<>("12", 0));
        chart1.getData().add(series1);

        chart2.getYAxis().setAutoRanging(false);
        chart2.getYAxis().setTickLength(10.0);
        chart2.getData().clear();
        series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data<>("1", 0));
        series2.getData().add(new XYChart.Data<>("2", 0));
        series2.getData().add(new XYChart.Data<>("3", 0));
        series2.getData().add(new XYChart.Data<>("4", 0));
        series2.getData().add(new XYChart.Data<>("5", 0));
        series2.getData().add(new XYChart.Data<>("6", 0));
        series2.getData().add(new XYChart.Data<>("7", 0));
        series2.getData().add(new XYChart.Data<>("8", 0));
        series2.getData().add(new XYChart.Data<>("9", 0));
        series2.getData().add(new XYChart.Data<>("10", 0));
        series2.getData().add(new XYChart.Data<>("11", 0));
        series2.getData().add(new XYChart.Data<>("12", 0));
        chart2.getData().add(series2);

        chartThrust.getYAxis().setAutoRanging(false);
        chartThrust.getYAxis().setTickLength(10.0);
        chartThrust.getData().clear();
        seriesThrust = new XYChart.Series<>();
        seriesThrust.getData().add(new XYChart.Data<>("1", 0));
        seriesThrust.getData().add(new XYChart.Data<>("2", 0));
        seriesThrust.getData().add(new XYChart.Data<>("3", 0));
        seriesThrust.getData().add(new XYChart.Data<>("4", 0));
        seriesThrust.getData().add(new XYChart.Data<>("5", 0));
        seriesThrust.getData().add(new XYChart.Data<>("6", 0));
        seriesThrust.getData().add(new XYChart.Data<>("7", 0));
        seriesThrust.getData().add(new XYChart.Data<>("8", 0));
        seriesThrust.getData().add(new XYChart.Data<>("9", 0));
        seriesThrust.getData().add(new XYChart.Data<>("10", 0));
        seriesThrust.getData().add(new XYChart.Data<>("11", 0));
        seriesThrust.getData().add(new XYChart.Data<>("12", 0));
        chartThrust.getData().add(seriesThrust);

        new Thread(() -> {
            while (true) {
                updateVal();
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("sleep error");
                }
            }
        }).start();
    }

    private void getVal() {
        Gauge[] psys = {psys1, psys2,psys3,psys4, psys5, psys6, psys7,psys8,psys9, psys10, psys11, psys12,psys13,psys14};
        for(int i = 1; i<=psys.length; i++) {
            try{
                File file = new File("PT" + i);
                Scanner scanner = new Scanner(file);

                double retVal = scanner.nextDouble();
                scanner.close();

                psysVals[i-1] = retVal;
                //psys[i].setValue(retVal);

            }catch(Exception e) {
                //do nothing
            }
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    /*File file = new File("Tank_Fuel_1");
                    Scanner scanner = new Scanner(file);
                    double currFunLevel1 = scanner.nextDouble()/100;
                    if(currFunLevel1 != prevFunLevel1) {
                        Tank_Fuel_1.setLevel(currFunLevel1);
                        prevFunLevel1 = currFunLevel1;
                    }
                    scanner.close();

                    Tank_PT_1.setValue((int)psysVals[12]);*/

                    File file = new File("Tank_Temp_1");
                    Scanner scanner = new Scanner(file);
                    Tank_Temp_1.setValue(scanner.nextDouble());
                    scanner.close();

                    /*file = new File("Tank_Fuel_2");
                    scanner = new Scanner(file);
                    double currFunLevel2 = scanner.nextDouble()/100;
                    if(currFunLevel2 != prevFunLevel2) {
                        Tank_Fuel_2.setLevel(currFunLevel2);
                        prevFunLevel2 = currFunLevel2;
                    }
                    scanner.close();

                    Tank_PT_2.setValue((int)psysVals[13]);*/

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
                }catch( Exception e) {
                    //do nothing;
                }
            }
        });

    }



    @FXML
    private void updateVal(){
        getVal();
        Gauge[] psys = {psys1, psys2,psys3,psys4, psys5, psys6, psys7,psys8,psys9, psys10, psys11, psys12,psys13,psys14};
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<psys.length; i++) {
                    psys[i].setValue((int)psysVals[i]);
                }
            }
        });

        //allows the the chart to update in real time

        timeToUpdate++;
        if (timeToUpdate%10 == 0) {
            Platform.runLater(new Runnable() {
                public void run() {
                    int y = (int)psysVals[12];
                    series1.getData().remove(0);
                    series1.getData().add(new XYChart.Data<>("" + ++xval, y));

                    y = (int)psysVals[13];
                    series2.getData().remove(0);
                    series2.getData().add(new XYChart.Data<>("" + xval, y));

                    y = (int)thrustVal;
                    seriesThrust.getData().remove(0);
                    seriesThrust.getData().add(new XYChart.Data<>("" + xval, y));
                }
            });
        }

    }

}
