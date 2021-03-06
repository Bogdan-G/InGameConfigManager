package com.creativemd.ingameconfigmanager.mod.furnace;

import java.util.ArrayList;
import java.util.Iterator;

import com.creativemd.creativecore.client.avatar.Avatar;
import com.creativemd.creativecore.client.avatar.AvatarItemStack;
import com.creativemd.creativecore.common.container.slot.ContainerControl;
import com.creativemd.creativecore.common.gui.controls.GuiControl;
import com.creativemd.creativecore.common.gui.controls.GuiLabel;
import com.creativemd.creativecore.common.gui.controls.GuiStateButton;
import com.creativemd.creativecore.common.gui.controls.GuiTextfield;
import com.creativemd.creativecore.common.recipe.BetterShapedRecipe;
import com.creativemd.creativecore.common.recipe.GridRecipe;
import com.creativemd.creativecore.common.recipe.Recipe;
import com.creativemd.creativecore.common.recipe.entry.BetterShapelessRecipe;
import com.creativemd.creativecore.common.utils.stack.StackInfo;
import com.creativemd.creativecore.common.utils.stack.StackInfoBlock;
import com.creativemd.creativecore.common.utils.stack.StackInfoFuel;
import com.creativemd.creativecore.common.utils.stack.StackInfoItem;
import com.creativemd.creativecore.common.utils.stack.StackInfoItemStack;
import com.creativemd.creativecore.common.utils.stack.StackInfoMaterial;
import com.creativemd.creativecore.common.utils.stack.StackInfoOre;
import com.creativemd.ingameconfigmanager.api.common.machine.RecipeMachine;
import com.creativemd.ingameconfigmanager.api.common.segment.machine.AddRecipeSegment;
import com.creativemd.ingameconfigmanager.api.tab.ModTab;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

public class FurnaceMachine extends RecipeMachine<FurnaceRecipe>{

	public FurnaceMachine(ModTab tab, String name) {
		super(tab, name);
	}

	@Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public int getOutputCount() {
		return 1;
	}

	@Override
	public void addRecipeToList(FurnaceRecipe recipe) {
		StackInfo info = recipe.input[0];
		ItemStack output = recipe.output[0];
		if(info != null && output != null)
		{
			if(info instanceof StackInfoBlock)
				FurnaceRecipes.smelting().func_151393_a(((StackInfoBlock) info).block, output, recipe.experience);
			if(info instanceof StackInfoItem)
				FurnaceRecipes.smelting().func_151396_a(((StackInfoItem) info).item, output, recipe.experience);
			if(info instanceof StackInfoItemStack)
				FurnaceRecipes.smelting().func_151394_a(((StackInfoItemStack) info).stack.copy(), output, recipe.experience);
			if(info instanceof StackInfoOre)
			{
				ArrayList<ItemStack> stacks = OreDictionary.getOres(((StackInfoOre) info).ore);
				for (int i = 0; i < stacks.size(); i++) {
					FurnaceRecipes.smelting().func_151394_a(stacks.get(i), output, 0.1F);
				}
			}
			if(info instanceof StackInfoMaterial)
			{
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				for (Object name : Block.blockRegistry.getKeys()) {
					Block block = Block.getBlockFromName((String) name);
					if(block != null && block.getMaterial() == ((StackInfoMaterial) info).material)
					{
						FurnaceRecipes.smelting().func_151393_a(block, output, recipe.experience);
					}
				}
			}
			if(info instanceof StackInfoFuel)
			{
				ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
				Iterator iterator = Item.itemRegistry.iterator();

		        while (iterator.hasNext())
		        {
		            Item item = (Item)iterator.next();

		            if (item != null && item.getCreativeTab() != null)
		            {
		                item.getSubItems(item, (CreativeTabs)null, stacks);
		            }
		        }
		        
				for (int i = 0; i < stacks.size(); i++) {
					if(TileEntityFurnace.isItemFuel(stacks.get(i)))
						FurnaceRecipes.smelting().func_151394_a(stacks.get(i), output, 0.1F);
				}
			}
		}
	}

	@Override
	public void clearRecipeList() {
		FurnaceRecipes.smelting().getSmeltingList().clear();
	}

	@Override
	public ItemStack[] getOutput(FurnaceRecipe recipe) {
		return recipe.output;
	}

	@Override
	public ArrayList<FurnaceRecipe> getAllExitingRecipes() {
		ArrayList<FurnaceRecipe> recipes = new ArrayList<FurnaceRecipe>();
		Object[] array = FurnaceRecipes.smelting().getSmeltingList().keySet().toArray();
		for(int zahl = 0; zahl < array.length; zahl++)
		{
			if(array[zahl] != null)
			{
				Object object = FurnaceRecipes.smelting().getSmeltingList().get(array[zahl]);
				if(object instanceof ItemStack && ((ItemStack) object).getItem() != null)
					recipes.add(new FurnaceRecipe((ItemStack)object, array[zahl], FurnaceRecipes.smelting().func_151398_b((ItemStack)object)));
			}
		}
		return recipes;
	}

	@Override
	public void fillGrid(ItemStack[] grid, FurnaceRecipe recipe) {
		if(recipe.input[0] != null)
			grid[0] = recipe.input[0].getItemStack();
	}

	@Override
	public void fillGridInfo(StackInfo[] grid, FurnaceRecipe recipe) {
		grid[0] = recipe.input[0];
	}

	@Override
	public FurnaceRecipe parseRecipe(StackInfo[] input, ItemStack[] output, NBTTagCompound nbt, int width, int height) {
		if(input.length == 1 && input[0] != null && output.length == 1 && output[0] != null)
			return new FurnaceRecipe(output[0], input[0], nbt.getFloat("exp"));
		return null;
	}

	@Override
	public ItemStack getAvatar() {
		return new ItemStack(Blocks.furnace);
	}
	
	@Override
	public void onBeforeSave(FurnaceRecipe recipe, NBTTagCompound nbt)
	{
		nbt.setFloat("exp", recipe.experience);
	}
	
	@Override
	public void parseExtraInfo(NBTTagCompound nbt, AddRecipeSegment segment, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		for (int i = 0; i < guiControls.size(); i++) {
			if(guiControls.get(i).is("exp"))
			{
				float exp = 0;
				try{
					exp = Float.parseFloat(((GuiTextfield)guiControls.get(i)).text);
				}catch (Exception e){
					exp = 0;
				}
				nbt.setFloat("exp", exp);
			}
		}
	}
	
	@Override
	public void onControlsCreated(FurnaceRecipe recipe, boolean isAdded, int x, int y, int maxWidth, ArrayList<GuiControl> guiControls, ArrayList<ContainerControl> containerControls)
	{
		if(isAdded)
		{
			guiControls.add(new GuiTextfield("exp", recipe != null ? "" + recipe.experience : "0.0", x+maxWidth-80, y, 70, 20).setFloatOnly());
		}else{
			guiControls.add(new GuiLabel("exp: " + recipe.experience, x+maxWidth-80, y));
		}
	}

	@Override
	public boolean doesSupportStackSize() {
		return false;
	}

}
