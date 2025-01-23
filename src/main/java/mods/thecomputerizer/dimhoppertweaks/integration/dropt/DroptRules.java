package mods.thecomputerizer.dimhoppertweaks.integration.dropt;

import com.codetaylor.mc.dropt.api.DroptAPI;
import com.codetaylor.mc.dropt.api.api.IDroptDropBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptHarvesterRuleBuilder;
import com.codetaylor.mc.dropt.api.api.IDroptRuleBuilder;
import com.codetaylor.mc.dropt.api.event.DroptLoadRulesEvent;
import com.codetaylor.mc.dropt.modules.dropt.Util;
import com.codetaylor.mc.dropt.modules.dropt.rule.RuleLoader;
import com.codetaylor.mc.dropt.modules.dropt.rule.log.DebugFileWrapper;
import com.codetaylor.mc.dropt.modules.dropt.rule.log.LoggerWrapper;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.util.*;
import java.util.function.Function;

import static com.codetaylor.mc.dropt.api.reference.EnumHarvesterGameStageType.ALL;
import static com.codetaylor.mc.dropt.api.reference.EnumHarvesterGameStageType.ANY;
import static com.codetaylor.mc.dropt.api.reference.EnumListType.BLACKLIST;
import static com.codetaylor.mc.dropt.api.reference.EnumReplaceStrategy.REPLACE_ITEMS;
import static com.codetaylor.mc.dropt.modules.dropt.ModuleDropt.LOG_FILE_WRITER_PROVIDER;
import static com.codetaylor.mc.dropt.modules.dropt.ModuleDropt.RULE_CACHE;
import static com.codetaylor.mc.dropt.modules.dropt.ModuleDropt.RULE_LISTS;
import static com.codetaylor.mc.dropt.modules.dropt.ModuleDropt.RULE_PATH;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.LOGGER;
import static mods.thecomputerizer.dimhoppertweaks.core.DHTRef.MODID;
import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOWEST;
import static thebetweenlands.common.registries.ItemRegistry.ITEMS;

@SuppressWarnings("SameParameterValue")
@EventBusSubscriber(modid = MODID)
public class DroptRules {

    @SubscribeEvent(priority = LOWEST)
    public static void onDroptLoadRules(DroptLoadRulesEvent event) {
        if(event.isCanceled()) return;
        List<IDroptRuleBuilder> builders = new ArrayList<>();
        addGenericStagedBuilder(builders,item -> matchModItems(item,"aoa3"),false,"advent");
        DroptAPI.registerRuleList(DHTRef.res("dropt_aoa3_all"), 0, builders);
    }

    private static void addBuilder(
            Function<IDroptRuleBuilder,IDroptRuleBuilder> settings, List<IDroptRuleBuilder> builders) {
        builders.add(settings.apply(DroptAPI.rule()));
    }

    private static void addGenericStagedBuilder(
            List<IDroptRuleBuilder> builders, Function<Item,String> dropMatcher, boolean any, String ... stages) {
        addBuilder(builder -> builder.matchHarvester(gameStageHarvester(any,stages))
                .matchDrops(matchItems(dropMatcher)).replaceStrategy(REPLACE_ITEMS)
                .addDrops(emptyDrops()),builders);
    }

    private static IDroptDropBuilder[] emptyDrops() {
        return new IDroptDropBuilder[]{DroptAPI.drop()};
    }

    private static IDroptHarvesterRuleBuilder gameStageHarvester(boolean any, String ... stages) {
        return DroptAPI.harvester().gameStages(BLACKLIST,any ? ANY : ALL,stages);
    }

    private static String[] matchItems(Function<Item,String> dropMatcher) {
        Set<String> items = new HashSet<>();
        for(Item item : ITEMS) {
            String matched = dropMatcher.apply(item);
            if(StringUtils.isNotBlank(matched)) items.add(matched);
        }
        LOGGER.info("Matched item drops {}",items);
        return items.toArray(items.toArray(new String[0]));
    }

    private static String matchModItems(Item item, String mod) {
        ResourceLocation res = item.getRegistryName();
        return Objects.nonNull(res) && res.getNamespace().equals(mod) ? res+":"+32767 : null;
    }

    public static void reload() {
        FileWriter logFileWriter = LOG_FILE_WRITER_PROVIDER.createLogFileWriter();
        LoggerWrapper wrapper = new LoggerWrapper(LOGGER,logFileWriter);
        RULE_LISTS.clear();
        RULE_CACHE.clear();
        RuleLoader.loadRuleLists(RULE_PATH,RULE_LISTS,wrapper,new DebugFileWrapper(logFileWriter));
        RuleLoader.parseRuleLists(RULE_LISTS,wrapper,new DebugFileWrapper(logFileWriter));
        Util.closeSilently(logFileWriter);
        LOGGER.info("Reload [{}] dropt rule files",RULE_LISTS.size());
    }
}
