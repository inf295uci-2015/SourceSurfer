package org.spideruci.analysis.statik.sourcecode.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.cedarsoftware.util.io.JsonWriter;

public class JsonUtils {
  
  public static void spitJsonToFile(String json, String name, File jsonRepository) {
    final String message = 
        String.format("The json repository is not a direcotry. {path: %s}", 
            jsonRepository.getAbsolutePath());
    assert jsonRepository.isDirectory() : message;
    
    File sourceJson = new File(jsonRepository, name + ".json");
    try {
      FileWriter filewriter = new FileWriter(sourceJson);
      JsonWriter.writeJsonUtf8String(json, filewriter);
      filewriter.flush();
      filewriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
