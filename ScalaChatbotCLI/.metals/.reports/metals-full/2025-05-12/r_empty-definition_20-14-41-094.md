error id: scala/collection/immutable/List.empty().
file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
empty definition using pc, found symbol in pc: scala/collection/immutable/List.empty().
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -java/io/List.empty.
	 -java/io/List.empty#
	 -java/io/List.empty().
	 -List.empty.
	 -List.empty#
	 -List.empty().
	 -scala/Predef.List.empty.
	 -scala/Predef.List.empty#
	 -scala/Predef.List.empty().
offset: 32501
uri: file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
text:
```scala
import java.io._
import scala.io.Source
import scala.io.StdIn // Make StdIn explicit
import scala.util.Try
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.boundary, boundary.break 


val baseDir = "../data/"
val explanationsPath = s"${baseDir}explanations.csv"
val questionsPath = s"${baseDir}questions.csv"
val userInteractionsLogFilePath = s"${baseDir}user_interactions_log.txt"

//Response Templates
val responseTemplates = List(
    "For {concept}, check this out: {response}",
    "Let's break {concept} down for you: {response}",
    "Aight, let's talk about {concept}: {response}",
    "So you wanna know about {concept}? Let me help with that: {response}",
    "Okay, so basically {concept} is {response}",
    "Let's unpack {concept} together: {response}",
    "Let me walk you through {concept}: {response}"
)
val followUpTemplates = List(
    "No worries, let's go over {concept} one more time: {response}",
    "Okay, let's show a different angle on {concept}: {response}",
    "Still fuzzy on {concept}? Here's another way to look at it: {response}",
    "Alright, don't worry, let's dig deeper on {concept}: {response}",
    "Got it, here's a clearer take on {concept}: {response}",
    "Cool, let's revisit {concept} for you: {response}",
    "Here's another shot at explaining {concept}: {response}"
)
val exampleTemplates = List(
    "Here's a quick example of {concept} in action: {response}",
    "Let me dig deeper on {concept} with a practical example: {response}",
    "Alrighty, check out this use case for {concept}: {response}",
    "Here's how {concept} works in a real scenario: {response}",
    "Want to see {concept} through a more practical example? Here you go: {response}",
    "Here's a simple example to illustrate {concept}: {response}",
    "For {concept}, picture this example: {response}"
)
val clarificationPrompts = List(
    "Does that make sense, or would you like more clarification?",
    "Got it so far? Need a bit more explanation or an example?",
    "Is that clear, or should I dive deeper into {concept}?",
    "How's that explanation? Want to explore {concept} further?",
    "Feeling good about {concept}? I can provide more details or an example if you'd like!"
)
val suggestionPrompts = List(
    "Not sure what to do next? You can try 'quiz python', 'explain encapsulation', or ask about another topic!",
    "What's next? I can explain a concept, start a quiz, or we can explore something new!",
    "Need ideas? How about a quiz on 'javascript' or learning about 'inheritance'?",
    "You can ask me to explain a concept, start a quiz, or type 'exit' to leave!"
)
//Keywords
val greetingKeywords = List(
  "hi", "hello", "hey", "greetings", "start", "hola", "good morning",
  "good afternoon", "good evening", "what's up", "sup", "yo", "howdy",
  "hiya", "let's begin", "begin"
)
val quizKeywords = List(
  "quiz", "test", "challenge", "exam", "assessment", "questions", "practice",
  "check my knowledge", "test me", "quiz me", "give me a quiz", "start quiz",
  "pop quiz"
)
val explainKeywords = List(
  "explain", "what is", "what's", "define", "definition of", "tell me about",
  "describe", "how does", "how do", "why is", "why does", "details on",
  "more info on", "elaborate on", "clarify", "what are", "what do"
)
val farewellKeywords = List(
  "exit", "bye", "goodbye", "quit", "see ya", "later", "farewell",
  "end session", "log off", "sign out", "terminate", "im done", "i'm done"
)
val analyticsKeywords = List(
  "analytics", "stats", "statistics", "usage", "performance", "report",
  "show my progress", "how am i doing", "my stats", "chatbot usage",
  "analyze my quiz"
)
val languageKeywords = List("cpp", "c++", "javascript", "js", "python", "py", "scala", "java", "c", "clang", "go", "golang")

val conceptKeywords = List(
    "algorithm", "scala", "class", "arrays", "variables", "dsa", "oop", "loops", "inheritance", "polymorphism",
    "closure", "list", "array", "interface", "function", "pointer", "dictionary", "abstraction", "virtual function", "module", "prototype",
    "structure", "lambda function", "encapsulation", "template", "set", "linked list", "exception handling", "list comprehension", "overloading",
    "callback", "generator", "abstract class", "stack", "tuple", "promise", "destructor", "method overriding", "queue", "file i/o",
    "arrow function", "stl", "dynamic memory allocation", "regular expression", "map", "graph", "filter", "exception", "constructor",
    "binary tree", "class template", "global variable", "polymorphic method", "heap", "string formatting", "friend function", "overloaded constructor",
    "hash table", "spread operator", "multithreading", "avl tree", "deque", "tree", "zip function", "trie",
    "lambda expression", "binary search", "dataframe", "annotation", "unit testing", "type inference", "numpy array",
    "garbage collection", "sorting", "api", "disjoint set", "enum", "kruskal's", "metaclass", "fenwick tree", "class method", "huffman coding",
    "multiprocessing", "reflection", "bellman ford", "hash function", "segment tree", "floyd warshall", "lru cache", "websocket", "immutability",
    "dataclasses", "bigint", "fold expression", "memory management", "divide and conquer", "pattern matching", "sealed class", "backtracking",
    "bit manipulation", "server-side rendering", "dynamic programming", "refactoring", "scalability", "memory leak", "code reusability", "microservices",
    "iterator", "debugging", "data compression", "data encryption", "graphql", "data visualization", "rest api", "data cleaning",
    "automation", "authorization", "logic programming", "accessibility", "seo optimization", "code validation", "machine learning",
    "reinforcement learning", "data security", "framework", "greedy algorithms", "recurrence relations", "big o notation", "relational databases",
    "sql", "tables", "primary keys", "foreign keys", "indexes", "normalization", "denormalization", "joins", "inner join", "left join", "right join",
    "full join", "queries", "select statement", "insert statement", "update statement", "delete statement", "transactions", "nosql",
    "graph databases", "mongodb", "html", "css", "dom", "ajax", "json", "xml", "apis",
    "rest", "soap", "http methods", "status codes", "frontend", "backend", "frameworks", "react", "angular", "vue js", "node js", "express js",
    "django", "flask", "cookies", "sessions", "websockets", "sdlc", "agile methodology", "scrum", "waterfall model", "version control", "git",
    "github", "commits", "branches", "merging", "pull requests", "continuous integration", "continuous deployment", "testing", "procedural programming",
    "declarative programming", "imperative programming", "concurrent programming", "parallel programming", "tcp ip", "http", "https", "dns",
    "ip address", "ports", "sockets", "firewalls", "load balancers", "pure functions", "function composition", "partial application", "tail recursion", "memoization", "closures", "currying", "higher order functions",
    "time complexity", "space complexity"
).map(_.toLowerCase).distinct 

//Type Aliases for Readability 
type ExplanationData = (String, String, String, String) // explanation, explanation2, example, note
type QuizQuestion = (String, List[String], String, String, String) // questionText, choices, correctAnswerText, generalNote, questionExplanation
type QuizBank = Map[String, List[QuizQuestion]] // Topic -> List of questions

type InteractionLog = List[(Int, String, String)] // seqNum, userInput, chatbotResponse
type QuizSessionState = (String, List[QuizQuestion], List[Boolean], Int, List[QuizQuestion]) // topic, remainingQuestions, answersSoFar (bools), currentQuestionIndex, missedQuestionsAccumulator (full QuizQuestion)
type UserPreferences = Map[String, String] // e.g., "favTopic" -> "scala"

type LastActionContext = Option[(String, String)]

type ChatbotState = (InteractionLog, Int, Option[QuizSessionState], UserPreferences,LastActionContext) // log, nextSeqNum, currentQuizState, preferences



// --- Keyword Variations (Initialized once) ---
val keywordVariations: Map[String, List[String]] = {
  conceptKeywords.map { concept => // concept is already lowercase here
    val canonical = concept
    var variations = List(canonical)
    if (!canonical.endsWith("s") && !canonical.contains(" ")) variations = (canonical + "s") :: variations
    if (canonical.endsWith("s") && !canonical.contains(" ")) variations = canonical.stripSuffix("s") :: variations
    val specialCases = Map( // Keys should be lowercase
      "python" -> List("Python", "py"), "loop" -> List("loop", "loops"),
      "oop" -> List("oop", "object oriented programming", "object-oriented", "object oriented"),
      "dsa" -> List("dsa", "data structures", "algorithms", "data structures and algorithms"),
      "c++" -> List("cpp", "c++", "c plus plus", "cplusplus"),
      "javascript" -> List("javascript", "js", "ecmascript"), "scala" -> List("scala"),
      "java" -> List("java"), "c" -> List("c", "clang", "c programming", "c lang", "ansi c"),
      "function" -> List("function", "functions", "func"),
      "regular expression" -> List("regular expression", "regex", "regexp"),
      "file i/o" -> List("file i/o", "file input/output", "file operations"),
      "stl" -> List("stl", "standard template library"),
      "abstract class" -> List("abstract class", "abstract classes"),
      "tcp ip" -> List("tcpip", "tcp ip"), "aws" -> List("aws", "amazon web services"),
      "golang" -> List("go", "golang", "go lang")
    )
    variations = specialCases.getOrElse(canonical, Nil).map(_.toLowerCase) ++ variations
    if (concept.contains(" ")) {
        variations = canonical.replace(" ", "") :: canonical.replace("-", " ") :: variations
    }
    canonical -> variations.distinct
  }.toMap
}

val variationToCanonical: Map[String, String] = keywordVariations.flatMap {
  case (canonical, variations) => variations.map(variation => variation -> canonical)
}

val allKeywordsForParsing = (greetingKeywords ++ quizKeywords ++ explainKeywords ++ farewellKeywords ++ analyticsKeywords ++ languageKeywords ++ conceptKeywords).map(_.toLowerCase).distinct

// --- Core Chatbot Module (Section 3.1) ---

def greetUser(): String = {
  val greetingTemplates = List(
    "Hello! I'm your Scala Functional Chatbot. Ready to learn or take a quiz?",
    "Hey there! Let's explore some programming concepts. What's on your mind?",
    "Hi! Ask me to explain a concept, a quiz on a specific topic or show some analytics.",
    "Hello, ready to learn something new today?",
    "Hey there! Let's dive into some programming concepts—what's on your mind?",
    "Hi! I'm here to help you with programming concepts. What would you like to explore?",
    "Greetings, learner! Want to quiz yourself or dive into a concept?",
    "Yo, what's up? Let's get started with some coding knowledge!"
  )
  greetingTemplates(Random.nextInt(greetingTemplates.length)) + "\n" +
  suggestionPrompts(Random.nextInt(suggestionPrompts.length))
}

def parseInput(input: String, knownKeywords: List[String] = allKeywordsForParsing): List[String] = {
  val normalizedInput = input.replaceAll("[^a-zA-Z0-9\\-_ +]", "")
                            .replaceAll("\\s+", " ")
                            .toLowerCase
                            .trim
  // Prioritize multi-word concepts by checking longest first
  val sortedConceptKeywords = conceptKeywords.sortBy(-_.length)

  val (matchedMultiWordConcepts, textAfterMultiWord) =
    sortedConceptKeywords.foldLeft((Set.empty[String], normalizedInput)) {
      case ((accConcepts, currentText), conceptKey) =>
        if (currentText.contains(conceptKey)) { // conceptKey is already lowercase
          // Use variationToCanonical to ensure we store the canonical form
          val canonical = variationToCanonical.getOrElse(conceptKey, conceptKey)
          (accConcepts + canonical, currentText.replace(conceptKey, " "))
        } else {
          (accConcepts, currentText)
        }
    }

  val singleWordTokens = textAfterMultiWord
    .split(" ")
    .filter(_.nonEmpty)
    .map(token => variationToCanonical.getOrElse(token, token))
    .filter(knownKeywords.contains)
    .toSet

  (matchedMultiWordConcepts ++ singleWordTokens).toList.distinct
}

type IntentInfo = (String, List[String], Option[String])

private def determineIntent(tokens: List[String]): IntentInfo = {
  val lowerTokens = tokens.map(_.toLowerCase)

  val isGreeting = lowerTokens.exists(greetingKeywords.contains)
  val isQuiz = lowerTokens.exists(quizKeywords.contains)
  val isExplain = lowerTokens.exists(explainKeywords.contains)
  val isFarewell = lowerTokens.exists(farewellKeywords.contains)
  val isAnalytics = lowerTokens.exists(analyticsKeywords.contains)

  val foundConcepts = lowerTokens.filter(conceptKeywords.contains)
  val foundLanguagesOrTopics = lowerTokens.filter(t => languageKeywords.contains(t) || conceptKeywords.contains(t))
                                    .map(t => variationToCanonical.getOrElse(t, t)) // Get canonical form

  val mainTopic = foundLanguagesOrTopics.headOption

  if (isQuiz) {
    ("quiz", foundConcepts.distinct, mainTopic)
  } else if (isExplain) {
    ("explain", foundConcepts.distinct, mainTopic)
  } else if (isAnalytics) {
    ("analytics", List(), None)
  } else if (isGreeting) {
    ("greeting", List(), None)
  } else if (isFarewell) {
    ("farewell", List(), None)
  } else if (mainTopic.isDefined) {
    ("explain", foundConcepts.distinct, mainTopic) // Default to explain if a topic is mentioned
  }
  else {
    ("unknown", List(), None)
  }
}

def generateExplanationResponse(
  concept: String,
  explanations: Map[String, ExplanationData]
): String = {
  variationToCanonical.get(concept.toLowerCase) match {
    case Some(canonicalConcept) =>
      explanations.get(canonicalConcept) match {
        case Some((explanation, _, _, note)) =>
          val template = responseTemplates(Random.nextInt(responseTemplates.length))
          val baseResponse = template
            .replace("{concept}", canonicalConcept)
            .replace("{response}", explanation)
          if (note.nonEmpty) baseResponse + s"\nTip: ${note}" else baseResponse
        case None =>
          s"Sorry, I don't have an explanation for '$canonicalConcept'. Available concepts: ${Random.shuffle(conceptKeywords).take(3).mkString(", ")}..."
      }
    case None => s"Sorry, I don't recognize the concept '$concept'."
  }
}

// --- Functional Chatbot Quiz Generator (Section 3.2) ---
private def loadQuizBankFromFile(filePath: String = questionsPath): QuizBank = {
  val file = new java.io.File(filePath)
  val absoluteFilePath = file.getAbsolutePath
  // println(s"--- DEBUG: Attempting to load quiz bank from ---")
  // println(s"Provided filePath: $filePath")
  // println(s"Absolute path resolved to: $absoluteFilePath")
  // println(s"Does file exist? ${file.exists()}")
  // println(s"Is it a file? ${file.isFile()}")
  // println(s"Can it be read? ${file.canRead()}")
  // println(s"------------------------------------------------")

  Try {
    val source = Source.fromFile(filePath, "UTF-8")
    val lines = source.getLines().drop(1).toList
    source.close()
    // println(s"--- DEBUG: Read ${lines.length} lines (after header) from questions.csv ---")

    lines.flatMap { line =>
      Try {
        val columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
        if (columns.length == 9) {
          val csvConcept = columns(0).trim.toLowerCase
          val canonicalConcept = variationToCanonical.getOrElse(csvConcept, csvConcept)
          val questionText = columns(1).trim.stripPrefix("\"").stripSuffix("\"")
          val choices = List(columns(2), columns(3), columns(4), columns(5))
            .map(_.trim.stripPrefix("\"").stripSuffix("\""))
          val correctAnswer = columns(6).trim.stripPrefix("\"").stripSuffix("\"")
          val generalNote = columns(7).trim.stripPrefix("\"").stripSuffix("\"")
          val questionExplanation = columns(8).trim.stripPrefix("\"").stripSuffix("\"")
          Some(canonicalConcept -> (questionText, choices, correctAnswer, generalNote, questionExplanation))
        } else {
          // println(s"--- DEBUG: MALFORMED LINE --- Expected 9 columns, got ${columns.length} for line: [$line]")
          None
        }
      }.recover {
        case e: Exception =>
          // println(s"--- DEBUG: EXCEPTION while processing line '$line': ${e.getClass.getSimpleName} - ${e.getMessage} ---")
          None
      }.toOption.flatten
    }
    .groupBy(_._1)
    .map { case (topic, questionsWithTopic) =>
      topic -> questionsWithTopic.map(_._2)
    }
  }.getOrElse {
    println(s"Failed to load quiz bank from $filePath. Using an empty quiz bank.")
    Map.empty[String, List[QuizQuestion]]
  }
}
val globalQuizBank: QuizBank = loadQuizBankFromFile()

def selectQuizQuestions(topic: String, numQuestions: Int, quizBank: QuizBank): Option[List[QuizQuestion]] = {
  val canonicalTopic = variationToCanonical.getOrElse(topic.toLowerCase, topic.toLowerCase)
  quizBank.get(canonicalTopic).map { questions =>
    Random.shuffle(questions).take(numQuestions)
  }.filter(_.nonEmpty)
}

def presentQuizQuestion(question: QuizQuestion, questionNumber: Int): String = {
  val (questionText, choices, _, generalNote, _) = question
  val choicesString = choices.zipWithIndex.map { case (opt, idx) =>
    s"${(idx + 'a').toChar}. $opt"
  }.mkString("\n")
  val hintMessage = if (generalNote.nonEmpty) "\nType 'hint' for a note on this question." else ""
  val explainMessage = "\nType 'explain question' for an explanation (before answering)."
  s"\nQuestion ${questionNumber}: $questionText\n$choicesString$hintMessage$explainMessage\nYour answer (letter or full text): "
}

def evaluateQuizAnswer(userAnswerRaw: String, choices: List[String], correctAnswerText: String): Option[Boolean] = {
  val userAnswerProcessed = userAnswerRaw.trim.toLowerCase
  val chosenAnswerText = userAnswerProcessed match {
    case singleChar if singleChar.length == 1 && "abcd".contains(singleChar) =>
      val choiceIndex = "abcd".indexOf(singleChar)
      if (choiceIndex >= 0 && choiceIndex < choices.length) Some(choices(choiceIndex).toLowerCase)
      else None
    case fullText => Some(fullText)
  }
  chosenAnswerText.map { ansText =>
    def levenshteinDistance(s1: String, s2: String): Int = {
      val dist = Array.tabulate(s2.length + 1, s1.length + 1) { (j, i) => if (j == 0) i else if (i == 0) j else 0 }
      for (j <- 1 to s2.length; i <- 1 to s1.length)
        dist(j)(i) = if (s2(j - 1) == s1(i - 1)) dist(j - 1)(i - 1)
        else math.min(math.min(dist(j - 1)(i) + 1, dist(j)(i - 1) + 1), dist(j - 1)(i - 1) + 1)
      dist(s2.length)(s1.length)
    }
    def isSimilar(s1: String, s2: String, threshold: Double = 0.2): Boolean = {
      if (s1.trim.isEmpty || s2.trim.isEmpty) return false
      val distance = levenshteinDistance(s1, s2)
      val maxLength = math.max(s1.length, s2.length)
      if (maxLength == 0) true else (distance.toDouble / maxLength) <= threshold
    }
    ansText == correctAnswerText.toLowerCase || isSimilar(ansText, correctAnswerText.toLowerCase)
  }
}

def summarizeQuizResults(answers: List[Boolean]): String = {
  val correctCount = answers.count(identity)
  val totalCount = answers.length
  if (totalCount == 0) "No questions were answered in the quiz."
  else {
    val scorePercentage = (correctCount.toDouble / totalCount * 100).toInt
    s"Quiz finished! You scored $correctCount/$totalCount ($scorePercentage%)."
  }
}

// --- Immutable Interaction Analytics Dashboard (Section 3.3) ---
private def persistentLogInteraction(userInput: String, chatbotResponse: String, context: Option[String] = None): Unit = {
  val timestamp = LocalDateTime.now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  val logEntry = context match {
    case Some(ctx) => s"[$timestamp] Context: $ctx, User: $userInput, Chatbot: $chatbotResponse\n"
    case None => s"[$timestamp] User: $userInput, Chatbot: $chatbotResponse\n"
  }
  Try(new PrintWriter(new FileOutputStream(new File(userInteractionsLogFilePath), true)))
    .foreach { writer =>
      try writer.write(logEntry) finally writer.close()
    }
}

def logInteraction(
  userInput: String,
  chatbotResponse: String,
  currentLog: InteractionLog,
  currentSeqNum: Int
): (InteractionLog, Int) = {
  persistentLogInteraction(userInput, chatbotResponse)
  val newEntry = (currentSeqNum, userInput, chatbotResponse)
  (currentLog :+ newEntry, currentSeqNum + 1)
}

def getInteractionLog(log: InteractionLog): List[(Int, String, String)] = log

def analyzeInteractions(log: InteractionLog): String = {
  if (log.isEmpty) return "No interactions logged yet to analyze."
  val totalInteractions = log.length
  val quizRequests = log.count(_._2.toLowerCase.contains("quiz"))
  val explainRequests = log.count(_._2.toLowerCase.contains("explain"))
  s"Analytics Report:\n" +
  s"Total Interactions: $totalInteractions\n" +
  s"Requests for Quizzes: $quizRequests\n" +
  s"Requests for Explanations: $explainRequests\n"
}

def analyzeQuizPerformance(log: InteractionLog): String = {
  if (log.isEmpty) return "No interactions logged yet for quiz performance."
  val quizEndResponses = log.filter(_._3.toLowerCase.startsWith("quiz finished!"))
  if (quizEndResponses.isEmpty) return "No completed quiz sessions found in logs for performance analysis."

  // This is a simplified analysis. A real one would parse scores from responses or have structured quiz logs.
  val scores = quizEndResponses.flatMap { case (_, _, response) =>
    val scorePattern = "You scored (\\d+)/(\\d+)".r
    scorePattern.findFirstMatchIn(response).map { m =>
      (Try(m.group(1).toInt).getOrElse(0), Try(m.group(2).toInt).getOrElse(0))
    }
  }
  if (scores.isEmpty) return "Could not parse scores from completed quiz sessions."
  val totalCorrect = scores.map(_._1).sum
  val totalAttempted = scores.map(_._2).sum
  val avgAccuracy = if (totalAttempted > 0) (totalCorrect.toDouble / totalAttempted * 100).toInt else 0

  s"Overall Quiz Performance:\n" +
  s"Total Quizzes Taken (parsed): ${scores.length}\n" +
  s"Total Questions Answered (parsed): $totalAttempted\n" +
  s"Total Correct Answers (parsed): $totalCorrect\n" +
  s"Average Accuracy: $avgAccuracy%\n"
}

// --- User Preferences (Section 4.3) ---
def storeUserPreferences(key: String, value: String, currentPreferences: UserPreferences): UserPreferences = {
  currentPreferences + (key -> value)
}

def getUserPreferences(key: String, preferences: UserPreferences): Option[String] = {
  preferences.get(key)
}

// --- Main Chatbot Logic ---
def handleUserInput(
  rawInput: String,
  chatbotState: ChatbotState,
  explanations: Map[String, ExplanationData], // Renamed for clarity
  quizBank: QuizBank
): (String, ChatbotState) = {
  val (currentLog, currentSeqNum, currentQuizSessOpt, currentPrefs) = chatbotState
  val input = rawInput.trim.toLowerCase

  // Log raw input to persistent file first
  persistentLogInteraction(input, "N/A - Processing Input", Some("RAW_USER_INPUT"))

  def startNewQuiz(topic: String, numQuestions: Int = 5): (String, ChatbotState) = {
    selectQuizQuestions(topic, numQuestions, quizBank) match {
      case Some(questions) if questions.nonEmpty =>
        val firstQuestionText = presentQuizQuestion(questions.head, 1)
        val newQuizSession: QuizSessionState = (topic, questions.tail, List.empty[Boolean], 1, List.empty[QuizQuestion])
        val response = s"Let's start a quiz on $topic!\n$firstQuestionText"
        val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
        (response, (updatedLog, nextSeq, Some(newQuizSession), currentPrefs))
      case _ =>
        val availableTopics = Random.shuffle(quizBank.keys.toList).filter(_.nonEmpty).take(5).mkString(", ")
        val response = s"No questions found for '$topic'. Available topics: $availableTopics..."
        val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
        (response, (updatedLog, nextSeq, None, currentPrefs))
    }
  }

  currentQuizSessOpt match {
    case Some((topic, remainingQuestions, answersSoFar, currentQuestionNum, missedSoFar)) =>
      val allQuestionsForTopic = quizBank.getOrElse(topic, List.empty)
      val currentQuestionOpt = if (currentQuestionNum - 1 < allQuestionsForTopic.length) {
        Try(allQuestionsForTopic(currentQuestionNum - 1)).toOption // currentQuestionNum is 1-based
      } else None
      
      currentQuestionOpt match {
        case Some(currentQuestionTuple) =>
          val (qText, qChoices, qCorrectAnswer, qNote, qExplanation) = currentQuestionTuple

          if (input == "hint" && qNote.nonEmpty) {
            val response = s"Hint: $qNote\nYour answer: "
            val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
            (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs))
          } else if (input == "hint" && qNote.isEmpty) {
            val response = "No specific hint for this question.\nYour answer: "
            val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
            (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs))
          }
          else if (input == "explain question" || input == "explain") {
            val explanationText = if (qExplanation.nonEmpty) qExplanation else "No detailed explanation for this specific question."
            val response = s"Explanation: $explanationText\nYour answer (to the original question): "
            val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
            (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs))
          } else {
            evaluateQuizAnswer(input, qChoices, qCorrectAnswer) match {
              case Some(isCorrect) =>
                val feedback = if (isCorrect) "Correct!" else s"Wrong. The correct answer was: $qCorrectAnswer."
                val updatedAnswers = answersSoFar :+ isCorrect
                val updatedMissed = if (!isCorrect) missedSoFar :+ currentQuestionTuple else missedSoFar

                if (remainingQuestions.isEmpty) {
                  val summary = summarizeQuizResults(updatedAnswers)
                  val missedExplanations = updatedMissed.map { case (qt, _, ca, _, qe) =>
                    s"\nFor: \"$qt\"\nCorrect: $ca\nWhy: ${if (qe.nonEmpty) qe else "No specific explanation."}"
                  }.mkString("\n")
                  val missedHeader = if (updatedMissed.nonEmpty) "\n\nReview of questions you missed:" else ""
                  val response = s"$feedback\n$summary$missedHeader$missedExplanations\n\n${suggestionPrompts(Random.nextInt(suggestionPrompts.length))}"
                  val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
                  (response, (updatedLog, nextSeq, None, currentPrefs)) // End quiz
                } else {
                  val nextQuestionText = presentQuizQuestion(remainingQuestions.head, currentQuestionNum + 1)
                  val response = s"$feedback\n$nextQuestionText"
                  val newQuizSession = (topic, remainingQuestions.tail, updatedAnswers, currentQuestionNum + 1, updatedMissed)
                  val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
                  (response, (updatedLog, nextSeq, Some(newQuizSession), currentPrefs))
                }
              case None =>
                val response = "Invalid answer format. Please use 'a', 'b', 'c', 'd' or the full text.\n" + presentQuizQuestion(currentQuestionTuple, currentQuestionNum)
                val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
                (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs))
            }
          }
        case None =>
          val r = "Error: Could not retrieve current question. Ending quiz."
          val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs)) // End quiz due to error
      }

    case None => // No quiz in progress
      val tokens = parseInput(rawInput) // Use rawInput for parseInput
      val (intent, concepts, langOrConceptOpt) = determineIntent(tokens)

      val (responseString, newChatbotStateTuple) = intent match {
        case "greeting" =>
          val r = greetUser()
          val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))
        case "farewell" =>
          val r = "Goodbye! Happy learning!"
          val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))
        case "quiz" =>
          langOrConceptOpt match {
            case Some(topic) => startNewQuiz(topic)
            case None =>
              val r = s"Please specify a topic for the quiz (e.g., 'quiz python'). Available: ${Random.shuffle(quizBank.keys.toList).filter(_.nonEmpty).take(5).mkString(", ")}"
              val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
              (r, (l, s, None, currentPrefs))
          }
        case "explain" =>
          // The original `explanationFlow` was interactive. We simplify this.
          // A more functional way would be to make `explanationFlow` return (String, NewState)
          // and manage sub-states (e.g., "awaiting_clarification_response").
          // For now, provide initial explanation and prompt.
          langOrConceptOpt.orElse(concepts.headOption) match {
            case Some(conceptToExplain) =>
              val canonicalConcept = variationToCanonical.getOrElse(conceptToExplain.toLowerCase, conceptToExplain.toLowerCase)
              val explanationText = generateExplanationResponse(canonicalConcept, explanations)
              val prompt = if (explanations.contains(canonicalConcept)) { // Only prompt if explanation was found
                clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", canonicalConcept)
              } else ""
              val r = s"$explanationText\n$prompt"
              val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
              (r, (l, s, None, currentPrefs)) // No specific "explain" state after initial response
            case None =>
              val r = "Please specify a concept to explain (e.g., 'explain oop')."
              val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
              (r, (l, s, None, currentPrefs))
          }
        case "analytics" =>
          val interactionAnalysis = analyzeInteractions(currentLog)
          val quizAnalysis = analyzeQuizPerformance(currentLog)
          val r = s"$interactionAnalysis\n\n$quizAnalysis"
          val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))

        case "unknown" =>
          val r = "I'm not sure how to help with that. " + suggestionPrompts(Random.nextInt(suggestionPrompts.length))
          val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))
      }
      (responseString, newChatbotStateTuple)
  }
}

// --- Chatbot Execution Loop ---
def runChatbot(
  initialExplanations: Map[String, ExplanationData],
  initialQuizBank: QuizBank
): Unit = {
  println("Welcome to the Scala Functional Chatbot!")
  val initialGreeting = greetUser()
  println(initialGreeting)
  
  // Log the initial greeting as if it was a response to an implicit "start"
  val (initialLog, initialSeqNum) = logInteraction("system_start", initialGreeting, List.emp@@ty, 1)
  val initialBotState: ChatbotState = (initialLog, initialSeqNum, None, Map.empty)

  @scala.annotation.tailrec
  def loop(currentState: ChatbotState): Unit = {
    print("\n> ")
    Try(StdIn.readLine()) match {
      case scala.util.Success(userInputStringOrNull) =>
        val userInput = Option(userInputStringOrNull).map(_.trim).getOrElse("exit") // Treat EOF as exit

        if (farewellKeywords.contains(userInput.toLowerCase)) {
          val farewellMsg = "Goodbye! Happy learning!"
          println(farewellMsg)
          persistentLogInteraction(userInput, farewellMsg) // Final persistent log
        } else {
          val (response, nextState) = handleUserInput(userInput, currentState, initialExplanations, initialQuizBank)
          println(response)
          if (!farewellKeywords.contains(userInput.toLowerCase) && response != "Goodbye! Happy learning!") {
             loop(nextState)
          } else {
            // If handleUserInput resulted in a farewell, or input was farewell
            if (response != "Goodbye! Happy learning!") { // Log if response wasn't already the goodbye
                persistentLogInteraction(userInput, response)
            }
          }
        }
      case scala.util.Failure(ex) =>
        println(s"Error reading input: ${ex.getMessage}. Exiting.")
        persistentLogInteraction("INPUT_ERROR", s"Chatbot session ended due to input error: ${ex.getMessage}")
    }
  }
  loop(initialBotState)
}

// --- Data Loading (Side Effects) ---
def loadExplanationsFromFile(filePath: String = explanationsPath): Map[String, ExplanationData] = {
  Try {
    val source = Source.fromFile(filePath, "UTF-8")
    val lines = source.getLines().drop(1).toList
    source.close()
    lines.flatMap { line =>
      Try {
        val columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
        if (columns.length == 5) {
          val concept = columns(0).trim.toLowerCase
          val explanation = columns(1).trim.stripPrefix("\"").stripSuffix("\"")
          val explanation2 = columns(2).trim.stripPrefix("\"").stripSuffix("\"")
          val example = columns(3).trim.stripPrefix("\"").stripSuffix("\"")
          val note = columns(4).trim.stripPrefix("\"").stripSuffix("\"")
          if (concept.nonEmpty) Some(concept -> (explanation, explanation2, example, note))
          else None
        } else None
      }.toOption.flatten
    }.toMap
  }.getOrElse {
    println(s"Failed to load concept explanations from $filePath. Using an empty map.")
    Map.empty[String, ExplanationData]
  }
}

@main def main(): Unit = {
  val explanations = loadExplanationsFromFile()
  // globalQuizBank is loaded at the top level
  if (explanations.isEmpty && globalQuizBank.isEmpty) {
    println("Error: Could not load explanation AND question data. Chatbot may not function as expected.")
    println(s"Please check paths: $explanationsPath and $questionsPath")
    println(s"Current base directory used for data: ${new File(baseDir).getAbsolutePath}")
  } else if (explanations.isEmpty) {
    println(s"Warning: Could not load concept explanations from $explanationsPath. Explain feature will be limited.")
  } else if (globalQuizBank.isEmpty) {
    println(s"Warning: Could not load quiz questions from $questionsPath. Quiz feature will be limited.")
  }
  runChatbot(explanations, globalQuizBank)
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/collection/immutable/List.empty().