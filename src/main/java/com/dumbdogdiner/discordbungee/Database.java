package com.dumbdogdiner.discordbungee;

import com.dumbdogdiner.discordbungee.models.Ban;
import com.dumbdogdiner.discordbungee.models.User;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.UUID;

public class Database {

    private Connection conn = null;
    private DiscordBungee plugin = null;
    private ConnectionSource source = null;
    private Dao<Ban,String> banDao = null;
    private Dao<User,String> userDao = null;

    Database(DiscordBungee plugin) throws DatabaseError {
        this.plugin = plugin;
        String sqlHost = plugin.configuration.getString("mysql.host");
        String sqlDatabase = plugin.configuration.getString("mysql.database");
        String sqlPort = plugin.configuration.getString("mysql.port");
        String sqlUsername = plugin.configuration.getString("mysql.username");
        String sqlPassword = plugin.configuration.getString("mysql.password");
        try {
            source = new JdbcConnectionSource("jdbc:mysql://" + sqlHost + ":"+ sqlPort + "/" + sqlDatabase, sqlUsername, sqlPassword);
            banDao = DaoManager.createDao(source, Ban.class);
            userDao = DaoManager.createDao(source, User.class);
            TableUtils.createTableIfNotExists(source, Ban.class);
            TableUtils.createTableIfNotExists(source, User.class);
        } catch (Exception e) {
            throw new DatabaseError("Unable to configure MySQL connection");
        }
    }

    /**
     * Closes the MySQL connection
     */
    public void Close() {
        try {
            source.close();
        } catch (IOException e) {
            plugin.getLogger().warning("Issue closing MySQL connection");
        }
    }

    public String getMinecraftFromDiscordID(String id) {
        try {
            List<User> results = userDao.queryBuilder().where().eq("discordID", id).query();
            if (!results.isEmpty()) {
                return results.get(0).UUID();
            } else {
                return null;
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while trying to get Minecraft UUID");
            return null;
        }
    }

    public void banUser(String id, String ip, String reason, Date expires) {
        if (ip.equals("")) {
            ip = null;
        }

        Ban b = new Ban(id, ip, reason);
        b.setExpiration(expires);
        try {
            banDao.create(b);
        } catch (SQLException e) {
            plugin.getLogger().warning("Error adding ban to DB");
        }
    }

    public boolean isUserBanned(ProxiedPlayer p) {
        try {
            List<Ban> results = banDao.queryBuilder().where().eq("uuid", p.getUniqueId().toString())
                    .or().eq("ip", p.getAddress().toString())
                    .and().gt("expires", new Date(System.currentTimeMillis())).query();
            return !results.isEmpty();
        } catch (SQLException e) {
            plugin.getLogger().warning("Error getting ban from DB: " + e.getMessage());
        }
        return false;
    }

    public boolean isWhitelisted(ProxiedPlayer p) {
        try {
            List<User> results = userDao.queryBuilder().where().eq("uuid", p.getUniqueId().toString()).query();
            return !results.isEmpty();
        } catch (SQLException e) {
            plugin.getLogger().warning("Error getting whitelist from DB: " + e.getMessage());
        }
        return false;
    }
}
