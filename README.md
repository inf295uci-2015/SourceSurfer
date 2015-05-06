# SourceSurfer

### Usage

```
usage:
 -c,--config-file <file>                                use given file path as the plugin
                                                        configuration file. Default: `./conf`
 -e,--source-file-extensions <source-file-extensions>   use given file extensions to search for
                                                        source code file. Default: .java. NOTE:
                                                        start each file extension with a dot (.),
                                                        e.g. `.java`, `.scala`.
 -h,--help                                              print this message.
 -s,--source-directory <src-dir>                        use given path as the source directory to be
                                                        analyzed.
```


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

- [ ] Write test cases.
- [ ] Create a single executable.
- [ ] Convert to maven project.
- [x] Cmd line argument for source-file extensions
- [x] Documentaion/Help-menu on how to use the program.
- [ ] Documentation about creating and using plugins.
- [ ] Improved plugin discovery.  
- [x] CLI (command line interface).  
- [ ] YAML support for conf file(s).  
- [ ] Explore individual conf files v. Seperate for each plugin.  
- [ ] Add plugin argument support in conf files for plugins.  
- [x] Create extensible plugin system.

### Dependencies

- Json-IO, for creating JSON dumps. -- [https://code.google.com/p/json-io/](https://code.google.com/p/json-io/)
- Apache Commons CLI, for command line interface support. -- [https://commons.apache.org/proper/commons-cli](https://commons.apache.org/proper/commons-cli)