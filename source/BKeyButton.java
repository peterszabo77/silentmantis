package mypackages;

import javafx.scene.control.Button;
import javafx.collections.ObservableMap;

public class BKeyButton extends Button
{
 private ObservableMap<String, String> mapList;

 public BKeyButton(ObservableMap<String, String> mapList_in)
 {
  super();
  mapList=mapList_in;
 }

 public final ObservableMap<String, String> getMapList(){ return mapList;}
}
