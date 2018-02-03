# ColossusGUI

## User Guide
### Download and Run
The executable for the Colosssus GUI can be found on the [releases page](https://github.com/Rtmurase/SFS-Colossus/releases) as a `.jar` file. Download the latest release and place the executable `.jar` file in whichever folder the DAQ data files will be written to.

The GUI requires having at least JRE 1.8 installed; the download that can be found [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).

After downloading and installing a compatible version of Java, the `.jar` can be run as a normal executable or through the terminal or command prompt via the following command:

```
~$ java -jar <filename>
```

## Developer Guide
### Clone and Build
This repository uses Git for version control and GitHub for hosting.
1. [Download and install Git.](https://git-scm.com/downloads)
2. Clone the repository: `git clone https://github.com/Rtmurase/SFS-Colossus`.

This repository is written in Java and uses Gradle for dependency management and build automation.
1. [Download and install the latest version of the JDK&mdash;__not__ the JRE.](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. [Download and install Gradle.](https://gradle.org/install/)
3. Navigate to the main directory (the one with `build.gradle`).
3. Run the Gradle command to build the project (see [the list of Gradle commands](#gradle-commands)).


Once the project has been built, run it as an executable: `java -jar build/libs/ColossusGUI.jar`. __NOTE:__ `ColossusGUI.jar` cannot be run from the `build/libs/` directory since it pulls the DAQ data from files in the working directory.

### "Testing"
To test if the GUI works, there's a simulation available within `src/test` that randomly generates data files for the GUI to read in as if they were from the DAQ itself. Note: the entire project must be built (see [the section on Gradle commands](#gradle-commands)) before the simulation can be run. After building, run the following commands in a separate terminal:
```bash
~$ cd build/classes/java/test
~$ java sim.random
```

The `sim.random` process will be running through the other terminal now, so the original terminal can be used to run the GUI as an executable JAR (as outlined above):
```bash
~$ java -jar build/libs/ColossusGUI.jar
```

To stop running the simulations, interrupt both the `sim.random` and GUI processes via `CTRL-C`.

### Gradle Commands
#### `gradle clean`
Deletes the build directory.

#### `gradle build`
Assembles the project. Bound to the custom `shadowJar`, `handleDependencies`, and `rmtmp` tasks. `shadowJar` assembles a "fat" or "uber" JAR, which is basically just a JAR that contains the compiled bytecode (project class files) and resources packed together with all the project dependencies. `handleDependencies` pulls dependencies from the central Maven repository and copies them over to `libs/`. `rmtmp` removes the `build/tmp/` directory since it only contains manifests from other internal tasks.

#### `gradle post`
Cleans up after the ColossusGUI post-execution. Right now, deletes the `MARCO1` files the GUI generates and the various test files `randomIn` generates.

These commands can all be chained together as well (e.g. `gradle clean build`).

## Collaborators
+ [Ryan Murase](https://github.com/Rtmurase/)
+ [Sumeet Bansal](https://github.com/sumeet-bansal/)
+ [Cyrus Cowley](https://github.com/cyficowley/)