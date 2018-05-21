package net.ltxom.glGame.resources.map;

import static net.ltxom.glGame.util.Terminal.PREFIX;

import java.util.ArrayList;

import net.ltxom.glGame.resources.TileImgResource;
import net.ltxom.glGame.resources.TilesXMLDataResource;

/**
 * Layer类将贴图坐标转换并将贴图与材质文件对齐
 * 
 * */
public class Layer
{
	private String layerName;
	private int layerWidth;
	private int layerHeight;
	private ArrayList<TilesXMLDataResource> tilesXMLDataResourceList;
	private ArrayList<Integer> startIndex;
	public ArrayList<String> layerContent;

	public Layer(ArrayList<TilesXMLDataResource> tilesXMLDataResourceList, ArrayList<Integer> startIndex,
			ArrayList<String> layerContent, String layerName, int layerWidth, int layerHeight)
	{
		this.tilesXMLDataResourceList = tilesXMLDataResourceList;
		this.startIndex = startIndex;
		this.layerName = layerName;
		this.layerHeight = layerHeight;
		this.layerWidth = layerWidth;
		this.layerContent = layerContent;
		init();
	}

	private void init()
	{
		if (tilesXMLDataResourceList.size() != startIndex.size())
		{
			System.err.println(PREFIX + "初始化图层时关键错误，这是由于贴图索引数量与贴图数量不一致导致的");
			throw new RuntimeException("Fatal ERROR!");
		} else if (layerHeight != layerContent.size())
		{
			System.err.println(PREFIX + "初始化图层时关键错误，这是由于预设的贴图高度与实际贴图高度不一致导致的");
			throw new RuntimeException("Fatal ERROR!");
		}

		for (int i = 0; i < startIndex.size(); i++)
		{
			
		}
	}
	
//	public TileImgResource getTileImgResource() {
		
//	}
	
	public String getLayerName()
	{
		return layerName;
	}

	public int getLayerWidth()
	{
		return layerWidth;
	}

	public int getLayerHeight()
	{
		return layerHeight;
	}

}
