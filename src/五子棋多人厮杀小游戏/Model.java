package �����������ɱС��Ϸ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.ForkJoinPool;

import javax.print.attribute.standard.JobKOctetsProcessed;
import javax.swing.JOptionPane;

public class Model {
	protected static GameScene scene;
	private static Thread times;
	protected static final int MAX_TIME = 46;

	protected static int time;
	protected static boolean startTimer = true;
	protected ArrayList<position> MyHistory;
	protected ArrayList<position> OtherHistory;
	protected static int tipX = -1;
	protected static int tipY = -1;
	protected PriorityQueue<Exposition> warning = new PriorityQueue<Exposition>(new Comparator<Exposition>() {
		@Override
		public int compare(Exposition o1, Exposition o2) {
            if (o1.n < o2.n)
                return 1;
            else if (o1.n > o2.n)
                return -1;
            else
               return 0;
        }
	});
	Model(GameScene scene){

		this.scene = scene;
		MyHistory = new ArrayList<position>();
		OtherHistory = new ArrayList<position>();
		this.time = MAX_TIME;
		new Thread() {
			public void run() {
				while(true) {
					try {
						String line = scene.in.readLine();
						System.out.println(line);
						DealWith(line);
					} catch (IOException e) {
						
					}
				}
			};
		}.start();
		if(Control.myturn) {
			scene.getChatArea().append("�������\n");
			countTime();
		}
		else {
			scene.getChatArea().append("�Է�����\n");
		}
	}
	//��ʱ��
	protected static void countTime(){;
	times = new Thread() {
		@Override
		public void run() {
			while(startTimer) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
				}
				time = time - 1;
				if(time >= 10) scene.getTimeLabel().setText("00 : " + time);
				else scene.getTimeLabel().setText("00 : 0" + time);
				scene.repaint();
				if(time == 0) {
					scene.getChatArea().append("�㳬ʱ�ˣ���Ϸʧ�ܣ�\n");
					Control.Send("Timeout");
					Control.loser();
					break;
				}
			}
		}
	};
	times.start();
}
	//��Ϣ����
	protected void DealWith(String line) {
		if(line.startsWith("PutChess")) {
			String[] temp;
			temp = line.split(" ");
			int x = Integer.parseInt(temp[1]);
			int y = Integer.parseInt(temp[2]);
			scene.control.OtherPutChess(x, y);
		}
		else if(line.startsWith("WIN")) {
			scene.getChatArea().append("�����������ӣ���Ϸʧ�ܣ�\n");
			Control.loser();
		}
		else if(line.startsWith("TakeBack")) {
			int rst = JOptionPane.showConfirmDialog(null, "�Է������", "����", JOptionPane.YES_NO_OPTION);
			if(rst == JOptionPane.YES_OPTION) {
				Control.Send("AllowTakeBack");
				Control.OtherTakeBack();
			}
			else {
				Control.Send("RefuseTakeBack");
			}
		}
		else if(line.startsWith("AllowTakeBack")) {
			Control.AllowTakeBack();
			
		}
		else if(line.startsWith("RefuseTakeBack")) {
			Control.RefuseTakeBack();
		}
		else if(line.startsWith("AdmitDefeat")) {
			int rst = JOptionPane.showConfirmDialog(null, "�Է�������", "����", JOptionPane.YES_NO_OPTION);
			if(rst == JOptionPane.YES_OPTION) {
				Control.Send("AllowAdmit");
				Control.OtherAdmit();
			}
			else {
				Control.Send("RefuseAdmit");
			}
		}
		else if(line.startsWith("AllowAdmit")) {
			Control.AllowAdmit();
		}
		else if(line.startsWith("RefuseAdmit")) {
			Control.RefuseAdmit();
		}
		else if(line.startsWith("Message:")) {
			String temp= line.substring(8, line.length());
			scene.getChatArea().append(scene.OtherName + "��" + temp + "\n");
		}
		else if(line.startsWith("Timeout")){
			scene.getChatArea().append("���ֳ�ʱ����������Ϸʤ����\n");
			Control.winner();
		}
		else if(line.startsWith("Restart")) {
			if(Control.restart == true) {}
			else {
				int rst = JOptionPane.showConfirmDialog(null, "�Է�������һ��", "����һ��", JOptionPane.OK_CANCEL_OPTION);
				if(rst == JOptionPane.OK_OPTION) {
					Control.Send("AllowRestart");
					Control.newGame();
					winnerWindow.instance.dispose();
				}
				else if(rst == JOptionPane.CANCEL_OPTION) {
					Control.Send("RefuseRestart");
				}
			}
		}
		else if(line.startsWith("AllowRestart")){
			winnerWindow.instance.dispose();
			Control.newGame();
		}
		else if(line.startsWith("RefuseRestart")){
			JOptionPane.showConfirmDialog(null, "�Է��ܾ�����������", "", JOptionPane.CLOSED_OPTION);
		}
		else if(line.startsWith("Exit")) {
			int rst = JOptionPane.showConfirmDialog(null, "�Է��˳�����Ϸ", "�Ͽ�����", JOptionPane.CLOSED_OPTION);
			scene.dispose();
			GameScene.clearAll();
			scene.setVisible(false);
			StartMenu.instance.setVisible(true);
		}
	}
	//��ȡ��ʾ
	protected void GetTips(int row, int col) {
		int dx[] = {-1, -1, -1, 0, 1, 1, 1, 0};
		int dy[] = {-1, 0, 1, -1, 1, 0, -1, 1};
		for(int i = 0; i < 4; i++) {
			int TempRow = row + dx[i], TempCol = col + dy[i], num = 1;
			Exposition temp1 = new Exposition(-1, -1, -1);
			Exposition temp2 = new Exposition(-1, -1, -1);
			while(TempRow >= 0 && TempCol >= 0 && 
				  TempRow < GameScene.WIDTH && TempCol < GameScene.WIDTH 
				  && Control.board[TempRow][TempCol] == -1) {
				num++;
				TempRow += dx[i];
				TempCol += dy[i];
			}
			temp1.X = TempRow;temp1.Y = TempCol;
			TempRow = row + dx[i + 4];
			TempCol = col + dy[i + 4];
			while(TempRow >= 0 && TempCol >= 0 && 
				  TempRow < GameScene.WIDTH && TempCol < GameScene.WIDTH
				  && Control.board[TempRow][TempCol] == -1) {
				num++;
				TempRow += dx[i + 4];
				TempCol += dy[i + 4];
			}
			temp2.X = TempRow;
			temp2.Y = TempCol;
			temp2.n = num;
			temp1.n = num;
			if(num >= 2) {
				if(temp1.X >= 0 && temp1.Y >= 0 && 
				   temp1.X < GameScene.WIDTH && temp1.Y < GameScene.WIDTH 
				 &&Control.board[temp1.X][temp1.Y] == 0) {
					System.out.println("��ʾx: " + temp1.X + "��ʾy : " + temp1.Y + "Ȩ�� ��" + num);
					warning.add(temp1);
				}
				if(temp2.X >= 0 && temp2.Y >= 0 && 
				   temp2.X < GameScene.WIDTH && temp2.Y < GameScene.WIDTH 
				 &&Control.board[temp2.X][temp2.Y] == 0) {
					System.out.println("��ʾx: " + temp2.X + "��ʾy : " + temp2.Y + "Ȩ�� ��" + num);
					warning.add(temp2);
				}
			}
		}
		return;
	}
	//�ж��Ƿ�ʤ��
	protected boolean isWin(int row, int col) {
		int dx[] = {-1, -1, -1, 0, 1, 1, 1, 0};
		int dy[] = {-1, 0, 1, -1, 1, 0, -1, 1};
		for(int i = 0; i < 4; i++) {
			int TempRow = row + dx[i], TempCol = col + dy[i], num = 1;
			Exposition temp1 = new Exposition(-1, -1, -1);
			Exposition temp2 = new Exposition(-1, -1, -1);
			while(TempRow >= 0 && TempCol >= 0 && 
				  TempRow < GameScene.WIDTH && TempCol < GameScene.WIDTH 
				  && Control.board[TempRow][TempCol] == 1) {
				num++;
				TempRow += dx[i];
				TempCol += dy[i];
			}
			temp1.X = TempRow;temp1.Y = TempCol;
			TempRow = row + dx[i + 4];
			TempCol = col + dy[i + 4];
			while(TempRow >= 0 && TempCol >= 0 && 
				  TempRow < GameScene.WIDTH && TempCol < GameScene.WIDTH
				  && Control.board[TempRow][TempCol] == 1) {
				num++;
				TempRow += dx[i + 4];
				TempCol += dy[i + 4];
			}
			temp2.X = TempRow;
			temp2.Y = TempCol;
			temp2.n = num + 0.5;
			temp1.n = num + 0.5;
			if(num >= 3) {
				if(temp1.X >= 0 && temp1.Y >= 0 && 
				   temp1.X < GameScene.WIDTH && temp1.Y < GameScene.WIDTH 
				 &&Control.board[temp1.X][temp1.Y] == 0) {
					System.out.println("��ʾx: " + temp1.X + "��ʾy : " + temp1.Y + "Ȩ�� ��" + (num + 0.5));
					warning.add(temp1);
				}
				if(temp2.X >= 0 && temp2.Y >= 0 && 
				   temp2.X < GameScene.WIDTH && temp2.Y < GameScene.WIDTH 
		     	 &&Control.board[temp2.X][temp2.Y] == 0) {
					System.out.println("��ʾx: " + temp2.X + "��ʾy : " + temp2.Y + "Ȩ�� ��" + (num + 0.5));
					warning.add(temp2);
				}
			}
			if(num >= 5) return true;
		}
		return false;
	}
}
//������
class position{
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		position other = (position) obj;
		return X == other.X && Y == other.Y;
	}
	public int X;
	public int Y;
	position(int X, int Y){
		this.X = X;
		this.Y = Y;
	}
	position(position other){
		this.X = other.X;
		this.Y = other.Y;
	}
}
//��չ�����ࣨ��Ȩ�أ�
class Exposition extends position{
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		position other = (position) obj;
		return X == other.X && Y == other.Y;
	}
	public int X;
	public int Y;
	public double n;
	Exposition(int X, int Y, double n){
		super(X, Y);
		this.X = X;
		this.Y = Y;
		this.n = n;
	}
	Exposition(Exposition other){
		super(other.X, other.Y);
		this.n = other.n;
	}
}