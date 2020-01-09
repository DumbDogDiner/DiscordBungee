package com.dumbdogdiner.discordbungee.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "bans")
public class Ban {
    @DatabaseField
    private String uuid;

    @DatabaseField
    private String ip;

    @DatabaseField
    private String reason;

    @DatabaseField
    private Date expires;

    Ban() {}

    public Ban(String uuid, String reason) {
        this.uuid = uuid;
        this.reason = reason;
    }

    public Ban(String uuid, String ip, String reason) {
        this.uuid = uuid;
        this.reason = reason;
        this.ip = ip;
    }

    public String UUID() {
        return uuid;
    }

    public String IP() {
        return ip;
    }

    public String Reason() {
        return reason;
    }

    public Date Expiration() {
        return expires;
    }

    public void setExpiration(Date expires) {
        this.expires = expires;
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
        return uuid.equals(((Ban) other).UUID());
    }
}
