package diagenesim

  import scalafx.Includes._
  import scalafx.application.JFXApp
  import scalafx.scene.Scene
  import scalafx.scene.control.Label
  import scalafx.scene.layout.BorderPane
  import scalafx.geometry.Insets

  object DiageneSim extends JFXApp {
    stage = new JFXApp.PrimaryStage {
      title.value = "DiageneSim"
      width = 600
      height = 450
      scene = new Scene {
        root = new BorderPane {
          padding = Insets(25)
          center = new Label("Hello SBT")
        }

      }
    }
  }

