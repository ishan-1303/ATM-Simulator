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

public class Home extends JFrame implements ActionListener
{
	long card_no=0;
	int pin=0;
	JPanel p1,p2;
	JLabel l1,l2,title,menuLabel;
	JButton depositbtn,withdrawbtn,changePin,miniStatement,checkBalance,f1,f2,f3;
	JButton[] menu=new JButton[8];
	ArrayList<String> menuItems = new ArrayList<String>();
	Connection con;
	Statement stat;
	PreparedStatement pstat;
	
	
	Home(long card_no,int pin)
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
		
		System.out.println("Home-"+card_no);
		
		withdrawbtn.addActionListener(this);
		depositbtn.addActionListener(this);
		miniStatement.addActionListener(this);
		changePin.addActionListener(this);
		checkBalance.addActionListener(this);
		this.card_no=card_no;
		this.pin=pin;
	}
	
	private void init()			//to initialize all the components
	{
		p1=new JPanel();
		p1.setBounds(0, 0, getSize().width, 100);
		p1.setBackground(Color.BLUE);
		p1.setLayout(null);
		
		l1=new JLabel();
		l1.setBounds(0,0,633,p1.getHeight());
		l1.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		l1.setLayout(null);
		
		title=new JLabel("ATM Simulator");
		title.setBounds(211, 33, 211, 33);
		title.setFont(new Font("Serif",Font.BOLD,30));
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
		l1.add(title);
		l1.add(timeLabel);
		
		p2=new JPanel();
		p2.setBounds(0,100,getSize().width,539);
		p2.setBackground(Color.BLUE);
		p2.setLayout(null);
		
		l2=new JLabel();
		l2.setBounds(0,0,633,510);
		l2.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		l2.setLayout(null);
		
		
		
		withdrawbtn=new JButton(">");
		withdrawbtn.setBounds(10, 125, 130, 50);
		withdrawbtn.setFont(new Font("Serif",Font.BOLD,30));
		
		
		checkBalance=new JButton(">");
		checkBalance.setBounds(10, 205, 130, 50);
		checkBalance.setFont(new Font("Serif",Font.BOLD,30));
		
		changePin=new JButton(">");
		changePin.setBounds(10, 285, 130, 50);
		changePin.setFont(new Font("Serif",Font.BOLD,30));
		
		f3=new JButton(">");
		f3.setBounds(10, 365, 130, 50);
		f3.setFont(new Font("Serif",Font.BOLD,30));
		
		
		depositbtn=new JButton("<");
		depositbtn.setBounds(480, 125, 130, 50);
		depositbtn.setFont(new Font("Serif",Font.BOLD,30));
		
		miniStatement=new JButton("<");
		miniStatement.setBounds(480, 205, 130, 50);
		miniStatement.setFont(new Font("Serif",Font.BOLD,30));
		
		f1=new JButton("<");
		f1.setBounds(480, 285, 130, 50);
		f1.setFont(new Font("Serif",Font.BOLD,30));
		
		
		f2=new JButton("<");
		f2.setBounds(480, 365, 130, 50);
		f2.setFont(new Font("Serif",Font.BOLD,30));
		
		
		
		
		
		int m=100;
		int n=60;
		menuLabel=new JLabel();
		menuLabel.setBounds((getSize().width/3)-n,(l2.getSize().height/3)-n,(getSize().width/3)+m,(getSize().height/3)+m);
		menuLabel.setLayout(new GridLayout(4, 2));
		//menuLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		menuItems.add("WITHDRAW");
		menuItems.add("DEPOSIT");
		menuItems.add("CHECK BALANCE");
		menuItems.add("MINI STATEMENT");
		menuItems.add("CHANGE PIN");
		menuItems.add("NA");
		menuItems.add("NA");
		menuItems.add("NA");
		
		for(int i=0;i<8;i++)
		{
			menu[i]=new JButton(menuItems.get(i).toString());
			menu[i].setFont(new Font("Serif",Font.BOLD,12));
			menu[i].setForeground(Color.WHITE);
			menu[i].setBackground(Color.BLUE);
			//menu[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
			menu[i].setEnabled(false);
			
			menuLabel.add(menu[i]);
		
		}
		
		l2.add(menuLabel);
		l2.add(depositbtn);
		l2.add(withdrawbtn);
		l2.add(checkBalance);
		l2.add(miniStatement);
		l2.add(changePin);
		l2.add(f1);
		l2.add(f2);
		l2.add(f3);
		
		p1.add(l1);
		
		p2.add(l2);
		
		this.add(p1);
		this.add(p2);
	}
	
	public void checkBal(long c_no)
	{
		try
		{
			String ifsc="";
			long a=0;
			int bal=0;
			Class.forName("com.mysql.jdbc.Driver");  
	        System.out.println("Driver OK");
	        con=DriverManager.getConnection("jdbc:mysql://localhost:3307/ATMDB","root","");
	        System.out.println("Connection OK");
	        
	        String sql="SELECT ifsc FROM atm_card WHERE card_no="+c_no;
	        stat=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	     
	        ResultSet rs=stat.executeQuery(sql);
	        while(rs.next())				//retrieving current details of ATM
	        {
	        	ifsc=rs.getString(1);
	        }
	        
	        String temp="SELECT acc_no FROM atm_card WHERE card_no="+card_no;
			 rs=stat.executeQuery(temp);
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
			JOptionPane.showMessageDialog(this, "Account Balance: "+bal, "ALERT", 1);
	     
		}
		catch(Exception e)
		{
			System.out.println("checkbal-"+e);
		}
	}
	
	public void actionPerformed(ActionEvent ae)	
	{
		if(ae.getSource()==withdrawbtn)		//to withdraw window
		{
			new Withdraw(card_no).setVisible(true);
		}
		if(ae.getSource()==depositbtn)		//to deposit window
		{
			new Deposit(card_no).setVisible(true);
		}
		if(ae.getSource()==miniStatement)		//to deposit window
		{
			try
			{
				
				new MiniStatement(1,card_no).setVisible(true);
			}
		
			catch(Exception e)
			{
				System.out.println("Home "+e);
			}
		}
		if(ae.getSource()==changePin)
		{
			new PINChange(card_no,pin).setVisible(true);
		}
		
		if(ae.getSource()==checkBalance)
		{
			checkBal(card_no);
		}
	}
	
}
