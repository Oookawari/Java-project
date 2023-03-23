package �����������ɱС��Ϸ;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartMenu extends JFrame{
	protected static StartMenu instance;
	private static JLayeredPane LayeredPanel;
	private StartMenuButton Quit = null;
	private JPanel ButtonsPanel = null;
	private StartMenuButton Settings = null;
	private StartMenuButton MultiPlayer = null;
	private StartMenuButton SinglePlayer = null;
	private JLabel titleLabel = null;
	private JLabel designer = null;
	
	public StartMenu() {
		instance = this;
		this.setTitle("������");
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(400, 600));
		this.setResizable(false);
		setLocationRelativeTo(null);
		titleLabel = new JLabel();
		titleLabel.setPreferredSize(new Dimension(200, 200));
		designer = new JLabel("Designer : Shikia");
		getContentPane().add(titleLabel, BorderLayout.NORTH);
		getContentPane().add(getButtonsPanel(),BorderLayout.CENTER);
		getContentPane().add(designer, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBackGround();
		this.setVisible(true);
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("�����п�", Font.BOLD, 50));
		g.drawString("��  ��  ��", 95, 160);
		getQuit().repaint();
		getSinglePlayer().repaint();
		getSettings().repaint();
		getQuit().repaint();
		getMultiPlayer().repaint();
		designer.repaint();
	}
	private void setBackGround() {
		ImageIcon background = new ImageIcon("src/�����������ɱС��Ϸ/����ͼ1.jpg");
		JLabel BackgroundLabel = new JLabel(background);
		BackgroundLabel.setOpaque(false);
		BackgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
		LayeredPanel = this.getLayeredPane();
		LayeredPanel.setLayout(null);
		LayeredPanel.add(BackgroundLabel);
	}
	//���������
	private JPanel getButtonsPanel() {
		if(this.ButtonsPanel == null) {
			ButtonsPanel = new JPanel();
			ButtonsPanel.setOpaque(true);
			ButtonsPanel.setLayout(new FlowLayout());
			ButtonsPanel.add(getSinglePlayer());
			ButtonsPanel.add(getMultiPlayer());
			ButtonsPanel.add(getSettings());
			ButtonsPanel.add(getQuit());
		}
		return ButtonsPanel;
	}
	private StartMenuButton getQuit() {
		if(Quit == null) {
			Quit = new StartMenuButton("�˳�");
			Quit.addActionListener(new ExitListener());
		}
		return Quit;
	}
	private StartMenuButton getSettings() {
		if(Settings == null) {
			Settings = new StartMenuButton("����");
			Settings.addActionListener(new SettingsListener());
		}
		return Settings;
	}
	private StartMenuButton getMultiPlayer() {
		if(MultiPlayer == null) {
			MultiPlayer = new StartMenuButton("������Ϸ");
			MultiPlayer.addActionListener(new MultiListener());
			
		}
		return MultiPlayer;
	}
	private StartMenuButton getSinglePlayer() {
		if(SinglePlayer == null) {
			SinglePlayer = new StartMenuButton("������Ϸ");
			SinglePlayer.addActionListener(new SingleListener());
		}
		return SinglePlayer;
	}
	//�������
	public static void main(String[] args) {
		StartMenu a = new StartMenu();
		ImageIcon background = new ImageIcon("src/�����������ɱС��Ϸ/BoardBackground.jpg");
	}
	//��������
	private class MultiListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			NewPlayer temp = new NewPlayer();
			temp.setLocationRelativeTo(null);instance.dispose();
			temp.setVisible(true);
			
		}
	}
	private class SingleListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showConfirmDialog(null, "�����У������ڴ�", "�����ڴ�", JOptionPane.CLOSED_OPTION);
		}
	}
	private class SettingsListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Setting.getSettings();
			Setting.getSettings().setVisible(true);
		}
	}
	private class ExitListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
}
