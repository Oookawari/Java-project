package 五子棋多人厮杀小游戏;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class winnerWindow extends JDialog{
	protected static winnerWindow instance = null;
	private String myName;
	private String otherName;
	private boolean win;
	private JLayeredPane LayeredPanel;
	protected JLabel titleLabel;
	protected JPanel BottomPanel;
	protected JButton RestartBtn;
	protected JButton ReplayBtn;
	protected JButton ExitBtn;
	public winnerWindow(boolean isWin, String name, String othername, GameScene gs) {
		super();
		instance = this;
		setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.myName = name;
		this.otherName = othername;
		this.win = isWin;
		this.setSize(new Dimension(400, 600));
		setBackGround();
		LayeredPanel.add(gettitleLabel(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getRestartBtn(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getReplayBtn(), JLayeredPane.MODAL_LAYER);
		LayeredPanel.add(getExitBtn(), JLayeredPane.MODAL_LAYER);
	
	}
	private void setBackGround() {
		ImageIcon background = new ImageIcon("src/五子棋多人厮杀小游戏/背景图1.jpg");
		JLabel BackgroundLabel = new JLabel(background);
		BackgroundLabel.setOpaque(false);
		BackgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
		LayeredPanel = this.getLayeredPane();
		LayeredPanel.setLayout(null);
		LayeredPanel.add(BackgroundLabel);
	}
	//懒加载
	private JLabel gettitleLabel() {
		if(titleLabel == null) {
			if(win) {
				this.setTitle("游戏胜利");
				titleLabel = new JLabel("你赢得了这局游戏！",JLabel.CENTER);
			}else {
				this.setTitle("游戏失败");
				titleLabel = new JLabel("很遗憾，再来一局吧",JLabel.CENTER);
			}
			titleLabel.setFont(new Font("华文行楷", Font.PLAIN, 40));
			titleLabel.setBounds(0, 0, 400, 200);
		}
		return titleLabel;
	}
	private JButton getRestartBtn() {
		if(RestartBtn == null) {
			RestartBtn = new JButton("再来一局");
			RestartBtn.setBounds(30, 500, 100, 40);
			RestartBtn.setPreferredSize(new Dimension(100, 40));
			RestartBtn.addActionListener(new Restart());
		}
		return RestartBtn;
	}
	private JButton getReplayBtn() {
		if(ReplayBtn == null) {
			ReplayBtn = new JButton("复盘");
			ReplayBtn.setBounds(150, 500, 100, 40);
			ReplayBtn.setPreferredSize(new Dimension(100, 40));
			ReplayBtn.addActionListener(new Replay());
		}
		return ReplayBtn;
	}
	private JButton getExitBtn() {
		if(ExitBtn == null) {
			ExitBtn = new JButton("退出");
			ExitBtn.setBounds(270, 500, 100, 40);
			ExitBtn.setPreferredSize(new Dimension(100, 40));
			ExitBtn.addActionListener(new Exit());
		}
		return ExitBtn;
	}
	//按钮监听器
	private class Restart implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Control.Send("Restart");
			instance.dispose();
		}
	}
	private class Replay implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			ReplayScene RS = new ReplayScene(myName, otherName, GameScene.first,GameScene.instance.model.MyHistory, GameScene.instance.model.OtherHistory);
			instance.dispose();
		}
	}
	private class Exit implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Control.Send("Exit");
			GameScene.instance.dispose();
			winnerWindow.instance.dispose();
			StartMenu.instance.setVisible(true);
		}
	}
	
}
