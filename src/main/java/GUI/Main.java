package GUI;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


	public Main() {
	}

	@Override
	public void start(Stage primaryStage) throws Exception{

		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/VG_Fitted.fxml"));
		Pane pane = (Pane)loader.load();
		ControllerVG contr1 = loader.getController();
		//ControllerVG contr1 = new ControllerVG();
		//contr1.setMain(this); //give controller access to main methods such as updateChart
		Scene scene = new Scene(pane);
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
		stage.setScene(scene);
		stage.show();

		//DoubleController dubCont = new DoubleController( contr1, contPNID );
		/*new Thread(() -> {
			while (true) {
				contr1.updateVal();
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					System.out.println("sleep error");
				}
			}
		}).start();*/


		Stage newstage = new Stage();
		newstage.setTitle("Executive Cue");
		loader = new FXMLLoader(Main.class.getResource("/ExecCue_Fitted.fxml"));
		Pane newpane2 = (Pane)loader.load();
		scene = new Scene(newpane2);
		newstage.setScene(scene);
		newstage.show();

	}


	public static void main(String[] args) {
		launch(args);
	}
}
