package org.spideruci.analysis.statik.sourcecode.plugins;

import org.spideruci.analysis.statik.sourcecode.SourceLine;
import org.spideruci.analysis.statik.sourcecode.SourceSurfingEventListener;
import org.spideruci.analysis.statik.sourcecode.SourcefileData;

public class JavaSourcePrinter implements SourceSurfingEventListener {

  private String javafileName;
  
  @Override
  public void sourceFileDiscovered(SourcefileData filedata) {
    javafileName = filedata.srcFileName;
    System.out.println(javafileName);
  }

  @Override
  public void sourceLineRead(SourceLine line) {
    System.out.print("├── ");
    
    System.out.println(line.linenumber + "\t" + line.sourceLine);
  }

  @Override
  public void sourceFileReadingDone() {
    System.out.println("*");
  }

  @Override
  public void startListening() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void stopListening() {
    // TODO Auto-generated method stub
    
  }
}
