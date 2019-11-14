
# Kotlin MPP Collection utils

MPP re-implementation of some collections as `inline` classes.

 [![Download](https://api.bintray.com/packages/miha-x64/maven/Collection-utils/images/download.svg) ](https://bintray.com/miha-x64/maven/Collection-utils/_latestVersion)
 ```
implementation 'net.aquadc.collections:Collection-utils:1.0-alpha01'
implementation 'net.aquadc.collections:Collection-utils-(js|jvm|linux):1.0-alpha01'
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
