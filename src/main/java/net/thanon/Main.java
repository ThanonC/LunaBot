package net.thanon;

import net.thanon.Discord.Bot;

public class Main {
    public static void main(String[] args) {
        try {
            new Bot();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}