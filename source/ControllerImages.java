import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Slider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.List;
import javafx.collections.ObservableMap;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCharacterCombination;
import javafx.beans.binding.Bindings;
import javafx.scene.text.TextAlignment;
import java.util.Map;
import javafx.scene.Node ;

import mypackages.DataModel;
import mypackages.BKey;
import mypackages.BKeyButton;
import mypackages.BRecord;
import mypackages.ImagesTab;

public class ControllerImages
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public ImagesTab imagesTab;
 public Button prevButton, nextButton;
 public Slider posSlider;

 public ControllerImages(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;
  imagesTab=view.imagesTab;

  prevButton=view.imagesTab.prevButton;
  nextButton=view.imagesTab.nextButton;
  posSlider=view.imagesTab.posSlider;

  prevButton.setOnAction(e -> 
  {
   int length=dataModel.getImageFileList().size();
   int currentIndex=dataModel.getImageIndex();
   if (currentIndex>0) dataModel.setImageIndex(currentIndex-1);
   controllerMain.updateImage();
  });

  nextButton.setOnAction(e -> 
  {
   int length=dataModel.getImageFileList().size();
   int currentIndex=dataModel.getImageIndex();
   if (currentIndex<length-1) dataModel.setImageIndex(currentIndex+1);
   controllerMain.updateImage();
  });

  posSlider.valueProperty().addListener(new ChangeListener<Number>() 
  {
   public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) 
   {
    dataModel.setImageIndex(new_val.intValue()-1);
    controllerMain.updateImage();
   }
  });
 }

 public void rebuildKeyButtons()
 {
  view.imagesTab.keyButtonsPane.rebuildKeyButtonsWithShortcuts();
  for (BKeyButton keyButton : view.imagesTab.keyButtonsPane.keyButtonList)
  {
   ObservableMap<String, String> maplist=keyButton.getMapList();
   keyButton.setOnAction((event) -> { addRecord(maplist); });
  }   
 }

 public void addRecord(ObservableMap<String, String> mapList)
 {
  if (dataModel.getImageFileList().size()==0) return;
  ObservableMap<String, String> newmaplist=FXCollections.observableHashMap();
  for (Map.Entry<String, String> entry : mapList.entrySet())
  {
   newmaplist.put(entry.getKey(), entry.getValue());
  }
  controllerMain.actionAddRecord(getTime(), newmaplist);
 }

 public int getTime()
 {
  return (int)imagesTab.posSlider.getValue();
 }

}
