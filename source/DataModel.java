package mypackages;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Map;
import java.io.File;
import java.io.Serializable;

import mypackages.BVariable;
import mypackages.BKey;
import mypackages.BRecord;

public class DataModel
{
 final private ObservableList<BVariable> variableList;
 final private ObservableList<BKey> keyList;
 final private ObservableList<BRecord> recordList;
 final private ObservableList<File> imageFileList;
 private File mediaFile;
 private int imageIndex;
 private File wsFile;

 public DataModel()
 {
  variableList = FXCollections.observableArrayList();
  keyList = FXCollections.observableArrayList();
  recordList = FXCollections.observableArrayList();
  imageFileList = FXCollections.observableArrayList();
  imageIndex=-1;
 }

 public ObservableList<BVariable> getVariableList() { return variableList;}
 public ObservableList<BKey> getKeyList() { return keyList;}
 public ObservableList<BRecord> getRecordList() { return recordList;}
 public ObservableList<File> getImageFileList() { return imageFileList;}
 public void setImageFileList(ObservableList<File> value) { imageFileList.setAll(value);}
 public void setMediaFile(File value) { mediaFile=value;}
 public File getMediaFile() { return mediaFile;}
 public void setWsFile(File f) {wsFile=f;}
 public File getWsFile() {return wsFile;}

 public ObservableList<String> getShortcuts() 
 {
  ObservableList<String> templist=FXCollections.observableArrayList();
  for (BKey bKey : keyList)
  {
   templist.addAll(bKey.getShortcut());
  }
  return templist;
 }

 public void addVariable(BVariable v)
 {
  variableList.addAll(v);
 }

 public void delVariable(BVariable v)
 {
  variableList.removeAll(v);
 }

 public void addKey(BKey v)
 {
  keyList.addAll(v);
 }

 public void delKey(BKey v)
 {
  keyList.removeAll(v);
 }

 public void addRecord(BRecord r)
 {
  recordList.addAll(r);
 }

 public void delRecord(BRecord r)
 {
  recordList.removeAll(r);
 }

 public void delAllRecords()
 {
  recordList.clear();
 }

 public void addImageFile(File f)
 {
  imageFileList.addAll(f);
 }

 public void delAllImageFiles()
 {
  imageFileList.clear();
 }

 public int getImageIndex()
 {
  return imageIndex;
 }

 public void setImageIndex(int i)
 {
  imageIndex=i;
 }

 public void deleteMaps(String oldVarName)
 {
  //delete maps from keys and records
  for (BKey bKey : keyList)
  {
   bKey.getMapList().remove(oldVarName);
  }
  for (BRecord bRecord : recordList)
  {
   bRecord.getMapList().remove(oldVarName);
  }
 }

 public void renameMaps(String oldVarName, String newVarName)
 {
  //rename maps in keys and records
  for (BKey bKey : keyList)
  {
   String value=bKey.getMapList().get(oldVarName);
   bKey.getMapList().remove(oldVarName);
   if (value!=null) bKey.getMapList().put(newVarName, value);
  }
  for (BRecord bRecord : recordList)
  {
   String value=bRecord.getMapList().get(oldVarName);
   bRecord.getMapList().remove(oldVarName);
   if (value!=null) bRecord.getMapList().put(newVarName, value);
  }
 }

 public void clear()
 {
  variableList.clear();
  keyList.clear();
  recordList.clear();
  imageFileList.clear();
  mediaFile=null;
  imageIndex=-1;
  wsFile=null;
 }

}
