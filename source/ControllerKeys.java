import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleStringProperty;

import mypackages.DataModel;
import mypackages.BKey;

public class ControllerKeys
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public Button upButton, downButton, newButton, deleteButton;
 
 public ControllerKeys(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;

  upButton=view.keysTab.upButton;
  downButton=view.keysTab.downButton;
  newButton=view.keysTab.newButton;
  deleteButton=view.keysTab.deleteButton;

  newButton.setOnAction(e -> 
  {
   ArrayList<String> nameList=new ArrayList<String>();
   for (BKey element : dataModel.getKeyList()) nameList.add(element.getName());
   int i=1;
   while (nameList.contains("key"+i)) i++;
   String newName="key"+i;
   controllerMain.actionAddKey(new BKey(newName));
  });

  deleteButton.setOnAction(e -> 
  {
   BKey v=view.keysTab.keysTable.getSelectionModel().getSelectedItem();
   if (v !=null) {controllerMain.actionDelKey(v);}
  });

  upButton.setOnAction(e -> 
  {
   int i=view.keysTab.keysTable.getSelectionModel().getSelectedIndex();
   BKey v=view.keysTab.keysTable.getSelectionModel().getSelectedItem();
   if (i==0 || v==null) return;
   controllerMain.actionUpKey(i,v);
  });

  downButton.setOnAction(e -> 
  {
   int i=view.keysTab.keysTable.getSelectionModel().getSelectedIndex();
   BKey v=view.keysTab.keysTable.getSelectionModel().getSelectedItem();
   if (i==dataModel.getKeyList().size()-1 || v==null) return;
   controllerMain.actionDownKey(i,v);
  });

  attachKeyEventHandlers();
  

 }

 public void attachKeyEventHandlers()
 {
  view.keysTab.nameColumn.setOnEditCommit(new EventHandler<CellEditEvent<BKey, String>>()
  {
   @Override
   public void handle(CellEditEvent<BKey, String> t)
   {
    ObservableList<String> nameList=FXCollections.observableArrayList();
    for (BKey element : dataModel.getKeyList())
    {
     nameList.addAll(element.getName());
    }
    if (!nameList.contains(t.getNewValue()) && t.getNewValue().length()>0)
    {
     String oldName=t.getOldValue();
     String newName=t.getNewValue();
     BKey v=((BKey) t.getTableView().getItems().get(t.getTablePosition().getRow()));
     v.setName(newName);
     controllerMain.actionRenameKey(v, oldName, newName);
    }
   }
  });

  view.keysTab.shortcutColumn.setOnEditCommit(new EventHandler<CellEditEvent<BKey, String>>()
  {
   @Override
   public void handle(CellEditEvent<BKey, String> t)
   {
    ObservableList<String> shortcutList=FXCollections.observableArrayList();
    for (BKey element : dataModel.getKeyList())
    {
     shortcutList.addAll(element.getShortcut());
    }
    String newShortcut=t.getNewValue().toLowerCase().substring(0,1);
    if (!shortcutList.contains(newShortcut) && t.getNewValue().length()>0)
    {
     String oldShortcut=t.getOldValue();
     BKey v=((BKey) t.getTableView().getItems().get(t.getTablePosition().getRow()));
     v.setShortcut(newShortcut);
     controllerMain.actionAddShortcutToKey(v, oldShortcut, newShortcut);
    }
   }
  });

  for (TableColumn<BKey,String> valueColumn : view.keysTab.varColumnList)
  {
   valueColumn.setCellValueFactory(new Callback<CellDataFeatures<BKey, String>, ObservableValue<String>>() 
   {
    public ObservableValue<String> call(CellDataFeatures<BKey, String> p) 
    {
	 String varName=p.getTableColumn().getText();
     ObservableMap<String, String> mapList=p.getValue().getMapList();
     String value=mapList.get(varName);
     return (new SimpleStringProperty(value));
    }
   });
   valueColumn.setOnEditCommit(new EventHandler<CellEditEvent<BKey, String>>()
   {
    @Override
    public void handle(CellEditEvent<BKey, String> t)
    {
     ObservableMap<String, String> mapList=t.getRowValue().getMapList();
     String varName=t.getTableColumn().getText();
     if (t.getNewValue().length()>0) mapList.put(varName, t.getNewValue());
     else mapList.remove(varName);
    t.getTableView().refresh();
    }
   });
  }
  view.keysTab.keysTable.refresh();

 }

}
