package mypackages;

public enum BVScale 
{ 
 NUMERIC ("NUMERIC"), FACTOR ("FACTOR");
 String value;
 
 BVScale(String s)
 {
  value=s;
 }
}
