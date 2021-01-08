package ATM;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.math.BigInteger;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.*;

public class MiniStatement extends JFrame implements ActionListener
{
	long acc_no,card_no;
	JLabel title;
	JTextArea st;
	JPanel p1;
	JButton ok;
	
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	
	MiniStatement(long acc_no)
	{
		setLayout(null);
		setSize(739,639);
		setResizable(false);
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		init();
		
		this.acc_no=acc_no;
		this.card_no=card_no;
		
		
		dbConn();
		ok.addActionListener(this);
	}
	
	MiniStatement(long acc_no,long card_no)
	{
		setLayout(null);
		setSize(739,639);
		setResizable(false);
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		init();
		
		this.acc_no=acc_no;
		this.card_no=card_no;
		
		
		dbConn2(card_no);
		ok.addActionListener(this);
	}
	
	
	
	private void init()
	{
		p1=new JPanel();
		p1.setBounds(0,0,getSize().width,getSize().height);
		p1.setLayout(null);
		p1.setBackground(Color.WHITE);
		
		title=new JLabel("MINI STATEMENT");
		title.setFont(new Font("Serif",Font.BOLD,20));
		title.setForeground(Color.GRAY);
		title.setBounds(266, 20, 213, 30);
		
		st=new JTextArea("    Transcation_ID     Account_no     ifsc(Debitors Bank)    Transcation   Paid_To/From    Amount   Balance   Date\n");
		st.setEditable(false);
		st.setBounds(0, 120, getSize().width, 300);
		
		ok=new JButton("OK");
		ok.setBounds(246,500,213,30);
		
		p1.add(title);
		p1.add(st);
		p1.add(ok);
		
		this.add(p1);
		

	}
	
	public void dbConn2(long card_no)
	{
		long tid,acc_no2,paid_to;
        int amount,balance;
        String ifsc;
        java.sql.Timestamp sqlDate;
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT acc_no FROM atm_card WHERE card_no="+card_no;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);
	        while(rs.next())				//retrieving current details of ATM
	        {
	        	acc_no=rs.getLong(1);
	        }
	        
	         sql="SELECT transaction_id,acc_no,ifsc,paid_to,amount,date,new_balance_s,new_balance_r FROM transaction WHERE acc_no=? OR paid_to=? ORDER BY transaction_id DESC LIMIT 5";
	        pstat=con.prepareStatement(sql);
	        pstat.setLong(1, acc_no);
	        pstat.setLong(2, acc_no);
	        
	        
	     
	         rs=pstat.executeQuery();	
	        while(rs.next())
	        {
	        	long paid_to2=1,tmp=0;
	        //	String str=rs.getInt(1);
	        	tid=rs.getLong(1);
	        	acc_no2=rs.getLong(2);
	        	ifsc=rs.getString(3);
	        	paid_to=rs.getLong(4);
	        	amount=rs.getInt(5);
	        	sqlDate=rs.getTimestamp(6);
	        	
	        	tmp=paid_to;
	        	String trtype;
	        	if(acc_no==paid_to)
	        	{
	        		balance=rs.getInt(8);
	        		trtype="CREDIT";
	        		paid_to=acc_no2;
		        	acc_no2=acc_no;
		        	//System.out.println("pd to "+paid_to);
	        	}
	        	
	        	else
	        	{
	        		
	        		balance=rs.getInt(7);
	        		trtype="DEBIT";
	        		int count=0;
	        		while(paid_to != 0)
	                {
	                    // num = num/10
	                    
	        			paid_to = paid_to/10;
	                    ++count;
	                }
	        		//System.out.println("pd to "+paid_to);
	        		System.out.println(count);;
	        		if(count==16)
	        		{
	        			paid_to2=0;
	        		}
	        	}
	        	
	        	if(paid_to2!=0)
	        	{
	        		paid_to2=tmp;
	        	}
	        	else
	        	{
	        		paid_to2=paid_to;
	        	}
	        	System.out.println("pd to "+paid_to2);
	        	
	        	String statement="\n"+tid+"\t "+acc_no2+"\t  "+ifsc+"\t   "+trtype+" \t "+paid_to2+"\t   "+amount+"   "+balance+"   "+sqlDate+"  ";
	        	
	        	st.append(statement);
	        	
	        }
	      
		}
		catch(Exception e)
		{
			System.out.println("dbconn2(Mini) "+e);
		}
	}
	
	public void dbConn()
	{
		long tid,acc_no2,paid_to;
        int amount,balance;
        String ifsc;
        java.sql.Timestamp sqlDate;
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	       
	        
	        String sql="SELECT transaction_id,acc_no,ifsc,paid_to,amount,date,new_balance_s,new_balance_r FROM transaction WHERE acc_no=? OR paid_to=? ORDER BY transaction_id DESC LIMIT 5";
	        pstat=con.prepareStatement(sql);
	        pstat.setLong(1, acc_no);
	        pstat.setLong(2, acc_no);
	        
	        
	     
	        ResultSet rs=pstat.executeQuery();	
	        while(rs.next())
	        {
	        	long paid_to2=1,tmp=0;
	        //	String str=rs.getInt(1);
	        	tid=rs.getLong(1);
	        	acc_no2=rs.getLong(2);
	        	ifsc=rs.getString(3);
	        	paid_to=rs.getLong(4);
	        	amount=rs.getInt(5);
	        	sqlDate=rs.getTimestamp(6);
	        	
	        	tmp=paid_to;
	        	String trtype;
	        	if(acc_no==paid_to)
	        	{
	        		balance=rs.getInt(8);
	        		trtype="CREDIT";
	        		paid_to=acc_no2;
		        	acc_no2=acc_no;
		        	//System.out.println("pd to "+paid_to);
	        	}
	        	
	        	else
	        	{
	        		
	        		balance=rs.getInt(7);
	        		trtype="DEBIT";
	        		int count=0;
	        		while(paid_to != 0)
	                {
	                    // num = num/10
	                    
	        			paid_to = paid_to/10;
	                    ++count;
	                }
	        		//System.out.println("pd to "+paid_to);
	        		System.out.println(count);;
	        		if(count==16)
	        		{
	        			paid_to2=0;
	        		}
	        	}
	        	
	        	if(paid_to2!=0)
	        	{
	        		paid_to2=tmp;
	        	}
	        	else
	        	{
	        		paid_to2=paid_to;
	        	}
	        	System.out.println("pd to "+paid_to2);
	        	
	        	String statement="\n"+tid+"\t "+acc_no2+"\t  "+ifsc+"\t   "+trtype+" \t "+paid_to2+"\t   "+amount+"   "+balance+"   "+sqlDate+"  ";
	        	
	        	st.append(statement);
	        	
	        }
	      
		}
		catch(Exception e)
		{
			System.out.println("dbconn(Mini) "+e);
		}
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==ok)
		{
			this.setVisible(false);
		
		}
	}
	

}
