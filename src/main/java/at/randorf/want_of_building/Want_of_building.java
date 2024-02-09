package at.randorf.want_of_building;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Want_of_building implements ModInitializer {
    public static final String MOD_ID = "want_of_building";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItemsGroups.registerItemGroups();
        ModItems.registerModItems();
    }
}