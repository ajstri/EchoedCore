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
package echoedcore.utilities;

import java.io.PrintStream;

/**
 * Logger class of the EchoedCore project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class Logger {

    private static final PrintStream err = System.err;
    private static final PrintStream other = System.out;

    private static String title = "[EchoedCore";
    private static boolean log = true;

    public Logger() { }

    public Logger (String newTitle) {
        title = "[" + newTitle;
    }

    public void setTitle(String newTitle) {
        title = "[" + newTitle;
    }

    /**
     * Welcomes user to the Core
     */
    public void welcome() {
        blank("", Constants.ANSI_PURPLE,
                "                                                     \n" +
                        "                     ,---._                                   \n" +
                        "   ,---,           .-- -.' \\                   ,----,        \n" +
                        "  '  .' \\          |    |   :       ,---.    .'   .' \\      \n" +
                        " /  ;    '.        :    :   |      /__./|  ,----,'    |       \n" +
                        ":  :       \\       :        | ,---.;  ; |  |    :  .  ;      \n" +
                        ":  |   /\\   \\      |    :   :/___/ \\  | |  :    |.'  /     \n" +
                        "|  :  ' ;.   :     :         \\   ;  \\ ' |  `----'/  ;       \n" +
                        "|  |  ;/  \\   \\    |    ;   | \\   \\  \\: |    /  ;  /     \n" +
                        "'  :  | \\  \\ ,'___ l           :   \\  ' .   ;  /  /-,      \n" +
                        "|  |  '  '--'/    /\\    J   :   \\   \\   '  /  /  /.`|      \n" +
                        "|  :  :     /  ../  `..-    ,    \\   `  ;./__;      :        \n" +
                        "|  | ,'     \\    \\         ;      :   \\ ||   :    .'       \n" +
                        "`--''        \\    \\      ,'        '---\" :   | .'          \n" +
                        "              \"---....--'                `---'               \n" +
                        "                                                              \n"
        );

        blank("", Constants.ANSI_CYAN, "E  C  H  O  E  D  C  O  R  E  -  V  E  R  S  I  O  N  " + Constants.VERSION);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tVersion: \t" + Constants.VERSION);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tBuild: \t\t" + Constants.BUILD_NUMBER);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tJVM: \t\t" + Constants.JVM + "\n");
        blank("", Constants.ANSI_WHITE, "\n\n\n");
    }

    /**
     * Toggles if internal logging should be used
     *
     * @param logging true if yes, false if no
     */
    public void setLogging(boolean logging) {
        log = logging;
    }

    public static boolean isLogging() {
        return log;
    }

    /**
     * Logs an informational message
     *
     * @param message Message to log
     */
    public void info(String message) {
        if (isLogging()) {
            other.println(Constants.ANSI_BLUE + title + "/INFO] " + message + Constants.ANSI_RESET);
        }
    }

    /**
     * Logs a warning message
     *
     * @param message Message to log
     */
    public void warning(String message) {
        if (isLogging()) {
            other.println(Constants.ANSI_YELLOW + title + "/WARNING] " + message + Constants.ANSI_RESET);
        }
    }

    /**
     * Logs an error message
     *
     * @param message Message to log
     * @param e Exception encountered
     */
    public void error(String message, Exception e) {
        if (isLogging()) {
            err.println(Constants.ANSI_RED + title + "/ERROR] " + message + Constants.ANSI_RESET);

            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Logs a debug message
     *
     * @param message Message to log
     * @param stage Stage of log
     */
    public void debug(String message, String stage) {
        if (isLogging()) {
            other.println(Constants.ANSI_PURPLE + title + "/DEBUG/" + stage + "] " + message + Constants.ANSI_RESET);
        }
    }

    /**
     * Logs a custom message
     *
     * @param message message to log
     * @param header header of message, eg. "[EchoedBot/Utils]"
     * @param color color of message
     */
    public void blank(String header, String color, String message) {
        if (isLogging()) {
            other.println(color + header + message + Constants.ANSI_RESET);
        }
    }
}