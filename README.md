# SourceSurfer

I created this simple tool to scan source code directories, to be used in my own research. If you find this useful, feel free to use as you deem fit. I invite any suggestions, improvements, ideas, and pull requests. - Vijay

### Usage

usage: `java -cp .:<dependencies> org.spideruci.analysis.statik.sourcecode.SourceSurfer [cmd line options]`

`-c,--config-file <file>`  
use given file path as the plugin configuration file. Default: `./conf`

`-e,--source-file-extensions <source-file-extensions>`  
use given file extensions to search for source code file. Default: .java. NOTE: start each file extension with a dot (.), e.g. `.java`, `.scala`.

`-h,--help`  
print this message.
 
`-s,--source-directory <src-dir>`  
use given path as the source directory to be analyzed.

E.g. 

`~$ java -cp .:<dependencies> org.spideruci.analysis.statik.sourcecode.SourceSurfer -h` will print the help menu as listed above.

`~$ java -cp .:<dependencies> org.spideruci.analysis.statik.sourcecode.SourceSurfer -s ../src` will surf the source specified source directory (`../src`) and run the plugins declared in the config file.

`~$ java -cp .:<dependencies> org.spideruci.analysis.statik.sourcecode.SourceSurfer -s ../src -e .java .scala` will search for source code written in java and scala. This is basically done with a simple check of the file names being considered for the search.

#### Declaring Plugins

- You can declare plugins as a list of plugin names in a simple text file (check `conf`).
- You can also declare plugins in a couple of different ways with YMAL (check conf.yaml).
- If you do not know YAML stick the simple text files.
- Check conf and conf.yaml to see exactly how plugin should be declared.
- Plugins can be declared as (yaml-)serialized objects; however, this can only be done if the config file is a yaml file.

#### Creating Plugins

- The plugin system in SourceSurfer is event-based. 
- All plugins are event listeners and subscribe to the events being dispatched from the SourceSurfer, to a list of subscribers (plugins).
- You create plugins by extending the `AbstractSourceSurferPlugin` class in the `org.spideruci.analysis.statik.sourcecode.plugins` package.
- You are essentially provided with the following methods:
  - `init(Object arg)`/ `init(Object[] args)`: used to initiate the plugin's field(s) with the supplied `arg`/`args`. This is typically done with the plugin is declared as a key-value pair in the YAML based config file -- where the key is used as the name of the plugin-class, and the value is read in as the object that is passed to the plugin. Unlike the following methods, this is **not** an event.
  - `startListening()`
    - this *event* indicates that the surfing of the source directory is about to begin.
  - `sourceFileDiscovered(SourcefileData filedata)`
    - this *event* indicates that a source-file has been found and is about to be read in.
    - the details of the source-file, available as a `SourcefileData` object, is passed in as an argument.
  - `sourceLineRead(SourceLine line)`
    - this *event* indicates that a line in the source-file that was last discovered has been read, and is passed in as a `SourceLine` object in the arguments.
  - `sourceFileReadingDone()`
    - this *event* indicates that the last discovered source-code file has been read completely.
  - `stopListening()`
    - this *event* indicates that the entire source directory has been surfed (or navigated) and all the discovered files within the directory have been read.
- The above interface of plugin methods were designed to be stateless.
- Once, you have written the plugin, you can declare it in the config file for the SourceSurfer to discover it. 
- The plugin-discovery involves creating a new instance of the plugin via reflection (sometimes with additional arguments like provided in conf.yaml), or the re-creation of an existing yaml-serialized plugin object in the YAML-based config file (see conf.yaml).



### Versions

**v0.1 Event/Hook Based Plugin architecture in place**  

- commit - 7b1cb73f36f997fe200e6ef96616135783ea2876
- SourceSurfer (class) sends out events; Plugins are event listeners and react accordingly.  
- The 'surfer' code is now more-or-less self contained and *not* dependent on any plugins.  
- Plugins allow multiple analyes to be performed on the same SourceSurfing event stream.  
- Check out the `org/spideruci/analysis/statik/sourcecode/plugins` package for two simple plugin examples.
  - For now, create plugins in the plugins package, and list them in the `conf` file (checked in). The program reads the conf file to discover plugins.
  - The conf file is a simple list of plugin names. (Soon to come: YAML support!)

### TODO

- More Documentation about creating and using plugins.
- Generate/Create Javadocs.
- Write test cases.
- Create a single executable.
- Convert to maven project.
- Improved plugin discovery.  
- Explore individual conf files v. Seperate for each plugin.  
- ~~Cmd line argument for source-file extensions.~~
- ~~Documentaion/Help-menu on how to use the program.~~
- ~~Documentation about creating and using plugins.~~
- ~~CLI (command line interface).~~  
- ~~YAML support for conf file(s).~~  
- ~~Add plugin argument support in conf files for plugins.~~  
- ~~Create extensible plugin system.~~

### Dependencies

- Json-IO, for creating JSON dumps. -- [https://code.google.com/p/json-io/](https://code.google.com/p/json-io/)
- Apache Commons CLI, for command line interface support. -- [https://commons.apache.org/proper/commons-cli](https://commons.apache.org/proper/commons-cli)
- YamlBeans, for working with YAML based config files. -- [http://yamlbeans.sourceforge.net/](http://yamlbeans.sourceforge.net/)
