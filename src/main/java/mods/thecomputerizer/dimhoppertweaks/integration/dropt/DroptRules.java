package mods.thecomputerizer.dimhoppertweaks.integration.dropt;

import com.codetaylor.mc.dropt.api.DroptAPI;
import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptHarvesterRuleBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.api.event.DroptLoadRulesEvent;
import com.codetaylor.mc.dropt.api.reference.EnumHarvesterGameStageType;
import com.codetaylor.mc.dropt.api.reference.EnumListType;
import com.codetaylor.mc.dropt.api.reference.EnumReplaceStrategy;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;

@SuppressWarnings("SameParameterValue")
@EventBusSubscriber(modid = DHTRef.MODID)
public class DroptRules {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDroptLoadRules(DroptLoadRulesEvent event) {
        if(event.isCanceled()) return;
        List<IDroptRuleBuilder> builders = new ArrayList<>();
        addGenericStagedBuilder(builders,item -> matchModItems(item,"aoa3"),false,"advent");
        DroptAPI.registerRuleList(DHTRef.res("dropt_aoa3_all"),0,builders);
    }

    private static void addBuilder(
            Function<IDroptRuleBuilder,IDroptRuleBuilder> settings, List<IDroptRuleBuilder> builders) {
        builders.add(settings.apply(DroptAPI.rule()));
    }

    private static void addGenericStagedBuilder(
            List<IDroptRuleBuilder> builders, Function<Item,String> dropMatcher, boolean any, String ... stages) {
        addBuilder(builder -> builder.matchHarvester(gameStageHarvester(any,stages))
                .matchDrops(matchItems(dropMatcher)).replaceStrategy(EnumReplaceStrategy.REPLACE_ITEMS)
                .addDrops(emptyDrops()),builders);
    }

    private static IDroptDropBuilder[] emptyDrops() {
        return new IDroptDropBuilder[]{DroptAPI.drop()};
    }

    private static IDroptHarvesterRuleBuilder gameStageHarvester(boolean any, String ... stages) {
        return DroptAPI.harvester().gameStages(EnumListType.BLACKLIST,any ? EnumHarvesterGameStageType.ANY :
                EnumHarvesterGameStageType.ALL,stages);
    }

    private static String[] matchItems(Function<Item,String> dropMatcher) {
        Set<String> items = new HashSet<>();
        for(Item item : ForgeRegistries.ITEMS) {
            String matched = dropMatcher.apply(item);
            if(StringUtils.isNotBlank(matched)) items.add(matched);
        }
        DHTRef.LOGGER.error("MATCHED ITEMS {}",items);
        return items.toArray(items.toArray(new String[0]));
    }

    private static String matchModItems(Item item, String mod) {
        ResourceLocation res = item.getRegistryName();
        return Objects.nonNull(res) && res.getNamespace().equals(mod) ? res.toString() : null;
    }
}
