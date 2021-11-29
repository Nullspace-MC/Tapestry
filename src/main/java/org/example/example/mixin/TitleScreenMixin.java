package org.example.example.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(TitleScreen.class)
public class TitleScreenMixin
{
    @ModifyConstant(
            method = "render",
            constant = @Constant(stringValue = "Minecraft 1.7.2")
    )
    private static String replaceString(String string) {
        return string + " (Fabric Modded)";
    }
}
