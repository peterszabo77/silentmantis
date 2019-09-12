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
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.layout.Priority;
import javafx.scene.input.KeyEvent; 
import javafx.scene.input.KeyCode; 
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ContentDisplay;
import javafx.scene.paint.Color;

import mypackages.DataModel;
import mypackages.BRecord;

public class RecordsTab extends Tab
{
 public DataModel dataModel;
 public Button deleteButton;
 public Button deleteAllButton;
 public TableView<BRecord> recordsTable;
 public TableColumn<BRecord,Integer> timeColumn;
 public List<TableColumn<BRecord,String>> varColumnList;

 public RecordsTab(String tablabel, DataModel datamodel_in)
 {
  super(tablabel);
  dataModel=datamodel_in;

  //create buttons
  deleteButton = new Button("Delete record");
  deleteAllButton = new Button("Delete all records");

  //create tableview
  recordsTable = new TableView<BRecord>(dataModel.getRecordList());
  recordsTable.setEditable(false);
  recordsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
  recordsTable.setColumnResizePolicy((param) -> true );

  rebuildTable();
  //set sizers - buttons
  HBox buttonHBox = new HBox(deleteButton, deleteAllButton);
  buttonHBox.setAlignment(Pos.CENTER_LEFT);
  buttonHBox.setPadding(new Insets(5, 0, 5, 10));
  buttonHBox.setSpacing(10);
  //set sizers - whole tab
  VBox vBox = new VBox(recordsTable, buttonHBox);
  vBox.setVgrow(recordsTable, Priority.ALWAYS);
  this.setContent(vBox);
 }

 public void rebuildTable()
 {
  //set timeColumn
  timeColumn = new TableColumn<BRecord,Integer>("Time");
  timeColumn.setCellValueFactory(new PropertyValueFactory<BRecord,Integer>("time"));
  timeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
  timeColumn.setEditable(false);
  timeColumn.setSortable(false);

  //set variableColumns
  varColumnList = new ArrayList<TableColumn<BRecord,String>>();
  for (BVariable element : dataModel.getVariableList())
  {
   TableColumn<BRecord,String> valueColumn=new TableColumn<BRecord,String>(element.getName());
   int variableindex=dataModel.getVariableList().indexOf(element);
   valueColumn.textProperty().bind(dataModel.getVariableList().get(variableindex).nameProperty());
   valueColumn.setCellValueFactory(new Callback<CellDataFeatures<BRecord, String>, ObservableValue<String>>() 
   {
    public ObservableValue<String> call(CellDataFeatures<BRecord, String> p) 
    {
	 String varName=p.getTableColumn().getText();
     ObservableMap<String, String> mapList=p.getValue().getMapList();
     String value=mapList.get(varName);
     return (new SimpleStringProperty(value));
    }
   });
   valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
   valueColumn.setOnEditCommit(
    new EventHandler<CellEditEvent<BRecord, String>>()
    {
     @Override
     public void handle(CellEditEvent<BRecord, String> t)
     {
      ObservableMap<String, String> mapList=t.getRowValue().getMapList();
      String varName=t.getTableColumn().getText();
      mapList.put(varName, t.getNewValue());
      t.getTableView().refresh();
     }
    });

   valueColumn.setSortable(false);
   varColumnList.add(valueColumn);
  }

  //add columns to tableview
  recordsTable.getColumns().clear();
  recordsTable.getColumns().add(timeColumn);
  recordsTable.getColumns().addAll(varColumnList);
 } 

}
