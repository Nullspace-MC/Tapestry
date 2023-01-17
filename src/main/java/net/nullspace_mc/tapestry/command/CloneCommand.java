package net.nullspace_mc.tapestry.command;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.exception.IncorrectUsageException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.world.ScheduledTick;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBox;

import net.nullspace_mc.tapestry.helpers.InventoryHelper;
import net.nullspace_mc.tapestry.helpers.ServerWorldHelper;
import net.nullspace_mc.tapestry.helpers.SetBlockFlags;
import net.nullspace_mc.tapestry.helpers.SetBlockHelper;
import net.nullspace_mc.tapestry.settings.Settings;

public class CloneCommand extends TapestryCommand {

    @Override
    public String getName() {
        return "clone";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return Settings.commandClone ? 2 : 5;
    }

    @Override
    public String getUsage(CommandSource source) {
        return "/clone <x1> <y1> <z1> <x2> <y2> <z2> <x> <y> <z> [maskMode] [cloneMode]";
    }

    @Override
    public void run(CommandSource source, String[] args) {
        if (args.length < 9) {
            throw new IncorrectUsageException(getUsage(source), new Object[0]);
        } else {
            BlockPos blockPos1 = parseBlockPos(source, args, 0);
            BlockPos blockPos2 = parseBlockPos(source, args, 3);
            BlockPos destPos = parseBlockPos(source, args, 6);
            StructureBox sourceBox = new StructureBox(Math.min(blockPos1.x, blockPos2.x), Math.min(blockPos1.y, blockPos2.y), Math.min(blockPos1.z, blockPos2.z), Math.max(blockPos1.x, blockPos2.x), Math.max(blockPos1.y, blockPos2.y), Math.max(blockPos1.z, blockPos2.z));
            StructureBox destBox = new StructureBox(destPos.x, destPos.y, destPos.z, destPos.x + sourceBox.getSpanX() - 1, destPos.y + sourceBox.getSpanY() - 1, destPos.z + sourceBox.getSpanZ() - 1);
            int volume = sourceBox.getSpanX() * sourceBox.getSpanY() * sourceBox.getSpanZ();
            if (volume > Settings.fillLimit) {
                throw new CommandException(String.format("Too many blocks in the specified area (%d > %d)", volume, Settings.fillLimit), new Object[0]);
            } else {
                boolean bl = false;
                Block filterBlock = null;
                int filterMeta = -1;
                if ((args.length < 11 || !args[10].equals("force") && !args[10].equals("move")) && sourceBox.intersects(destBox)) {
                    throw new CommandException("Source and destination can not overlap", new Object[0]);
                } else {
                    if (args.length >= 11 && args[10].equals("move")) {
                        bl = true;
                    }

                    if (sourceBox.minY >= 0 && sourceBox.maxY < 256 && destBox.minY >= 0 && destBox.maxY < 256) {
                        World world = source.getSourceWorld();
                        if (world.isRegionLoaded(sourceBox.minX, sourceBox.minY, sourceBox.minZ, sourceBox.maxX, sourceBox.maxY, sourceBox.maxZ) && world.isRegionLoaded(destBox.minX, destBox.minY, destBox.minZ, destBox.maxX, destBox.maxY, destBox.maxZ)) {
                            boolean bl2 = false;
                            if (args.length >= 10) {
                                if (args[9].equals("masked")) {
                                    bl2 = true;
                                } else if (args[9].equals("filtered")) {
                                    if (args.length < 12) {
                                        throw new IncorrectUsageException(getUsage(source), new Object[0]);
                                    }

                                    filterBlock = parseBlock(source, args[11]);
                                    if (args.length >= 13) {
                                        filterMeta = parseInt(source, args[12], 0, 15);
                                    }
                                }
                            }

                            List<CloneCommand.BlockInfo> list1 = Lists.newArrayList();
                            List<CloneCommand.BlockInfo> list2 = Lists.newArrayList();
                            List<CloneCommand.BlockInfo> list3 = Lists.newArrayList();
                            LinkedList<BlockPos> linkedList = Lists.newLinkedList();
                            BlockPos translate = new BlockPos(destBox.minX - sourceBox.minX, destBox.minY - sourceBox.minY, destBox.minZ - sourceBox.minZ);

                            for (int z = sourceBox.minZ; z <= sourceBox.maxZ; ++z) {
                                for (int y = sourceBox.minY; y <= sourceBox.maxY; ++y) {
                                    for (int x = sourceBox.minX; x <= sourceBox.maxX; ++x) {
                                        BlockPos src = new BlockPos(x, y, z);
                                        BlockPos dest = new BlockPos(x + translate.x, y + translate.y, z + translate.z);
                                        Block block = world.getBlock(x, y, z);
                                        int meta = world.getBlockMetadata(x, y, z);
                                        if ((!bl2 || block != Blocks.AIR) && (filterBlock == null || block == filterBlock && (filterMeta < 0 || meta == filterMeta))) {
                                            BlockEntity blockEntity = world.getBlockEntity(x, y, z);
                                            if (blockEntity != null) {
                                                NbtCompound compoundTag = new NbtCompound();
                                                blockEntity.writeNbt(compoundTag);
                                                list2.add(new CloneCommand.BlockInfo(dest, block, meta, compoundTag));
                                            } else if (!block.isOpaque() && !block.isFullCube()) {
                                                list3.add(new CloneCommand.BlockInfo(dest, block, meta, null));
                                                linkedList.addFirst(src);
                                            } else {
                                                list1.add(new CloneCommand.BlockInfo(dest, block, meta, null));
                                                linkedList.addLast(src);
                                            }
                                        }
                                    }
                                }
                            }

                            if (bl) {
                                Iterator iter;
                                BlockPos blockPos;
                                for(iter = linkedList.iterator(); iter.hasNext();) {
                                    blockPos = (BlockPos)iter.next();
                                    BlockEntity blockEntity = world.getBlockEntity(blockPos.x, blockPos.y, blockPos.z);
                                    if (blockEntity instanceof Inventory) {
                                        InventoryHelper.clearInventory((Inventory)blockEntity);
                                    }
                                    setBlock(world, blockPos.x, blockPos.y, blockPos.z, Blocks.AIR, 0);
                                }
                            }

                            List<CloneCommand.BlockInfo> list4 = Lists.newArrayList();
                            list4.addAll(list1);
                            list4.addAll(list2);
                            list4.addAll(list3);
                            List<CloneCommand.BlockInfo> list5 = Lists.reverse(list4);

                            Iterator iter;
                            CloneCommand.BlockInfo info;
                            BlockEntity blockEntity;
                            for (iter = list5.iterator(); iter.hasNext();) {
                                info = (CloneCommand.BlockInfo)iter.next();
                                blockEntity = world.getBlockEntity(info.pos.x, info.pos.y, info.pos.z);
                                if (blockEntity instanceof Inventory) {
                                    InventoryHelper.clearInventory((Inventory)blockEntity);
                                }
                                setBlock(world, info.pos.x, info.pos.y, info.pos.z, Blocks.AIR, 0);
                            }

                            volume = 0;
                            iter = list4.iterator();

                            while (iter.hasNext()) {
                                info = (CloneCommand.BlockInfo)iter.next();
                                if (setBlock(world, info.pos.x, info.pos.y, info.pos.z, info.block, info.meta)) {
                                    ++volume;
                                }
                            }

                            for (iter = list2.iterator(); iter.hasNext();) {
                                info = (CloneCommand.BlockInfo)iter.next();
                                blockEntity = world.getBlockEntity(info.pos.x, info.pos.y, info.pos.z);
                                if(info != null && blockEntity != null) {
                                    info.nbt.putInt("x", info.pos.x);
                                    info.nbt.putInt("y", info.pos.y);
                                    info.nbt.putInt("z", info.pos.z);
                                    blockEntity.readNbt(info.nbt);
                                    blockEntity.markDirty();
                                }
                                setBlock(world, info.pos.x, info.pos.y, info.pos.z, info.block, info.meta);
                            }

                            if (Settings.fillUpdates) {
                                iter = list5.iterator();

                                while (iter.hasNext()) {
                                    info = (CloneCommand.BlockInfo)iter.next();
                                    world.updateNeighbors(info.pos.x, info.pos.y, info.pos.z, info.block);
                                }

                                if (world instanceof ServerWorldHelper) {
                                    List<ScheduledTick> scheduledTicks = ((ServerWorldHelper)world).getScheduledTicksInBox(sourceBox);

                                    if (scheduledTicks != null) {
                                        iter = scheduledTicks.iterator();

                                        while (iter.hasNext()) {
                                            ScheduledTick tick = (ScheduledTick)iter.next();
                                            if (sourceBox.contains(tick.x, tick.y, tick.z)) {
                                                world.scheduleTick(tick.x + translate.x, tick.y + translate.y, tick.z + translate.x, tick.getBlock(), (int)(tick.time - world.getData().getTime()), tick.priority);
                                            }
                                        }
                                    }
                                }
                            }

                            if (volume <= 0) {
                                throw new CommandException("No blocks cloned", new Object[0]);
                            } else {
                                sendSuccess(source, String.format("%d blocks cloned", volume), new Object[0]);
                            }
                        } else {
                            throw new CommandException("Cannot access blocks outside of the world", new Object[0]);
                        }
                    } else {
                        throw new CommandException("Cannot access blocks outside of the world", new Object[0]);
                    }
                }
            }
        }
    }

    private boolean setBlock(World world, int x, int y, int z, Block block, int metadata) {
        int flags = SetBlockFlags.UPDATE_CLIENTS;

        if (Settings.fillUpdates) {
            flags |= SetBlockFlags.UPDATE_NEIGHBORS; // enabled UPDATE_NEIGHBORS flag
        }

        SetBlockHelper.applyFillOrientationFixRule = true;
        SetBlockHelper.applyFillUpdatesRule = true;

        return world.setBlockWithMetadata(x, y, z, block, metadata, flags);
    }

    @Override
    public List getSuggestions(CommandSource source, String[] args) {
        if (!Settings.commandClone) return Collections.emptyList();
        if (args.length > 0 && args.length <= 3) {
            return suggestCoordinates(source, args, 0);
        } else if (args.length > 3 && args.length <= 6) {
            return suggestCoordinates(source, args, 3);
        } else if (args.length > 6 && args.length <= 9) {
            return suggestCoordinates(source, args, 6);
        } else if (args.length == 10) {
            return suggestMatching(args, new String[]{"replace", "masked", "filtered"});
        } else if (args.length == 11) {
            return suggestMatching(args, new String[]{"normal", "force", "move"});
        } else {
            return args.length == 12 && "filtered".equals(args[9]) ? suggestMatching(args, Block.REGISTRY.keySet()) : null;
        }
    }

    static class BlockInfo {
        public final BlockPos pos;
        public final Block block;
        public final int meta;
        public final NbtCompound nbt;

        public BlockInfo(BlockPos p, Block b, int m, NbtCompound n) {
            this.pos = p;
            this.block = b;
            this.meta = m;
            this.nbt = n;
        }
    }
}
