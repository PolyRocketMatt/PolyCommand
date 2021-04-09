package com.github.polyrocketmatt.polycommand;

import org.bukkit.ChatColor;

public class Utils {

    public static String translate(String message) {
        return message.replace("&4", ChatColor.DARK_RED + "")
                .replace("&c", ChatColor.RED + "")
                .replace("&6", ChatColor.GOLD + "")
                .replace("&e", ChatColor.YELLOW + "")
                .replace("&2", ChatColor.DARK_GREEN + "")
                .replace("&a", ChatColor.GREEN + "")
                .replace("&b", ChatColor.AQUA + "")
                .replace("&3", ChatColor.DARK_AQUA + "")
                .replace("&1", ChatColor.DARK_BLUE + "")
                .replace("&9", ChatColor.BLUE + "")
                .replace("&d", ChatColor.LIGHT_PURPLE + "")
                .replace("&5", ChatColor.DARK_PURPLE + "")
                .replace("&f", ChatColor.WHITE + "")
                .replace("&7", ChatColor.GRAY + "")
                .replace("&8", ChatColor.DARK_GRAY + "")
                .replace("&0", ChatColor.BLACK + "")
                .replace("&k", ChatColor.MAGIC + "")
                .replace("&l", ChatColor.BOLD + "")
                .replace("&m", ChatColor.STRIKETHROUGH + "")
                .replace("&n", ChatColor.UNDERLINE + "")
                .replace("&o", ChatColor.ITALIC + "")
                .replace("&r", ChatColor.RESET + "");
    }

}
