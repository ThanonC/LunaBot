package net.thanon.Music;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateVoiceStatusEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicListener extends ListenerAdapter {
    private boolean isInVoice = false;
    private boolean isUserInVoice = false;
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        VoiceChannel channel = e.getGuild().getVoiceChannelsByName("holocaust", true).get(0);
        AudioManager manager = e.getGuild().getAudioManager();
        switch (e.getMessage().getContentRaw()) {
            case "?join" -> {
                    if (isInVoice == false) {
                        //manager.setSendingHandler(new AudioPlayerSendHandler());
                        manager.openAudioConnection(channel);
                        isInVoice = true;
                    } else {
                        e.getChannel().sendMessage("The bot is currently already in a channel").queue();
                    }
            }
            case "?leave" -> {
                if(isInVoice == true) {
                    manager.closeAudioConnection();
                    isInVoice = false;
                } else {
                    e.getChannel().sendMessage("The bot is currently not in a channel").queue();
                }
            }
        }
        super.onMessageReceived(e);
    }
}
