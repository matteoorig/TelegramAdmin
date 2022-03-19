import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

public class grafica extends JFrame {

    openStreatMapManager c = new openStreatMapManager();
    telegramManager man = new telegramManager("5220725554:AAE4s92QSGANF5cCjQ9CBB0FOacLYy8xS4Q", c);

    JPanel panel;
    public grafica(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        panel = new JPanel();
        man.start();


        JLabel placeLabelBenvenuto = new JLabel("Benvenuto!");
        placeLabelBenvenuto.setBounds(10,20,120,25);
        add(placeLabelBenvenuto);

        JLabel placeLabelins = new JLabel("INSERISCI UN EVENTO");
        placeLabelins.setBounds(10,40,200,25);
        add(placeLabelins);

        JLabel placeLabecitta = new JLabel("Città evento: ");
        placeLabecitta.setBounds(10,80,200,25);
        add(placeLabecitta);

        JTextField citta = new JTextField();
        citta.setBounds(150, 80, 200, 25);
        add(citta);
        JLabel placeLaberaggio = new JLabel("Raggio di espansione evento: ");
        placeLaberaggio.setBounds(10,110,200,25);
        add(placeLaberaggio);

        JTextField raggio = new JTextField();
        raggio.setBounds(195, 110, 155, 25);
        add(raggio);

        JPanel line = new JPanel();
        line.setBackground(Color.BLUE);
        line.setBounds(10, 150, 335,3);
        add(line);

        JLabel placeLabelContenuti= new JLabel("Contenuto: ");
        placeLabelContenuti.setBounds(10,165,200,25);
        add(placeLabelContenuti);
        JTextArea contenuto = new JTextArea();
        contenuto.setBounds(150, 180, 195, 100);
        add(contenuto);

        JPanel line2 = new JPanel();
        line2.setBackground(Color.BLUE);
        line2.setBounds(10, 290, 335,3);
        add(line2);


        Button formGetUpdates = new Button("AGGIUNGI");
        formGetUpdates.setBounds(250, 300, 100, 35);
        formGetUpdates.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    man.addEvent(citta.getText(), Integer.parseInt(raggio.getText()), contenuto.getText());
                    String conf = "[SERVER] Evento aggiunto a "+ citta.getText()+" | "+ raggio.getText()+ "Km |";
                    man.stampLog(conf);
                    System.out.println(conf);


                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (ParserConfigurationException parserConfigurationException) {
                    parserConfigurationException.printStackTrace();
                } catch (SAXException saxException) {
                    saxException.printStackTrace();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        add(formGetUpdates);

    }


    
    public static void main(String[] args){
        grafica g = new grafica();
        g.setTitle("Telegram Admin");
        g.setSize(600, 400);
        g.setVisible(true);
    }
}
