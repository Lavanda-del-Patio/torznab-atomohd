// package es.lavanda.torznab.atomohd.model.dto;

// import java.io.IOException;

// public class TorznabDTO {

//     public class Welcome4 {
//         private Schema schema;

//         public Schema getSchema() {
//             return schema;
//         }

//         public void setSchema(Schema value) {
//             this.schema = value;
//         }
//     }

//     public class Schema {
//         private SimpleType simpleType;
//         private Element element;
//         private String xmlnsXs;
//         private String xmlnsTorznab;
//         private String targetNamespace;
//         private Prefix prefix;

//         public SimpleType getSimpleType() {
//             return simpleType;
//         }

//         public void setSimpleType(SimpleType value) {
//             this.simpleType = value;
//         }

//         public Element getElement() {
//             return element;
//         }

//         public void setElement(Element value) {
//             this.element = value;
//         }

//         public String getXmlnsXs() {
//             return xmlnsXs;
//         }

//         public void setXmlnsXs(String value) {
//             this.xmlnsXs = value;
//         }

//         public String getXmlnsTorznab() {
//             return xmlnsTorznab;
//         }

//         public void setXmlnsTorznab(String value) {
//             this.xmlnsTorznab = value;
//         }

//         public String getTargetNamespace() {
//             return targetNamespace;
//         }

//         public void setTargetNamespace(String value) {
//             this.targetNamespace = value;
//         }

//         public Prefix getPrefix() {
//             return prefix;
//         }

//         public void setPrefix(Prefix value) {
//             this.prefix = value;
//         }
//     }

//     public class Element {
//         private ComplexType complexType;
//         private String name;
//         private Prefix prefix;

//     }

//     public class ComplexType {
//         private Attribute[] attribute;
//         private Prefix prefix;

//         // Attribute.java

//         public class Attribute {
//             private String name;
//             private String type;
//             private Prefix prefix;

//         }

//         public enum Prefix {
//             XS;

//             public String toValue() {
//                 switch (this) {
//                     case XS:
//                         return "xs";
//                 }
//                 return null;
//             }

//             public static Prefix forValue(String value) throws IOException {
//                 if (value.equals("xs"))
//                     return XS;
//                 throw new IOException("Cannot deserialize Prefix");
//             }
//         }

//         public class SimpleType {
//             private Restriction restriction;
//             private String name;
//             private Prefix prefix;

//         }

//         public class Restriction {
//             private Enumeration[] enumeration;
//             private String base;
//             private Prefix prefix;

//         }

// public class Enumeration {
//     private String value;
//     private Prefix prefix;

// }