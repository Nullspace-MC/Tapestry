package net.nullspace_mc.tapestry.mixin.core;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import net.nullspace_mc.tapestry.command.CounterCommand;
import net.nullspace_mc.tapestry.command.LogCommand;
import net.nullspace_mc.tapestry.command.TapCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin extends CommandRegistry {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerCommands(CallbackInfo ci) {
        this.registerCommand(new TapCommand());
        this.registerCommand(new LogCommand());
        this.registerCommand(new CounterCommand());
    }
}
