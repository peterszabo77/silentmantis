package mypackages;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.control.SelectionMode;
import java.io.File;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.layout.Priority;
import javafx.scene.input.KeyEvent; 
import javafx.scene.input.KeyCode; 
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.collections.ObservableMap;
import java.util.Map;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status; 


import mypackages.DataModel;
import mypackages.BVScale;
import mypackages.BVType;
import mypackages.BVColor;
import mypackages.KeyButtonsPane;

public class VideoTab extends Tab
{
 public DataModel dataModel;
 public MediaView mainVideoView;
 public Slider posSlider;
 public HBox ctrlHBox;
 public HBox timeHBox;
 public HBox settingsHBox;
 public HBox msHBox;
 public Button stopButton;
 public Button playButton;
 public Label fileLabel;
 public Label timePosLabel;
 public Label timeLengthLabel;
 public Label speedLabel;
 public Label volumeLabel;
 public Label msPosLabel;
 public Label msLengthLabel;
 public Slider volumeSlider;
 public SpinnerValueFactory<String> speedFactory;
 public Spinner<String> speedSpinner;

 public KeyButtonsPane keyButtonsPane;

 public VideoTab(String tablabel, DataModel datamodel_in)
 {
  super(tablabel);
  dataModel=datamodel_in;

  //create main image and its horizontal sizer
  mainVideoView=new MediaView();
  mainVideoView.setPreserveRatio(true); 
  StackPane stackPane=new StackPane(mainVideoView);
  stackPane.setMinWidth(1);
  stackPane.setMinHeight(1);
  stackPane.setMaxWidth(Double.MAX_VALUE);
  stackPane.setMaxHeight(Double.MAX_VALUE);
  mainVideoView.fitHeightProperty().bind(stackPane.heightProperty());
  mainVideoView.fitWidthProperty().bind(stackPane.widthProperty());

  //create slider and its horizontal sizer
  posSlider=new Slider();
  posSlider.setMaxWidth(Double.MAX_VALUE);
  HBox sliderHBox=new HBox(posSlider);
  sliderHBox.setHgrow(posSlider, Priority.ALWAYS);
  sliderHBox.setSpacing(5);
  sliderHBox.setPadding(new Insets(5, 10, 5, 10));

  //create control buttons and their horizontal sizer
  int imagesize=20;
  String imagespath=System.getProperty("user.dir").replace('\\', '/') + "/images/";
  File stopFile = new File(imagespath+"stop.png");
  File playFile = new File(imagespath+"play.png");
  Image stopImage = new Image(stopFile.toURI().toString(), imagesize, imagesize, false, false);
  Image playImage = new Image(playFile.toURI().toString(), imagesize, imagesize, false, false);
  ImageView stopImageView=new ImageView(stopImage);
  ImageView playImageView=new ImageView(playImage);
  stopButton = new Button();
  stopButton.setGraphic(stopImageView);
  playButton = new Button();
  playButton.setGraphic(playImageView);
  fileLabel=new Label("");
  timePosLabel=new Label("");
  timeLengthLabel=new Label("");
  timeHBox=new HBox(timePosLabel, timeLengthLabel);
  timeHBox.setSpacing(5);
  timeHBox.setPadding(new Insets(5, 10, 5, 10));
  timeHBox.setAlignment(Pos.CENTER_RIGHT);
  ctrlHBox=new HBox(stopButton, playButton, fileLabel, timeHBox);
  ctrlHBox.setSpacing(5);
  ctrlHBox.setPadding(new Insets(5, 10, 5, 10));
  ctrlHBox.setHgrow(timeHBox, Priority.ALWAYS);
  ctrlHBox.setAlignment(Pos.CENTER_LEFT);

  //create settings and their horizontal sizer
  speedLabel=new Label("Speed:");
  ObservableList<String> speedList=FXCollections.observableArrayList("0.1x","0.2x","0.5x","1x","2x","5x","10x");
  speedFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(speedList);
  speedFactory.setValue("1x");
  speedSpinner = new Spinner<String>(speedList);
  speedSpinner.setValueFactory(speedFactory);
  volumeLabel=new Label("Volume:");
  volumeSlider=new Slider();
  volumeSlider.setMin(0); volumeSlider.setMax(1); volumeSlider.setValue(1);
  msPosLabel=new Label("");
  msLengthLabel=new Label("");
  msHBox=new HBox(msPosLabel, msLengthLabel);
  msHBox.setSpacing(5);
  msHBox.setPadding(new Insets(5, 10, 5, 10));
  msHBox.setAlignment(Pos.CENTER_RIGHT);
  settingsHBox=new HBox(speedLabel, speedSpinner, volumeLabel, volumeSlider, msHBox);
  settingsHBox.setSpacing(5);
  settingsHBox.setPadding(new Insets(5, 10, 5, 10));
  settingsHBox.setHgrow(msHBox, Priority.ALWAYS);
  settingsHBox.setAlignment(Pos.CENTER_LEFT);

  //create keybuttons pane
  keyButtonsPane=new KeyButtonsPane(dataModel);

  //set main sizer - whole tab
  VBox vBox = new VBox(stackPane, sliderHBox, ctrlHBox, settingsHBox, keyButtonsPane);
  vBox.setVgrow(stackPane, Priority.SOMETIMES);

  this.setContent(vBox);
 }

}
