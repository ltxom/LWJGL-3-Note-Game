package net.ltxom.learnLWJGL.note;

import static org.lwjgl.glfw.GLFW.*;

//为了区分GLFW的方法与OpenGL的GL11的方法，此程序我用GL11.method()来调用GL11的方法。
import org.lwjgl.opengl.GL11;

import org.lwjgl.opengl.GL;

/**
 * 更正式地创建窗口的方式，并渲染一个四边形
 * 
 * video link:
 * https://www.youtube.com/watch?v=qSWLYu0yBgU&list=PLILiqflMilIxta2xKk2EftiRHD4nQGW0u&index=2
 * 
 * @author ElegantWhelp Commented and Revised by LTXOM
 * @version 4/24/2018
 */
public class B_TheQuad
{
	public B_TheQuad()
	{
		/*
		 * 视频上说返回1或0，但该版本文档显示
		 * return TRUE if successful, or FALSE if an error occurred.
		 * */
		if (!glfwInit())
		{
			System.err.println("GLFW初始化失败！");
			System.exit(0);
		}

		/*
		 * 第三个参数控制窗口是否全屏
		 * 
		 * 第四个作者不清楚是什么用，这个参数应该是填另一个window的handle（long类型），则当前窗口可以和另一个窗口共享资源
		 * 文档： the window whose context to share resources with, or NULL to not share resources
		 * */
		long window = glfwCreateWindow(640, 480, "The Quad", 0, 0);

		glfwShowWindow(window);

		// 使window有OpenGL的环境
		glfwMakeContextCurrent(window);

		GL.createCapabilities();

		while (!glfwWindowShouldClose(window))
		{
			glfwPollEvents();

			// 写了一个循环，控制颜色的部分
			// 这是一个错误的写法，仅仅为了方便
			float ramdom = (float) Math.random();
			boolean flag = false;
			for (float i = 0.0f;; i += ((i < 1.0f && !flag) ? 0.01f : -0.01f))
			{
				if (i >= 1.0)
					flag = true;
				if (flag && (i <= 0.0f))
					break;
				
				// 这堆代码的作用就是让它瞎闪不同的颜色
				float color = ((i * ramdom) > 1) ? i : 1.0f - i * ramdom;
				// Indicates the buffers currently enabled for color writing.
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				//过时，只是为了演示用：
				GL11.glBegin(GL11.GL_QUADS);
				{
					GL11.glColor4f((1.0f - color), color, 1, 0);
					GL11.glVertex2f(-0.5f, 0.5f);

					GL11.glColor4f(color, 1.0f - color, 1, 0);
					GL11.glVertex2f(0.5f, 0.5f);

					GL11.glColor4f(1, 0, color, 0);
					GL11.glVertex2f(0.5f, -0.5f);

					GL11.glColor4f(1.0f - color, color, 1.0f - color, 0);
					GL11.glVertex2f(-0.5f, -0.5f);
				}
				GL11.glEnd();

				// Swaps the front and back buffers of the specified window when rendering with OpenGL or OpenGL ES
				glfwSwapBuffers(window);

				try
				{
					Thread.sleep((int) (1.0f / 30.0) * 1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args)
	{
		new B_TheQuad();

	}

}
