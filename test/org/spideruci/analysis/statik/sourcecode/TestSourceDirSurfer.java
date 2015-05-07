package org.spideruci.analysis.statik.sourcecode;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSourceDirSurfer {
  
  static File currentDir;
  static File topJavaFile, topRubyFile, topDir, lowerJavaFile, lowerRubyFile, lowerDir;
  
  @BeforeClass
  public static void setupClass() throws IOException {
    currentDir = new File(System.getProperty("user.dir"));
    topJavaFile = new File(currentDir, "garbage.java"); topJavaFile.createNewFile();
    topRubyFile = new File(currentDir, "garbage.rb"); topRubyFile.createNewFile();
    topDir = new File(currentDir, "top-dir"); topDir.mkdir();
    lowerJavaFile = new File(topDir, "garbage2.java"); lowerJavaFile.createNewFile();
    lowerRubyFile = new File(topDir, "garbage2.rb"); lowerRubyFile.createNewFile();
    lowerDir = new File(topDir, "lower-dir"); lowerDir.mkdir();
  }
  
  @AfterClass
  public static void afterClass() {
    lowerDir.delete();
    lowerRubyFile.delete();
    lowerJavaFile.delete();
    topDir.delete();
    topJavaFile.delete();
    topRubyFile.delete();
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
  public void nextShouldReturnNullWithScalaFileExtension() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".scala");
    
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
  public void nextShouldReturnTopJavaFileWhenInvokedOnceWithJavaExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".java");
    
    //when
    File nextSource = surfer.next();
    
    //then
    assertEquals(topJavaFile, nextSource);
  }
  
  @Test
  public void nextShouldReturnLowerJavaFileWhenInvokedTwiceWithJavaExtn() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(currentDir, ".java");
    
    //when
    surfer.next();
    File nextSource = surfer.next();
    
    //then
    assertEquals(lowerJavaFile, nextSource);
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
  public void nextShouldReturnNullWithLowerDirAsSourceAndJavaRubeExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(lowerDir, ".java", ".rb");
    
    //when
    File firstsource = surfer.next();
    
    //then
    assertNull(firstsource);
  }
  
  @Test 
  public void nextShouldRunTwiceReturnBeforeNullWithTopDirAsSourceAndJavaRubeExtns() {
    //given
    SourceDirSurfer surfer = SourceDirSurfer.init(topDir, ".java", ".rb");
    int count = 0;
    
    //when
    while(surfer.next() != null) {
      count += 1;
    }
    
    //then
    assertEquals(2, count);
  }

}
