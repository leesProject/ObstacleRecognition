import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashSet;


public class MainFrame extends JFrame{
	static Toolkit tk=Toolkit.getDefaultToolkit();
	static Dimension screen_size=tk.getScreenSize(); /// How big your Screeen is?
		
	static int Grid_Num=20;	
	static String title="PathFinding for Mobile Agents";	
	static JLabel resultLabel;
	
	int buttonWidth;
	int buttonHeight;
	static Grid grids[][];	
	MainFrame mainframe = this;
	static boolean add_Pressed = false;
	
	///
	
	LinkedList<Obstacle> obstacleList;
	LinkedList<Obstacle> updateOstacleList;
	HashSet<Integer> SearchCompleted;
		
	public static void main(String[] args) {		
		Grid_Num=20;	
		new MainFrame(title, 705, 728).setVisible(true);
	}
	
	MainFrame(String para_title, int width, int height ){
		super("PathFindgin for Mobile Agents");	
		
		width=width-105;
		
		for(int i=width;i>=0;i--){
			if(i%(2*Grid_Num)==0){
				width=i+105;
				height=i+128;
				break;
			}
		}
		
		if(width==0){
			System.out.println("Sizing Error! Change the Number of Grids!");
			return;
		}
		
		buttonWidth=(width-105)/Grid_Num;
		buttonHeight=(height-128)/Grid_Num;	
		
		
		grids= new Grid[MainFrame.Grid_Num][MainFrame.Grid_Num];		
		
		for(int i=0;i<Grid_Num;i++){		
			for(int j=0;j<Grid_Num;j++){				
				grids[i][j]=new Grid();	
				grids[i][j].setEnabled(false);
				grids[i][j].row=i;
				grids[i][j].col=j;
				grids[i][j].setFocusable(false);				
				grids[i][j].setSize(buttonWidth,buttonHeight);				
				grids[i][j].setLocation(j*buttonWidth+1,height-128-((i+1)*buttonHeight));		
				this.getContentPane().add(grids[i][j]);					
			}
		}		
		
		
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode()==(int)'A'){
					mainframe.add_buttonAction();
				}				
			}
		});
		
		
		resultLabel=new JLabel();
		resultLabel.setText("Result Label");
		resultLabel.setSize(300,20);
		resultLabel.setLocation(5,height-48);
		resultLabel.setVisible(true);
		
		
		this.setFocusable(true);
		//this.add(add_button);	
		this.getContentPane().add(resultLabel);
		this.getContentPane().setLayout(null);;	
		
		setSize(width, height);
		setLocation((screen_size.width-width)/2, (screen_size.height-height)/2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setResizable(false);		
	}
	
	public void add_buttonAction(){
		if(add_Pressed==false){
			add_Pressed=true;
			resultLabel.setText("Adding starts!");
		}
		
		else if(add_Pressed==true&&Grid.click==0){
			add_Pressed=false;
			resultLabel.setText("Adding ends!");
			distinguishObstacles();
		}
	}
	public void distinguishObstacles(){
	
		initialization();
		
		
		/*
		 * 	obstacleList = new LinkedList<Obstacle>();
		 *	updateOstacleList = new LinkedList<Obstacle>();
		 * */	
		
		SearchCompleted = new HashSet<Integer>();
		GridPaintThread gp = new GridPaintThread(this);
		gp.start();		
		
	}
	
	public void initialization(){
		Iterator<Grid> iter = Grid.selectedGSet.iterator();
		obstacleList = new LinkedList<Obstacle>();
		updateOstacleList = new LinkedList<Obstacle>();
		
		
		while(iter.hasNext()){
			iter.next().blocked=1;
		}
		
		
		for(int i=0;i<Grid_Num;i++){
			for(int j=0;j<Grid_Num;j++){
				if(grids[i][j].blocked == 1){
					obstacleList.add(new Obstacle());
					updateOstacleList.add(new Obstacle());
					
					Obstacle obs = obstacleList.get(obstacleList.size()-1);
					Obstacle updateObs = updateOstacleList.get(updateOstacleList.size()-1);
					
					while(grids[i][j].blocked == 1){
						obs.gridList.add(grids[i][j]);
						updateObs.gridList.add(grids[i][j]);
						j++;
						if(j==Grid_Num){
							break;
						}
					}
				}
				else{}
			}
		}
				
		
		for(int i=0;i<obstacleList.size();i++){
			obstacleList.get(i).changedIndex=i;
			obstacleList.get(i).originalIndex=i;
			for(int j=0;j<obstacleList.get(i).gridList.size();j++){
				obstacleList.get(i).gridList.get(j).obstacleNum=i;			
			}
		}		
	}	
}




class GridPaintThread extends Thread{
		
	MainFrame mainframe;
	LinkedList <Obstacle> obList; 
	LinkedList <Obstacle> upObeList;
	
	
	HashSet searched;
	
	GridPaintThread(MainFrame paraMain){		
		this.mainframe=paraMain;
		this.obList = mainframe.obstacleList;
		this.upObeList = mainframe.updateOstacleList;
		this.searched = mainframe.SearchCompleted;
	}
	
	public void run(){		
		
		loop1:
		for(int i=0;i<obList.size();i++){
			Obstacle tempObs = obList.get(i);	
			//System.out.println("loop1  "+i);
			loop2:
			for(int j=0;j<tempObs.gridList.size();j++){
				//System.out.println("loop2  "+j);
				Grid curGrid = tempObs.gridList.get(j);
				//System.out.println("tempObs ");
				// if next grid does not violate row indexes start
				if(curGrid.row<mainframe.Grid_Num-1){
					searched.add(curGrid.obstacleNum);
					
					Grid nextGrid = mainframe.grids[curGrid.row+1][curGrid.col];
					
					if(curGrid.obstacleNum==nextGrid.obstacleNum){}
					
					// if the next grid is already searched start!
					else if(searched.contains(nextGrid.obstacleNum)){
						//System.out.println("else if 2");
						searched.remove(curGrid.obstacleNum);						
						
						upObeList.get(nextGrid.obstacleNum).gridList.addAll(upObeList.get(curGrid.obstacleNum).gridList);
						upObeList.get(curGrid.obstacleNum).gridList.clear();
						
						for(int k=0;k<upObeList.get(nextGrid.obstacleNum).gridList.size();k++){
							upObeList.get(nextGrid.obstacleNum).gridList.get(k).obstacleNum = nextGrid.obstacleNum;
						}						
					}
					// if the next grid is already searched end!
										
					else if(nextGrid.obstacleNum>0){
						//System.out.println("else if 3  "+ upObeList.get(curGrid.obstacleNum).gridList.size());
						//System.out.println(upObeList.get(nextGrid.obstacleNum));
						upObeList.get(curGrid.obstacleNum).gridList.addAll(upObeList.get(nextGrid.obstacleNum).gridList);
						//System.out.println("else if 4");
						upObeList.get(nextGrid.obstacleNum).gridList.clear();
						//System.out.println("else if 5");
						for(int k=0;k<upObeList.get(curGrid.obstacleNum).gridList.size();k++){
							upObeList.get(curGrid.obstacleNum).gridList.get(k).obstacleNum = curGrid.obstacleNum;
						}											
					}
				}// if next grid does not violate row indexes end
			}// loop2 end			
		}// loop1 end
		
	
		int numOfBlocks =0;
		
		for(int i=0;i<upObeList.size();i++){
			System.out.println(upObeList.get(i));
			if(upObeList.get(i).gridList.size()>0){
				numOfBlocks++;
				for(int j=0;j<upObeList.get(i).gridList.size();j++){
					upObeList.get(i).gridList.get(j).setBackground(Color.BLUE);
					upObeList.get(i).gridList.get(j).setOpaque(true);						
				}
				
				try{
					sleep(500);
				}catch(Exception e){}
				
				
				
				for(int j=0;j<upObeList.get(i).gridList.size();j++){
					upObeList.get(i).gridList.get(j).setBackground(Color.GRAY);
					upObeList.get(i).gridList.get(j).setOpaque(true);					
							
				}
			}			
		}
		
		System.out.println("There are "+numOfBlocks+" blocks!");
	
	}// run method ends!
}