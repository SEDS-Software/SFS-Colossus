# ColossusGUI

## Using Gradle
Gradle's a build automation system that just makes it easier to manage dependencies and build projects.
1. [Install the latest version of the JDK&mdash;__not__ the JRE.](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. [Install Gradle.](https://gradle.org/install/)
3. In the directory with `build.gradle`, run a Gradle command (see [the list of Gradle commands](#gradle-commands)).
4. Once the project has been built, run it as an executable JAR: `java -jar build/libs/ColossusGUI.jar`.

## "Testing"
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

## Gradle Commands
#### `gradle clean`
Deletes the build directory.

#### `gradle build`
Assembles the project. Bound to the custom `shadowJar`, `handleDependencies`, and `rmtmp` tasks. `shadowJar` assembles a "fat" or "uber" JAR, which is basically just a JAR that contains the compiled bytecode (project class files) and resources packed together with all the project dependencies. `handleDependencies` pulls dependencies from the central Maven repository and copies them over to `libs/`. `rmtmp` removes the `build/tmp/` directory since it only contains manifests from other internal tasks.

#### `gradle post`
Cleans up after the ColossusGUI post-execution. Right now, deletes the `MARCO1` files the GUI generates and the various test files `randomIn` generates.

These commands can all be chained together as well (e.g. `gradle clean build`).
