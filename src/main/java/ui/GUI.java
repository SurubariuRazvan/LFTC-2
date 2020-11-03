package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application {
    public void run(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(getClass().getResource("/fxml/MainWindow.fxml"));
        StackPane rootLayout = loader.load();
        MainWindowController controller = loader.getController();
        controller.setStage(primaryStage);
        
        Scene scene = new Scene(rootLayout);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
