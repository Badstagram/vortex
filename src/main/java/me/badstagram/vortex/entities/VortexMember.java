package me.badstagram.vortex.entities;

import me.badstagram.vortex.managers.EconomyManager;
import net.dv8tion.jda.api.entities.Member;

public interface VortexMember extends Member {

    EconomyManager getEconomyManager();
}
