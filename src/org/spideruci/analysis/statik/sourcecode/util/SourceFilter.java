package org.spideruci.analysis.statik.sourcecode.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class SourceFilter implements FileFilter {
  
    private final ArrayList<String> extensions;
    
    public SourceFilter() {
      extensions = new ArrayList<>();
    }
    
    public SourceFilter initFilter(List<String> fileExtensions) {
      if(fileExtensions == null || fileExtensions.isEmpty()) {
        return this;
      }
      
      for(String extn : fileExtensions) {
        if(extn == null || extn.isEmpty()) {
          continue;
        }
        this.extensions.add(extn);
      }
      return this;
    }
    
    public String[] getExtensions() {
      return this.extensions.toArray(new String[this.extensions.size()]);
    }
    
    @Override
    public boolean accept(File file) {
      if(file.isDirectory()) {
        return true;
      }
      
      String name = file.getName();
      for(String extn : extensions) {
        if(extn == null || extn.isEmpty()) {
          continue;
        }
        
        if(name.endsWith(extn)) {
          return true;
        }
      }
      
      return false;
    }
    
    public static class InvalidFileExtensionException extends Exception {
      private static final long serialVersionUID = 1L;
    }


}
