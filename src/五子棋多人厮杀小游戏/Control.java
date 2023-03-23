package 五子棋多人厮杀小游戏;

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
	//与model对接的处理函数
	protected static void MyPutChess(int x, int y) {
		if(myturn == false) {return;}
		if(x < 0 || x >= GameScene.WIDTH || y < 0 || y >= GameScene.WIDTH) {
			scene.getChatArea().append("你不能在这下棋\n");
			return;
		}
		if(board[x][y] != 0) {
			scene.getChatArea().append("你不能在这下棋\n");
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
		scene.getChatArea().append("对手回合\n");
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
		scene.getChatArea().append("你的回合\n");
	};
	protected static void MyTakeBack() {
		if(myturn == true || TakeBackEnabled == false) {
			scene.getChatArea().append("现在不能悔棋\n");
			return;
		}
		scene.getChatArea().append("发送请求：悔棋\n");
		Send("TakeBack");
	};
	protected static void AllowTakeBack() {
		scene.getChatArea().append("对方接受了你的请求，现在是你的回合\n");
		position temp = scene.model.MyHistory.remove(scene.model.MyHistory.size() - 1);
		board[temp.X][temp.Y] = 0;
		myturn = true;
	}
	protected static void RefuseTakeBack() {	
		scene.getChatArea().append("对方拒绝了你的请求\n");
		return;
	}
	protected static void OtherTakeBack() {
		scene.getChatArea().append("对方悔棋，现在是对手的回合\n");
		position temp = scene.model.OtherHistory.remove(scene.model.OtherHistory.size() - 1);
		board[temp.X][temp.Y] = 0;
		myturn = false;
	};
	protected static void MyAdmit() {
		scene.getChatArea().append("发送请求：认输\n");
		AdmitEnabled = false;
		Send("AdmitDefeat");
	};
	protected static void OtherAdmit() {
		scene.getChatArea().append("对方认输了\n");
		winner();
	};
	protected static void RefuseAdmit() {
		scene.getChatArea().append("对方拒绝了你的请求\n");
		AdmitEnabled = true;
		return;
	}
	protected static void AllowAdmit() {
		scene.getChatArea().append("对方同意了你的请求\n");
		loser();
	}
	protected static void loser() {
		scene.getChatArea().append("你输啦\n");
		Model.startTimer = false;
		myturn = false;
		JDialog WinWindow = new winnerWindow(false, scene.Myname, scene.OtherName, scene);
		WinWindow.setVisible(true);
	}
	protected static void winner() {
		scene.getChatArea().append("恭喜你，你赢了！！！\n");
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
		scene.getChatArea().append("暂无可用提示！加油思考吧\n");
	}
	//发送信息
	protected static void Send(String line) {
		scene.out.println(line);
		return;
	}

}
