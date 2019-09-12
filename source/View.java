import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tab;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.media.Media;
import javafx.geometry.Orientation;
import javafx.scene.layout.Priority;
import javafx.geometry.Rectangle2D;

import mypackages.DataModel;
import mypackages.VideoTab;
import mypackages.ImagesTab;
import mypackages.VariablesTab;
import mypackages.KeysTab;
import mypackages.RecordsTab;
import mypackages.RecButView;
import mypackages.MenuBarView;
import mypackages.TimeLineView;
import mypackages.KeyButtonsPane;

public class View extends Stage
{
 public DataModel dataModel;
 public TabPane tabPane;
 public VideoTab videoTab;
 public ImagesTab imagesTab;
 public VariablesTab variablesTab;
 public KeysTab keysTab;
 public RecordsTab recordsTab;
 public MenuBarView menuBarView;
 TimeLineView timeLineView;

 public View(DataModel dm)
 {
  dataModel=dm;

  tabPane = new TabPane();
  tabPane.setMaxHeight(Double.MAX_VALUE);

  videoTab = new VideoTab("Video", dataModel);
  imagesTab = new ImagesTab("Images", dataModel);
  variablesTab = new VariablesTab("Variables", dataModel);
  keysTab = new KeysTab("Keys", dataModel);
  recordsTab = new RecordsTab("Records", dataModel);

  tabPane.getTabs().addAll(videoTab);
  tabPane.getTabs().addAll(imagesTab);
  tabPane.getTabs().addAll(variablesTab);
  tabPane.getTabs().addAll(keysTab);
  tabPane.getTabs().addAll(recordsTab);
  tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

  menuBarView=new MenuBarView();
  timeLineView=new TimeLineView();
  SplitPane splitpane=new SplitPane(tabPane, timeLineView);
  splitpane.setOrientation(Orientation.VERTICAL);
  splitpane.setDividerPositions(1.0f);

  VBox vBox = new VBox(menuBarView, splitpane);
  vBox.setVgrow(splitpane, Priority.SOMETIMES);

  Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
  Scene scene = new Scene(vBox, primScreenBounds.getWidth()*0.5, primScreenBounds.getWidth()*0.4);
  setScene(scene);
  
  splitpane.setMaxHeight(Double.MAX_VALUE);
  splitpane.setResizableWithParent(timeLineView, false);

  setTitle("Monyok");
  show();
 }
 
}
