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
package echoedcore.core.listeners;

import echoedcore.core.EchoedCore;
import echoedcore.utilities.MessageUtilities;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 *  TagListener class of the EchoedDungeons project
 *
 * @author EchoedAJ
 * @since June 2020
 */
public class TagListener extends ListenerAdapter {

    final User botMention = EchoedCore.getApi().getSelfUser();
    String args;

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        // Check if message or author is null
        if (!event.getMessage().equals(null) && event.getAuthor() != null) {
            // Check if message mentions the bot
            if (event.getMessage().getMentionedUsers().contains(botMention)) {

                args = event.getMessage().getContentRaw().replace(botMention.getAsMention(), "");

                // Check if they are asking for prefix
                if (args.contains("prefix")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", the prefix is " + EchoedCore.getConfig().getPrefix()).queue();
                }
                else if (args.contains("info")) {
                    event.getChannel().sendMessage(MessageUtilities.embedCoreInfo().build()).queue();
                }
            }
        }
    }
}
