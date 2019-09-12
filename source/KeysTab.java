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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;
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
import mypackages.BKey;
import mypackages.BKInput;

public class KeysTab extends Tab
{
 public DataModel dataModel;
 public Button upButton;
 public Button downButton;
 public Button newButton;
 public Button deleteButton;
 public TableView<BKey> keysTable;
 public TableColumn<BKey,String> nameColumn;
 public TableColumn<BKey,BKInput> inputColumn;
 public TableColumn<BKey,String> shortcutColumn;
 public List<TableColumn<BKey,String>> varColumnList;

 public KeysTab(String tablabel, DataModel datamodel_in)
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
  newButton = new Button("Add key");
  deleteButton = new Button("Delete key");

  //create tableview
  keysTable = new TableView<BKey>(dataModel.getKeyList());
  keysTable.setEditable(true);
  keysTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  keysTable.setColumnResizePolicy((param) -> true );

  rebuildTable();
  //set sizers - buttons
  HBox buttonHBox = new HBox(upButton, downButton, newButton, deleteButton);
  buttonHBox.setAlignment(Pos.CENTER_LEFT);
  buttonHBox.setPadding(new Insets(5, 0, 5, 10));
  buttonHBox.setSpacing(10);
  //set sizers - whole tab
  VBox vBox = new VBox(keysTable, buttonHBox);
  vBox.setVgrow(keysTable, Priority.ALWAYS);
  this.setContent(vBox);
 }

 public void rebuildTable()
 {
  //set nameColumn
  nameColumn = new TableColumn<BKey,String>("Name");
  nameColumn.setCellValueFactory(new PropertyValueFactory<BKey,String>("name"));
  nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
  nameColumn.setEditable(true);
  nameColumn.setSortable(false);

  //set inputColumn
  TableColumn<BKey,BKInput> inputColumn = new TableColumn<BKey,BKInput>("Input");
  inputColumn.setCellValueFactory(new PropertyValueFactory<BKey,BKInput>("input"));
  ObservableList<BKInput> typeList1=FXCollections.observableArrayList(BKInput.values());
  inputColumn.setCellFactory(ChoiceBoxTableCell.forTableColumn(typeList1));
  inputColumn.setEditable(true);
  inputColumn.setSortable(false);

  //set shortcutColumn
  shortcutColumn = new TableColumn<BKey,String>("Shortcut");
  shortcutColumn.setCellValueFactory(new PropertyValueFactory<BKey,String>("shortcut"));
  shortcutColumn.setCellFactory(TextFieldTableCell.forTableColumn());
  shortcutColumn.setEditable(true);
  shortcutColumn.setSortable(false);
  shortcutColumn.setOnEditCommit(
   new EventHandler<CellEditEvent<BKey, String>>()
   {
    @Override
    public void handle(CellEditEvent<BKey, String> t)
    {
	 String newvalue=t.getNewValue().toLowerCase().substring(0,1);
     if (!dataModel.getShortcuts().contains(newvalue))
     {
      ((BKey) t.getTableView().getItems().get(t.getTablePosition().getRow())).setShortcut(newvalue);
     }
     t.getTableView().refresh();
     //videoTab.keyButtonsPane.rebuildKeyButtons();
     //imagesTab.keyButtonsPane.rebuildKeyButtons();
    }
   });

  //set variableColumns
  varColumnList = new ArrayList<TableColumn<BKey,String>>();
  for (BVariable element : dataModel.getVariableList())
  {
   TableColumn<BKey,String> valueColumn=new TableColumn<BKey,String>(element.getName());
   int variableindex=dataModel.getVariableList().indexOf(element);
   valueColumn.textProperty().bind(dataModel.getVariableList().get(variableindex).nameProperty());
   valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
   valueColumn.setSortable(false);
   varColumnList.add(valueColumn);
  }

  //add columns to tableview
  keysTable.getColumns().clear();
  keysTable.getColumns().add(nameColumn);
  keysTable.getColumns().add(inputColumn);
  keysTable.getColumns().add(shortcutColumn);
  keysTable.getColumns().addAll(varColumnList);
  keysTable.refresh();
 } 

}
