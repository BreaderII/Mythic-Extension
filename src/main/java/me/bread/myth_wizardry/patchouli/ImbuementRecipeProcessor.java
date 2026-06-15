package me.bread.myth_wizardry.patchouli;

import com.binaris.wizardry.content.recipe.ImbuementAltarRecipe;
import com.binaris.wizardry.setup.registries.EBRecipeTypes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.*;

import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;

public class ImbuementRecipeProcessor implements IComponentProcessor {

    private ImbuementAltarRecipe recipe;

    @Override
    public void setup(Level level, IVariableProvider variables) {
        String recipeId = variables.get("recipe").asString();

        RecipeManager manager = level.getRecipeManager();

        recipe = manager.byKey(new ResourceLocation(recipeId))
                .filter(r -> r instanceof ImbuementAltarRecipe)
                .map(r -> (ImbuementAltarRecipe) r)
                .orElse(null);
    }

    @Override
    public IVariable process(Level level, String key) {
        if (recipe == null)
            return null;

        return switch (key) {
            case "center" ->
                    IVariable.from(recipe.getCenterIngredient());

            case "r0" ->
                    IVariable.from(recipe.getReceptacleIngredients().get(0));

            case "r1" ->
                    IVariable.from(recipe.getReceptacleIngredients().get(1));

            case "r2" ->
                    IVariable.from(recipe.getReceptacleIngredients().get(2));

            case "r3" ->
                    IVariable.from(recipe.getReceptacleIngredients().get(3));

            case "output" ->
                    IVariable.from(recipe.getResultItem(level.registryAccess()));

            default -> null;
        };
    }
}