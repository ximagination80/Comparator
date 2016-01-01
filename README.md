[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)
[![codecov.io](https://codecov.io/github/ximagination80/Comparator/coverage.svg?branch=master)](https://codecov.io/github/ximagination80/Comparator?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ximagination80/Comparator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
[![License](http://img.shields.io/:license-Apache%202-red.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
# Xml,Json template comparator

#License
The Apache License.

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

## Dependency

```scala
"imagination" % "comparator_2.11" % "0.6-SNAPSHOT" % "test"
```
or

```scala
 <dependency>
    <groupId>imagination</groupId>
    <artifactId>comparator_2.11</artifactId>
    <version>0.6-SNAPSHOT</version>
    <scope>test</scope>
 </dependency>
```
==
## Installation

* git clone https://github.com/ximagination80/Comparator.git
* cd Comparator
* sbt publishLocal publishM2  (publishing into [ivy,m2] repo)

### Dependency list

* "com.fasterxml.jackson.core" % "jackson-core" % "2.6.3"
* "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.3"

==
### How to use:

* Comparator.strict.compare("a","b") or Comparator(mode = Strict).compare("a","b")
* Comparator.lenient.compare("a","b") or Comparator(mode = Lenient).compare("a","b")

==
Usage:

```scala
  import comparator._

  Comparator(mode = Strict).compare(
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

  Comparator(mode = Strict).compare(
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

  Comparator(mode = Strict).compare(
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

  Comparator(mode = Strict).compare(
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

  Comparator(mode = Lenient).compare(
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

  Comparator(mode = Lenient).compare(
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

# Additional features

* Custom pattern registration

```scala
import comparator._

implicit val aliases = AliasMap().
    add("date","\\d{4}-\\d{2}-\\d{2}").
    add("number", Pattern.compile("\\d+"))

Comparator.strict.compare(
  """
    {"date":"p(date)", "cost":"p(number)"}
  """,
  """
    {"date":"2015-11-11", "cost":"100"}
  """)
```

* Predefined patterns/functions.Use "p(function name)"

 > any
 > digit
 > >0
 > >=0
 > <0
 > <=0
 > money
 > email
 > ip
 > url
 > uuid
 > color
 > y-m-d
 > d-m-y
 > y/m/d
 > d/m/y

```scala
import comparator._

Comparator.strict.compare(
  """
    {
      "response" : [ {
        "1" : "p(any)",
        "2" : "p(digit)",
        "3" : "p(>0)",
        "4" : "p(>=0)",
        "5" : "p(<0)",
        "6" : "p(<=0)",
        "7" : "p(money)",
        "8" : "p(email)",
        "9" : "p(ip)",
        "10" : "p(url)",
        "11" : "p(uuid)",
        "12" : "p(color)",
        "13" : "p(y-m-d)",
        "14" : "p(d-m-y)",
        "15" : "p(y/m/d)",
        "16" : "p(d/m/y)"
      } ]
    }
  """,
  """
    {
      "response" : [ {
        "1" : "1qaA2../=2313((78^(_}|{$*%()$*!)__+#(!@%)(!@*@+_#%_>?<LKmasO",
        "2" : "-100",
        "3" : "10",
        "4" : "+100",
        "5" : "-1",
        "6" : "0",
        "7" : "100.10",
        "8" : "ximagination80@gmail.com",
        "9" : "10.10.10.10",
        "10" : "http://google.com",
        "11" : "550e8400-e29b-41d4-a716-446655440000",
        "12" : "#1f1f1f",
        "13" : "2015-11-12",
        "14" : "12-11-2015",
        "15" : "2015/11/12",
        "16" : "12/11/2015"
      } ]
    }
  """)
```

* Java support   (see examples)
```java
  package root;

  import comparator.Comparator;
  import org.junit.Test;

  import java.util.HashMap;
  import java.util.Map;
  import java.util.regex.Pattern;

  public class ComparatorTest {

      private static final Map<String, Pattern> ALIASES = new HashMap<String, Pattern>() {
          {
              put("digit", Pattern.compile("\\d+"));
          }
      };

      private static Comparator createComparator() {
          return Comparator.java().strict(ALIASES);
      }

      @Test
      public void matches() throws Exception {
          String expected = "{\"count\":\"p(digit)\"}";
          String actual = "{\"count\":\"120\"}";

          createComparator().compare(expected, actual);
      }
  }


```