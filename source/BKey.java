package mypackages;

import java.lang.String;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import mypackages.BKInput;

public class BKey
{
 private StringProperty name;
 private BKInput input;
 private StringProperty shortcut;
 private ObservableMap<String, String> mapList;

 public BKey(String n)
 {
  name=new SimpleStringProperty(n);
  input=BKInput.KEYBOARD;
  shortcut=new SimpleStringProperty("");
  mapList = FXCollections.observableHashMap();
 }

 public final String getName(){ return name.get();}
 public final void setName(String value){ name.set(value);}
 public StringProperty nameProperty() {return name;}

 public BKInput getInput() {return input;}
 public void setInput(BKInput inp) {input=inp;}

 public final String getShortcut(){ return shortcut.get();}
 public final void setShortcut(String value){ shortcut.set(value);}
 public StringProperty shortcutProperty() {return shortcut;}
 
 public final ObservableMap<String, String> getMapList(){ return mapList;}
}
