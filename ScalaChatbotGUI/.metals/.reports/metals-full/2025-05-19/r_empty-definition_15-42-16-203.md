error id: file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT1/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala:`<none>`.
file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT1/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala
empty definition using pc, found symbol in pc: `<none>`.
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -scalafx/Includes.Future.
	 -scalafx/Includes.Future#
	 -scalafx/Includes.Future().
	 -scala/concurrent/Future.
	 -scala/concurrent/Future#
	 -scala/concurrent/Future().
	 -scala/concurrent/duration/Future.
	 -scala/concurrent/duration/Future#
	 -scala/concurrent/duration/Future().
	 -Future.
	 -Future#
	 -Future().
	 -scala/Predef.Future.
	 -scala/Predef.Future#
	 -scala/Predef.Future().
offset: 5490
uri: file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT1/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotGUI.scala
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
import scalafx.scene.control.ScrollPane
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps


object ChatbotGUI extends JFXApp3 {

  var isDarkMode = false
  var chatScrollPane: ScrollPane = _
  var chatBox: VBox = _ 
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

  def addMessageBubble(text: String, isUser: Boolean): Unit = {
    val bubble = new Label(text) {
      maxWidth = 400
      wrapText = true
      padding = Insets(10)
      style =
        if (isUser)
          if (isDarkMode)
            "-fx-background-color: #4A6C4F;" +
            " -fx-background-radius: 16px;" +
            " -fx-font-size: 14px;" +
            " -fx-text-fill: #fff;" +
            " -fx-border-radius: 16px;"
          else
            "-fx-background-color: #DCF8C6;" +
            " -fx-background-radius: 16px;" +
            " -fx-font-size: 14px;" +
            " -fx-text-fill: #222;" +
            " -fx-border-radius: 16px;"
          
        else
          (if (isDarkMode)
            "-fx-background-color: #333C43; -fx-background-radius: 16px; -fx-font-size: 14px; -fx-text-fill: #fff; -fx-border-radius: 16px;"
          else
            "-fx-background-color: #FFFFFF; -fx-background-radius: 16px; -fx-font-size: 14px; -fx-text-fill: #222; -fx-border-radius: 16px;")      
      }
    val hbox = new HBox {
      children = bubble
      alignment = if (isUser) Pos.CenterRight else Pos.CenterLeft
      padding = Insets(4, 0, 4, 0)
    }
    Platform.runLater {
      chatBox.children.add(hbox)
      Future {
        Thread.sleep(30)
        Platform.runLater {
          chatScrollPane.vvalue = 1.0
        }
      }
    }
  }

  def addAnimatedBotBubble(fullText: String): Unit = {
    val bubble = new Label("") {
      maxWidth = 400
      wrapText = true
      padding = Insets(10)
      style = 
        if (isDarkMode)
          "-fx-background-color: #333C43;" +
          " -fx-background-radius: 16px;" +
          " -fx-font-size: 14px;" +
          " -fx-text-fill: #fff;" +
          " -fx-border-radius: 16px;"
        else
          "-fx-background-color: #FFFFFF;" +
          " -fx-background-radius: 16px;" +
          " -fx-font-size: 14px;" +
          " -fx-text-fill: #222;" +
          " -fx-border-radius: 16px;"
    }
    val hbox = new HBox {
      children = bubble
      alignment = Pos.CenterLeft
      padding = Insets(4, 0, 4, 0)
    }
    Platform.runLater {
      chatBox.children.add(hbox)
      chatScrollPane.vvalue = 1.0
    }
    @@Future {
      for (i <- 1 to fullText.length) {
        val partial = fullText.substring(0, i)
        Platform.runLater {
          bubble.text = partial
          chatScrollPane.vvalue = 1.0
        }
        Thread.sleep(18)
      }
    }
  }

  def showBotTypingBubble(): HBox = {
    val typingLabel = new Label("Bot is Thinking...") {
      maxWidth = 400
      wrapText = true
      padding = Insets(10)
      style = "-fx-background-color: #FFFFFF; -fx-background-radius: 16px; -fx-font-size: 14px; -fx-text-fill: #888; -fx-border-radius: 16px; -fx-font-style: italic;"
    }
    val hbox = new HBox {
      children = typingLabel
      alignment = Pos.CenterLeft
      padding = Insets(4, 0, 4, 0)
    }
    Platform.runLater {
      chatBox.children.add(hbox)
      chatScrollPane.vvalue = 1.0
    }
    hbox
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

    val darkSend = new ImageView(new Image(getClass.getResource("/dark send.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }

    val sendIcon = new ImageView(new Image(getClass.getResource("/send.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }
    
    val darkclear = new ImageView(new Image(getClass.getResource("/dark clear.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }

    val clearIcon = new ImageView(new Image(getClass.getResource("/clear.png").toExternalForm)) {
      fitWidth = 24
      fitHeight = 24
      preserveRatio = true
    }

    chatBox = new VBox {
      spacing = 4
      padding = Insets(8)
    }

    chatScrollPane = new ScrollPane {
      content = chatBox
      prefHeight = 400
      prefWidth = 600
      fitToWidth = true
      style = "-fx-background: transparent; -fx-background-color: transparent;"
      vbarPolicy = ScrollPane.ScrollBarPolicy.AsNeeded
      hbarPolicy = ScrollPane.ScrollBarPolicy.Never
    }

    def processInput(): Unit = {
      val inputText = userInput.text.value.trim
      if (inputText.nonEmpty) {
        addMessageBubble(s"You: $inputText", isUser = true)
        
        val farewellKeywords = List(
          "exit", "bye", "goodbye", "quit", "see ya", "later", "farewell",
          "end session", "log off", "sign out", "terminate", "im done", "i'm done"
        )
        if (farewellKeywords.exists(word => inputText.toLowerCase.contains(word))) {
          addMessageBubble("Goodbye! Happy learning!", isUser = false)
          userInput.editable = false
          sendButton.disable = true
          toggleThemeButton.disable = true
          new Thread(() => {
            Thread.sleep(1000)
            System.exit(0)
          }).start()
          return
        }

        val (botResponse, newState) = ChatbotEngine.handleUserInput(inputText, chatbotState, explanations, quizBank)        
        val typingBubble = showBotTypingBubble()

        Future {
          Thread.sleep(250)
          Platform.runLater {
            chatBox.children.remove(typingBubble)
            botResponse.split("\n\n").foreach(line => addAnimatedBotBubble(line))
          }
        }
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
      graphic = darkIcon
      tooltip = "Toggle Theme"
      style = "-fx-background-color: transparent; -fx-padding: 0;"
      onAction = _ => {
        isDarkMode = !isDarkMode
        if (isDarkMode) {
          applyDarkTheme()
          toggleThemeButton.graphic = lightIcon
          sendButton.graphic = darkSend
          clearButton.graphic = darkclear
        } else {
          applyLightTheme()
          toggleThemeButton.graphic = darkIcon
          sendButton.graphic = sendIcon
          clearButton.graphic = clearIcon
        }
      }
      prefHeight = 40
      maxHeight = 40
      minHeight = 40
    }
    
    clearButton = new Button {
      graphic = clearIcon
      tooltip = "Clear Chat"
      style = "-fx-background-color: transparent; -fx-padding: 0;"
      onAction = _ => {
        chatBox.children.clear() 
        addMessageBubble("Welcome back to the Language Functional Chatbot!", isUser = false)
        val quizOpt = chatbotState._3 
        if (quizOpt.isDefined) {
          chatbotState = chatbotState.copy(_3 = None) 
        }
      }
      prefHeight = 40
      maxHeight = 40
      minHeight = 40
    }


    val inputRow = new HBox(10, userInput, sendButton, toggleThemeButton, clearButton){
      alignment = Pos.CenterLeft
    }

    titleLabel = new Label("Language Functional Chatbot")

    layoutRoot = new VBox(10, titleLabel, chatScrollPane, inputRow)
    layoutRoot.padding = Insets(15)
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
    addMessageBubble("Welcome to the Language Functional Chatbot!", isUser = false)
    addMessageBubble(s"${ChatbotEngine.greetUser()}", isUser = false)

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