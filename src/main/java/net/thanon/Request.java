package net.thanon;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.json.JSONArray;
import org.json.JSONObject;

public class Request {
    public Request() {
    }

    public void requestAnimeData(String name) {
        String apiUrl = "https://oblivion.wtf/api/anime/" + name;
        try {
            // Create a URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray dataArray = jsonResponse.getJSONArray("data");

                if (dataArray.length() > 0) {
                    JSONObject firstEntry = dataArray.getJSONObject(0);

                    if (firstEntry.has("alt_titles")) {
                        System.out.println("Alternative Titles: " + firstEntry.getJSONArray("alt_titles"));
                    } else {
                        System.out.println("No 'alt_titles' found in the first entry.");
                    }
                } else {
                    System.out.println("No data available in the response.");
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void animeEmbed(String name, Guild g, String channelId) {
        String apiUrl = "https://oblivion.wtf/api/animes/search?query=" + name;
        try {
            // Create a URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray dataArray = jsonResponse.getJSONArray("animes");

                if (dataArray.length() > 0) {
                    JSONObject firstEntry = dataArray.getJSONObject(0);

                    /*MessageEmbed embed = new EmbedBuilder()
                            .setTitle(firstEntry.getString("title"))
                            .setDescription("Beschreibung:\n\n " + firstEntry.getString("description").toString() +
                                    "\n\n Runtime: " + firstEntry.getInt("start_year") + " - " + firstEntry.getInt("end_year") +
                                    "\n\nGenres: " + firstEntry.getJSONArray("genres").toString())
                            .setThumbnail(firstEntry.getString("poster_url").toString())
                            .build();

                    g.getTextChannelById(channelId).sendMessageEmbeds(embed).addActionRow(Button.link("https://lunaranime.com/anime/", "Watch on Lunar Anime")).queue();

                    MessageEmbed embed2 = new EmbedBuilder()
                            .setTitle("Found anime: " + firstEntry.getString("title"))
                            .setImage(firstEntry.getString("poster_url"))
                            .setColor(Color.BLACK)
                            .build();

                    g.getTextChannelById(channelId).sendMessageEmbeds(embed2).addActionRow(Button.link("https://lunaranime.com/anime/" + firstEntry.getString("slug"), "Watch it now on Lunar Anime")).queue();*/

                    MessageEmbed embed3 = new EmbedBuilder()
                            .setTitle(firstEntry.getString("title"), "https://lunaranime.com/anime/" + firstEntry.getString("slug"))
                            .setImage(firstEntry.getString("poster_url"))
                            .setFooter("Click me", null)
                            .setUrl("https://lunaranime.com/anime/" + firstEntry.getString("slug"))
                            .build();

                    g.getTextChannelById(channelId).sendMessageEmbeds(embed3).queue();
                } else {
                    g.getTextChannelById(channelId).sendMessage("No data available in the response").queue();
                }
                //g.getTextChannelById(channelId).sendMessage(dataArray.toString()).queue();
            } else {
                g.getTextChannelById(channelId).sendMessage("GET request failed. Response Code: " + responseCode).queue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void levelUpMessage(String name, Guild g, String channelId) {
        String apiUrl = "https://oblivion.wtf/api/animes/watched/count?username=" + name;
        String apiUrl2 = "https://oblivion.wtf/api/animes/watched/count?username=" + name;
        try {
            // Create a URL object
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to GET
            connection.setRequestMethod("GET");

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONObject data = jsonResponse.getJSONObject("data");

                g.getTextChannelById(channelId).sendMessageFormat("user: %s\ntotal watched episodes: %s\nlevel: %s", name, data.getInt("total_episodes"), data.getInt("total_episodes")/25).queue();
            } else {
                g.getTextChannelById(channelId).sendMessage("no data available").queue();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
