[![Build Status](https://travis-ci.org/mpobjects/sofalife.svg?branch=master)](https://travis-ci.org/mpobjects/sofalife)
[![Maven Central](https://img.shields.io/maven-central/v/com.mpobjects.formats/sofalife.svg)](http://www.maven.org/#search%7Cga%7C1%7Cg%3A%22com.mpobjects.formats%22%20AND%20a%3A%22sofalife%22)
![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.mpobjects.formats/sofalife.svg)

# sofalife 

Reader for Structured Fixed Length Formats

Sofalife is a Java library to process text files which have a structured fixed length format. Fixed length files are column based, like CSV. In some cases a file is used where lines can have different record types with different definitions, and possibly a hierarchy in elements. Standard fixed length parsers are therefore not usable to process these file.

This library only exist in order to cope with these kinds of horrible formats. Please do not define your own format which can be handled by this library, make use of the various properly structured text formats like XML, JSON, or YAML.


## Format Specification

The format to read is specified by a XML file according to the XML Schema defined in [sofalife.xsd](https://github.com/mpobjects/sofalife/blob/master/src/main/resources/sofalife.xsd).

An example specification is available in the source.

## Usage

```java
// Load the specification file
FormatSpec spec = new SpecLoader().load(inputStream);

// Create a converter from the flat file to a XML file
XmlConverter xmlConverter = new XmlConverter(spec);

// Convert the file
xmlConverter.convert(inputStream, outputWriter)
```
