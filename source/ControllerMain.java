import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.util.List;
import javafx.scene.image.Image;
import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import javafx.collections.ObservableMap;
import java.io.PrintWriter;
import java.util.Map;
import java.util.function.Predicate;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.regex.Pattern;
import java.util.Collections;

import mypackages.DataModel;
import mypackages.BVariable;
import mypackages.BKey;
import mypackages.BRecord;
import mypackages.ExportParameters;
import mypackages.BVScale;
import mypackages.BVType;
import mypackages.BVColor;
import mypackages.BKInput;

public class ControllerMain
{
 public DataModel dataModel;
 public View view;
 public ControllerVideo controllerVideo;
 public ControllerImages controllerImages;
 public ControllerVariables controllerVariables;
 public ControllerKeys controllerKeys;
 public ControllerRecords controllerRecords;
 public ControllerMenu controllerMenu;
 public ControllerTimeline controllerTimeline;
 
 public ControllerMain()
 {
  dataModel=new DataModel();
  view=new View(dataModel);
  controllerVideo=new ControllerVideo(this, dataModel, view);
  controllerImages=new ControllerImages(this, dataModel, view);
  controllerVariables=new ControllerVariables(this, dataModel, view);
  controllerKeys=new ControllerKeys(this, dataModel, view);
  controllerRecords=new ControllerRecords(this, dataModel, view);
  controllerMenu=new ControllerMenu(this, dataModel, view);
  controllerTimeline=new ControllerTimeline(this, dataModel, view);
 }

 //CLEAR WORKSPACE
 public void actionClearWorkspace()
 {
  dataModel.clear();
  if (controllerVideo.mediaPlayer!=null) controllerVideo.mediaPlayer.dispose();
  updateImage();
  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();

  System.out.println("workspace cleared");
 }

 //OPEN WORKSPACE
 public void actionOpenWorkspace(File file) throws IOException
 {
  BufferedReader inp=null;
  String lineStr;
  BVariable tempVar=null;
  BKey tempKey=null;
  ObservableList<File> tempImageFiles=FXCollections.observableArrayList();
  ObservableMap<String, String> tmapList=FXCollections.observableHashMap();

  if (controllerVideo.mediaPlayer!=null) controllerVideo.mediaPlayer.dispose();
  try
  {
   inp = new BufferedReader(new FileReader(file));
   dataModel.clear();
   while ((lineStr = inp.readLine()) != null) 
   {
    String[] lineparts=lineStr.split(Pattern.quote("|<"));
    switch (lineparts[0])
    {
     case "VARIABLE":
      tempVar=new BVariable(lineparts[1]);
      tempVar.setScale(BVScale.valueOf(lineparts[2]));
      tempVar.setType(BVType.valueOf(lineparts[3]));
      tempVar.setColor(BVColor.valueOf(lineparts[4]));
      actionAddVariable(tempVar);
      break; 
     case "KEY":
      tempKey=new BKey(lineparts[1]);
      tempKey.setInput(BKInput.valueOf(lineparts[2]));
      tempKey.setShortcut(lineparts[3]);
      if(lineparts.length>4)
      {
       String[] keyparts=lineparts[4].split(Pattern.quote("*<"));       
       for(int i=0; i<=keyparts.length-1; i++)
       {
		String tKey=keyparts[i].split(":")[0];
		String tValue=keyparts[i].split(":")[1];
		tempKey.getMapList().put(tKey, tValue);
	   }
	  }
      actionAddKey(tempKey);
      break; 
     case "RECORD":
      int time=Integer.parseInt(lineparts[1]);
      tmapList.clear();
      if(lineparts.length>2)
      {
       String[] keyparts=lineparts[2].split(Pattern.quote("*<"));       
       for(int i=0; i<=keyparts.length-1; i++)
       {
		String tKey=keyparts[i].split(":")[0];
		String tValue=keyparts[i].split(":")[1];
		tmapList.put(tKey, tValue);
	   }
	  }
      dataModel.addRecord(new BRecord(time, tmapList));
      break; 
     case "IMAGE":
      System.out.println(lineparts[1]);
      tempImageFiles.add(new File(lineparts[1]));
      break; 
     case "MEDIA":
      importVideo(new File(lineparts[1]));
      break; 
     case "WSFILE":
      dataModel.setWsFile(new File(lineparts[1]));
      break; 
     case "IMAGEINDEX":
      dataModel.setImageIndex(Integer.parseInt(lineparts[1]));
      System.out.println("IMAGEINDEX set to "+dataModel.getImageIndex());
      break; 
    }
   }
   importImages(tempImageFiles, dataModel.getImageIndex());
  } 
  catch(IOException exception)
  {
   exception.printStackTrace();
  }
  finally 
  {
   if (inp != null) { inp.close(); }
  }

  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();

  System.out.println("workspace opened");
 }

 //SAVE WORKSPACE
 public void actionSaveWorkspace(File file) throws IOException
 {
  String lineStr;
  String fieldSep="|<";
  String mapSep="*<";
  PrintWriter out=null;

  try
  {
   out = new PrintWriter(file);
   for (BVariable variable : dataModel.getVariableList())
   {
	lineStr="VARIABLE"+fieldSep+variable.getName()+fieldSep+variable.getScale()+fieldSep+variable.getType()+fieldSep+variable.getColor();
    out.println(lineStr);
   }
   for (BKey key : dataModel.getKeyList())
   {
	lineStr="KEY"+fieldSep+key.getName()+fieldSep+key.getInput()+fieldSep+key.getShortcut()+fieldSep;
    for (Map.Entry<String, String> entry : key.getMapList().entrySet())
    {
	 lineStr+=entry.getKey()+":"+entry.getValue()+mapSep;
	}
    out.println(lineStr);
   }
   for (BRecord record : dataModel.getRecordList())
   {
	lineStr="RECORD"+fieldSep+record.getTime()+fieldSep;
    for (Map.Entry<String, String> entry : record.getMapList().entrySet())
    {
	 lineStr+=entry.getKey()+":"+entry.getValue()+mapSep;
	}
    out.println(lineStr);
   }
   for (File f : dataModel.getImageFileList())
   {
	lineStr="IMAGE"+fieldSep+f.getPath();
    out.println(lineStr);
   }
   if (dataModel.getMediaFile()!=null) out.println("MEDIA"+fieldSep+dataModel.getMediaFile().getPath());
   if (dataModel.getWsFile()!=null) out.println("WSFILE"+fieldSep+dataModel.getWsFile().getPath());
   out.println("IMAGEINDEX"+fieldSep+dataModel.getImageIndex());
  }
  catch(IOException exception)
  {
   exception.printStackTrace();
  }
  finally 
  {
   if (out != null) { out.close(); }
  }
  System.out.println("workspace saved");
 }

 ////////////////// VIDEO
 public void importVideo(File mediaFile)
 {
  view.tabPane.getSelectionModel().select(view.videoTab);  
  dataModel.setMediaFile(mediaFile);
  controllerVideo.LoadMediaFile(mediaFile);
  System.out.println("Import Media");
 }

 ////////////////// IMAGES
 public void importImages(List<File> imageFiles, int imageIndex)
 {
  if (imageFiles.size()==0) return;
  view.tabPane.getSelectionModel().select(view.imagesTab);  
  dataModel.delAllImageFiles();
  for (File file : imageFiles) dataModel.addImageFile(file);
  dataModel.setImageFileList(dataModel.getImageFileList().sorted());
  dataModel.setImageIndex(imageIndex);
  view.imagesTab.posSlider.setMin(1);
  view.imagesTab.posSlider.setMax(imageFiles.size());
  updateImage();
  System.out.println("Import Images");
 }

 public void updateImage() 
 {
  int imageIndex=dataModel.getImageIndex();
  if (imageIndex==-1)
  { 
   view.imagesTab.mainImageView.setImage(null);
   view.imagesTab.fileLabel.setText("");
   view.imagesTab.posLabel.setText("");
   view.imagesTab.posSlider.setMin(1);
   view.imagesTab.posSlider.setMax(1);
   view.imagesTab.posSlider.setValue(1);
   return;
  }
  try
  {
   File imageFile=dataModel.getImageFileList().get(imageIndex);
   String lengthString=Integer.toString(dataModel.getImageFileList().size());
   String positionString=Integer.toString(imageIndex+1);
   String fileName=imageFile.getName();
   Image image = new Image(new FileInputStream(imageFile.getAbsoluteFile())); 
   view.imagesTab.mainImageView.setImage(image);
   view.imagesTab.fileLabel.setText(fileName);
   view.imagesTab.posLabel.setText(positionString+" / "+lengthString);
   view.imagesTab.posSlider.setValue(imageIndex+1);
  }
  catch(FileNotFoundException exception)
  {
  }
  System.out.println("Update Displayed Image");
 }

 ////////////////// VARIABLESTAB
 
 public void actionAddVariable(BVariable v)
 {
  dataModel.addVariable(v);
  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("New variable");
 }

 public void actionDelVariable(BVariable v)
 {
  dataModel.delVariable(v);
  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  dataModel.deleteMaps(v.getName()); //delete maps in Keys and Records
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Variable deleted");
 }

 public void actionUpVariable(int i, BVariable v)
 {
  dataModel.getVariableList().add(i-1, v);
  dataModel.getVariableList().remove(i+1);
  view.variablesTab.variableTable.getSelectionModel().select(i-1);
  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Variable moved up");
 }

 public void actionDownVariable(int i, BVariable v)
 {
  dataModel.getVariableList().add(i+2, v);
  dataModel.getVariableList().remove(i);
  view.variablesTab.variableTable.getSelectionModel().select(i+1);
  view.variablesTab.variableTable.refresh();
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  view.recordsTab.rebuildTable();
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Variable moved down");
 }

 public void actionRenameVariable(BVariable v, String on, String nn)
 {
  dataModel.renameMaps(on, nn); //rename maps in Keys and Records
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Variable renamed from "+on+" to "+nn);
 }

 ////////////////// KEYSTAB
 public void actionAddKey(BKey v)
 {
  dataModel.addKey(v);
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  controllerImages.rebuildKeyButtons();
  controllerVideo.rebuildKeyButtons();
  System.out.println("New key");
 }

 public void actionDelKey(BKey v)
 {
  dataModel.delKey(v);
  view.keysTab.rebuildTable();
  controllerKeys.attachKeyEventHandlers();
  System.out.println("Key deleted");
  controllerImages.rebuildKeyButtons();
  controllerVideo.rebuildKeyButtons();
 }

 public void actionUpKey(int i, BKey v)
 {
  dataModel.getKeyList().add(i-1, v);
  dataModel.getKeyList().remove(i+1);
  view.keysTab.keysTable.getSelectionModel().select(i-1);
  view.keysTab.keysTable.refresh();
  controllerImages.rebuildKeyButtons();
  controllerVideo.rebuildKeyButtons();
  System.out.println("Key moved up");
 }

 public void actionDownKey(int i, BKey v)
 {
  dataModel.getKeyList().add(i+2, v);
  dataModel.getKeyList().remove(i);
  view.keysTab.keysTable.getSelectionModel().select(i+1);
  view.keysTab.keysTable.refresh();
  controllerImages.rebuildKeyButtons();
  controllerVideo.rebuildKeyButtons();
  System.out.println("Key moved down");
 }

 public void actionRenameKey(BKey v, String on, String nn)
 {
  System.out.println("Key renamed from "+on+" to "+nn);
 }

 public void actionAddShortcutToKey(BKey v, String os, String ns)
 {
  controllerImages.rebuildKeyButtons();
  controllerVideo.rebuildKeyButtons();
  System.out.println("Key shortcut changed from "+os+" to "+ns);
 }

 ////////////////// RECORDSTAB
 public void actionDelRecord(BRecord r)
 {
  dataModel.delRecord(r);
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Record deleted");
 }

 public void actionDelAllRecords()
 {
  dataModel.delAllRecords();
  view.recordsTab.recordsTable.refresh();
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("All records deleted");
 }

 public void actionAddRecord(int time, ObservableMap<String, String> maplist)
 {
  if (!view.videoTab.isSelected() && !view.imagesTab.isSelected()) return;
  if (maplist.size()>0) dataModel.addRecord(new BRecord(time, maplist));
  Collections.sort(dataModel.getRecordList());
  controllerTimeline.update(view.timeLineView.updateCheckBox.isSelected());
  System.out.println("Record added"+maplist);
 }

 public void actionExportRecords(File file, ExportParameters ep)
 {
  try(PrintWriter out = new PrintWriter(file))
  {
   String sep=ep.getSeparator();
   if(ep.getFormat()=="long")
   {
    String expValue;
    out.println("TIME"+ep.getSeparator()+"VARIABLE"+ep.getSeparator()+"VALUE");
    for (BRecord record : dataModel.getRecordList())
    {
     for (Map.Entry<String, String> entry : record.getMapList().entrySet())
     {
	  String timeStr=Integer.toString(record.getTime());
	  String varName=entry.getKey();
	  BVariable variable=getVariableByName(varName);
	  if (variable.getScale()==BVScale.FACTOR)
	  {
	   expValue=entry.getValue();
	   if(ep.getEnclosing()=="single") expValue="'"+expValue+"'";
	   if(ep.getEnclosing()=="double") expValue="\""+expValue+"\"";
	  }
	  else
	  {
       expValue=entry.getValue();
	  }
      out.println(timeStr+sep+varName+sep+expValue);
     }
    }
   }
   else
   {
	String headerStr="TIME";
	String lineStr="";
	String expValue="";
	ObservableMap<String, String> mapList;
    for (BVariable variable : dataModel.getVariableList())
    {
	 headerStr=headerStr+sep+variable.getName();
    }
    out.println(headerStr);
    for (BRecord record : dataModel.getRecordList())
    {
	 mapList=record.getMapList();
	 lineStr=Integer.toString(record.getTime());
     for (BVariable variable : dataModel.getVariableList())
     {
      if(mapList.containsKey(variable.getName()))
      {
	   expValue=mapList.get(variable.getName());
	  }
	  else
	  {
       if(ep.getNAreplace()) expValue="NA"; else expValue="";
	  }
      System.out.println(variable.getScale());
      if(variable.getScale()==BVScale.FACTOR && ep.getEnclosing()=="single" && expValue.length()>0) expValue="'"+expValue+"'";
      if(variable.getScale()==BVScale.FACTOR && ep.getEnclosing()=="double" && expValue.length()>0) expValue="\""+expValue+"\"";
      lineStr=lineStr+sep+expValue;
     }
     out.println(lineStr);
    }
   }
  }
  catch(FileNotFoundException exception)
  {
  }
 }

 public BVariable getVariableByName(String name) 
 {
  Predicate<BVariable> predicate = c-> c.getName().equals(name);
  BVariable  variable = dataModel.getVariableList().stream().filter(predicate).findFirst().get();
  return variable;
 }
}
