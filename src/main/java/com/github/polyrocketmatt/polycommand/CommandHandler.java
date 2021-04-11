package com.github.polyrocketmatt.polycommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by PolyRocketMatt on 06/04/2021.
 */

public class CommandHandler implements CommandExecutor {

    private @NotNull String prefix;
    private @NotNull String command;
    private @NotNull Set<AbstractCommand> commands;

    public CommandHandler(@NotNull String prefix, @NotNull String command, @NotNull AbstractCommand... commands) {
        this.prefix = prefix;
        this.command = command;
        this.commands = new HashSet<>(Arrays.asList(commands));
    }

    public @NotNull Set<AbstractCommand> getCommands() {
        return commands;
    }

    public @NotNull String getCommand() {
        return command;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase(this.command))
                internalCommandDispatch(player, label, args);

            return true;
        }

        sender.sendMessage(prefix + ChatColor.RED + "Only players can execute commands!");

        return true;
    }

    /**
     * Handle commands dispatched by players.
     *
     * @param player the player
     * @param label  the label
     * @param args   the arguments
     * @throws CommandException if the command was not found
     */
    private void internalCommandDispatch(@NotNull Player player, @NotNull String label, @NotNull String[] args) throws CommandException {
        if (args.length == 0) {
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[s] " + ChatColor.GRAY + " = Alphanumeric"));
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[b] " + ChatColor.GRAY + " = True | False"));
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[i] " + ChatColor.GRAY + " = Number"));
            player.sendMessage("\n");

            for (AbstractCommand cmd : commands) {
                StringBuilder builder = new StringBuilder();

                CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
                builder.append(prefix)
                        .append(ChatColor.GRAY)
                        .append(label)
                        .append(" ")
                        .append(info.name())
                        .append(" ")
                        .append(info.arguments())
                        .append(" - ")
                        .append(info.description())
                        .append(" [")
                        .append(info.permission())
                        .append("]");
                player.sendMessage(Utils.translate(builder.toString()));
            }

            return;
        }

        @Nullable AbstractCommand cmd = commands
                .stream()
                .filter(filterCmd -> filterCmd.getClass().getAnnotation(CommandInfo.class).name().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);
        if (cmd != null) {
            CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);

            if (player.hasPermission(info.permission())) {
                List<String> arguments = new ArrayList<>(Arrays.asList(args));

                arguments.remove(0);
                cmd.execute(player, arguments.toArray(new String[arguments.size()]), label);

                return;
            }

            player.sendMessage(Utils.translate(prefix + ChatColor.RED + "Oops! You do not have the permission to do that"));
        } else {
            player.sendMessage(Utils.translate(prefix + ChatColor.RED + "That command does not exist!"));
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[s] " + ChatColor.GRAY + " = Alphanumeric"));
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[b] " + ChatColor.GRAY + " = True | False"));
            player.sendMessage(Utils.translate(prefix + ChatColor.YELLOW + "[i] " + ChatColor.GRAY + " = Number"));
            player.sendMessage("\n");

            for (AbstractCommand acmd : commands) {
                StringBuilder builder = new StringBuilder();

                CommandInfo info = acmd.getClass().getAnnotation(CommandInfo.class);
                builder.append(prefix)
                        .append(ChatColor.GRAY)
                        .append(label)
                        .append(" ")
                        .append(info.name())
                        .append(" ")
                        .append(info.arguments())
                        .append(" - ")
                        .append(info.description())
                        .append(" [")
                        .append(info.permission())
                        .append("]");
                player.sendMessage(Utils.translate(builder.toString()));
            }

            return;
        }
    }
}
