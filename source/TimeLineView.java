package mypackages;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;

public class TimeLineView extends HBox
{
 public Canvas canvas;
 public GraphicsContext gc;
 HBox canvasBox;
 StackPane canvasPane;
 public CheckBox updateCheckBox;
 public Button updateButton;
 VBox vBox;

 public TimeLineView()
 {
  super();
  canvas = new Canvas();
  gc = canvas.getGraphicsContext2D();
  gc.setFill(Color.BLUE);
  gc.fillRect(75,75,100,100);
  canvasPane=new StackPane(canvas);
  canvasPane.setMinWidth(1);
  canvasPane.setMinHeight(1);
  canvasPane.setMaxWidth(Double.MAX_VALUE);
  canvasPane.setMaxHeight(Double.MAX_VALUE);
  updateCheckBox=new CheckBox("Live update"); updateCheckBox.setSelected(true);
  updateButton=new Button("Update"); updateButton.setDisable(true);
  vBox=new VBox(updateCheckBox, updateButton);
  super.getChildren().addAll(canvasPane, vBox);
  setHgrow(canvasPane, Priority.ALWAYS);
  //setAlignment(Pos.BASELINE_LEFT);
  canvas.widthProperty().bind(canvasPane.widthProperty());
  canvas.heightProperty().bind(canvasPane.heightProperty());
 }
 public void redraw() 
 {
  gc.setFill(Color.BLUE);
  gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
 }
}
