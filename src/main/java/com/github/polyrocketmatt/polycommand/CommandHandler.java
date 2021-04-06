package com.github.polyrocketmatt.polycommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by PolyRocketMatt on 06/04/2021.
 */

public class CommandHandler implements CommandExecutor {

    private String prefix;
    private Set<AbstractCommand> commands;

    public CommandHandler(String prefix, AbstractCommand... commands) {
        this.prefix = prefix;
        this.commands = new HashSet<>(Arrays.asList(commands));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("sierra"))
                internalCommandDispatch(player, command, label, args);

            return true;
        }

        sender.sendMessage(prefix + ChatColor.RED + "Only players can execute commands!");

        return true;
    }

    /**
     * Handle commands dispatched by players.
     *
     * @param player the player
     * @param command the command
     * @param label the label
     * @param args the arguments
     * @throws CommandException if the command was not found
     */
    private void internalCommandDispatch(Player player, Command command, String label, String[] args) throws CommandException {
        if (args.length == 0) {
            player.sendMessage(prefix + ChatColor.YELLOW + "[s] " + ChatColor.GRAY + " = Alphanumeric");
            player.sendMessage(prefix + ChatColor.YELLOW + "[b] " + ChatColor.GRAY + " = True | False");
            player.sendMessage(prefix + ChatColor.YELLOW + "[i] " + ChatColor.GRAY + " = Number");
            player.sendMessage(prefix + "\n");

            for (AbstractCommand cmd : commands) {
                StringBuilder builder = new StringBuilder();

                CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
                builder.append(prefix)
                        .append(ChatColor.GRAY)
                        .append(info.name())
                        .append(" ")
                        .append(info.arguments())
                        .append(" - ")
                        .append(info.description())
                        .append(" [")
                        .append(info.permission())
                        .append("]");
                player.sendMessage(builder.toString());
            }

            return;
        }

        AbstractCommand cmd = commands
                .stream()
                .filter(filterCmd -> filterCmd.getClass().getAnnotation(CommandInfo.class).name().equalsIgnoreCase(args[0]))
                .findAny()
                .orElseThrow(() -> new CommandException(player.getName() + " tried executing a command \"" + args[0] + "\" that does not exist"));

        if (cmd != null) {
            CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);

            if (player.hasPermission(info.permission())) {
                List<String> arguments = new ArrayList<>(Arrays.asList(args));

                arguments.remove(0);
                cmd.execute(player, arguments.toArray(new String[arguments.size()]));

                return;
            }

            player.sendMessage(prefix + ChatColor.RED + "Oops! You do not have the permission to do that");
        }

        player.sendMessage(prefix  + ChatColor.RED + "That command does not exist!");
    }
}
