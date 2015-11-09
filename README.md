[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)
[![codecov.io](https://codecov.io/github/ximagination80/Comparator/coverage.svg?branch=master)](https://codecov.io/github/ximagination80/Comparator?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ximagination80/Comparator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
# Xml,Json template comparator

# Usage

* Unit testing (can replace assert)
* Functional testing
* Regression testing

# Assert list

* Incorrect types (Expected Null but was String etc)
* Incorrect field name
* Incorrect field value ( Equals or Pattern )
* Incorrect array length
* Field is missing

# Installation

> git clone https://github.com/ximagination80/Comparator.git
> sbt publishLocal

### Version

> LAST_VERSION = 0.2-SNAPSHOT

### Dependency
> imagination % comparator_2.11 % LAST_VERSION % test

### Dependency list

> "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3"
> "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3"

==

Usage:

```scala
  import comparator._

  Comparator(mode = STRICT).compare(
  """
    {"name":"p([a-z]+)","date":"p(\\d{4}-\\d{2}-\\d{2})"}
  """,
  """
    {"name":"imagination","date":"2015-11-01"}
  """)
```
  match ( Comparator won't throw ComparisonError )

* Will compare name by pattern template [a-z]+ (Pattern.DOTALL flag)
* Will compare date by pattern template \\d{4}-\\d{2}-\\d{2} (Pattern.DOTALL flag)


==
```scala
  import comparator._

  Comparator(mode = STRICT).compare(
  """
    {"name":"p([a-z]+)","date":"2015-11-01"}
  """,
  """
    {"name":"imagination","date":"2015-11-01"}
  """)
```
  match ( Comparator won't throw ComparisonError )

* Will compare name by pattern template [a-z]+ (Pattern.DOTALL flag)
* Will compare date by equals strategy


==
```scala
  import comparator._

  Comparator(mode = STRICT).compare(
  """
    {"name":"p([a-z]+)","date":"p(.*)"}
  """,
  """
    {"name":"imagination","date":"blah or 01-01-2015"}
  """)
```
  match ( Comparator won't throw ComparisonError )

* Will compare name by pattern template [a-z]+ (Pattern.DOTALL flag)
* Field "date" should present with any string value

==
```scala
  import comparator._

  Comparator(mode = STRICT).compare(
  """
    {"name":"p([a-z]+)","date":"p(\\d{4}-\\d{2}-\\d{2})"}
  """,
  """
    {"name":"imagination","date":"01-01-2015"}
  """)
```
  not match because "date" field template isn't correct ( Comparator will throw ComparisonError)

==
```scala
  import comparator._

  Comparator(mode = LENIENT).compare(
  """
    {"date":"p(\\d{4}-\\d{2}-\\d{2})"}
  """,
  """
    {"name":"imagination","date":"01-01-2015"}
  """)
```
  match. ( Comparator won't throw ComparisonError )

* Field "date" should present and it should match pattern \\d{4}-\\d{2}-\\d{2})

==
```scala
  import comparator._

  Comparator(mode = LENIENT).compare(
  """
    {"date":"p(\\d{4}-\\d{2}-\\d{2})", "fields":
          [
            {
              "field1":"name",
              "type":"int"
            },
            {
               "field2":"surname",
               "type":"string"
            }, {}
      ]
    }
  """,
  """
    {"date":"2015-11-11", "fields":
          [
            {
              "field1":"name",
              "type":"int",
              "default": "0"
            },
            {
              "field2":"surname",
              "type":"string",
              "default": ""
            },
            {
              "field2":"surname",
              "type":"string",
              "default": ""
            }
          ] 
    }
  """)
```
  match. 
  
* Assert "date" pattern
* Assert "date" / "fields" column names
* Assert "fields" array length
* Assert each array object and ignore "default" column





