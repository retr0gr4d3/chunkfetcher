package com.chunkfetcher.listeners;

import com.chunkfetcher.ChunkFetcher;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkFetcherListener implements Listener {

    private final ChunkFetcher plugin;
    private final Map<Chunk, Location> chunkFetcherLocations = new ConcurrentHashMap<>();

    public ChunkFetcherListener(ChunkFetcher plugin) {
        this.plugin = plugin;
        
        // Schedule task to collect items in chunks with chunk fetchers
        new BukkitRunnable() {
            @Override
            public void run() {
                collectItemsInChunks();
            }
        }.runTaskTimer(plugin, 20L, 10L); // Run every half second (10 ticks)
    }

    private void collectItemsInChunks() {
        for (Map.Entry<Chunk, Location> entry : chunkFetcherLocations.entrySet()) {
            Chunk chunk = entry.getKey();
            Location hopperLoc = entry.getValue();
            
            // Skip if the chunk is not loaded
            if (!chunk.isLoaded()) {
                continue;
            }
            
            Block hopperBlock = hopperLoc.getBlock();
            
            // Skip if the hopper no longer exists or is no longer a chunk fetcher
            if (hopperBlock.getType() != Material.HOPPER || 
                !plugin.getChunkFetcherItem().isChunkFetcherBlock(hopperBlock)) {
                chunkFetcherLocations.remove(chunk);
                continue;
            }
            
            // Get all item entities in the chunk
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    
                    // Skip if the item is already being collected
                    if (!item.isValid()) {
                        continue;
                    }
                    
                    Hopper hopper = (Hopper) hopperBlock.getState();
                    Inventory hopperInventory = hopper.getInventory();
                    
                    // Try to add the item to the hopper
                    HashMap<Integer, ItemStack> leftover = new HashMap<>();
                    leftover.putAll(hopperInventory.addItem(item.getItemStack()));
                    
                    if (leftover.isEmpty()) {
                        // Item was fully added to the hopper
                        item.remove();
                    } else {
                        // Update the dropped item with the leftover amount
                        item.setItemStack(leftover.get(0));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        
        // Scan the chunk for a ChunkFetcher
        new BukkitRunnable() {
            @Override
            public void run() {
                scanChunkForChunkFetcher(chunk);
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();
        
        // Check if player is placing a chunk fetcher
        if (block.getType() == Material.HOPPER && plugin.getChunkFetcherItem().isChunkFetcher(item)) {
            // Tag the placed hopper block as a chunk fetcher
            if (plugin.getChunkFetcherItem().tagHopperBlock(block)) {
                chunkFetcherLocations.put(block.getChunk(), block.getLocation());
                plugin.getLogger().info("ChunkFetcher placed in chunk at: " + block.getLocation());
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();
        
        // Check if a chunk fetcher hopper was broken
        if (block.getType() == Material.HOPPER && 
            plugin.getChunkFetcherItem().isChunkFetcherBlock(block)) {
            chunkFetcherLocations.remove(chunk);
            plugin.getLogger().info("ChunkFetcher removed from chunk at: " + block.getLocation());
        }
    }

    private void scanChunkForChunkFetcher(Chunk chunk) {
        // Check if we already have a chunk fetcher in this chunk
        if (chunkFetcherLocations.containsKey(chunk)) {
            return;
        }

        int minY = chunk.getWorld().getMinHeight();
        int maxY = chunk.getWorld().getMaxHeight();
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    Block block = chunk.getBlock(x, y, z);
                    
                    if (block.getType() == Material.HOPPER && 
                        plugin.getChunkFetcherItem().isChunkFetcherBlock(block)) {
                        chunkFetcherLocations.put(chunk, block.getLocation());
                        plugin.getLogger().info("Found existing ChunkFetcher in chunk at: " + block.getLocation());
                        return;
                    }
                }
            }
        }
    }
} 