# Flower Shop
A flower order processor for a code interview.

![GitHub CI](https://github.com/nicosalvato/flowershop/actions/workflows/gradle.yml/badge.svg)
[![codecov](https://codecov.io/gh/nicosalvato/flowershop/branch/main/graph/badge.svg)](https://codecov.io/gh/codecov/java-Standard)

## Getting Started
Flower Shop is a Gradle project built with Java 17. 

### Gradle Build
Download the project and execute Gradle tasks either from your favourite IDE
or using _gradlew_ wrapper.
You can build the project as an executable JVM application with the Shadow Distribution
plugin:
```shell
 ./gradlew distZip 
```

### Exacutable JVM App
Download [flowershop-1.0.zip](https://github.com/nicosalvato/flowershop/releases/download/1.0/flowershop-1.0.zip),
unzip it and launch it form the command line:
```shell
./flowershop <order_file> <json_configuration_file>
```
The **order_file** is a text file containing desired items - amount and code, space separated - one per line:
```text
10 R12
15 L09
13 T58
```
The **json_configuration_file** is a JSON (surprise) file containing a list of products and their bundles, formatted as in the example below:
```json
[
  {
    "code": "R12",
    "name": "Roses",
    "bundles": [
      {
        "bundleSize": 5,
        "price": 6.99
      }, {
        "bundleSize": 10,
        "price": 12.99
      }
    ]
  }, {
  "code": "L09",
  "name": "Lilies",
  "bundles": [
    {
      "bundleSize": 3,
      "price": 9.95
    }, {
      "bundleSize": 6,
      "price": 16.95
    }, {
      "bundleSize": 9,
      "price": 24.95
    }
  ]
}, {
  "code": "T58",
  "name": "Tulips",
  "bundles": [
    {
      "bundleSize": 3,
      "price": 5.95
    }, {
      "bundleSize": 5,
      "price": 9.95
    }, {
      "bundleSize": 9,
      "price": 16.99
    }
  ]
}
]
```

