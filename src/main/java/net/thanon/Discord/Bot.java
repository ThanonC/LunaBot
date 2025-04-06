package net.thanon.Discord;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.time.Duration;

public class Bot {
    public Bot() throws InterruptedException {
        Dotenv env = Dotenv.load();
        JDA jda = JDABuilder.createDefault(env.get("token"))
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new Listener())
                .build().awaitReady();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("find", "Finds an anime for you on Lunar Anime")
                        .addOption(OptionType.STRING, "name", "the name of the anime", true),
                Commands.slash("level", "Episodes watched + user level")
                        .addOption(OptionType.STRING, "name", "user name", true)
        ).queue();
    }
}
