import javafx.scene.control.Button;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;
import javafx.collections.ObservableMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Dialog;
import javafx.util.Pair;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.CheckBox;
import java.util.Optional;
import java.io.IOException;

import mypackages.DataModel;
import mypackages.MenuBarView;
import mypackages.ExportDialog;
import mypackages.ExportParameters;

public class ControllerMenu
{
 public ControllerMain controllerMain;
 public DataModel dataModel;
 public View view;
 public MenuBarView menuBarView;

 public ControllerMenu(ControllerMain c, DataModel dm, View vi)
 {
  controllerMain=c;
  dataModel=dm;
  view=vi;

  menuBarView=view.menuBarView;

  //CLEAR WORKSPACE
  menuBarView.newItem.setOnAction(e -> 
  {
   Alert alert = new Alert(AlertType.CONFIRMATION);
   alert.setTitle("Confirmation");
   alert.setHeaderText("The workspace is about to be cleared.");
   alert.setContentText("All unsaved data will be lost. Are you ok with this?");

   Optional<ButtonType> result = alert.showAndWait();
   if (result.get() == ButtonType.OK)
   {
	controllerMain.actionClearWorkspace();
   }
  });

  //OPEN WORKSPACE
  menuBarView.loadItem.setOnAction(e -> 
  {
   Alert alert = new Alert(AlertType.CONFIRMATION);
   alert.setTitle("Confirmation");
   alert.setHeaderText("Another workspace is to be opened.");
   alert.setContentText("All unsaved data will be lost. Are you ok with this?");

   Optional<ButtonType> result = alert.showAndWait();
   if (result.get() != ButtonType.OK)
   {
    return;
   }

   FileChooser fileChooser = new FileChooser();
   fileChooser.setTitle("Open Workspace...");
   fileChooser.getExtensionFilters().addAll(new ExtensionFilter("SW Workspace files", "*.sws"));
   File openFile = fileChooser.showOpenDialog(null);
   if (openFile != null)
   {
    try
    {
	 controllerMain.actionOpenWorkspace(openFile);
	 menuBarView.saveItem.setDisable(false);
    }
    catch(IOException exception)
    {
     exception.printStackTrace();
    }
   }
  });
 
  //SAVE WORKSPACE
  //menuBarView.saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
  menuBarView.saveItem.setOnAction(e -> 
  {
   if (dataModel.getWsFile() != null)
   {
    try
    {
     controllerMain.actionSaveWorkspace(dataModel.getWsFile());
    }
    catch(IOException exception)
    {
     exception.printStackTrace();
    }
   }
  });

  //SAVE WORKSPACE AS
  menuBarView.saveAsItem.setOnAction(e -> 
  {
   FileChooser fileChooser = new FileChooser();
   fileChooser.setTitle("Save Workspace As...");
   fileChooser.getExtensionFilters().addAll(new ExtensionFilter("SW Workspace files", "*.sws"));
   File saveFile = fileChooser.showSaveDialog(null);
   if (saveFile != null)
   {
    String filePath=saveFile.getPath();
    if(!filePath.contains(".sws")) filePath=filePath+".sws";
    saveFile=new File(filePath);
	dataModel.setWsFile(saveFile);
    try
    {
     controllerMain.actionSaveWorkspace(saveFile);
	 menuBarView.saveItem.setDisable(false);
    }
    catch(IOException exception)
    {
     exception.printStackTrace();
    }
   }
  });

  //IMPORT MEDIA
  //menuBarView.importVideoItem.setAccelerator(KeyCombination.keyCombination("Ctrl+M"));
  menuBarView.importVideoItem.setOnAction(e -> 
  {
   FileChooser fileChooser = new FileChooser();
   fileChooser.setTitle("Open Media File");
   fileChooser.getExtensionFilters().addAll(
   new ExtensionFilter("Media Files", "*.aif", "*.aiff", "*.fxm", "*.flv", "*.m3u8", "*.mp3", "*.mp4", "*.m4a", "*.m4v", "*.wav"));
   File selectedFile = fileChooser.showOpenDialog(null);
   if (selectedFile != null)
   {
    controllerMain.importVideo(selectedFile);
   }
  });

  //IMPORT IMAGES
  //menuBarView.importImagesItem.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
  menuBarView.importImagesItem.setOnAction(e -> 
  {
   FileChooser fileChooser = new FileChooser();
   fileChooser.setTitle("View Images");
   fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));    
   fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Images", "*.*"));
   List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
   if (selectedFiles != null)
   {
	controllerMain.importImages(selectedFiles, 0);
   }
  });

 //ABOUT
  menuBarView.aboutItem.setOnAction(e -> 
  {
   int imageWidth=861;
   int imageHeight=1076;
   double scale=0.4;
   String imagespath=System.getProperty("user.dir").replace('\\', '/') + "/images/";
   File swFile = new File(imagespath+"swabout.png");
   Image swImage = new Image(swFile.toURI().toString(), imageWidth*scale, imageHeight*scale, false, false);
   ImageView swImageView=new ImageView(swImage);
   swImageView.setPreserveRatio(true); 

   Dialog<Pair<String, String>> dialog = new Dialog<>();
   dialog.setTitle("About S&W");
   dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

   VBox vBox=new VBox(swImageView);
   dialog.getDialogPane().setContent(vBox);
   dialog.showAndWait();
  });

 //EXPORT
  menuBarView.exportItem.setOnAction(e -> 
  {
   ExportDialog dialog = new ExportDialog();

   Optional<ExportParameters> result = dialog.showAndWait();

   result.ifPresent(e_result -> 
   {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Export Records");
    File exportFile = fileChooser.showSaveDialog(null);
    if (exportFile != null)
    {
     controllerMain.actionExportRecords(exportFile, e_result);
    }
    System.out.println(e_result);
   });

  });
  
  //QUIT
  //menuBarView.quitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
  menuBarView.quitItem.setOnAction(e -> 
  {
   Alert alert = new Alert(AlertType.CONFIRMATION);
   alert.setTitle("Confirmation");
   alert.setHeaderText("The program is about to quit.");
   alert.setContentText("All unsaved data will be lost. Are you ok with this?");

   Optional<ButtonType> result = alert.showAndWait();
   if (result.get() == ButtonType.OK)
   {
    System.exit(0);
   }
  });

 }


}
