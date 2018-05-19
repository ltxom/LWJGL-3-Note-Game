package net.ltxom.glGame.resourses;

import java.io.File;
import static net.ltxom.glGame.util.Terminal.PREFIX;

/**
 * 基本资源文件抽象类，资源可以包括贴图、序列动画、地形坐标、音频等。 请参见继承此类的子类了解更多
 * 
 * @author LTXOM
 * @version 5/19/2018
 */
public abstract class Resourse
{
	private String path;
	private ResourseType type;
	private File file;

	/**
	 * @param path
	 *            资源文件地址，必须指向单个文件
	 * @param type
	 *            区别资源类型的枚举
	 * @see ResourseType
	 */
	public Resourse(String path, ResourseType type)
	{
		this.path = path;
		this.type = type;

		file = new File(path);

		if (!file.exists() || file.isDirectory())
		{
			System.err.println(PREFIX + "This resourse does not exist or is a folder. Please check it!");
			throw new RuntimeException();
		}
	}

	/**
	 * @return 返回资源文件的文件路径
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @return 返回资源文件的File对象
	 */
	public File getFile()
	{
		return file;
	}

	/**
	 * @return 返回资源枚举类型
	 * @see ResourseType
	 */
	public ResourseType getType()
	{
		return type;
	}
}
