package net.nullspace_mc.tapestry.command;

import net.minecraft.command.AbstractCommand;
import net.minecraft.command.Command;

public abstract class TapestryAbstractCommand extends AbstractCommand {
    /**
     * Added so the compiler would stop crying
     */
    public int compareTo(Object object) {
        if(object instanceof Command) {
            return super.compareTo((Command)object);
        } else {
            return this.toString().compareTo(object.toString());
        }
    }
}
