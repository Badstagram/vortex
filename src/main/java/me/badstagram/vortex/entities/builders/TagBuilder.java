package me.badstagram.vortex.entities.builders;

import me.badstagram.vortex.entities.Tag;

public class TagBuilder {
    private String name;
    private String value;
    private String addedBy;
    private String guildId;


    public TagBuilder setGuildId(String guildId) {
        this.guildId = guildId;
        return this;
    }

    public TagBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TagBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public TagBuilder setAddedBy(String addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    public Tag build() {
        return new Tag(this.name, this.value, this.addedBy, guildId);
    }


}
