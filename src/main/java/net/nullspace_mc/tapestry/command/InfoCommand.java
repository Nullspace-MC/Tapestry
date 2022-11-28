package net.nullspace_mc.tapestry.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.exception.IncorrectUsageException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.nullspace_mc.tapestry.mixin.feature.infocommand.BlockEntityMixin;
import net.nullspace_mc.tapestry.settings.Settings;

public class InfoCommand extends TapestryCommand {

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return Settings.commandInfo ? 2 : 5;
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/info <type> <target>";
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (args[0].equals("block")) {
            if (args.length != 4) throw new IncorrectUsageException(getUsage(source));
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
        else if (args.length <= 4) suggestions.addAll(suggestCoordinates(source, args, 1));
        suggestions.removeIf(suggestion -> !suggestion.startsWith(prefix));
        return suggestions;
    }

    private void sendBlockInfo(CommandSource source, String[] args) {
        BlockPos pos = parseBlockPos(source, args, 1);
        World world = source.getSourceWorld();
        Block block = world.getBlock(pos.x, pos.y, pos.z);
        source.sendMessage(new LiteralText(String.format("Block > %s : %d", block.getName(), world.getBlockMetadata(pos.x, pos.y, pos.z))));
        if (block instanceof BlockWithBlockEntity) {
            BlockEntity blockEntity = world.getBlockEntity(pos.x, pos.y, pos.z);
            source.sendMessage(new LiteralText("Block Entity > " + BlockEntityMixin.getTypeToId().get(blockEntity.getClass())));
            if (implementsInventory(blockEntity.getClass())) {
                Inventory inv = (Inventory) blockEntity;
                StringBuilder bobTheBuilder = new StringBuilder("Inventory > ");
                for (int i = 1; i < inv.getSize() + 1; i++) {
                    ItemStack item = inv.getStack(i - 1);
                    if (item != null)   {
                        bobTheBuilder.append(String.format("[slot %d: %s, count: %d]", i, item.getDisplayName(), item.size));
                        if (i != inv.getSize()) bobTheBuilder.append(", ");
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
