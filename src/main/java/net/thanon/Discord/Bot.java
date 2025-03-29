package net.thanon.Discord;

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
        JDA jda = JDABuilder.createDefault("")
                .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new Listener())
                .build().awaitReady();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                Commands.slash("find", "2")
                        .addOption(OptionType.STRING, "name", "a", true)
        ).queue();
    }
}
