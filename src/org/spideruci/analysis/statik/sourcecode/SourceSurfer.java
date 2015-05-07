package org.spideruci.analysis.statik.sourcecode;

import static org.spideruci.analysis.statik.sourcecode.util.Cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InvalidObjectException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.spideruci.analysis.statik.sourcecode.models.SourceLine;
import org.spideruci.analysis.statik.sourcecode.models.SourcefileData;
import org.spideruci.analysis.statik.sourcecode.plugins.AbstractSourceSurferPlugin;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

public class SourceSurfer {
  
  public static void main(String[] args) 
      throws InvalidObjectException, FileNotFoundException, YamlException {
    CommandLine cmd = CLI.parse(args);
    
    if(!cmd.hasOption(SRC)) {
      CLI.printHelp();
      System.exit(0);
    }
    
    if(cmd.hasOption(HELP)) {
      CLI.printHelp();
    }
    
    String sourcePath = cmd.getOptionValue(SRC);
    File sourceDirectory = new File(sourcePath);
    checkDirPath(sourcePath, sourceDirectory);
    
    File conf = null;
    if(cmd.hasOption(CONFIG)) {
      String confPath = cmd.getOptionValue(CONFIG);
      conf = new File(confPath);
      
    }
    
    List<AbstractSourceSurferPlugin> plugins = new ArrayList<>();
    if(conf.getName().endsWith(".yml") || conf.getName().endsWith("yaml")) {
      discoverPlugins_yml(conf, plugins);
    } else {
      discoverPlugins_text(conf, plugins);
    }
    
    
    ArrayList<String> fileExtensions = new ArrayList<String>();
    if(cmd.hasOption(FILE_EXTNS)) {
      String[] extensions = cmd.getOptionValues(FILE_EXTNS);
      for(String extn : extensions) {
        if(!extn.startsWith(".")) {
          System.err.printf("Ignoring unrecognized source-file extension: %s. "
              + "File extensions must start with a dot (.).\n", extn);
          continue;
        }
        
        fileExtensions.add(extn);
      }
    } else {
      fileExtensions.add(".java");
    }
    
    SourceDirSurfer sourceNavigator = SourceDirSurfer.init(sourceDirectory, 
        fileExtensions.toArray(new String[fileExtensions.size()]));
    
    for(SourceSurfingEventListener listener : plugins) {
      listener.startListening();
    }
    
    if(plugins.size() == 0) return;
    
    while(true) {
      File sourcefile = sourceNavigator.next();
      if(sourcefile == null) break;
      
      String className = sourcefile.getAbsolutePath().replace(sourcePath, "").replaceAll("/", ".");
      SourcefileData sourceFileData = new SourcefileData(className);
      
      for(SourceSurfingEventListener listener : plugins) {
        listener.sourceFileDiscovered(sourceFileData);
      }
      
      SourcefileSurfer fileSurfer = SourcefileSurfer.init(sourcefile);
      while(fileSurfer.hasNext()) {
        SourceLine line = fileSurfer.readNextLine();
        
        for(SourceSurfingEventListener listener : plugins) {
          listener.sourceLineRead(line);
        }
      }
      
      for(SourceSurfingEventListener listener : plugins) {
        listener.sourceFileReadingDone();
      }
    }
    
    for(SourceSurfingEventListener listener : plugins) {
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

  private static void discoverPlugins_text(File conf, 
      List<AbstractSourceSurferPlugin> listeners) throws FileNotFoundException {
    if(conf == null) {
      conf = new File("conf");
    }
    
    Scanner confScanner = new Scanner(conf);
    while(confScanner.hasNextLine()) {
      String pluginName = confScanner.nextLine();
      pluginName = pluginName.trim();
      if(pluginName == null || pluginName.isEmpty() 
          || pluginName.startsWith("#")) {
        continue;
      }
      
      AbstractSourceSurferPlugin plugin = createPlugin(pluginName);
      if(plugin != null) {
        listeners.add(plugin);
      }
      
      continue;
    }
    
    confScanner.close();
  }
  
  private static void discoverPlugins_yml(File conf, 
  List<AbstractSourceSurferPlugin> listeners) throws FileNotFoundException, YamlException {
    if(conf == null) {
      conf = new File("conf");
    }
    
    YamlReader reader = new YamlReader(new FileReader(conf));
    while(true) {
      Object object = reader.read();
      if(object == null) break;
      Class<?> objectClass = object.getClass(); 
      if(objectClass == String.class) {
        AbstractSourceSurferPlugin plugin = createPlugin((String) object);
        if(plugin != null) {
          listeners.add(plugin);
        }
        continue;
      }

      
      if(AbstractSourceSurferPlugin.class.isInstance(object)) {
        AbstractSourceSurferPlugin plugin = (AbstractSourceSurferPlugin) object;
        listeners.add(plugin);
        continue;
      }
      
      if(objectClass == HashMap.class) {
        @SuppressWarnings("unchecked")
        HashMap<String, ?> map = (HashMap<String, ?>) object;
        for(Entry<String, ?> entry : map.entrySet()) {
          AbstractSourceSurferPlugin plugin = createPlugin(entry.getKey());
          if(plugin != null) {
            plugin.init(entry.getValue());
            listeners.add(plugin);
          }
          continue;
        }
        continue;
      }

    }
  }
  
    private static AbstractSourceSurferPlugin createPlugin(String name) {
      if(name == null || name.isEmpty()) return null;
      
      Object plugin = null;
      
      try {
        Class<?> pluginClass = Class.forName(name);
        plugin = pluginClass.newInstance();
      } catch (ClassNotFoundException | InstantiationException 
          | IllegalAccessException e) {
        System.err.println("Issue with the following plugin: " + name);
        e.printStackTrace();
        return null;
      }
      
      if (AbstractSourceSurferPlugin.class.isInstance(plugin)) {
        return (AbstractSourceSurferPlugin) plugin;
      } else {
        System.err.println(name 
            + " is not a plugin (does not extend AbstractSourceSurferPlugin).");
        return null;
      }
    }
}