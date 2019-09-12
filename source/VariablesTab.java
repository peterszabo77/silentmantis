package mypackages;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import javafx.scene.paint.Color;

import mypackages.DataModel;
import mypackages.BVScale;
import mypackages.BVType;
import mypackages.BVColor;

public class VariablesTab extends Tab
{
 public DataModel dataModel;
 public Button upButton;
 public Button downButton;
 public Button newButton;
 public Button deleteButton;
 public TableView<BVariable> variableTable;
 public TableColumn<BVariable,String> nameColumn;
 public TableColumn<BVariable,BVColor> colorColumn;

 public VariablesTab(String tablabel, DataModel datamodel_in)
 {
  super(tablabel);
  dataModel=datamodel_in;

  //buttonimages
  String imagespath=System.getProperty("user.dir").replace('\\', '/') + "/images/";
  File upFile = new File(imagespath+"arrowup.png");
  File downFile = new File(imagespath+"arrowdown.png");
  int imagesize=20;

  Image upImage = new Image(upFile.toURI().toString(), imagesize, imagesize, false, false);
  Image downImage = new Image(downFile.toURI().toString(), imagesize, imagesize, false, false);
  ImageView upImageView=new ImageView(upImage);
  ImageView downImageView=new ImageView(downImage);

  //create buttons
  upButton = new Button(""); upButton.setGraphic(upImageView);
  downButton = new Button(""); downButton.setGraphic(downImageView);
  newButton = new Button("Add variable");
  deleteButton = new Button("Delete variable");

  //create tableview
  variableTable = new TableView<BVariable>(dataModel.getVariableList());
  variableTable.setEditable(true);
  variableTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  variableTable.setColumnResizePolicy((param) -> true );

  //set nameColumn
  nameColumn = new TableColumn<BVariable,String>("Name");
  nameColumn.setCellValueFactory(new PropertyValueFactory<BVariable,String>("name"));
  nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
  nameColumn.setEditable(true);
  nameColumn.setSortable(false);

  //set scaleColumn
  TableColumn<BVariable,BVScale> scaleColumn = new TableColumn<BVariable,BVScale>("Scale");
  scaleColumn.setCellValueFactory(new PropertyValueFactory<BVariable,BVScale>("scale"));
  ObservableList<BVScale> typeList1=FXCollections.observableArrayList(BVScale.values());
  scaleColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(typeList1));
  scaleColumn.setEditable(true);
  scaleColumn.setSortable(false);
  scaleColumn.setOnEditCommit(new EventHandler<CellEditEvent<BVariable, BVScale>>()
  {
   @Override
   public void handle(CellEditEvent<BVariable, BVScale> t)
   {
    BVariable v=((BVariable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
	v.setScale(t.getNewValue());
   }
  });

  //set typeColumn
  TableColumn<BVariable,BVType> typeColumn = new TableColumn<BVariable,BVType>("Type");
  typeColumn.setCellValueFactory(new PropertyValueFactory<BVariable,BVType>("type"));
  ObservableList<BVType> typeList2=FXCollections.observableArrayList(BVType.values());
  typeColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(typeList2));
  typeColumn.setEditable(true);
  typeColumn.setSortable(false);
  typeColumn.setOnEditCommit(new EventHandler<CellEditEvent<BVariable, BVType>>()
  {
   @Override
   public void handle(CellEditEvent<BVariable, BVType> t)
   {
    BVariable v=((BVariable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
	v.setType(t.getNewValue());
   }
  });

  //set colorColumn
  colorColumn = new TableColumn<BVariable,BVColor>("Color");
  colorColumn.setCellValueFactory(new PropertyValueFactory<BVariable,BVColor>("color"));
  ObservableList<BVColor> typeList3=FXCollections.observableArrayList(BVColor.values());
  colorColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(typeList3));
  colorColumn.setEditable(true);
  colorColumn.setSortable(false);

  //add columns to tableview
  variableTable.getColumns().add(nameColumn);
  variableTable.getColumns().add(scaleColumn);
  variableTable.getColumns().add(typeColumn);
  variableTable.getColumns().add(colorColumn);

  //set sizers - buttons
  HBox buttonHBox = new HBox(upButton, downButton, newButton, deleteButton);
  buttonHBox.setAlignment(Pos.CENTER_LEFT);
  buttonHBox.setPadding(new Insets(5, 0, 5, 10));
  buttonHBox.setSpacing(10);
  //set sizers - whole tab
  VBox vBox = new VBox(variableTable, buttonHBox);
  vBox.setVgrow(variableTable, Priority.ALWAYS);
  this.setContent(vBox);

 }
}
