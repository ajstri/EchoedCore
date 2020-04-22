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
package utilities;

import core.EchoedCore;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * MessageUtilities class of the EchoedCore project
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class MessageUtilities {
    public static void addEmbedDefaults(EmbedBuilder embed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        String timestamp = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);

        // Add defaults.
        embed.setFooter("EchoedCore by EchoedAJ#1840", null);
        embed.addField("", "Try `" + EchoedCore.config.getPrefix() + "help [command]` for more.", false);
        embed.setTimestamp(temporalAccessor);
    }

    /**
     * Adds an author to the embed
     * @param embed EmbedBuilder to add author to
     * @param user Author
     */
    public static void addEmbedAuthor(EmbedBuilder embed, User user) {
        embed.setAuthor(user.getAsMention(), user.getAvatarUrl());
    }

    /**
     * Sends a message to the given channel the Event was
     * triggered in.
     * @param mre Event triggered
     * @param message Message to send
     * @return message to send
     */
    public static Message sendMessage(MessageReceivedEvent mre, Message message) {
        if(mre.isFromType(ChannelType.PRIVATE)) {
            return mre.getPrivateChannel().sendMessage(message).complete();
        }
        else {
            return mre.getTextChannel().sendMessage(message).complete();
        }
    }
}
