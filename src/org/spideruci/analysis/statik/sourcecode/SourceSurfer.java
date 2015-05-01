package org.spideruci.analysis.statik.sourcecode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.spideruci.analysis.statik.sourcecode.SourceFileData.SourceLine;
import org.spideruci.analysis.statik.sourcecode.util.Navigator;

import com.cedarsoftware.util.io.JsonWriter;

public class SourceSurfer {
  
  public static void main(String[] args) {
    String sourcePath = args[0];
    File sourceDirectory = new File(sourcePath);
    checkDirPath(sourcePath, sourceDirectory, 1);
    String jsonRepositoryPath = args[1];
    File jsonRepository = new File(jsonRepositoryPath);
    checkDirPath(jsonRepositoryPath, jsonRepository, 2);
    
    Navigator sourceNavigator = Navigator.init(sourceDirectory);
    while(true) {
      File file = sourceNavigator.next();
      if(file == null) break;
      String className = file.getAbsolutePath().replace(sourcePath, "").replaceAll("/", ".");
      System.out.println(className);
      SourceLine[] lines = getFileLines(file);
      SourceFileData source = new SourceFileData(className, lines);
      spitJson(source, jsonRepository);
    }
  }

  private static void checkDirPath(String repoPath, File repository, 
      int argNumber) {
    if(!repository.exists() && !repository.mkdir()) {
      System.err.printf("Creation of Json Repository {path: %s} failed!\n", 
          repoPath);
      System.exit(1);
    }
    
    if(!repository.isDirectory()) {
      System.err.printf("ERROR: Json Repository isn't a path to a directory.\n"
          + "(arg %d: %s)", argNumber, repository);
      System.exit(1);
    }
  }
  
  private static void spitJson(SourceFileData source, File jsonRepository) {
    final String message = String.format("The json repository is not a direcotry. {path: %s}", jsonRepository.getAbsolutePath());
    assert jsonRepository.isDirectory() : message;
    
    File sourceJson = new File(jsonRepository, source.className + ".json");
    try {
      FileWriter filewriter = new FileWriter(sourceJson);
      String json = JsonWriter.objectToJson(source);
      JsonWriter.writeJsonUtf8String(json, filewriter);
      filewriter.flush();
      filewriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
//    System.out.println(json);
  }
  
  private static SourceLine[] getFileLines(File file) {
    assert file.isFile() && file.getName().endsWith(".java");
    Scanner scanner;
    ArrayList<SourceLine> lines = new ArrayList<SourceLine>();
    try {
      scanner = new Scanner(file);
      int linenumber = 1;
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        
        lines.add(new SourceLine(linenumber, line));
        linenumber += 1;
      }
      
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return lines.toArray(new SourceLine[lines.size()]);
  }
  
  private static void printFile(File file) {
    assert file.isFile() && file.getName().endsWith(".java");
    Scanner scanner;
    try {
      scanner = new Scanner(file);
      while(scanner.hasNextLine()) {
        String line = scanner.nextLine();
        System.out.println(line);
      }
      
      scanner.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
  }
  
}
