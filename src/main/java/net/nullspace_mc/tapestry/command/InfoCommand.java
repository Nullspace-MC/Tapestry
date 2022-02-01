package net.nullspace_mc.tapestry.command;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.IncorrectUsageException;
import net.minecraft.command.NotFoundException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.nullspace_mc.tapestry.mixin.core.BlockEntityMixin;
import net.nullspace_mc.tapestry.settings.Settings;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InfoCommand extends TapestryAbstractCommand {
    @Override
    public String getName() {
        return "info";
    }

    @Override
    public int getPermissionLevel() {
        return Settings.commandInfo ? 2 : 5;
    }

    @Override
    public String getUsageTranslationKey(CommandSource source) {
        return "/info <type> <target>";
    }

    @Override
    public void execute(CommandSource source, String[] args) {
        if (args[0].equals("block")) {
            if (args.length != 4) throw new IncorrectUsageException(getUsageTranslationKey(source));
            sendBlockInfo(source, args);
        } else {
            throw new IncorrectUsageException("Target not found");
        }
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (!Settings.commandInfo) return Collections.emptyList();
        ArrayList<String> suggestions = new ArrayList<>();
        String prefix = args[args.length-1].toLowerCase();
        if (args.length == 1) suggestions.addAll(Arrays.asList("block"));
        else if (args.length <= 4) suggestions.addAll(getCoordinateSuggestions(source, args, 1));
        suggestions.removeIf(suggestion -> !suggestion.startsWith(prefix));
        return suggestions;
    }

    private void sendBlockInfo(CommandSource source, String[] args) {
        BlockPos pos = parseBlockPos(source, args, 1);
        World world = source.getWorld();
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        source.sendMessage(new LiteralText(String.format("Block > %s : %d", block.getTranslatedName(), world.getBlockMetadata(pos.x, pos.y, pos.z))));
        if (block instanceof BlockWithEntity) {
            BlockEntity blockEntity = world.getBlockEntity(pos.x, pos.y, pos.z);
            source.sendMessage(new LiteralText("Block Entity > " + ((BlockEntityMixin)blockEntity).getTypeToId().get(blockEntity.getClass())));
            if (implementsInventory(blockEntity.getClass())) {
                Inventory inv = (Inventory) blockEntity;
                StringBuilder bobTheBuilder = new StringBuilder("Inventory > ");
                for (int i = 1; i < inv.getInvSize() + 1; i++) {
                    ItemStack item = inv.getInvStack(i - 1);
                    if (item != null)   {
                        bobTheBuilder.append(String.format("[slot %d: %s, count: %d]", i, item.getName(), item.count));
                        if (i != inv.getInvSize()) bobTheBuilder.append(", ");
                    }
                }
                if (bobTheBuilder.toString().equals("Inventory > ")) bobTheBuilder.append("The inventory is empty");
                source.sendMessage(new LiteralText(bobTheBuilder.toString()));
            }
        }
    }

    private boolean implementsInventory(Class<?> clazz) {
        if (ArrayUtils.contains(clazz.getInterfaces(), Inventory.class)) return true;
        for (Class<?> classToCheck : clazz.getInterfaces()) if (implementsInventory(classToCheck)) return true;
        return false;
    }
}
