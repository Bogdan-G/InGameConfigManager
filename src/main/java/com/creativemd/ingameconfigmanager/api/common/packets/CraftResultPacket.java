package com.creativemd.ingameconfigmanager.api.common.packets;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;
import com.creativemd.creativecore.common.packet.PacketHandler;
import com.creativemd.ingameconfigmanager.api.common.event.ConfigEventHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;

public class CraftResultPacket extends CreativeCorePacket{
	
	public ItemStack stack;
	public int index;
	
	public CraftResultPacket(int index, ItemStack stack)
	{
		this.stack = stack;
		this.index = index;
	}
	
	public CraftResultPacket()
	{
		this.stack = null;
	}
	
	@Override
	public void executeClient(EntityPlayer player) {
		if(player.openContainer instanceof ContainerWorkbench)
		{
			ConfigEventHandler.index = index;
			((ContainerWorkbench)player.openContainer).craftResult.setInventorySlotContents(0, stack);
		}
	}

	@Override
	public void executeServer(EntityPlayer player) {
		if(player.openContainer instanceof ContainerWorkbench)
		{
			((ContainerWorkbench)player.openContainer).craftResult.setInventorySlotContents(0, stack);
			PacketHandler.sendPacketToPlayer(new CraftResultPacket(index, stack), (EntityPlayerMP) player);
		}
	}

	@Override
	public void writeBytes(ByteBuf buf) {
		ByteBufUtils.writeItemStack(buf, stack);
		buf.writeInt(index);
	}

	@Override
	public void readBytes(ByteBuf buf) {
		stack = ByteBufUtils.readItemStack(buf);
		index = buf.readInt();
	}

}
