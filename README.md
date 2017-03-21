# command line parser
Command line parsing utility, that converts a given command line to a name, value `Map`  
 
## Example
`java -jar MyApp.jar -t 10 -p raw`


Will produce a setting map:
```java
Map<String, Object> settings = new HashMap<>();
settings.put("t", 10);
settings.put("p", "raw");`
```

## Setup
```xml
<dependency>      
     <groupId>com.zandero</groupId>      
     <artifactId>cmd</artifactId>      
     <version>1.0</version>      
</dependency>
```

## Usage

```java
// define command arguments
CommandOption all = new BoolOption("a")
	.longCommand("all")
	.setting("getAll");

CommandOption file = new StringOption("f")
	.longCommand("file")
	.setting("fileName");

CommandOption size = new IntOption("s")
	.longCommand("size")
	.setting("fileSize");

// create builder
CommandBuilder builder = new CommandBuilder();
builder.add(all);
builder.add(file);
builder.add(size);

// create parser
CommandLineParser parser = new CommandLineParser(builder);

```

```java
// use parser to parse input arguments
String[] args = new String[]{"-a", "--file", "/this.file", "-s", "100");
    
Settings out = parser.parse(args);
    
int size = out.get("fileSize");
boolean getAll = out.get("getAll");
Stirng fileName = out.get("fileName");
```

