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
package utilities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.managers.AudioManager;
import utilities.*;

import java.util.*;
public class MusicUtilities {

    private static Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager playerManager;

    /**
     * Constructor for the MusicUtils class.
     * Useful because the player needs one instance.
     *
     * @param musicManagers guild music managers
     * @param playerManager player manager
     */
    public MusicUtilities(Map<Long, GuildMusicManager> musicManagers, AudioPlayerManager playerManager) {
        MusicUtilities.musicManagers = musicManagers;
        this.playerManager = playerManager;
    }

    /**
     * Loads a track and plays it
     * @param channel Channel requested from
     * @param trackUrl Song to load
     * @param author Author who requested song
     * @param utils MusicUtils instance
     * @param first true if it should override queue
     */
    public void loadAndPlay(TextChannel channel, String trackUrl, Member author, MusicUtilities utils, boolean first) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        playerManager.loadItemOrdered(musicManager, trackUrl, new MusicHandler(channel, trackUrl, author, utils, first));
    }

    /**
     *
     * @param guild Guild to play in
     * @param track Track to play
     * @param author Author who requested song
     * @param first true if it overrides queue
     * @return int based on end state
     */
    public int play(Guild guild, AudioTrack track, Member author, boolean first) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        int connect = connectToVoiceChannel(guild.getAudioManager(), author);
        if (connect == Constants.VOICE_CONNECT_SUCCESS) {
            musicManager.scheduler.queue(track, first);
            return connect;
        }
        return connect;
    }

    /**
     * Skips current track
     * @param channel Channel request was sent from
     */
    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        if(musicManager.scheduler.getCurrentlyPlaying() != null) {
            channel.sendMessage("Skipped to next track: " + getTrackTitle(channel.getGuild())).queue();
        }
        else {
            channel.sendMessage("End of Queue! Stopping player").queue();
        }
    }

    /**
     * Gets the tracks title
     * @param guild Guild the request was sent from
     * @return String title of the track
     */
    public String getTrackTitle(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        return musicManager.scheduler.getCurrentlyPlaying().getInfo().title;
    }

    /**
     * Connects Bot to voice channel
     * @param audioManager Audio managed
     * @param author Author who requested
     * @return int based on end state
     */
    @SuppressWarnings("ConstantConditions")
    private int connectToVoiceChannel(AudioManager audioManager, Member author) {
        if (isInVoiceChannel(author)) {
            try {
                VoiceChannel vc = author.getVoiceState().getChannel();
                audioManager.openAudioConnection(vc);
                return Constants.VOICE_CONNECT_SUCCESS;
            }
            catch (PermissionException pe) {
                if (pe.getPermission() == Permission.VOICE_CONNECT) {
                    return Constants.VOICE_CONNECT_NO_PERMS;
                }
                return Constants.VOICE_CONNECT_FAIL_OTHER;
            }
        }
        else return Constants.VOICE_CONNECT_NOT_IN_CHANNEL;
    }

    /**
     * Checks if author is in a voice channel
     * @param author to check
     * @return true if in voice channel, false if not
     */
    @SuppressWarnings("ConstantConditions")
    public boolean isInVoiceChannel(Member author) {
        VoiceChannel vc = author.getVoiceState().getChannel();
        return vc != null;
    }

    /**
     * Pauses the player
     * @param guild Guild request was sent from
     */
    public void pause(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.pause();
    }

    /**
     * Unpause the player
     * @param guild Guild request was from
     */
    public void continuePlaying(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.continuePlaying();
    }

    /**
     * Determine if player is paused
     * @param channel Channel request is from
     * @return true if paused, false if not
     */
    public boolean isPaused(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        return musicManager.scheduler.isPaused();
    }

    /**
     * Display the queue
     * @param channel Channel to display in
     * @param page Page of queue
     */
    public void displayQueue(TextChannel channel, int page) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        List<AudioTrack> list = new ArrayList<>();

        // Start embed
        EmbedBuilder embed = new EmbedBuilder();
        MessageUtilities.addEmbedDefaults(embed);
        embed.setTitle("Queue [10 Tracks] / Page " + page);

        musicManager.scheduler.getQueueAsList(list);

        String audioTrack;

        if (page == 1) {
            // add current song
            audioTrack = String.format("Duration: [%s / %s]",
                    getTimestamp(musicManager.scheduler.getCurrentlyPlaying().getPosition()),
                    getTimestamp(musicManager.scheduler.getCurrentlyPlaying().getDuration()));
            embed.addField(musicManager.scheduler.getCurrentlyPlaying().getInfo().title, audioTrack, false);
        }

        boolean sendEmbed = true;

        for (int i = (page - 1) * 10; i < ((page - 1 ) * 10) + 9; i++) {
            // Add 9 depending on the page
            try {
                audioTrack = String.format("Duration: [%s]", getTimestamp(list.get(i).getDuration()));
                embed.addField(list.get(i).getInfo().title, audioTrack, false);
            }
            catch (IndexOutOfBoundsException iobe) {
                if (i == (page - 1) * 10) {
                    channel.sendMessage("No tracks on this page!").queue();
                    sendEmbed = false;
                }
                break;
            }
        }

        // send embed if not empty
        if (sendEmbed) channel.sendMessage(embed.build()).queue();
    }

    /**
     * Gets audio player from the Guild
     * @param guild Guild to retrieve player from
     * @return Player
     */
    private GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager;

        musicManager = musicManagers.get(guildId);
        if(musicManager == null) {
            musicManager = new GuildMusicManager(this.playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    private static String getTimestamp(long milliseconds)
    {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if (hours > 0)
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else
            return String.format("%02d:%02d", minutes, seconds);
    }



}