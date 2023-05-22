# Stryker Dashboard reporter for PIT 

[![Build status](https://github.com/mthmulders/pit-stryker-dashboard-reporter/actions/workflows/main.yml/badge.svg)](https://github.com/mthmulders/pit-stryker-dashboard-reporter/actions/workflows/main.yml)
[![SonarCloud quality gate](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=alert_status)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![SonarCloud vulnerability count](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![SonarCloud technical debt](https://sonarcloud.io/api/project_badges/measure?project=mthmulders_pit-stryker-dashboard-reporter&metric=sqale_index)](https://sonarcloud.io/dashboard?id=mthmulders_pit-stryker-dashboard-reporter)
[![Mutation testing badge](https://img.shields.io/endpoint?style=flat&url=https%3A%2F%2Fbadge-api.stryker-mutator.io%2Fgithub.com%2Fmthmulders%2Fpit-stryker-dashboard-reporter%2Fmain)](https://dashboard.stryker-mutator.io/reports/github.com/mthmulders/pit-stryker-dashboard-reporter/main)
[![Maven Central](https://img.shields.io/maven-central/v/it.mulders.stryker/pit-dashboard-reporter.svg?color=brightgreen&label=Maven%20Central)](https://search.maven.org/artifact/it.mulders.stryker/pit-dashboard-reporter)

A PIT reporter that publishes your mutation testing results to the [Stryker Mutator Dashboard](https://dashboard.stryker-mutator.io/).

## Setup
Setup is relatively easy:
1. make sure there is an environment variable `STRYKER_DASHBOARD_API_KEY` with the API key that you got when you set up your project

The reporter will autoconfigure itself, given you work in any of the following environments:
- GitHub Actions

If your builds run in another environment, please feel free to [open an issue](https://github.com/mthmulders/pit-stryker-dashboard-reporter/issues/new).

When uploading the report fails, this reporter will make the whole build fail.
If this happens and you want to open an issue, please make sure to run again with the `verbose` switch for PIT.
Note that this will also log the last three characters of your Stryker Dashboard API key.

## Usage (Maven)
1. Find the place in your **pom.xml** where you define the _pitest-maven_ plugin.
2. Add a dependency to this plugin declaration:
    ```xml
    <dependency>
       <groupId>it.mulders.stryker</groupId>
       <artifactId>pit-dashboard-reporter</artifactId>
       <version>0.2.1</version>
    </dependency>
    ```
3. Configure PIT to use the new output format:
    ```xml
    <configuration>
        <outputFormats>
            <format>stryker-dashboard</format>
        </outputFormats>
    </configuration>
    ```
   1. Alternatively, if `<configuration>` is already there, add the `<outputFormats>`.
   2. Similarly, if `<outputFormats>` is already there, add (or replace) with `<format>stryker-dashboard</format>`.
4. **Important** If you are working on a multi-module Maven project, add the following to the `<configuration>` block:
   ```xml
   <pluginConfiguration>
       <stryker.moduleName>${project.artifactId}</stryker.moduleName>
   </pluginConfiguration>
   ```
   This will ensure the mutation testing results of the various Maven modules will not mix up in the report.
   You **should not** do this if you have a single-module Maven project!

## Usage (Gradle)
1. Find the place in your **build.gradle** where you declare your dependencies.
2. Add a dependency to this plugin declaration:
    ```groovy
   pitest it.mulders.stryker:pit-dashboard-reporter:0.2.1
    ```
3. Configure PIT to use the new output format:
    ```groovy
   apply plugin: 'info.solidsoft.pitest'
   
   pitest {
       outputFormats = ['stryker-dashboard']
   }
    ``` 
   1. Alternatively, if `pitest` is already there, add the  `outputFormats` line
   2. Similarly, if `outputFormats` is already there, append the existing array.
4. **Important** If you are working on a multi-module Gradle project, add the following to the `pitest` block **for each subproject**:
   Make sure to do this **inside** a `subprojects` block, like so:
   ```groovy
   subprojects {
     // apply plugin: 'info.solidsoft.pitest'

     pluginManager.withPlugin('info.solidsoft.pitest') {
       pluginConfiguration = [ "stryker.moduleName": project.name ]
     }
   }
   ```
   This will ensure the mutation testing results of the various Gradle modules will not mix up in the report.
   You **should not** do this if you have a single-module Gradle project!

## Versions
In the table below you will find which version of this reporter you can use for a given version of Pitest.

| Pitest version   | Reporter version |
|------------------|------------------|
| 1.14.0 and lower | v0.1.5           |
| 1.14.0 and up    | v0.2.1 or higher |

## License
This project is licensed under the MIT license.
See the [LICENSE](./LICENSE) file for the full text of the license.

## Credits
Credit where credit is due: this reporter would not exist without the [tremendous work of ](https://github.com/wmaarts/pitest-mutation-testing-elements-plugin) by Wouter Aarts.
His reporter contains all the complex mapping from PIT results to [mutation-testing-elements' JSON format](https://github.com/stryker-mutator/mutation-testing-elements/tree/master/packages/report-schema).
