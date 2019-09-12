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

import mypackages.DataModel;
import mypackages.BVScale;
import mypackages.BVType;
import mypackages.BVColor;
import mypackages.KeyButtonsPane;

import mypackages.BVariable;

public class ImagesTab extends Tab
{
 public DataModel dataModel;
 public KeyButtonsPane keyButtonsPane;
 public Slider posSlider;
 public HBox posHBox;
 public HBox ctrlHBox;
 public Button prevButton;
 public Button nextButton;
 public Label fileLabel;
 public Label posLabel;
 public ImageView mainImageView;

 public ImagesTab(String tablabel, DataModel datamodel_in)
 {
  super(tablabel);
  dataModel=datamodel_in;

  //create main image and its horizontal sizer
  Image image = new Image("next.png");
  mainImageView=new ImageView();
  mainImageView.setPreserveRatio(true); 
  StackPane stackPane=new StackPane(mainImageView);
  stackPane.setMinWidth(1);
  stackPane.setMinHeight(1);
  stackPane.setMaxWidth(Double.MAX_VALUE);
  stackPane.setMaxHeight(Double.MAX_VALUE);
  mainImageView.fitHeightProperty().bind(stackPane.heightProperty());
  mainImageView.fitWidthProperty().bind(stackPane.widthProperty());

  //create slider and its horizontal sizer
  posSlider=new Slider();
  posSlider.setMaxWidth(Double.MAX_VALUE);
  HBox sliderHBox=new HBox(posSlider);
  sliderHBox.setHgrow(posSlider, Priority.ALWAYS);
  sliderHBox.setSpacing(5);
  sliderHBox.setPadding(new Insets(5, 10, 5, 10));

  //create control buttons buttons and their horizontal sizer
  int imagesize=20;
  String imagespath=System.getProperty("user.dir").replace('\\', '/') + "/images/";
  File prevFile = new File(imagespath+"prev.png");
  File nextFile = new File(imagespath+"next.png");
  Image prevImage = new Image(prevFile.toURI().toString(), imagesize, imagesize, false, false);
  Image nextImage = new Image(nextFile.toURI().toString(), imagesize, imagesize, false, false);
  ImageView prevImageView=new ImageView(prevImage);
  ImageView nextImageView=new ImageView(nextImage);
  prevButton = new Button();
  prevButton.setGraphic(prevImageView);
  nextButton = new Button();
  nextButton.setGraphic(nextImageView);
  fileLabel=new Label("");
  posLabel=new Label("");
  posHBox=new HBox(posLabel);
  posHBox.setSpacing(5);
  posHBox.setPadding(new Insets(5, 10, 5, 10));
  posHBox.setAlignment(Pos.CENTER_RIGHT);
  ctrlHBox=new HBox(prevButton, nextButton, fileLabel, posHBox);
  ctrlHBox.setSpacing(5);
  ctrlHBox.setPadding(new Insets(5, 10, 5, 10));
  ctrlHBox.setHgrow(posHBox, Priority.ALWAYS);
  ctrlHBox.setAlignment(Pos.CENTER_LEFT);

  //create keybuttons pane
  keyButtonsPane=new KeyButtonsPane(dataModel);

  //set main sizer - whole tab
  VBox vBox = new VBox(stackPane, sliderHBox, ctrlHBox, keyButtonsPane);
  vBox.setVgrow(stackPane, Priority.SOMETIMES);

  this.setContent(vBox);
 }


}
