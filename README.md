# CoNuS
CoNuS stands for "Concurrent Numerical Simulations". As it names implies, CoNuS is a generic library for numerical modelling in Scala. CoNuS is currently experimental, with active testing and coding currently in progress within Cédric John's research group: http//www.carbonateresearch.org 

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

The preferred and easiest way to work with forward models using CoNuS is via notebooks. To use CoNuS within a Jupyter Notebook with the Almond kernel, first add the following resolver:

```scala
interp.repositories() ++= Seq(coursierapi.MavenRepository.of(
"https://jitpack.io"
))
```
Then import the CoNuS library:

```scala
import $ivy. `org.carbonateresearch::conus:0.2.0`
```

You can import all of the useful classes simply by running the following:

```scala
import org.carbonateresearch.conus._
```
Followed by the following command to set the graphic output to nicely print on Almond:
```scala
Simulator.almond_display
```
