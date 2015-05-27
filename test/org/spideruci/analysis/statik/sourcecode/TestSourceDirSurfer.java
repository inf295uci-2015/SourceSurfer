package org.spideruci.analysis.statik.sourcecode;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSourceDirSurfer {
  
  static File currentDir;
  static File topScalaFile, topRubyFile, topDir, lowerScalaFile, lowerRubyFile, lowerDir;
  
  @BeforeClass
  public static void setupClass() throws IOException {
    currentDir = new File(System.getProperty("user.dir"));
    topScalaFile = new File(currentDir, "garbage.scala"); topScalaFile.createNewFile();
    topRubyFile = new File(currentDir, "garbage.rb"); topRubyFile.createNewFile();
    topDir = new File(currentDir, "top-dir"); topDir.mkdir();
    lowerScalaFile = new File(topDir, "garbage2.scala"); lowerScalaFile.createNewFile();
    lowerRubyFile = new File(topDir, "garbage2.rb"); lowerRubyFile.createNewFile();
    lowerDir = new File(topDir, "lower-dir"); lowerDir.mkdir();
  }
  
  @AfterClass
  public static void afterClass() {
    lowerDir.delete();
    lowerRubyFile.delete();
    lowerScalaFile.delete();
    topDir.delete();
    topScalaFile.delete();
    topRubyFile.delete();
  }
  
  /**
   * this is a test for tacoco.
   */
  @Test(expected = NullPointerException.class)
  public void shouldThrowNullException() {
    throw new NullPointerException();
  }

  @Test
  public void nextShouldReturnNullWithNoSpecifiedFileExtension() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir);
    
    //when
    File nextSourceFile = surfer.next();
    
    //then
    assertNull(nextSourceFile);;
  }
  
  @Test
  public void nextShouldReturnNullWithPythonFileExtension() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".python");
    
    //when
    File nextSourceFile = surfer.next();
    
    //then
    assertNull(nextSourceFile);
  }
  
  @Test
  public void nextShouldReturnTopRubyFileWhenInvokedOnceWithRubyExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".rb");
    
    //when
    File nextSourceFile = surfer.next();
    
    //then
    assertEquals(topRubyFile, nextSourceFile);
  }
  
  @Test
  public void nextShouldReturnTopJavaFileWhenInvokedOnceWithScalaExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala");
    
    //when
    File nextSource = surfer.next();
    
    //then
    assertEquals(topScalaFile, nextSource);
  }
  
  @Test
  public void nextShouldReturnLowerJavaFileWhenInvokedTwiceWithScalaExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala");
    
    //when
    surfer.next();
    File nextSource = surfer.next();
    
    //then
    assertEquals(lowerScalaFile, nextSource);
  }
  
  @Test
  public void nextShouldReturnLowerRubyFileWhenInvokedTwiceWithRubyExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".rb");
    
    //when
    surfer.next();
    File nextSourceFile = surfer.next();
    
    //then
    assertEquals(lowerRubyFile, nextSourceFile);
  }
    
  @Test
  public void nextShouldReturnNullWhenInvokedThirceWithrubyExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".rb");
    
    //when
    surfer.next();
    surfer.next();
    File thirdSourceFile = surfer.next();
    
    //then
    assertNull(thirdSourceFile);
  }
  
  @Test
  public void nextShouldReturnNullWhenInvokedThirceWithScalaExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala");
    
    //when
    surfer.next();
    surfer.next();
    File thirdSourceFile = surfer.next();
    
    //then
    assertNull(thirdSourceFile);
  }
  
  @Test 
  public void nextShouldReturnNullWithLowerDirAsSourceAndScalaRubyExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(lowerDir, ".scala", ".rb");
    
    //when
    File firstsource = surfer.next();
    
    //then
    assertNull(firstsource);
  }
  
  @Test 
  public void nextShouldRunTwiceReturnBeforeNullWithTopDirAsSourceAndScalaRubyExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(topDir, ".scala", ".rb");
    int runCount = 0;
    int expectedRuns = 2;
    
    //when
    while(surfer.next() != null) {
      runCount += 1;
    }
    
    //then
    assertEquals(expectedRuns, runCount);
  }
  
  @Test 
  public void nextShouldRun4TimesReturnBeforeNullWithCurrentDirAsSourceAndScalaRubyExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala", ".rb");
    int runCount = 0;
    int expectedRuns = 4;
    
    //when
    while(surfer.next() != null) {
      runCount += 1;
    }
    
    //then
    assertEquals(expectedRuns, runCount);
  }
  
  @Test 
  public void nextShouldRun2TimesReturnBeforeNullWithCurrentDirAsSourceAndRubyExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".rb");
    int runCount = 0;
    int expectedRuns = 2;
    
    //when
    while(surfer.next() != null) {
      runCount += 1;
    }
    
    //then
    assertEquals(expectedRuns, runCount);
  }
  
  @Test 
  public void nextShouldRun2TimesReturnBeforeNullWithCurrentDirAsSourceAndScalaExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala");
    int runCount = 0;
    int expectedRuns = 2;
    
    //when
    while(surfer.next() != null) {
      runCount += 1;
    }
    
    //then
    assertEquals(expectedRuns, runCount);
  }
  


}
