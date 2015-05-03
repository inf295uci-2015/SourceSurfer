package org.spideruci.analysis.statik.sourcecode;

public class SourcefileData {
  public final String srcFileName;
  
  public SourcefileData(String sourcefileName) {
    this.srcFileName = sourcefileName;
  }
  
  public String sourcefileName() {
    return srcFileName;
  }
}
