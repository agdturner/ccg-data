/*
 * Copyright 2024 Centre for Computational Geography, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.data.xml;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 *
 * @author Andy Turner
 */
public class Validate {
    
    public static void main(String[] args) {
        try {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            Path dataDir = Paths.get("C:", "Users", "geoagdt", "src", "agdt", "java", "generic",  "ccg-data", "data");
            System.out.println("dataDir " + dataDir);
            Path xsdPath = Paths.get(dataDir.toString(), "schemas", "mesmerNew.xsd");
            
            
            // Print xsd
            String xsdContent = new String(Files.readAllBytes(xsdPath));
            System.out.println("xsdContent:\n" + xsdContent);
            
            Schema schema = factory.newSchema(xsdPath.toFile());
            System.out.println("schema:" + schema);
            Validator validator = schema.newValidator();
            
            Path xmlPath = Paths.get(dataDir.toString(), "examples", "AcetylO2", "Acetyl_O2_associationEx.xml");

            // Print XML
            String xmlContent = new String(Files.readAllBytes(xmlPath));
            System.out.println("xmlContent:\n" + xmlContent);
            
            validator.validate(new StreamSource(xmlPath.toFile()));
            System.out.println("XML is valid.");
        } catch (Exception e) {
            System.out.println("XML is not valid because ");
            e.printStackTrace(System.out);
            System.out.println(e.getMessage());
        }
    }
}
