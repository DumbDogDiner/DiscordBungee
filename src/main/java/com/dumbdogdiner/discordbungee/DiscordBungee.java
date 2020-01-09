package com.dumbdogdiner.discordbungee;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class DiscordBungee extends Plugin {

    private static JDA discord = null;
    public Configuration configuration = null;
    public Database db = null;


    public void initBot() {
        String token = configuration.getString("com.dumbdogdiner.discordbungee.discord.token");
        try {
            discord = new JDABuilder(AccountType.BOT).setToken(token).build().awaitReady();
            if (configuration.getBoolean("banSync")) {
                discord.addEventListener();
            }
        } catch (LoginException | InterruptedException ignored) {
            getLogger().warning("Unable to initialize Discord bot.");
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("Loading com.dumbdogdiner.discordbungee.DiscordBungee...");
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists()) {
            try {
                InputStream in = getResourceAsStream("config.yml");
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (java.io.IOException e) {
            getLogger().severe("Unable to load com.dumbdogdiner.discordbungee.DiscordBungee config");
            return;
        }

        try {
            db = new Database(this);
        } catch (DatabaseError databaseError) {
            databaseError.printStackTrace();
            return;
        }

        initBot();
    }

    @Override
    public void onDisable() {
        db.Close();
        getLogger().info("Disabled com.dumbdogdiner.discordbungee.DiscordBungee.");
    }
}
