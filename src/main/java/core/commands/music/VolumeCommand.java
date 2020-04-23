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
import java.util.List;

/**
 *  VolumeCommand class of the EchoedCore project
 *  On call, displays the current volume or changes it
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class VolumeCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("VOLUME");

        Member author = mre.getMember();

        if (author != null) {
            if (EchoedCore.getMusicUtils().isInVoiceChannel(author)) {
                if (args.length == 1) {
                    // Just asking for current volume
                    mre.getChannel().sendMessage("Volume: " + EchoedCore.getMusicUtils().getVolume(mre.getGuild())).queue();
                }
                else if (args.length == 2) {
                    // Setting new volume
                    int newVolume;
                    try {
                        newVolume = Integer.parseInt(args[1]);
                        if (newVolume < 0 || newVolume > 100) {
                            mre.getChannel().sendMessage("Invalid Number. Please only use numbers 0-100").queue();
                            return;
                        }
                    }
                    catch (NumberFormatException ex) {
                        mre.getChannel().sendMessage("Invalid Number. Please only use numbers 0-100").queue();
                        return;
                    }
                    // Set volume
                    EchoedCore.getMusicUtils().setVolume(mre.getGuild(), newVolume);
                }
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
        return Arrays.asList("volume", "vol", "setvolume", "setvol");
    }

    @Override
    public String getDescription() {
        return "Sends current volume settings or changes to a given volume";
    }

    @Override
    public String getName() {
        return "Volume Command";
    }

    @Override
    public List<String> getUsage() {
        return Arrays.asList(
                "`" + EchoedCore.getConfig().getPrefix() + "volume` **OR** ",
                "`" + EchoedCore.getConfig().getPrefix() + "volume`"
        );
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
