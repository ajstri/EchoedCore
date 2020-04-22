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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import utilities.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *  HelpCommand class of the EchoedCore project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class HelpCommand extends Command {
    private static final String NO_NAME = "No name provided for this command. Sorry!";
    private static final String NO_DESCRIPTION = "No description has been provided for this command. Sorry!";
    private static final String NO_USAGE = "No usage instructions have been provided for this command. Sorry!";

    public final TreeMap<String, Command> commands;
    public List<String> modules;

    /**
     * Constructor for the HelpCommand
     */
    public HelpCommand() {
        commands = new TreeMap<>();
        registerModules();
    }

    /**
     * Adds command to TreeMap.
     * @param command command to add
     * @return command added.
     */
    public Command registerCommand(Command command) {
        commands.put(command.getAliases().get(0), command);
        return command;
    }

    private void registerModules() {
        modules = Arrays.asList(
                Modules.ADMIN,
                Modules.FUN,
                Modules.GENERIC,
                Modules.MUSIC
        );
    }

    @Override
    public void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.log.info("HELP");
        // Bypass sending message if it is already in a private message.
        if(!mre.isFromType(ChannelType.PRIVATE)) {
            // Send help message
            mre.getTextChannel().sendMessage(new MessageBuilder()
                    .append("Hey, ")
                    .append(mre.getAuthor())
                    .append(": Help information was sent as a private message.")
                    .build()).queue();
        }
        // Send help message
        sendPrivateDefault(mre.getAuthor().openPrivateChannel().complete(), args);

    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("help", "commands");
    }

    @Override
    public String getDescription() {
        return "Command that helps use all other commands!";
    }

    @Override
    public String getName() {
        return "Help Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                EchoedCore.config.getPrefix() + "help   **OR**  " + EchoedCore.config.getPrefix() + "help *<command>*\n"
                        + EchoedCore.config.getPrefix() + "help - returns the list of commands along with a simple description of each.\n"
                        + EchoedCore.config.getPrefix() + "help <command> - returns the name, description, aliases and usage information of a command.\n"
                        + "   - This can use the aliases of a command as input as well.\n"
                        + "__Example:__ " + EchoedCore.config.getPrefix() + "help ping");
    }

    @Override
    public boolean getDefaultPermission() {
        return true;
    }

    @Override
    public String getModule() {
        return Modules.GENERIC;
    }

    /**
     * Sends a message to a private channel.
     * @param channel channel to send to
     * @param args arguments used to build the message.
     */
    private void sendPrivateDefault(PrivateChannel channel, String[] args) {
        if (args.length < 2) {
            EmbedBuilder embed = new EmbedBuilder().setTitle("Modules Supported").setColor(Color.RED);
            MessageUtilities.addEmbedDefaults(embed);

            // For each Module, add its values to embed.
            for (String m : modules) {
                embed.addField(m, commandsInModule(m) + " commands active", true);
            }

            // Send embed.
            channel.sendMessage(embed.build()).queue();
        }
        else {
            EmbedBuilder embed = new EmbedBuilder();

            MessageUtilities.addEmbedDefaults(embed);

            String command = args[1];
            // Check each command. If it is the command searched for, build embed.
            for (Command c : commands.values()) {
                if (c.getAliases().contains(command)) {
                    // Define values.
                    addCommandValues(embed, c);

                    // Send embed.
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }

            // Needs to find module, then list all commands with that module.
            boolean hasACommand = false;
            for (String module : modules) {
                if (module.contains(command)) {
                    List<Command> commandsInModule = getCommandsInModule(module);
                    for (Command c : commandsInModule) {
                        // Define values.
                        embed.addField(c.getName(), "", false);
                        hasACommand = true;
                    }
                }
                // Send embed.
                if (hasACommand) {
                    channel.sendMessage(embed.build()).queue();
                    return;
                }
            }

            // If it reaches this point, the command/module searched for does not exist.
            doesNotExist(channel, args);
        }
    }

    /**
     * Sends a message telling the user their search doesn't exist
     * @param channel channel to send message
     * @param args arguments to build message
     */
    private static void doesNotExist(PrivateChannel channel, String[] args) {
        // If it reaches this point, the command searched for does not exist.
        channel.sendMessage(new MessageBuilder()
                .append("The provided command '**")
                .append(args[1])
                .append("**' does not exist. Use ")
                .append(EchoedCore.config.getPrefix())
                .append("help to list all commands.")
                .build()).queue();
    }

    /**
     * Add Commands to the embed
     * @param embed embed to add Commands to
     * @param c Command to add
     */
    private void addCommandValues(EmbedBuilder embed, Command c) {
        String name = c.getName();
        String description = c.getDescription();
        List<String> usageInstructions = c.getUsage();

        // Null checks.
        if (name == null || name.isEmpty()) {
            name = NO_NAME;
        }
        if (description == null || description.isEmpty()) {
            description = NO_DESCRIPTION;
        }
        if (usageInstructions == null || usageInstructions.isEmpty()) {
            usageInstructions = Collections.singletonList(NO_USAGE);
        }

        // Add values to embed.
        embed.setTitle(name);
        embed.addField(description, "", false);
        embed.addField("Aliases", StringUtils.join(c.getAliases(), ", ") + "\n", false);
        embed.addField("Usage", usageInstructions.get(0), false);

        // Add remaining usage instructions.
        for (int i = 1; i < usageInstructions.size(); i++) {
            embed.addField("", usageInstructions.get(i), false);
        }
    }

    /**
     * Returns a list of Commands within a given module
     * @param module Module to search in
     * @return List of commands
     */
    private List<Command> getCommandsInModule(String module) {
        List<Command> activeCommands = null;

        for (Command command : commands.values()) {
            if (command.getModule().toLowerCase().contains(module)) {
                activeCommands.add(command);
            }
        }

        return activeCommands;
    }

    /**
     * Returns the number of commands in a module
     * @param module Module to search in
     * @return Number of commands in Module
     */
    private int commandsInModule(String module) {
        return getCommandsInModule(module).size();
    }
}