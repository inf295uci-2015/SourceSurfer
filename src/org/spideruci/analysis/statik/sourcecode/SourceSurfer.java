package org.spideruci.analysis.statik.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SourceSurfer {
  
  public static void main(String[] args) 
      throws InvalidObjectException, FileNotFoundException {
    String sourcePath = args[0];
    File sourceDirectory = new File(sourcePath);
    checkDirPath(sourcePath, sourceDirectory);
    
    File conf = null;
    if(args.length == 2) {
      String confPath = args[1];
      conf = new File(confPath);
    }
    
    List<SourceSurfingEventListener> listeners = new ArrayList<>();
    discoverPlugins(conf, listeners);
    
    SourceDirSurfer sourceNavigator = SourceDirSurfer.init(sourceDirectory, ".java");
    
    for(SourceSurfingEventListener listener : listeners) {
      listener.startListening();
    }
    
    if(listeners.size() == 0) return;
    
    while(true) {
      File sourcefile = sourceNavigator.next();
      if(sourcefile == null) break;
      
      String className = sourcefile.getAbsolutePath().replace(sourcePath, "").replaceAll("/", ".");
      SourcefileData sourceFileData = new SourcefileData(className);
      
      for(SourceSurfingEventListener listener : listeners) {
        listener.sourceFileDiscovered(sourceFileData);
      }
      
      SourcefileSurfer fileSurfer = SourcefileSurfer.init(sourcefile);
      while(fileSurfer.hasNext()) {
        SourceLine line = fileSurfer.readNextLine();
        
        for(SourceSurfingEventListener listener : listeners) {
          listener.sourceLineRead(line);
        }
      }
      
      for(SourceSurfingEventListener listener : listeners) {
        listener.sourceFileReadingDone();
      }
    }
    
    for(SourceSurfingEventListener listener : listeners) {
      listener.stopListening();
    }
  }

  private static void checkDirPath(String repoPath, File repository) {
    if(!repository.exists() && !repository.mkdir()) {
      System.err.printf("Creation of directory {path: %s} failed!\n", 
          repoPath);
      System.exit(1);
    }
    
    if(repository.exists() && !repository.isDirectory()) {
      System.err.printf("ERROR: This isn't a path to a directory.\n"
          + ": %s)", repository);
      System.exit(1);
    }
  }
  
  private static void discoverPlugins(File conf, 
      List<SourceSurfingEventListener> listeners) throws FileNotFoundException {
    if(conf == null) {
      conf = new File("conf");
    }
    
    Scanner confScanner = new Scanner(conf);
    while(confScanner.hasNextLine()) {
      String plugin = confScanner.nextLine();
      if(plugin == null || plugin.isEmpty() || plugin.startsWith("#")) continue;
      try {
        Class<?> clazz = Class.forName(plugin);
        listeners.add((SourceSurfingEventListener) clazz.newInstance());
      } catch (ClassNotFoundException | InstantiationException 
          | IllegalAccessException e) {
        System.err.println("Issue with the following plugin: " + plugin);
        e.printStackTrace();
        continue;
      }
    }
    
    confScanner.close();
  }
}
