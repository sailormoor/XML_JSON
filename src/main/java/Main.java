import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        final String fileName = "data.xml";
        final String jsonName = "data2.json";

        toCreateFile(fileName);

        List<Employee> list = parseXML(fileName);

        String json = listToJson(list);

        writeString(json, jsonName);
    }

    public static void toCreateFile(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element staff = document.createElement("staff");
            document.appendChild(staff);

            Element employee1 = document.createElement("employee");
            staff.appendChild(employee1);

            Element id1 = document.createElement("id");
            id1.appendChild(document.createTextNode("1"));
            employee1.appendChild(id1);
            Element firstName1 = document.createElement("firstName");
            firstName1.appendChild(document.createTextNode("John"));
            employee1.appendChild(firstName1);
            Element lastName1 = document.createElement("lastName");
            lastName1.appendChild(document.createTextNode("Smith"));
            employee1.appendChild(lastName1);
            Element country1 = document.createElement("country");
            country1.appendChild(document.createTextNode("USA"));
            employee1.appendChild(country1);
            Element age1 = document.createElement("age");
            age1.appendChild(document.createTextNode("25"));
            employee1.appendChild(age1);

            Element employee2 = document.createElement("employee");
            staff.appendChild(employee2);

            Element id2 = document.createElement("id");
            id2.appendChild(document.createTextNode("2"));
            employee2.appendChild(id2);
            Element firstName2 = document.createElement("firstName");
            firstName2.appendChild(document.createTextNode("Ivan"));
            employee2.appendChild(firstName2);
            Element lastName2 = document.createElement("lastName");
            lastName2.appendChild(document.createTextNode("Petrov"));
            employee2.appendChild(lastName2);
            Element country2 = document.createElement("country");
            country2.appendChild(document.createTextNode("RU"));
            employee2.appendChild(country2);
            Element age2 = document.createElement("age");
            age2.appendChild(document.createTextNode("23"));
            employee2.appendChild(age2);

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(
                    new File(fileName));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);
        } catch (ParserConfigurationException | TransformerException e) {
            e.fillInStackTrace();
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> list = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(fileName);

            Node staff = document.getDocumentElement();
            NodeList nodeList = staff.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.ELEMENT_NODE == node.getNodeType()) {
                    Element employee = (Element) node;

                    long id = Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent());
                    String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = employee.getElementsByTagName("country").item(0).getTextContent();
                    int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());

                    list.add(new Employee(id, firstName, lastName, country, age));
                }
            }
            return list;

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.fillInStackTrace();
        }
        return null;
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static void writeString(String json, String jsonName) {
        try (FileWriter writer = new FileWriter(jsonName)) {
            writer.write(json);
            writer.append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}