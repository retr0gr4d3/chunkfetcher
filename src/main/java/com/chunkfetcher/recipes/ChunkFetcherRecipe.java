package com.chunkfetcher.recipes;

import com.chunkfetcher.ChunkFetcher;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

public class ChunkFetcherRecipe {

    private final ChunkFetcher plugin;
    private final NamespacedKey recipeKey;

    public ChunkFetcherRecipe(ChunkFetcher plugin) {
        this.plugin = plugin;
        this.recipeKey = new NamespacedKey(plugin, "chunk_fetcher_recipe");
    }

    public void register() {
        ItemStack chunkFetcher = plugin.getChunkFetcherItem().getChunkFetcherItem();
        
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, chunkFetcher);
        
        // Recipe: 8 hoppers surrounding 1 ender pearl
        recipe.shape("HHH", "HEH", "HHH");
        recipe.setIngredient('H', Material.HOPPER);
        recipe.setIngredient('E', Material.ENDER_PEARL);
        
        plugin.getServer().addRecipe(recipe);
        plugin.getLogger().info("ChunkFetcher recipe registered!");
    }
} 