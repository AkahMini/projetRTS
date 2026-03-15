package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.building.Building;
import engine.mobile.building.HQ;
import engine.mobile.building.UnitProducer;
import engine.mobile.unit.StatsLoader;
import engine.process.GameBuilder;
import engine.process.MobileInterface;
/**
 * 
 * Main graphic class that create the game window and manage the different user input.
 * 
 * @author LE RAY Yann
 * @author ATCHAOUI Ilias
 * @author POSE Romain
 * @version 1.0
 *
 */
public class MainGUI extends JFrame implements Runnable {

	public DefaultGameSettings gameSettings = new DefaultGameSettings();

	private static final long serialVersionUID = 1L;

	private Map map;



	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

	private MobileInterface manager;

	private GameDisplay dashboard;

	public MainGUI(String title) {
		super(title);
		init();
	}

	private void init() {


		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		KeyControls keyControls = new KeyControls();

		//This part is used to see text input if wanted

		/*
		JTextField textField = new JTextField();
		textField.addKeyListener(keyControls);
		contentPane.add(textField, BorderLayout.SOUTH);
		 */



		//THIS PART IS FOR TEST ONLY WILL BE REMOVED

		javax.swing.JPanel RightPanel = new javax.swing.JPanel();
		RightPanel.setBackground(java.awt.Color.GRAY); 


		javax.swing.JButton testButton = new javax.swing.JButton("Test Unit Producer");

		testButton.addActionListener(new ActionListener() { //Temporary button to test some features
			public void actionPerformed(ActionEvent e) {
				manager.selectBuilding("Producer");
				manager.setTypeSelection("build");
				manager.setSelectedTier(1);
			}
		});
		javax.swing.JButton testButton2 = new javax.swing.JButton("Test Tower Defense");

		testButton2.addActionListener(new ActionListener() { //Temporary button to test some features
			public void actionPerformed(ActionEvent e) {
				manager.selectBuilding("DefenseTower");
				manager.setTypeSelection("build");
				manager.setSelectedTier(2);
			}
		});

		javax.swing.JButton testButton3 = new javax.swing.JButton("Test Population Building ");

		testButton3.addActionListener(new ActionListener() { //Temporary button to test some features
			public void actionPerformed(ActionEvent e) {
				manager.selectBuilding("PopulationBuilding");
				manager.setTypeSelection("PopulationBuilding");
				manager.setSelectedTier(1);
			}
		});
		javax.swing.JButton testButton4 = new javax.swing.JButton("Test Research Building ");

		testButton4.addActionListener(new ActionListener() { //Temporary button to test some features
			public void actionPerformed(ActionEvent e) {
				manager.selectBuilding("ResearchBuilding");
				manager.setTypeSelection("build");
				manager.setSelectedTier(2);
			}
		});

		javax.swing.JButton testButton5 = new javax.swing.JButton(" Test Infantry T1");
		testButton5.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("ARTILLERY");
				manager.setTypeSelection("unitAllie");
				manager.setSelectedTier(1);
			}
		});

		javax.swing.JButton testButton6 = new javax.swing.JButton("Test Unit ennemy T1");
		testButton6.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("INFANTRY");
				manager.setTypeSelection("unitEnnemy");
				manager.setSelectedTier(1);
			}
		});

		javax.swing.JButton testButton7 = new javax.swing.JButton("Test ranged Unit T2");
		testButton7.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("ARTILLERY");
				manager.setTypeSelection("unitEnnemy");
				manager.setSelectedTier(2);
			}
		});

		javax.swing.JButton testButton8 = new javax.swing.JButton("Test cavalry T3");
		testButton8.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("CAVALRY");
				manager.setTypeSelection("unitAllie");
				manager.setSelectedTier(3);
			}
		});

		RightPanel.add(testButton);
		RightPanel.add(testButton2);
		RightPanel.add(testButton3);
		RightPanel.add(testButton4);
		RightPanel.add(testButton5);
		RightPanel.add(testButton6);
		RightPanel.add(testButton7);
		RightPanel.add(testButton8);
		contentPane.add(RightPanel, BorderLayout.SOUTH);



		map = GameBuilder.buildMap();
		manager = GameBuilder.buildInitMobile(map,this.gameSettings);
		dashboard = new GameDisplay(map, manager);

		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);

		dashboard.setPreferredSize(preferredSize);
		contentPane.add(dashboard, BorderLayout.CENTER);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setPreferredSize(preferredSize);
		setResizable(false);

	}


	@Override
	public void run() {
		manager.firstRound();
		while (true) {
			try {
				Thread.sleep(GameConfiguration.GAME_SPEED);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}

			manager.nextRound();
			dashboard.repaint();
		}
	}

	private class KeyControls implements KeyListener {

		//this part is for the keybord interaction 
		@Override
		public void keyPressed(KeyEvent event) {
			char keyChar = event.getKeyChar();
			switch (keyChar) {

			default:
				break;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	private class MouseControls implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			int blockSize = GameConfiguration.BLOCK_SIZE;
			int line = e.getY() / blockSize;
			int column = e.getX() / blockSize;

			//System.out.println("x :"+e.getX()+" y :"+e.getY()+" - Block line : "+line+" Block column : "+column);

			
			//we check if the player clicked in the button zone for x and y
			boolean xZone = (e.getX()>=1020 && e.getX()<=1240);
			boolean yZone = (e.getY()>=560 && e.getY()<=700);
			if(xZone && yZone) {
				manager.areaButtonPressed(e.getX(),e.getY());
			}

			Block position = map.getBlock(line, column);
			String typeSelection =manager.getTypeSelection();
			if(typeSelection!=null && typeSelection.equals("build") && manager.ifBlockInGamePanel(position)) {
				manager.buildBuilding(position,manager.getSelectedTier(),manager.getPlayer().getFactionName(),manager.getPlayer());
				if(manager.getSelectedWorker()!=null) {
					manager.addUnitsInSelectedArea(manager.getSelectedWorker());
					manager.unitMoveOrder(position);
					manager.setSelectedWorker(null);
				}
				typeSelection=null;
			}else if(typeSelection!=null && typeSelection.equals("PopulationBuilding")  && manager.ifBlockInGamePanel(position)) {
				manager.buildBuilding(position,manager.getSelectedTier(),manager.getPlayer().getFactionName(),manager.getPlayer());
				if(manager.getSelectedWorker()!=null) {
					manager.addUnitsInSelectedArea(manager.getSelectedWorker());
					manager.unitMoveOrder(position);
					manager.setSelectedWorker(null);
				}
				typeSelection=null;
			}
			else if(typeSelection!=null && typeSelection.equals("unitAllie")) {
				manager.spawnUnit(position, manager.getPlayer().getFactionName());
				typeSelection=null;

			}else if(typeSelection!=null && typeSelection.equals("unitEnnemy")) {
				manager.spawnUnit(position, "HADES"); // faction harcoded for testing
				typeSelection=null;
			}

			if(manager.ifBlockInGamePanel(position)) {
				manager.unitMoveOrder(position);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {

			Block firstBlock=manager.getMousePosition(e.getY(), e.getX());
			if(manager.ifBlockInGamePanel(firstBlock)) {
				manager.unitMoveOrder(firstBlock);
			}
			manager.initSelectedArea(firstBlock);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			Block lastBlock=manager.getMousePosition(e.getY(), e.getX());
			manager.calculateSelectedArea(lastBlock);
			manager.getUnitsInSelectedArea();
			manager.getBuildingsInSelectedArea();
			//System.out.println(manager.getUnits().size());

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}


	}




}
