import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.Semaphore;

public class ProducerConsumer extends JFrame
{

    static int theBuffer;
    static Semaphore s = new Semaphore(1);
    int avgProduced;
    int avgConsumed;
    boolean canRun; // control for loops
    JLabel resource, producersLabel, consumersLabel, avgProducedLabel, avgConsumedLabel, sResource; // shows resource current value
    JTextArea textarea; // shows console messages
    String text; // text to show in text area
    JTextField filename = new JTextField(),
            d1 = new JTextField(7),d2 = new JTextField(7),
            d3= new JTextField(7),
            dir= new JTextField(7);


    public static void mySleep(){
        // this function puts the thread "to sleep" for a while,
        // to simulate time spent processing

        try{
            Thread.sleep((int)(Math.random()*1000));
        }
        catch(InterruptedException e){
            // do nothing
        }
    } // close sleep method


    public static void main(String [] args) throws Exception
    {
        ProducerConsumer gui = new ProducerConsumer();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setTitle("ProducerConsumer GUI");
        gui.setSize(600,600);
        gui.setVisible(true);

        Consumer [] c = new Consumer[5];
        Producer [] p = new Producer[5];

        for(int i = 0; i < 5; i++){
            c[i] = new Consumer(i);
            p[i] = new Producer(i);
            c[i].start();
            p[i].start();
        }
    }

    public ProducerConsumer()
    {
      /*
      JFrame frame = new JFrame("Concurrency GUI");
      frame.setSize(600,600);
      BuildGUI();
      frame.setVisible(true);
      */
        //has panels and components inside those panels
        Container pane = this.getContentPane();
        pane.setLayout(new GridLayout(15, 2, 2, 2));

        //font
        Font appFontLarge = new Font("Arial", Font.PLAIN, 50);
        Font appFontSmall = new Font("Arial", Font.PLAIN, 18);

        //Top panel
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(1, 1));

        //panel
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2,1));
        JPanel titlePanel = new JPanel();
        JPanel buildPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        //label
        producersLabel = new JLabel("PRODUCERS: ");
        consumersLabel = new JLabel("PRODUCERS: ");
        avgProducedLabel = new JLabel("AVERAGE PRODUCED: ");
        avgConsumedLabel = new JLabel("AVERAGE CONSUMED: ");


        //creating the MIDDLE JPanel
        JPanel mid = new JPanel();
        mid.setLayout(new GridLayout(4,2));
        pane.add(mid);


        //text fields
        JTextField prodVal = new JTextField(5),
                consVal = new JTextField(5),
                avgProdVal = new JTextField(5),
                avgConsVal = new JTextField(5);
        mid.add(producersLabel);
        mid.add(prodVal);

        mid.add(consumersLabel);
        mid.add(consVal);

        mid.add(avgProducedLabel);
        mid.add(avgProdVal);

        mid.add(avgConsumedLabel);
        mid.add(avgConsVal);

        //buttons
        JButton start = new JButton("start"),
                stop = new JButton("stop");
        pane.add(start);
    }

    private static class Producer extends Thread
    {
        int i;
        public Producer(int i){
            super();
            this.i = i;
        }

        public void run(){
            while(true){
                mySleep();
                System.out.println("Producer " + i + ": attempting to acquire");
                try{
                    s.acquire();
                    System.out.println("Producer " + i + ": resource acquired!");
                    mySleep();
                    System.out.println("Producer " + i + ": theBuffer (pre)  is " + theBuffer);
                    theBuffer += (int) (Math.random()*6);
                    System.out.println("Producer " + i + ": theBuffer (post) is " + theBuffer);
                    System.out.println("Producer " + i + ": resource released");
                    s.release();
                }
                catch(InterruptedException e){}
            }
        }
    }

    private static class Consumer extends Thread{
        int i;
        public Consumer(int i){
            super();
            this.i = i;
        }

        public void run(){
            while(true){
                mySleep();
                System.out.println("Consumer " + i + ": attempting to acquire");
                try{
                    s.acquire();
                    System.out.println("Consumer " + i + ": resource acquired!");
                    mySleep();
                    System.out.println("Consumer " + i + ": theBuffer is " + theBuffer);
                    int need = (int) (1 + Math.random()*6);
                    System.out.println("Consumer " + i + ": my need is " + need);
                    if (theBuffer >= need){
                        theBuffer -= need;
                        System.out.println("Consumer " + i + ": got what I needed!");
                        System.out.println("Consumer " + i + ": theBuffer is now " + theBuffer);
                    }
                    else{
                        System.out.println("Consumer " + i + ": resource unavailable");
                    }
                    System.out.println("Consumer " + i + ": resource released");
                    s.release();
                }
                catch(InterruptedException e){}
            }
        }
    }
}
