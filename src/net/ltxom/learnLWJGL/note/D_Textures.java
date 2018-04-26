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
 * 下个版本将会补充注释
 * 
 * video link:
 * https://www.youtube.com/watch?v=2Q7K2Ma5u1U&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=4
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/25/2018
 */
public class D_Textures
{
	private long window;
	private int id;
	private int width;
	private int height;

	public D_Textures(String file, String windowTitle)
	{

		initWindow(windowTitle);

		GL.createCapabilities();

		// 下面是用awt的BufferedImage的到图片的像素信息
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

			for (int height = this.height - 1; height > 0; height--)
			{
				for (int width = 0; width < this.width; width++)
				{
					int pixel = pixels_raw[height * this.width + width];

					pixels.put((byte) ((pixel >> 16) & 0xFF)); // Red
					pixels.put((byte) ((pixel >> 8) & 0xFF)); // Green
					pixels.put((byte) ((pixel) & 0xFF)); // Blue
					pixels.put((byte) ((pixel >> 24) & 0xFF));//Alpha
				}
			}

			pixels.flip();

			id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		glEnable(GL_TEXTURE_2D);

		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			this.bind();

			glBegin(GL_QUADS);
			{
				glTexCoord2f(0, 0);
				glVertex3f(-1.0f, -1.0f, 0.0f);

				glTexCoord2f(1, 0);
				glVertex3f(1.0f, -1.0f, 0.0f);

				glTexCoord2f(1, 1);
				glVertex3f(1.0f, 1.0f, 0.0f);

				glTexCoord2f(0, 1);
				glVertex3f(-1.0f, 1.0f, 0.0f);

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

	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public static void main(String[] args)
	{
		new D_Textures("res/uw-quad.png", "Texture Demo");
	}

}
