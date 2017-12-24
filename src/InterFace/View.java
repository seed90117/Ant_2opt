package InterFace;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ACO.Ant;
import InPut.LoadFile;
import OutPut.DrawPanel;


public class View extends JFrame {

public static boolean b = false;
	
	//*****宣告介面*****//
	Container cp = this.getContentPane();
	
	//*****宣告物件*****//
	JLabel antamount = new JLabel("Ant Amount:");
	JLabel pheromone = new JLabel("Pheromone:");
	JLabel time = new JLabel("Time:");
	JLabel A = new JLabel("a:");
	JLabel B = new JLabel("B:");
	JLabel q = new JLabel("q:");
	JLabel p = new JLabel("p:");
	JLabel time_output = new JLabel();
	JLabel dis_output = new JLabel();
	public static JTextField antamount_input = new JTextField("10");
	JTextField pheromone_input = new JTextField("0.0001");
	JTextField time_input = new JTextField("100");
	JTextField A_input = new JTextField("1");
	JTextField B_input = new JTextField("2");
	JTextField q_input = new JTextField("0.8");
	JTextField p_input = new JTextField("0.1");
	JButton loadfile = new JButton("Load File");
	JButton drawpanel = new JButton("Draw Panel");
	JButton start = new JButton("Starts");
	public static JPanel show = new JPanel();
	public static JFileChooser open = new JFileChooser();
	
	View()
	{
		//*****設定介面*****//
		this.setSize(1000, 800);
		this.setLayout(null);
		this.setTitle("ACO + SA Algorithm");
		
		//*****設定物件*****//
		antamount.setBounds(730, 30, 100, 30);
		antamount_input.setBounds(810, 30, 150, 30);
		pheromone.setBounds(730, 80, 100, 30);
		pheromone_input.setBounds(810, 80, 150, 30);
		time.setBounds(750, 130, 100, 30);
		time_input.setBounds(810, 130, 150, 30);
		A.setBounds(760, 180, 100, 30);
		A_input.setBounds(810, 180, 150, 30);
		B.setBounds(760, 230, 100, 30);
		B_input.setBounds(810, 230, 150, 30);
		q.setBounds(760, 280, 100, 30);
		q_input.setBounds(810, 280, 150, 30);
		p.setBounds(760, 330, 100, 30);
		p_input.setBounds(810, 330, 150, 30);
		loadfile.setBounds(750, 380, 200, 30);
		drawpanel.setBounds(750, 430, 200, 30);
		start.setBounds(750, 480, 200, 30);
		show.setBounds(20, 20, 700, 700);
		time_output.setBounds(750, 530, 150, 30);
		dis_output.setBounds(750, 560, 200, 30);
		
		//*****設定JPanel邊框*****//
		show.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		//*****將物件加入介面*****//
		cp.add(antamount);
		cp.add(antamount_input);
		cp.add(pheromone);
		cp.add(pheromone_input);
		cp.add(time);
		cp.add(time_input);
		cp.add(A);
		cp.add(A_input);
		cp.add(B);
		cp.add(B_input);
		cp.add(q);
		cp.add(q_input);
		cp.add(p);
		cp.add(p_input);
		cp.add(loadfile);
		cp.add(drawpanel);
		cp.add(start);
		cp.add(show);
		cp.add(time_output);
		cp.add(dis_output);
		
		//*****�]�w����ݩ�*****//
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//*****���J�ɮ׫��s*****//
		loadfile.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoadFile.loadfile();
			}});
		
		//*****JPanel�W�e�I���s*****//
		drawpanel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					
					DrawPanel.drawpanel();
				}
			}});
		
		//*****������s*****//
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b && antamount_input.getText() != "" && pheromone_input.getText() != "" && time_input.getText() != "" && A_input.getText() != "" && B_input.getText() != "" && q_input.getText() != "" && p_input.getText() != "")
				{
					long T = System.currentTimeMillis();
					Ant.Antamount = Integer.parseInt(antamount_input.getText());
					Ant.pheromone = Double.parseDouble(pheromone_input.getText());
					Ant.T = Integer.parseInt(time_input.getText());
					Ant.A = Double.parseDouble(A_input.getText());;
					Ant.B = Double.parseDouble(B_input.getText());;
					Ant.q = Double.parseDouble(q_input.getText());;
					Ant.p = Double.parseDouble(p_input.getText());;
					Ant.main();
					time_output.setText("執行時間:"+(((double)System.currentTimeMillis()-(double)T)/1000));
					dis_output.setText("最短距離:"+Ant.bestdistance);
				}
			}});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new View();
	}

}
