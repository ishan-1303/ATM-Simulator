package ATM;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;

public class Deposit extends JFrame implements ActionListener
{
	long card_no,acc_no;
	private int  k2=2000,h5=500,h1=100,dba1=0,dba2=0,dba3=0,dbamount=0, amtAcc, amtAcc2,amtAccOld, amtAcc2Old;
	private String ifsc,ifsc2;
	JLabel l1,amtLabel,title;
	JPanel p1;
	JTextField amtText;
	JButton depositbtn,cancel;
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	
	Deposit(long card_no)
	{
		setTitle("Deposit Money");
		setSize(639,639);
		setResizable(false);
		setLayout(null);
		
		this.card_no=card_no;
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		try
		{
			dbConn();		//connect to database
			
		}
		catch(Exception e)
		{
			System.out.println("init "+e);
		}
		init();
		
		depositbtn.addActionListener(this);
		cancel.addActionListener(this);
	}
	
	
	private void init()			//to initialize components
	{
		p1=new JPanel();
		p1.setBounds(0, 0, this.getWidth(), this.getHeight());
		p1.setLayout(null);
		p1.setBackground(Color.BLUE);
		
		title=new JLabel("DEPOSIT MONEY");
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
		
		amtLabel=new JLabel("ENTER ACCOUNT NUMBER OF RECEIVER");
		amtLabel.setBounds(30, 30, 250, 33);
		amtLabel.setFont(new Font("Serif",Font.BOLD,13));
		amtLabel.setForeground(Color.WHITE);
		
		amtText=new JTextField();
		amtText.setBounds(46,80,211,33);
		
		depositbtn=new JButton("DEPOSIT AMOUNT");
		depositbtn.setBounds(46,130,211,33);
		
		cancel=new JButton("CANCEL");
		cancel.setBounds(46,180,211,33);
		
		l1.add(amtLabel);
		l1.add(amtText);
		l1.add(depositbtn);
		l1.add(cancel);
	
		
		p1.add(title);
		p1.add(l1);
		p1.add(timeLabel);
		
		this.add(p1);
	}
	
	public void dbConn()			//connect to database 
	{
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT * FROM atm WHERE ATM_id="+10001;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);
	        while(rs.next())				//retrieving current details of ATM
	        {
	        	dbamount=rs.getInt(2);
	        	dba1=rs.getInt(3);
	        	dba2=rs.getInt(4);
	        	dba3=rs.getInt(5);
	        }
	     
	        System.out.println("ATM- "+dbamount);
	     
		}
		catch(Exception e)
		{
			System.out.println("dbconn "+e);
		}
	}
	
	public void connBank(long card_no,long acc_no2)
	{
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT acc_no,ifsc FROM atm_card WHERE card_no="+card_no;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);	//retrieving current details of ATM
	        while(rs.next())
	        {
	        	acc_no=rs.getLong(1);
	        	ifsc=rs.getString(2);
	        }
	       // System.out.println("ATM- "+dbamount);
	        System.out.println("accno- "+acc_no);
	        System.out.println("Bank- "+ifsc);
	        
	        sql="SELECT ifsc FROM atm_card WHERE acc_no="+acc_no2;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        rs=stat.executeQuery(sql);	//retrieving current details of ATM
	        while(rs.next())
	        {
	        	//acc_no=rs.getLong(1);
	        	ifsc2=rs.getString(1);
	        }
	       // System.out.println("ATM- "+dbamount);
	        System.out.println("accno2- "+acc_no2);
	        System.out.println("Bank2- "+ifsc2);
	        
	        
	        String sql1="SELECT balance FROM "+ifsc+" WHERE acc_no="+acc_no;
	        stat=con.prepareStatement(sql1);
	     //   pstat.setString(1,ifsc);
	      //  pstat.setLong(1,acc_no);
	        
	        rs=stat.executeQuery(sql1);
	        while(rs.next())
	        {
	        	amtAcc=rs.getInt(1);
	        }
	        System.out.println("account balance1- "+amtAcc);
	        
	         
	        String sql2="SELECT balance FROM "+ifsc2+" WHERE acc_no="+acc_no2;
	        stat=con.prepareStatement(sql1);
	     //   pstat.setString(1,ifsc);
	      //  pstat.setLong(1,acc_no);
	        
	        rs=stat.executeQuery(sql2);
	        while(rs.next())
	        {
	        	amtAcc2=rs.getInt(1);
	        }
	        System.out.println("account balance2- "+amtAcc2);
		}
		catch(Exception e)
		{
			System.out.println("dbconn-bank "+e);
		}
	}
	
	private void deposit1(int amt,long acc_no2) throws SQLException	//Logic behind deposit money
	{
		
		int amt1=amt;
		int amt2=amt;
		System.out.println("amt1="+amt1);
		
		if(amt1<100)			//to check if user has entered amount more than 100
		{
			JOptionPane.showMessageDialog(this, "PLEASE ENTER AMOUNT>100", "ALERT", 1);
		}		
		else
		{							
			int a1=amt1/k2;			//to calculate count of 2000 rs notes deposited by user
				amt1=amt1-(k2*a1);
			
			int a2=amt1/h5;			//to calculate count of 500 rs notes deposited by user
				amt1=amt1-(h5*a2);
			
			int a3=amt1/h1;			//to calculate count of 100 rs notes deposited by user
				amt1=amt1-(h1*a3);
			
			if(amt1!=0)				//to check if user has entered valid amount(i.e. not 150,230 etc)
			{
				JOptionPane.showMessageDialog(this, "PLEASE ENTER VALID AMOUNT", "ALERT", 1);
			}
			else					//Asking user to put in CASH and proceed with transaction
			{
				//JOptionPane.showMessageDialog(this, "PLEASE PUT IN THE CASH", "ALERT", 1);
			
				success(a1,a2,a3,amt2,acc_no2);	
			}
		}
		
	}
	
	private void deposit2(int amt,long acc_no2) throws SQLException	//Logic behind deposit money
	{
		
		int amt1=amt;
		int amt2=amt;
		System.out.println("amt1="+amt1);
		
		
		
			success2(amt1,acc_no2);
		
		
	}
	
private void success(int a1,int a2,int a3,int amt2,long acc_no2) 	//	to proceed transaction
	{
		amtAcc2Old=amtAcc2;
		amtAcc2=amtAcc2+amt2;
		
		System.out.println("new bal2-"+amtAcc2);
		
		dbamount=dbamount+amt2;	//updating amount in ATM machine
		dba1=dba1+a1;			//updating count of 2000 rs notes in ATM
		dba2=dba2+a2;			//updating count of 500 rs notes in ATM
		dba3=dba3+a3;			//updating count of 100 rs notes in ATM
		System.out.println(dbamount);
		if(dbamount<=999999)
		{
			try 					//updating ATM details
			{
				Long a=null,tmp=(long) 0;
				
				String temp="SELECT acc_no FROM atm_card WHERE card_no="+card_no;
				ResultSet rs=stat.executeQuery(temp);
				if(rs.next())
				{
					a=rs.getLong(1);
				}
			
				
				
				String sql="UPDATE atm SET amount=?, n2000=?, n500=?, n100=? WHERE atm_id="+10001;
				
				pstat=con.prepareStatement(sql);
				pstat.setInt(1, dbamount);
				pstat.setInt(2, dba1);
				pstat.setInt(3, dba2);
				pstat.setInt(4, dba3);
				
				pstat.executeUpdate();
				
				
				
				String sql3="UPDATE "+ifsc2+" SET balance=? WHERE acc_no=?";
				pstat=con.prepareStatement(sql3);
				//pstat.setString(1, ifsc);
				pstat.setInt(1, amtAcc2);
				pstat.setLong(2, acc_no2);
				
				pstat.executeUpdate();
				
				
				
				
				
				java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
				
				String sql2="INSERT INTO transaction (card_no,ifsc,paid_to,amount,date,old_balance_s,new_balance_s,old_balance_r,new_balance_r) VALUES(?,?,?,?,?,?,?,?,?)";
				pstat=con.prepareStatement(sql2);
				pstat.setLong(1, card_no);
				pstat.setString(2, ifsc);
				pstat.setLong(3, acc_no2);
				pstat.setInt(4, amt2);
				pstat.setTimestamp(5, sqlDate);
				pstat.setInt(6, amtAccOld);
				pstat.setInt(7, amtAccOld);
				pstat.setInt(8, amtAcc2Old);
				pstat.setInt(9, amtAcc2);
				
				pstat.executeUpdate();
				
				String msg="TRANSACTION SUCCESSFUL\n"
						+ "AMOUNT DEPOSITED=" +amt2
						+ "\n2000 RS NOTES="+ a1
						+ "\n500 RS NOTES="+ a2
						+ "\n100 RS NOTES="+ a3;
				JOptionPane.showMessageDialog(this, msg, "ALERT", 1);
				
				this.setVisible(false);
				new MiniStatement(a).setVisible(true);
			}
			
			catch(Exception e)
			{
				System.out.println("Success- "+e);
			}
			
				//Displaying details of amount deposited
		
		}	//con.close();
		else
		{
			JOptionPane.showMessageDialog(this, "ATM IS FULL", "ALERT", 1);
		}
		
		
	}
	
	private void success2(int amt2,long acc_no2)
	{
		amtAccOld=amtAcc;
		amtAcc=amtAcc-amt2;
		
		amtAcc2Old=amtAcc2;
		amtAcc2=amtAcc2+amt2;
		
		System.out.println("new bal1-"+amtAcc);
		System.out.println("new bal2-"+amtAcc2);
		try
		{
			Long a=null,tmp=(long) 0;
			int bal=0;
			String temp="SELECT acc_no FROM atm_card WHERE card_no="+card_no;
			ResultSet rs=stat.executeQuery(temp);
			if(rs.next())
			{
				a=rs.getLong(1);
			}
			String balSql="SELECT balance FROM " +ifsc+" WHERE acc_no=?";
			pstat=con.prepareStatement(balSql);
			pstat.setLong(1, a);
			ResultSet rs1 =pstat.executeQuery();
			if(rs1.next())
			{
				bal=rs1.getInt(1);
			}
			
			
			
			if(bal<=0)
			{
				JOptionPane.showMessageDialog(this, "INSUFFICIENT BALANCE", "ALERT", 1);
				this.setVisible(false);
			}
			else {
			String sql1="UPDATE "+ifsc+" SET balance=? WHERE acc_no=?";
			pstat=con.prepareStatement(sql1);
			//pstat.setString(1, ifsc);
			pstat.setInt(1, amtAcc);
			pstat.setLong(2, acc_no);
			
			pstat.executeUpdate();
			
			String sql3="UPDATE "+ifsc2+" SET balance=? WHERE acc_no=?";
			pstat=con.prepareStatement(sql3);
			//pstat.setString(1, ifsc);
			pstat.setInt(1, amtAcc2);
			pstat.setLong(2, acc_no2);
			
			pstat.executeUpdate();
			
			
			
			
			
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
			
			
			String sql2="INSERT INTO transaction (card_no,acc_no,ifsc,paid_to,amount,date,old_balance_s,new_balance_s,old_balance_r,new_balance_r) VALUES(?,?,?,?,?,?,?,?,?,?)";
			pstat=con.prepareStatement(sql2);
			pstat.setLong(1, card_no);
			pstat.setLong(2, a);
			pstat.setString(3, ifsc);
			pstat.setLong(4, acc_no2);
			pstat.setInt(5, amt2);
			pstat.setTimestamp(6, sqlDate);
			pstat.setLong(7, amtAccOld);
			pstat.setLong(8, amtAcc);
			pstat.setLong(9, amtAcc2Old);
			pstat.setLong(10, amtAcc2);
			
			pstat.executeUpdate();
			
			String msg="TRANSACTION SUCCESSFUL";
			JOptionPane.showMessageDialog(this, msg, "ALERT", 1);
			
			
			this.setVisible(false);
			new MiniStatement(a).setVisible(true);
			}
		}
		catch(Exception e)
		{
			System.out.println("Success2-"+e);
		}
		
		
	}
	
public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource()==depositbtn)
		{
			long acc_no2=Long.parseLong(amtText.getText().toString());
			try
			{
				if(dbamount==999999)		//to check if user has entered more amount than ATM capacity 
				{
					JOptionPane.showMessageDialog(this, "ATM IS FULL", "ALERT", 1);
				}
				else 
				{
					
					
					String[] options = { "BY CASH", "FROM ACCOUNT" ,"CANCEL" };
					int rc = JOptionPane.showOptionDialog(null, "Question ?", "Confirmation",
					        JOptionPane.WARNING_MESSAGE, 0, null, options, options[2]);
					
					String temp=JOptionPane.showInputDialog("ENTER AMOUNT");
					int amt=Integer.parseInt(temp);
					
					if(rc==0)
					{
						connBank(card_no,acc_no2);
						deposit1(amt,acc_no2);
					}
					else if(rc==1)
					{
						connBank(card_no,acc_no2);
						deposit2(amt,acc_no2);
					}
					else
					{
						
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("CLick "+e);
			}
		}
		if(ae.getSource()==cancel)
		{
			this.setVisible(false);
		}
	}
	
	
}
