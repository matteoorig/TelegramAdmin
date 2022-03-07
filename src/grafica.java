import javax.swing.*;
import java.awt.*;

public class grafica extends JFrame {

    JPanel panel;
    public grafica(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        panel = new JPanel();
        JLabel placeLabel = new JLabel("ciao");
        placeLabel.setBounds(10,20,120,25);
        add(placeLabel);

        panel.setBounds(0, 0, 600, 75);
        panel.setBackground(Color.orange);
        add(panel);

    }
    
    public static void main(String[] args){
        grafica g = new grafica();
        g.setTitle("Telegram Admin");
        g.setSize(600, 300);
        g.setVisible(true);
    }
}
