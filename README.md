# OT Platform. Software Development Kit
Software development kit for creating custom commands.

## Dependencies

- Spark 2.4.3
- sbt 1.5.8
- scala 2.11.12
- eclipse temurin 1.8.0_312 (formerly known as AdoptOpenJDK)

### Creating plugin
1. Append dispatcher_sdk to `libraryDependencies` with scope __Compile__.

   `libraryDependencies += "dispatcher" % "dispatcher-sdk_2.11" % "sdk_version"  % Compile
   `

   In order to avoid missed dependency publishLocal by sbt this project before.
2. Extend your command class from `ot.dispatcher.sdk.PluginCommand` class and realise
   transform method.
    ```
    MyCommand(sq: SimpleQuery, utils: PluginUtils) extends PluginCommand(sq, utils, Set("from", "to")){
        def transform(_df: DataFrame): DataFrame = {
             /* some dataframe transformations*/
        }
    }
   ```    
3. Create file _commands.properties_ in _resources_ folder.
4. Write your commandname and path to the class into file _commands.properties_:
   `    commandname=mypackage.MyCommand
   `
5. Create file _plugin.conf_ in _resources_ folder.
6. Write required properties `pluginName` and `loglevel` to file _plugin.conf_.

   `pluginName="my-plugin"
   `

   `loglevel = "src/main/resources/loglevel.properties"
   `
7. Create file _loglevel.properties_ in _resources_ folder.
8. Write the required logging level for each of your commands in _loglevel.properties_.
   `MyCommand = "INFO"
   `

10. Build your project by sbt with target __package__.


### Deploying plugin
1. Create folder with name _plugins_ in distributed fs if it is not exist.
2. Create folder with the same name as value of `pluginName` property from _plugin.conf_.
3. Put your .jar file into _somepath/plugins/%pluginName%_ folder.
4. If you need override or append some properties add your custom _plugin.conf_ to
   _somepath/plugins/%pluginName%_ folder.
5. If you using some external libraries in your plugin put them into
   _somepath/plugins/%pluginName%/libs_ folder.
6. Add path to your plugin folder in file _application.conf_ if it not exist.
    ```
    plugins{
        fs = "file:/"
        path = "somepath/plugins"
    }
    ```
7. Restart spark driver.

### Removing plugin
1. Remove _somepath/plugins/%pluginName%_ folder.
2. Restart spark driver.

### Updating plugin
1. Remove the deployed plugin.
2. Deploy new version of the plugin.

### Plugin files structure
Resources folder:
- _plugin.conf_ - contains default values of properties.
  File should contain property `pluginName`.
- _commands.properties_ - contains mapping from command names to classes with command realisation.
- _loglevel.properties_ - contains mapping log levels to simple class names(without package).
  For using you should specify path in `loglevel` property in _plugin.conf_.
- _libs_ - folder with library jars which your plugin depends on.

For deployed plugin you can override any property excluding `pluginName` from default _plugin.conf_ by adding
another _plugin.conf_ file to deployed plugin folder.

### Configuration
If you need some parameters for using in your plugin you should store them in file _plugin.conf_.
Parameters from _application.conf_ are available from val _utils.mainConfig_.
Parameters from _plugin.conf_ are available from val _utils.pluginConfig_.

### Testing
1.  Make your command class extends `ot.dispatcher.sdk.test.CommandTest`  
    and implements  `dataset` value with your input dataset as json string.
    ```
    class MyCommandTest extends CommandTest {
        val dataset = """[{"x":1, "y":2}]"""
          /* some test methods */
    }
    ```
2. Create test method:
    1. Create `SimpleQuery` with args of you command.
    2. Create an instance of your command class using your query and `utils` val from `CommandTest`.
    3. Call `execute` with your command instance as argument. You can use many commands in `execute` call.
    4. Create string with expected value in json format.
    5. Check the result using `assert` with `jsonCompare` method.
    ```
    test("Test 1. Command: mycommand  ") {
        val query = SimpleQuery("""args string""")
        val command = new MyCommand(query, utils)
        val actual = execute(command)
        val expected = """[{"x":3, "y":4}]"""
        assert(jsonCompare(actual, expected), f"Result : $actual\n---\nExpected : $expected")
    }
    ```
3. Create files _plugin.conf_ and _application.conf_ with testing values in `src/test/resources` folder.
4. If you need to write some files for your test, use `src/test/resources/temp` folder for that purpose.
   It will be created before first test running and deleted at the end. Highly recommended
   to create separate folder for each test to avoid tests influence to each other.

## Versioning

We use SemVer for versioning. For the versions available, see the tags on this repository.

## Authors

Dmitriy Gusarov (dgusarov@isgneuro.com)  
Andrey Starchenkov (astarchenkov@isgneuro.com)

## License

[OT.PLATFORM. License agreement.](LICENSE.md)

## Acknowledgments

Nikolay Ryabykh (nryabykh@isgneuro.com)  
Sergei Ermilov (sermilov@isgneuro.com)  
