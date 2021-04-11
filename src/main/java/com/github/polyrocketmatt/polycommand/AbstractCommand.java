package com.github.polyrocketmatt.polycommand;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a subcommand.
 *
 * Created by PolyRocketMatt on 11/03/2021.
 */

public abstract class AbstractCommand {

    /**
     * The implementation of the command.
     *
     * @param player the player that executed the command
     * @param args the arguments the player provided
     * @param label the label that has been used to trigger the command
     */
    public abstract void execute(@NotNull Player player, @NotNull String[] args, @NotNull String label);

}
