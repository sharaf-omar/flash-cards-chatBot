error id: scala/package.List#
file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
empty definition using pc, found symbol in pc: scala/package.List#
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -java/io/List#
	 -scala/io/StdIn.List#
	 -List#
	 -scala/Predef.List#
offset: 5747
uri: file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
text:
```scala
import java.io._
import scala.io.Source
import scala.io.StdIn._
import scala.util.Try
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.util.boundary, boundary.break // Scala 3 specific

// --- Configuration and Constants ---
val baseDir = "../data/" 
val explanationsPath = s"${baseDir}explanations.csv"
val questionsPath = s"${baseDir}questions.csv"
val userInteractionsLogFilePath = s"${baseDir}user_interactions_log.txt" // For persistent logging

// --- Response Templates (Kept for generating varied responses) ---
val responseTemplates = List(
    "For {concept}, check this out: {response}",
    "Let's break {concept} down for you: {response}",
    "Aight, let's talk about {concept}: {response}"
)
val followUpTemplates = List(
    "No worries, let's go over {concept} one more time: {response}",
    "Okay, let's show a different angle on {concept}: {response}"
)
val exampleTemplates = List(
    "Here's a quick example of {concept} in action: {response}",
    "Let me dig deeper on {concept} with a practical example: {response}"
)
val clarificationPrompts = List(
    "Does that make sense, or would you like more clarification?",
    "Got it so far? Need a bit more explanation or an example?"
)
val suggestionPrompts = List(
    "Not sure what to do next? You can try 'quiz python', 'explain encapsulation', or ask about another topic!",
    "What's next? I can explain a concept, start a quiz, or we can explore something new!"
)

// --- Keywords ---
val greetingKeywords = List("hi", "hello", "hey", "greetings", "start")
val quizKeywords = List("quiz", "test", "questions", "practice")
val explainKeywords = List("explain", "what", "how", "why", "describe", "tell")
val farewellKeywords = List("exit", "bye", "goodbye", "quit")
val analyticsKeywords = List("analytics", "stats", "usage", "performance")
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
)

// --- Type Aliases for Readability (instead of case classes) ---
type Explanation = (String, String, String, String) // explanation, explanation2, example, note
type QuizQuestion = (String, List[String], String, String) // questionText, choices, correctAnswerText, generalNote
type QuizBank = Map[String, List[QuizQuestion]] // Topic -> List of questions

type InteractionLog = Li@@st[(Int, String, String)] // seqNum, userInput, chatbotResponse
// For more detailed quiz analytics, we might need a richer log structure or parse info from chatbotResponse
// For simplicity adhering to spec, analyzeQuizPerformance will work on the general InteractionLog.

type QuizSessionState = (String, List[QuizQuestion], List[Boolean], Int) // topic, remainingQuestions, answersSoFar, currentQuestionIndex
type UserPreferences = Map[String, String] // e.g., "favTopic" -> "scala"

type ChatbotState = (InteractionLog, Int, Option[QuizSessionState], UserPreferences) // log, nextSeqNum, currentQuizState, preferences

// --- Keyword Variations (Initialized once) ---
val keywordVariations: Map[String, List[String]] = {
  conceptKeywords.map { concept =>
    val canonical = concept.toLowerCase
    var variations = List(canonical)
    if (!canonical.endsWith("s") && !canonical.contains(" ")) variations = (canonical + "s") :: variations
    if (canonical.endsWith("s") && !canonical.contains(" ")) variations = canonical.stripSuffix("s") :: variations
    val specialCases = Map(
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
    variations = specialCases.getOrElse(canonical, Nil) ++ variations
    if (concept.contains(" ")) variations = lowercase.replace(" ", "") :: lowercase.replace("-", " ") :: variations
    canonical -> variations.distinct.map(_.toLowerCase)
  }.toMap
}

val variationToCanonical: Map[String, String] = keywordVariations.flatMap {
  case (canonical, variations) => variations.map(variation => variation -> canonical)
}

val allKeywordsForParsing = greetingKeywords ++ quizKeywords ++ explainKeywords ++ farewellKeywords ++ analyticsKeywords ++ languageKeywords ++ conceptKeywords

// --- Core Chatbot Module (Section 3.1) ---

def greetUser(): String = {
  val greetingTemplates = List(
    "Hello! I'm your Scala Functional Chatbot. Ready to learn or take a quiz?",
    "Hey there! Let's explore some programming concepts. What's on your mind?",
    "Hi! Ask me to 'explain <concept>', 'quiz <topic>', or 'analytics'."
  )
  greetingTemplates(Random.nextInt(greetingTemplates.length)) + "\n" +
  suggestionPrompts(Random.nextInt(suggestionPrompts.length))
}

// Spec: parseInput(input: String): Tokenizes the input into manageable parts for easier processing.
// Current implementation extracts keywords and concepts. This is a reasonable interpretation of "manageable parts".
def parseInput(input: String, knownKeywords: List[String] = allKeywordsForParsing): List[String] = {
  val normalizedInput = input.replaceAll("[^a-zA-Z0-9\\-_ +]", "") // Allow hyphen, underscore, plus
                            .replaceAll("\\s+", " ")
                            .toLowerCase
                            .trim

  // Prioritize multi-word concepts
  val (matchedCanonicalConcepts, textAfterMultiWord) =
    conceptKeywords.sortBy(-_.length).foldLeft((Set.empty[String], normalizedInput)) {
      case ((accConcepts, currentText), conceptKey) =>
        if (currentText.contains(conceptKey)) {
          (accConcepts + conceptKey, currentText.replace(conceptKey, " ")) // Replace with space to separate words
        } else {
          (accConcepts, currentText)
        }
    }

  // Then match single words or variations
  val singleWordTokens = textAfterMultiWord
    .split(" ")
    .filter(_.nonEmpty)
    .map(token => variationToCanonical.getOrElse(token, token)) // Map variations to canonical
    .filter(knownKeywords.contains) // Only keep known keywords/concepts
    .toSet

  (matchedCanonicalConcepts ++ singleWordTokens).toList.distinct
}


// Helper to determine intent from tokens
// Returns: (IntentString, List[ConceptString], Option[LanguageString])
type IntentInfo = (String, List[String], Option[String])

private def determineIntent(tokens: List[String]): IntentInfo = {
  val isGreeting = tokens.exists(greetingKeywords.contains)
  val isQuiz = tokens.exists(quizKeywords.contains)
  val isExplain = tokens.exists(explainKeywords.contains)
  val isFarewell = tokens.exists(farewellKeywords.contains)
  val isAnalytics = tokens.exists(analyticsKeywords.contains)

  val foundConcepts = tokens.filter(conceptKeywords.contains)
  val foundLanguages = tokens.filter(languageKeywords.contains)
    .map(lang => variationToCanonical.getOrElse(lang, lang)) // Canonical form for language
  
  val mainConceptOrLang = (foundConcepts ++ foundLanguages).distinct.headOption

  if (isQuiz) {
    ("quiz", foundConcepts.distinct, mainConceptOrLang)
  } else if (isExplain) {
    ("explain", foundConcepts.distinct, mainConceptOrLang)
  } else if (isAnalytics) {
    ("analytics", List(), None)
  } else if (isGreeting) {
    ("greeting", List(), None)
  } else if (isFarewell) {
    ("farewell", List(), None)
  } else if (mainConceptOrLang.isDefined) {
    // If a concept or language is mentioned without explain/quiz, assume explain
    ("explain", foundConcepts.distinct, mainConceptOrLang)
  }
  else {
    ("unknown", List(), None)
  }
}

// Spec: generateResponse(query: String): Crafts a well-structured response based on the query category.
// This will be a part of the explanation flow.
// For simplicity, this function will retrieve the main explanation.
// The interactive part (follow-ups, examples) will be handled by `handleUserInput` based on state.
def generateExplanationResponse(
  concept: String,
  explanations: Map[String, Explanation] // Concept -> ExplanationTuple
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
          s"Sorry, I don't have an explanation for '$concept'. Available concepts include: ${Random.shuffle(conceptKeywords).take(3).mkString(", ")}..."
      }
    case None => s"Sorry, I don't recognize the concept '$concept'."
  }
}

// --- Functional Chatbot Quiz Generator (Section 3.2) ---

// Helper to load all questions from file, keyed by canonical concept
private def loadQuizBankFromFile(filePath: String = questionsPath): QuizBank = {
  Try {
    val source = Source.fromFile(filePath)
    val lines = source.getLines().drop(1).toList // Read all lines
    source.close()

    lines.flatMap { line =>
      Try {
        val columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
        // Concept,Question,Option One,Option Two,Option Three,Option Four,Correct Answer,Note
        if (columns.length == 9) { // Ensure correct number of columns
          val csvConcept = columns(0).trim.toLowerCase
          val canonicalConcept = variationToCanonical.getOrElse(csvConcept, csvConcept)
          val questionText = columns(1).trim.stripPrefix("\"").stripSuffix("\"")
          val choices = List(columns(2), columns(3), columns(4), columns(5))
            .map(_.trim.stripPrefix("\"").stripSuffix("\""))
          val correctAnswer = columns(6).trim.stripPrefix("\"").stripSuffix("\"")
          val generalNote = columns(7).trim.stripPrefix("\"").stripSuffix("\"")
          Some(canonicalConcept -> (questionText, choices, correctAnswer, generalNote))
        } else {
          // println(s"Skipping malformed CSV line (expected 9 columns, got ${columns.length}): $line")
          None
        }
      }.toOption.flatten // Flatten Option[Option[...]] to Option[...]
    }
    .groupBy(_._1) // Group by canonicalConcept
    .map { case (topic, questionsWithTopic) =>
      topic -> questionsWithTopic.map(_._2) // Map to List[QuizQuestion]
    }
  }.getOrElse {
    println(s"Failed to load quiz bank from $filePath. Using an empty quiz bank.")
    Map.empty[String, List[QuizQuestion]]
  }
}
val globalQuizBank: QuizBank = loadQuizBankFromFile() // Load once

// Spec: selectQuizQuestions(topic: String): Returns a list of questions
def selectQuizQuestions(topic: String, numQuestions: Int, quizBank: QuizBank): Option[List[QuizQuestion]] = {
  val canonicalTopic = variationToCanonical.getOrElse(topic.toLowerCase, topic.toLowerCase)
  quizBank.get(canonicalTopic).map { questions =>
    Random.shuffle(questions).take(numQuestions)
  }.filter(_.nonEmpty) // Ensure we don't return an empty list if topic exists but has no questions after shuffle/take
}

// Spec: presentQuizQuestion(question: (String, List[String], String)): Formats and displays
// Changed signature to include question number for better UX.
def presentQuizQuestion(question: QuizQuestion, questionNumber: Int): String = {
  val (questionText, choices, _, generalNote) = question
  val choicesString = choices.zipWithIndex.map { case (opt, idx) =>
    s"${(idx + 'a').toChar}. $opt"
  }.mkString("\n")
  s"\nQuestion ${questionNumber}: $questionText\n$choicesString\nType 'hint' for a note.\nYour answer (letter or full text): "
}

// Spec: evaluateQuizAnswer(userAnswer: String, correctAnswer: String): Uses pattern matching to classify the answer as “correct,” “incorrect,” or “invalid”
// For simplicity, returning Option[Boolean]: Some(true) for correct, Some(false) for incorrect, None for invalid (e.g. if choice mapping fails)
// The similarity check is a good addition from original code.
def evaluateQuizAnswer(userAnswerRaw: String, choices: List[String], correctAnswerText: String): Option[Boolean] = {
  val userAnswerProcessed = userAnswerRaw.trim.toLowerCase
  
  val chosenAnswerText = userAnswerProcessed match {
    case singleChar if singleChar.length == 1 && "abcd".contains(singleChar) =>
      val choiceIndex = "abcd".indexOf(singleChar)
      if (choiceIndex >= 0 && choiceIndex < choices.length) Some(choices(choiceIndex).toLowerCase)
      else None // Invalid choice letter
    case fullText => Some(fullText)
  }

  chosenAnswerText.map { ansText =>
    // Helper for similarity, e.g., Levenshtein distance
    def levenshteinDistance(s1: String, s2: String): Int = {
      // Standard Levenshtein implementation
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
      if (maxLength == 0) true // Both empty
      else (distance.toDouble / maxLength) <= threshold
    }

    ansText == correctAnswerText.toLowerCase || isSimilar(ansText, correctAnswerText.toLowerCase)
  }
}

// Spec: summarizeQuizResults(answers: List[Boolean]): Summarizes quiz performance
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

// Spec: logInteraction(userInput: String, chatbotResponse: String): Unit
// Will return new log: (InteractionLog, Int) -> (newLog, newSeqNum)
// The persistent file logging is a side effect and separate.
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
  persistentLogInteraction(userInput, chatbotResponse) // Keep persistent log
  val newEntry = (currentSeqNum, userInput, chatbotResponse)
  (currentLog :+ newEntry, currentSeqNum + 1)
}

// Spec: getInteractionLog(): List[(Int, String, String)]
// This function will take the current log as state.
def getInteractionLog(log: InteractionLog): List[(Int, String, String)] = {
  log
}

// Spec: analyzeInteractions (log: List[(Int, String, String)]): Process the log using higher-order functions
def analyzeInteractions(log: InteractionLog): String = {
  if (log.isEmpty) return "No interactions logged yet to analyze."

  val totalInteractions = log.length
  
  // Example: Count interactions containing "quiz" or "explain"
  val quizRequests = log.count(_._2.toLowerCase.contains("quiz"))
  val explainRequests = log.count(_._2.toLowerCase.contains("explain"))

  s"Analytics Report:\n" +
  s"Total Interactions: $totalInteractions\n" +
  s"Requests for Quizzes: $quizRequests\n" +
  s"Requests for Explanations: $explainRequests\n"
  // More complex analysis can be added here, e.g., most common concepts discussed.
}

// Spec: analyzeQuizPerformance(log: List[(Int, String, String)]): Extract quiz-specific analytics
// This is tricky if only general log is available. For now, a simple analysis.
// A richer log (e.g. `QuizInteractionLogEntry`) would be better.
// Assume chatbot responses during quiz contain "Correct!" or "Wrong." to parse.
def analyzeQuizPerformance(log: InteractionLog): String = {
  if (log.isEmpty) return "No interactions logged yet to analyze quiz performance."

  val quizRelatedInteractions = log.filter { case (_, userInput, chatbotResponse) =>
    chatbotResponse.contains("Correct!") || chatbotResponse.contains("Wrong.") || userInput.toLowerCase.startsWith("answer for q") // Heuristic
  }

  if (quizRelatedInteractions.isEmpty) return "No quiz activity found in the logs."

  val correctAnswers = quizRelatedInteractions.count(_._3.contains("Correct!"))
  val incorrectAnswers = quizRelatedInteractions.count(_._3.contains("Wrong."))
  val totalAnswered = correctAnswers + incorrectAnswers

  if (totalAnswered == 0) return "Quiz activity found, but no answers could be parsed."

  val accuracy = if (totalAnswered > 0) (correctAnswers.toDouble / totalAnswered * 100).toInt else 0

  s"Quiz Performance Report:\n" +
  s"Total Questions Answered (parsed): $totalAnswered\n" +
  s"Correct Answers: $correctAnswers\n" +
  s"Incorrect Answers: $incorrectAnswers\n" +
  s"Accuracy: $accuracy%\n"
  // To get "frequently missed questions", we'd need to log question text with answer outcome.
}

// --- User Preferences (Section 4.3 - Basic Implementation) ---
def storeUserPreferences(
  key: String,
  value: String,
  currentPreferences: UserPreferences
): UserPreferences = {
  currentPreferences + (key -> value)
}

def getUserPreferences(key: String, preferences: UserPreferences): Option[String] = {
  preferences.get(key)
}

// --- Main Chatbot Logic ---
// This function replaces the old `processUserInput` and is the core of interaction.
// It's becoming complex and might need further breakdown if more states are added.
def handleUserInput(
  rawInput: String,
  chatbotState: ChatbotState,
  explanations: Map[String, Explanation],
  quizBank: QuizBank // Pass quizBank to avoid reloading
): (String, ChatbotState) = {
  val (currentLog, currentSeqNum, currentQuizSessOpt, currentPrefs) = chatbotState
  val input = rawInput.trim.toLowerCase

  // Persistent logging for raw input
  persistentLogInteraction(input, "N/A - User Input Processing", Some("RAW_USER_INPUT"))


  def startNewQuiz(topic: String, numQuestions: Int = 5): (String, ChatbotState) = {
    selectQuizQuestions(topic, numQuestions, quizBank) match {
      case Some(questions) if questions.nonEmpty =>
        val firstQuestionText = presentQuizQuestion(questions.head, 1)
        val newQuizSession = (topic, questions.tail, List.empty[Boolean], 1) // topic, remaining, answers, currentQNum
        val response = s"Let's start a quiz on $topic!\n$firstQuestionText"
        val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
        (response, (updatedLog, nextSeq, Some(newQuizSession), currentPrefs))
      case _ =>
        val availableTopics = Random.shuffle(quizBank.keys.toList).take(5).mkString(", ")
        val response = s"No questions found for '$topic'. Available topics: $availableTopics..."
        val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
        (response, (updatedLog, nextSeq, None, currentPrefs)) // No quiz state if no questions
    }
  }
  
  // Handle quiz interaction if a quiz is in progress
  currentQuizSessOpt match {
    case Some((topic, remainingQuestions, answersSoFar, currentQuestionNum)) =>
      val currentQuestion = globalQuizBank(topic).find { q => // Get full current question from global bank
          !remainingQuestions.contains(q) && // It's not remaining
          (globalQuizBank(topic).length - remainingQuestions.length) == currentQuestionNum // It's the current one
      }.get // This assumes currentQuestionNum correctly points to a question not in remainingQuestions

      if (input == "hint") {
        val (_, _, _, note) = currentQuestion
        val response = if (note.nonEmpty) s"Hint: $note\nYour answer: " else "No specific hint for this question.\nYour answer: "
        // Log this interaction but don't advance quiz state
        val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
        (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs))
      } else {
        evaluateQuizAnswer(input, currentQuestion._2, currentQuestion._3) match {
          case Some(isCorrect) =>
            val feedback = if (isCorrect) "Correct!" else s"Wrong. The correct answer was: ${currentQuestion._3}"
            val updatedAnswers = answersSoFar :+ isCorrect

            if (remainingQuestions.isEmpty) { // Last question
              val summary = summarizeQuizResults(updatedAnswers)
              val response = s"$feedback\n$summary\n\n${suggestionPrompts(Random.nextInt(suggestionPrompts.length))}"
              val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
              (response, (updatedLog, nextSeq, None, currentPrefs)) // End quiz
            } else {
              val nextQuestionText = presentQuizQuestion(remainingQuestions.head, currentQuestionNum + 1)
              val response = s"$feedback\n$nextQuestionText"
              val newQuizSession = (topic, remainingQuestions.tail, updatedAnswers, currentQuestionNum + 1)
              val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
              (response, (updatedLog, nextSeq, Some(newQuizSession), currentPrefs))
            }
          case None => // Invalid answer format
            val response = "Invalid answer format. Please use 'a', 'b', 'c', 'd' or the full text.\n" + presentQuizQuestion(currentQuestion, currentQuestionNum)
            val (updatedLog, nextSeq) = logInteraction(input, response, currentLog, currentSeqNum)
            (response, (updatedLog, nextSeq, currentQuizSessOpt, currentPrefs)) // Re-ask
        }
      }

    case None => // No quiz in progress, general interaction
      val tokens = parseInput(input)
      val (intent, concepts, langOrConceptOpt) = determineIntent(tokens)

      val (response, newChatbotState) = intent match {
        case "greeting" =>
          val r = greetUser()
          val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))
        case "farewell" =>
          val r = "Goodbye! Happy learning!"
          val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs)) // Final state, though it won't be used
        case "quiz" =>
          langOrConceptOpt match {
            case Some(topic) => startNewQuiz(topic)
            case None =>
              val r = s"Please specify a topic for the quiz. Available topics: ${Random.shuffle(quizBank.keys.toList).take(5).mkString(", ")}"
              val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
              (r, (l, s, None, currentPrefs))
          }
        case "explain" =>
          langOrConceptOpt match {
            case Some(concept) =>
              // For explanations, we enter an interactive sub-flow
              // The spec's `generateResponse` should give the initial text.
              // The interactive flow is complex to manage with purely immutable state return.
              // The original `explanationFlow` uses `readLine` internally.
              // To fit the functional model, `explanationFlow` would need to return next state.
              // This makes `handleUserInput` very complex.
              // Let's provide initial explanation and prompt for follow-up.
              val explanationText = generateExplanationResponse(concept, explanations)
              val prompt = clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", concept)
              val r = s"$explanationText\n$prompt"
              val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
              // We're not entering a specific "explain" state here for simplicity with immutable returns.
              // The user's next input will be parsed fresh.
              (r, (l, s, None, currentPrefs))
            case None =>
              val r = "Please specify a concept to explain."
              val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
              (r, (l, s, None, currentPrefs))
          }
        case "analytics" =>
          // For simplicity, show both. Could be made interactive.
          val interactionAnalysis = analyzeInteractions(currentLog)
          val quizAnalysis = analyzeQuizPerformance(currentLog)
          val r = s"$interactionAnalysis\n\n$quizAnalysis"
          val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))

        case "unknown" =>
          val r = "I'm not sure how to help with that. " + suggestionPrompts(Random.nextInt(suggestionPrompts.length))
          val (l, s) = logInteraction(input, r, currentLog, currentSeqNum)
          (r, (l, s, None, currentPrefs))
      }
      (response, newChatbotState)
  }
}

// --- Chatbot Execution Loop ---
def runChatbot(
  initialExplanations: Map[String, Explanation],
  initialQuizBank: QuizBank
): Unit = {
  println("Welcome to the Scala Functional Chatbot!")
  println(greetUser()) // Initial greeting outside the loop's response cycle

  val initialBotState: ChatbotState = (List.empty, 1, None, Map.empty)

  @scala.annotation.tailrec
  def loop(currentState: ChatbotState): Unit = {
    print("\n> ")
    Try(readLine()) match {
      case Success(null) | Success("exit") | Success("quit") | Success("bye") => // Handle EOF or exit commands
        println("Goodbye! Happy learning!")
        persistentLogInteraction("exit", "Chatbot session ended.")
      case Success(userInput) =>
        val (response, nextState) = handleUserInput(userInput, currentState, initialExplanations, initialQuizBank)
        println(response)
        if (!farewellKeywords.contains(userInput.toLowerCase)) { // Check if input itself was a farewell
             loop(nextState)
        } else {
            // If userInput was a farewell, response might already be "Goodbye...", but we ensure persistent log
            persistentLogInteraction(userInput, response)
        }
      case Failure(ex) =>
        println(s"Error reading input: ${ex.getMessage}. Exiting.")
        persistentLogInteraction("INPUT_ERROR", s"Chatbot session ended due to input error: ${ex.getMessage}")
    }
  }
  loop(initialBotState)
}

// --- Data Loading (Side Effects) ---
def loadExplanationsFromFile(filePath: String = explanationsPath): Map[String, Explanation] = {
  Try {
    val source = Source.fromFile(filePath)
    val lines = source.getLines().drop(1).toList // Read all lines
    source.close() // Close the source after reading

    lines.flatMap { line =>
      Try {
        // Regex to split CSV, handling quotes
        val columns = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)
        if (columns.length == 5) { // Concept, Explanation, Explanation2, Example, Note
          val concept = columns(0).trim.toLowerCase
          val explanation = columns(1).trim.stripPrefix("\"").stripSuffix("\"")
          val explanation2 = columns(2).trim.stripPrefix("\"").stripSuffix("\"")
          val example = columns(3).trim.stripPrefix("\"").stripSuffix("\"")
          val note = columns(4).trim.stripPrefix("\"").stripSuffix("\"")
          if (concept.nonEmpty) Some(concept -> (explanation, explanation2, example, note))
          else None
        } else {
          // println(s"Skipping malformed CSV line (expected 5 columns, got ${columns.length}): $line")
          None
        }
      }.toOption.flatten // Flatten Option[Option[...]] to Option[...]
    }.toMap
  }.getOrElse {
    println(s"Failed to load explanations from $filePath. Using an empty map.")
    Map.empty[String, Explanation]
  }
}


@main def main(): Unit = {
  val explanations = loadExplanationsFromFile()
  // QuizBank is already loaded globally: val globalQuizBank
  if (explanations.isEmpty && globalQuizBank.isEmpty) {
    println("Error: Could not load explanation and question data. Chatbot cannot function effectively.")
    println(s"Please check paths: $explanationsPath and $questionsPath")
    println(s"Current base directory used: $baseDir (Set CHATBOT_DATA_DIR environment variable to override)")
  } else {
     runChatbot(explanations, globalQuizBank)
  }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: scala/package.List#