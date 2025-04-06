package net.thanon.Utils;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Config {
    private File f = new File("C:/Users/Kai/Desktop/Me/LunaBot/assets", "femboy.json");
    JSONObject json;
    public Config() {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(f.toURI())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        json = new JSONObject(content);
    }

    public String getString(String string) {
        return json.getString(string);
    }

    public int getInt(int number) {
        return json.getInt(String.valueOf(number));
    }
}
