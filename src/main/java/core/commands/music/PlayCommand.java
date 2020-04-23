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
package core.commands.music;

import core.EchoedCore;
import core.commands.Command;
import core.commands.Modules;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  PlayCommand class of the EchoedCore project
 *  On call, plays a given song or restarts the player
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class PlayCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("PLAY");

        Member author = mre.getMember();
        if (author == null) {
            // Somehow the author is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong.").queue();
        }
        // Check if author is in a voice channel
        else if (EchoedCore.getMusicUtils().isInVoiceChannel(author)) {
            if (args.length == 2) {
                // Add song to queue
                EchoedCore.getLog().info("Loading a track: " + args[1]);
                EchoedCore.getMusicUtils().loadAndPlay(mre, args[1], false);
            }
            else if (args.length == 1) {
                if (EchoedCore.getMusicUtils().isPaused(mre.getTextChannel())) {
                    EchoedCore.getMusicUtils().continuePlaying(mre.getGuild());
                }
                else mre.getChannel().sendMessage("Please provide something to play!").queue();
            }
            // possibly overriding queue
            else if (args.length == 3) {
                if (args[2].toLowerCase().contains("first")) {
                    // Play song first
                    EchoedCore.getMusicUtils().loadAndPlay(mre, args[1], true);
                }
            }
        }
        else {
            // Not in voice channel
            mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("play", "pl");
    }

    @Override
    public String getDescription() {
        return "Plays audio from a YouTube URL";
    }

    @Override
    public String getName() {
        return "Play Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                "`" + EchoedCore.getConfig().getPrefix() + getAliases().get(0) + "[OPTIONAL: URL] [OPTIONAL: first]\n"
                        + "Write first if you want to override the queue\n"
                        + "Write your URL if you are adding, ignore this if you are just restarting the player"
        );
    }

    /*@Override
    public boolean getDefaultPermission() {
        return false;
    }*/

    @Override
    public String getModule() {
        return Modules.MUSIC;
    }
}