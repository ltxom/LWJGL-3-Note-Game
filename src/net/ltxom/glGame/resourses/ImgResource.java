/**
 * 
 */
package net.ltxom.glGame.resourses;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import static net.ltxom.glGame.util.Terminal.PREFIX;

/**
 * 储存图像资源
 * @author LTXOM
 * @version 05/20/2018
 */
public class ImgResource extends Resource
{
	private BufferedImage image;

	/**
	 * 构造器传入图像文件的位置
	 */
	public ImgResource(String path)
	{
		super(path, ResourceType.IMAGE);
		try
		{
			image = ImageIO.read(new FileInputStream(path));
		} catch (IOException e)
		{
			System.err.println(PREFIX + "图片文件读取错误，以下是报错信息：");
			e.printStackTrace();
		}
	}

	public BufferedImage getBufferedImage()
	{
		return image;
	}
}
