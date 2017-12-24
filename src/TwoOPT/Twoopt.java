package TwoOPT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import ACO.Ant;

public class Twoopt {
	
	static double bestdistance;//�̵u�Z��
	static int[] bestpath;//�̵u�Z�������|
	static int bestt;
	static double olddis;//�̵u�Z��
	static double dis;//�̵u�Z��
	static List<Integer> newpath;
	static List<Integer> oldpath;
	Random rd = new Random();
	
	public static int[] twoopt()
	{
		newpath = new ArrayList<>();
		oldpath = new ArrayList<>();
		bestpath = new int[Ant.total+1];
		
		//****Stage1 �_�l�i���*****//
		produce();
		
		//*****Stage2 �F��j�M*****//
		for(int t=0;t<Ant.T/2;t++)
		{
			topt();
			if(dis>olddis)
			{
				best(0);
			}
			else if (dis<bestdistance)
			{
				best(1);
			}
			oldpath.clear();
			oldpath.addAll(newpath);
			newpath.clear();
		}
		System.out.println(bestdistance);
		Ant.bestdistance=bestdistance;
		return bestpath;
		
	}
	
	//*****���Ͱ_�l��******//
	public static void produce()
	{
		Set<Integer> check = new HashSet<>();
		Random rd = new Random();
		for(int i=0;i<Ant.total;i++)
		{
			int c = rd.nextInt(Ant.total);
			while(check.add(c) == false)
			{
				c = rd.nextInt(Ant.total);
			}
			oldpath.add(c);
		}
		oldpath.add(oldpath.get(0));
		calculatedis(0);
		best(0);
	}
	
	//*****�p��Z��*****//
	public static double distance(int pointA, int pointB)
	{
		double re=0;
		re = Math.sqrt(Math.pow(Ant.x[pointA]-Ant.x[pointB], 2)+Math.pow(Ant.y[pointA]-Ant.y[pointB], 2));
		return re;
	}
	
	//*****�����̨θ��|�P�Z��*****//
	public static void best(int type)
	{
		if(type == 0)
		{
			bestdistance = olddis;
			for(int i=0;i<=Ant.total;i++)
			{
				bestpath[i] = oldpath.get(i);
			}
		}
		else
		{
			bestdistance = dis;
			for(int i=0;i<=Ant.total;i++)
			{
				bestpath[i] = newpath.get(i);
			}
		}
		
	}
	
	//*****�p��u�q�`�Z��*****//
	public static void calculatedis(int type)
	{
		double d = 0;
		if(type == 0)
		{
			for(int i=0;i<Ant.total;i++)
			{
				d += distance(oldpath.get(i),oldpath.get(i+1));
			}
			olddis = d;
		}
		else
		{
			for(int i=0;i<Ant.total;i++)
			{
				d += distance(newpath.get(i),newpath.get(i+1));
			}
			dis = d;
		}
	}
	
	//*****2-opt*****//
	public static void topt()
	{
		newpath.addAll(oldpath);
		for(int pc=0;pc<Ant.total-2;pc++)
		{
			for(int pc1=pc+2;pc1<Ant.total;pc1++)
			{
				double A = distance(newpath.get(pc),newpath.get(pc+1))+distance(newpath.get(pc1),newpath.get(pc1+1));//�u�qAB+�u�qCD
				double B = distance(newpath.get(pc),newpath.get(pc1))+distance(newpath.get(pc+1),newpath.get(pc1+1));//�u�qAC+�u�qBD
				if(A > B)
				{
					int tmp = newpath.get(pc+1);
					newpath.set(pc+1, newpath.get(pc1));
					newpath.set(pc1,tmp);
				}
			}
		}
		calculatedis(1);
	}
}
