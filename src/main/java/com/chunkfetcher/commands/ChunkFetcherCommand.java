package com.chunkfetcher.commands;

import com.chunkfetcher.ChunkFetcher;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChunkFetcherCommand implements CommandExecutor {

    private final ChunkFetcher plugin;

    public ChunkFetcherCommand(ChunkFetcher plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3 || !args[0].equalsIgnoreCase("add")) {
            sender.sendMessage(ChatColor.RED + "Usage: /chunkfetcher add <player> <amount>");
            return true;
        }

        String playerName = args[1];
        Player target = Bukkit.getPlayer(playerName);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + playerName + " is not online or doesn't exist!");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
            if (amount <= 0) {
                sender.sendMessage(ChatColor.RED + "Amount must be a positive number!");
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be a valid number!");
            return true;
        }

        ItemStack chunkFetcher = plugin.getChunkFetcherItem().getChunkFetcherItem();
        chunkFetcher.setAmount(amount);

        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItem(target.getLocation(), chunkFetcher);
            target.sendMessage(ChatColor.GREEN + "You received " + amount + " Chunk Fetcher(s), but your inventory was full. They were dropped at your feet.");
        } else {
            target.getInventory().addItem(chunkFetcher);
            target.sendMessage(ChatColor.GREEN + "You received " + amount + " Chunk Fetcher(s)!");
        }

        sender.sendMessage(ChatColor.GREEN + "Gave " + amount + " Chunk Fetcher(s) to " + target.getName() + "!");
        return true;
    }
} 