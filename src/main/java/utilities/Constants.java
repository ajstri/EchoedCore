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
package utilities;

/**
 * Constants used by the EchoedCore project
 */
@SuppressWarnings("unused")
public class Constants {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final int STATUS_FRIENDLY = 0;
    public static final int STATUS_NO_EVENT = -1;
    public static final int STATUS_NO_JDA = -2;
    public static final int STATUS_NO_CONFIG = -3;
    public static final int STATUS_CONFIG_UNUSABLE = -4;
    public static final int STATUS_UNABLE_TO_CONNECT = -5;

    public static final String DEBUG_KEY = "debug_mode";
    public static final String TOKEN_KEY = "token";
    public static final String PREFIX_KEY = "prefix";
    public static final String GAME_STATUS_KEY = "game_status";
    public static final String SHARDS_KEY = "shards";

    public static final String DEBUG_VALUE = "true";
    public static final String TOKEN_VALUE = "Place your bot's token here";
    public static final String PREFIX_VALUE = "/";
    public static final String GAME_STATUS_VALUE = "with my friends";
    public static final String SHARDS_VALUE = "0";

    public static final String VERSION = "0";
    public static final String BUILD_NUMBER = "0.2S";
    public static final String JVM = System.getProperty("java.version");

    public static final String stagePreInit = "PRE-INITIALIZATION";
    public static final String stageInit = "INITIALIZATION";
    public static final String stagePostInit = "POST-INITIALIZATION";
    public static final String stageShutdown = "SHUTDOWN";
    public static final String stageCommand = "COMMAND CALL";

    public static final int WRITE_TO_FILE_SUCCESS = 1;
    public static final int WRITE_TO_FILE_FAIL = -1;

    public static final int VOICE_CONNECT_SUCCESS = 1;
    public static final int VOICE_CONNECT_NOT_IN_CHANNEL = -1;
    public static final int VOICE_CONNECT_NO_PERMS = -2;
    public static final int VOICE_CONNECT_FAIL_OTHER = -3;

}