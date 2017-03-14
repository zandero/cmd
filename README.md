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

```

