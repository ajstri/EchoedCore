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

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import utilities.Constants;

public class MusicHandler implements AudioLoadResultHandler {

    private final TextChannel channel;
    private final String trackUrl;
    private final Member author;
    private final MusicUtilities utils;
    private final boolean first;

    public MusicHandler(TextChannel channel, String trackUrl, Member author, MusicUtilities utils, boolean first) {
        this.channel = channel;
        this.trackUrl = trackUrl;
        this.author = author;
        this.utils = utils;
        this.first = first;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        if (utils.isInVoiceChannel(author)) {
            channel.sendMessage("Adding to queue " + audioTrack.getInfo().title).queue();


            int connect = utils.play(channel.getGuild(), audioTrack, author, first);

            if (connect == Constants.VOICE_CONNECT_NO_PERMS) {
                channel.sendMessage("I don't have permission to join your voice channel!").queue();
            }
            else if (connect == Constants.VOICE_CONNECT_FAIL_OTHER) {
                channel.sendMessage("Uh oh! Something went wrong...").queue();
            }
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        channel.sendMessage(
                "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist "
                + playlist.getName()
                + " with " + playlist.getTracks().size()
                + " tracks)"
        ).queue();

        for (AudioTrack audio : playlist.getTracks()) {
            if (utils.play(channel.getGuild(), audio, author, false) == -1) {
                channel.sendMessage("You are not in a voice channel!").queue();
            }
        }
    }

    @Override
    public void noMatches() {
        channel.sendMessage("Nothing found by " + trackUrl).queue();
    }

    @Override
    public void loadFailed(FriendlyException e) {

    }
}
