package at.randorf.want_of_building;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item BASIC_WANT_HEAD = registerItem("basic_want_head", new Item(new FabricItemSettings().maxCount(1)));
    public static final Item BASIC_WANT = registerItem("basic_want", new basic_want(new FabricItemSettings().maxCount(1).maxDamage(5000)));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(BASIC_WANT_HEAD);
        entries.add(BASIC_WANT);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Want_of_building.MOD_ID, name), item);
    }

    public static void registerModItems() {
        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}