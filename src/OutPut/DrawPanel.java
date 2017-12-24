package OutPut;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ACO.Ant;
import InterFace.View;

public class DrawPanel {
	static Graphics g;
	public static double size;
	
	public static void drawpanel()
	{
		//*****JPanel轉畫布*****//
		g = View.show.getGraphics();
				
		//*****清空畫布*****//
		g.setColor(Color.white);
								
		//*****清空範圍*****//
		g.fillRect(0, 0, View.show.getWidth(), View.show.getHeight());
				
		//*****設定顏色*****//
		g.setColor(Color.black);
				
		//*****畫布上打點*****//
		for(int i = 0; i < Ant.total; i++)
		{
			int x = (int)((Ant.x[i])*size);
			int y = (int)((Ant.y[i])*size);
			g.fillRect(x, y, 3, 3);
		}
	}
	
	//*****畫線*****//
	public static void drawline(int A,int B)
	{
		int x1 = (int)((Ant.x[A])*size);
		int y1 = (int)((Ant.y[A])*size);
		int x2 = (int)((Ant.x[B])*size);
		int y2 = (int)((Ant.y[B])*size);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(1.0f));
	    g2d.drawLine(x1, y1, x2, y2);
	}
}
