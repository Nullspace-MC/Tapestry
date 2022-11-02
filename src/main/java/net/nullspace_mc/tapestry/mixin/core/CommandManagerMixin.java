package net.nullspace_mc.tapestry.mixin.core;

import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.handler.CommandRegistry;
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
    private void registers(CallbackInfo ci) {
        this.register(new CloneCommand());
        this.register(new CounterCommand());
        this.register(new FillCommand());
        this.register(new InfoCommand());
        this.register(new LogCommand());
        this.register(new TapCommand());
        this.register(new TickCommand());
    }
}
