package net.ltxom.glGame.resourses;

import java.util.HashMap;

/**
 * 本类用来存储单个贴图的碰撞器坐标信息，
 * 每个TilesXMLDataResource都对应一个CollidersResource对象
 * */
public class CollidersResource extends Resource
{
	private String tileName;
	private HashMap<Integer, Double> x;
	private HashMap<Integer, Double> y;
	private HashMap<Integer, Double> width;
	private HashMap<Integer, Double> height;

	public CollidersResource(String tileName, HashMap<Integer, Double> x, HashMap<Integer, Double> y,
			HashMap<Integer, Double> width, HashMap<Integer, Double> height)
	{
		super(ResourceType.COLLIDER);
		this.tileName = tileName;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public String getTileName()
	{
		return tileName;
	}

	public double getX(int id)
	{
		return x.get(id);
	}

	public double getY(int id)
	{
		return y.get(id);
	}

	public double getWidth(int id)
	{
		return width.get(id);
	}

	public double getHeight(int id)
	{
		return height.get(id);
	}
}
