[license]: https://img.shields.io/badge/License-Apache%202.0-lightgrey.svg
[issues]: https://img.shields.io/github/issues/ajstri/EchoedCore.svg 
[issues-link]: https://github.com/ajstri/EchoedCore/issues

[ ![license][] ](https://github.com/ajstri/EchoedCore/blob/master/LICENSE)
[ ![issues][] ][issues-link]

# EchoedCore - Framework for Discord Bots in Java
 A simple-to-use Discord Bot framework written in Java using [JDA](https://github.com/DV8FromTheWorld/JDA) and [LavaPlayer](https://github.com/sedmelluq/lavaplayer)

# Features
#### **Command framework**
Commands can be easily created and defined using [Command](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/Command.java).
The [Help Command](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/HelpCommand.java)
automatically registers developer-defined commands for further use in its methods.

To register a set of Commands, assuming a new instance of [EchoedCore](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/EchoedCore.java) called `main`:

```java_holder_method_tree
    List<Command> commandList = new Arrays.asList(
            new YourCommand1();
            new YourCommand2();
        );

    main.registerCommands(commandList);
```

#### **Pre-made basic Music Commands**
Included in this framework is a list of default Music Commands:
- [NowPlayingCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/NowPlayingCommand.java)
- [PauseCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/PauseCommand.java)
- [PlayCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/PlayCommand.java)
- [QueueCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/QueueCommand.java)
- [SkipCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/SkipCommand.java)
- [StopCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/StopCommand.java)
- [VolumeCommand](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/commands/music/VolumeCommand.java)

These are not included in the user-defined Bot instance, however, it is simple to include into your project:

Assuming a new instance of [EchoedCore](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/core/EchoedCore.java) called `main`:

`main.enableDefaultMusicCommands();`

They are meant to be a comprehensive set of commands that the developer can use without needing to create their own, but is not mandatory to use.

# Usage

Creating a Bot is easy. In your main method:
```java_holder_method_tree
    // Starts the Bot.
    main.startup();
```
Failing on the very first run is normal. On this run, the [Configuration](https://github.com/ajstri/EchoedCore/blob/master/src/main/java/configuration/Configuration.java)
generates, but is not usable in its default state. After the first run, edit the generated `config.json` file
to include your bot's actual token, replacing the default value.
