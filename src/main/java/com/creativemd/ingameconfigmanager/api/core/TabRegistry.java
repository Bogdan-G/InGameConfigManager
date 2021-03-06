package com.creativemd.ingameconfigmanager.api.core;

import java.util.ArrayList;

import com.creativemd.ingameconfigmanager.api.tab.ModTab;

public class TabRegistry {
	
	private static ArrayList<ModTab> tabs = new ArrayList<ModTab>();
	
	public static ModTab registerModTab(ModTab tab)
	{
		tab.setID(tabs.size());
		tabs.add(tab);
		return tab;
	}
	
	public static ModTab getTabByIndex(int index)
	{
		return tabs.get(index);
	}
	
	public static ArrayList<ModTab> getTabs()
	{
		return tabs;
	}
	
}
