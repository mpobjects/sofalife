[![Apache License, Version 2.0](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://raw.githubusercontent.com/mpobjects/sofalife/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/mpobjects/sofalife.svg)](https://github.com/mpobjects/sofalife/issues)
[![Build Status](https://travis-ci.org/mpobjects/sofalife.svg?branch=master)](https://travis-ci.org/mpobjects/sofalife)

# sofalife 

Reader for Structured Fixed Length Formats

Sofalife is a Java library to process text files which have a structured fixed length format. Fixed length files are column based, like CSV. In some cases a file is used where lines can have different record types with different definitions, and possibly a hierarchy in elements. Standard fixed length parsers are therefor not usable to process these file.

This library only exist in order to cope with these kinds of horrible formats. Please do not define your own format which can be handled by this library, make use of the various properly structured text formats like XML, JSON, or YAML.


## Format Specification

The format to read is specified by an XML file according to the XML Schama defined in [sofalife.xsd](https://github.com/mpobjects/sofalife/blob/master/src/main/resources/sofalife.xsd).

An example specification is available in the source.

## Usage

```java
// Load the specification file
FormatSpec spec = new SpecLoader().load(inputStream);

// Create an converter from the flat file to and XML file
XmlConverter xmlConverter = new XmlConverter(spec);

// Convert the file
xmlConverter.convert(inputStream, outputWriter)
```
