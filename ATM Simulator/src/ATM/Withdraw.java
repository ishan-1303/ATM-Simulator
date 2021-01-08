package ATM;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Withdraw extends JFrame implements ActionListener
{
	//private int 
	long card_no,acc_no=0,tmp=0;
	private int  k2=2000,h5=500,h1=100,dba1=0,dba2=0,dba3=0,dbamount=0, amtAcc, amtAcc2;
	private String ifsc;
	JLabel l1,amtLabel,title;
	JPanel p1;
	JTextField amtText;
	JButton withdrawbtn,cancel;
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	
	Withdraw(long card_no)
	{
		setTitle("Withdraw Money");
		setSize(639,639);
		setResizable(false);
		setLayout(null);
		
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		int x=(d.width-getSize().width)/2;
		int y=(d.height-getSize().height)/2;
		setLocation(x,y);
		
		System.out.println("Withdraw-"+card_no);
		
		try
		{
			dbConn();		//connect to database
		}
		catch(Exception e)
		{
			System.out.println("init "+e);
		}
		init();
		
		this.card_no=card_no;
		
		withdrawbtn.addActionListener(this);
		cancel.addActionListener(this);
		
	}
	
	private void init()				//to initialize components
	{
		p1=new JPanel();
		p1.setBounds(0, 0, this.getWidth(), this.getHeight());
		p1.setLayout(null);
		p1.setBackground(Color.BLUE);
		
		title=new JLabel("WITHDRAW MONEY");
		title.setBounds(211, 33, 211, 33);
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
		
		amtLabel=new JLabel("ENTER AMOUNT TO WITHDRAW");
		amtLabel.setBounds(30, 30, 250, 33);
		amtLabel.setFont(new Font("Serif",Font.BOLD,15));
		amtLabel.setForeground(Color.WHITE);
		
		amtText=new JTextField();
		amtText.setBounds(46,80,211,33);
		
		withdrawbtn=new JButton("WITHDRAW AMOUNT");
		withdrawbtn.setBounds(46,130,211,33);
		
		cancel=new JButton("CANCEL");
		cancel.setBounds(46,180,211,33);
		
		l1.add(amtLabel);
		l1.add(amtText);
		l1.add(withdrawbtn);
		l1.add(cancel);
		
		
		p1.add(title);
		p1.add(l1);
		p1.add(timeLabel);
		
		this.add(p1);
	}
	
	public void dbConn()		//connect to database
	{
		try
		{	
	        Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT * FROM atm WHERE ATM_id="+10001;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);	//retrieving current details of ATM
	        while(rs.next())
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
	
	public void connBank(long card_no)
	{
		try
		{	
	        
	        String sql="SELECT acc_no,ifsc FROM atm_card WHERE card_no="+card_no;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);	//retrieving current details of ATM
	        while(rs.next())
	        {
	        	acc_no=rs.getLong(1);
	        	ifsc=rs.getString(2);
	        }
	        System.out.println("ATM- "+dbamount);
	        System.out.println("accno- "+acc_no);
	        System.out.println("Bank- "+ifsc);
	        
	        String sql1="SELECT balance FROM "+ifsc+" WHERE acc_no="+acc_no;
	        stat=con.prepareStatement(sql1);
	     //   pstat.setString(1,ifsc);
	      //  pstat.setLong(1,acc_no);
	        
	        rs=stat.executeQuery(sql1);
	        while(rs.next())
	        {
	        	amtAcc=rs.getInt(1);
	        }
	        System.out.println("account balance- "+amtAcc);
		}
		catch(Exception e)
		{
			System.out.println("dbconn-bank "+e);
		}
	}
	
	private void withdraw(int amt) throws SQLException		//Logic for withdrawing money
	{
		connBank(card_no);
		int amt1=amt;
		int amt2=amt;
		System.out.println("amt1="+amt1);
		
		if(dbamount==0)			//to check if ATM is out of cash
		{
			JOptionPane.showMessageDialog(this, "ATM IS OUT OF CASH", "ALERT", 1);
		}
		else if(amt1<100)		//to check if amount entered by user is more than 100
		{
			JOptionPane.showMessageDialog(this, "PLEASE ENTER VALID AMOUNT", "ALERT", 1);
		}
		else if(amt1<=dbamount && amt1<=amtAcc)	//to check if ATM has enough  money to complete the transaction
		{
			int a1=amt1/k2;			//to calculate count of 2000 rs notes to be withdrawn
			if(a1<=dba1)			//to check if ATM has required count of 2000 rs notes 
			{
				amt1=amt1-(k2*a1);
			}
			else
			{
				a1=0;
			}
			
			int a2=amt1/h5;			//to calculate count of 500 rs notes to be withdrawn
			if(a2<=dba2)			//to check if ATM has required count of 500 rs notes 
			{
				amt1=amt1-(h5*a2);
			}
			else
			{
				a2=0;
			}
			
			int a3=amt1/h1;			//to calculate count of 100 rs notes to be withdrawn
			if(a3<=dba3)			//to check if ATM has required count of 100 rs notes 
			{
				amt1=amt1-(h1*a3);
			}	
			else
			{
				a3=0;
			}
					 	 
			if(amt1!=0)	//to check if user has entered valid amount(i.e. not 150,230 etc)
			{
				String msg="TRANSACTION CAN NOT BE PROCESSED"
						+ "\n1.YOU MAY HAVE ENTERED INVALID AMOUNT"
						+ "\n2.ATM MAY NOT HAVE ENOUGH REQUIRED NOTES";
				JOptionPane.showMessageDialog(this, msg, "ALERT", 1);
			}
			else				//to proceed with transaction
			{
				success(a1,a2,a3,amt2);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(this, "NOT ENOUGH MONEY", "ALERT", 1);
		}
		
	}
	
	private void success(int a1,int a2,int a3,int amt2)	//proceeding transcation
	{
		String msg="TRANSACTION SUCCESSFUL\n"
				+ "AMOUNT WITHDRAWN=" +amt2
				+ "\n2000 RS NOTES="+ a1
				+ "\n500 RS NOTES="+ a2
				+ "\n100 RS NOTES="+ a3;
		JOptionPane.showMessageDialog(this, msg, "ALERT", 1);	//displaying details of withdrawn amount
		dbamount=dbamount-amt2;		//updating amount in ATM machine
		dba1=dba1-a1;				//updating count of 2000 rs notes in ATM
		dba2=dba2-a2;				//updating count of 500 rs notes in ATM
		dba3=dba3-a3;				//updating count of 100 rs notes in ATM
		System.out.println(dbamount);
		
		try 						//updating ATM details
		{
			
			Long a=null;
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
			
			String sql="UPDATE atm SET amount=?, n2000=?, n500=?, n100=? WHERE atm_id="+10001;
			
			pstat=con.prepareStatement(sql);
			pstat.setInt(1, dbamount);
			pstat.setInt(2, dba1);
			pstat.setInt(3, dba2);
			pstat.setInt(4, dba3);
			
			pstat.executeUpdate();
			
			amtAcc2=amtAcc;
			amtAcc=amtAcc-amt2;
			
			String sql1="UPDATE "+ifsc+" SET balance=? WHERE acc_no=?";
			pstat=con.prepareStatement(sql1);
			//pstat.setString(1, ifsc);
			pstat.setInt(1, amtAcc);
			pstat.setLong(2, acc_no);
			
			pstat.executeUpdate();
			
			
			java.sql.Timestamp sqlDate = new java.sql.Timestamp(new java.util.Date().getTime());
			
			
			String sql2="INSERT INTO transaction (card_no,acc_no,ifsc,paid_to,amount,date,old_balance_s,new_balance_s,old_balance_r,new_balance_r) VALUES(?,?,?,?,?,?,?,?,0,0)";
			pstat=con.prepareStatement(sql2);
			pstat.setLong(1, card_no);
			pstat.setLong(2, a);
			pstat.setString(3, ifsc);
			pstat.setLong(4, card_no);
			pstat.setInt(5, amt2);
			pstat.setTimestamp(6, sqlDate);
			pstat.setLong(7, amtAcc2);
			pstat.setLong(8, amtAcc);
			
			pstat.executeUpdate();
			
			this.setVisible(false);
			new MiniStatement(a).setVisible(true);
			
			//			con.close();
		}
		}
		catch(Exception e)
		{
			System.out.println("Success "+e);
		}
		
	}
	
	public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource()==withdrawbtn)
		{
			int amt=Integer.parseInt(amtText.getText().toString());
			try
			{
				withdraw(amt);
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
