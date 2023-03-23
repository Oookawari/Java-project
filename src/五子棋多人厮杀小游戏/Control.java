package �����������ɱС��Ϸ;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;

public class Control {
	protected static boolean restart = false;
	protected static boolean TakeBackEnabled = false;
	protected static boolean AdmitEnabled = true;
	protected static GameScene scene= null;
	protected static int board[][];{
		board = new int[GameScene.WIDTH][GameScene.WIDTH];
	}
	protected static boolean myturn = true;
	
	public Control(GameScene gs) {
		this.scene = gs;
	}	
	//��model�ԽӵĴ�����
	protected static void MyPutChess(int x, int y) {
		if(myturn == false) {return;}
		if(x < 0 || x >= GameScene.WIDTH || y < 0 || y >= GameScene.WIDTH) {
			scene.getChatArea().append("�㲻����������\n");
			return;
		}
		if(board[x][y] != 0) {
			scene.getChatArea().append("�㲻����������\n");
			return;
		}
		board[x][y] = 1;
		position temp = new position(x, y);
		scene.model.MyHistory.add(temp);
		myturn = false;
		scene.repaint(GameScene.startX - GameScene.units, GameScene.startY - GameScene.units, GameScene.finalX + GameScene.units, GameScene.finalY + GameScene.units);
		Send("PutChess " + x + " " + y);
		scene.model.warning.clear();
		if(scene.model.isWin(x, y)) {
			Send("WIN");
			winner();
		}
		Model.startTimer = false;
		TakeBackEnabled = true;
		Model.time = Model.MAX_TIME;
		scene.getChatArea().append("���ֻغ�\n");
		scene.tips = false;
	};
	protected static void OtherPutChess(int x, int y) {
		myturn = true;
		board[x][y] = -1;
		position temp = new position(x, y);
		scene.model.OtherHistory.add(temp);
		Model.startTimer = true;
		Model.countTime();
		scene.model.GetTips(x, y);
		scene.getChatArea().append("��Ļغ�\n");
	};
	protected static void MyTakeBack() {
		if(myturn == true || TakeBackEnabled == false) {
			scene.getChatArea().append("���ڲ��ܻ���\n");
			return;
		}
		scene.getChatArea().append("�������󣺻���\n");
		Send("TakeBack");
	};
	protected static void AllowTakeBack() {
		scene.getChatArea().append("�Է����������������������Ļغ�\n");
		position temp = scene.model.MyHistory.remove(scene.model.MyHistory.size() - 1);
		board[temp.X][temp.Y] = 0;
		myturn = true;
	}
	protected static void RefuseTakeBack() {	
		scene.getChatArea().append("�Է��ܾ����������\n");
		return;
	}
	protected static void OtherTakeBack() {
		scene.getChatArea().append("�Է����壬�����Ƕ��ֵĻغ�\n");
		position temp = scene.model.OtherHistory.remove(scene.model.OtherHistory.size() - 1);
		board[temp.X][temp.Y] = 0;
		myturn = false;
	};
	protected static void MyAdmit() {
		scene.getChatArea().append("������������\n");
		AdmitEnabled = false;
		Send("AdmitDefeat");
	};
	protected static void OtherAdmit() {
		scene.getChatArea().append("�Է�������\n");
		winner();
	};
	protected static void RefuseAdmit() {
		scene.getChatArea().append("�Է��ܾ����������\n");
		AdmitEnabled = true;
		return;
	}
	protected static void AllowAdmit() {
		scene.getChatArea().append("�Է�ͬ�����������\n");
		loser();
	}
	protected static void loser() {
		scene.getChatArea().append("������\n");
		Model.startTimer = false;
		myturn = false;
		JDialog WinWindow = new winnerWindow(false, scene.Myname, scene.OtherName, scene);
		WinWindow.setVisible(true);
	}
	protected static void winner() {
		scene.getChatArea().append("��ϲ�㣬��Ӯ�ˣ�����\n");
		Model.startTimer = false;
		myturn = false;
		JDialog WinWindow = new winnerWindow(true, scene.Myname, scene.OtherName, scene);
		WinWindow.setVisible(true);
	}
	protected static void newGame() {
		scene.first = !scene.first;
		myturn = scene.first;
		Model.time = Model.MAX_TIME;
		if(myturn == true) {
			Model.startTimer = true;
			Model.countTime();
		}
		scene.model.MyHistory = new ArrayList<position>();
		scene.model.OtherHistory = new ArrayList<position>();
		for (int i = 0; i < GameScene.WIDTH; i++) {
			for (int j = 0; j < GameScene.WIDTH; j++) {
				board[i][j] = 0;
			}
		}
		scene.setVisible(true);
		return;
	}
	protected static void Prompt() {
		while(!scene.model.warning.isEmpty()) {
			Exposition temp = scene.model.warning.poll();
			if(board[temp.X][temp.Y] == 0) {
				scene.tips = true;
				Model.tipX = temp.X;
				Model.tipY = temp.Y;
				scene.repaint();
				return;
			}
			else continue;
		}
		scene.getChatArea().append("���޿�����ʾ������˼����\n");
	}
	//������Ϣ
	protected static void Send(String line) {
		scene.out.println(line);
		return;
	}

}
