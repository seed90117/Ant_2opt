package ACO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import OutPut.DrawPanel;

public class Ant {

	//*****宣告變數*****//
		public static double[] x;//X軸值
		public static double[] y;//Y軸值
		public static int total;//總資料量
		public static double A;//α值
		public static double B;//β值
		public static double q;//使用轉換規則或轉換機率的參數
		public static double p;//衰退參數
		public static int T;//執行代數
		public static double enddistance;
		public static int[][] Ant;//螞蟻陣列
		public static int Antamount;//螞蟻數
		public static double pheromone;//起始費洛蒙
		public static double bestdistance;//最短距離
		public static int[] bestpath;//最短距離的路徑
		
		static List<Double> line;//線段費洛蒙
		static List<String> lineAB;//紀錄線段費洛蒙的點
		static Set<Integer> chpoint;//紀錄並判斷每隻螞蟻有無走重複點
		
		public static void main()
		{
			//*****初始化*****//
			Random rd = new Random();
			line = new ArrayList<>();
			lineAB = new ArrayList<>();
			chpoint = new HashSet<>();
			bestpath = new int[total+1];
			//int count;
			
			//*****初始化各線段費洛蒙*****//
			for(int x=0;x<total;x++)
			{
				for(int y=x+1;y<total;y++)
				{
					if(x != y)//不跟自己重複
					{
						line.add(pheromone);
						lineAB.add(String.valueOf(x)+String.valueOf(y));
					}
				}
			}
			
			//*****2OPT尋找最佳解****//
			bestpath = TwoOPT.Twoopt.twoopt();

			//*****更新費洛蒙*****//
			int count;
			int n;
			for(int i=0;i<bestpath.length-1;i++)
			{
				n = bestpath[i+1];
				if(bestpath[i] < n)
				{
					count = lineAB.indexOf(String.valueOf(bestpath[i]) + String.valueOf(n));
				}
				else
				{
					count = lineAB.indexOf(String.valueOf(n) + String.valueOf(bestpath[i]));
				}
				line.set(count,pheromone(1,distance(bestpath[i],n),1,line.get(count)));
			}
			
			for(int t=0;t<T;t++)//執行次數
			{
				Ant = new int[Antamount][total+1];
				for(int pc=0;pc<=total;pc++)//Point
				{
					for(int ac=0;ac<Antamount;ac++)//Ant
					{
						//*****Stage1 隨機將螞蟻放在點上*****//
						if(pc == 0)
						{
							Ant[ac][pc] = rd.nextInt(total);
						}
						
						//*****Stage2選擇下一個點*****//
						if(pc != 0 && pc != total)
						{
							Ant[ac][pc] = nextpoint(ac,pc-1,t+1);
						}
						
						//*****Stage3回到原點*****//
						if(pc == total)
						{
							Ant[ac][pc] = Ant[ac][0];
						}
					}
					//*****Part of Pheromone update*****//
					if(pc != 0)
					{
						for(int ac=0;ac<Antamount;ac++)
						{
							updatepheromone(0,ac,pc,1,1,pc);
						}
					}
				}
				//*****2-opt*****//
				/*double optrd = rd.nextDouble();
				if(optrd <= 0.8)
				{
					for(int ac=0;ac<Antamount;ac++)//Ant
					{
						for(int pc=0;pc<total-2;pc++)//Point
						{
							for(int pc1=pc+2;pc1<total;pc1++)
							{
								double A = distance(Ant[ac][pc],Ant[ac][pc+1])+distance(Ant[ac][pc1],Ant[ac][pc1+1]);//線段AB+線段CD
								double B = distance(Ant[ac][pc],Ant[ac][pc1])+distance(Ant[ac][pc+1],Ant[ac][pc1+1]);//線段AC+線段BD
								if(A > B)
								{
									int tmp = Ant[ac][pc+1];
									Ant[ac][pc+1] = Ant[ac][pc1];
									Ant[ac][pc1] = tmp;
								}
							}
						}
					}
				}*/
				//*****All of Pheromone update*****//
				chpoint.clear();
				for(int ac=0;ac<Antamount;ac++)//Ant
				{
					for(int pc=0;pc<total;pc++)//Point
					{
						updatepheromone(1,ac,pc,0,1,t);//增強費洛蒙
					}
				}
				for(int i=0;i<line.size();i++)
				{
					if(!chpoint.contains(i))
					{
						line.set(i,pheromone(0,0,0,line.get(i)));//揮發
					}
				}
				//*****紀錄最佳路徑與距離*****//
				best(t);
			}
			//*****畫線*****//
			for(int pc=0;pc<total;pc++)
			{
				DrawPanel.drawline(bestpath[pc], bestpath[pc+1]);
			}
		}
		
		//*****下一個點*****//
		public static int nextpoint(int ac,int pc,int t)
		{
			//****設定參數*****//
			int next =0;
			List<Double> chance = new ArrayList<>();
			Random rd = new Random();
			double r = rd.nextDouble();//紀錄轉換規則或轉換機率的隨機值
			double tmp = rd.nextDouble();//隨機產生機率
			//*****將該隻螞蟻走過的點放入Set*****//
			chpoint.clear();//初始化
			for(int i=0;i<=pc;i++)
			{
				chpoint.add(Ant[ac][i]);
			}
			
			//*****計算尚未走過點的機率*****//
			double sum = transform(ac,pc,t);
			for(int i=0;i<total;i++)
			{
				if(!chpoint.contains(i))//點未走過則進入
				{
					if(r <= q)//q<=q0 使用轉換規則
					{
						chance.add(roll(ac,pc,i,t));
					}
					else//q>q0 使用轉換機率
					{
						chance.add(roll(ac,pc,i,t)/sum);
					}
				}
				else//走過的點機率設為0
				{
					chance.add(0.0);
				}
			}
			
			//*****決定下一個點*****//
			for(int i=0;i<chance.size();i++)
			{
				if(r <= q)//q<=q0 使用轉換規則
				{
					//*****取機率大的為下一點*****//
					if(chance.get(i) > chance.get(next))
					{
						next = i;
					}
				}
				else//q>q0 使用轉換機率
				{
					//*****使用輪盤法*****//
					tmp = tmp-chance.get(i);
					if(tmp <= 0)
					{
						next = i;
						break;
					}
				}
			}
			
			
			return next;
		}
		
		//*****更新Pheromone*****//
		public static void updatepheromone(int b,int ac,int pc,int rang,int type,int t)
		{
			int count;
			int n;
			if(b == 0)//局部
			{
				n = Ant[ac][pc-1];
			}
			else//全域
			{
				n = Ant[ac][pc+1];
			}
			if(Ant[ac][pc] < n)
			{
				count = lineAB.indexOf(String.valueOf(Ant[ac][pc]) + String.valueOf(n));
			}
			else
			{
				count = lineAB.indexOf(String.valueOf(n) + String.valueOf(Ant[ac][pc]));
			}
			line.set(count,pheromone(rang,distance(Ant[ac][pc],n),type,line.get(count)));
			if(rang == 0)
			{
				chpoint.add(count);
			}
		}
		
		//*****計算距離*****//
		public static double distance(int pointA, int pointB)
		{
			double re=0;
			re = Math.sqrt(Math.pow(x[pointA]-x[pointB], 2)+Math.pow(y[pointA]-y[pointB], 2));
			return re;
		}
		
		//*****Pheromone公式*****//
		public static double pheromone(int rang,double Ls, int type,double ph)
		{
			double re=0;
			double tr;
			if(rang == 0)//全部更新
			{
				if(type == 0)//揮發
				{
					tr = 0;
				}
				else//增加費洛蒙
				{
					tr = 100/Ls;
				}
				re = ((1-p)*ph)+(p*tr);
			}
			else//局部更新
			{
				re = ((1-p)*ph)+(p*pheromone);
			}
			return re;
		}
		
		//*****轉換機率(分母)*****//
		public static double transform(int ac,int pc,int t)
		{
			double p = 0;
			int count = 0;
			for(int y=0;y<total;y++)
			{
				if(chpoint.contains(y) == false)
				{
					if(Ant[ac][pc] > y)
					{
						count = lineAB.indexOf(String.valueOf(y)+String.valueOf(Ant[ac][pc]));
					}
					else
					{
						count = lineAB.indexOf(String.valueOf(Ant[ac][pc])+String.valueOf(y));
					}
					p += Math.pow((line.get(count)*t),A)*Math.pow(1/distance(Ant[ac][pc],y),B);
				}
			}
			return p;
		}
		
		//*****轉換規則(分子)*****//
		public static double roll(int ac , int pc , int n,int t)
		{
			double p = 0;
			int count;
			for(int y=0;y<total;y++)
			{
				if(Ant[ac][pc] != y)
				{
					if(y == n )
					{
						if(Ant[ac][pc] > y)
						{
							count = lineAB.indexOf(String.valueOf(y)+String.valueOf(Ant[ac][pc]));
						}
						else
						{
							count = lineAB.indexOf(String.valueOf(Ant[ac][pc])+String.valueOf(y));
						}
						p = Math.pow((line.get(count)*t),A)*Math.pow((1/distance(Ant[ac][pc],y)),B);
						break;
					}
				}
			}
			return p;
		}
		
		//*****紀錄最佳路徑與距離*****//
		public static void best(int t)
		{
			double[] dis = new double[Antamount];
			int tmp=0;
			//*****計算每隻螞蟻走的總線段長*****//
			for(int ac=0;ac<Antamount;ac++)//Ant
			{
				for(int pc=0;pc<total;pc++)//Point
				{
					dis[ac] += distance(Ant[ac][pc],Ant[ac][pc+1]); 
				}
			}
			//*****找走最短路徑的螞蟻*****//
			for(int ac=0;ac<Antamount;ac++)
			{
				if(dis[ac]<dis[tmp])
				{
					tmp = ac;
				}
			}
			//*****紀錄最短路徑螞蟻以及路徑*****//
			if(bestdistance > dis[tmp])
			{
				for(int pc=0;pc<=total;pc++)
				{
					bestpath[pc] = Ant[tmp][pc];
				}
				bestdistance = dis[tmp];
				//twoopt();
			}
			System.out.println(t+"  "+bestdistance);
		}
		
		//*****2-opt*****//
		public static void twoopt()
		{
			boolean chloop = true;
			while(chloop)
			{
				chloop = false;
				for(int pc=0;pc<total-2;pc++)//Point
				{
					for(int pc1=pc+2;pc1<total;pc1++)
					{
						double A = distance(bestpath[pc],bestpath[pc+1])+distance(bestpath[pc1],bestpath[pc1+1]);//線段AB+線段CD
						double B = distance(bestpath[pc],bestpath[pc1])+distance(bestpath[pc+1],bestpath[pc1+1]);//線段AC+線段BD
						if(A > B)
						{
							int tmp = bestpath[pc+1];
							bestpath[pc+1] = bestpath[pc1];
							bestpath[pc1] = tmp;
							chloop = true;
						}
					}
				}
			}
		}
}
