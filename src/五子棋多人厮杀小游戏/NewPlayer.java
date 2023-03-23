package 五子棋多人厮杀小游戏;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class NewPlayer extends JDialog {
	private JTextField input;
	private JButton sure;
	private JButton cancel;
	private NewPlayer p;
	public NewPlayer(){
		super();
		p = this;
		setLocationRelativeTo(null);
		this.setTitle("New User");
		this.setModal(true);
		this.setResizable(false);
		this.setSize(new Dimension(400, 100));
		this.setLayout(new FlowLayout());
		this.add(new JLabel("Input your name:"));
		this.add(getInput());
		this.add(getSure());
		this.add(getCancel());
	}
	//组件懒加载
	private JButton getCancel() {
		if(cancel == null) {
			cancel = new JButton("取消");
			cancel.setPreferredSize(new Dimension(80, 30));
			cancel.addActionListener(new CancelListener());
		}
		return cancel;
	}
	private JButton getSure() {
		if(sure == null) {
			sure = new JButton("确认");
			sure.setPreferredSize(new Dimension(80, 30));
			sure.addActionListener(new getName());
		}
		return sure;
	}
	private JTextField getInput() {
		if(input == null) {
			input = new JTextField();
			input.setText(Setting.DefaultName);
			input.setPreferredSize(new Dimension(100, 40));
		}
		return input;
	}
	//监听器类
	private class getName implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = input.getText();
			GameHall temp = new GameHall(name);
			p.dispose();
		}
	}
	private class CancelListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			StartMenu.instance.setVisible(true);
			p.dispose();
		}
	}
}
