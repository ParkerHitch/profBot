package messageProcessing;

import commands.Clear;
import commands.role.RoleCommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class GuildMessageProcessor {

    public static void process(GuildMessageReceivedEvent event){
        String raw = event.getMessage().getContentRaw();
        System.out.println(raw);
        TextChannel chan = event.getChannel();
        if (raw.startsWith("]")){
            String command = raw.substring(1, !raw.contains(" ") ?raw.length():raw.indexOf(" "));
            switch (command){
                case "role":
                case "r":
                    if (raw.contains(" ")) {
                        RoleCommand.process(raw.substring(raw.indexOf(" ") + 1), event);
                    } else {
                        event.getChannel().sendMessage("Please add a minecraft username").queue();
                    }
                    break;
                case "clear":
                    Clear.process(event);
            }
        }
    }

}
