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
 *  StopCommand class of the EchoedCore project
 *  On call, stops the player completely
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class StopCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("STOP");

        Member author = mre.getMember();
        if (author != null) {
            // Check if in a voice channel
            if (EchoedCore.getMusicUtils().isInVoiceChannel(mre.getMember())) {
                // Stop the player
                EchoedCore.getMusicUtils().stopPlayer(mre.getGuild());
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // Member is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong!").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("stop", "st", "leave");
    }

    @Override
    public String getDescription() {
        return "Stops the player";
    }

    @Override
    public String getName() {
        return "Stop Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(EchoedCore.getConfig().getPrefix() + "stop");
    }

    /*@Override
    public boolean getDefaultPermission() {
        return true;
    }*/

    @Override
    public String getModule() {
        return Modules.MUSIC;
    }
}
