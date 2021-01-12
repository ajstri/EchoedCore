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
 *  QueueCommand class of the EchoedDungeons project
 *  On call, displays the song queue
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class QueueCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.getLog().info("QUEUE (called by " + mre.getAuthor().getAsTag() + ")");

        Member author = mre.getMember();
        if (author != null) {
            // Check if in a voice channel
            if (EchoedCore.getMusicUtils().isInVoiceChannel(author)) {
                if (args.length == 1) {
                    // No page number, display first
                    EchoedCore.getLog().info("Displaying queue");
                    EchoedCore.getMusicUtils().displayQueue(mre.getTextChannel(), 1);
                }
                else {
                    int page = 1;
                    try {
                        // Set page to argument
                        page = Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException nfe) {
                        mre.getChannel().sendMessage("If you want to visit a page in the queue, please use an integer.").queue();
                    }
                    // Actually display queue
                    EchoedCore.getMusicUtils().displayQueue(mre.getTextChannel(), page);
                }
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            // Author's member is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("queue", "q");
    }

    @Override
    public String getModule() {
        return Modules.MUSIC;
    }

    @Override
    public String getDescription() {
        return "Displays the queue of the music player";
    }

    @Override
    public String getName() {
        return "Queue Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList("`" + EchoedCore.getConfig().getPrefix() + "queue (page number)`");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }
}
