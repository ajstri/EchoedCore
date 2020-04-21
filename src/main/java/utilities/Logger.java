/*
    Copyright 2020 EchoedAJ

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at:

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package utilities;

import java.io.PrintStream;

/**
 *
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
        title = newTitle;
    }

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

        blank("", Constants.ANSI_CYAN, "E  C  H  O  _  C  O  R  E  --  V  E  R  S  I  O  N  " + Constants.VERSION);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tVersion: \t" + Constants.VERSION);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tBuild: \t\t" + Constants.BUILD_NUMBER);
        blank("", Constants.ANSI_YELLOW, "\t\t\t\tJVM: \t\t" + Constants.JVM + "\n");
        blank("", Constants.ANSI_WHITE, "\n\n\n");
    }

    public void setLogging(boolean logging) {
        log = logging;
    }

    /**
     *
     * @param message
     */
    public void info(String message) {
        if (log) {
            other.println(Constants.ANSI_BLUE + title + "/INFO] " + message + Constants.ANSI_RESET);
        }
    }

    /**
     *
     * @param message
     */
    public void warning(String message) {
        if (log) {
            other.println(Constants.ANSI_YELLOW + title + "/WARNING]" + message + Constants.ANSI_RESET);
        }
    }

    /**
     *
     * @param message
     * @param e
     */
    public void error(String message, Exception e) {
        if (log) {
            err.println(Constants.ANSI_RED + title + "/ERROR]" + message + Constants.ANSI_RESET);

            System.out.println("Here is the error: \n");
            e.getMessage();
            e.printStackTrace();
        }
    }

    /**
     *
     * @param message
     * @param stage
     */
    public void debug(String message, String stage) {
        if (log) {
            other.println(Constants.ANSI_PURPLE + title + "/DEBUG/" + stage + "] " + message + Constants.ANSI_RESET);
        }
    }

    /**
     * Logs a custom message
     * @param message message to log
     * @param header header of message, eg. "[EchoedBot/Utils]"
     * @param color color of message
     */
    public void blank(String header, String color, String message) {
        other.println(color + header + message + Constants.ANSI_RESET);
    }
}