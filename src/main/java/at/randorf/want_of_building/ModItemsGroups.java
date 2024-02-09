package at.randorf.want_of_building;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class ModItemsGroups {
    public static final ItemGroup RUBY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Want_of_building.MOD_ID, "ruby"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ruby"))
                    .icon(() -> new ItemStack(ModItems.BASIC_WANT)).entries((displayContext, entries) -> {
                        entries.add(ModItems.BASIC_WANT_HEAD);
                        entries.add(ModItems.BASIC_WANT);
                    }).build());


    public static void registerItemGroups() {
        Want_of_building.LOGGER.info("Registering Item Groups for " + Want_of_building.MOD_ID);
    }
}