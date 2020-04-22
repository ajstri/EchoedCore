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
package utilities.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.EchoedCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utilities.Constants;
import utilities.MessageUtilities;

/**
 * MusicHandler class of the EchoedCore project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class MusicHandler implements AudioLoadResultHandler {

    private final MessageReceivedEvent mre;
    private final String trackUrl;
    private final boolean first;

    public MusicHandler(MessageReceivedEvent mre, String trackUrl, boolean first) {
        this.mre = mre;
        this.trackUrl = trackUrl;
        this.first = first;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        if (mre.getMember() != null) {
            if (EchoedCore.getMusicUtils().isInVoiceChannel(mre.getMember())) {
                //channel.sendMessage("Adding to queue " + audioTrack.getInfo().title).queue();
                EmbedBuilder embed = new EmbedBuilder();
                MessageUtilities.addEmbedDefaults(embed);
                MessageUtilities.addEmbedAuthor(embed, mre.getMember().getUser());

                embed.addField("Adding to queue", audioTrack.getInfo().title, false);
                mre.getChannel().sendMessage(embed.build()).queue();

                int connect = EchoedCore.getMusicUtils().play(mre.getGuild(), audioTrack, mre.getMember(), first);

                if (connect == Constants.VOICE_CONNECT_NO_PERMS) {
                    mre.getChannel().sendMessage("I don't have permission to join your voice channel!").queue();
                }
                else if (connect == Constants.VOICE_CONNECT_FAIL_OTHER) {
                    mre.getChannel().sendMessage("Uh oh! Something went wrong...").queue();
                }
            }
        }
        else {
            mre.getChannel().sendMessage("Uh oh! Something went wrong...").queue();
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        // Send message regarding queue
        mre.getChannel().sendMessage(
                "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist **"
                + playlist.getName()
                + "** with " + playlist.getTracks().size()
                + " tracks)"
        ).queue();

        // Add tracks
        for (AudioTrack audio : playlist.getTracks()) {
            if (EchoedCore.getMusicUtils().play(mre.getGuild(), audio, mre.getMember(), false) == -1) {
                // Quit if user is not in a voice channel
                mre.getChannel().sendMessage("You are not in a voice channel!").queue();
                return;
            }
        }
    }

    @Override
    public void noMatches() {
        mre.getChannel().sendMessage("Nothing found by " + trackUrl).queue();
    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
