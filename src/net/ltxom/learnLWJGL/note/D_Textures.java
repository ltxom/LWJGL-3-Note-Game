package net.ltxom.learnLWJGL.note;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * 例子展示了如何用LWJGJ3导入显示图片
 * 
 * 
 * 关于本章的原理详解： https://blog.csdn.net/wangdingqiaoit/article/details/51457675
 * 
 * video link:
 * https://www.youtube.com/watch?v=2Q7K2Ma5u1U&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=4
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/27/2018
 */
public class D_Textures
{
	private long window;
	private int id;
	private int width;
	private int height;

	public D_Textures(String file, String windowTitle)
	{
		// 调用OpenGL的资源之前一定要初始化GLFW窗口并create capabilities
		initWindow(windowTitle);

		GL.createCapabilities();

		// 下面是用awt的BufferedImage的到图片的像素信息
		// BufferedImage 子类描述具有可访问图像数据缓冲区的 Image 
		// 继承自Image实现其方法，主要的作用就是将图片加载到内存对 其进行操作
		// https://blog.csdn.net/qq_36238595/article/details/73733429
		BufferedImage bi;
		try
		{
			bi = ImageIO.read(new File(file));
			this.height = bi.getHeight();
			this.width = bi.getWidth();

			// 储存像素信息
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			// 从下到上扫描图片并将之放入ByteBuffer类型的pixels对象中
			for (int height = this.height - 1; height > 0; height--)
			{
				// 从左到右扫描
				for (int width = 0; width < this.width; width++)
				{
					int pixel = pixels_raw[height * this.width + width];

					// 通过位移将四种(RGBA)颜色信息放入pixels中
					pixels.put((byte) ((pixel >> 16) & 0xFF)); // Red
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // Green
					pixels.put((byte) ((pixel) & 0xFF)); // Blue
					pixels.put((byte) ((pixel >> 24) & 0xFF));//Alpha
				}
			}

			// 通过flip()把缓冲区当前位置更改为第一个位置
			pixels.flip();

			// 我们现在想新建一个贴图，需要告诉OpenGL
			// glGenTextures可以返回一个int型唯一ID
			id = glGenTextures();

			// https://blog.csdn.net/yf0811240333/article/details/43524791
			// 现在GL_TEXTURE_2D就和id是绑定的了
			glBindTexture(GL_TEXTURE_2D, id);

			/*
			 * 纹理过滤：在图像进行不同的操作的时候，比如放大缩小，对像素进行不同的操作
			 * GL_TEXTURE_MIN_FILTER在图像缩小的时候
			 * 	// GL_NEAREST 这样的效果：http://cfile29.uf.tistory.com/image/164FFC3C4F4B908E11AC51
			 * GL_TEXTURE_MAG_FILTER在图像放大的时候
			 *  // GL_LINEAR 这样的效果：http://cfile25.uf.tistory.com/image/13397D3C4F4B908E341D34
			 *  
			 * 方法详解：http://huanghl97.blog.163.com/blog/static/593888602013616101142876/
			 * 名词详解：https://blog.csdn.net/meegomeego/article/details/8265203
			 * 
			 * Tips: 使用渣像素图片可以更好地看出效果
			 */
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

			// 当上方预设好纹理之后，就可以通过glTexImage2D把像素信息传入了
			// glTexImage2D 中的参数大部分是设置颜色如何显示以及像素信息如何储存的
			// https://www.khronos.org/opengl/wiki/GLAPI/glTexImage2D
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
		// 启动2D纹理
		glEnable(GL_TEXTURE_2D);

		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBegin(GL_QUADS);
			{
				// 关于绘制坐标问题，这里有原理详解 https://blog.csdn.net/wangdingqiaoit/article/details/51457675
				glTexCoord2f(0, 0);
				glVertex3f(-1f, -1f, 0.0f);

				glTexCoord2f(1, 0);
				glVertex3f(1f, -1f, 0.0f);

				glTexCoord2f(1, 1);
				glVertex3f(1f, 1f, 0.0f);

				glTexCoord2f(0, 1);
				glVertex3f(-1f, 1f, 0.0f);

			}
			glEnd();

			glfwSwapBuffers(window);
		}
	}

	private void initWindow(String title)
	{

		if (!glfwInit())
		{
			System.err.println("GLFW初始化失败！");
			System.exit(0);
		}

		window = glfwCreateWindow(640, 480, title, 0, 0);

		glfwShowWindow(window);

		glfwMakeContextCurrent(window);

	}


	public static void main(String[] args)
	{
		new D_Textures("res/uw-quad.png", "Texture Demo");
	}

}
