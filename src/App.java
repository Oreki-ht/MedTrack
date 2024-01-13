import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
    
    Parent root;
    try {
    root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
      Scene scene = new Scene(root);
        primaryStage.setTitle("Pharmacy Login");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
    } catch (IOException e) {
        System.out.println(e);
    }
    }
 
 public static void main(String[] args) {
      launch(args);
    }
}