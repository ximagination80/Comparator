# Comparator

We use [Travis CI](http://travis-ci.org/) to verify the build:
[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)

We use [Coveralls](https://coveralls.io/r/ximagination80/Comparator) for code coverage results:
[![Coverage Status](https://coveralls.io/repos/ximagination80/Comparator/badge.png?branch=develop)](https://coveralls.io/r/ximagination80/Comparator?branch=develop)

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





