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
 *
 * General use CommandHandler used to dispatch subcommands of a main command in an orderly fashion.
 * There may only exist one {@code CommandHandler} per base command.
 * <p>
 *
 * Created by PolyRocketMatt on 06/04/2021.
 * Adapted by Mmaarten on 11/04/2021.
 */

public class CommandHandler implements CommandExecutor {

    private final @NotNull String prefix;

    private final @NotNull Set<AbstractCommand> commands;

    private @NotNull String PLAYER_ONLY = "&4Only players can execute commands!";
    private @NotNull String NOT_A_COMMAND = "&4That command does not exist!";
    private @NotNull String MISSING_PERMISSION = "&4Oops! You do not have the permission to do that!";
    private @Nullable String PLUGIN_DESCRIPTION = null;
    private @NotNull String COMMAND_HELP_PATTERN = "%prefix% &7/%label% %name% %args% - %desc% [%perm%]";
    private @Nullable String COMMAND_HELP_HEADER =
            "%prefix% &e[s] &7= Alphanumeric\n" +
                    "%prefix% &e[b] &7= True | False\n" +
                    "%prefix% &e[i] &7= Number";

    /**
     * Create a new {@code CommandHandler} instance.
     * @param prefix the prefix used when reporting errors or help menus to the player
     * @param commands a collection of {@link AbstractCommand}s that this {@code CommandHandler} is handling
     */
    public CommandHandler(@NotNull String prefix, @NotNull AbstractCommand... commands) {
        this.prefix = prefix;
        this.commands = new HashSet<>(Arrays.asList(commands));
    }

    /**
     * Returns a copy of the set of {@link AbstractCommand}s being observed by this {@code CommandHandler}.
     *
     * @return A new set containing the original {@link AbstractCommand} objects
     */
    public @NotNull Set<AbstractCommand> getCommands() {
        return new HashSet<>(commands);
    }

    /**
     * @param command The {@link AbstractCommand} to check
     * @return iff this {@code CommandHandler} handles the {@code command}
     */
    private boolean commandExists(AbstractCommand command) {
        return this.commands.stream().anyMatch(command1 ->
                command1.getClass().getAnnotation(CommandInfo.class).name().equals(command.getClass().getAnnotation(CommandInfo.class).name())
        );
    }

    /**
     * Removes all commands from this {@code CommandHandler}.
     */
    public void clearCommands() {
        this.commands.clear();
    }

    /**
     * Adds an {@link AbstractCommand} to this {@code CommandHandler} to handle.
     *
     * @param command the {@link AbstractCommand} to be added to this {@code CommandHandler}
     * @return this {@code CommandHandler}
     * @throws CommandException if a command with the given {@link CommandInfo#name()} has already been added to this {@code CommandHandler}
     */
    public @NotNull CommandHandler addCommand(AbstractCommand command) {
        if (commandExists(command))
            throw new CommandException("Command with name " + command.getClass().getAnnotation(CommandInfo.class).name() + " already exists!");
        this.commands.add(command);
        return this;
    }

    /**
     * Removes an {@link AbstractCommand} from this {@code CommandHandler}.
     * @param command the {@link AbstractCommand} to be removed from this {@code CommandHandler}
     * @return this {@code CommandHandler}
     * @throws CommandException if a command with the given {@link CommandInfo#name()} is not handled by this {@code CommandHandler}
     */
    public @NotNull CommandHandler removeCommand(AbstractCommand command) {
        if (!commandExists(command))
            throw new CommandException("Command with name " + command.getClass().getAnnotation(CommandInfo.class).name() + " does not exist!");
        this.commands.remove(command);
        return this;
    }

    /**
     * Sets the message to display to the {@link CommandExecutor} if they are not a {@link Player}.
     * Default minecraft color codes can be used through the use of the '&amp;' char. <br>
     * Default: &amp;4Only players can execute commands!
     * @param message the message to display to non-player CommandSenders
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withPlayerOnlyMessage(@NotNull String message) {
        this.PLAYER_ONLY = message;
        return this;
    }

    /**
     * Sets the message to display to the {@link Player} if they enter a subcommand
     * that is not being handled by this {@link CommandHandler}.
     * Default minecraft color codes can be used through the use of the '&amp;' char. <br>
     * Default: &amp;4That command does not exist!
     * @param message the message to display when entering an incorrect subcommand
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withNotACommandMessage(@NotNull String message) {
        this.NOT_A_COMMAND = message;
        return this;
    }

    /**
     * Sets the message to display to the {@link Player} if they are lacking
     * the {@link CommandInfo#permission()} node for this command.
     * Default minecraft color codes can be used through the use of the '&amp;' char. <br>
     * Default: &amp;4Oops! You do not have the permission to do that!
     * @param message the message to display when missing permission
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withMissingPermissionMessage(@NotNull String message) {
        this.MISSING_PERMISSION = message;
        return this;
    }

    /**
     * Adds a plugin description to display when the base command is used without any arguments.
     * If set to {@code null}, the help menu will be printed instead.
     * Default minecraft color codes can be used through the use of the '&amp;' char. <br>
     * @param description the message to display
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withPluginDescription(String description) {
        this.PLUGIN_DESCRIPTION = description;
        return this;
    }

    /**
     * Sets the pattern used when the help menu is displayed.
     * The following placeholders are available: <br>
     *   {@code %label%} -> The command label the player used <br>
     *   {@code %name%} -> The {@link CommandInfo#name()} of the command <br>
     *   {@code %args%} -> The {@link CommandInfo#arguments()} of the command <br>
     *   {@code %desc%} -> The {@link CommandInfo#description()} of the command <br>
     *   {@code %perm%} -> The {@link CommandInfo#permission()} node of the command <br>
     * Default minecraft color codes can be used through the use of the '&amp;' char. <br>
     * Default: %prefix% &amp;7/%label% %name% %args% - %desc% [%perm%]
     *
     * @param pattern used to format the help menu
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withCommandHelpPattern(@NotNull String pattern) {
        this.COMMAND_HELP_PATTERN = pattern;
        return this;
    }

    /**
     * Sets the header for the help menu. If {@code header} is {@code null}, there will be no header. <br>
     * You can use the {@code %prefix%} placeholder to insert the prefix for this {@code CommandHandler}.
     * Use {@code \n} to indicate multiple lines in your header. <br>
     * Default: %prefix% &amp;e[s] &amp;7= Alphanumeric\n%prefix% &amp;e[b] &amp;7= True | False\n%prefix% &amp;e[i] &amp;7= Number
     * @param header the header to be displayed in the help menu
     * @return this {@code CommandHandler}
     */
    public @NotNull CommandHandler withCommandHelpHeader(@Nullable String header) {
        this.COMMAND_HELP_HEADER = header;
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', prefix + PLAYER_ONLY));
            return true;
        }
        Player player = (Player) sender;
        internalCommandDispatch(player, label, args);
        return true;
    }

    /**
     * Handle commands dispatched by players.
     *
     * If there are no arguments, the help menu or the plugin description will be printed.
     * {@link CommandHandler#withPluginDescription(String)}
     *
     * @param player the player
     * @param label  the label
     * @param args   the arguments
     */
    private void internalCommandDispatch(@NotNull Player player, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (PLUGIN_DESCRIPTION == null) {
                printHelp(player, label);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', PLUGIN_DESCRIPTION));
            }
            return;
        }

        @Nullable AbstractCommand cmd = commands
                .stream()
                .filter(filterCmd -> filterCmd.getClass().getAnnotation(CommandInfo.class).name().equalsIgnoreCase(args[0]))
                .findAny()
                .orElse(null);
        if (cmd == null) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + NOT_A_COMMAND));
            printHelp(player, label);
            return;
        }

        CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
        if (!player.hasPermission(info.permission())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + MISSING_PERMISSION));
            return;
        }
        List<String> arguments = new ArrayList<>(Arrays.asList(args));
        arguments.remove(0);
        cmd.execute(player, arguments.toArray(new String[0]), label);
    }

    /**
     * Send the help menu to the {@link Player}.
     * @param player the player to show the help menu to
     * @param label the command label the player used
     */
    private void printHelp(Player player, String label) {
        if (COMMAND_HELP_HEADER != null) {
            for (String s : COMMAND_HELP_HEADER.split("\n")) {
                s = s.replace("%prefix%", prefix);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
            }
        }

        for (AbstractCommand acmd : commands) {
            CommandInfo info = acmd.getClass().getAnnotation(CommandInfo.class);
            String help = COMMAND_HELP_PATTERN.replace("%prefix%", prefix)
                    .replace("%label%", label)
                    .replace("%name%", info.name())
                    .replace("%args%", info.arguments())
                    .replace("%desc%", info.description())
                    .replace("%perm%", info.permission());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', help));
        }
    }
}
