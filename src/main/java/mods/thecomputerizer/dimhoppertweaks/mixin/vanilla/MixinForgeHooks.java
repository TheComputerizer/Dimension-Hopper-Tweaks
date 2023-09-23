package mods.thecomputerizer.dimhoppertweaks.mixin.vanilla;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Mixin(value = ForgeHooks.class, remap = false)
public class MixinForgeHooks {

    /**
     * @author The_Computerizer
     * @reason Remove unnecessary advancement error log spam
     */
    @SuppressWarnings("DataFlowIssue")
    @Overwrite
    private static boolean loadAdvancements(Map<ResourceLocation, Advancement.Builder> map, ModContainer mod) {
        JsonContext ctx = new JsonContext(mod.getModId());

        return CraftingHelper.findFiles(mod,"assets/"+mod.getModId()+"/advancements",null,(root, file) -> {
                    String relative = root.relativize(file).toString();
                    if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_"))
                        return true;
                    String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                    ResourceLocation key = new ResourceLocation(mod.getModId(),name);
                    if (!map.containsKey(key)) {
                        BufferedReader reader = null;
                        try {
                            reader = Files.newBufferedReader(file);
                            String contents = IOUtils.toString(reader);
                            JsonObject json = JsonUtils.gsonDeserialize(CraftingHelper.GSON,contents,JsonObject.class);
                            if (!CraftingHelper.processConditions(json,"conditions",ctx)) return true;
                            Advancement.Builder builder = JsonUtils.gsonDeserialize(AdvancementManager.GSON,contents,Advancement.Builder.class);
                            map.put(key,builder);
                        }
                        catch (JsonParseException jsonparseexception) {
                            FMLLog.log.info("Ingoring errored advacncement from {}",mod.getModId());
                            return false;
                        }
                        catch (IOException ioexception) {
                            FMLLog.log.info("Ingoring unreadable advacncement from {}",mod.getModId());
                            return false;
                        }
                        finally {
                            IOUtils.closeQuietly(reader);
                        }
                    }
                    return true;
                },
                true, true
        );
    }
}
