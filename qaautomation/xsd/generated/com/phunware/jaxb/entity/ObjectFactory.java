//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.0 in JDK 1.6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.07 at 02:45:15 PM IST 
//


package com.phunware.jaxb.entity;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.phunware.jaxb.entity package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.phunware.jaxb.entity
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Testcase.Case }
     * 
     */
    public Testcase.Case createTestcaseCase() {
        return new Testcase.Case();
    }

    /**
     * Create an instance of {@link Testcase }
     * 
     */
    public Testcase createTestcase() {
        return new Testcase();
    }

    /**
     * Create an instance of {@link Testcase.Case.Param }
     * 
     */
    public Testcase.Case.Param createTestcaseCaseParam() {
        return new Testcase.Case.Param();
    }
    
    /**
     * Create an instance of {@link Config }
     * 
     */
    public Config createConfig() {
        return new Config();
    }

    /**
     * Create an instance of {@link Config.Environment }
     * 
     */
    public Config.Environment createConfigEnvironment() {
        return new Config.Environment();
    }

}
