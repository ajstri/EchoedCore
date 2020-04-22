package core.commands.music;

import core.EchoedCore;
import core.commands.Command;
import core.commands.Modules;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NowPlayingCommand extends Command {
    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        EchoedCore.log.info("NOW PLAYING");

        Member author = mre.getMember();
        if (author != null) {
            if (EchoedCore.utils.isInVoiceChannel(mre.getMember())) {
                mre.getChannel().sendMessage(EchoedCore.utils.getTrackTitle(mre.getGuild())).queue();
            }
            else {
                mre.getChannel().sendMessage("You must be in a voice channel to use this command").queue();
            }
        }
        else {
            mre.getChannel().sendMessage("Uh oh. Something went wrong!").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("nowplaying", "now", "np");
    }

    @Override
    public String getDescription() {
        return "Displays the currently playing song";
    }

    @Override
    public String getName() {
        return "Now Playing Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(EchoedCore.config.getPrefix() + "nowplaying");
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
