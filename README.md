# Comparator

We use [Travis CI](http://travis-ci.org/) to verify the build:
[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)

XML/Json template comparator.

Usage:

```scala
  import ObjectComparator._

  StringComparator.compare(
  """
    {"name":"p([a-z]+)","date":"p(\\d{4}-\\d{2}-\\d{2})"}
  """,
  """
    {"name":"imagination","date":"2015-11-01"}
  """)
```
  match


```scala
  import ObjectComparator._

  StringComparator.compare(
  """
    {"name":"p([a-z]+)","date":"p(\\d{4}-\\d{2}-\\d{2})"}
  """,
  """
    {"name":"Imagination","date":"01-01-2015"}
  """)
```
  not match





