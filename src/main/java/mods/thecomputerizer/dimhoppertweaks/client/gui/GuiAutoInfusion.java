package mods.thecomputerizer.dimhoppertweaks.client.gui;

import mcjty.lib.container.GenericContainer;
import mcjty.lib.gui.GenericGuiContainer;
import mcjty.lib.gui.Window;
import mcjty.lib.gui.layout.HorizontalLayout;
import mcjty.lib.gui.widgets.BlockRender;
import mcjty.lib.gui.widgets.Button;
import mcjty.lib.gui.widgets.ChoiceLabel;
import mcjty.lib.gui.widgets.EnergyBar;
import mcjty.lib.gui.widgets.ImageChoiceLabel;
import mcjty.lib.gui.widgets.Label;
import mcjty.lib.gui.widgets.Panel;
import mcjty.lib.gui.widgets.WidgetList;
import mcjty.lib.tileentity.GenericEnergyStorageTileEntity;
import mcjty.lib.varia.BlockTools;
import mcjty.lib.varia.ItemStackList;
import mcjty.rftools.craftinggrid.CraftingRecipe;
import mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode;
import mods.thecomputerizer.dimhoppertweaks.common.containers.InventoryAutoInfusion;
import mods.thecomputerizer.dimhoppertweaks.core.DHTRef;
import mods.thecomputerizer.dimhoppertweaks.network.DHTNetwork;
import mods.thecomputerizer.dimhoppertweaks.network.PacketAutoInfusion;
import mods.thecomputerizer.dimhoppertweaks.registry.tiles.AutoInfusionTableEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

import static mcjty.lib.base.StyleConfig.colorTextInListNormal;
import static mcjty.lib.gui.layout.HorizontalAlignment.ALIGN_LEFT;
import static mcjty.rftools.RFTools.instance;
import static mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode.EXT;
import static mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode.EXTC;
import static mcjty.rftools.craftinggrid.CraftingRecipe.CraftMode.INT;
import static mcjty.rftools.network.RFToolsMessages.INSTANCE;
import static mcjty.rftools.setup.GuiProxy.GUI_MANUAL_MAIN;
import static net.minecraft.client.renderer.OpenGlHelper.lightmapTexUnit;
import static net.minecraft.item.ItemStack.EMPTY;

public class GuiAutoInfusion extends GenericGuiContainer<AutoInfusionTableEntity> {
    
    private EnergyBar energyBar;
    private WidgetList recipeList;
    private ChoiceLabel keepItem;
    private ChoiceLabel internalRecipe;
    private Button applyButton;
    private static final ResourceLocation iconGuiElements = DHTRef.res("textures/gui/auto_infusion.png");
    private static int lastSelected = -1;
    
    public GuiAutoInfusion(AutoInfusionTableEntity tile, GenericContainer container) {
        super(instance,INSTANCE,tile,container,GUI_MANUAL_MAIN,"crafter");
    }
    
    private void addRecipeLine(ItemStack craftingResult) {
        String readableName = BlockTools.getReadableName(craftingResult);
        int color = colorTextInListNormal;
        if(craftingResult.isEmpty()) {
            readableName = "<no recipe>";
            color = -11513776;
        }
        Panel panel = (new Panel(this.mc,this)).setLayout(new HorizontalLayout())
                .addChild((new BlockRender(this.mc,this))
                                  .setRenderItem(craftingResult).setTooltips("Double click to edit this recipe"))
                .addChild((new Label(this.mc,this)).setColor(color)
                                  .setHorizontalAlignment(ALIGN_LEFT).setDynamic(true).setText(readableName)
                                  .setTooltips("Double click to edit this recipe"));
        this.recipeList.addChild(panel);
    }
    
    private void applyRecipe() {
        int selected = this.recipeList.getSelected();
        if(selected!=-1) {
            if(selected>=this.tileEntity.getSupportedRecipes()) this.recipeList.setSelected(-1);
            else {
                CraftingRecipe craftingRecipe = this.tileEntity.getRecipe(selected);
                InventoryCrafting inv = craftingRecipe.getInventory();
                for(int i=0;i<10;i++) {
                    ItemStack old = inv.getStackInSlot(i);
                    ItemStack stack = this.inventorySlots.getSlot(i).getStack();
                    if(!itemStacksEqual(old,stack)) inv.setInventorySlotContents(i,stack);
                }
                IRecipe recipe = CraftingRecipe.findRecipe(this.mc.world,inv);
                ItemStack result = Objects.nonNull(recipe) ? recipe.getCraftingResult(inv) : EMPTY;
                ItemStack old = this.inventorySlots.getSlot(10).getStack();
                if(!itemStacksEqual(old,result)) this.inventorySlots.getSlot(10).putStack(result);
                craftingRecipe.setResult(result);
                updateRecipe();
                populateList();
            }
        }
    }
    
    private void drawGhostSlots() {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)this.guiLeft,(float)this.guiTop,0f);
        GlStateManager.color(1f,0f,0f,1f);
        GlStateManager.enableRescaleNormal();
        OpenGlHelper.setLightmapTextureCoords(lightmapTexUnit,240f,240f);
        ItemStackList ghostSlots = this.tileEntity.getGhostSlots();
        this.zLevel = 100f;
        this.itemRender.zLevel = 100f;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        for(int i=0;i<ghostSlots.size();i++) {
            ItemStack stack = ghostSlots.get(i);
            if(!stack.isEmpty()) {
                int slotId = i<26 ? i+11 : i+37-26;
                Slot slot = this.inventorySlots.getSlot(slotId);
                if(!slot.getHasStack()) {
                    this.itemRender.renderItemAndEffectIntoGUI(stack,slot.xPos,slot.yPos);
                    GlStateManager.disableLighting();
                    GlStateManager.enableBlend();
                    GlStateManager.disableDepth();
                    this.mc.getTextureManager().bindTexture(iconGuiElements);
                    mcjty.lib.client.RenderHelper.drawTexturedModalRect(slot.xPos,slot.yPos,224,48,16,16);
                    GlStateManager.enableDepth();
                    GlStateManager.disableBlend();
                    GlStateManager.enableLighting();
                }
            }
        }
        this.itemRender.zLevel = 0f;
        this.zLevel = 0f;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int x, int y) {
        drawWindow();
        long currentRF = GenericEnergyStorageTileEntity.getCurrentRF();
        this.energyBar.setValue(currentRF);
        this.tileEntity.requestRfFromServer("rftools");
        drawGhostSlots();
    }
    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        updateButtons();
        super.drawScreen(mouseX,mouseY,partialTicks);
        testRecipe();
    }
    
    @Override
    public void initGui() {
        this.window = new Window(this,this.tileEntity,INSTANCE,DHTRef.res("gui/auto_infusion.gui"));
        super.initGui();
        initializeFields();
        if(lastSelected!=-1 && lastSelected<this.tileEntity.getSizeInventory())
            this.recipeList.setSelected(lastSelected);
        this.window.event("apply",(source,params) -> applyRecipe());
        this.window.event("select",(source,params) -> selectRecipe());
        this.tileEntity.requestRfFromServer("rftools");
    }
    
    private void initializeFields() {
        this.recipeList = this.window.findChild("recipes");
        this.energyBar = this.window.findChild("energybar");
        this.applyButton = this.window.findChild("apply");
        this.keepItem = this.window.findChild("keep");
        this.internalRecipe = this.window.findChild("internal");
        this.energyBar.setMaxValue(this.tileEntity.getCapacity());
        this.energyBar.setValue(GenericEnergyStorageTileEntity.getCurrentRF());
        ((ImageChoiceLabel)this.window.findChild("redstone")).setCurrentChoice(this.tileEntity.getRSMode().ordinal());
        ((ImageChoiceLabel)this.window.findChild("speed")).setCurrentChoice(this.tileEntity.getSpeedMode());
        populateList();
    }
    
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean itemStacksEqual(ItemStack stack, ItemStack old) {
        if(stack.isEmpty()) return old.isEmpty();
        else return !old.isEmpty() && stack.isItemEqual(old);
    }
    
    private void populateList() {
        this.recipeList.removeChildren();
        for(int i=0;i<this.tileEntity.getSupportedRecipes();i++) {
            CraftingRecipe recipe = this.tileEntity.getRecipe(i);
            addRecipeLine(recipe.getResult());
        }
    }
    
    private void selectRecipe() {
        int selected = this.recipeList.getSelected();
        lastSelected = selected;
        if(selected==-1) {
            for(int i=0;i<11;i++) this.inventorySlots.getSlot(i).putStack(EMPTY);
            this.keepItem.setChoice("All");
            this.internalRecipe.setChoice("Ext");
        } else {
            CraftingRecipe craftingRecipe = this.tileEntity.getRecipe(selected);
            InventoryCrafting inv = craftingRecipe.getInventory();
            for(int i=0;i<10;i++) this.inventorySlots.getSlot(i).putStack(inv.getStackInSlot(i));
            this.inventorySlots.getSlot(10).putStack(craftingRecipe.getResult());
            this.keepItem.setChoice(craftingRecipe.isKeepOne() ? "Keep" : "All");
            this.internalRecipe.setChoice(craftingRecipe.getCraftMode().getDescription());
        }
    }
    
    private void sendChangeToServer(int index, InventoryCrafting inv, ItemStack result, boolean keepOne, CraftMode mode) {
        DHTNetwork.sendToServer(new PacketAutoInfusion(this.tileEntity.getPos(),index,inv,result,keepOne,mode));
    }
    
    private void testRecipe() {
        int selected = this.recipeList.getSelected();
        if(selected!=-1) {
            InventoryCrafting inv = new InventoryAutoInfusion(new Container() {
                public boolean canInteractWith(EntityPlayer player) {
                    return false;
                }
            });
            for(int i=0;i<10;i++) inv.setInventorySlotContents(i,this.inventorySlots.getSlot(i).getStack());
            IRecipe recipe = CraftingRecipe.findRecipe(this.mc.world,inv);
            ItemStack result = Objects.nonNull(recipe) ? recipe.getCraftingResult(inv) : EMPTY;
            this.inventorySlots.getSlot(10).putStack(result);
        }
    }
    
    private void updateButtons() {
        boolean selected = this.recipeList.getSelected()!=-1;
        this.keepItem.setEnabled(selected);
        this.internalRecipe.setEnabled(selected);
        this.applyButton.setEnabled(selected);
    }
    
    private void updateRecipe() {
        int selected = this.recipeList.getSelected();
        if(selected!=-1) {
            CraftingRecipe recipe = this.tileEntity.getRecipe(selected);
            boolean keepOne = "Keep".equals(this.keepItem.getCurrentChoice());
            CraftMode mode = EXTC;
            String choice = this.internalRecipe.getCurrentChoice();
            if("Int".equals(choice)) mode = INT;
            else if("Ext".equals(choice)) mode = EXT;
            recipe.setKeepOne(keepOne);
            recipe.setCraftMode(mode);
            sendChangeToServer(selected,recipe.getInventory(),recipe.getResult(),keepOne,mode);
        }
    }
}