package com.chunkfetcher.items;

import com.chunkfetcher.ChunkFetcher;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ChunkFetcherItem {

    private final ChunkFetcher plugin;
    private final NamespacedKey chunkFetcherKey;
    private final ItemStack chunkFetcherItem;

    public ChunkFetcherItem(ChunkFetcher plugin) {
        this.plugin = plugin;
        this.chunkFetcherKey = new NamespacedKey(plugin, "chunk_fetcher");
        this.chunkFetcherItem = createChunkFetcherItem();
    }

    private ItemStack createChunkFetcherItem() {
        ItemStack item = new ItemStack(Material.HOPPER);
        ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(ChatColor.GOLD + "Chunk Fetcher");
        
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Collects all items dropped within the chunk");
        lore.add(ChatColor.GRAY + "Works like a normal hopper with chest connections");
        lore.add(ChatColor.GRAY + "Made by Retr0gr4d3 with <3");
        meta.setLore(lore);
        
        // Add custom tag to identify this as a chunk fetcher
        meta.getPersistentDataContainer().set(chunkFetcherKey, PersistentDataType.BYTE, (byte) 1);
        
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getChunkFetcherItem() {
        return chunkFetcherItem.clone();
    }

    public NamespacedKey getChunkFetcherKey() {
        return chunkFetcherKey;
    }

    public boolean isChunkFetcher(ItemStack item) {
        if (item == null || item.getType() != Material.HOPPER) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }
        
        return meta.getPersistentDataContainer().has(chunkFetcherKey, PersistentDataType.BYTE);
    }
    
    /**
     * Apply the chunk fetcher tag to a placed hopper block
     * 
     * @param block The hopper block to tag
     * @return true if successfully tagged, false otherwise
     */
    public boolean tagHopperBlock(Block block) {
        if (block.getType() != Material.HOPPER) {
            return false;
        }
        
        if (block.getState() instanceof TileState) {
            TileState tileState = (TileState) block.getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            
            container.set(chunkFetcherKey, PersistentDataType.BYTE, (byte) 1);
            tileState.update();
            return true;
        }
        
        return false;
    }
    
    /**
     * Check if a block is a chunk fetcher hopper
     * 
     * @param block The block to check
     * @return true if it's a chunk fetcher, false otherwise
     */
    public boolean isChunkFetcherBlock(Block block) {
        if (block.getType() != Material.HOPPER) {
            return false;
        }
        
        if (block.getState() instanceof TileState) {
            TileState tileState = (TileState) block.getState();
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            
            return container.has(chunkFetcherKey, PersistentDataType.BYTE);
        }
        
        return false;
    }
} 