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
        if(args.length == 1) {
            mre.getChannel().sendMessage("Volume: " + EchoedCore.utils.getVolume(mre.getGuild())).queue();
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
            EchoedCore.utils.setVolume(mre.getGuild(), newVolume);
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
                "`" + EchoedCore.config.getPrefix() + "volume` **OR**",
                "`" + EchoedCore.config.getPrefix() + "volume`"
        );
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
