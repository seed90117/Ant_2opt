package ACO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import OutPut.DrawPanel;

public class Ant {

	//*****�ŧi�ܼ�*****//
		public static double[] x;//X�b��
		public static double[] y;//Y�b��
		public static int total;//�`��ƶq
		public static double A;//�\��
		public static double B;//�]��
		public static double q;//�ϥ��ഫ�W�h���ഫ���v���Ѽ�
		public static double p;//�I�h�Ѽ�
		public static int T;//����N��
		public static double enddistance;
		public static int[][] Ant;//���ư}�C
		public static int Antamount;//���Ƽ�
		public static double pheromone;//�_�l�O���X
		public static double bestdistance;//�̵u�Z��
		public static int[] bestpath;//�̵u�Z�������|
		
		static List<Double> line;//�u�q�O���X
		static List<String> lineAB;//�����u�q�O���X���I
		static Set<Integer> chpoint;//�����çP�_�C�����Ʀ��L�������I
		
		public static void main()
		{
			//*****��l��*****//
			Random rd = new Random();
			line = new ArrayList<>();
			lineAB = new ArrayList<>();
			chpoint = new HashSet<>();
			bestpath = new int[total+1];
			//int count;
			
			//*****��l�ƦU�u�q�O���X*****//
			for(int x=0;x<total;x++)
			{
				for(int y=x+1;y<total;y++)
				{
					if(x != y)//����ۤv����
					{
						line.add(pheromone);
						lineAB.add(String.valueOf(x)+String.valueOf(y));
					}
				}
			}
			
			//*****2OPT�M��̨θ�****//
			bestpath = TwoOPT.Twoopt.twoopt();

			//*****��s�O���X*****//
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
			
			for(int t=0;t<T;t++)//���榸��
			{
				Ant = new int[Antamount][total+1];
				for(int pc=0;pc<=total;pc++)//Point
				{
					for(int ac=0;ac<Antamount;ac++)//Ant
					{
						//*****Stage1 �H���N���Ʃ�b�I�W*****//
						if(pc == 0)
						{
							Ant[ac][pc] = rd.nextInt(total);
						}
						
						//*****Stage2��ܤU�@���I*****//
						if(pc != 0 && pc != total)
						{
							Ant[ac][pc] = nextpoint(ac,pc-1,t+1);
						}
						
						//*****Stage3�^����I*****//
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
								double A = distance(Ant[ac][pc],Ant[ac][pc+1])+distance(Ant[ac][pc1],Ant[ac][pc1+1]);//�u�qAB+�u�qCD
								double B = distance(Ant[ac][pc],Ant[ac][pc1])+distance(Ant[ac][pc+1],Ant[ac][pc1+1]);//�u�qAC+�u�qBD
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
						updatepheromone(1,ac,pc,0,1,t);//�W�j�O���X
					}
				}
				for(int i=0;i<line.size();i++)
				{
					if(!chpoint.contains(i))
					{
						line.set(i,pheromone(0,0,0,line.get(i)));//���o
					}
				}
				//*****�����̨θ��|�P�Z��*****//
				best(t);
			}
			//*****�e�u*****//
			for(int pc=0;pc<total;pc++)
			{
				DrawPanel.drawline(bestpath[pc], bestpath[pc+1]);
			}
		}
		
		//*****�U�@���I*****//
		public static int nextpoint(int ac,int pc,int t)
		{
			//****�]�w�Ѽ�*****//
			int next =0;
			List<Double> chance = new ArrayList<>();
			Random rd = new Random();
			double r = rd.nextDouble();//�����ഫ�W�h���ഫ���v���H����
			double tmp = rd.nextDouble();//�H�����;��v
			//*****�N�Ӱ����ƨ��L���I��JSet*****//
			chpoint.clear();//��l��
			for(int i=0;i<=pc;i++)
			{
				chpoint.add(Ant[ac][i]);
			}
			
			//*****�p��|�����L�I�����v*****//
			double sum = transform(ac,pc,t);
			for(int i=0;i<total;i++)
			{
				if(!chpoint.contains(i))//�I�����L�h�i�J
				{
					if(r <= q)//q<=q0 �ϥ��ഫ�W�h
					{
						chance.add(roll(ac,pc,i,t));
					}
					else//q>q0 �ϥ��ഫ���v
					{
						chance.add(roll(ac,pc,i,t)/sum);
					}
				}
				else//���L���I���v�]��0
				{
					chance.add(0.0);
				}
			}
			
			//*****�M�w�U�@���I*****//
			for(int i=0;i<chance.size();i++)
			{
				if(r <= q)//q<=q0 �ϥ��ഫ�W�h
				{
					//*****�����v�j�����U�@�I*****//
					if(chance.get(i) > chance.get(next))
					{
						next = i;
					}
				}
				else//q>q0 �ϥ��ഫ���v
				{
					//*****�ϥν��L�k*****//
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
		
		//*****��sPheromone*****//
		public static void updatepheromone(int b,int ac,int pc,int rang,int type,int t)
		{
			int count;
			int n;
			if(b == 0)//����
			{
				n = Ant[ac][pc-1];
			}
			else//����
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
		
		//*****�p��Z��*****//
		public static double distance(int pointA, int pointB)
		{
			double re=0;
			re = Math.sqrt(Math.pow(x[pointA]-x[pointB], 2)+Math.pow(y[pointA]-y[pointB], 2));
			return re;
		}
		
		//*****Pheromone����*****//
		public static double pheromone(int rang,double Ls, int type,double ph)
		{
			double re=0;
			double tr;
			if(rang == 0)//������s
			{
				if(type == 0)//���o
				{
					tr = 0;
				}
				else//�W�[�O���X
				{
					tr = 100/Ls;
				}
				re = ((1-p)*ph)+(p*tr);
			}
			else//������s
			{
				re = ((1-p)*ph)+(p*pheromone);
			}
			return re;
		}
		
		//*****�ഫ���v(����)*****//
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
		
		//*****�ഫ�W�h(���l)*****//
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
		
		//*****�����̨θ��|�P�Z��*****//
		public static void best(int t)
		{
			double[] dis = new double[Antamount];
			int tmp=0;
			//*****�p��C�����ƨ����`�u�q��*****//
			for(int ac=0;ac<Antamount;ac++)//Ant
			{
				for(int pc=0;pc<total;pc++)//Point
				{
					dis[ac] += distance(Ant[ac][pc],Ant[ac][pc+1]); 
				}
			}
			//*****�䨫�̵u���|������*****//
			for(int ac=0;ac<Antamount;ac++)
			{
				if(dis[ac]<dis[tmp])
				{
					tmp = ac;
				}
			}
			//*****�����̵u���|���ƥH�θ��|*****//
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
						double A = distance(bestpath[pc],bestpath[pc+1])+distance(bestpath[pc1],bestpath[pc1+1]);//�u�qAB+�u�qCD
						double B = distance(bestpath[pc],bestpath[pc1])+distance(bestpath[pc+1],bestpath[pc1+1]);//�u�qAC+�u�qBD
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
