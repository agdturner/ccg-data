/**
 * Copyright 2010 Andy Turner, The University of Leeds, UK
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package uk.ac.leeds.ccg.data.format;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Adapted from:
 * http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 */
public abstract class Data_ReadXML {

    protected Path p;
    protected NodeList nodeList;
    protected String nodeName;
    protected DocumentBuilderFactory docBuilderFactory;
    protected DocumentBuilder docBuilder;
    protected Document doc;

    /**
     * Create a new instance.
     */
    public Data_ReadXML(){}
    
    /**
     * Create a new instance.
     *
     * @param p The Path to the file to read.
     * @param nodeName For setting {@link #nodeName} to. If nodeName is "*" this
     * will initialise the node list for all nodes otherwise it will only
     * initialise the node list for those nodes with the given name.
     */
    public Data_ReadXML(Path p, String nodeName) {
        this.p = p;
        this.nodeName = nodeName;
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Data_ReadXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            doc = docBuilder.parse(p.toFile());
        } catch (SAXException | IOException ex) {
            Logger.getLogger(Data_ReadXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
    }

    protected void initNodeList(String nodeName) {
        nodeList = doc.getElementsByTagName(nodeName);
    }

    protected void readNodeListElements() {
        int depth = 0;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            System.out.println("depth " + depth + ", node " + i);
            System.out.println(node.getNodeName());
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList childNodes = element.getChildNodes();
                if (childNodes.getLength() == 0) {
                    String attributeName = node.getNodeValue();
                    String attributeValue = element.getElementsByTagName(attributeName).item(0).getTextContent();
                    System.out.println("depth " + depth + ", node " + i);
                    System.out.println("" + attributeName + " : " + attributeValue);
                } else {
                    readNodeListElements(childNodes, depth + 1);
                }
            }
//            if (aNodeType == Node.TEXT_NODE) {
//                String attributeName = aNode.getNodeValue();
//                String attributeValue = aNode.getTextContent();
//                System.out.println("" + attributeName + " : " + attributeValue);
//            }
        }
    }

    protected void readNodeListElements(NodeList nodeList, int depth) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            System.out.println("depth " + depth + ", node " + i);
            System.out.println(node.getNodeName());
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                NodeList childnodes = element.getChildNodes();
                if (childnodes.getLength() == 0) {
                    String attributeName = node.getNodeValue();
                    String attributeValue = element.getElementsByTagName(attributeName).item(0).getTextContent();
                    System.out.println("depth " + depth + ", node " + i);
                    System.out.println("" + attributeName + " : " + attributeValue);
                } else {
                    readNodeListElements(childnodes, depth + 1);
                }
            }
            if (nodeType == Node.TEXT_NODE) {
                String attributeName = node.getNodeValue().trim();
                String attributeValue = node.getTextContent().trim();
                System.out.println("depth " + depth + ", node " + i);
                System.out.println("" + attributeName + " : " + attributeValue);
            }
        }
    }

    protected abstract void parseNodeList();
}
