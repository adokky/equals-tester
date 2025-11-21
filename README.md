[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.adokky/equals-tester)](https://mvnrepository.com/artifact/io.github.adokky/equals-tester)
[![javadoc](https://javadoc.io/badge2/io.github.adokky/equals-tester/javadoc.svg)](https://javadoc.io/doc/io.github.adokky/equals-tester)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

Automatic tester for `equals()`, `hashCode()`, `compareTo()`, and `toString()` methods of a class.  
Similar to Guava's [EqualsTester][GuavaEqualsTester], but designed for Kotlin Multiplatform with enhanced validation features.

[GuavaEqualsTester]: https://www.javadoc.io/doc/com.google.guava/guava-testlib/21.0/com/google/common/testing/EqualsTester.html

## Example

```kotlin
testEquality {
    group(User("alex"), User("alex"), User("alex"))
    group(User("sergey"), User("sergey"))
    // User("page") is instantiated multiple times (default is 2).
    // Can be customized via defaultGroupSize.
    group { User("page") }
}
```

* Each group must contain objects that are equal to each other, but not equal to objects in any other group.
* Adding more than one group is optional, but highly recommended.
* There is no limit on the number of groups or objects. If the total number of test iterations is below `maxIterations`, all combinations are tested exhaustively. Otherwise, randomized testing is used to keep execution time reasonable.


## Setup

```kotlin
implementation("io.github.adokky:equals-tester:1.1.0")
```

## This tests

### Basic Equality Checks

* An object is equal to itself.
* Comparing an object to `null` returns `false`.
* Comparing an object to an instance of an incompatible class returns `false`.
* Objects within the same group are equal to each other.
* Objects from different groups are not equal.
* Equal objects have the same hash code.
* Multiple calls to `hashCode()` return consistent results.

### `Comparable` Checks

* A `Comparable` object compared to itself returns `0`.
* Objects within the same group return `0` when compared.
* Objects from different groups return non-zero values.
* Ensures consistent ordering across all equality groups.

### Collection Contract Checks

For objects implementing `List`, `Map`, or `Set`:
* Verifies that the hash code matches the value defined by the collection contract.
* Ensures iteration over elements produces a collection equal to the original.

### `toString` Checks

If `checkToString` is enabled:
* Objects within the same group have equal `toString()` results.
* Objects from different groups have different `toString()` results.