package mypackages;

import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ObservableMapValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import java.io.File;
import javafx.util.Callback;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javafx.collections.ListChangeListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.binding.Bindings;
import javafx.scene.text.TextAlignment;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Tab;

import mypackages.DataModel;

public class KeyButtonsPane extends TilePane
{
 public DataModel dataModel;
 public List<BKeyButton> keyButtonList;
 
 public KeyButtonsPane(DataModel datamodel_in)
 {
  super();

  dataModel=datamodel_in;
  keyButtonList = new ArrayList<BKeyButton>();


  setPadding(new Insets(5, 0, 5, 10));
  setHgap(5);
  setVgap(5);

  rebuildKeyButtons();
 }

 public void rebuildKeyButtons()
 {
  keyButtonList.clear();
  for (BKey bKey : dataModel.getKeyList())
  {
   BKeyButton keyButton = new BKeyButton(bKey.getMapList());
   if(bKey.getShortcut().length()>0)
   {
    keyButton.textProperty().bind(Bindings.concat(bKey.nameProperty(), " \n", "[",bKey.shortcutProperty(),"]"));
   }
   else
   {
    keyButton.textProperty().bind(Bindings.concat(bKey.nameProperty(), " \n"));
   }
   keyButton.setTextAlignment(TextAlignment.CENTER);
   keyButtonList.add(keyButton);
  }
  getChildren().setAll(keyButtonList);
 }

 public void rebuildKeyButtonsWithShortcuts()
 {
  keyButtonList.clear();
  for (BKey bKey : dataModel.getKeyList())
  {
   BKeyButton keyButton = new BKeyButton(bKey.getMapList());
   keyButton.textProperty().bind(Bindings.concat(bKey.nameProperty(), " \n"));
   keyButton.setTextAlignment(TextAlignment.CENTER);
   keyButtonList.add(keyButton);
  }
  getChildren().setAll(keyButtonList);
 }


}
