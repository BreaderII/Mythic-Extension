package me.bread.myth_wizardry.utils;

import me.bread.myth_wizardry.registers.ModAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class ManaData {

    public static int getCurrentMana(Player player) {
        AttributeInstance attr = player.getAttribute(ModAttributes.MANA.get());
        return (int) attr.getValue();
    }

    public static int getMaxMana(Player player) {
        AttributeInstance attr = player.getAttribute(ModAttributes.MAX_MANA.get());
        return (int) attr.getValue();
    }
    public static int getRegenMana(Player player) {
        AttributeInstance attr = player.getAttribute(ModAttributes.REGEN_MANA.get());
        return (int) attr.getValue();
    }

    public static void setCurrentMana(Player player, int mana) {
        int max = getMaxMana(player);
        int clamped = Math.max(0, Math.min(mana, max));

        AttributeInstance attr = player.getAttribute(ModAttributes.MANA.get());
        if (attr != null) {
            attr.setBaseValue(clamped);
        }
    }

    public static void setMaxMana(Player player, int maxMana) {
        AttributeInstance attr = player.getAttribute(ModAttributes.MAX_MANA.get());
        if (attr != null) {
            attr.setBaseValue(maxMana);
        }

        if (getCurrentMana(player) > maxMana) {
            setCurrentMana(player, maxMana);
        }
    }
    public static void setRegenMana(Player player, int regen) {
        AttributeInstance attr = player.getAttribute(ModAttributes.REGEN_MANA.get());
        if (attr != null) {
            attr.setBaseValue(regen);
        }
    }

    public static void regen(Player player) {
        AttributeInstance regen = player.getAttribute(ModAttributes.REGEN_MANA.get());
        AttributeInstance max = player.getAttribute(ModAttributes.MAX_MANA.get());
        AttributeInstance mana = player.getAttribute(ModAttributes.MANA.get());
        if(max.getValue() - mana.getValue() < regen.getValue()){
            mana.setBaseValue(max.getValue());
        }else {
            mana.setBaseValue(mana.getValue() + regen.getValue());
        }
    }


    public static void addMana(Player player, int amount) {
        setCurrentMana(player, getCurrentMana(player) + amount);
    }

    public static void removeMana(Player player, int amount) {
        setCurrentMana(player, getCurrentMana(player) - amount);
    }

    public static boolean hasEnoughMana(Player player, int amount) {
        return getCurrentMana(player) >= amount;
    }


    public static boolean isMage(Player player){
        AttributeInstance attr = player.getAttribute(ModAttributes.MAGE.get());
        return attr.getValue() == 1;
    }
    public static void setMage(Player player){
        AttributeInstance attr = player.getAttribute(ModAttributes.MAGE.get());
        attr.setBaseValue(1);
    }
    public static void removeMage(Player player){
        AttributeInstance attr = player.getAttribute(ModAttributes.MAGE.get());
        attr.setBaseValue(0);
    }
}