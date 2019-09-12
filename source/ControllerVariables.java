import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import mypackages.DataModel;
import mypackages.BVariable;
import mypackages.BVColor;

public class ControllerVariables
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public Button upButton, downButton, newButton, deleteButton;
 
 public ControllerVariables(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;

  upButton=view.variablesTab.upButton;
  downButton=view.variablesTab.downButton;
  newButton=view.variablesTab.newButton;
  deleteButton=view.variablesTab.deleteButton;

  newButton.setOnAction(e -> 
  {
   ArrayList<String> nameList=new ArrayList<String>();
   for (BVariable element : dataModel.getVariableList()) nameList.add(element.getName());
   int i=1;
   while (nameList.contains("variable"+i)) i++;
   String newName="variable"+i;
   controllerMain.actionAddVariable(new BVariable(newName));
  });

  deleteButton.setOnAction(e -> 
  {
   BVariable v=view.variablesTab.variableTable.getSelectionModel().getSelectedItem();
   if (v !=null) {controllerMain.actionDelVariable(v);}
  });

  upButton.setOnAction(e -> 
  {
   int i=view.variablesTab.variableTable.getSelectionModel().getSelectedIndex();
   BVariable v=view.variablesTab.variableTable.getSelectionModel().getSelectedItem();
   if (i==0 || v==null) return;
   controllerMain.actionUpVariable(i,v);
  });

  downButton.setOnAction(e -> 
  {
   int i=view.variablesTab.variableTable.getSelectionModel().getSelectedIndex();
   BVariable v=view.variablesTab.variableTable.getSelectionModel().getSelectedItem();
   if (i==dataModel.getVariableList().size()-1 || v==null) return;
   controllerMain.actionDownVariable(i,v);
  });

  view.variablesTab.nameColumn.setOnEditCommit(
   new EventHandler<CellEditEvent<BVariable, String>>()
   {
    @Override
    public void handle(CellEditEvent<BVariable, String> t)
    {
     ObservableList<String> nameList=FXCollections.observableArrayList();
     for (BVariable element : dataModel.getVariableList())
     {
      nameList.addAll(element.getName());
     }
     if (!nameList.contains(t.getNewValue()) && t.getNewValue().length()>0)
     {
      String oldName=t.getOldValue();
      String newName=t.getNewValue();
      BVariable v=((BVariable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
      v.setName(newName);
      controllerMain.actionRenameVariable(v, oldName, newName);
     }
     view.variablesTab.variableTable.refresh();
    }
  });

  view.variablesTab.colorColumn.setOnEditCommit(new EventHandler<CellEditEvent<BVariable, BVColor>>()
  {
   @Override
   public void handle(CellEditEvent<BVariable, BVColor> t)
   {
    BVariable v=((BVariable) t.getTableView().getItems().get(t.getTablePosition().getRow()));
	v.setColor(t.getNewValue());
    controllerMain.controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
   }
  });

 }
}
