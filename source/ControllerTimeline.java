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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaPlayer.Status; 
import javafx.util.Duration;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import java.lang.*;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import mypackages.DataModel;
import mypackages.BKey;
import mypackages.BVColor;
import mypackages.BKeyButton;
import mypackages.BRecord;
import mypackages.VideoTab;
import mypackages.TimeLineView;

import mypackages.BVariable;

public class ControllerTimeline
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public TimeLineView timeLineView;
 public Canvas canvas;
 public GraphicsContext gc;
 public ObservableMap<BVColor,Color> colorMapping;

 public ControllerTimeline(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;
  timeLineView=view.timeLineView;
  canvas=timeLineView.canvas;
  gc=timeLineView.gc;

  colorMapping=FXCollections.observableHashMap();
  colorMapping.put(BVColor.BLUE, Color.BLUE);
  colorMapping.put(BVColor.RED, Color.RED);
  colorMapping.put(BVColor.GREEN, Color.GREEN);
  colorMapping.put(BVColor.CYAN, Color.CYAN);
  colorMapping.put(BVColor.ORANGE, Color.ORANGE);
  colorMapping.put(BVColor.MAGENTA, Color.MAGENTA);

  canvas.widthProperty().addListener(observable -> update(true));
  canvas.heightProperty().addListener(observable -> update(true)); 

  timeLineView.updateCheckBox.setOnAction((event) -> 
  {
   boolean selected = timeLineView.updateCheckBox.isSelected();
   if (selected)
   {
    timeLineView.updateButton.setDisable(true);
    update(true);
   }
   else timeLineView.updateButton.setDisable(false);
  });

  timeLineView.updateButton.setOnAction((event) -> 
  {
   update(true);
  });
 }

 public void update(boolean doit)
 {
  if (!doit) return;
  int numOfVars=dataModel.getVariableList().size();
  if (numOfVars==0) { canvas.setVisible(false); return;}
  canvas.setVisible(true);
  int laneWidth=(int) canvas.getWidth();
  int laneHeight=(int)canvas.getHeight()/numOfVars;
  int laneIndex=0;
  int minTime=-1, maxTime=-1;
  if(dataModel.getRecordList().size()>2)
  {
   minTime=dataModel.getRecordList().get(0).getTime();
   maxTime=dataModel.getRecordList().get(dataModel.getRecordList().size()-1).getTime();
  }
  for (BVariable var : dataModel.getVariableList())
  {
   Color paneColor=colorMapping.get(var.getColor());
   gc.setFill(paneColor);
   gc.fillRect(0, laneIndex*laneHeight, laneWidth, laneHeight);
   if(maxTime==minTime) continue;
   for (BRecord rec : dataModel.getRecordList())
   {
    int t=rec.getTime();
    if(rec.getMapList().containsKey(var.getName()))
    {
     gc.setFill(Color.BLACK);
     gc.fillRect((int) canvas.getWidth()*(t-minTime)/(maxTime-minTime)-1, laneIndex*laneHeight, 2, laneHeight);
    }
   }
   laneIndex++;
  }
  System.out.println("Timeline updated");
 }
}
