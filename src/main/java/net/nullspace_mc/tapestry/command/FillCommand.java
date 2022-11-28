package net.nullspace_mc.tapestry.command;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.IncorrectUsageException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.nullspace_mc.tapestry.helpers.InventoryHelper;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;

public class FillCommand extends TapestryCommand {

    @Override
    public String getName() {
        return "fill";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return Settings.commandFill ? 2 : 5;
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/fill <x1> <y1> <z1> <x2> <y2> <z2> <block> [dataValue|state] [oldBlockHandling] [dataTag]";
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (args.length < 7) {
            throw new IncorrectUsageException(getUsage(source), new Object[0]);
        } else {
            BlockPos blockPos1 = parseBlockPos(source, args, 0);
            BlockPos blockPos2 = parseBlockPos(source, args, 3);
            Block block = parseBlock(source, args[6]);
            int meta = 0;
            if (args.length >= 8) {
                meta = parseInt(source, args[7], 0, 15);
            }

            BlockPos minPos = new BlockPos(Math.min(blockPos1.x, blockPos2.x), Math.min(blockPos1.y, blockPos2.y), Math.min(blockPos1.z, blockPos2.z));
            BlockPos maxPos = new BlockPos(Math.max(blockPos1.x, blockPos2.x), Math.max(blockPos1.y, blockPos2.y), Math.max(blockPos1.z, blockPos2.z));
            int volume = (maxPos.x-minPos.x + 1)*(maxPos.y-minPos.y + 1)*(maxPos.z-minPos.z + 1);
            if (volume > Settings.fillLimit) {
                throw new CommandException(String.format("Too many blocks in the specified area (%d > %d)", volume, Settings.fillLimit), new Object[0]);
            } else if (minPos.y >= 0 && maxPos.y < 256) {
                World world = source.getSourceWorld();

                for (int z = minPos.z; z < maxPos.z + 16; z += 16) {
                    for (int x = minPos.x; x < maxPos.x + 16; x += 16) {
                        if (!world.isLoaded(x, maxPos.y - minPos.y, z)) {
                            throw new CommandException("Cannot place blocks outside of the world", new Object[0]);
                        }
                    }
                }

                NbtCompound compoundTag = new NbtCompound();
                boolean hasTag = false;
                if (args.length >= 10 && block.hasBlockEntity()) {
                    String tagString = parseText(source, args, 9).getString();

                    try {
                        NbtElement element = StringNbtReader.parse(tagString);
                        if (!(element instanceof NbtCompound)) {
                            throw new NbtException("Not a valid tag");
                        }

                        compoundTag = (NbtCompound)element;
                        hasTag = true;
                    } catch (NbtException e) {
                        throw new CommandException(String.format("Data tag parsing failed: %s", e.getMessage()), new Object[0]);
                    }
                }

                List<BlockPos> list = Lists.newArrayList();
                volume = 0;

                for (int x = minPos.x; x <= maxPos.x; ++x) {
                    for (int y = minPos.y; y <= maxPos.y; ++y) {
                        for (int z = minPos.z; z <= maxPos.z; ++z) {
                            BlockPos pos = new BlockPos(x, y, z);

                            if (args.length >= 9) {
                                if (!args[8].equals("outline") && !args[8].equals("hollow")) {
                                    if (args[8].equals("destroy")) {
                                        world.breakBlock(x, y, z, true);
                                    } else if (args[8].equals("keep")) {
                                        if (!world.isAir(x, y, z)) {
                                            continue;
                                        }
                                    } else if (args[8].equals("replace") && !block.hasBlockEntity()) {
                                        if (args.length > 9) {
                                            Block replaceTarget = parseBlock(source, args[9]);
                                            if (world.getBlock(x, y, z) != replaceTarget) {
                                                continue;
                                            }
                                        }

                                        if (args.length > 10) {
                                            int targetMeta = parseInt(source, args[10], 0, 15);
                                            if (world.getBlockMetadata(x, y, z) != targetMeta) {
                                                continue;
                                            }
                                        }
                                    }
                                } else if (x != minPos.x && x != maxPos.x && y != minPos.y && y != maxPos.y && z != minPos.z && z != maxPos.z) {
                                    if (args[8].equals("hollow")) {
                                        SetBlockHelper.applyFillOrientationFixRule = true;
                                        SetBlockHelper.applyFillUpdatesRule = true;
                                        world.setBlockWithMetadata(x, y, z, Blocks.AIR, 0, 2);
                                        list.add(pos);
                                    }
                                    continue;
                                }
                            }

                            BlockEntity blockEntity = world.getBlockEntity(x, y, z);
                            if (blockEntity != null) {
                                if (blockEntity instanceof Inventory) {
                                    InventoryHelper.clearInventory((Inventory)blockEntity);
                                }

                                SetBlockHelper.applyFillOrientationFixRule = true;
                                SetBlockHelper.applyFillUpdatesRule = true;
                                world.setBlockWithMetadata(x, y, z, Blocks.AIR, 0, 2);
                                if (block == Blocks.AIR) ++volume;
                            }

                            SetBlockHelper.applyFillOrientationFixRule = true;
                            SetBlockHelper.applyFillUpdatesRule = true;
                            if (world.setBlockWithMetadata(x, y, z, block, meta, 2)) {
                                list.add(pos);
                                ++volume;
                                if (hasTag) {
                                    blockEntity = world.getBlockEntity(x, y, z);
                                    if (blockEntity != null) {
                                        compoundTag.putInt("x", x);
                                        compoundTag.putInt("y", y);
                                        compoundTag.putInt("z", z);
                                        blockEntity.readNbt(compoundTag);
                                    }
                                }
                            }
                        }
                    }
                }

                Iterator iterator = list.iterator();

                if (Settings.fillUpdates) {
                    while (iterator.hasNext()) {
                        BlockPos pos = (BlockPos)iterator.next();
                        Block affectedBlock = world.getBlock(pos.x, pos.y, pos.z);
                        world.updateNeighbors(pos.x, pos.y, pos.z, affectedBlock);
                    }
                }

                if (volume <= 0) {
                    throw new CommandException("No blocks filled", new Object[0]);
                } else {
                    sendSuccess(source, String.format("%d blocks filled", volume), new Object[0]);
                }
            } else {
                throw new CommandException("Cannot place blocks outside of the world", new Object[0]);
            }
        }
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (!Settings.commandFill) return Collections.emptyList();
        if (args.length > 0 && args.length <= 3) {
            return suggestCoordinates(source, args, 0);
        } else if (args.length > 3 && args.length <= 6) {
            return suggestCoordinates(source, args, 3);
        } else if (args.length == 7) {
            return suggestMatching(args, Block.REGISTRY.keySet());
        } else if (args.length == 9) {
            return suggestMatching(args, new String[]{"replace", "destroy", "keep", "hollow", "outline"});
        } else {
            return args.length == 10 && "replace".equals(args[8]) ? suggestMatching(args, Block.REGISTRY.keySet()) : null;
        }
    }
}
