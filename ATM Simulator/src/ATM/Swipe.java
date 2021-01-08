package ATM;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class Swipe extends JFrame implements ActionListener
{
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	JLabel latm,title;
	JPanel p1;
	JTextField tatm;
	//JPasswordField ipin;
	JButton next;
	Swipe()
	{
		super("ATM Simlulator");
		setLayout(null);
		setSize(639,639);
		setResizable(false);
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		init();
		
		next.addActionListener(this);
	}
	
	private void init()
	{	
		p1=new JPanel();
		p1.setBounds(0, 0, this.getWidth(), this.getHeight());
		p1.setBackground(Color.BLUE);
		p1.setLayout(null);
		
		title=new JLabel("ATM SIMULATOR");
		title.setBounds(213,63,213,30);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setForeground(Color.white);
		title.setFont(new Font("Serif",Font.BOLD,20));
		
		latm=new JLabel("ENTER ATM CARD NUMBER");
		latm.setBounds(213,213,213,30);
		latm.setHorizontalAlignment(SwingConstants.CENTER);
		latm.setVerticalAlignment(SwingConstants.CENTER);
		latm.setForeground(Color.white);
		
		tatm=new JTextField();
		tatm.setBounds(213,263,213,30);
		
		next=new JButton("NEXT");
		next.setBounds(213, 313, 213, 30);
		
		p1.add(latm);
		p1.add(tatm);
		p1.add(next);
		p1.add(title);
		
		this.add(p1);
		
	}
	
	public void dbConn(long card_no)			//connect to database 
	{
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT * FROM atm_card WHERE card_no="+card_no;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        int i=0;
	        ResultSet rs=stat.executeQuery(sql);
	        while(rs.next())
	        {
	        	i++;
	        }
	        int pin=0;
	        if(i>0)
	        {
	        	pin=Integer.parseInt(JOptionPane.showInputDialog("ENTER PIN"));
	        }
	        
	        sql="SELECT * FROM atm_card WHERE card_no=? AND pin=?";
	        pstat=con.prepareStatement(sql);
			pstat.setLong(1, card_no);
			pstat.setInt(2, pin);
			
	        i=0;
	        rs=pstat.executeQuery();
	        while(rs.next())
	        {
	        	i++;
	        }
	        
	        if(i>0)
	        {
	        	JOptionPane.showMessageDialog(this,"AUTHENTICATION SUCCESS","ALERT",1);
	        	new Home(card_no,pin).setVisible(true);
	        }
	        else
	        {
	        	JOptionPane.showMessageDialog(this,"AUTHENTICATION FAILURE","ALERT",1);
	        }
	        
	       // System.out.println(dbamount);
		}
		catch(Exception e)
		{
			System.out.println("dbconn "+e);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		long card_no;
		if(ae.getSource()==next)
		{
			int length=tatm.getText().toString().length();
			if(length==16)
			{
				card_no=Long.parseLong(tatm.getText().toString());
				dbConn(card_no);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "ATM CARD IS NOT VALID", "ALERT", 1);
			}
		}
	}
	
	public static void main(String[] args) 
	{
		Swipe s=new Swipe();
		s.setVisible(true);

	}
	
}
