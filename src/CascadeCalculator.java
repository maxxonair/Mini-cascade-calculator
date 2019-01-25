
import java.io.*;
import java.net.URISyntaxException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

//import java.sql.*;
//import java.util.Scanner;

//import javax.swing.ImageIcon;
import javax.swing.*;
//import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

//import javax.swing.JFrame;
//import java.lang.ProcessBuilder;

//import javax.imageio.ImageIO;

public class CascadeCalculator implements  ActionListener{

    // Definition of global values and items that are part of the GUI.
    String redScoreAmount = "void";
    int blueScoreAmount = 0;
    public static double E = 2.7182818284590452353602874713527; // Euler number
    public static double g_0 = 9.80665;   		// Average gravitational factor [m/s^2]
    double margin_prop = 0;         // Fuel margin [percent]
    double m_init;
    double m_payload;
    double boiloff_rate = 0;

    
    
    public String content, lae_content, lde_content;

    JPanel titlePanel, scorePanel, buttonPanel, resPanel;
    JLabel redLabel, blueLabel, redScore, Linp1, Linp2, Lres1, Lres2, Lres3, Lres4, Res1, Res2, Res3, Res4,  boiloff_label, boiloff_label2, steering_label, margin_label , FuelMar_label;
    JButton redButton, blueButton, resetButton, greenButton, purpleButton, yellowButton;
    JButton moveup, movedown;
    JTextField   Minit, Mpayload, BoilOff_TF, FuelMar_TF;
    JPasswordField bluePWD;
    JScrollPane ResBar, LAEBar, LDEBar;
    JTextArea textArea;
	JTextArea LAEArea;
	JTextArea LDEArea;
	JTable table;
	
	JCheckBox boiloff_box, steering_box, margin_box;
	
	DefaultTableModel model;
	
    String[] columns = { "Event", "Delta-V [m/s]", "ISP [s]", "Steering loss margin [%]", "Margin [%]", "Final Delta-V [m/s]", "Time T+ [days] ",  "Add. Propellant [kg]", "Add. Propellant to compensate Boil-off",  "Propellant mass [kg]", "S/C mass [kg] |pre|", "S/C mass [kg] |post|", "Tank content [kg]"};
    
    Object[] row = new Object[13];
    

	
	BufferedImage myImage ;
	
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "CascadeFiles", "csv");

    public JPanel createContentPane () throws IOException{

        // We create a bottom JPanel to place everything on.
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        
        // Creation of a Panel to contain the title labels
        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(0, 0);
        titlePanel.setSize(1360, 20);
        totalGUI.add(titlePanel);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(0, 20);
        buttonPanel.setSize(100, 500);
        totalGUI.add(buttonPanel);
        resPanel = new JPanel();
        resPanel.setLayout(null);
        resPanel.setLocation(100, 290);
        resPanel.setSize(800, 600);
        totalGUI.add(resPanel);

        redButton = new JButton("Update");
        redButton.setLocation(0, 0);
        redButton.setSize(87, 30);
        redButton.addActionListener(this);
        buttonPanel.add(redButton);
        
        boiloff_box = new JCheckBox();
        boiloff_box.setLocation(400, 70);
        boiloff_box.setSize(10, 10);
        boiloff_box.setHorizontalAlignment(0);
        resPanel.add(boiloff_box);
        
        boiloff_label = new JLabel("Boil-off ");
        boiloff_label.setLocation(420, 65);
        boiloff_label.setSize(190, 20);
        boiloff_label.setHorizontalAlignment(0);
        boiloff_label.setHorizontalAlignment(SwingConstants.LEFT);
        resPanel.add(boiloff_label);
        
        BoilOff_TF = new JTextField("0.025");
        BoilOff_TF.setLocation(550, 67);
        BoilOff_TF.setSize(40, 20);
        BoilOff_TF.setHorizontalAlignment(0);
        BoilOff_TF.setForeground(Color.black);
        resPanel.add(BoilOff_TF );
        
        boiloff_label2 = new JLabel("[%/day]");
        boiloff_label2.setLocation(600, 65);
        boiloff_label2.setSize(50, 20);
        boiloff_label2.setHorizontalAlignment(0);
        boiloff_label2.setHorizontalAlignment(SwingConstants.LEFT);
        resPanel.add(boiloff_label2);
        
        FuelMar_TF = new JTextField("2.0");
        FuelMar_TF.setLocation(290, 5);
        FuelMar_TF.setSize(40, 20);
        FuelMar_TF.setHorizontalAlignment(0);
        FuelMar_TF.setForeground(Color.black);
        resPanel.add(FuelMar_TF );
        
        FuelMar_label = new JLabel("[%]");
        FuelMar_label.setLocation(335, 4);
        FuelMar_label.setSize(50, 20);
        FuelMar_label.setHorizontalAlignment(0);
        FuelMar_label.setHorizontalAlignment(SwingConstants.LEFT);
        resPanel.add(FuelMar_label);
        
        steering_box = new JCheckBox();
        steering_box.setLocation(400, 95);
        steering_box.setSize(10, 10);
        steering_box.setHorizontalAlignment(0);
        resPanel.add(steering_box);
        
        steering_label = new JLabel("Steering loss ");
        steering_label.setLocation(420, 90);
        steering_label.setSize(190, 20);
        steering_label.setHorizontalAlignment(0);
        steering_label.setHorizontalAlignment(SwingConstants.LEFT);
        resPanel.add(steering_label);
        
        margin_box = new JCheckBox();
        margin_box.setLocation(400, 120);
        margin_box.setSize(10, 10);
        margin_box.setHorizontalAlignment(0);
        resPanel.add(margin_box);
        
        margin_label = new JLabel("Calculate with margin");
        margin_label.setLocation(420, 115);
        margin_label.setSize(190, 20);
        margin_label.setHorizontalAlignment(0);
        margin_label.setHorizontalAlignment(SwingConstants.LEFT);
        resPanel.add(margin_label);
        
        blueButton = new JButton("Add");
        blueButton.setLocation(0, 35);
        blueButton.setSize(87, 30);
        blueButton.addActionListener(this);
        buttonPanel.add(blueButton);
        
        greenButton = new JButton("Delete");
        greenButton.setLocation(0, 70);
        greenButton.setSize(87, 30);
        greenButton.addActionListener(this);
        buttonPanel.add(greenButton);
        
        purpleButton = new JButton("Export");
        purpleButton.setLocation(0, 280);
        purpleButton.setSize(87, 30);
        purpleButton.addActionListener(this);
        buttonPanel.add(purpleButton);
        
        yellowButton = new JButton("Import");
        yellowButton.setLocation(0, 315);
        yellowButton.setSize(87, 30);
        yellowButton.addActionListener(this);
        buttonPanel.add(yellowButton);
        
        
       
        //int returnVal = fc.showOpenDialog(aComponent);
        
        moveup = new JButton("up");
        moveup.setLocation(400, 0);
        moveup.setSize(70, 30);
        moveup.addActionListener(this);
        resPanel.add(moveup);
        
        movedown = new JButton("down");
        movedown.setLocation(475, 0);
        movedown.setSize(70, 30);
        movedown.addActionListener(this);
        resPanel.add(movedown);
 
        Linp1 = new JLabel("Wet mass [kg]");
        Linp1.setLocation(0, 105);
        Linp1.setSize(87, 30);
        Linp1.setHorizontalAlignment(0);
        buttonPanel.add(Linp1);
        Minit = new JTextField("-");
        Minit.setLocation(0, 140);
        Minit.setSize(87, 30);
        Minit.setHorizontalAlignment(0);
        Minit.setForeground(Color.black);
        buttonPanel.add(Minit );
        Linp2 = new JLabel("Payload [kg]");
        Linp2.setLocation(0, 175);
        Linp2.setSize(87, 30);
        Linp2.setHorizontalAlignment(0);
        buttonPanel.add(Linp2);
        Mpayload = new JTextField("-");
        Mpayload.setLocation(0, 210);
        Mpayload.setSize(87, 30);
        Mpayload.setHorizontalAlignment(0);
        Mpayload.setForeground(Color.black);
        buttonPanel.add(Mpayload );
        //--------------------------------------------------
        Lres1 = new JLabel("Residual propellant mass [kg] ");
        Lres1.setLocation(0, 0);
        Lres1.setSize(200, 30);
        Lres1.setHorizontalAlignment(JLabel.LEFT);
        resPanel.add(Lres1);
        Lres2 = new JLabel("Total propellant mass [kg] ");
        Lres2.setLocation(0, 35);
        Lres2.setSize(200, 30);
        Lres2.setHorizontalAlignment(JLabel.LEFT);
        resPanel.add(Lres2);
        Lres3 = new JLabel("Maximum dry mass [kg] ");
        Lres3.setLocation(0, 70);
        Lres3.setSize(200, 30);
        Lres3.setHorizontalAlignment(JLabel.LEFT);
        resPanel.add(Lres3);

        Lres4 = new JLabel("Total Delta-V [m/s] ");
        Lres4.setLocation(0, 105);
        Lres4.setSize(200, 30);
        Lres4.setHorizontalAlignment(JLabel.LEFT);
        resPanel.add(Lres4);

        //------------------------------------------------
        Res1 = new JLabel("-");
        Res1.setLocation(205, 0);
        Res1.setSize(87, 30);
        Res1.setHorizontalAlignment(0);
        resPanel.add(Res1);
        Res2 = new JLabel("-");
        Res2.setLocation(205, 35);
        Res2.setSize(87, 30);
        Res2.setHorizontalAlignment(0);
        resPanel.add(Res2);
        Res3 = new JLabel("-");
        Res3.setLocation(205, 70);
        Res3.setSize(87, 30);
        Res3.setForeground(Color.red);
        Res3.setHorizontalAlignment(0);
        resPanel.add(Res3);

        Res4 = new JLabel("-");
        Res4.setLocation(205, 105);
        Res4.setSize(87, 30);
        Res4.setHorizontalAlignment(0);
        resPanel.add(Res4);

        
        int tablewidth = 1230;
        int tablehight = 250;
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setLocation(100,20);
        panel.setSize(tablewidth,tablehight);
        totalGUI.add(panel);
        
        table = new JTable();
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table.setModel(model);
        
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(190);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(50);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(70);
        table.getColumnModel().getColumn(5).setPreferredWidth(110);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(60);
        table.getColumnModel().getColumn(8).setPreferredWidth(60);
        table.getColumnModel().getColumn(9).setPreferredWidth(130);
        table.getColumnModel().getColumn(10).setPreferredWidth(135);
        table.getColumnModel().getColumn(11).setPreferredWidth(135);
        table.getColumnModel().getColumn(12).setPreferredWidth(100);


        
        JScrollPane spTable = new JScrollPane(table);
        spTable.setLocation(0,0);
        spTable.setSize(tablewidth,tablehight);

        panel.add(spTable);
        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e)  {
        if(e.getSource() == redButton)
        {   
        	int n_rows = model.getRowCount(); 
        	m_init = Double.parseDouble(Minit.getText());
        	m_payload = Double.parseDouble(Mpayload.getText());
        	boiloff_rate = Double.parseDouble(BoilOff_TF.getText());
        	margin_prop = Double.parseDouble(FuelMar_TF.getText());
        	double deltav_int, deltav, m_prop;
        	double m_prop_total=0;
        	double[] isp = null;
        	isp = new double[n_rows];
        	double[] add_loss = null ; 
        	add_loss = new double[n_rows];
        	double[] m_prop_int = null ; 
        	m_prop_int = new double[n_rows];
        	double[] m_tank_int = null ; 
        	m_tank_int = new double[n_rows];
        	double[] m_bo_corr = null ;              // Boil-off correction
        	m_bo_corr = new double[n_rows];
        	double[] m_SC_c = null ;                 // Current spacecraft mass 
        	m_SC_c = new double[n_rows];
        	double mar_sl, mar_gen;
        	double[] t_days = null ;                 // Time from launch 
        	t_days = new double[n_rows];
        	double total_corr = 0 ;
        	double total_corr_mo = 0;
        	double conv_a = 0.000000000001;
        	double m_steering=0;
        	double m_total_steering_loss=0; 
        	int k =0;
        	double m_prop_total_int = 0;
        	double m_tank=0;
        	double total_deltav = 0; 
        	double total_boiloff_int = 0; 
        	double total_add_loss=0;
        	DecimalFormat df = new DecimalFormat("#.#");
    		for(int i =0 ; i < n_rows; i++){ // Read data from table 
	        	t_days[i] = Double.parseDouble((String) model.getValueAt(i, 6));
	        	isp[i]    	=  Double.parseDouble((String) model.getValueAt(i, 2));
	        	add_loss[i] =  Double.parseDouble((String) model.getValueAt(i, 7));
        		total_add_loss = total_add_loss + add_loss[i] ;
        		//System.out.println(add_loss[i]  + " -- " + total_add_loss);
    		}
        	m_prop_total=0;
        	while( (Math.sqrt(Math.pow((total_corr-total_corr_mo), 2.0)) < conv_a && k > 500 && k < 3000) == false )
        	{
        		if(m_prop_total>0 && boiloff_box.isSelected()==true){
        			m_tank=m_prop_total;
            	for(int i =0 ; i < n_rows; i++){
            		if (i>0){
            		m_tank_int[i] = m_tank - m_prop_int[i-1];
            		m_tank = m_tank - m_prop_int[i-1];
            		m_bo_corr[i]= m_tank_int[i] * (  Math.pow(((boiloff_rate+100)/100), (t_days[i]-t_days[i-1])) - 1);
            		} else { 
            		total_corr=0;
            		m_bo_corr[i]= m_prop_total * (t_days[i])*boiloff_rate/100;
            		}
            		total_corr = total_corr + m_bo_corr[i];
            	}
        		}
        		double fuel_margin= (margin_prop/100) * m_prop_total ;
        		m_prop_total = 0;
        		m_prop_total_int = 0;
        		m_total_steering_loss = 0; 
        		total_deltav = 0; 
        		total_boiloff_int = 0; 
        		m_SC_c[0] = m_init;
					        	for(int i =0 ; i < n_rows; i++){
					        	deltav_int 	=  Double.parseDouble((String) model.getValueAt(i, 1));
							        	if (margin_box.isSelected()==true){
							        	mar_gen = Double.parseDouble((String) model.getValueAt(i, 4));
							        	}  else {
							        	mar_gen = 0 ;
							        	}
					        	deltav = deltav_int * (100+mar_gen)/100 ;
					        	m_steering = 0; 
					        	double deltav_steering_loss= 0;
							        	if(steering_box.isSelected()==true){
							        		mar_sl = Double.parseDouble((String) model.getValueAt(i, 3)); 
							        		//model.setValueAt("" + df.format(mar_sl), i, 3);
							        		deltav_steering_loss = (mar_sl/100) * deltav; 
							        		double isp_steering_loss_correction = 285; 
							        		m_steering = (m_init-m_prop_total) * (1-Math.exp(-(deltav_steering_loss/(g_0 * isp_steering_loss_correction))));
							        	} else { 
							        		mar_sl = 0; 
							        		deltav_steering_loss = 0 ;
							        		m_steering = 0; 
							        	}

					        	m_total_steering_loss = m_total_steering_loss + m_steering; 
							        	if(boiloff_box.isSelected()==true){
							        	total_boiloff_int = total_boiloff_int + m_bo_corr[i] ; 
							        	double m_w_current = m_init - m_prop_total_int - total_boiloff_int;
							        	m_SC_c[i] = m_w_current;
							            m_prop  = (m_w_current) * (1-Math.exp( -(deltav/(g_0 * isp[i])))) + m_steering;
							           // System.out.println(i + " -- " + df.format((m_w_current)) +  " ; " + df.format(deltav) + " ; " + df.format(isp[i]) + " ; " + df.format(((m_w_current) * (1 - Math.exp(-deltav/(g_0 * isp[i]) )))) + " ; " + df.format(m_bo_corr[i]));
							        	} else {
							        	double m_w_current = m_init-m_prop_total_int;
							        	m_SC_c[i] = m_w_current;
							        	m_prop  = (m_w_current) * (1-Math.exp( -(deltav/(g_0 * isp[i])))) + m_steering;
							        	//System.out.println(i + " -- " + df.format((m_w_current)) +  " ; " + df.format(deltav) + " ; " + df.format(isp[i]) + " ; " + df.format(((m_w_current) * (1 - Math.exp(-deltav/(g_0 * isp[i]) )))) + " ; " + df.format(m_bo_corr[i]));
							        	}
							        	
					            m_prop_int[i]= m_prop;

					            m_prop_total_int = m_prop_total_int + m_prop ;
					            if (i==0) {
					            	 m_prop_total = m_prop + fuel_margin + total_corr + total_add_loss ;
					            } else {
					            	 m_prop_total = m_prop_total + m_prop;
					        	}
					           
					            total_deltav = total_deltav + deltav; 
					            model.setValueAt("" + df.format(deltav), i, 5);
					            model.setValueAt("" + df.format(m_prop), i, 9);
					            if (i<(n_rows-1)){
					            model.setValueAt("" + df.format(m_SC_c[i+1]- add_loss[i+1]), i+1, 10);
					            model.setValueAt("" + df.format(m_SC_c[i+1]+m_bo_corr[i+1]), i, 11);
					            //model.setValueAt("" + df.format(m_SC_c[i]-m_prop) + " - " + df.format(m_SC_c[i]-m_prop+m_bo_corr[i+1]), i, 10);
					            if(i==0) {
					            model.setValueAt("" + df.format(m_init), i, 10);
					            } } else {
					            model.setValueAt("" + df.format(m_SC_c[i]- add_loss[i]), i, 10);
					            model.setValueAt("" + df.format(m_SC_c[i]-m_prop+m_bo_corr[i]) , i, 11);
					            //model.setValueAt("" + df.format(m_SC_c[i]-m_prop) + " - " + df.format(m_SC_c[i]-m_prop+m_bo_corr[i+1]), i, 10);
					            }
					            model.setValueAt("" + df.format(0), i, 8);
					        	}
            if(m_prop_total>0  && boiloff_box.isSelected()==true){
            	m_tank=m_prop_total;
            	for(int i =0 ; i < n_rows; i++){
        		if (i>0){
        		m_tank_int[i] = m_tank - m_prop_int[i] - m_bo_corr[i] - add_loss[i];
        		m_tank = m_tank - m_prop_int[i] - m_bo_corr[i] - add_loss[i];
        		//System.out.println(i + "  :  " +  m_prop_int[i-1] + "  :  " +  m_tank_int[i]);
        		//m_bo_corr[i]= m_tank_int[i] * (  Math.pow(((boiloff_rate+100)/100), (t_days[i]-t_days[i-1]))  -  1 );
        		} else { 
        		total_corr_mo =0;
        		m_bo_corr[i]= m_prop_total * (t_days[i])*boiloff_rate/100;
        		}
        		total_corr_mo = total_corr_mo + m_bo_corr[i];
        		if(boiloff_box.isSelected()==true){
        		model.setValueAt("" + df.format(m_bo_corr[i]), i, 8);
        		} 
            }
        	} 
            
        	k++;
        	//double convergence = Math.sqrt(Math.pow((total_corr-total_corr_mo), 2.0)); 
        	//System.out.println("" + k + " ; " + convergence + "  --> " + total_corr_mo + " ; " + total_corr);	
        }
        	m_tank = m_prop_total;
        	for(int i =0 ; i < n_rows; i++){
        		m_tank = m_tank - m_prop_int[i] - m_bo_corr[i] - add_loss[i]; 
        		model.setValueAt("" + df.format(m_tank ), i, 12);
        	}
        	double m_fremain = m_tank;
        	double m_str = m_init - m_prop_total - m_payload;
        	Res1.setText("" + df.format(m_fremain));
        	Res2.setText("" + df.format(m_prop_total));
        	Res3.setText("" + df.format(m_str));
        	Res4.setText("" + df.format(total_deltav));
        	if(steering_box.isSelected()==true){
        	steering_label.setText("Steering loss ( " + df.format(m_total_steering_loss ) + " kg ) ");
        	} else  {
        	steering_label.setText("Steering loss ( " + df.format(0) + " kg ) ");	
        	}
        	if(boiloff_box.isSelected()==true){
        	boiloff_label.setText("Boil-off ( " + df.format(total_corr_mo) + " kg ) ");   
        	} else {
        	boiloff_label.setText("Boil-off ( " + df.format(0) + " kg ) "); 	
        	}
        	//--------------------------------------------------------------
        	
        	
        }
            else if(e.getSource() == blueButton)
        {
            	row[0] = "---";
            	row[1] = "1500";
            	row[2] = "340";
            	row[3] = "2.5";
            	row[4] = "5";
            	row[5] = "-";
            	row[6] = "1";
            	row[7] = "0";
            	row[8] = "-";
            	row[9] = "-";
            	row[10] = "-";
            	row[11] = "-";
            	row[12] = "-";
            	
            	
            	model.addRow(row);
        }
            else if(e.getSource() == greenButton)
            {
            	int i = table.getSelectedRow();
            	if (i >= 0){
            		model.removeRow(i);
            	}
            	else{
            		System.out.println("Delete Error");
            	}
            }
            else if(e.getSource() == purpleButton)            // EXPORT
            {
            	int n_rows = model.getRowCount(); 
            	if ( n_rows > 0 ) {
                try {
                	File myfile;
					myfile = new File(CascadeCalculator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
					
            	JFileChooser fileChooser = new JFileChooser(myfile);
            	if (fileChooser.showSaveDialog(purpleButton) == JFileChooser.APPROVE_OPTION) {
            	  File filer = fileChooser.getSelectedFile();
            	  //System.out.println(filer);
            	  // save to file
            	}
                File file = fileChooser.getSelectedFile();
            	PrintWriter os;
				try {
					os = new PrintWriter(file);
					String head_line = Minit.getText() + ";" + Mpayload.getText() + ";";    
					os.print(head_line);
					os.println("");
	            	for (int i = 0; i < model.getRowCount(); i++) {
	            	    for (int col = 0; col < model.getColumnCount(); col++) {
	            	        //os.print(table.getColumnName(col));
	            	        //os.print(": ");
	            	        os.print(model.getValueAt(i, col));
	            	        os.print(";");
	            	    }
	            	    os.println("");
	            	}
                    os.close();
                    //System.out.println("Done!");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				} catch (URISyntaxException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            	}
            }
            else if(e.getSource() == yellowButton)             // IMPORT
            {
            	if (model.getRowCount()>0){
            	for (int i = model.getRowCount(); i >= 1; i--) {
            		model.removeRow(i-1);
            	}
            	}
                try {
                	File myfile;
					myfile = new File(CascadeCalculator.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
					JFileChooser chooser = new JFileChooser(myfile);
					
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(yellowButton);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	//System.out.println(chooser.getSelectedFile().getName());
                }
            	   try{
            		   //System.out.println(chooser.getSelectedFile().getName());
            		   String import_file = chooser.getSelectedFile().getName();
            		    FileInputStream fstream = new FileInputStream(import_file);
            		          DataInputStream in = new DataInputStream(fstream);
            		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
            		          String strLine;
            		          int index=0; 
            		          while ((strLine = br.readLine()) != null)   {
            		        	  if (index == 0 ){
            		        		  String[] tokens = strLine.split(";");
            		        		  Minit.setText(tokens[0]);
            		        		  Mpayload.setText(tokens[1]);
            		        	  } else {
            		        	  	String[] tokens = strLine.split(";");
            		            	row[0] = tokens[0];
            		            	row[1] = tokens[1];
            		            	row[2] = tokens[2];
            		            	row[3] = tokens[3];
            		            	row[4] = tokens[4];
            		            	row[5] = tokens[5];
            		            	row[6] = tokens[6];
            		            	row[7] = tokens[7];
            		            	row[8] = tokens[8];
            		            	row[9] = tokens[9];
            		            	row[10] = tokens[10];
            		            	row[11] = tokens[11];
            		            	//row[11] = tokens[11];
            		            	model.addRow(row);
            		        	  }
            		            	index++;
            		          }
            		          in.close();
            		          }catch (FileNotFoundException e1) {
            						// TODO Auto-generated catch block
            						e1.printStackTrace();
            					} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				} catch (URISyntaxException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
            }
            else if(e.getSource() == moveup){
                DefaultTableModel model =  (DefaultTableModel)table.getModel();
                int[] rows = table.getSelectedRows();
                model.moveRow(rows[0],rows[rows.length-1],rows[0]-1);
                table.setRowSelectionInterval(rows[0]-1, rows[rows.length-1]-1);
            }
            else if(e.getSource() == movedown){
                DefaultTableModel model =  (DefaultTableModel)table.getModel();
                int[] rows = table.getSelectedRows();
                model.moveRow(rows[0],rows[rows.length-1],rows[0]+1);
                table.setRowSelectionInterval(rows[0]+1, rows[rows.length-1]+1);
            }

 
 }

    private static void createAndShowGUI() throws IOException{

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("  Delta-V Cascade-Calculator  |||   V1.15");

        //Create and set up the content pane.
        CascadeCalculator demo = new CascadeCalculator();
        frame.setContentPane(demo.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1345, 470);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        
        BufferedImage myImage = ImageIO.read(new File("C:\\logo.png"));
        frame.setIconImage(myImage);
    }

    public static void main(String[] args) throws IOException {
        //Schedule a job for the event-dispatching thread:
    	//Process process=Runtime.getRuntime().exec("LDE_EXE.exe");

        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
					createAndShowGUI();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}
