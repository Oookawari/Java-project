package �����������ɱС��Ϸ;

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
	//���������
	private JPanel getNamePanel() {
		if(NamePanel == null) {
			NamePanel = new JPanel();
			NamePanel.setBounds(0, 0, 300, 100);
			NamePanel.add(getNameField());
			NamePanel.setBorder(new TitledBorder("����������ǳ�"));
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
			BackBtn = new JButton("����");
			BackBtn.addActionListener(new BackListener());
			BackBtn.setBounds(240, 120, 100, 40);
			add(BackBtn);
		}
		return BackBtn;
	}
	//������
	private class BackListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			instance.dispose();
		}
	}
}
