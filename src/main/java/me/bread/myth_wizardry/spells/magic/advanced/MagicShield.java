package me.bread.myth_wizardry.spells.magic.advanced;

import com.binaris.wizardry.content.spell.abstr.ConstructSpell;

import java.util.function.Function;

public class MagicShield extends ConstructSpell {

    public MagicShield(Function constructFactory, boolean permanent) {
        super(constructFactory, permanent);
    }
}
