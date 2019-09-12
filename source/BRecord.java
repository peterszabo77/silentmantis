package mypackages;

import java.lang.String;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import mypackages.BRecord;

public class BRecord implements Comparable<BRecord>
{
 private IntegerProperty time;
 private ObservableMap<String, String> mapList;

 public BRecord(Integer t, ObservableMap<String, String> ml)
 {
  time=new SimpleIntegerProperty(t);
  mapList=ml;
 }

 public final Integer getTime(){ return time.get();}
 public final void setTime(Integer value){ time.set(value);}
 public IntegerProperty timeProperty() {return time;}

 public final ObservableMap<String, String> getMapList(){ return mapList;}
 
 public int compareTo(BRecord bRecord) 
 {
  int compareQuantity = (int) ((BRecord) bRecord).getTime();
 //ascending order
  return ((int) this.getTime() - compareQuantity);
  //descending order
  //return compareQuantity - this.quantity;
 }
}
