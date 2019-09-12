package mypackages;

public class ExportParameters 
{
 private String format;
 private Boolean headers;
 private String separator;
 private Boolean NAreplace;
 private String enclosing;
 
 public ExportParameters()
 {
 }

 public String getFormat() {return format;}
 public Boolean getHeaders() {return headers;}
 public String getSeparator() {return separator;}
 public Boolean getNAreplace() {return NAreplace;}
 public String getEnclosing() {return enclosing;}
 public void setFormat(String s) {format=s;}
 public void setHeaders(Boolean b) {headers=b;}
 public void setSeparator(String s) {separator=s;}
 public void setNAreplace(Boolean b) {NAreplace=b;}
 public void setEnclosing(String s) {enclosing=s;}
}
