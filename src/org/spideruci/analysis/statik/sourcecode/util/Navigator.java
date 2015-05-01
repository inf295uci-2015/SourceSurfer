package org.spideruci.analysis.statik.sourcecode.util;

import java.io.File;
import java.util.ArrayList;

public class Navigator {
  File root;
  ArrayList<File> tbd;
  
  public static Navigator init(File root) {
    Navigator x = new Navigator(root).initializeTbd();
    return x;
  }
  
  private Navigator(File root) {
    this.root = root;
    this.tbd = new ArrayList<>();
  }
  
  /**
   * 
   * @return null if there are no files left.
   */
  public File next() {
    if(tbd.isEmpty())
      return null;
   File file = tbd.remove(0);
   
   while(file.isDirectory()) {
     File[] files = file.listFiles();
     for(File f : files) {
       if(f == null) continue;
       this.tbd.add(f);
     }
     
     if(tbd.isEmpty())
        return null;
     file = tbd.remove(0);
   }
   return file;
  }
  
    private Navigator initializeTbd() {
      File[] files = this.root.listFiles();
      
      for(File f : files) {
        if(f == null) continue;
        this.tbd.add(f);
      }
      
      return this;
    }
}