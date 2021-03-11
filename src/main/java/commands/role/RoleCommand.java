package commands.role;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import commands.role.objects.PlayerStats;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RoleCommand {

    public static void process(String args, GuildMessageReceivedEvent event){
        try {

            String mcName = "";
            boolean addRoles = false;
            boolean verbose = false;
            if (args.contains(" ") && args.indexOf(" ") != args.length()-1){
                mcName = args.substring(0, args.indexOf(" "));

                List<String> arg = Arrays.asList(args.substring(args.indexOf(" ") + 1).split(" "));

                addRoles = arg.contains("-r") || arg.contains("-role");
                verbose  = arg.contains("-v") || arg.contains("-verbose");

            } else {
                mcName = args;
            }

            URL apiURL = new URL(String.format("https://api.wynncraft.com/v2/player/%s/stats", mcName));
            Scanner sc = new Scanner(apiURL.openStream());
            String json = sc.nextLine();

            StringBuilder messageResponse = new StringBuilder("");
            if (addRoles) {
                messageResponse.append("Roles that have been assigned: ");
            } else {
                messageResponse.append("Roles that would have been assigned: ");
            }

            int in = messageResponse.length();

            Type type = new TypeToken<PlayerStats>() {
            }.getType();

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(type, new PlayerStats.PlayerStatsDeserializer());
            Gson gson = gsonBuilder.create();

            //event.getChannel().sendMessage(json).queue(); API response way to long for a single message
            PlayerStats stats = gson.fromJson(json, type);

            for (Role role : Role.values()){
                if (verbose)
                    messageResponse.append("\n---").append(role.getDiscordRole().getAsMention()).append("---\n");
                if (Req.meetsReq(stats, role.getRequirements(), role.getNeeds(), messageResponse, verbose)){
                    messageResponse.insert(in, role.getDiscordRole().getAsMention() + ", ");
                    if (addRoles) {
                        try {
                            role.assignTo(event.getMember());
                        } catch (HierarchyException e) {
                            event.getChannel().sendMessage("You would have been assigned the role " + role.getDiscordRole().getAsMention() + " but the bot lacked the permission.").queue();
                        }
                    }
                }
            }

            messageResponse.delete(messageResponse.lastIndexOf(", "),messageResponse.lastIndexOf(", ")+2);

            event.getChannel().sendMessage(messageResponse.toString()).queue();

        } catch (IOException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

}
