package �����������ɱС��Ϸ;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketImpl;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

public class NetServer extends JFrame {
	private Thread thread;
	private JLabel state;
	private ServerSocket ss;
	protected static int MAX_USER_NUMBER = 10;
	private JPanel CenterPanel = null;
	private JButton StartButton = null;
	private JScrollPane JSPane = null;
	private JList<User> UserList = null;
	private StartServiceListener SSL = null;
	public static final int portNumber = 8092;
	public static final String IP = "localhost";
	protected DefaultListModel<User> users = null;
	private Queue<User> waiting;
	NetServer(){
		super();
		this.setTitle("����������");
		users = new DefaultListModel<User>();
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(400, 600));
		JLabel titleLabel = new JLabel("�����������");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setPreferredSize(new Dimension(200, 100));
		state = new JLabel("Designer : Shikia");
		this.add(titleLabel, BorderLayout.NORTH);
		this.add(getCenterPanel(), BorderLayout.CENTER);
		this.add(state, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		waiting = new LinkedList<User>();
		this.setVisible(true);
	}
	//���������
	private JPanel getCenterPanel() {
		if(CenterPanel == null) {
			CenterPanel = new JPanel(new FlowLayout());
			CenterPanel.add(getStartButton());
			CenterPanel.add(getJSPane());
		}
		return CenterPanel;
	}
	private JScrollPane getJSPane() {
		if(JSPane == null){
			JSPane = new JScrollPane(getUserList());
			JSPane.setPreferredSize(new Dimension(300,450));
			TitledBorder border = new TitledBorder("�û��б�");
			JSPane.setBorder(border);
			
		}
		return JSPane;
	}
	private JList getUserList() {
		if(UserList == null){
			UserList = new JList<User>(users);
		}
		return UserList;
	}
	private Component getStartButton() {
		if(StartButton == null){
			StartButton = new JButton("����������");
			StartButton.setPreferredSize(new Dimension(150, 60));
			SSL = new StartServiceListener();
			StartButton.addActionListener(SSL);
		}
		return StartButton;
	}
	public static void main(String[] args) {
		NetServer server = new NetServer();
	}
	//��������
	private class StartServiceListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				((JButton) e.getSource()).setEnabled(false);
				((JButton) e.getSource()).setText("����������");
				ss = new ServerSocket(portNumber);
				state.setText("���������ɹ����˿ڣ�" + portNumber);
				thread = new Thread(){ 
					public void run() {
						try {
							while(true) {
								Socket temp = ss.accept();
								if(temp != null) {
									BufferedReader in = new BufferedReader(new InputStreamReader(temp.getInputStream()));
									String name = in.readLine();
									PrintWriter out = new PrintWriter(temp.getOutputStream(), true);
									User newUser = new User(temp, name, in, out);
									users.add(users.size(), newUser);
									Thread HeartLink = new Thread() {
										public void run() {
											User me = newUser;
											String orders;
											boolean wait = true;
											while(wait) {
												try {
													orders = me.in.readLine();
													if(orders == null) continue;
													if(orders.equals("Search")) {
														SendUserList(me.out, me.name);
													}
													else if(orders.equals("Startgame")) {
														waiting.offer(me);
														me.state = "ƥ����";
														if(waiting.size() >= 2) {
															StartAGame();
														}
														wait = false;
													}
													else if(orders.equals("Exit")) {
														users.removeElement(me);
														break;
													}
												} catch (IOException e) {
													
												}
											}				
										}
									};
									HeartLink.setPriority(MIN_PRIORITY);
									HeartLink.setDaemon(true);
									HeartLink.start();
								}
							}
						} catch (IOException e) {
						}	
					}
				};
				thread.setDaemon(true);
				thread.start();
			} catch (IOException e1) {
				state.setText("��������ʧ��");
				((JButton) e.getSource()).setText("������������");
				((JButton) e.getSource()).setEnabled(true);
			}
		}
	}
	//�����û��б�
	private void SendUserList(PrintWriter out,String username) {
		for(int i = 0; i < users.size();i++) {
			out.println("NAME:" + users.get(i).name);
		}
		out.println("NULL");
	}
	//��ʼƥ��
	private synchronized void StartAGame() {
		if(waiting.size() < 2) {return;}
		User a = waiting.poll();
		User b = waiting.poll();
		a.state = "��Ϸ��";
		b.state = "��Ϸ��";
		a.out.println("SUCCESS");
		b.out.println("SUCCESS");
		a.out.println("First");
		b.out.println("Second");
		a.out.println(b.name);
		b.out.println(a.name);
		new Thread() {
			public void run() {
				User from = a;
				User to = b;
				while(true) {
					String read;
					try {
						read = from.in.readLine();
						System.out.println(from.name + " send " + read + " " + to.name);
						to.out.println(read);
						if(read.equals("Exit")) {
							users.removeElement(from);
							users.removeElement(to);
							break;
						}
						System.out.println(to.name + " received " + read + " " + from.name);
					} catch (IOException e) {
						System.out.println("Something wrong");
						break;
					}
				}
			};
		}.start();
		new Thread() {
			public void run() {
				User from = b;
				User to = a;
				while(true) {
					String read;				
					try {
						read = from.in.readLine();
						System.out.println(from.name + " " + read + " " + to.name);
						to.out.println(read);
						if(read.equals("Exit")) {
							users.removeElement(from);
							users.removeElement(to);
							break;
						}
						System.out.println(to.name + " received " + read + " " + from.name);
					} catch (IOException e) {
						System.out.println("Something wrong");
						break;
					}
				}
			};
		}.start();
	}
}
//�û���
class User{
	Socket s;
	String name;
	BufferedReader in;
	PrintWriter out;
	String state;
	public User(Socket s, String name, BufferedReader in, PrintWriter out) {
		this.s = s;
		this.name = name;
		this.in = in;
		this.out = out;
		this.state = "������";
	}
	public String toString(){
		return name + "------" + state;
	}
}
