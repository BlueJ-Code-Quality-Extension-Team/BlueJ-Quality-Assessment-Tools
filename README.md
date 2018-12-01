### BlueJ-Quality-Assessment-Tools
A BlueJ extension that manages multiple quality assessment tools

### Repository URL
- https://github.com/BlueJ-Code-Quality-Extension-Team/BlueJ-Quality-Assessment-Tools

### Installation Instructions
Copy the jar file quality-tools-extension-0.0.X.jar to the bluej extension directory (BlueJ/lib/extensions)

#### PMD
For default functionality unzip the desired pmd-bin to the BlueJ lib directory (BlueJ/lib) into a folder named pmd-bin-\*.

The pmd executable files (pmd.bat and run.sh) should be located in the BlueJ folder at %BlueJRoot%/lib/pmd-bin-\*/bin/ after extraction

To use an installation other than the default point under BlueJ's preferences set Path to PMD to the absolute path of the PMD installation file (pmd.bar or run.sh).

Checks can also be set or modified. You can set the checks in BlueJ's preferences, either by typing the desired PMD built-in catagory, or entering the absolute path of a single XML file with the desired checks and rules.

### Configuration / System Requirements
- BlueJ 4.1.4 | https://www.bluej.org/

### Development Environment
- Java 8
- JUnit 4
- Git with GitHub for version control
- Developers maintain fork of upstream. Pull requests are squashed and merged to upstream after review
- **(what IDE if any?)**
- **(what command line dev tool if any? ant?)**
- **(code format guideline - checkstyle Sun conventions? Google conventions?)**
- **(code quality tools - test coverage, style checks, JavaDoc API documentation generation)**

### Project Structure
- folder structure including production source location, test source location, libraries location

### Team Members
- James Niedfeldt | jniedfe1@msudenver.edu
- Jon Bowen | jbowen10@msudenver.edu
- Sky Liu | tliu1@msudenver.edu
- Travis Rebhan | trebhan@msudenver.edu
