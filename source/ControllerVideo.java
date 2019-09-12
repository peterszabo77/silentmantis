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

import mypackages.DataModel;
import mypackages.BKey;
import mypackages.BKeyButton;
import mypackages.BRecord;
import mypackages.VideoTab;

public class ControllerVideo
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public VideoTab videoTab;
 public Button prevButton, nextButton;
 public Slider posSlider;
 public Media media;
 public MediaPlayer mediaPlayer;
 public MediaView mediaView;
 public ChangeListener<Number> timeSliderListener;
 Timeline timeline;

 public ControllerVideo(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;
  videoTab=view.videoTab;
  mediaView=videoTab.mainVideoView;

  videoTab.stopButton.setOnAction(e -> 
  {
   if (mediaPlayer!=null)
   {
	mediaPlayer.stop();
    mediaPlayer.seek(new Duration(0));
    videoTab.timePosLabel.setText(getClockStringFromDuration(new Duration(0)));
    videoTab.msPosLabel.setText(getMsStringFromDuration(new Duration(0)));
    videoTab.posSlider.setValue(0);
   }
  });

  videoTab.playButton.setOnAction(e -> 
  {
   if (mediaPlayer!=null)
   {
    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) mediaPlayer.pause();
    else mediaPlayer.play();
   }
  });
 }

 public String getClockStringFromDuration(Duration duration)
 {
  long seconds = (long) Math.round(duration.toSeconds());
  String stringnumber=String.format("%4d:%02d", (int) seconds / 60, seconds % 60);
  return stringnumber;
 }

 public String getMsStringFromDuration(Duration duration)
 {
  long ms = (long) Math.round(duration.toMillis());
  String stringnumber=String.format("%8d", ms);
  return stringnumber;
 }

 public void LoadMediaFile(File mediaFile)
 {
  if (mediaPlayer!=null) mediaPlayer.dispose();
  mediaView.setMediaPlayer(null);
  media = new Media(mediaFile.toURI().toString());
  mediaPlayer = new MediaPlayer(media);
  mediaView.setMediaPlayer(mediaPlayer);
  mediaPlayer.setAutoPlay(true);
  mediaPlayer.setRate(1.0);

  if (timeline!=null) timeline.stop();
  timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> 
  {
   if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
   {
    videoTab.timePosLabel.setText(getClockStringFromDuration(mediaPlayer.getCurrentTime()));
    videoTab.msPosLabel.setText(getMsStringFromDuration(mediaPlayer.getCurrentTime()));
    videoTab.posSlider.valueProperty().removeListener(timeSliderListener);
    videoTab.posSlider.setValue(mediaPlayer.getCurrentTime().toMillis());
    videoTab.posSlider.valueProperty().addListener(timeSliderListener);
   }
  }));
  timeline.setCycleCount(Animation.INDEFINITE);
  timeline.play();

  mediaPlayer.setOnEndOfMedia(() -> 
  {
  });

  mediaPlayer.setOnReady(new Runnable() 
  {
   public void run()
   {
    videoTab.fileLabel.setText(mediaFile.getName());
    videoTab.timeLengthLabel.setText(" / "+getClockStringFromDuration(mediaPlayer.getMedia().getDuration()));
    videoTab.msLengthLabel.setText(" / "+getMsStringFromDuration(media.getDuration())+" (ms)");
    videoTab.posSlider.setMin(0);
    videoTab.posSlider.setMax(media.getDuration().toMillis());
    videoTab.posSlider.setValue(0);
    videoTab.speedSpinner.getValueFactory().setValue("1x");
   }
  });

  videoTab.speedSpinner.valueProperty().addListener((observable, oldValue, newValue) -> 
  {
   if (mediaPlayer==null) return;
   if (newValue=="0.1x") mediaPlayer.setRate(0.1);
   if (newValue=="0.2x") mediaPlayer.setRate(0.2);
   if (newValue=="0.5x") mediaPlayer.setRate(0.5);
   if (newValue=="1x") mediaPlayer.setRate(1);
   if (newValue=="2x") mediaPlayer.setRate(2);
   if (newValue=="5x") mediaPlayer.setRate(5);
   if (newValue=="10x") mediaPlayer.setRate(10);
  });

  videoTab.volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> 
  {
   if (mediaPlayer!=null) mediaPlayer.setVolume(newValue.doubleValue());
  });

  try {videoTab.posSlider.valueProperty().removeListener(timeSliderListener);} catch(NullPointerException exception) {}
  videoTab.posSlider.valueProperty().addListener(timeSliderListener=new ChangeListener<Number>() 
  {
   public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) 
   {
    if (mediaPlayer!=null)
    {
     mediaPlayer.seek(new Duration(newValue.doubleValue()));
     videoTab.timePosLabel.setText(getClockStringFromDuration(mediaPlayer.getCurrentTime()));
     videoTab.msPosLabel.setText(getMsStringFromDuration(mediaPlayer.getCurrentTime()));
    }
   }
  });

 }

 public void rebuildKeyButtons()
 {
  view.videoTab.keyButtonsPane.rebuildKeyButtons();
  if(view.getScene() != null) view.getScene().getAccelerators().clear();
  for (BKeyButton keyButton : view.videoTab.keyButtonsPane.keyButtonList)
  {
   ObservableMap<String, String> maplist=keyButton.getMapList();
   keyButton.setOnAction((event) -> { addRecord(maplist); });
  }   
  for (BKey bKey : dataModel.getKeyList())
  {
   KeyCombination kc = new KeyCharacterCombination(bKey.getShortcut());
   Runnable task = () -> { addRecord(bKey.getMapList()); };
   view.getScene().getAccelerators().put(kc, task);
  }   
 }

 public void addRecord(ObservableMap<String, String> mapList)
 {
  if (mediaPlayer == null) return;
  if (mediaPlayer.getStatus() == MediaPlayer.Status.DISPOSED) return;
  if (mediaPlayer.getStatus() == MediaPlayer.Status.HALTED) return;
  if (mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) return;
  ObservableMap<String, String> newmaplist=FXCollections.observableHashMap();
  for (Map.Entry<String, String> entry : mapList.entrySet())
  {
   newmaplist.put(entry.getKey(), entry.getValue());
  }
  controllerMain.actionAddRecord(getTime(), newmaplist);
 }

 public int getTime()
 {
  if (mediaPlayer==null) return(-1);
  else return( (int) mediaPlayer.getCurrentTime().toMillis());
 }

}
