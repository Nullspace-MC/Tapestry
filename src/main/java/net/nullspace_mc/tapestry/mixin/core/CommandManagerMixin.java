package net.nullspace_mc.tapestry.mixin.core;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.CommandRegistry;
import net.nullspace_mc.tapestry.command.CloneCommand;
import net.nullspace_mc.tapestry.command.CounterCommand;
import net.nullspace_mc.tapestry.command.FillCommand;
import net.nullspace_mc.tapestry.command.LogCommand;
import net.nullspace_mc.tapestry.command.TapCommand;
import net.nullspace_mc.tapestry.command.TickCommand;
import net.nullspace_mc.tapestry.command.InfoCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin extends CommandRegistry {

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void registerCommands(CallbackInfo ci) {
        this.registerCommand(new CloneCommand());
        this.registerCommand(new CounterCommand());
        this.registerCommand(new FillCommand());
        this.registerCommand(new InfoCommand());
        this.registerCommand(new LogCommand());
        this.registerCommand(new TapCommand());
        this.registerCommand(new TickCommand());
    }
}
