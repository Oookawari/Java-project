package 五子棋多人厮杀小游戏;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

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

public class GameScene extends JFrame{
	protected static boolean first;
	protected static GameScene instance;
	protected static final int WIDTH = 15;
	protected static final int startX = 230;
	protected static final int finalX = 930;
	protected static final int startY = 50;
	protected static final int finalY = 750;
	protected static final int units = 50;
	protected static final int ChessRadius = 23;
	protected position selected = new position(-1, -1);
	private JPanel ContentPanel = null;
	private JLayeredPane LayeredPanel = null;	
	private GameSceneButton PromptBtn = null;
	private GameSceneButton AdmitDefeatBtn = null;
	private GameSceneButton TakeBackBtn = null;
	protected JLabel TimeLabel = null;
	private JPanel StatePanel = null;
	private JTextArea ChatArea = null;
	protected Model model = null;
	protected Control control = null;
	private Image offScreenImage = null;
	protected static BufferedReader in = null;
	protected static PrintWriter out = null;
	protected static Socket s = null;
	protected String Myname;
	protected String OtherName;
	private JScrollPane JSPane = null;
	protected JTextField TextLine = null;
	protected JButton sendBtn = null;
	protected boolean tips = false;
	public GameScene(String Myname, String OtherName, boolean first, BufferedReader in, PrintWriter out, Socket s){
		super();
		this.addWindowListener(new closed());
		this.Myname = Myname;
		this.OtherName = OtherName;
		this.in = in;
		this.out = out;
		this.s = s;
		this.instance = this;
		this.first = first;
		this.addMouseMotionListener(new myMouseMotion());
		this.addMouseListener(new myMouseListener());
		this.setResizable(false);
		this.setLayout(null);
		ContentPanel = (JPanel) this.getContentPane();
		ContentPanel.setOpaque(false);
		ContentPanel.setLayout(null);
		setBackGround();
		LayeredPanel.add(getTakeBackBtn(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getAdmitDefeatBtn(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getPromptBtn(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getTimeLabel(),JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getStatePanel(),JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getJSPane(),JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getTextLine(),JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getsendBtn(),JLayeredPane.MODAL_LAYER);
		control = new Control(this);
		control.myturn = first;
		model = new Model(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
	//组件懒加载
	protected static void clearAll() {
		Control.Send("Exit");
	}
	private JButton getsendBtn() {
		if(sendBtn == null) {
			sendBtn = new JButton("发送");
			sendBtn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
			sendBtn.setBounds(1110, 700, 60, 30);
			sendBtn.addActionListener(new Send());
		}
		return sendBtn;
	}
	private JTextField getTextLine() {
		if(TextLine == null) {
			TextLine = new JTextField();
			TextLine.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
			TextLine.setBounds(950, 700, 164, 30);
		}
		return TextLine;
	}
	private JScrollPane getJSPane() {
		if(JSPane == null){
			JSPane = new JScrollPane(getChatArea());
			JSPane.setBounds(950, 450, 220, 254);
			JSPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
		}
		return JSPane;
	}
	protected JTextArea getChatArea() {
		if(ChatArea == null) {
			ChatArea = new JTextArea();
			ChatArea.setEditable(false);
			Font myfont = new Font("华文行楷", Font.BOLD, 15);
			ChatArea.setFont(myfont);
		}
		return ChatArea;
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
	protected JLabel getTimeLabel() {
		if(TimeLabel == null) {
			GradientPaint gp = new GradientPaint(200, 15, Color.WHITE, 300, 15, Color.BLUE, false);
			Border border = BorderFactory.createDashedBorder(gp, 20f, 20);
			Border border2 = BorderFactory.createTitledBorder(border, "剩余时间");
			TimeLabel = new JLabel("00 : " + (Model.MAX_TIME - 1), JLabel.CENTER);
			TimeLabel.setBounds(950, 60, 220, 120);
			Font myfont = new Font("华文行楷", Font.BOLD, 40);
			TimeLabel.setFont(myfont);
			TimeLabel.setBorder(border2);
		}
		return TimeLabel;
	}
	private GameSceneButton getPromptBtn() {
		if(PromptBtn == null) {
			PromptBtn = new GameSceneButton("提  示");
			PromptBtn.setBounds(20, 20, 150, 60);
			PromptBtn.addActionListener(new Prompt());
		}
		return PromptBtn;
	}
	private GameSceneButton getAdmitDefeatBtn() {
		if(AdmitDefeatBtn == null) {
			AdmitDefeatBtn = new GameSceneButton("认  输");
			AdmitDefeatBtn.addActionListener(new AdmitDefeat());
			AdmitDefeatBtn.setBounds(20, 220, 150, 60);
		}
		return AdmitDefeatBtn;
	}
	private GameSceneButton getTakeBackBtn() {
		if(TakeBackBtn == null) {
			TakeBackBtn = new GameSceneButton("悔  棋");
			TakeBackBtn.addActionListener(new TakeBack());
			TakeBackBtn.setBounds(20, 120, 150, 60);
		}		
		return TakeBackBtn;
	}
	//绘制
	public void paint(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(this.getWidth(), this.getHeight());
		}
		Graphics goffScreen = offScreenImage.getGraphics();
		//画你要的东西
		super.paint(goffScreen);
		drawLines(goffScreen);
		if(control.myturn == true)
			drawWaitingChess(goffScreen);
		drawChess(goffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		this.getAdmitDefeatBtn().repaint();
		this.getTakeBackBtn().repaint();
		this.getPromptBtn().repaint();
		this.getsendBtn().repaint();
		this.getTimeLabel().repaint();
		this.getChatArea().repaint();
	}
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
	private void drawWaitingChess(Graphics g) {
		if(this.selected.X == -1 || this.selected.Y == -1) {
			return;
		}
		Color c = g.getColor();
		Color myColor = new Color(0, 0, 0, 190);
		g.setColor(myColor);
		g.fillOval(selected.X * units - ChessRadius + startX + 1, selected.Y * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
		Color myColor2 = new Color(255, 255, 255, 190);
		g.setColor(myColor2);
		g.fillOval(selected.X * units - ChessRadius + startX + 8, selected.Y * units - ChessRadius + startY + 8, 4, 4);
		g.setColor(c);
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
		if(Control.myturn){g.drawRect(963, 240, 200, 80);}
		else {g.drawRect(963, 335, 200, 80);}
		
		for(int i = 0; i < model.MyHistory.size(); i++) {
			temp = model.MyHistory.get(i);
			g.setColor(Color.BLACK);
			g.fillOval(temp.X * units - ChessRadius + startX + 1, temp.Y * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
			g.setColor(Color.WHITE);
			g.fillOval(temp.X * units - ChessRadius + startX + 8, temp.Y * units - ChessRadius + startY + 8, 4, 4);	
		}
		for(int i = 0; i < model.OtherHistory.size(); i++) {
			temp = model.OtherHistory.get(i);
			g.setColor(Color.WHITE);
			g.fillOval(temp.X * units - ChessRadius + startX + 1, temp.Y * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
			g.setColor(Color.LIGHT_GRAY);
			g.fillOval(temp.X * units - ChessRadius + startX + 8, temp.Y * units - ChessRadius + startY + 8, 4, 4);	
		}
		g.setColor(Color.red);
		if(tips) {
			g.drawRect(Model.tipX * units - ChessRadius + startX + 1, Model.tipY * units - ChessRadius + startY + 1, ChessRadius * 2 - 2, ChessRadius * 2 - 2);
		}
		g.setColor(orc);
	}
	//按钮监听器
	private class myMouseMotion extends MouseMotionAdapter{
		@Override
		public void mouseMoved(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			position temp = new position(-1, -1);
			if((x < startX - units / 2) || (x >= finalX + units / 2)) {
			}
			else if((y < startY - units / 2) || (y >= finalY + units / 2)) {
			}
			else{
				temp.X = x + units / 2- startX;
				temp.Y = y + units / 2 - startY;
				temp = coord2pos(temp.X, temp.Y);
			}
			if(!temp.equals(selected)) {
				selected = temp;
				GameScene.instance.repaint(startX - units,startY - units,finalX + units,finalY + units);
			}
		}
	}
	private class myMouseListener extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
            Control.MyPutChess(selected.X, selected.Y);
		}
	}
	private class TakeBack implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Control.MyTakeBack();
		}
	}
	private class Prompt implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(tips == true) return;
			Control.Prompt();
		}
	}
	private class Send implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(getTextLine().getText().equals("")) return;
			getChatArea().append("我：" + getTextLine().getText() + "\n");
			Control.Send("Message:" + getTextLine().getText());
			getTextLine().setText("");
		}
	}
	private class AdmitDefeat implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(Control.AdmitEnabled) Control.MyAdmit();
		}
	}
	private class closed extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			clearAll();
			super.windowClosing(e);
		}
	}
	//像素转坐标
	protected static position coord2pos(int x, int y) {
		int posx = x / units;
		int posy = y / units;
		position temp = new position(posx, posy);
		return temp;
	}
	
}
