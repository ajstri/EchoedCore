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
package core.listeners;

import core.EchoedCore;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * TagListener class of the EchoedCore project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class TagListener extends ListenerAdapter {

    /**
     * Checks if a user is asking for the bot's prefix in a message event.
     * If a user is, sends a message containing the prefix.
     *
     * @param event MessageReceivedEvent
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    public void onMessageReceived(MessageReceivedEvent event) {
        User botMention = EchoedCore.getApi().getSelfUser();
        String args;

        // Check if message or author is null
        if (!event.getMessage().equals(null) && event.getAuthor() != null) {
            // Check if message mentions the bot
            if (event.getMessage().getMentionedUsers().contains(botMention)) {

                args = event.getMessage().getContentRaw().replace(botMention.getAsMention(), "");

                // Check if they are asking for prefix
                if (args.contains("prefix")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", the prefix is " + EchoedCore.getConfig().getPrefix()).queue();
                }
            }
        }
    }

}
