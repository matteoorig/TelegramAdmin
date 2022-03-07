import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public class main {
    static telegramManager tm = new telegramManager("5220725554:AAE4s92QSGANF5cCjQ9CBB0FOacLYy8xS4Q");
    static openMapManager map = new openMapManager();
    public static void main(String[] args) {


        JFrame frame = new JFrame("TELEGRAM ADMIN");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JButton getPosition = new JButton("Cordinata");
        getPosition.setBounds(40, 40, 150, 25);
        getPosition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get latitudine longitudine

                try {
                    CCordinata cord;
                    cord = map.getPosition("Meda");
                    System.out.println("Latitudine: "+ cord.getLat()+" Longitudine: "+ cord.getLon());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                }



            }
        });
        panel.add(getPosition);
    }

}
