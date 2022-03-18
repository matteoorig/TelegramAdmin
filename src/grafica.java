import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class grafica extends JFrame {

    openStreatMapManager c = new openStreatMapManager();
    telegramManager man = new telegramManager("5220725554:AAE4s92QSGANF5cCjQ9CBB0FOacLYy8xS4Q", c);

    JPanel panel;
    public grafica(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        panel = new JPanel();
        JLabel placeLabel = new JLabel("Benvenuto!");
        placeLabel.setBounds(10,20,120,25);
        add(placeLabel);
        man.start();

        Button formGetUpdates = new Button();
        formGetUpdates.setBounds(30, 100, 100, 50);
        formGetUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getUpdates

            }
        });
        add(formGetUpdates);

    }


    
    public static void main(String[] args){
        grafica g = new grafica();
        g.setTitle("Telegram Admin");
        g.setSize(600, 300);
        g.setVisible(true);
    }
}
