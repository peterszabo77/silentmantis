package mypackages;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

public class MenuBarView extends MenuBar
{
 public Menu fileMenu;
 public Menu aboutMenu;
 public MenuItem newItem;
 public MenuItem loadItem;
 public MenuItem saveItem;
 public MenuItem saveAsItem;
 public MenuItem importVideoItem;
 public MenuItem importImagesItem;
 public MenuItem exportItem;
 public MenuItem quitItem;
 public MenuItem aboutItem;

 public MenuBarView()
 {
  super();

  fileMenu = new Menu("File");
  aboutMenu = new Menu("About");
 
  newItem = new MenuItem("Clear workspace");
  loadItem = new MenuItem("Open Workspace");
  saveItem = new MenuItem("Save Workspace");
  saveAsItem = new MenuItem("Save Workspace As...");
  SeparatorMenuItem separatorMenuItem1 = new SeparatorMenuItem();
  importVideoItem = new MenuItem("Import Media File");
  importImagesItem = new MenuItem("Import Images");
  SeparatorMenuItem separatorMenuItem2 = new SeparatorMenuItem();
  exportItem = new MenuItem("Export Records");
  SeparatorMenuItem separatorMenuItem3 = new SeparatorMenuItem();
  quitItem = new MenuItem("Quit");
  fileMenu.getItems().addAll(newItem, loadItem, saveItem, saveAsItem, separatorMenuItem1, importVideoItem, importImagesItem, separatorMenuItem2, exportItem, separatorMenuItem3, quitItem);
  aboutItem = new MenuItem("About");
  aboutMenu.getItems().addAll(aboutItem);
  super.getMenus().addAll(fileMenu, aboutMenu);
  saveItem.setDisable(true);
 }

}
