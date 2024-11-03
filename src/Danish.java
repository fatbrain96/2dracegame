import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.random.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Danish extends JPanel implements ActionListener, KeyListener{

    int bwidth = 1920;
    int bheight = 1080;

    Image backImg;
    Image CarImg;
    Image RoadImg;
    Image Bushes;
    Image[] obstaclImages = new Image[3];
    Image banImg;
    Image BhaiImg;
    Image one;
    Image two;
    Image back;


    //--------------------------------------------------------------------------------------------------------
    // Music For Danish Bhai

    public Clip play1;
    public Clip play2;

    public void playBack(){
        try {
            AudioInputStream audioInputStream =  AudioSystem.getAudioInputStream(new File(getClass().getResource("./background.wav").toURI()));
            play1 = AudioSystem.getClip();
            play1.open(audioInputStream);
            play1.loop(Clip.LOOP_CONTINUOUSLY);
            play1.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void playOver(){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(getClass().getResource("./Over.wav").toURI()));
            play2 = AudioSystem.getClip();
            play2.open(audioInputStream);
            play2.loop(Clip.LOOP_CONTINUOUSLY);
            play2.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException | URISyntaxException e) {
            e.printStackTrace();
        }
    }





    // back road for covering up the fault 

    int banx = bwidth/6;
    int bany = 0;
    int banheight = 700;
    int banwidth = 700;
    
    class Ban{
        int x = banx;
        int y = bany;

        int height= banheight;
        int width = banwidth;

        Image img;

        Ban(Image img){
            this.img = img;
        }
    }

    // Road
    
    int roadX = bwidth/6;
    int roadY = 0;
    int roadWidth = 700;
    int roadHeight = 1080;


    class Road{
        int x = roadX;
        int y = roadY;

        int HEIGHT = roadHeight;
        int WIDTH  = roadWidth;

        Image img;
        
        Road(Image img){
            this.img = img;
        }
    }

    //Danish Car

    int carx = 610;
    int cary = 530;
    int carh = 120;
    int carw = 150;

    class Car{
        int x=carx;
        int y=cary;

        int height = carh;
        int width = carw;

        Image img;

        Car(Image img){
            this.img = img;
        }
    }

    //Danish Bhai
    
    int bhx = roadX + 195;
    int bhy = 105 ;
    int bhheight = 300;
    int bhwidth = 300;

    class Bhai{
        int x = bhx;
        int y = bhy;

        int height = bhheight;
        int width = bhwidth;

        Image img;

        Bhai (Image img){
            this.img = img;
        }
    }


    //obsticles

    class Obstacle {
        int x;
        int y;
        int width;
        int height;
        
        Image img;

      //  boolean passed = false;
    
        Obstacle(Image img, int x, int y) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = 150; // Set width for obstacle
            this.height = 120; // Set height for obstacle
        }
    }

    //left

    int Ox = 0;
    int Oy = 0;
    int Oheight = 1080;
    int Owidth = 290;

    class Left{
        int x =Ox;
        int y = Oy;

        int Height = Oheight;
        int width = Owidth;

        Image img;

        Left(Image img){
            this.img = img;
        }
    }

    //Right side

    int Tx = 1020;
    int Ty = 0;
    int Theight = 1080;
    int Twidth = 400;

    class Right{
        int x = Tx;
        int y = Ty;

        int height = Theight;
        int width = Twidth;

        Image img;

        Right(Image img){
            this.img = img;
        }
    }

    // back of the message 

    int backx = 400;
    int backy = 420;
    int backh = 200;
    int backw = 570;

    class Back{
        int x = backx;
        int y = backy;
        
        int height = backh;
        int width = backw;

        Image img;
        Back(Image img){
            this.img = img;
        }
    }
    

    // game logic    
    Road rd;
    Car cr;
    Ban bn;
    Bhai bh;
    Left l;
    Right r;
    Back bc;



    ArrayList<Obstacle> obstacles = new ArrayList<>();
    int velocityY = 10; //road goes down
    int position ;
    
    ArrayList<Road> roads;

    Timer gameloop;
    Timer movingRoadTimer;
    //double score = 0;
    boolean GameOver = false;


    Danish(){   
        setPreferredSize(new Dimension(bwidth,bheight));
    
        addKeyListener(this);
        setFocusable(true);

      // load Image
        backImg = new ImageIcon(getClass().getResource("./back.png")).getImage();
        RoadImg = new ImageIcon(getClass().getResource("./Road.jpg")).getImage();
        CarImg = new ImageIcon(getClass().getResource("./DanishCar.png")).getImage();
        obstaclImages[0] = new ImageIcon(getClass().getResource("./Car2.png")).getImage();
        obstaclImages[1] = new ImageIcon(getClass().getResource("./Car3.png")).getImage();
        obstaclImages[2] = new ImageIcon(getClass().getResource("./Car1.png")).getImage();
        banImg =  new ImageIcon(getClass().getResource("./ground.jpg")).getImage();
        BhaiImg = new ImageIcon(getClass().getResource("./Bhai.jpg")).getImage();
        one = new ImageIcon(getClass().getResource("./One.jpg")).getImage();
        two =new ImageIcon(getClass().getResource("./Two.jpg")).getImage();
        back = new ImageIcon(getClass().getResource("./back.jpg")).getImage();
        

        //creating Instances
        obstacles = new ArrayList<>();
        roads = new ArrayList<Road>();
        cr = new Car(CarImg);
        bn = new Ban(banImg);
		bh = new Bhai(BhaiImg);
        l = new Left(one);
        r = new Right(two);
        bc = new Back(back);

        //music
        playBack();


        //moving Road Timer 
        movingRoadTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                movingroad();
            }
        });
        
        movingRoadTimer.start();

        //game Timer
        gameloop = new Timer(1000/32, this);
        gameloop.start();

    }

    public void movingroad(){

        Road roaD = new Road(RoadImg);
        roads.add(roaD);
    }

    public void paint(Graphics g){
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g){
        //System.out.println("Draw");


        // background
       g.drawImage(backImg, 0,0,bwidth, bheight, null);

       g.drawImage(bn.img, bn.x, bn.y, bn.width, bn.height, null);
        // Road
        for (int i =0 ; i<roads.size(); i++){
            Road roaD =roads.get(i);
            g.drawImage(roaD.img, roaD.x, roaD.y, roaD.WIDTH , roaD.HEIGHT, null);
        }

        //obstacles
        for(Obstacle obstacle : obstacles){
            g.drawImage(obstacle.img, obstacle.x, obstacle.y, obstacle.width, obstacle.height, null);
        }

        //Danish Bhai's Car 
        g.drawImage(cr.img, cr.x, cr.y, cr.height, cr.width, null);

		g.setColor(Color.black);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 40));

        if(GameOver){
            g.drawImage(bh.img, bh.x, bh.y, bh.width, bh.height, null);
            g.drawImage(bc.img, bc.x, bc.y, bc.width, bc.height, null);

            //message
			g.drawString("Tej Chalai Tune Kyu Apni Gaadi ",425 , 480);
            g.drawString("You Left Danish Bhai Alone " ,450 , 580);
        }
        else{
            g.drawString("game", 20, 50);
        }

        g.drawImage(l.img, l.x, l.y, l.width, l.Height,null);
        g.drawImage(r.img, r.x, r.y, r.width, r.height, null);

    }

    //music stops 
    public void stopback(){
        if(play1 != null && play1.isRunning()){
            play1.stop();
        }
    }

    public void move(){
        // road
        for (Road roaD : roads) {
            roaD.y += velocityY; 
            
        }

        //obstacles
        if(Math.random() < 0.02){
            int ran = (int) (Math.random() * obstaclImages.length);
            Image carImage = obstaclImages[ran];
            int obstacleX = roadX + (int)(Math.random() * (roadWidth - 150));
            obstacles.add(new Obstacle(carImage , obstacleX, -120));
        }

        //moving them down
        for(Obstacle obstacle: obstacles){
            obstacle.y += velocityY;

            // //score check
            // if(!obstacle.passed && cr.x > obstacle.x + obstacle.width){
            //     obstacle.passed = true;    
            //     score++;
            // }

            //game over condition
            if(checkcolli()){
                System.out.println("Fucked");
                GameOver = true;
            }
        }
        obstacles.removeIf(obstacle -> obstacle.y > bheight);
        
        // Danish car
     
        int newX = cr.x + position;

        // Ensure the car stays within the road boundaries
        if (newX >= roadX && newX + cr.width <= roadX + roadWidth) {
            cr.x = newX; // Update car's x position
        }
    }

    public boolean checkcolli(){
        for(Obstacle obstacle: obstacles){
            return cr.x < obstacle.x + obstacle.width &&
                   cr.x + cr.width > obstacle.x &&
                   cr.y < obstacle.y + obstacle.height &&
                   cr.y + cr.height > obstacle.y ;
        }
        return false;
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        move();

        //stop game 
        if(GameOver){
            movingRoadTimer.stop();
            gameloop.stop();
            stopback();
            playOver();
            //System.out.println("why this happens");
        }

        repaint();

        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            position = -13; // Move left
        } 
        else if (key == KeyEvent.VK_RIGHT) {
            position = 13; // Move right
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    
    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            position = 0; // Stop moving
        }
    }
}
