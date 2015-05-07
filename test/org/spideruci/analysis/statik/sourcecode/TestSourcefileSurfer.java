package org.spideruci.analysis.statik.sourcecode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Reader;
import java.io.StringReader;

import org.hamcrest.core.IsEqual;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.spideruci.analysis.statik.sourcecode.models.SourceLine;

public class TestSourcefileSurfer {

  @Test
  public void nextLineShouldBeNullWhenReaderHasNothingToRead() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader(""));

    //when
    SourceLine line = fileSurfer.next();

    //then
    assertNull(line);
  }

  @Test
  public void shouldReadNullWhenThereisNothingToRead() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader(""));

    //when
    SourceLine line = fileSurfer.readNextLine();

    //then
    assertNull(line);
  }

  @Test
  public void thereIsNothingToReadWithAnEmptyReader() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader(""));

    //when
    boolean thereIsSomethingToRead = fileSurfer.hasNext();

    //then
    assertFalse(thereIsSomethingToRead);
  }
  
  @Test
  public void thereIsOneEmptyLineToReadWithANewline() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader("\n"));

    //when
    boolean thereIsSomethingToRead = fileSurfer.hasNext();
    SourceLine line = fileSurfer.next();
    String lineText = line.trimmedSourceline();

    //then
    assertTrue(thereIsSomethingToRead);
    assertEquals("", lineText);
  }
  
  @Test
  public void thereIsOneEmptyLineToReadWithACarriageReturn() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader("\r"));

    //when
    boolean thereIsSomethingToRead = fileSurfer.hasNext();
    SourceLine line = fileSurfer.next();
    String lineText = line.trimmedSourceline();

    //then
    assertTrue(thereIsSomethingToRead);
    assertEquals("", lineText);
  }

  @Test
  public void thereAreTwoEmptyLinesToReadWithTwoCarriageReturn() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader("\r\r"));
    int lineCount = 0;
    int expectedLineCount = 2;

    //when
    while(fileSurfer.hasNext()) {
      lineCount += 1;
      fileSurfer.next();
    }

    //then
    assertEquals(expectedLineCount, lineCount);
  }
  
  @Test
  public void shoudlTreatOneCarriageReturnFollowedByOneNewLineAsOneNewLine() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader("\r\n"));
    int lineCount = 0;
    int expectedLineCount = 1;

    //when
    while(fileSurfer.hasNext()) {
      lineCount += 1;
      fileSurfer.next();
    }

    //then
    assertEquals(expectedLineCount, lineCount);
  }
  
  @Test
  public void shoudlTreatOneNewLineFollowedOneCarriageReturnByAsOneNewLine() {
    //given
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(new StringReader("\n\r"));
    int lineCount = 0;
    int expectedLineCount = 2;

    //when
    while(fileSurfer.hasNext()) {
      lineCount += 1;
      fileSurfer.next();
    }

    //then
    assertEquals(expectedLineCount, lineCount);
  }
  
  @Test
  public void thereAreTwoEmptyLinesToReadWithTwoNewEmptylines() {
    //given
    StringReader reader = new StringReader("\n\n");
    SourcefileSurfer fileSurfer = SourcefileSurfer.init(reader);
    
    int lineCount = 0;
    int expectedLineCount = 2;

    //when
    while(fileSurfer.hasNext()) {
      lineCount += 1;
      fileSurfer.next();
    }
    
    reader.close();

    //then
    assertEquals(expectedLineCount, lineCount);
  }
  
  @Rule
  public ExpectedException npeThrown = ExpectedException.none();
  
  @Test
  public void thereIsNothingToReadWhenTheReaderIsNull() 
      throws NullPointerException {
    //given
    Reader reader = null;
    
    //then
    npeThrown.expect(NullPointerException.class);
    
    //when
    SourcefileSurfer.init(reader);
  }
  
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void throwInvalidObjectExceptionWhenReadingANullFile() 
      throws InvalidObjectException, FileNotFoundException {
    //given
    File sourcefile = null;
    
    //then
    thrown.expect(InvalidObjectException.class);
    
    //when
    SourcefileSurfer.init(sourcefile);
  }
  
  @Rule
  public ExpectedException thrown2 = ExpectedException.none();
  
  @Test
  public void throwInvalidObjectExceptionWhenReadingADirectory() 
      throws InvalidObjectException, FileNotFoundException {
    //given
    File randomDir = mock(File.class);
    when(randomDir.isDirectory()).thenReturn(true);
    
    //then
    thrown2.expect(InvalidObjectException.class);
    
    //when
    SourcefileSurfer.init(randomDir);
  }
  
}
