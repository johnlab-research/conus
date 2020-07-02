# CoNuS
CoNuS stands for "Concurrent Numerical Simulations". As it names implies, CoNuS is a generic library for numerical modelling in Scala. It is part of the <b>Carbonate Shells</b> numerical toolset. CoNuS is currently experimental software, with active testing and coding currently in progress within <a href="http//www.carbonateresearch.org">Cédric John's research group</a>.

The latest version of the library is 0.2.0, running on Scala 2.13

## Using CoNuS

### SBT

For SBT, add these lines to your SBT project definition:

```scala
libraryDependencies  ++= Seq(
  // Last  release
  "org.carbonateresearch" %% "conus" % "0.2.0"
)
```

### Jupyter Notebook with Almond.sh

The preferred and easiest way to work with forward models using CoNuS is via notebooks. We tested CoNuS with <a href="https://almond.sh/versions">Almond v.0.9.1</a>. To use CoNuS within a Jupyter Notebook with the Almond kernel, first add the following resolver:

```scala
interp.repositories() ++= Seq(coursierapi.MavenRepository.of(
"https://jitpack.io"
))
```
Then import the CoNuS library:

```scala
import $ivy. `org.carbonateresearch::conus:0.2.0`
```
All of the basic classes you need to work with stepped models can be imported with this wildcard import:

```scala
import org.carbonateresearch.conus._
```
Finally, you need to run this command to set the graphic output of the library to work correctly with Almond:
```scala
Simulator.almond_display
```
