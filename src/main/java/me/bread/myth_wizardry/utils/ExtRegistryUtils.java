package me.bread.myth_wizardry.utils;

import com.binaris.wizardry.api.content.spell.Element;
import com.binaris.wizardry.api.content.spell.SpellTier;
import com.binaris.wizardry.setup.registries.Elements;
import com.binaris.wizardry.setup.registries.SpellTiers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtRegistryUtils {

    public static Item getStaff(@NotNull SpellTier tier, @Nullable Element element,String mod_id) {
        if (element == null) element = Elements.MAGIC;
        String registryName = tier == SpellTiers.NOVICE && element == Elements.MAGIC ? "novice" : tier.getOrCreateLocation().getPath();
        if (element != Elements.MAGIC) registryName = registryName + "_" + element.getLocation().getPath();
        registryName = "staff_" + registryName;

        return BuiltInRegistries.ITEM.get(new ResourceLocation(mod_id, registryName));
    }

}
