/*
 *  Copyright 2020 EchoedAJ
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package core;

import utilities.Logger;

public class MainExample {

    private static final EchoedCore main = new EchoedCore();

    public static void main(String[] args) {
        // Start the bot.
        main.startup();

        /* Add an event listener
        main.registerEventListener(new YourListenerHere);
        */

        /* Register a set of commands
        List<Command> commandList = new Arrays.asList(
            new YourCommand1();
            new YourCommand2();
        );

        main.registerCommands(commandList);
         */

        // Enables music commands. This is false by default.
        // This adds default commands:
        // Play, Pause, Queue, Skip
        main.enableMusicCommands();

        // Disable internal logging. The bot logs by default.
        main.disableInternalLogging();
        // You can re-enable it at any time
        main.enableInternalLogging();

        // To create an external logger instance:
        Logger externalLogger = new Logger("BotTitle");
        externalLogger.info("Online!");

        // Change the title
        externalLogger.setTitle("New Bot Title");
    }
}
