package com.github.polyrocketmatt.polycommand;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by PolyRocketMatt on 11/03/2021.
 *
 * Represents a command that is part of Sierra.
 */

public abstract class AbstractCommand {

    /**
     * The implementation of the command.
     *
     * @param player the player that executed the command
     * @param args the arguments the player provided
     */
    public abstract void execute(@NotNull Player player, @NotNull String[] args);

}
