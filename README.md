# CoNuS
CoNuS stands for "Concurrent Numerical Simulations". As it names implies, CoNuS is a generic library for numerical modelling in Scala. It is part of the <b>Carbonate Shells</b> numerical toolset. CoNuS is currently experimental software, with active testing and coding currently in progress within <a href="http//www.carbonateresearch.com">Cédric John's research group</a>.

The latest version of the library is 0.2.3, running on Scala versions 2.12 and 2.13

## Content
<b>[Introduction to CoNuS](#Introduction-to-CoNuS)</b>   
  [The CoNuS modelling philosophy](#the-conus-modelling-philosophy)
  
[Using CoNuS](#using-conus)  
  [CoNuS with SBT](#sbt)  
  [CoNuS with Jupyter Notebooks](#Jupyter-Notebook-with-Almondsh)<cr>

## Introduction to CoNuS

### The CoNuS modelling philosophy

Stepwise simulation can be defined as the art of modeling a system and its changing states by dividing the modelling space into discrete 'steps'. There is often (but not always) an implicit notion of time associated with the steps, i.e. each step represents an increment in time to either later (forward models) or earlier (inverse models) time.

Stepwise simulation is a very general modelling approach that can be applied to any natural or manmade systems, such as for instance weather patterns, ocean circulation, population dynamic, financial markets, disease propagation, systems engineering, electrical grid supply and demand, to name just a fiew.

Stepwise simulation packages can be divided into two broad categories: professional packages and user code. Professional packages come with all the belts and whistles, they usually run on optimised code to maximize CPU/GPU efficiency with the most appropriate algorithm, and they typically have a nice GUI. However, professional code have their limitations too. They can be expensive, and as a user you have no control on the mathematical model used in your calculator. Also, only certain specific applications are possible with a given professional system: for instance, a stepwise simulator dedicated to model disease conrol will not be able to handle applications to weather patterns. So if a given dedicated code does not exist for your application, or if you want to slightly modify the modelling approach in your systen, chances are you won't be able to do it.

At the opposite end of this approach is pure user code: you can choose the programming language of your choice (typically C/C++, MATLAB, Python...) and implement the entire system in this language. This means you will have complete control on the mathematical model applied in your simulator, but you will also be respondible for implementing the simulator, i.e. design a grid system for your simulator, design an event loop that will run through the  steps in your model one by one and store the state of your system at each simulation step, and of course, design the testing strategy that will allow you to assess how well your model has performed. In my experience as a researcher, this is very prone to error, inefficiency in executing your code, and of course, it means reinventing the wheel for each new problem you want to solve with a stepwise modelling approach.

CoNuS offers an alternative model for running stepwise models (Figure 1(#conus-architecture)).


![CoNuS architecture](https://user-images.githubusercontent.com/25725554/89734485-39336900-da54-11ea-9a6b-8b5463bda7be.png)
<b>Figure 1:</b> Architecture of CoNuS

![conusExcecution](https://user-images.githubusercontent.com/25725554/89734490-3fc1e080-da54-11ea-962e-6845a38f2b98.png)

## Using CoNuS 

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

