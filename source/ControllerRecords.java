import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import javafx.scene.control.TableColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

import mypackages.DataModel;
import mypackages.BRecord;

public class ControllerRecords
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public Button deleteButton;
 public Button deleteAllButton;
 
 public ControllerRecords(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;

  deleteButton=view.recordsTab.deleteButton;
  deleteAllButton=view.recordsTab.deleteAllButton;

  deleteButton.setOnAction(e -> 
  {
   BRecord r=view.recordsTab.recordsTable.getSelectionModel().getSelectedItem();
   if (r !=null) {controllerMain.actionDelRecord(r);}
  });

  deleteAllButton.setOnAction(e -> 
  {
   controllerMain.actionDelAllRecords();
  });

  view.recordsTab.setOnSelectionChanged(e -> 
  {
   if(view.recordsTab.isSelected())
   {
	System.out.println("Records tab selected - sorting records...");
	//Collections.sort(dataModel.getRecordList());
   }
  });

 }

}
