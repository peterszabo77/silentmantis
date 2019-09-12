package mypackages;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import mypackages.BVScale;
import mypackages.BVType;
import mypackages.BVColor;

import java.lang.String;

public class BVariable
{
 private StringProperty name;
 private BVScale scale;
 private BVType type;
 private BVColor color;

 public BVariable(String n)
 {
  name=new SimpleStringProperty(n);
  this.scale=BVScale.FACTOR;
  this.type=BVType.EVENT;
  this.color=BVColor.BLUE;
 }

 public final String getName(){ return name.get();}
 public final void setName(String value){ name.set(value);}
 public StringProperty nameProperty() {return name;}
 
 public BVScale getScale() {return scale;}
 public void setScale(BVScale s) {scale=s;}
 
 public BVType getType() {return type;}
 public void setType(BVType t) {type=t;}
 
 public BVColor getColor() {return color;}
 public void setColor(BVColor c) {color=c;}
}
