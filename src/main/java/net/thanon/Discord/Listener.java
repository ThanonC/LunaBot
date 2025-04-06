package net.thanon.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
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
            case "report" -> {
                e.getChannel().sendMessage("You can report bugs on: https://lunaranime.com/report").queue();
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
                .setDescription(m.getAsMention() + " good day and welcome on Lunar Anime, where you can stream thousands of animes for free\n\n You are number " + e.getGuild().getMemberCount() + "/" + e.getGuild().getMaxMembers() + " on our server")
                .setImage("https://images-ext-1.discordapp.net/external/IgCKS8n02LtFPvbJ37TmkyjXHNNtdBtOIlXhdrMbBPw/%3Fcb%3D20161122231248%26path-prefix%3Dprotagonist/https/static.wikia.nocookie.net/p__/images/1/13/Luna.png/revision/latest?format=webp&width=819&height=786")
                .setThumbnail(m.getAvatarUrl())
                .setFooter("Bot by ThanonGaming")
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
            e.getChannel().sendMessage(new Config().getString(String.valueOf(Math.toIntExact((long) (Math.random() * 7))))).queue();
        }
        switch (e.getMessage().getContentRaw()) {
            case "!d20" -> {
                e.getChannel().sendMessage("Rolled: " + Math.toIntExact((long) (Math.random() * 19 + 1))).queue();
            }
            case "create!disposable" -> {
                e.getMessage().delete().queue();
                    e.getGuild().createTextChannel("disposable", e.getGuild().getCategoryById("1358438423291236453"))
                            .addPermissionOverride(e.getGuild().getPublicRole(), 0, Permission.VIEW_CHANNEL.getRawValue())
                            .addPermissionOverride(e.getMember(), Permission.VIEW_CHANNEL.getRawValue() | Permission.MESSAGE_SEND.getRawValue() | Permission.MESSAGE_HISTORY.getRawValue(), 0)
                            .setTopic("disposable text channel | " + e.getAuthor().getEffectiveName()).queue();
            }

            case "add!disposable" -> {
                String[] args = e.getMessage().getContentRaw().split(" ");
                if (args.length < 2) {
                    e.getChannel().sendMessage("Please mention a user to add!").queue();
                    return;
                }

                TextChannel channel = e.getChannel().asTextChannel();

                if (channel.getTopic() != null && channel.getTopic().contains(e.getAuthor().getName())) {
                    channel.upsertPermissionOverride(e.getGuild().getMember(UserSnowflake.fromId(e.getMember().getAsMention())))
                            .setAllowed(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND)
                            .queue(
                                    success -> e.getChannel().sendMessage("User added to the channel!").queue(),
                                    error -> e.getChannel().sendMessage("Failed to update permissions.").queue()
                            );
                } else {
                    e.getChannel().sendMessage("You can't modify this channel.").queue();
                }
            }

            case "delete!disposable" -> {
                if(e.getGuild().getTextChannelById(e.getChannel().getId()).getTopic().contains("disposable text channel")) {
                    e.getChannel().delete().complete();
                }
            }

            case "delete!disposable_all" -> {
                e.getMessage().delete().queue();
                e.getGuild().getTextChannelsByName("disposable", true).get(e.getGuild().getTextChannelsByName("disposable", true).toArray().length-1).delete().queue();
            }
        }
        super.onMessageReceived(e);
    }
}
