# servermc-maven-plugin
<p align="center">
  <img alt="License: Apache 2.0" src="https://img.shields.io/github/license/dederobert/servermc-maven-plugin">
</p>

This project is a maven plugin which helps you during Minecraft server plugin development. It can creates and starts a minecraft server with your plugin. 

## How to use it ?

You can use this plugin with maven commands or by adding it to your project.

### Command

The plugin proposes 4 maven goals, they are used as follow:
- To install the server: `mvn fr.lehtto.maven.plugins:servermc-maven-plugin:VERSION:install`
- To copy your plugin into sever files: `mvn fr.lehtto.maven.plugins:servermc-maven-plugin:VERSION:copy-plugins`
- To start the server: `mvn fr.lehtto.maven.plugins:servermc-maven-plugin:VERSION:start`
- To clean the server directory (delete it): `mvn fr.lehtto.maven.plugins:servermc-maven-plugin:VERSION:clean`

> It is possible to shorten the command by adding `fr.lehtto.maven.plugins` to your plugin groups configuration. In the maven settings.xml:
> ```xml
>  <pluginGroups>
>    <pluginGroup>fr.lehtto.maven.plugins</pluginGroup>
>    ...
>  </pluginGroups>
>  ```
> Now the command is `mvn servermc:GOAL` (maven will use the latest available version automatically)

### Configuration

You can configure your project to use this plugin by updating the `pom.xml`

```xml
<build>
  <plugins>
    <plugin>
      <groupId>fr.lehtto.maven.plugins</groupId>
      <artifactId>servermc-maven-plugin</artifactId>
      <version>VERSION</version>
      <configuration>
        ....
      </configuration>
    </plugin>
  </plugins>
</build>
```

## Supported minecraft servers

Currently the plugin supports only PaperMC server.

## Goals
### Install `mvn servermc:install`

This goal's purpose is to prepare the server to be run. Following actions are performed sequentially:
- Checks the EULA aggreement
- Creates server directory, if it doesn't exist
- Retrieves the server JAR, if it doesn't exist (or if it has to be overriden), and check its checksum
- Creates `server.properties` with specified properties, if it doesn't exist (or if it has to be overriden)

#### Configurations
The goal behavior can be fine-tune with following configurations:
| parameter | type | optional | default value | description |
| ---       | ---  | ---      | ---           | ---         |
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran |
| mcVersion | string | no | | Version of Minecraft |
| eula | boolean | no | | Aggreement of Minecraft EULA |
| baseUrl | URL | yes | `https://api.papermc.io/v2/minecraft/paper` | The base URL of API used to fetch server JAR |
| overrideJar | boolean | yes | true | Download and replace the server JAR file even if it exists |
| overrideProperties | boolean | yes | true | Clear the `server.properties` and override it with the provided properties |
| buildNumber | int | yes | *computed* | The server JAR's build number to use |
| properties | map<string, string> | yes | | The map of properties used to initialize the `server.properties` |

### Copy plugins `mvn servermc:copy-plugins`

This goal's purpose is to copy the minecraft plugin you are working on in the server `plugins` folder. It can also copy additional plugins.

#### Configurations
The goal behavior can be fine-tune with following configurations:
| parameter | type | optional | default value | description |
| ---       | ---  | ---      | ---           | ---         |
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran |
| skipAdditionalPlugins | boolean | yes | false | Should the additional plugins copy skipped |
| additionalPlugins | list<plugin> | yes | | The list of additional plugins to copy |

#### Additional plugins
To specify additional plugins to copy you can:
- Use a file path (**not recommanded**):
```xml
<configuration>
  <additionalPlugins>
    <plugin>
      <name>PLUGIN NAME</name>
      <file>FILE PATH (ABSOLUTE OR RELATIVE)</file>
    </plugin>
  </additionalPlugins>
</configuration>
```

- Use a URL to download from:
```xml
<configuration>
  <additionalPlugins>
    <plugin>
      <name>PLUGIN NAME</name>
      <url>URL</url>
      <sha256>SHA256 CHECKSUM (OPTIONAL)</sha256>
      <md5>MD5 CHECKSUM (OPTIONAL)</md5>
    </plugin>
  </additionalPlugins>
</configuration>
```

- Specify a maven dependency artifact to copy:
```xml
<configuration>
  <additionalPlugins>
    <plugin>
      <name>PLUGIN NAME</name>
      <groupId>DEPENDENCY GROUP ID</groupId>
      <artifactId>DEPENDENCY ARTIFACT ID</artifactId>
    </plugin>
  </additionalPlugins>
</configuration>
```

### Start `mvn servermc:start`
This goal's purpose is to start the minecraft server

#### Configurations
The goal behavior can be fine-tune with following configurations:
| parameter | type | optional | default value | description |
| ---       | ---  | ---      | ---           | ---         |
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran |
| minimumAllocationPoolSize | int | yes | 1 | Minimum size (in Go) of the memory allocation pool |
| maximumAllocationPoolSize | int | yes | 2 | Maximum size (in Go) of the memory allocation pool |

### Clean `mvn servermc:clean`
This goal's purpose is to clean the server folder, 

#### Configurations
The goal behavior can be fine-tune with following configurations:
| parameter | type | optional | default value | description |
| ---       | ---  | ---      | ---           | ---         |
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran |
| ignoredWorlds | boolean | yes | false | Is the worlds should keep |

## Upcomming features
- Adds support for other Minecraft server (e.g. spigot)
- Adds configurations to clean goal to filter what is deleted
- Allows debuging of developped plugin
