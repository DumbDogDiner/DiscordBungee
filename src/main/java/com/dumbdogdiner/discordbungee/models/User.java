package com.dumbdogdiner.discordbungee.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "whitelist")
public class User {
    @DatabaseField
    private String discordID;

    @DatabaseField
    private String uuid;

    User() {}

    public User(String discordID, String uuid) {
        this.uuid = uuid;
        this.discordID = discordID;
    }

    public String UUID() {
        return uuid;
    }

    public String DiscordID() {
        return discordID;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return discordID.equals(((User) other).DiscordID());
    }
}
