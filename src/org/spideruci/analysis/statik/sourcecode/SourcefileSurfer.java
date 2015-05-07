package org.spideruci.analysis.statik.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.util.Scanner;

import org.spideruci.analysis.statik.sourcecode.models.SourceLine;

public class SourcefileSurfer {
  
  private Scanner scanner;
  private int linecount;
  
  private SourcefileSurfer(Scanner scanner) {
    this.scanner = scanner;
    linecount = 0;
  }
  
  private static final Scanner initFileScanner(final File file) 
      throws InvalidObjectException, FileNotFoundException {
    if(file == null || file.isDirectory()) {
      throw new InvalidObjectException("File object is either null, or points to a directory.");
    }
    
    return new Scanner(file);
  }
  
  public static final SourcefileSurfer init(final Reader reader) {
    SourcefileSurfer x = new SourcefileSurfer(new Scanner(reader));
    return x;
  }
  
  public static final SourcefileSurfer init(final File file) 
      throws InvalidObjectException, FileNotFoundException {
    Scanner scanner = initFileScanner(file);
    SourcefileSurfer x = new SourcefileSurfer(scanner);
    return x;
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
