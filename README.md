<h1 align="center">Lehtto's maven servermc plugin</h1>
<p align="center">
    <img alt="License: Apache 2.0" src="https://img.shields.io/github/license/dederobert/servermc-maven-plugin">
    <img alt="GitHub release (latest SemVer including pre-releases)" src="https://img.shields.io/github/v/release/dederobert/servermc-maven-plugin?include_prereleases">
    <a href="https://github.com/dederobert/servermc-maven-plugin/actions/workflows/sonar.yml" title="Build & Sonar analysis">
        <img src="https://github.com/dederobert/servermc-maven-plugin/actions/workflows/sonar.yml/badge.svg" alt="Build & Sonar analysis">
    </a>
    <br />
    <br />
    <a href="https://sonarcloud.io/summary/new_code?id=dederobert_servermc-maven-plugin" title="Sonar quality gate">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=dederobert_servermc-maven-plugin&metric=alert_status" alt="Sonar quality gate">
    </a>
    <a href="https://sonarcloud.io/summary/new_code?id=dederobert_servermc-maven-plugin" title="Sonar quality gate">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=dederobert_servermc-maven-plugin&metric=sqale_rating" alt="Sonar maintainability rating">
    </a>
    <a href="https://sonarcloud.io/summary/new_code?id=dederobert_servermc-maven-plugin" title="Sonar quality gate">
        <img src="https://sonarcloud.io/api/project_badges/measure?project=dederobert_servermc-maven-plugin&metric=security_rating" alt="Sonar security rating">
    </a>
    <a href="https://codecov.io/gh/dederobert/servermc-maven-plugin" title="Codecov"> 
        <img src="https://codecov.io/gh/dederobert/servermc-maven-plugin/branch/main/graph/badge.svg?token=FIu5RU7NU2" alt="Codecov"/> 
    </a>
</p>

This project is a maven plugin which helps you during Minecraft server plugin development. It can create and start a minecraft server with your plugin.

## Table of content

- [How to use it ?](#how-to-use-it-)
  - [Command](#command)
  - [Configuration](#configuration)
- [Supported minecraft servers](#supported-minecraft-servers)
- [Goals](#goals)
  - [Install mvn servermc:install](#install-mvn-servermcinstall)
  - [Copy plugins mvn servermc:copy-plugins](#copy-plugins-mvn-servermccopy-plugins)
  - [Start mvn servermc:start](#start-mvn-servermcstart)
  - [Clean mvn servermc:clean](#clean-mvn-servermcclean)
  - [Upcoming features](#upcoming-features)

## How to use it ?

You can use this plugin with maven commands or by adding it to your project.

### Command

The plugin proposes 4 maven goals, they are used as follows:

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

## How to use snapshot version

**Snapshot versions are not recommended for production purpose. They can be used to test upcoming features.**

In order to use snapshot version, you have to add the snapshot repository to your configuration. You have two options:
- Add the repository to the `pom.xml` of your project
- Add the repository to the `settings.xml`

```xml
<repositories>
  <repository>
    <id>OSSRH-SNAPSHOT</id>
    <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
  </repository>
</repositories>
```

## Supported minecraft servers

Currently, the plugin supports only PaperMC server.

## Goals

### Install `mvn servermc:install`

This goal's purpose is to prepare the server to be run. Following actions are performed sequentially:

- Checks the EULA agreement
- Creates server directory, if it doesn't exist
- Retrieves the server JAR, if it doesn't exist (or if it has to be overridden), and check its checksum
- Creates `server.properties` with specified properties, if it doesn't exist (or if it has to be overridden)

#### Configurations

The goal behavior can be fine-tune with following configurations:

| parameter | type | optional | default value | description |
| --- | --- | --- | --- | --- | 
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran | 
| mcVersion | string | no | | Version of Minecraft | 
| eula | boolean | no | | Agreement of Minecraft EULA | 
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
| --- | --- | --- | --- | --- | 
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran | 
| skipAdditionalPlugins | boolean | yes | false | Should the additional plugins copy skipped | 
| additionalPlugins | list<plugin> | yes | | The list of additional plugins to copy |

#### Additional plugins

To specify additional plugins to copy you can:

- Use a file path (**not recommended**):

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

The goal behavior can be fine-tuned with following configurations:

| parameter | type | optional | default value | description                                         |
| --- | --- | --- | --- |-----------------------------------------------------|
| debugPort | int | yes | 5005 | The port to use for remote debugger                 |
| remoteDebug | boolean | yes | false | Prepare the sever to attach a remote debug          |  
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran | 
| minimumAllocationPoolSize | int | yes | 1 | Minimum size (in Go) of the memory allocation pool  | 
| maximumAllocationPoolSize | int | yes | 2 | Maximum size (in Go) of the memory allocation pool  |

### Clean `mvn servermc:clean`

This goal's purpose is to clean the server folder,

#### Configurations

The goal behavior can be fine-tuned with following configurations:

| parameter | type | optional | default value | description | 
| --- | --- | --- | --- | --- | 
| serverDirectory | directory path | yes | target/server | The directory where the server is installed and ran |
| ignoreWorlds | boolean | yes | false | Is the worlds should keep |

## Upcoming features

- Adds support for other Minecraft server (e.g. spigot)
- Adds configurations to clean goal to filter what is deleted
- ~~Allows debugging of developed plugin~~
