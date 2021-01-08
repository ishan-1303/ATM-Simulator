package ATM;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;


public class PINChange extends JFrame implements ActionListener
{
	int pin=0,pin2=0, a=0,opin=0;
	JLabel l1,amtLabel,title;
	JPanel p1;
	JTextField amtText;
	JButton confirm,cancel;
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	long card_no=0;
	
	PINChange(long card_no,int hp)
	{
		setTitle("PIN Change");
		setSize(639,639);
		setResizable(false);
		setLayout(null);
		
		this.card_no=card_no;
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		init();
		
		this.opin=hp;
		
		confirm.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	private void init()			//to initialize components
	{
		p1=new JPanel();
		p1.setBounds(0, 0, this.getWidth(), this.getHeight());
		p1.setLayout(null);
		p1.setBackground(Color.BLUE);
		
		title=new JLabel("PIN CHANGE");
		title.setBounds(231, 33, 211, 33);
		title.setFont(new Font("Serif",Font.BOLD,20));
		title.setForeground(Color.WHITE);
		
		//time code
			final JLabel timeLabel = new JLabel();
			timeLabel.setBounds(500, 33, 150, 33);
	        final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	        ActionListener timerListener = new ActionListener()
	        {
	            public void actionPerformed(ActionEvent e)
	            {
	                Date date = new Date(System.currentTimeMillis());
	                String time = timeFormat.format(date);
	                timeLabel.setText(time);
	            }
	        };
	        timeLabel.setForeground(Color.WHITE);
			
	        Timer timer = new Timer(1000, timerListener);
	        // to make sure it doesn't wait one second at the start
	        timer.setInitialDelay(0);
	        timer.start();
	    //time code end
	        
		l1=new JLabel();
		l1.setBounds(170, 183, 300, 250);
		l1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		l1.setBackground(Color.BLUE);
		
		amtLabel=new JLabel("             ENTER PIN");
		amtLabel.setBounds(30, 30, 250, 33);
		amtLabel.setFont(new Font("Serif",Font.BOLD,20));
		amtLabel.setForeground(Color.WHITE);
		
		amtText=new JTextField();
		amtText.setBounds(46,80,211,33);
		
		confirm=new JButton("SUBMIT");
		confirm.setBounds(46,130,211,33);
		
		cancel=new JButton("CANCEL");
		cancel.setBounds(46,180,211,33);
		
		l1.add(amtLabel);
		l1.add(amtText);
		l1.add(confirm);
		l1.add(cancel);
	
		
		p1.add(title);
		p1.add(l1);
		p1.add(timeLabel);
		
		this.add(p1);
	}
	
	public void pinChange(long card_no,int opin)
	{
		try
		{
			 int cnt=0,tmp=0;
			
			 Class.forName("com.mysql.jdbc.Driver");  
		     System.out.println("Driver OK");
		     con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
		     System.out.println("Connection OK");
			
		     	
		     
				
			pin2=Integer.parseInt(amtText.getText());
			tmp=pin2;
			while(pin2!=0)
			{
					pin2=pin2/10;
					cnt++;
			}
			
			if(cnt<4 || cnt>4)
			{
				JOptionPane.showMessageDialog(this, "PLEASE ENTER VALID PIN", "ALERT", 1);
			}
			else
			{
				pin=Integer.parseInt(JOptionPane.showInputDialog("CONFIRM PIN"));
				
				
				System.out.println("opin-"+opin);
				System.out.println("pin2-"+pin2);
				System.out.println("pin-"+pin);
				
				if(pin==tmp || pin!=opin)
				{
					String sql="UPDATE atm_card SET pin=? WHERE card_no=?";
					
					pstat=con.prepareStatement(sql);
					pstat.setInt(1, pin);
					pstat.setLong(2, card_no);
					
					
					pstat.executeUpdate();
					
					JOptionPane.showMessageDialog(this, "PIN Changed", "ALERT", 1);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Change Pin-" +e);
		}
		
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==confirm)
		{
			try
			{
				pinChange(card_no,opin);
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
				
		}
		if(ae.getSource()==cancel)
		{
			this.setVisible(false);
		}
	}
	
}
