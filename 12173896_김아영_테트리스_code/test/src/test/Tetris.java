package test;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



class Keylistener1 implements KeyListener{
	static public Keylistener1 instance = new Keylistener1();	

	int[] k = new int[100];	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		k[code]=1;
	}	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		k[code] = 0;
	}
	@Override
	public void keyTyped(KeyEvent arg0) {			
	}
	public boolean isKeyDown(int e) {
		if (k[e] == 1) {
			k[e] = 2;
			return true;
		}
		return false;
	}
}


class Point{	
	int [][]blocks;	
	int num,x,y,score;
	
	Point(){
		Random random = new Random();
		num = random.nextInt(7);
		x = 0; 
		y = 0;
		score=0;
		if(num==0) {
			blocks = new int[][] {{1,1,2},{1,1,2},{2,2,2}};
		}
		else if(num==1) {
			blocks = new int[][] {{1,1,1},{2,2,2},{2,2,2}};
		}
		else if(num==2) {
			blocks = new int[][] {{2,1,2},{1,1,1},{2,2,2}};
		}
		else if(num==3) {
			blocks = new int[][] {{1,2,2},{1,1,1},{2,2,2}};
		}
		else if(num==4) {
			blocks = new int[][] {{2,2,1},{1,1,1},{2,2,2}};
		}
		else if(num==5) {
			blocks = new int[][] {{2,1,1},{1,1,2},{2,2,2}};
		}
		else if(num==6) {
			blocks = new int[][] {{1,1,2},{2,1,1},{2,2,2}};
		}
	}
	
	
	void cycle(int a) {
		int [][]realshape = new int[3][3];
		int [][]temp = blocks;
		if(num == 0 || num == 1 || num == 2 ||num == 3 ||num == 4 || num == 5 || num == 6) {	

			if( a > 0) {
				for(int i=0 ;i<3; i++) {
					for(int j=0; j<3; j++) {
						realshape[j][2-i] = temp[i][j];
					}
				}
			}
		}		
		
		int l = 1;
		
		for(int i=0; i<realshape.length; i++) {
			for(int j=0; j<realshape[0].length; j++) {
				if(Boardtetris.panel[y+i][x+j] == 1 && realshape[i][j] == 1) {
					l = 0;
				}
			}
		}
		
		if(l == 1) {
			blocks = realshape;
		}
	}
	
}


class Boardtetris extends JPanel{	
	static int panel[][] = new int[20][10];
	int r = 25;
	int t;
	Point p = new Point();
	
	Boardtetris(){
		this.setLayout(null);	
	}	
		
	void shapeofb(Graphics g) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++)  {
				if(p.blocks[i][j] == 1) {
					g.fillRect((p.x+j)*r+100,(p.y+i)*r+100,r,r);
					
				}
			}
		}
	}
		
	protected void paintComponent(Graphics g) {
		super.paintComponent(g); 			
		try {
			Thread.sleep(15);
		}catch(Exception e) {
			e.printStackTrace();
		}
		update(g);
		repaint();
		g.drawRect(100,100,r*10,r*20);
		g.setColor(Color.black);
		for(int i=1; i<10; i++) {
			g.drawLine(i*r+100,100,i*r+100,100+r*20);
		}
		for(int i=1; i<20; i++) {
			g.drawLine(100,i*r+100,100+r*10,i*r+100);
		}
	}
		
	void clear() {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(p.blocks[i][j] == 1) {
					panel[p.y+i][p.x+j] = 1;
				}
			}
		}
		p = null;
		p = new Point();
		int sum=0;	
		for(int i=0; i<20; i++) {
			int count = 0;
			for(int j=0; j<10; j++) {
				if(panel[i][j] == 1) {
					count++;
				}
			}
			if(count == 10) {
				int [][]tmp = panel;
				panel = new int[20][10];
				int n = 19;
				for(int ch=19; ch>=0; ch--) {
					if(ch != i) {
						panel[n] = tmp[ch];
						n--;
					}
					p.score++;
				}	
				System.out.println("점수 획득 ");
				sum=sum+p.score;
				System.out.println(sum);
			}
		}
	}
	
	int show(int y,int x) {
		
		if(x > 9 || x < 0 || y > 20 || y < 0) {
			return -1;
		}else {
			return panel[y][x];
		}
	}

	int moveing(int n) {
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(p.blocks[i][j] == 1) {				
					if(n == 0 &&(show(p.y+i,p.x+j-1) == 1 || p.x+j < 1)) {
						return 0;
					
					}else if(n == 1 &&(show(p.y+i,p.x+j+1) == 1 || p.x+j > 8)) {
						return 0;
					}
				}
			}
		}
		return 1;
	}
	
	int fall() {
		p.y++;
		for(int i=0; i<3; i++) {
			for(int j=0; j<3; j++) {
				if(p.blocks[i][j] == 1) {					
					if(p.y+i == 19) {
						clear();
						return 0;
					}else if(show(p.y+i+1,p.x+j) == 1){
						clear();
						return 0;
					}
				}
			}
		}
		return 1;
	}
	
	public void update(Graphics g) {
		shapeofb(g);
		move();
		if(t > 40) {
			fall();
			t = 0;
		}
		t++;
		for(int i=0; i<20; i++) {
			for(int j=0; j<10; j++) {
				if(show(i,j) == 1) {
					g.fillRect(j*r+100,i*r+100,r,r);
				}
			}
		}
	}
	
	void move() {
		if(Keylistener1.instance.isKeyDown(KeyEvent.VK_LEFT)) {
			if(moveing(0)==1) {
				p.x--;
			}
		}
		else if(Keylistener1.instance.isKeyDown(KeyEvent.VK_RIGHT)) {
			if(moveing(1)==1) {
				p.x++;
			}
		}
		if(Keylistener1.instance.isKeyDown(KeyEvent.VK_UP)) {
			p.cycle(1);
		}
		if(Keylistener1.instance.isKeyDown(KeyEvent.VK_DOWN)) {
			while(fall()==1);
		}
	}		
}


public class Tetris extends JFrame implements ActionListener {
	
	Tetris(){
		setTitle("javahomework");
		makeMenu();
		setSize(500, 1000);
		addKeyListener(Keylistener1.instance);
		Boardtetris bts = new Boardtetris();	
		bts.setBackground(Color.white);
		add(bts);
		revalidate();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void makeMenu() {
		JMenuItem item;
		KeyStroke key;	
		JMenuBar mb=new JMenuBar();
		JMenu m1=new JMenu("저장하기");
		m1.setMnemonic(KeyEvent.VK_F);
		JMenu m2=new JMenu("불러오기");
		m2.setMnemonic(KeyEvent.VK_C);				
		item=new JMenuItem("새로만들기",KeyEvent.VK_N);
		item.addActionListener(this);
		m1.add(item);
		item=new JMenuItem("저장",KeyEvent.VK_S);
		item.addActionListener(this);
		m1.add(item);
		item=new JMenuItem("다른 이름으로 저장",KeyEvent.VK_A);
		item.addActionListener(this);
		m1.add(item);
		m1.addSeparator();
		m1.add(new JMenuItem("종료"));		
		m1.add(item);
		mb.add(m1);
		mb.add(m2);

		setJMenuBar(mb);
	}
	public static void main(String[] args) {
		new Tetris();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem)(e.getSource());		
		switch (mi.getText()) {
		case "새로만들기":
			System.out.println("새 파일을 눌렀습니다.");
			break;
		case "저장":
			System.out.println("저장을 눌렀습니다.");
			break;
		case "다른 이름으로 저장":
			System.out.println("다른 이름으로 저장을 눌렀습니다.");
			break;
		}
	}
}