package com.dumbdogdiner.discordbungee.listeners;

import com.dumbdogdiner.discordbungee.DiscordBungee;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinListener implements Listener {

    private DiscordBungee plugin = null;

    JoinListener(DiscordBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        // Ban check first
        if (plugin.db.isUserBanned(event.getPlayer())) {
            event.getPlayer().disconnect(new TextComponent("You're banned."));
            return;
        }

        // Whitelist check first
        if (!plugin.db.isWhitelisted(event.getPlayer())) {
            event.getPlayer().disconnect(new TextComponent("You're not whitelisted!"));
        }
    }
}
