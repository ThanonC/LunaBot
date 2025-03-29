package net.thanon.Discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thanon.Request;

import java.net.HttpURLConnection;
import java.net.URL;

public class Listener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        switch (e.getName()) {
            case "find" -> {
                String animeName = e.getOption("name").getAsString();
                new Request().animeEmbed(animeName, e.getGuild(), e.getChannelId());
            }
        }
        super.onSlashCommandInteraction(e);
    }
}
