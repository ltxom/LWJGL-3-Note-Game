package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

/**
 * 例子展示了如何使用VBO技术
 * 
 * 本章原理：https://blog.csdn.net/wangdingqiaoit/article/details/51318793
 * 拓展：AGP内存、系统内存、显存原理（非OpenGL）
 * https://blog.csdn.net/shareCode/article/details/6682449
 * 
 * 为什么要用顶点数组：https://www.cnblogs.com/iRidescent-ZONE/p/5475337.html(介绍部分)
 * 
 * More: https://blog.csdn.net/arag2009/article/details/52954879
 * 
 * VBO：VertexBufferObject, 讲模型的顶点信息放在显卡的显存的空间，而不占用CPU的内存空间
 * 
 * 
 * video link:
 * https://www.youtube.com/watch?v=2Q7K2Ma5u1U&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=5
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/27/2018
 */
public class E_VBO
{
	private long window;
	private int draw_count;
	private int v_id;
	private int texture_id;

	public E_VBO(String file, String windowTitle, float[] vertices, float[] text_coords)
	{

		initWindow(windowTitle);

		GL.createCapabilities();

		glEnable(GL_TEXTURE_2D);

		draw_count = vertices.length / 3;

		// 创建VBO的过程：generate, store, and bind a VAO(vertex array object)
		/*	https://www.cnblogs.com/hefee/p/3824300.html
		 * 1. 使用glGenBuffers()生成新缓存对象。
		 * 2. 使用glBindBuffer()绑定缓存对象。
		 * 3. 使用glBufferData()将顶点数据拷贝到缓存对象中。
		 * */

		// vertices buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();

		// 创建缓存对象并且返回缓存对象的标示符
		v_id = glGenBuffers();
		// 将缓存对象连接到相应的缓存上
		// 任何顶点属性，如顶点坐标、纹理坐标、法线与颜色分量数组都使用GL_ARRAY_BUFFER
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		// 将数据拷贝到缓存对象
		/*
		 * 第三个参数提供缓存对象将如何使用
		 * 九种枚举值：
		 * GL_STATIC_DRAW
		 * GL_STATIC_READ
		 * GL_STATIC_COPY
		 * GL_DYNAMIC_DRAW
		 * GL_DYNAMIC_READ
		 * GL_DYNAMIC_COPY
		 * GL_STREAM_DRAW
		 * GL_STREAM_READ
		 * GL_STREAM_COPY
		 * 
		 * "static"表示VBO中的数据将不会被改动（一次指定多次使用），
		 * "dynamic"表示数据将会被频繁改动（反复指定与使用），
		 * "stream"表示每帧数据都要改变（一次指定一次使用）。
		 * "draw"表示数据将被发送到GPU以待绘制（应用程序到GL），
		 * "read"表示数据将被客户端程序读取（GL到应用程序），
		 * "copy"表示数据可用于绘制与读取（GL到GL）。	
		 * 
		 * 仅仅draw标志对VBO有用，copy与read标志对顶点/帧缓存对象（PBO或FBO）更有意义，
		 * 如GL_STATIC_DRAW与GL_STREAM_DRAW使用显卡内存，GL_DYNAMIC使用AGP内存。
		 * */
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		// 取消缓存对象与缓存的连接
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		// text_coords
		buffer = BufferUtils.createFloatBuffer(text_coords.length);
		buffer.put(text_coords);
		buffer.flip();

		// 同上，创建贴图VBO
		texture_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		this.bindTexture(file);

		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			glClear(GL_COLOR_BUFFER_BIT);

			glBindTexture(GL_TEXTURE_2D, texture_file_id);

			this.render();

			glfwSwapBuffers(window);
		}
	}

	public void render()
	{
		// activate vertex array 开启顶点数组特性
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		// 将传送数据到GPU，当调用完glDrawArrays后，GPU中已经有了绘图所需数据
		// 参数：1. 每个顶点的维度 2. 顶点数据类型 3. 连续顶点(数组)之间的间隙 4. 启用的顶点属性数组中第一个数据的索引，填0就好
		glVertexPointer(3, GL_FLOAT, 0, 0);

		glBindBuffer(GL_ARRAY_BUFFER, texture_id);
		// 同上
		glTexCoordPointer(2, GL_FLOAT, 0, 0);

		/*
		 *  glDrawArrays( GLenum mode, GLint first, GLsizei count)参数：
		 *	1.mode 参数表示绘制的基本类型，OpenGL预制了 GL_POINTS, GL_LINE_STRIP等基本类型。一个复杂的图形，都是有这些基本类型构成的。 
		 *  2.first表示启用的顶点属性数组中第一个数据的索引。 
		 *	3.count表示绘制需要的顶点数目。
		 * */
		glDrawArrays(GL_TRIANGLES, 0, draw_count);

		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);

	}

	private int height;
	private int width;
	private int texture_file_id;
	private BufferedImage bi;

	public void bindTexture(String file)
	{

		try
		{
			bi = ImageIO.read(new File(file));
			this.height = bi.getHeight();
			this.width = bi.getWidth();

			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bi.getRGB(0, 0, width, height, null, 0, width);

			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);

			for (int height = this.height - 1; height >= 0; height--)
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

			texture_file_id = glGenTextures();

			glBindTexture(GL_TEXTURE_2D, texture_file_id);

			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

		} catch (IOException e)
		{
			e.printStackTrace();
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

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		float[] triangleVertices =
		{ // 三角形1顶点坐标
				-1, -1, 0, // LEFT
				0, 1, 0, // TOP
				1, -1, 0// RIGHT
		};
		float[] triangle2Vertices =
		{ // 三角形2顶点坐标
				-0.5f, 0.5f, 0, // TOP LEFT
				0.5f, 0.5f, 0, // TOP RIGHT
				0.5f, -0.5f, 0 // BOT RIGHT
		};

		float[] quadVertices =
		{ //由两个三角形拼成的长方形顶点坐标
				// TopTriangle
				-0.5f, 0.5f, 0, // TOP LEFT
				0.5f, 0.5f, 0, // TOP RIGHT
				0.5f, -0.5f, 0, // BOT RIGHT

				// BotTriangle
				0.5f, -0.5f, 0, // TOP LEFT
				-0.5f, -0.5f, 0, // TOP RIGHT
				-0.5f, 0.5f, 0 // BOT RIGHT
		};

		float[] texture =
		{ // 材质坐标，与quadVertices坐标对应
				// *视频中的坐标并没有与quadVertices对应，而是把坐标翻转过来了
				// 这是由于上方for (int height = this.height - 1; height >= 0; height--)
				// 没有正确扫描图片像素导致的
				0, 1, // TOP LEFT
				1, 1, // TOP RIGHT
				1, 0, // 

				1, 0, //
				0, 0, //
				0, 1//
		};

		new E_VBO("res/uw-quad.png", "VBO", quadVertices, texture);
	}

}
