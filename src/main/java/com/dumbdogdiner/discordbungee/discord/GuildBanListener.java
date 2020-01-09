package com.dumbdogdiner.discordbungee.discord;

import com.dumbdogdiner.discordbungee.DiscordBungee;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class GuildBanListener extends ListenerAdapter {

    private DiscordBungee plugin = null;

    GuildBanListener(DiscordBungee plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildBan(GuildBanEvent e) {
        User user = e.getUser();
        try {
            UUID pUUID = UUID.fromString(plugin.db.getMinecraftFromDiscordID(user.getId()));
            ProxiedPlayer p = plugin.getProxy().getPlayer(pUUID);
            if (p.isConnected()) {
                p.disconnect(new TextComponent("You've been banned on Discord."));
                this.plugin.db.banUser(pUUID.toString(), p.getAddress().toString(), "Discord banned", null);
            } else {
                this.plugin.db.banUser(pUUID.toString(), "", "Discord banned", null);
            }

        } catch (IllegalArgumentException err) {
            plugin.getLogger().warning("Got bad UUID from DB");
        }
    }
}
