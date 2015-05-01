package org.spideruci.analysis.statik.sourcecode;

public class SourceFileData {
  public final String className;
  public final SourceLine[] lines;
  
  public SourceFileData(String className, SourceLine[] lines) {
    this.className = className;
    this.lines = lines;
  }
  
  
  
  public static class SourceLine {
    public SourceLine(int number, String code) {
      linenumber = number;
      sourceLine = code;
    }
    
    public int linenumber;
    public String sourceLine;
  }
}
