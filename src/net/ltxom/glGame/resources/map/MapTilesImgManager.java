package net.ltxom.glGame.resources.map;

import java.util.ArrayList;

import net.ltxom.glGame.resources.TileImgResource;

import static net.ltxom.glGame.util.Terminal.PREFIX;

/**
 * 每一个MapTilesImgManager负责管理一个材质，
 * 该类实质是封装多个TileImgResource对象
 * @see TileImgResource
 * 
 * */
public class MapTilesImgManager
{
	private ArrayList<TileImgResource> imgList;
	private int colNumber;
	private int rowNumber;
	private String tileName;

	public MapTilesImgManager(String tileName, int colNumber, int rowNumber)
	{
		this.tileName = tileName;
		this.colNumber = colNumber;
		this.rowNumber = rowNumber;

		imgList = new ArrayList<TileImgResource>();
	}
	
	/**
	 * @return 返回当前贴图的名字
	 * */
	public String getTileName()
	{
		return tileName;
	}

	/**
	 * 将指定TileImgResource添加到当前类
	 * */
	public void addImg(TileImgResource tir)
	{
		imgList.add(tir);
	}

	/**
	 * @param x 材质相对贴图x坐标
	 * @param y 材质相对贴图y坐标
	 * @return 返回TileImgResource材质对象
	 * */
	public TileImgResource getImg(int x, int y)
	{

		if (colNumber < y || rowNumber < x)
		{
			System.err.println(PREFIX + "坐标超出贴图材质范围！");
			return null;
		}
		for (int i = 0; i < imgList.size(); i++)
		{
			if (imgList.get(i).getXLocation() == x && imgList.get(i).getYLocation() == y)
			{
				return imgList.get(i);
			}
		}
		System.err.println(PREFIX + "指定坐标的贴图不存在！");
		return null;
	}
}
