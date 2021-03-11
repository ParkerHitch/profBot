package main;

import messageProcessing.CustomListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {

    public static JDA jda;

    private static final CustomListener listenter = new CustomListener();

    public static void main(String[] args) {
        JDABuilder builder = JDABuilder.createDefault(args[0], GatewayIntent.GUILD_MESSAGES)
                .setActivity(Activity.listening("The Wynncraft OST")).setStatus(OnlineStatus.ONLINE);
        try {
            jda = builder.build().awaitReady();
            jda.addEventListener(listenter);
            System.out.println("Successfully logged in as "+jda.getSelfUser().getName()+"#"+jda.getSelfUser().getDiscriminator());
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
