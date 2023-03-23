package 五子棋多人厮杀小游戏;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.print.attribute.IntegerSyntax;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
public class ReplayScene extends JFrame{
	protected ReplayScene instance;
	protected static final int WIDTH = 15;
	protected static final int startX = 230;
	protected static final int finalX = 930;
	protected static final int startY = 50;
	protected static final int finalY = 750;
	protected static final int units = 50;
	protected static final int ChessRadius = 23;
	private JPanel ContentPanel = null;
	private JLayeredPane LayeredPanel = null;
	private JPanel StatePanel = null;
	protected Model model = null;
	private Image offScreenImage = null;
	protected String Myname;
	protected String OtherName;
	protected ArrayList<position> MyHistory = new ArrayList<position>();
	protected ArrayList<position> OtherHistory = new ArrayList<position>();
	protected boolean myturn;
	public ReplayScene(String Myname, String OtherName, boolean first, ArrayList<position> MH, ArrayList<position> OH){
		super();
		this.instance = this;
		this.setTitle("游戏复盘");
		myturn = first;
		this.Myname = Myname;
		this.OtherName = OtherName;
		this.setResizable(false);
		this.setLayout(null);
		ContentPanel = (JPanel) this.getContentPane();
		ContentPanel.setOpaque(false);
		ContentPanel.setLayout(null);
		setBackGround();
		LayeredPanel.add(getStatePanel(),JLayeredPane.MODAL_LAYER);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		new Thread() {
			public void run() {
				try {
					ArrayList<position> MC = (ArrayList<position>) MH.clone();
					ArrayList<position> OC = (ArrayList<position>) OH.clone();
					position temp;
					while(!OC.isEmpty() && !MC.isEmpty()) {
						sleep(700);
						if(myturn) {
							temp = MC.remove(0);
							MyHistory.add(temp);
							myturn = !myturn;
						}
						else {
							temp =OC.remove(0);
							OtherHistory.add(temp);
							myturn = !myturn;
						}
						repaint();
					}
					sleep(700);
					if(!OC.isEmpty()) {
						temp =OC.remove(0);
						OtherHistory.add(temp);
					}
					if(!MC.isEmpty()) {
						temp = MC.remove(0);
						MyHistory.add(temp);
					}
					repaint();
					GameSceneButton tempbtn = new GameSceneButton("返回");
					tempbtn.addActionListener(new Back());
					LayeredPanel.add(tempbtn, JLayeredPane.MODAL_LAYER);
					tempbtn.setBounds(980, 500, 100, 40);
				} catch (InterruptedException e) {}
			};		
		}.start();
	}
	private void setBackGround() {
		ImageIcon background = new ImageIcon("src/五子棋多人厮杀小游戏/chessboard.jpg");
		JLabel BackgroundLabel = new JLabel(background);
		BackgroundLabel.setOpaque(false);
		BackgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
		LayeredPanel = this.getLayeredPane();
		LayeredPanel.setLayout(null);
		LayeredPanel.add(BackgroundLabel);
		this.setSize(background.getIconWidth(), background.getIconHeight());
	}
	
	private JPanel getStatePanel() {
		if(StatePanel == null) {
			StatePanel = new JPanel();
			StatePanel.setBounds(950, 200, 220, 200);
			StatePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
			StatePanel.setBackground(new Color(200, 200, 200, 190));
			
		}
		return StatePanel;
	}
	//绘制棋盘
	private void drawLines(Graphics g) {
		g.setColor(Color.BLACK);
		for(int i = 0; i < WIDTH; i++) {
			g.drawLine(startX, startY + units * i, finalX, startY + units * i);
			g.drawLine(startX + units * i, startY, startX + units * i, finalY);
		}
		//绘制边框矩形
		int LineWidth = 5;
		g.fillRect(startX - 10, startY - 10, LineWidth ,finalX - startX + 20 );
		g.fillRect(startX - 10, startY - 10, finalY - startY + 20, LineWidth);
		g.fillRect(startX - 10, finalY + 5, finalX - startX+ 20, LineWidth);
		g.fillRect(finalX + 5, startY - 10, LineWidth, finalY - startY + 20);
		//绘制五点 数组为5点中心
		int CPoint[][] = new int[5][2];
		int Pointradius = 6;
		CPoint[0][0] = 3;CPoint[0][1] = 3;
		CPoint[1][0] = 11;CPoint[1][1] = 3;
		CPoint[2][0] = 3;CPoint[2][1] = 11;
		CPoint[3][0] = 11;CPoint[3][1] = 11;
		CPoint[4][0] = 7;CPoint[4][1] = 7;
		for(int i = 0; i < 5; i++) {
			g.fillOval(CPoint[i][0]*units - Pointradius + startX, CPoint[i][1]*units - Pointradius + startY, Pointradius * 2, Pointradius * 2);
		}

	}
	public void paint(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(this.getWidth(), this.getHeight());
		}
		Graphics goffScreen = offScreenImage.getGraphics();
		super.paint(goffScreen);
		drawLines(goffScreen);
		drawChess(goffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	private void drawChess(Graphics g) {
		Color orc = g.getColor();
		position temp;
		g.setColor(Color.BLACK);
		g.setFont(new Font("华文仿宋",Font.BOLD , 30));
		g.drawString(Myname, 1030, 290);
		g.drawString(OtherName, 1030, 390);
		g.fillOval(965, 250, 50, 50);
		g.setColor(Color.WHITE);
		g.drawLine(970, 330, 1160, 330);
		g.fillOval(975, 260, 5, 5);
		g.fillOval(965, 350, 50, 50);
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(975, 360, 5, 5);
		g.setColor(Color.red);
		if(myturn){g.drawRect(963, 240, 200, 80);}
		else {g.drawRect(963, 335, 200, 80);}
		
		for(int i = 0; i < MyHistory.size(); i++) {
			temp = MyHistory.get(i);
			g.setColor(Color.BLACK);
			g.fillOval(temp.X * units - ChessRadius + startX + 1, temp.Y * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
			g.setColor(Color.WHITE);
			g.fillOval(temp.X * units - ChessRadius + startX + 8, temp.Y * units - ChessRadius + startY + 8, 4, 4);	
		}
		for(int i = 0; i < OtherHistory.size(); i++) {
			temp = OtherHistory.get(i);
			g.setColor(Color.WHITE);
			g.fillOval(temp.X * units - ChessRadius + startX + 1, temp.Y * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
			g.setColor(Color.LIGHT_GRAY);
			g.fillOval(temp.X * units - ChessRadius + startX + 8, temp.Y * units - ChessRadius + startY + 8, 4, 4);	
		}
		g.setColor(orc);
		g.setColor(orc);
	}
	//像素转坐标
	protected static position coord2pos(int x, int y) {
		int posx = x / units;
		int posy = y / units;
		position temp = new position(posx, posy);
		return temp;
	}
	//监听器类
	private class Back implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			instance.dispose();
			winnerWindow.instance.setVisible(true);
		}
	}
}
