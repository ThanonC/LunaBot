package net.thanon.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.managers.AudioManager;
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
            case "commands" -> {
                MessageEmbed embed = new EmbedBuilder()
                        .setColor(Color.magenta)
                        .setTitle("Command list of " + e.getJDA().getSelfUser().getName())
                        .setDescription("**Slash**\n/find {name} - helps you find an anime (Works, but still being worked on to improve it)\n/level {name} - Find out which level a person is\n/report - brings you to the official report page of Lunar Anime\n\n**Disposable Chat commands**\ncreate!disposable - creates a new disposable channel\ndelete!disposable - lets you delete a disposable channel (you have to be the owner of that channel)\nadd!disposable - lets you add a person to your personal disposable channel\n\n**Music commands**\n?join - joins the users voice channel (currently still work in progress, it only joins: <#1358824476602794136>)\n?leave - makes the bot leave the current channel")
                        .build();
                e.deferReply().addEmbeds(embed).queue();
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
                .setImage("https://i.imgur.com/Jp9w4wF.png")
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
                if(e.getGuild().getTextChannelsByName("disposable", true).size() <= 9) {
                    e.getMessage().delete().queue();
                    e.getChannel().sendMessage("Created new disposable channel").queue();
                    e.getGuild().createTextChannel("disposable", e.getGuild().getCategoryById("1358438423291236453"))
                            .addPermissionOverride(e.getGuild().getPublicRole(), 0, Permission.VIEW_CHANNEL.getRawValue())
                            .addPermissionOverride(e.getMember(), Permission.VIEW_CHANNEL.getRawValue() | Permission.MESSAGE_SEND.getRawValue() | Permission.MESSAGE_HISTORY.getRawValue(), 0)
                            .setTopic("disposable text channel | " + e.getAuthor().getEffectiveName()).queue();
                } else {
                    e.getChannel().sendMessage("Error: Max disposable text channel amount reached").queue();
                }
            }

            case "add!disposable" -> {
                String[] args = e.getMessage().getContentRaw().split(" ");
                if (args.length < 2) {
                    e.getChannel().sendMessage("Please mention a user to add!").queue();
                    return;
                }

                TextChannel channel = e.getChannel().asTextChannel();

                if (channel.getTopic().contains(e.getAuthor().getEffectiveName())) {
                    channel.upsertPermissionOverride(e.getGuild().getMemberById(args[2]))
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

            case "ai!info" -> {
                e.getChannel().sendMessageFormat("Apollo Ai by ThanonGaming is not being incorporated here. This system uses a whole different Ai know as %s. Apollo Ai was started on 29/03/2025 while this one started on 07/04/2025", null).queue();
            }
        }
        if(e.getMessage().getContentRaw().contains("ai!input")) {
            if(!e.getMessage().getAuthor().isBot()) {
                e.getChannel().sendMessageFormat("Currently still work in progress. You entered: %s", e.getMessage().getContentRaw().replace("ai!input", "")).queue();
            }
        }
        super.onMessageReceived(e);
    }
}
