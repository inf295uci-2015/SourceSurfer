package org.spideruci.analysis.statik.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.Scanner;

public class SourcefileSurfer {
  
  final private File sourcefile;
  private Scanner scanner;
  private int linecount;
  
  private SourcefileSurfer(File file) {
    sourcefile = file;
    linecount = 0;
  }
  
  public static final SourcefileSurfer init(final File file) throws InvalidObjectException, FileNotFoundException {
    if(file == null || file.isDirectory()) {
      throw new InvalidObjectException("File object is either null, or points to a directory.");
    }
    
    SourcefileSurfer x = new SourcefileSurfer(file).init();
    
    
    return x;
  }
  
  public SourcefileSurfer init() throws FileNotFoundException {
    this.scanner = new Scanner(this.sourcefile);
    linecount = 0;
    return this;
  }
  
  public boolean hasNext() {
    return scanner != null && scanner.hasNextLine();
  }
  
  public SourceLine next() {
    return readNextLine();
  }
  
  public SourceLine readNextLine() {
    SourceLine line;
    if(scanner.hasNextLine()) {
      String lineText = scanner.nextLine();
      line = new SourceLine(linecount + 1, lineText);
      linecount += 1;
      return line;
    } else {
      scanner.close();
      return null;
    }
  }

}
