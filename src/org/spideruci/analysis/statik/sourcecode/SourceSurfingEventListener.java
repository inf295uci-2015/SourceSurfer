package org.spideruci.analysis.statik.sourcecode;

public interface SourceSurfingEventListener {
  public void startListening();
  public void sourceFileDiscovered(SourcefileData filedata);
  public void sourceLineRead(SourceLine line);
  public void sourceFileReadingDone();
  public void stopListening();
}
