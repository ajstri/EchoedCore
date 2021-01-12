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
 *  NowPlayingCommand class of the EchoedDungeons project
 *  On call, displays the currently playing song
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class NowPlayingCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("NOW PLAYING");

        Member author = mre.getMember();
        if (author != null) {
            if (EchoedCore.getMusicUtils().isInVoiceChannel(mre.getMember())) {
                // Display song title
                mre.getChannel().sendMessage("Now Playing: " + EchoedCore.getMusicUtils().getTrackTitle(mre.getGuild())).queue();
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // User is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong!").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("nowplaying", "now", "np");
    }

    @Override
    public String getDescription() {
        return "Displays the currently playing song";
    }

    @Override
    public String getName() {
        return "Now Playing Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + "nowplaying`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    @Override
    public String getModule() {
        return Modules.MUSIC;
    }
}
