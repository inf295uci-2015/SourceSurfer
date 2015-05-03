package org.spideruci.analysis.statik.sourcecode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.spideruci.analysis.statik.sourcecode.util.SourceFilter;

public class SourceDirSurfer {
  File root;
  final ArrayList<File> filesToInspect;
  final SourceFilter sourceFilter;
  
  public static SourceDirSurfer init(File root, String ... sourcefileExtentions) {
    SourceDirSurfer x = new SourceDirSurfer(root).initializeTbd();
    x.sourceFilter.initFilter(Arrays.asList(sourcefileExtentions));
    return x;
  }
  
  private SourceDirSurfer(File root) {
    this.root = root;
    this.filesToInspect = new ArrayList<>();
    this.sourceFilter = new SourceFilter();
  }
  
  /**
   * 
   * @return null if there are no files left.
   */
  public File next() {
    if(filesToInspect.isEmpty())
      return null;
   File file = filesToInspect.remove(0);
   
   while(file.isDirectory()) {
    File[] files = file.listFiles(sourceFilter);
     for(File f : files) {
       if(f == null) continue;
       this.filesToInspect.add(f);
     }
     
     if(filesToInspect.isEmpty())
        return null;
     file = filesToInspect.remove(0);
   }
   return file;
  }
  
    private SourceDirSurfer initializeTbd() {
      File[] files = this.root.listFiles();
      
      for(File f : files) {
        if(f == null) continue;
        this.filesToInspect.add(f);
      }
      
      return this;
    }
}