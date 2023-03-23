package 五子棋多人厮杀小游戏;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ConnectionBuilder;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class GameHall extends JFrame{
	protected String name;
	protected static int MAX_USER_NUMBER = 10;
	private Socket s = null;
	private JPanel CenterPanel = null;
	private JButton SearchButton = null;
	private JButton StartButton = null;
	private JButton ConnectButton = null;
	private JScrollPane JSPane = null;
	private JList<String> UserList = null;
	private JPanel BottomPanel = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private InputStreamReader ISR = null;
	private InputStream IS = null;
	protected DefaultListModel<String> users = null;
	private GameHall instance;
	protected GameHall(String name){
		super();
		this.setTitle("游戏大厅");
		setLocationRelativeTo(null);
		this.instance = this;
		this.name = name;
		users = new DefaultListModel<String>();
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(400, 600));
		JLabel titleLabel = new JLabel("欢迎  " + name + " !! 来一盘惊险刺激的五子棋吧");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setPreferredSize(new Dimension(200, 100));
		this.add(titleLabel, BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(getBottomPanel(), BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	//组件懒加载
	private JPanel getBottomPanel() {
		if(BottomPanel == null){
			BottomPanel = new JPanel(new FlowLayout());
			BottomPanel.add(getStartButton());
		}
		return BottomPanel;
	}
	private JPanel getCenterPanel() {
		if(CenterPanel == null) {
			CenterPanel = new JPanel(new FlowLayout());
			CenterPanel.add(getSearchButton());
			CenterPanel.add(getConnectButton());
			CenterPanel.add(getJSPane());
		}
		return CenterPanel;
	}
	private JButton getConnectButton() {
		if(ConnectButton == null){
			ConnectButton = new JButton("连接");
			ConnectButton.addActionListener(new Connect());
			ConnectButton.setPreferredSize(new Dimension(100, 40));
		}
		return ConnectButton;
	}
	private JScrollPane getJSPane() {
		if(JSPane == null){
			JSPane = new JScrollPane(getUserList());
			JSPane.setPreferredSize(new Dimension(300,450));
			TitledBorder border = new TitledBorder("用户列表");
			JSPane.setBorder(border);	
		}
		return JSPane;
	}
	private JList getUserList() {
		if(UserList == null){
			UserList = new JList<String>(users);
		}
		return UserList;
	}
	private JButton getStartButton() {
		if(StartButton == null){
			StartButton = new JButton("匹配对手");
			StartButton.addActionListener(new Start());
			StartButton.setPreferredSize(new Dimension(100, 40));
			StartButton.setEnabled(false);
		}
		return StartButton;
	}
	private JButton getSearchButton() {
		if(SearchButton == null){
			SearchButton = new JButton("搜索玩家");
			SearchButton.addActionListener(new Search());
			SearchButton.setPreferredSize(new Dimension(100, 40));
			SearchButton.setEnabled(false);
		}
		return SearchButton;
	}
	//按钮监听器
	private class Connect implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				s = new Socket(NetServer.IP, NetServer.portNumber);
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(s.getOutputStream(), true);
				out.println(name);
				((JButton) e.getSource()).setEnabled(false);
				SearchButton.setEnabled(true);
				StartButton.setEnabled(true);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	private class Search implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				out.println("Search");
				users.clear();
				String names;
				names = in.readLine();
				while(names.startsWith("NAME:") ) {
					String[] temp;
					temp = names.split("NAME:");
					users.add(users.size(), temp[1]);
					names = in.readLine();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	private class Start implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			((JButton) e.getSource()).setEnabled(false);
			out.println("Startgame");
			try {
				String result = in.readLine();
				if(result.equals("SUCCESS")) {
					boolean right = false;
					if(in.readLine().equals("First")) {
						right = true;
					}
					String otherName = in.readLine();
					GameScene scene = new GameScene(name, otherName, right, in, out, s);
					instance.setVisible(false);
				}
			} catch (IOException e1) {
				return;
			}
		}
	}
	private class closed extends WindowAdapter{
		@Override
		public void windowClosing(WindowEvent e) {
			super.windowClosing(e);
			((GameHall)e.getSource()).out.println("Exit");
		}
	}
}