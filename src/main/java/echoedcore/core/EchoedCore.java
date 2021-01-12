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
package echoedcore.core;

import echoedcore.configuration.Configuration;
import echoedcore.core.commands.Command;
import echoedcore.core.commands.HelpCommand;
import echoedcore.core.commands.music.*;
import echoedcore.core.listeners.TagListener;
import echoedcore.utilities.Constants;
import echoedcore.utilities.Logger;
import echoedcore.utilities.music.GuildMusicManager;
import echoedcore.utilities.music.MusicUtilities;
import echoedcore.utilities.music.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * EchoedCore class of the EchoedCore project
 * The very Core of the framework. Contains all commands for
 * instantiating the Bot.
 *
 * @author EchoedAJ
 * @since April 2020
 */
@SuppressWarnings("unused")
public class EchoedCore {
    // jda specific
    private static JDA api;
    private static JDABuilder builder;

    // core specific
    private static final Logger log = new Logger();
    private static Configuration config;
    private static final HelpCommand help = new HelpCommand();
    private static long time = 0;

    // toggleable by user
    private static boolean useDefaultMusicCommands = false;

    // LavaPlayer specific
    private static AudioPlayerManager audioManager;
    private static MusicUtilities utils;
    private static Map<Long, GuildMusicManager> musicManagers;

    public EchoedCore() {
        time = System.currentTimeMillis();
    }

    // ----- Accessible Bot Methods -----

    /**
     * Returns if the Core is using the built-in music
     * functions.
     *
     * @return true if using default music, false if not
     */
    public static boolean usingDefaultMusicCommands() {
        return useDefaultMusicCommands;
    }

    /**
     * Disables the use of the {@link Logger} internally
     *
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore disableInternalLogging() {
        getLog().setLogging(false);
        return this;
    }

    /**
     * Enabled the use of the {@link Logger} internally
     *
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore enableInternalLogging() {
        getLog().setLogging(true);
        return this;
    }

    /**
     * Enables the use of the {@link Configuration} internally
     *
     * Do not enable this if you need to add your own values
     * @return EchoedCore instance
     */
    public EchoedCore enableInternalConfig() {
        config = new Configuration();
        return this;
    }

    /**
     * Registers an external {@link Configuration} for use by the core.
     *
     * This is optional.
     */
    public EchoedCore registerConfiguration(Configuration externalConfig) {
        config = externalConfig;
        return this;
    }

    /**
     * Registers an event listener
     *
     * @param listener Event listener to register
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore registerEventListener(Object... listener) {
        try {
            api.addEventListener(listener);
        }
        catch (Exception e) {
            log.error("Unable to register Event Listeners.", e);
            shutdown(Constants.STATUS_NO_EVENT);
        }
        return this;
    }

    /**
     * Registers a list of commands
     *
     * @param commands Commands to register
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore registerCommands(List<Command> commands) {
        for (Command command: commands) {
            registerCommand(command);
        }
        return this;
    }

    /**
     * Registers a single command
     *
     * @param command Command to register
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore registerCommand(Command command) {
        api.addEventListener(getHelp().registerCommand(command));
        return this;
    }

    /**
     * Enables the default Music functions
     *
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore enableMusicCommands() {
        useDefaultMusicCommands = true;

        // Initialize audio portion
        initMusicPlayer();
        return this;
    }

    /**
     * Starts the Bot
     *
     * @return EchoedCore instance
     */
    @SuppressWarnings("UnusedReturnValue")
    public EchoedCore startup() {
        getLog().welcome();
        debugOnlyInitialization();
        preInitialization();
        initialization();
        postInitialization();
        return this;
    }

    // ----- Internal Methods -----

    /**
     *
     */
    private static void debugOnlyInitialization() {
        if (config.getDebug()) {
            getLog().debug("Welcome to EchoedCore! \n \n", Constants.stagePreInit);
            getLog().debug("Prefix: " + config.getPrefix(), Constants.stagePreInit);
            getLog().debug("Game Status: " + "config.getGameStatus()", Constants.stagePreInit);
            getLog().debug("Debug Status: " + "true", Constants.stagePreInit);
            getLog().debug("Token: " + config.getToken(), Constants.stagePreInit);
        }
    }

    /**
     *
     */
    private static void preInitialization() {
        getLog().debug("Beginning Pre-Initialization.", Constants.stagePreInit);

        time = System.currentTimeMillis();

        builder = JDABuilder.createDefault(config.getToken())
                .setAutoReconnect(true);
        //.setActivity(Activity.watching("time pass by"));
    }

    /**
     *
     */
    private void initialization() {
        getLog().debug("Beginning initialization.", Constants.stageInit);
        // Define the JDA Instance.
        try {
            getLog().debug("Defining JDA instance.", Constants.stageInit);

            if (getConfig().getShards() > 0) {
                // Adding event listeners.
                registerEventListeners();
                // Sharding.
                for (int i = 0; i < getConfig().getShards(); i++)
                {
                    api = builder.useSharding(i, config.getShards())
                            .build();
                    api.awaitReady();
                }
            }
            else {
                api = builder.build();
                registerEventListeners();
                api.awaitReady();
            }
        }
        catch (LoginException le){
            getLog().error("Unable to define the JDA instance.", le);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (InterruptedException ie) {
            getLog().error("Interrupted upon waiting JDA Instance.", ie);
            shutdown(Constants.STATUS_NO_JDA);
        }
        catch (ErrorResponseException ere) {
            getLog().error("Unable to connect.", ere);
            shutdown(Constants.STATUS_UNABLE_TO_CONNECT);
        }

    }

    /**
     *
     */
    private static void postInitialization() {
        getLog().debug("Beginning post-initialization.", Constants.stagePostInit);

        // Set the Bot's ID.
        try {
            getLog().debug("Bot ID: " + getId(), Constants.stagePostInit);
        }
        catch (Exception e) {
            getLog().error("Error retrieving Bot ID. This is not a vital step, but may cause issues later.", e);
        }

        // Set auto-reconnect to true & set game status.
        api.setAutoReconnect(true);
        api.getPresence().setActivity(Activity.watching("time pass by"));
    }

    /**
     * Adds default event listeners.
     */
    private void registerEventListeners() {
        registerEventListener(new TagListener());
    }

    // ----- Music -----

    /**
     *
     */
    private void initMusicPlayer() {
        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioPlayer player = audioManager.createPlayer();

        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicManagers = new HashMap<>();
        utils = new MusicUtilities();

        if (usingDefaultMusicCommands()) {
            List<Command> musicCommands = Arrays.asList(
                    new PauseCommand(),
                    new PlayCommand(),
                    new QueueCommand(),
                    new SkipCommand(),
                    new StopCommand(),
                    new NowPlayingCommand(),
                    new VolumeCommand()
            );
            registerCommands(musicCommands);
        }
    }

    // ----- Getter Methods -----

    /**
     * Retrieve the {@link Configuration} instance
     *
     * @return The {@link Configuration} instance used
     * by the bot
     */
    public static Configuration getConfig() {
        return config;
    }

    /**
     * Retrieve the {@link Logger} instance
     *
     * @return The {@link Logger} instance used
     * by the bot
     */
    public static Logger getLog() {
        return log;
    }

    /**
     * Retrieve the {@link HelpCommand} instance
     *
     * @return The {@link HelpCommand} instance used
     * by the bot
     */
    public static HelpCommand getHelp() {
        return help;
    }

    /**
     * Retrieve the {@link GuildMusicManager}s
     *
     * @return The {@link GuildMusicManager}s
     */
    public static Map<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    /**
     * Retrieve the {@link MusicUtilities} instance
     *
     * @return The {@link MusicUtilities} instance used
     * by the bot
     */
    public static MusicUtilities getMusicUtils() {
        return utils;
    }

    /**
     * Retrieve the {@link AudioPlayerManager} instance
     *
     * @return The {@link AudioPlayerManager} instance used
     * by the bot
     */
    public static AudioPlayerManager getAudioManager() {
        return audioManager;
    }

    /**
     * Retrieve the Bot's ID
     *
     * @return The Bot's ID
     */
    public static String getId() {
        return getApi().getSelfUser().getId();
    }

    /**
     * Retrieve the {@link JDA} instance
     *
     * @return The {@link JDA} instance used
     * by the bot
     */
    public static JDA getApi() {
        return api;
    }

    /**
     * Shutdown the Bot Instance and exit the program
     *
     * @param status Status of the shutdown
     */
    public static void shutdown(int status) {
        System.exit(status);

        long endTime = System.currentTimeMillis();
        long timeActive = endTime - time;

        getLog().info("Active for " + ((timeActive / 1000) / 60) + " minutes. (" + (timeActive / 1000) + " seconds)");
        getLog().info("Beginning shutdown.");

        // Remove event listeners. The Bot can shutdown before these are defined.
        try {
            api.removeEventListener(api.getRegisteredListeners());
        }
        catch (NullPointerException npe) {
            getLog().debug("No Event Listeners to remove.", Constants.stageShutdown);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException ie) {
            getLog().debug("Ignored InterruptedException on shut down.", Constants.stageShutdown);
        }

        if (status != Constants.STATUS_NO_JDA && status != Constants.STATUS_CONFIG_UNUSABLE && status != Constants.STATUS_UNABLE_TO_CONNECT) {
            api.shutdownNow();
        }
        System.exit(status);
    }

}