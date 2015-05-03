package org.spideruci.analysis.statik.sourcecode.plugins;

import java.util.ArrayList;

import org.spideruci.analysis.statik.sourcecode.SourceLine;
import org.spideruci.analysis.statik.sourcecode.SourceSurfingEventListener;
import org.spideruci.analysis.statik.sourcecode.SourcefileData;

import com.cedarsoftware.util.io.JsonWriter;

public class JavaSourceJsonifier implements SourceSurfingEventListener {

  @SuppressWarnings("unused")
  private String sourcefileName = null;
  private ArrayList<String> sourcelines;
  
  @Override
  public void sourceFileDiscovered(SourcefileData filedata) {
    sourcefileName = filedata.srcFileName;
    sourcelines = new ArrayList<>();
  }

  @Override
  public void sourceLineRead(SourceLine line) {
    sourcelines.add(line.sourceLine);
  }

  @Override
  public void sourceFileReadingDone() {
    String json = JsonWriter.objectToJson(this);
    System.out.println(json);
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
