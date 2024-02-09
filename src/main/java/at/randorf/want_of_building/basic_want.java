package at.randorf.want_of_building;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class basic_want extends Item {
    public basic_want(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            BlockPos start_pos = context.getBlockPos();
            BlockState start_block = context.getWorld().getBlockState(start_pos);
            Item blockItem = start_block.getBlock().asItem();
            World world = context.getWorld();
            ArrayList<BlockPos> validBlocks = new ArrayList<>();
            ArrayList<BlockPos> extrude = scan_blocks(world, start_pos, validBlocks, context.getSide(),start_block);
            extrude.forEach((BlockPos block) -> {
                if(!context.getPlayer().isCreative() &&get_item_count(blockItem, context.getPlayer()) != 0){
                    world.setBlockState(block, start_block);
                    removeItem (context.getPlayer(),blockItem.getDefaultStack(), 1);
                    context.getStack().damage(1, context.getPlayer(),
                            playerEntity -> playerEntity.sendToolBreakStatus(playerEntity.getActiveHand()));
                } else if (context.getPlayer().isCreative()) {
                    world.setBlockState(block, start_block);
                }
            });
        }
        return ActionResult.SUCCESS;
    }

    private ArrayList<BlockPos> scan_blocks(World world, BlockPos origin, ArrayList<BlockPos> validBlocks, Direction dir,BlockState block) {
        RelativeDirResult result = calculateRelativeDirections(dir, origin);

        ArrayList<BlockPos> extruded = new ArrayList<>();

        boolean isValid = !world.getBlockState(origin).isAir() && world.getBlockState(result.extruded).isAir()&&block.equals(world.getBlockState(origin));
        if ((validBlocks.contains(origin) || (validBlocks.size() >= 1024)) || !isValid) {
            return extruded;
        }
        if (isValid) {
            validBlocks.add(origin);
            extruded.add(result.extruded);
        }

        result.dirs.forEach((BlockPos newOrigin) -> {
            extruded.addAll(scan_blocks(world, newOrigin, validBlocks, dir,block));
        });
        return extruded;
    }

    private RelativeDirResult calculateRelativeDirections(Direction dir, BlockPos origin) {
        BlockPos extruded = null;
        ArrayList<BlockPos> neighbourBlocks = null;
        if (dir.equals(Direction.EAST) || dir.equals(Direction.WEST)) {
            neighbourBlocks = new ArrayList<>(Arrays.asList(
                    origin.up(),
                    origin.down(),
                    origin.north(),
                    origin.south()
            ));
        }
        if (dir.equals(Direction.EAST)) {
            extruded = origin.east();
        } else if (dir.equals(Direction.WEST)) {
            extruded = origin.west();
        }
        if (dir.equals(Direction.UP) || dir.equals(Direction.DOWN)) {
            neighbourBlocks = new ArrayList<>(Arrays.asList(
                    origin.north(),
                    origin.south(),
                    origin.east(),
                    origin.west()
            ));
        }
        if (dir.equals(Direction.UP)) {
            extruded = origin.up();
        } else if (dir.equals(Direction.DOWN)) {
            extruded = origin.down();
        }

        if (dir.equals(Direction.NORTH) || dir.equals(Direction.SOUTH)) {
            neighbourBlocks = new ArrayList<>(Arrays.asList(
                    origin.up(),
                    origin.down(),
                    origin.east(),
                    origin.west()
            ));
        }
        if (dir.equals(Direction.NORTH)) {
            extruded = origin.north();
        } else if (dir.equals(Direction.SOUTH)) {
            extruded = origin.south();
        }
        return new RelativeDirResult(extruded, neighbourBlocks);
    }

    private void remove_item(Block block, PlayerEntity player, int count) {
        if (get_item_count(block.asItem(), player) >= count && !player.isCreative()) {
            removeItem(player, block.asItem().getDefaultStack(), count);
        }
    }

    private int get_item_count(Item item, PlayerEntity player) {
        int count = 0;
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static void removeItem(PlayerEntity player, ItemStack item, int count) {
        int totalCount = count;
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == item.getItem()) {
                if (stack.getCount() >= totalCount) {
                    stack.decrement(totalCount);
                    totalCount = 0;
                    break;
                } else {
                    totalCount -= stack.getCount();
                    stack.setCount(0);
                }
            }
        }
    }

    class RelativeDirResult {
        private BlockPos extruded;
        private ArrayList<BlockPos> dirs;

        public BlockPos getExtruded() {
            return extruded;
        }

        public ArrayList<BlockPos> getDirs() {
            return dirs;
        }

        public RelativeDirResult(BlockPos extruded, ArrayList<BlockPos> dirs) {
            this.extruded = extruded;
            this.dirs = dirs;
        }
    }
}
