
# Kotlin-MPP Collection utils

MPP re-implementation of some collections as `inline` classes.

[![](https://jitpack.io/v/Miha-x64/Kotlin-MPP_Collection_utils.svg)](https://jitpack.io/#Miha-x64/Kotlin-MPP_Collection_utils)
 ```
repositories {
    maven { url 'https://jitpack.io' }
}
...
implementation 'com.github.Miha-x64:Kotlin-MPP_Collection_utils:1.0-alpha05'
// or
implementation 'com.github.Miha-x64:Kotlin-MPP_Collection_utils-(js|jvm|linux):1.0-alpha05'
```

* Array & List with non-reified element type `Arr<E>`:
  `emptyArr()`, `arrOf(...)`, `arrOfNulls(size)`, `Arr(size, init)`; `Arr.map`, `Arr.mapIndexed`

* Immutable EnumSet `InlineEnumSet`:
  `noneOf<E>`, `allOf<E>`, `E.asSet`, `(E | set) + (E | set)`;
  `set1 intersect set2`, `set1 subtract set2`, `set1 union set2` 
  
* Mutable EnumMap `InlineEnumMap`:
  `enumMapOf(...)`


`Arr` implements List and could be boxed if necessary for interop with collection-based API.
Other collections do not implement any interfaces and should be used 'as is'
