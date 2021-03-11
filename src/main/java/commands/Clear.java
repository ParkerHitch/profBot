package commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class Clear {

    public static void process(GuildMessageReceivedEvent event) {

        List<Role> roles = event.getMember().getRoles();

        roles.forEach(role -> {
            event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
        });

        event.getChannel().sendMessage("Removed your roles").queue();

    }
}
