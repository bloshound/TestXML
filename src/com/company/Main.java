package com.company;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String input;
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            while (!(input = consoleReader.readLine()).equals("exit")) {

                findDuplicateItems(input);
                findHousesWithSities(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void findDuplicateItems(String input) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(input));
             XMLEventReaderInitializer xmlEventReaderInitializer = new XMLEventReaderInitializer(bufferedInputStream)) {

            XMLEventReader xmlReader = xmlEventReaderInitializer.getXmlEventReader();
            HashMap<String, Long> startElements = new HashMap<>();

            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();

                if (event.isStartElement() && event.asStartElement().getName().equals(new QName("item"))) {
                    String startElement = event.toString();
                    startElements.merge(startElement, 1L, Long::sum);
                }
            }
            Set<Map.Entry<String, Long>> clearSet = startElements.entrySet();
            clearSet.removeIf(pair -> pair.getValue() < 2);
            clearSet.forEach(pair -> System.out.println("Итем: " + pair.getKey() + ", колличество: " + pair.getValue()));

        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private static void findHousesWithSities(String input) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(input));
             XMLEventReaderInitializer xmlEventReaderInitializer = new XMLEventReaderInitializer(bufferedInputStream)) {

            XMLEventReader xmlReader = xmlEventReaderInitializer.getXmlEventReader();
            HashMap<String, int[]> startElements = new HashMap<>();
            QName cityQName = new QName("city");
            QName floorQName = new QName("floor");

            while (xmlReader.hasNext()) {
                XMLEvent event = xmlReader.nextEvent();
                if (event.isStartElement()
                        && event.asStartElement().getAttributeByName(cityQName) != null
                        && event.asStartElement().getAttributeByName(floorQName) != null) {

                    StartElement startElement = event.asStartElement();
                    startElements.merge(startElement.getAttributeByName(cityQName).getValue(),
                            new int[6],
                            (oldValue, newValue) -> {
                                int index = Integer.parseInt(startElement.getAttributeByName(floorQName).getValue());
                                oldValue[index]++;
                                return oldValue;
                            });
                }
            }
            TreeMap<String, int[]> sorted = new TreeMap<>(String::compareTo);
            sorted.putAll(startElements);
            sorted.forEach((city, floors) -> System.out.println("Город: " + city + ", этажи: " + Arrays.toString(floors)));


        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
