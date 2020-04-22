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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class VolumeCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("VOLUME");
        
        if(args.length == 1) {
            mre.getChannel().sendMessage("Volume: " + EchoedCore.getMusicUtils().getVolume(mre.getGuild())).queue();
        }
        else if(args.length==2) {
            int newVolume;
            try {
                newVolume = Integer.parseInt(args[1]);
                if(newVolume < 0 || newVolume > 100) {
                    mre.getChannel().sendMessage("Invalid Number. Please only use numbers 0-100").queue();
                    return;
                }
            }
            catch(NumberFormatException ex) {
                mre.getChannel().sendMessage("Invalid Number. Please only use numbers 0-100").queue();
                return;
            }
            EchoedCore.getMusicUtils().setVolume(mre.getGuild(), newVolume);

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
