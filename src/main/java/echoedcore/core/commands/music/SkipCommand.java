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
package echoedcore.core.commands.music;

import echoedcore.core.EchoedCore;
import echoedcore.core.commands.Command;
import echoedcore.core.commands.Modules;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  SkipCommand class of the EchoedDungeons project
 *  On call, skips the current song
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class SkipCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("SKIP (called by " + mre.getAuthor().getAsTag() + ")");

        Member author = mre.getMember();
        if (author != null) {
            if (EchoedCore.getMusicUtils().isInVoiceChannel(mre.getMember())) {
                // Skip track
                EchoedCore.getLog().info("Skipping a track");
                EchoedCore.getMusicUtils().skipTrack(mre.getTextChannel());
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // Member is null
            mre.getChannel().sendMessage("Uh oh! Something went wrong.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("skip", "s");
    }

    @Override
    public String getModule() {
        return Modules.MUSIC;
    }

    @Override
    public String getDescription() {
        return "Skips the currently playing song";
    }

    @Override
    public String getName() {
        return "Skip Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + "skip`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
