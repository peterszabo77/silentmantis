import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Sitandwait extends Application 
{
 ControllerMain controllerMain;

 public static void main(String[] args) 
 {
  Application.launch(args);
 }

 @Override
 public void start(Stage stage)
 {
  controllerMain=new ControllerMain();
 }
}
