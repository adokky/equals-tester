Tester for `equals()`, `hashCode()`, `compareTo`, `toString` methods of a class. Similar to Guava [EqualsTester](https://www.javadoc.io/doc/com.google.guava/guava-testlib/21.0/com/google/common/testing/EqualsTester.html) but in pure Kotlin and with more advanced checks.

Example:

```kotlin
testEquality {
    group(User("alex"), User("alex"), User("alex"))
    group(User("sergey"), User("sergey"))

    // User("page") created multiple times (default is 3).
    // Can be changed via testEquality(defaultGroupSize = N).
    group { User("page") }
}
```

* Each group should contain objects that are equal to each other but unequal to the objects in any other group.
* Adding more than one group is not required but strongly recommended.

## Setup

```kotlin
implementation("io.github.adokky:equals-tester:0.11")
```

## This tests

Base:
* comparing each object against itself returns true
* comparing each object against null returns false
* comparing each object against an instance of an incompatible class returns false
* comparing each pair of objects within the same equality group returns true
* comparing each pair of objects from different equality groups returns false
* the hash codes of any two equal objects are equal

`Comparable` checking:
* comparing every `Comparable` object against itself returns 0
* comparing every `Comparable` object against objects within the same equality group returns 0
* comparing every `Comparable` object against objects from different equality group returns non-zero
* consistent order of all equality groups 

For every object that implements `List`, `Map`, `Set`:
* check hash code value exactly matches the computed value described in specific collection contract
* check iteration over all elements yields the collection that equals to the original

If `checkToString` set to `true`:
* comparing each pair of object's `toString()` within the same equality group returns true
* comparing each pair of object's `toString()` from different equality groups returns false