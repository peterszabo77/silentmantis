package mypackages;

import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import mypackages.ExportParameters;

public class ExportDialog extends Dialog<ExportParameters>
{
 public ExportDialog()
 {
  super();
  this.setTitle("Export records");
  this.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);

  //table format
  Label labelForm=new Label("TABLE FORMAT");
  ToggleGroup groupForm = new ToggleGroup();
  RadioButton rbFormWide = new RadioButton("wide (varibles in separate columns)");
  rbFormWide.setToggleGroup(groupForm);
  rbFormWide.setSelected(true);
  RadioButton rbFormLong = new RadioButton("long (variables in the same 'VARIABLE' column)");
  rbFormLong.setToggleGroup(groupForm);
  VBox vBoxForm=new VBox(labelForm, rbFormWide, rbFormLong);
  vBoxForm.setAlignment(Pos.CENTER_LEFT);
  vBoxForm.setSpacing(5);

  //headers
  Label labelCH=new Label("COLUMN HEADERS");
  CheckBox cbHead=new CheckBox("export headers");
  cbHead.setSelected(true);
  VBox vBoxHead=new VBox(labelCH, cbHead);
  vBoxHead.setAlignment(Pos.CENTER_LEFT);
  vBoxHead.setSpacing(5);

  //separator
  Label labelSep=new Label("COLUMN SEPARATOR");
  ToggleGroup groupSep = new ToggleGroup();
  RadioButton rbSepSemi = new RadioButton("semicolon");
  rbSepSemi.setToggleGroup(groupSep);
  rbSepSemi.setSelected(true);
  RadioButton rbSepCom = new RadioButton("comma");
  rbSepCom.setToggleGroup(groupSep);
  RadioButton rbSepSpa = new RadioButton("space");
  rbSepSpa.setToggleGroup(groupSep);
  RadioButton rbSepTab = new RadioButton("tabulator");
  rbSepTab.setToggleGroup(groupSep);
  VBox vBoxSep=new VBox(labelSep, new HBox(rbSepSemi, rbSepCom, rbSepSpa, rbSepTab));
  vBoxSep.setAlignment(Pos.CENTER_LEFT);
  vBoxSep.setSpacing(5);

  //missing values
  Label labelMis=new Label("MISSING VALUES");
  ToggleGroup groupMis = new ToggleGroup();
  RadioButton rbMisNone = new RadioButton("not replaced");
  rbMisNone.setToggleGroup(groupMis);
  rbMisNone.setSelected(true);
  RadioButton rbMisNA = new RadioButton("replaced with NA string");
  rbMisNA.setToggleGroup(groupMis);
  rbMisNA.setSelected(false);
  VBox vBoxMis=new VBox(labelMis, rbMisNone, rbMisNA);
  vBoxMis.setAlignment(Pos.CENTER_LEFT);
  vBoxMis.setSpacing(5);

  //enclosing strings
  Label labelEnc=new Label("NONNUMERIC VALUES");
  ToggleGroup groupEnc = new ToggleGroup();
  RadioButton rbEncNot = new RadioButton("not enclosed");
  rbEncNot.setToggleGroup(groupEnc);
  rbEncNot.setSelected(true);
  RadioButton rbEncSin = new RadioButton("enclosed between single quotation marks");
  rbEncSin.setToggleGroup(groupEnc);
  RadioButton rbEncDou = new RadioButton("enclosed between double quotation marks");
  rbEncDou.setToggleGroup(groupEnc);
  VBox vBoxEnc=new VBox(labelEnc, rbEncNot, rbEncSin, rbEncDou);
  vBoxEnc.setAlignment(Pos.CENTER_LEFT);
  vBoxEnc.setSpacing(5);

  VBox vBox=new VBox(vBoxForm, vBoxHead, vBoxSep, vBoxMis, vBoxEnc);
  vBox.setSpacing(20);
  this.getDialogPane().setContent(vBox);

  this.setResultConverter(dialogButton -> 
  {
   if (dialogButton == ButtonType.OK) 
   {
	ExportParameters ep=new ExportParameters();
	if (rbFormWide.isSelected()) ep.setFormat("wide");
	if (rbFormLong.isSelected()) ep.setFormat("long");
	if (cbHead.isSelected()) ep.setHeaders(true);
	if (!cbHead.isSelected()) ep.setHeaders(false);
	if (rbSepSemi.isSelected()) ep.setSeparator(";");
	if (rbSepCom.isSelected()) ep.setSeparator(",");
	if (rbSepSpa.isSelected()) ep.setSeparator(" ");
	if (rbSepTab.isSelected()) ep.setSeparator("\t");
	if (rbMisNone.isSelected()) ep.setNAreplace(false);
	if (rbMisNA.isSelected()) ep.setNAreplace(true);
	if (rbEncNot.isSelected()) ep.setEnclosing("none");
	if (rbEncSin.isSelected()) ep.setEnclosing("single");
	if (rbEncDou.isSelected()) ep.setEnclosing("double");
    return(ep);
   }
   return null;
  });

 }
}
