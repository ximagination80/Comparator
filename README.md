[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)
[![codecov.io](https://codecov.io/github/ximagination80/Comparator/coverage.svg?branch=master)](https://codecov.io/github/ximagination80/Comparator?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ximagination80/Comparator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)
# Xml,Json template comparator

[![Join the chat at https://gitter.im/ximagination80/Comparator](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/ximagination80/Comparator?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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
  match

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
  match only 2015-11-01 value

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
  match any date or string


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
  not match because of incorrect "date" field template

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
  match. Do not check field "name" in actual input
  
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
  
  1) Assert "date" pattern
  2) Assert "date" / "fields" column names
  3) Assert "fields" array length
  4) Assert each array object & ignore "default" column 





