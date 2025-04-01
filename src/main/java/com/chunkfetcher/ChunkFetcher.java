package com.chunkfetcher;

import com.chunkfetcher.commands.ChunkFetcherCommand;
import com.chunkfetcher.items.ChunkFetcherItem;
import com.chunkfetcher.listeners.ChunkFetcherListener;
import com.chunkfetcher.recipes.ChunkFetcherRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkFetcher extends JavaPlugin {

    private static ChunkFetcher instance;
    private ChunkFetcherItem chunkFetcherItem;

    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize the chunk fetcher item
        chunkFetcherItem = new ChunkFetcherItem(this);
        
        // Register the crafting recipe
        new ChunkFetcherRecipe(this).register();
        
        // Register event listeners
        getServer().getPluginManager().registerEvents(new ChunkFetcherListener(this), this);
        
        // Register commands
        getCommand("chunkfetcher").setExecutor(new ChunkFetcherCommand(this));
        
        getLogger().info("ChunkFetcher plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ChunkFetcher plugin has been disabled!");
    }

    public static ChunkFetcher getInstance() {
        return instance;
    }
    
    public ChunkFetcherItem getChunkFetcherItem() {
        return chunkFetcherItem;
    }
} 