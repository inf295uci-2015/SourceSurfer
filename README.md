# SourceSurfer

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
- Plugins can be decalred as (yaml-)serialized objects; however, this can only be done if the config file is a yaml file.

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

- Write test cases.
- Create a single executable.
- Convert to maven project.
- ~~Cmd line argument for source-file extensions.
- ~~Documentaion/Help-menu on how to use the program.
- Documentation about creating and using plugins.
- Improved plugin discovery.  
- ~~CLI (command line interface).~~  
- ~~YAML support for conf file(s).~~  
- Explore individual conf files v. Seperate for each plugin.  
- ~~Add plugin argument support in conf files for plugins.~~  
- ~~Create extensible plugin system.~~

### Dependencies

- Json-IO, for creating JSON dumps. -- [https://code.google.com/p/json-io/](https://code.google.com/p/json-io/)
- Apache Commons CLI, for command line interface support. -- [https://commons.apache.org/proper/commons-cli](https://commons.apache.org/proper/commons-cli)