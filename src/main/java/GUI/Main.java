package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

	private static final double PNID_SCALE = .8;
	private static final double VG_SCALE = .8;
	private static final double EC_SCALE = .8;

	private static final double PNID_OFFSET = 2;
	private static final double VG_OFFSET = 1.8;
	private static final double EC_OFFSET = 1.8;

	private static final double screenWidth = 1280;
	private static final double screenHeight = 720;

	public Main() {}

	@Override
	public void start(Stage primaryStage) throws Exception {
		(new Thread(new FTPData())).start();

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/VG_Fitted.fxml"));
		Pane pane = (Pane) loader.load();
		ControllerVG contr1 = loader.getController();
		//ControllerVG contr1 = new ControllerVG();
		//contr1.setMain(this); //give controller access to main methods such as updateChart
		Scene scene = new Scene(pane);
		scene.setFill(Color.BLACK);
		pane.setScaleX(VG_SCALE);
		pane.setScaleY(VG_SCALE);
		pane.setTranslateX(-screenWidth * (1-VG_SCALE)/VG_OFFSET);
		pane.setTranslateY(-screenHeight * (1-VG_SCALE)/VG_OFFSET);
		primaryStage.setScene(scene);
		primaryStage.show();

		Stage stage = new Stage();
		stage.setTitle("PNID Scene");
		loader = new FXMLLoader(Main.class.getResource("/PNID_Fitted.fxml"));
		Pane pane2 = (Pane)loader.load();
		//ControllerPNID contPNID = loader.getController();
		ControllerPNID contPNID = new ControllerPNID();
		loader.setController(contPNID);

		scene = new Scene(pane2);
		pane2.setScaleX(PNID_SCALE);
		pane2.setScaleY(PNID_SCALE);
		pane2.setTranslateX(-screenWidth * (1-PNID_SCALE)/PNID_OFFSET);
		pane2.setTranslateY(-screenHeight * (1-PNID_SCALE)/PNID_OFFSET);
		stage.setScene(scene);
		stage.setFullScreen(true);
		stage.show();



		Stage newstage = new Stage();
		newstage.setTitle("Executive Queue");
		loader = new FXMLLoader(Main.class.getResource("/ExecQueue_Fitted.fxml"));
		Pane newpane2 = (Pane)loader.load();

		newpane2.setScaleX(EC_SCALE);
		newpane2.setScaleY(EC_SCALE);
		newpane2.setTranslateX(-screenWidth * (1-EC_SCALE)/EC_OFFSET);
		newpane2.setTranslateY(-screenHeight * (1-EC_SCALE)/EC_OFFSET);
		scene = new Scene(newpane2);
		scene.setFill(Color.BLACK);
		newstage.setScene(scene);
		newstage.setFullScreen(true);
		newstage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
