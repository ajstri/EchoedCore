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

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import configuration.Configuration;
import core.commands.*;
import core.commands.music.*;
import core.listeners.TagListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import utilities.*;
import utilities.music.*;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * EchoedCore class of the EchoedCore project
 * The very Core of the framework. Contains all commands for
 * instantiating the Bot.
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class EchoedCore {
    // jda specific
    private static JDA api;
    private static JDABuilder builder;

    // core specific
    private static final Logger log = new Logger();
    private static final Configuration config = new Configuration();
    private static final HelpCommand help = new HelpCommand();
    private static long time = System.currentTimeMillis();

    // toggleable by user
    private static boolean useDefaultMusicCommands = false;

    // LavaPlayer specific
    private static AudioPlayerManager audioManager;
    private static MusicUtilities utils;
    private static Map<Long, GuildMusicManager> musicManagers;

    public EchoedCore() {}

    // ----- Accessible Bot Methods -----

    public static boolean UsingDefaultMusicCommands() {
        return useDefaultMusicCommands;
    }

    public void disableInternalLogging() {
        getLog().setLogging(false);
    }

    public void enableInternalLogging() {
        getLog().setLogging(true);
    }

    public void registerEventListener(Object... listener) {
        try {
            api.addEventListener(listener);
        }
        catch (Exception e) {
            log.error("Unable to register Event Listeners.", e);
            shutdown(Constants.STATUS_NO_EVENT);
        }
    }

    public static void registerCommands(List<Command> commands) {
        for (Command command: commands) {
            api.addEventListener(getHelp().registerCommand(command));
        }
    }

    public void enableMusicCommands() {
        useDefaultMusicCommands = true;

        // Initialize audio portion
        initMusicPlayer();
    }

    public void startup() {
        getLog().welcome();
        debugOnlyInitialization();
        preInitialization();
        initialization();
        postInitialization();
    }

    // ----- Internal Methods -----

    private static void debugOnlyInitialization() {
        if (config.getDebug()) {
            getLog().debug("Welcome to EchoedCore! \n \n", Constants.stagePreInit);
            getLog().debug("Prefix: " + config.getPrefix(), Constants.stagePreInit);
            getLog().debug("Game Status: " + "config.getGameStatus()", Constants.stagePreInit);
            getLog().debug("Debug Status: " + "true", Constants.stagePreInit);
            getLog().debug("Token: " + config.getToken(), Constants.stagePreInit);
        }
    }

    private static void preInitialization() {
        getLog().debug("Beginning Pre-Initialization.", Constants.stagePreInit);

        time = System.currentTimeMillis();

        builder = JDABuilder.createDefault(config.getToken())
                .setAutoReconnect(true);
        //.setActivity(Activity.watching("time pass by"));
    }

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

    private void initMusicPlayer() {
        audioManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioManager);
        AudioPlayer player = audioManager.createPlayer();

        TrackScheduler trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);

        musicManagers = new HashMap<>();
        utils = new MusicUtilities();

        if (UsingDefaultMusicCommands()) {
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

    public static Configuration getConfig() {
        return config;
    }

    public static Logger getLog() {
        return log;
    }

    public static HelpCommand getHelp() {
        return help;
    }

    public static Map<Long, GuildMusicManager> getMusicManagers() {
        return musicManagers;
    }

    public static MusicUtilities getMusicUtils() {
        return utils;
    }

    public static AudioPlayerManager getAudioManager() {
        return audioManager;
    }

    public static String getId() {
        return getApi().getSelfUser().getId();
    }

    public static JDA getApi() {
        return api;
    }

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
