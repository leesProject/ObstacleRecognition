import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Grid extends JButton{
	
	int blocked = -1;				
	int obstacleNum = -1;
	
	
	static int click=0;
	static int clicked_row[]=new int[2];
	static int clicked_col[]=new int[2];
	static HashSet selectedGSet = new HashSet<Grid>();
	
	int row, col;
	
	Grid(){
		
		Grid g = this;
		setContentAreaFilled(false);
		setBackground(Color.white);
		setOpaque(true);
		
		
		addMouseListener(new MouseAdapter() { // mouseAdapter grid ���� ��
			public void mouseReleased(MouseEvent me) { // override
				//System.out.println(row+", "+col+"\t");
				
				
				if(MainFrame.add_Pressed==true){
					
					MainFrame.resultLabel.setText("Selec the end point");
					setBackground(Color.gray);
					setOpaque(true);							
					
					clicked_row[click]=row;
					clicked_col[click]=col;
					click++;
					
					if(click==2){
						
						int row_min=Math.min(clicked_row[0], clicked_row[1]);
						int row_max=Math.max(clicked_row[0], clicked_row[1]);
						int col_min=Math.min(clicked_col[0], clicked_col[1]);
						int col_max=Math.max(clicked_col[0], clicked_col[1]);
						
						for(int a=row_min;a<=row_max;a++){
							for(int b=col_min;b<=col_max;b++){								
								
								MainFrame.grids[a][b].setBackground(Color.gray);
								MainFrame.grids[a][b].setOpaque(true);
								
								selectedGSet.add(MainFrame.grids[a][b]);
							}
						}
						MainFrame.resultLabel.setText("If you wanna quit adding, Press a");
						click=0;						
					}///click==2
				}///
			}
		}); // mouseAdapter		
		setVisible(true);
	}
}
