error id: file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala:`<none>`.
file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -scalafx/Includes.getClass.getResource.
	 -scalafx/Includes.getClass.getResource#
	 -scalafx/Includes.getClass.getResource().
	 -getClass/getResource.
	 -getClass/getResource#
	 -getClass/getResource().
	 -scala/Predef.getClass.getResource.
	 -scala/Predef.getClass.getResource#
	 -scala/Predef.getClass.getResource().
offset: 3701
uri: file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala
text:
```scala
import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.control.{TextArea, TextField, Button, Label}
import scalafx.scene.layout.{VBox, HBox}
import scalafx.geometry.Insets
import scalafx.Includes._
import scalafx.scene.image.{Image, ImageView}
import scalafx.application.Platform
import scalafx.geometry.Pos


object ChatbotGUI extends JFXApp3 {

  var isDarkMode = false

  var chatArea: TextArea = _
  var userInput: TextField = _
  var sendButton: Button = _
  var toggleThemeButton: Button = _
  var layoutRoot: VBox = _
  var clearButton: Button = _
  var titleLabel: Label = _


  def applyLightTheme(): Unit = {
    titleLabel.setStyle(
      "-fx-text-fill: #222222;" +
      "-fx-font-size: 20px;" +
      " -fx-font-weight: bold;"
    )

    layoutRoot.setStyle(
      "-fx-background-color: #f5f5f5;" +
      "-fx-font-family: 'Segoe UI';" +
      "-fx-font-size: 14px;"
    )
    chatArea.setStyle(
      "-fx-control-inner-background: #ffffff;" +
      "-fx-text-fill: #000000;" +
      "-fx-border-color: #cccccc;" +
      "-fx-border-radius: 6px;" +
      "-fx-background-radius: 6px;"
    )
    userInput.setStyle(
      "-fx-background-radius: 6px;" +
      "-fx-border-color: #cccccc;" +
      "-fx-text-fill: #000000;" +
      "-fx-background-color: white;"
    )
    sendButton.setStyle(
      "-fx-background-color: #4CAF50;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;"
    )
    toggleThemeButton.setStyle(
      "-fx-background-color: #607D8B;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;" +
      "-fx-padding: 8px 16px;"
    )

    clearButton.setStyle(
      "-fx-background-color: #e57373;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;" +
      "-fx-padding: 8px 16px;"
    )
  }

  def applyDarkTheme(): Unit = {
    titleLabel.setStyle(
      "-fx-text-fill: #ffffff;" +
      " -fx-font-size: 20px;" +
      " -fx-font-weight: bold;"
    )

    layoutRoot.setStyle(
      "-fx-background-color: #2b2b2b;" +
      "-fx-font-family: 'Segoe UI';" +
      "-fx-font-size: 14px;"
    )
    chatArea.setStyle(
      "-fx-control-inner-background: #3c3f41;" +
      "-fx-text-fill: #ffffff;" +
      "-fx-border-color: #555555;" +
      "-fx-border-radius: 6px;" +
      "-fx-background-radius: 6px;"
    )
    userInput.setStyle(
      "-fx-background-radius: 6px;" +
      "-fx-border-color: #777777;" +
      "-fx-text-fill: #ffffff;" +
      "-fx-background-color: #2b2b2b;"
    )
    sendButton.setStyle(
      "-fx-background-color: #6a8759;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;"
    )
    toggleThemeButton.setStyle(
      "-fx-background-color: #455A64;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;" +
      "-fx-padding: 8px 16px;"
    )
    
    clearButton.setStyle(
      "-fx-background-color: #d32f2f;" +
      "-fx-text-fill: white;" +
      "-fx-background-radius: 6px;" +
      "-fx-padding: 8px 16px;"
    )    
  }

  override def start(): Unit = {
    val explanations = ChatbotEngine.loadExplanationsFromFile()
    val quizBank = ChatbotEngine.globalQuizBank
    
    var chatbotState = ChatbotEngine.handleUserInput("hello", ChatbotEngine.runInitialState(), explanations, quizBank)._2
    
    val darkIcon = new ImageView(new Image(getClass.getResource("/moon.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }

    val lightIcon = new ImageView(new Image(getClass.getResource("/sun.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }

    val sendIcon = new ImageView(new Image(getClass.getResource@@("/send.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }
    
    val clearIcon = new ImageView(new Image(getClass.getResource("/clear.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }
    
    def processInput(): Unit = {
      val inputText = userInput.text.value.trim
      if (inputText.nonEmpty) {
        chatArea.appendText(s"\nYou: $inputText\n")
        
        val farewellKeywords = List(
          "exit", "bye", "goodbye", "quit", "see ya", "later", "farewell",
          "end session", "log off", "sign out", "terminate", "im done", "i'm done"
        )
        if (farewellKeywords.exists(word => inputText.toLowerCase.contains(word))) {
          chatArea.appendText("Bot: Goodbye! Happy learning!\n")
          userInput.editable = false
          sendButton.disable = true
          toggleThemeButton.disable = true
          new Thread(() => {
            Thread.sleep(500)
            System.exit(0)
          }).start()
          return
        }

        val (botResponse, newState) = ChatbotEngine.handleUserInput(inputText, chatbotState, explanations, quizBank)
        chatArea.appendText(s"Bot: $botResponse\n")
        chatbotState = newState
        userInput.clear()
      }
    }

    chatArea = new TextArea {
      editable = false
      wrapText = true
      prefHeight = 400
      prefWidth = 600
      text.onChange { (_, _, _) =>
        scrollTop = Double.MaxValue
      }
    }

    userInput = new TextField {
      promptText = "Type your message..."
      prefWidth = 500
      prefHeight = 40
    }

    sendButton = new Button {
      graphic = sendIcon
      tooltip = "Send"
      style = "-fx-background-color: transparent; -fx-padding: 0; -fx-background-radius: 0;"
      onAction = _ => processInput()
      prefHeight = 40
      maxHeight = 40
      minHeight = 40
    }      

    toggleThemeButton = new Button {
      graphic = themeIcon
      tooltip = "Toggle Theme"
      style = "-fx-background-color: transparent; -fx-padding: 0;"
      onAction = _ => {
        isDarkMode = !isDarkMode
        if (isDarkMode) applyDarkTheme() else applyLightTheme()
      }
      prefHeight = 40
      maxHeight = 40
      minHeight = 40
    }
    
    clearButton = new Button {
      graphic = clearIcon
      tooltip = "Clear Chat"
      style = "-fx-background-color: transparent; -fx-padding: 0;"
      onAction = _ => chatArea.clear()
      prefHeight = 40
      maxHeight = 40
      minHeight = 40
    }

    val inputRow = new HBox(10, userInput, sendButton, toggleThemeButton, clearButton){
      alignment = Pos.CenterLeft
    }

    titleLabel = new Label("Language Functional Chatbot")

    layoutRoot = new VBox(10, titleLabel, chatArea, inputRow)
    layoutRoot.padding = Insets(15)
    VBox.setMargin(chatArea, Insets(10, 0, 10, 0))
    VBox.setMargin(inputRow, Insets(0, 0, 10, 0))

    sendButton.onAction = _ => processInput()
    userInput.onAction = _ => processInput()

    stage = new JFXApp3.PrimaryStage {
      title = "CTRL ALT ELITE"
      icons += new Image(getClass.getResource("/icon.png").toExternalForm)
      scene = new Scene {
        root = layoutRoot
      }      
    }
    applyLightTheme()
    chatArea.appendText("Bot: Welcome to the Language Functional Chatbot!\n")
    chatArea.appendText(s"Bot: ${ChatbotEngine.greetUser()}\n")

    Platform.runLater {
      stage.alwaysOnTop = true
      stage.toFront()
      Thread.sleep(100)
      stage.alwaysOnTop = false
    }
  }
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: `<none>`.