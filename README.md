# CoNuS
CoNuS stands for "Concurrent Numerical Simulations". As it names implies, CoNuS is a generic library for numerical modelling in Scala. It is part of the <b>Carbonate Shells</b> numerical toolset. CoNuS is currently experimental software, with active testing and coding currently in progress within <a href="http//www.carbonateresearch.com">Cédric John's research group</a>.

The latest version of the library is 0.2.3, running on Scala versions 2.12 and 2.13

## Content
[Usning CoNuS](#ausing-conus)

## Using CoNuS 
(#using-conus)
### SBT

For SBT, add these lines to your SBT project definition:

```scala
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies  ++= Seq(
  // Last  release
  "org.carbonateresearch" %% "conus" % "0.2.3"
)
```
To access the simulator, simply create an interface to the BasicSimulator:
```scala
val sim = new BasicSimulator
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
import $ivy. `org.carbonateresearch::conus:0.2.3`
```
All of the basic classes you need to work with stepped models can be imported with this wildcard import:

```scala
import org.carbonateresearch.conus._
```
Finally, you need to create an interface to the simulator, which is the object you will use to run your code. For the Almond kernel, create an AlmondSimulator this way:
```scala
val sim = new AlmondSimulator
```
This will add nice graphical output to your cells, and will ensure that CoNuS integrates well with the Jupyter notebook. Once you have created a model definition, you will use your simulator interface to run and fetch results like this:
```scala
sim.evaluate(myModel)
sim.getResults(myModel)
sim.save(myModel)
```
## Basic principle of CoNuS
### Focus user code on the mathematical model

Stepwise simulation can be defined as the art of modeling a system and its changing states at discrete 'steps'. There is often (but not always) an implicit notion of time associated with the steps, as in each step represents a new time either later (forward models) or earlier (inverse models) in time.

Stepwise simulation is not new

![CoNuS architecture](https://user-images.githubusercontent.com/25725554/89734485-39336900-da54-11ea-9a6b-8b5463bda7be.png)
<b>Figure 1:</b> Architecture of CoNuS

![conusExcecution](https://user-images.githubusercontent.com/25725554/89734490-3fc1e080-da54-11ea-962e-6845a38f2b98.png)
