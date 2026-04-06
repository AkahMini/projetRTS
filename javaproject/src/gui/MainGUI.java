package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;

import config.DefaultGameSettings;
import config.GameConfiguration;
import engine.map.Block;
import engine.map.Map;
import engine.mobile.unit.Unit;
import engine.process.GameBuilder;
import engine.process.MenuInterface;
import engine.process.MobileInterface;
//import gui.instrument.ChartManager;
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

	private boolean stop = false;
	
	private boolean running = true;
	
	private final static Dimension preferredSize = new Dimension(GameConfiguration.WINDOW_WIDTH, GameConfiguration.WINDOW_HEIGHT);

	private MobileInterface manager;
	private MenuInterface menu;

	private GameDisplay dashboard;
	
	private JPanel statsPanel = new JPanel();
	
	//private ChartPanel typeCountPie;
	//private ChartPanel typeCountBar;
	//private ChartPanel heightEvolutionChart;

	//private ChartManager chartManager = new ChartManager();
	
	//launch the game with MENU as the current state
	//please refer to the game config to see the list
	private String currentState=GameConfiguration.GAMESTATE.get(0);

	public MainGUI(String title) {
		super(title);
		init();
	}

	private void init() {

		//System.out.println(GameConfiguration.GAMESTATE);
		//System.out.println(currentState);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ?
		//KeyControls keyControls = new KeyControls();

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

		javax.swing.JButton testButton5 = new javax.swing.JButton(" ARTILLERY T1");
		testButton5.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("ARTILLERY");
				manager.setTypeSelection("unitAllie");
				manager.setSelectedTier(1);
			}
		});

		javax.swing.JButton testButton6 = new javax.swing.JButton("ennemy(INFANTRY) T1");
		testButton6.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("INFANTRY");
				manager.setTypeSelection("unitEnnemy");
				manager.setSelectedTier(1);
			}
		});

		javax.swing.JButton testButton7 = new javax.swing.JButton("ranged Unit T2");
		testButton7.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				manager.selectUnit("ARTILLERY");
				manager.setTypeSelection("unitEnnemy");
				manager.setSelectedTier(2);
			}
		});

		javax.swing.JButton testButton8 = new javax.swing.JButton("cavalry T3");
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
		menu = GameBuilder.buildInitMenu(this.gameSettings);
		dashboard = new GameDisplay(map, manager, menu);

		MouseControls mouseControls = new MouseControls();
		dashboard.addMouseListener(mouseControls);

		dashboard.setPreferredSize(preferredSize);
		contentPane.add(dashboard, BorderLayout.CENTER);

		
		this.addKeyListener(new KeyControls());
		setFocusable(true);
		requestFocusInWindow();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		setPreferredSize(preferredSize);
		setResizable(false);

	}


	@Override
	public void run() {
		while (running) {
			try {
				Thread.sleep(GameConfiguration.GAME_SPEED);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			switch(currentState) {
				case "MENU":
					menu.updateMenu(currentState);
					break;
				case "CHOOSE":
					menu.updateMenu(currentState);
					break;
				case "PLAYING":
					menu.updateMenu(currentState);
					if(!stop) {
						manager.nextRound();
					}
					if(!manager.winningFaction().equals("null")) {
						currentState=GameConfiguration.GAMESTATE.get(3);
					}
					break;
				case "END":
					menu.updateMenu(currentState);
					break;
			}
			dashboard.repaint();
		
		}
		ExitGame();
	}

	public void startGame() {
		manager = GameBuilder.buildInitMobile(map,this.gameSettings,menu.getSelectedFaction());
		dashboard.resetManager(manager);
	    manager.firstRound();
	    currentState = GameConfiguration.GAMESTATE.get(2);
	    System.out.println("jeu lancé");
	}
	
	private void ExitGame() {
		//if this message is  here = the game closed proprely
	    System.out.println("Fermeture du jeu");
	    //put here method for saving or for stopping other thread
	    System.exit(0);
	}
	
	private class KeyControls implements KeyListener {

		//this part is for the keybord interaction 
		//please indicate the gamestate (main game =2)
		@Override
		public void keyPressed(KeyEvent event) {
			int keyCode = event.getKeyCode();
			switch (keyCode) {
			
			case KeyEvent.VK_ENTER:
				if (currentState.equals("MENU")) {
					currentState=GameConfiguration.GAMESTATE.get(1);
					break;
				} else if (currentState.equals("CHOOSE")) {
					startGame();
					break;
				} else if (currentState.equals("END")) {
					currentState=GameConfiguration.GAMESTATE.get(0);
					break;
				}
				break;
				
			case KeyEvent.VK_ESCAPE:
				if (currentState.equals("PLAYING")) {
					if (!stop) {
						stop = true;
					} else {
						stop = false;
					}
					manager.setIsGameStoped(stop);
					dashboard.repaint();
					break;
				}else if(currentState.equals("CHOOSE")) {
					currentState=GameConfiguration.GAMESTATE.get(0);
					break;
				}else if(currentState.equals("MENU") || currentState.equals("END")) {
					running=false;
				}
	            break;
	        
			case KeyEvent.VK_1:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Zeus");
				}
				break;
				
			case KeyEvent.VK_2:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Hades");
				}
				break;	
				
			case KeyEvent.VK_3:
				if (currentState.equals("CHOOSE")) {
					menu.setSelectedFaction("Poseidon");
				}
				break;
				
			case KeyEvent.VK_S:
				if (currentState.equals("CHOOSE")) {
					menu.setselectedMode(0);
				}
				break;
				
			case KeyEvent.VK_Q:
				if (currentState.equals("PLAYING")) {
					if(stop) {
						ExitGame();
					}
				}
				break;
			
			case KeyEvent.VK_A:
				if (currentState.equals("PLAYING")) {
					if(stop) {
						//do here the event for end the game ( a loose, this input is considered as giving up the game)
					}
				}
				break;
				
			case KeyEvent.VK_F:
				if (currentState.equals("CHOOSE")) {
					menu.setselectedMode(1);
				}
				break;
				
			case KeyEvent.VK_M:
				if (currentState.equals("PLAYING")) {
					manager.motherload();
					manager.cpuLoad();
				}
				break;
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
		}
		

		@Override
		public void mousePressed(MouseEvent e) {
			Block firstBlock=manager.getMousePosition(e.getY(), e.getX());
			manager.initSelectedArea(firstBlock);
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			int blockSize = GameConfiguration.BLOCK_SIZE;
			int line = e.getY() / blockSize;
			int column = e.getX() / blockSize;
			
			//we check if the player clicked in the button zone for x and y
			boolean xZone = (e.getX()>=1020 && e.getX()<=1240);
			boolean yZone = (e.getY()>=560 && e.getY()<=700);
			if(xZone && yZone) {
				//manager.areaButtonPressed(e.getX(),e.getY());
			}

			Block position = map.getBlock(line, column);
			
			
			//if(clickDuration<GameConfiguration.SHORT_CLICK_TIME_DURATION||manager.getSelectedArea().get(0).equals(position)) {
			if(manager.getSelectedArea().get(0).equals(position)) {
				shortClick(e,position);
			}
			else {
				longClick(e,position);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}
		

		public void shortClick(MouseEvent e, Block position) {
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

			//Move all units
			if(manager.ifBlockInGamePanel(position)) {
				manager.unitMoveOrder(position);
			}
			
			
			String typeSelection =manager.getTypeSelection();
			
			//if we selected a build
			if(typeSelection!=null && typeSelection.equals("build") && manager.ifBlockInGamePanel(position)) {
				manager.buildBuilding(position,manager.getSelectedTier(),manager.getPlayer().getFactionName(),manager.getPlayer());
				if(manager.getSelectedWorker()!=null) {
					manager.addUnitsInSelectedArea(manager.getSelectedWorker());
					manager.unitMoveOrder(position);
					manager.setSelectedWorker(null);
				}
				typeSelection=null;
			}
			
			//if we selected a population building
			else if(typeSelection!=null && typeSelection.equals("PopulationBuilding")  && manager.ifBlockInGamePanel(position)) {
				manager.buildBuilding(position,manager.getSelectedTier(),manager.getPlayer().getFactionName(),manager.getPlayer());
				manager.setNotifText(typeSelection+" posé",true);
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
			manager.setTypeSelection(typeSelection);
			
			
			//create 4x4 selection, because only the top left block of a building is recognized as a building
			Block firstBlock = manager.getSelectedArea().get(0);
			line = position.getLine()-1;
			column = position.getColumn()-1;
			manager.calculateSelectedArea(map.getBlock(line, column));
			manager.getBuildingsInSelectedArea();
			
			//create a new 1x1 selection, for units
			//dosen't work because when we click, we usually move units, so we remove the selection
			//needs correction
			ArrayList<Block> newSelection = new ArrayList<Block>();
			newSelection.add(firstBlock);
			manager.setSelectedArea(newSelection);
			manager.calculateSelectedArea(position);
			manager.getUnitsInSelectedArea();
			
			
			
			//manager.setUnitsInSelectedArea(new ArrayList<Unit>()); //Reset the selection
		}
		public void longClick(MouseEvent e,Block position) {
			manager.calculateSelectedArea(position);
			manager.getUnitsInSelectedArea();
			manager.getBuildingsInSelectedArea();
		}
	}

	public DefaultGameSettings getGameSettings() {
		return gameSettings;
	}




}
