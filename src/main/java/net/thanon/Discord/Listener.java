package net.thanon.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.thanon.Request;
import net.thanon.Utils.Config;

import java.awt.*;
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
            case "level" -> {
                String userName = e.getOption("name").getAsString();
                new Request().levelUpMessage(userName, e.getGuild(), e.getChannel().getId());
            }
        }
        super.onSlashCommandInteraction(e);
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Member m = e.getMember();
        MessageEmbed embed = new EmbedBuilder()
                .setColor(Color.MAGENTA)
                .setTitle("Welcome too " + e.getGuild().getName())
                .setDescription(m.getAsMention() + " good evening and welcome on Lunar Anime, where you can stream thousands of animes for free\n\n If you need help you can ask our <@1343141581334511616>, <@1355197376763138239> and <@1343141519283978260>\n\n You are number " + e.getGuild().getMemberCount() + "/" + e.getGuild().getMaxMembers() + " on our server")
                .setImage("https://images-ext-1.discordapp.net/external/IgCKS8n02LtFPvbJ37TmkyjXHNNtdBtOIlXhdrMbBPw/%3Fcb%3D20161122231248%26path-prefix%3Dprotagonist/https/static.wikia.nocookie.net/p__/images/1/13/Luna.png/revision/latest?format=webp&width=819&height=786")
                .setThumbnail(m.getAvatarUrl())
                .build();
        e.getGuild().getTextChannelById("1357716115245240320").sendMessageEmbeds(embed).queue();
        super.onGuildMemberJoin(e);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if(e.getMessage().getContentRaw().contains("recommendations")) {
            e.getChannel().sendMessage("vist the channel: " + e.getGuild().getTextChannelById("1343142812165869649").getAsMention()).queue();
        }
        if(e.getMessage().getContentRaw().equals("Femboy")) {
            e.getMessage().delete().queue();
            e.getChannel().sendMessage(new Config().getString(String.valueOf(Math.toIntExact((long) (Math.random() * 7))))).queue();
        }
        super.onMessageReceived(e);
    }
}
