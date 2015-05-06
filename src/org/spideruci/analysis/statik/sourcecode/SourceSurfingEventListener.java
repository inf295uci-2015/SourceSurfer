package org.spideruci.analysis.statik.sourcecode;

import org.spideruci.analysis.statik.sourcecode.models.SourceLine;
import org.spideruci.analysis.statik.sourcecode.models.SourcefileData;

public interface SourceSurfingEventListener {
  public void startListening();
  public void sourceFileDiscovered(SourcefileData filedata);
  public void sourceLineRead(SourceLine line);
  public void sourceFileReadingDone();
  public void stopListening();
}
