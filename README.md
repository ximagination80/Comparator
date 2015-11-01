[![Build Status](https://travis-ci.org/ximagination80/Comparator.png)](https://travis-ci.org/ximagination80/Comparator)
[![Coverage Status](https://coveralls.io/repos/ximagination80/Comparator/badge.svg?branch=master&service=github)](https://coveralls.io/github/ximagination80/Comparator?branch=master)

| [https://codecov.io/][1] | [@codecov][2] | [hello@codecov.io][3] |
| ------------------------ | ------------- | --------------------- |

# Comparator XML/Json template comparator

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





