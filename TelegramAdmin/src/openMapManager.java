import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

public class openMapManager {

    public openMapManager(){

    }

    public CCordinata getPosition(String in) throws IOException, ParserConfigurationException, SAXException {
        CCordinata pos = new CCordinata();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new URL("https://nominatim.openstreetmap.org/search?q="+in+"&format=xml&addressdetails=1").openStream());

        NodeList nList = doc.getElementsByTagName("place");

        NamedNodeMap childs = nList.item(0).getAttributes();
        pos.setLat(Float.parseFloat(childs.getNamedItem("lat").getTextContent()));
        pos.setLon(Float.parseFloat(childs.getNamedItem("lon").getTextContent()));
        return pos;
    }

}
