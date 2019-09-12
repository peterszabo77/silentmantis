package mypackages;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.layout.FlowPane;

public class RecButView extends FlowPane
{
 public RecButView()
 {
  super();
  Text reclabel = new Text("RecButView");
  super.getChildren().add(reclabel);

 }
}
