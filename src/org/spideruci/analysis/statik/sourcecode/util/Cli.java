package org.spideruci.analysis.statik.sourcecode.util;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {
  
  public static final Cli CLI = new Cli().init();
  public static final char CONFIG = 'c';
  public static final char SRC = 's';
  public static final char HELP = 'h';
  public static final char FILE_EXTNS = 'e';
  
  private Option conf;
  private Option sourceDir;
  private Option fileExtensions;
  private Option help;
  
  private Options options = new Options();
  
  public Cli init() {
    OptionBuilder.withArgName("help");
    OptionBuilder.hasArg(false);
    OptionBuilder.withDescription("print this message.");
    OptionBuilder.withLongOpt("help");
    help = OptionBuilder.create('h');
    
    OptionBuilder.withArgName("file");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription("use given file path as the plugin configuration file. Default: `./conf`");
    OptionBuilder.withLongOpt("config-file");
    conf = OptionBuilder.create(CONFIG);
    
    OptionBuilder.withArgName("src-dir");
    OptionBuilder.withLongOpt("source-directory");
    OptionBuilder.hasArg();
    OptionBuilder.withDescription("use given path as the source directory to be analyzed.");
    sourceDir = OptionBuilder.create(SRC);
    
    OptionBuilder.withArgName("source-file-extensions");
    OptionBuilder.withLongOpt("source-file-extensions");
    OptionBuilder.hasArgs();
    OptionBuilder.withDescription("use given file extensions to search for source code file. "
        + "Default: .java. NOTE: start each file extension with a dot (.), e.g. `.java`, `.scala`.");
    fileExtensions = OptionBuilder.create(FILE_EXTNS);
    
    options.addOption(conf);
    options.addOption(sourceDir);
    options.addOption(help);
    options.addOption(fileExtensions);
    
    return this;
  }
  
  public CommandLine parse(String[] args) {
    CommandLineParser parser = new BasicParser();
    CommandLine cmd = null;
    try {
      cmd = parser.parse(this.options, args);
    } catch (ParseException e) {
      System.err.println(e.getLocalizedMessage());
      printHelp();
      System.exit(0);
    }
    return cmd;
  }
  
  public void printHelp() {
    HelpFormatter formatter = new HelpFormatter();
    formatter.setWidth(100);
    formatter.printHelp("java -cp .:<dependencies> "
        + "org.spideruci.analysis.statik.sourcecode.SourceSurfer", options );
  }
  

}
