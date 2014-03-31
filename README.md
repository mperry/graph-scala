Graph algorithms in Scala
=====================

Shortest path problem in Scala

I used:

* Gradle 1.11
* Intellij Community Edition 13.1
* Scala 2.10.3
* Java 7
* Windows 7

To import into Intellij `File? -> Import Project -> select build.gradle -> Use local gradle distribution 1.11 -> Right click root folder -> Add frameworks support -> local Scala 2.10.3 home`

To build using Gradle (build and running the tests): `gradle`

To run using the file input.json for the graph: `gradle run -q -Pargs=input.json`

An example session, where my comments are between "[" and "]" is:
```
> { "start":"A", "end":"F" }
{"distance":360}
> { "A": {"B":80} }
> { "start":"A", "end":"F" }
{"distance":360}
[ no change to distance, lets reduce the shortest path now]
> { "A": {"C":20} }
> { "start":"A", "end":"F" }
{"distance":350}
[ now make another path shorter using A -> B -> F using the distance 80 above and then 20]
> { "B": {"F":20} }
> { "start":"A", "end":"F" }
{"distance":100}
```

Output for a session can be captured using standard redirection: `gradle run -q -Pargs=input.json <in.txt >out.txt`

Note that:
* deleting paths is not implemented as it was unclear from the description how the interface for this would work.
* Cannot use as input: { "map": [{ "A": {"I":70, "J":150} }]}
