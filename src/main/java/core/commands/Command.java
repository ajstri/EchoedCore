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
package core.commands;

import core.EchoedCore;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utilities.Logger;
import utilities.MessageUtilities;

import java.util.List;

/**
 *  Command class of the EchoedDungeons project
 *
 *  All methods are explained here
 *
 * @author EchoedAJ
 * @since April 2020
 */
public abstract class Command extends ListenerAdapter {

    /**
     * Calls a command
     * @param mre Event triggered
     * @param args Arguments from event trigger
     */
    protected abstract void onCommand(MessageReceivedEvent mre, String[] args);

    /**
     * Returns a list of aliases that the command can be called by.
     * The first one is it's "mainly" used alias.
     * @return list of possible aliases.
     */
    public abstract List<String> getAliases();

    /**
     * Gets the description of the Command.
     * @return description of the Command.
     */
    public abstract String getDescription();

    /**
     * Gets the name of the Command
     * @return name of the Command
     */
    public abstract String getName();

    /**
     * Gets example usage for the Command
     * @return example usage for the Command
     */
    public abstract List<String> getUsage();

    /**
     * Determines default permissions
     * @return true if default, false if not
     */
    public abstract boolean getDefaultPermission();

    /**
     * Called when Message is received visible to the Bot.
     * Determines if there was a command called, and if so,
     * executes the command.
     * @param mre Event triggered.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent mre) {
        if (mre.getAuthor().isBot() && !respondToBots()) {
            return;
        }
        if(commandArgs(mre.getMessage())[0].contains(EchoedCore.config.getPrefix()) && containsCommand(mre.getMessage())) {
            EchoedCore.log.info("Calling a command:");
            onCommand(mre, commandArgs(mre.getMessage()));
        }

    }

    /**
     * Determines if the message contained a command.
     * @param message message to scan
     * @return true if message contained command, false if not.
     */
    private boolean containsCommand(Message message) {
        return getAliases().contains(commandArgs(message)[0].replace(EchoedCore.config.getPrefix(), ""));
    }

    /**
     * Breaks up the message into String[].
     * @param message message to split
     * @return String[] of the split message.
     */
    private String[] commandArgs(Message message) {
        return commandArgs(message.getContentDisplay());
    }

    /**
     * Breaks up the message into String[].
     * @param string message to split
     * @return String[] of the split message.
     */
    private String[] commandArgs(String string) {
        return string.split(" ");
    }

    /**
     * Sends a message to the given channel the Event was
     * triggered in.
     * @param mre Event triggered
     * @param message Message to send
     * @return message to send
     */
    @SuppressWarnings("unused")
    Message sendMessage(MessageReceivedEvent mre, String message) {
        return MessageUtilities.sendMessage(mre, new MessageBuilder().append(message).build());
    }

    /**
     * Determines if the Bot will respond to other Bots.
     * @return true if the Bot will respond to other Bots,
     * false if not.
     */
    @SuppressWarnings("SameReturnValue")
    private boolean respondToBots() {
        return false;
    }
}