import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;

import java.io.*;

import java.util.Timer;
import java.util.TimerTask;

public class Matcher implements ActionListener {

	//Instantiating Variables:
	JFrame mainFrame;
	JButton saved;
	JTextField namePlacard;
		
	//Declaring Variables:
	int score = 0;
	String name = "";
	int rows = 0;
	int columns = 2;
	boolean alreadyClicked = false;
	ImageIcon image = new ImageIcon("MAGTABLogo3inches.png");
	int judge = 0; //number of times clicked
	
	//Instantiating Q&A Parts:
	String[][] array;
	int[][] slots;
	JButton[][] buttons;
		
	//Instantiating Leaderboard Parts:
	String[] names;
	int[] scores;
		
	String fileName = "Text.txt";
	String line = null;
	Random rng = new Random();
		
	JPanel scorepanel;
	
	//A broken clock...
	static boolean canStart = false;
	JLabel totalTime;
	Timer timer = new Timer();
	
	int time = 0;
	String clock = "";
		
	TimerTask task = new TimerTask(){
		public void run(){
			if(canStart){
			time++;
			clock = Integer.toString(time / 60) + ":";	
			if(time % 60 < 10)
				clock += "0";
				clock += time % 60;
				totalTime.setText("Time: " + clock);
				controlPanel.updateUI();
				judgment(tested);
			}
		}	
	};
	
	public void start(){
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	public static void main(String [] args){
		Matcher timer = new Matcher();
		((Matcher)timer).start();
	}

	//Commanding GUI:
	public Matcher(){
		prepareGUI();
	}
	
	private void determineGrid(){
		try{
			FileReader reader = new FileReader(fileName);
			BufferedReader buffReader = new BufferedReader(reader);
			while((line = buffReader.readLine()) != null){
				rows++;
			}
			reader.close();	
		} 
		catch(FileNotFoundException e){
			System.out.println("No file?");
		} 
		catch(IOException x){
			System.out.println("Something's wrong...");	
		}
		
		array = new String[rows][columns];
		slots = new int[rows][columns];
		buttons = new JButton[rows][columns];

		try{
			FileReader reader = new FileReader(fileName);
			BufferedReader buffReader2 = new BufferedReader(reader);
			int placeholder = 1;
			while((line = buffReader2.readLine()) != null){
				
				/* Separating the Question and Answer...*/
				int midpoint = 0;
				for (int k = 0; k < line.length(); k++){
					if (line.charAt(k) == ','){
						midpoint = k;
					k = line.length();}	
				}
				
				//Printing out and setting up the Q&A Array:
				boolean input1 = false;
				boolean input2 = false;
				
				while(input1 == false){
			
					int randomQR = rng.nextInt(rows);
					int randomQC = rng.nextInt(columns);
				
					if(slots[randomQR][randomQC] == 0){
						array[randomQR][randomQC] = line.substring(0,midpoint);
						slots[randomQR][randomQC] = placeholder;
						System.out.println("R" + randomQR);
						System.out.println("C" + randomQC);
						System.out.println(array[randomQR][randomQC]);
						System.out.println(slots[randomQR][randomQC]);
						input1 = true;
				
					}
				}
				while(input2 == false){
					
					int randomAR = rng.nextInt(rows);
					int randomAC = rng.nextInt(columns);
					
					if(slots[randomAR][randomAC] == 0){
						array[randomAR][randomAC] = line.substring(midpoint + 1,line.length() - 1);
						slots[randomAR][randomAC] = placeholder;
						System.out.println("R" + randomAR);
						System.out.println("C" + randomAC);
						System.out.println(array[randomAR][randomAC]);
						System.out.println(slots[randomAR][randomAC]);
						input2 = true;	
					}
				}
				placeholder++;
			}
			
			reader.close();
		} 
		catch(FileNotFoundException e){
			System.out.println("No file?");
		} 
		catch(IOException x){
			System.out.println("Something's wrong...");
		}
		
		fileName = "Leaderboard.txt";
		int scorerows = 0;
		try {
			FileReader reader = new FileReader(fileName);
			BufferedReader scoreReader = new BufferedReader(reader);
			while((line = scoreReader.readLine()) != null){
				scorerows++;
			}
			reader.close();
		} 
		catch(FileNotFoundException e){
			System.out.println("No file?");
		} 
		catch(IOException x){
			System.out.println("Something's wrong...");
		}
		scores = new int[scorerows];
		names = new String[scorerows];
		
		try{
			int counter = 0;
			FileReader reader = new FileReader(fileName);
			BufferedReader scoreReader = new BufferedReader(reader);
			while((line = scoreReader.readLine()) != null){
				
				/* Separating the Name and Score...*/
				int midpoint = 0;
				for (int k = 3; k < line.length(); k++){
					if (line.charAt(k) == ','){
						midpoint = k;
					k = line.length();}	
				}
				
				names[counter] = line.substring(3,midpoint);
				String stringScore = line.substring(midpoint + 2,line.length());
				scores[counter] = Integer.valueOf(stringScore);
				counter++;
			}
			reader.close();
		} 
		catch(FileNotFoundException e){
			System.out.println("No file?");
		} 
		catch(IOException x){
			System.out.println("Something's wrong...");
		}
	}
	
	JPanel controlPanel = new JPanel();
	JPanel panel = new JPanel();
	private void prepareGUI(){
		//Call to the function above
		determineGrid();
			
		mainFrame = new JFrame("MAGTAB Matching Game");
		mainFrame.setSize(200 * columns,50 * rows + 100);
		mainFrame.setLayout(new GridLayout(rows, columns));
			
		mainFrame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent windowEvent){
		    	System.exit(0);
		        }});
		    //Setting up each button per Q & A...
		    for (int r = 0; r < rows; r++){
		    	for (int c = 0; c < columns; c++){
		    		int title = (1 + 2*r + c);
		    		
		    		String string = Integer.toString(title);
		    		JButton button = new JButton(string);
		    		buttons[r][c] = button;
		    		button.setIcon(image);
		    		
		    		button.addActionListener(this);
		    		
		    		mainFrame.add(button);	
		    	}
		    }
		    
		    //???
		    JLabel headerLabel = new JLabel("", JLabel.CENTER);        
		    
		    controlPanel.setLayout(new FlowLayout());

		    //Setting up the excess labels...
		    
		    totalTime = new JLabel("Time: " + clock);
		    
		    mainFrame.add(headerLabel);
		    mainFrame.add(controlPanel);
		    
		    //Timer Setup:
	        panel.setBackground(Color.red);
	        panel.setLayout(new FlowLayout());        
	        panel.add(totalTime);
	        controlPanel.add(panel);
	        
	        //Initial Score Setup:
	        scorepanel = new JPanel();
	        scorepanel.setBackground(Color.red);
	        scorepanel.setLayout(new FlowLayout());        
	        scorepanel.add(new JLabel("Score: " + Integer.toString(score)));
	        controlPanel.add(scorepanel);
	        namePlacard = new JTextField("Name Here:");
		    mainFrame.add(namePlacard);
		    JButton scoresButton = new JButton("Scores");
		    scoresButton.addActionListener(this);
		    mainFrame.add(scoresButton);
		    JButton resetButton = new JButton("Reset");
		    resetButton.addActionListener(this);
		    mainFrame.add(resetButton);
		    mainFrame.setVisible(true);  
		    JButton startButton = new JButton("Start");
			startButton.addActionListener(this);
		    mainFrame.add(startButton);
		    mainFrame.setVisible(true);
	}
	
	/*private void showJPanel(){
		//don't know what this is for   
	}*/
	
	//variables for checking matches
	String savedNum = "";
	String piece;
	String savedPiece = "";
	String Text;
	JButton tested;
	
	//checking whether cards match
	private void judgment(JButton e){
		if(judge == 1)
			judge++;
		else if(judge == 2){
			
			//If this is the second card, check the following...
			if(saved.equals(e)){
				
				//If it was the same card chosen, penalty...
				saved.setText(savedNum);
					score -= 5;
			} 
			else{
				//Else, check the placement of the second card...
				System.out.println(savedNum);
				int savedRowSlot = 0; 
				int savedColumnSlot = 0; 
				int newRowSlot = 0; 
				int newColumnSlot = 0;
				for (int k = 0; k < rows; k++){
					for (int j = 0; j < columns; j++){
						if(array[k][j].equals(savedPiece)){
							savedRowSlot = k; 
							savedColumnSlot = j; 
							j = columns;
							k = rows;
						}
					}
				}
				System.out.println(piece);
				for (int k = 0; k < rows; k++){
					for (int j = 0; j < columns; j++){
						if(array[k][j].equals(piece)){
							newRowSlot = k; 
							newColumnSlot = j; 
							j = columns;
							k = rows;
						}
					}
				}
				
				int savedPos = slots[savedRowSlot][savedColumnSlot];
				int newPos = slots[newRowSlot][newColumnSlot];
					
				System.out.println("HX" + newPos + savedPos);
					
				if(savedPos == newPos){
					System.out.println("Match!");
					saved.setText("MATCH");
					saved.disable();
					((AbstractButton) e).setText("MATCH");
					((Component) e).disable();
					score += 10;
					scorepanel.removeAll();
					scorepanel.setBackground(Color.red);
			        scorepanel.setLayout(new FlowLayout());        
			        scorepanel.add(new JLabel("Score: " + Integer.toString(score)));		
				} 
				else{
					/*This only occurs when the second card wasn't the first,
					 * And when the two chosen cards don't match.
					 * The only special piece here is a small reduction. */
					if(saved.getText() != "MATCH"){
						saved.setText(savedNum);
						saved.setIcon(image);}
					if(e.getText() != "MATCH"){
						e.setText(Text);
						e.setIcon(image);	
					}
					score -= 3;
					scorepanel.removeAll();
					scorepanel.setBackground(Color.red);
			        scorepanel.setLayout(new FlowLayout());        
			        scorepanel.add(new JLabel("Score: " + Integer.toString(score)));
				}
			}
			alreadyClicked = false;
			judge = 0;
		}
	}
	
	//Square Clicking:
	public void actionPerformed(ActionEvent e) {
		//if the player pressed score, it shows the scoreboard
		if(((AbstractButton) e.getSource()).getText().equals("Scores")){
			int replacedScore;
			int newlyReplacedScore;
			String replacedName;
			String newlyReplacedName;
			name = namePlacard.getText();
					
			//adjusts scoreboard
			for (int k = 0; k < scores.length; k++){					
				if(score > scores[k]){
					newlyReplacedScore = scores[k];
					newlyReplacedName = names[k];
					scores[k] = score;
					names[k] = name;
					for(int j = k + 1; j < names.length; j++){
						replacedScore = scores[j];
						replacedName = names[j];
						scores[j] = newlyReplacedScore;
						names[j] = newlyReplacedName;
						newlyReplacedScore = replacedScore;
						newlyReplacedName = replacedName;
					}
					k = scores.length;
				}
			}
			
			JFrame scoreFrame = new JFrame();
			JPanel scorePanel = new JPanel();
			JPanel scoresPanel = new JPanel();
				
			for (int i = 0; i < names.length; i++){		
				scoresPanel.add(new JLabel(Integer.toString(i + 1) + ": " + names[i] + ", " + Integer.toString(scores[i])));
			}
			scoreFrame.add(scoresPanel);
			scoreFrame.setSize(100, 25*scores.length);
			scoreFrame.setTitle("Leaderboard");
			scoreFrame.setVisible(true);
		}
		//If the player pressed reset, reset the board.
		else if (((AbstractButton) e.getSource()).getText().equals("Reset")){
			//Wiping the board clean...
			canStart = false;
			time = 0;
			namePlacard.setText("Name here:");
			for (int r = 0; r < rows; r++){
			   	for (int c = 0; c < columns; c++){
			   		int title = (1 + 2*r + c);   		
			   		buttons[r][c].setText(Integer.toString(title));
			   		buttons[r][c].setIcon(image);
			   		array[r][c] = "";
			    	slots[r][c] = 0;
			    	score = 0;
			    }
			}
				
			//Re-randomizing the order of buttons...
			fileName = "Text.txt";
			try {
				FileReader reader = new FileReader(fileName);
				BufferedReader buffReader2 = new BufferedReader(reader);
				int placeholder = 1;
				while((line = buffReader2.readLine()) != null){
					
					/* Separating the Question and Answer...*/
					int midpoint = 0;
					for (int k = 0; k < line.length(); k++){
						if (line.charAt(k) == ','){
							midpoint = k;
						k = line.length();}	
					}
					//Printing out and setting up the Q&A Array:
					boolean input1 = false;
					boolean input2 = false;
						
					while(input1 == false){
					
					int randomQR = rng.nextInt(rows);
					int randomQC = rng.nextInt(columns);
						
					if(slots[randomQR][randomQC] == 0){
						
						array[randomQR][randomQC] = line.substring(0,midpoint);
						slots[randomQR][randomQC] = placeholder;
						
						System.out.println("R" + randomQR);
						System.out.println("C" + randomQC);
						System.out.println(array[randomQR][randomQC]);
						System.out.println(slots[randomQR][randomQC]);
						input1 = true;
						}
					}
					while(input2 == false){
						int randomAR = rng.nextInt(rows);
						int randomAC = rng.nextInt(columns);
							
						if(slots[randomAR][randomAC] == 0){
							
							array[randomAR][randomAC] = line.substring(midpoint + 1,line.length() - 1);
							slots[randomAR][randomAC] = placeholder;
							
							System.out.println("R" + randomAR);
							System.out.println("C" + randomAC);
							System.out.println(array[randomAR][randomAC]);
							System.out.println(slots[randomAR][randomAC]);
							input2 = true;	
							}
						}
						placeholder++;
					}
					
					reader.close();
				} 
				catch(IOException x){
					System.out.println("Something's wrong...");
				}
			}
			else if (((AbstractButton) e.getSource()).getText().equals("Start")) {
				canStart = true;
			}
			else if(((AbstractButton) e.getSource()).getText() != "MATCH"){
				if(!alreadyClicked){
					saved = (JButton) e.getSource();
					savedNum = saved.getText();
				}
				//Always flip the card...
				((AbstractButton) e.getSource()).setIcon(null);
				Text = ((AbstractButton) e.getSource()).getText();
				if (saved.equals(e.getSource()) && alreadyClicked){
					
				} 
				else {
					Integer Number = Integer.valueOf(Text);
					System.out.println(Number);
					int Num = Number.intValue() - 1;
					piece = array[Num / 2][Num % 2];
					((AbstractButton) e.getSource()).setText(piece);
					
				}
				
				//If it's the first question chosen, save it...
				if(!alreadyClicked){
					savedPiece = saved.getText();
					alreadyClicked = true;
					System.out.println("Uh...");
				} 
				else if(judge <= 2){
					tested = (JButton) e.getSource();
					judge++;
					System.out.println(judge);
				}	
				else{
					((AbstractButton) e.getSource()).setText(piece);
					judge = 1;
					alreadyClicked = true;
				}
			}
		
			//Then, the score board is updated...
			scorepanel.removeAll();
			scorepanel.setBackground(Color.red);
	        scorepanel.setLayout(new FlowLayout());        
	        scorepanel.add(new JLabel("Score: " + Integer.toString(score)));      
	}
	
	
	
	//stops timer once game is done - unfinished
	public void timerStop(){
		int match = 0;
		for(int c = 0; c < array.length; c++){
			for(int r = 0; r < array[0].length; r++){
				if(array[c][r].equals("MATCH")){
					match++;
				}
			}
		}
		
		if(match == (array.length * array[0].length)){
			canStart = false;
		}
	}

}

