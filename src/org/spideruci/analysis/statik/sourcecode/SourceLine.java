package org.spideruci.analysis.statik.sourcecode;

public class SourceLine {
  public SourceLine(int number, String code) {                 
    linenumber = number;
    sourceLine = code;
  }
  
  public int linenumber;
  public String sourceLine;
  
  public String trimmedSourceline() {
    if(sourceLine == null) return "";
    return sourceLine.replaceFirst("\\s+$", "");
  }
}