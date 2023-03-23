package 五子棋多人厮杀小游戏;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class Setting extends JDialog{
	protected static JTextField NameField = null;
	protected static Setting instance;
	protected static JPanel NamePanel = null;
	private static JButton BackBtn = null;
	protected static Setting getSettings() {
		if(instance == null) instance = new Setting();
		return instance;
	}
	protected static String DefaultName = "NewPlayer";
	Setting(){
		super();
		this.setLayout(null);
		instance = this;
		this.setModal(true);
		this.setSize(new Dimension(400, 200));
		this.add(getNamePanel());
		this.add(getBackBtn());
		
	}
	//组件懒加载
	private JPanel getNamePanel() {
		if(NamePanel == null) {
			NamePanel = new JPanel();
			NamePanel.setBounds(0, 0, 300, 100);
			NamePanel.add(getNameField());
			NamePanel.setBorder(new TitledBorder("请输入你的昵称"));
		}
		return NamePanel;
	}
	private JTextField getNameField() {
		if(NameField == null) {
			NameField = new JTextField();
			NameField.setText(DefaultName);
			NameField.setPreferredSize(new Dimension(250,40));
		}
		return NameField;
	}
	private JButton getBackBtn() {
		if(BackBtn == null) {
			BackBtn = new JButton("返回");
			BackBtn.addActionListener(new BackListener());
			BackBtn.setBounds(240, 120, 100, 40);
			add(BackBtn);
		}
		return BackBtn;
	}
	//监听器
	private class BackListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			instance.dispose();
		}
	}
}
