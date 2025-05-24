file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotEngine.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/ScalaChatbotGUI/src/main/scala/ChatbotEngine.scala
text:
```scala
import java.io._
import scala.io.Source
import scala.io.StdIn 
import scala.util.Try
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.github.tototoshi.csv._  

object ChatbotEngine {
  val baseDir = "data/"
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
      "Not sure what to do next? You can try a quiz on python, explain encapsulation, or ask about another topic!",
      "What's next? I can explain a concept, start a quiz, or we can explore something new!",
      "Need ideas? How about a quiz on javascript or learning about inheritance?",
      "You can ask me to explain a concept, start a quiz, or type exit to leave!",
      "If you're looking for something specific, just let me know!"
  )
  //Keywords
  val greetingKeywords = List(
    "hi", "hello", "hey", "greetings", "start", "hola", "good morning",
    "good afternoon", "good evening", "what's up", "sup", "yo", "howdy",
    "hiya", "let's begin", "begin","wsg", "wassup", "how's it going", "how are you"
  )
  val quizKeywords = List(
    "quiz", "test", "challenge", "exam", "assessment", "questions", "practice",
    "check my knowledge", "test me", "quiz me", "give me a quiz", "start quiz",
    "pop quiz", "flash card"
  )
  val explainKeywords = List(
    "explain", "what is", "what's", "define", "definition of", "tell me about",
    "describe", "how does", "how do", "why is", "why does", "details on",
    "more info on", "elaborate on", "clarify", "what are", "what do", "Understand"
  )
  val farewellKeywords = List(
    "exit", "bye", "goodbye", "quit", "see ya", "later", "farewell",
    "end session", "log off", "sign out", "terminate", "im done", "i'm done"
  )
  val analyticsKeywords = List(
    "analytics", "stats", "statistics", "usage", "performance", "report",
    "my progress", "how am i doing", "my stats", "chatbot usage",
    "analyze my quiz", "data"
  )
  val languageKeywords = List("cpp", "c++", "javascript", "js", "python", "py", "scala", "java", "c", "clang", "go", "golang")

  val conceptKeywords = List(
      "c++", "javascript", "java","algorithm", "scala", "class", "arrays", "variables", "dsa", "oop", "loops", "inheritance", "polymorphism",
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
      "time complexity", "space complexity","ai","cyber security","web programming", "software engineer","data scientist","machine learning engineer","devops engineer","cloud engineer", "cybersecurity analyst","game developer", "mobile developer","full stack developer"
  ).map(_.toLowerCase).distinct 

  //Type Aliases for Readability 
  type ExplanationData = (String, String, String, String) // explanation, explanation2, example, note
  type QuizQuestion = (String, List[String], String, String, String) // questionText, choices, correctAnswerText, generalNote, questionExplanation
  type QuizBank = Map[String, List[QuizQuestion]] // Topic -> List of questions

  type InteractionLog = List[(Int, String, String)] // seqNum, userInput, chatbotResponse
  type QuizSessionState = (
      String, 
      List[QuizQuestion], 
      List[Boolean],      
      Int,           
      List[QuizQuestion] 
  )
  type UserPreferences = Map[String, String] 

  type LastActionContext = Option[(String, String)]

  type CurrentUser = Option[String]

  type ChatbotState = (InteractionLog, Int, Option[QuizSessionState], UserPreferences, LastActionContext, CurrentUser) //


  //Keyword Variations

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
      "golang" -> List("go", "golang", "go lang"),
      "ai" -> List("ai", "artificial intelligence", "a.i."),
      "machine learning" -> List("machine learning", "ml", "m.l."),
      "machine learning engineer" -> List("machine learning engineer", "ml engineer", "mle"),
      "data scientist" -> List("data scientist", "data science"),
      "cyber security" -> List("cyber security", "cybersecurity", "infosec", "information security"), 
      "cybersecurity analyst" -> List("cybersecurity analyst", "cyber security analyst", "soc analyst", "security analyst"),
      "web programming" -> List("web programming", "web dev", "web development"),
      "software engineer" -> List("software engineer", "swe", "software developer", "developer", "coder", "programmer"),
      "full stack developer" -> List("full stack developer", "fullstack developer", "full-stack dev", "fullstack dev"),
      "mobile developer" -> List("mobile developer", "mobile dev", "app developer", "ios developer", "android developer"), 
      "game developer" -> List("game developer", "game dev", "game programmer"),
      "devops engineer" -> List("devops engineer", "devops"),
      "cloud engineer" -> List("cloud engineer", "cloud engineering", "aws engineer", "azure engineer", "gcp engineer")
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

  //Core Chatbot Module

  def greetUser(username: Option[String] = None): String = {
    val namePart = username.filter(_ != "Guest").getOrElse("")
    val greetingTemplates = List(
      s"Hello $namePart! I'm your Scala Functional Chatbot. Ready to learn or take a quiz?",
      s"Hey there $namePart! Let's explore some programming concepts. What's on your mind?",
      s"Hi $namePart! Ask me to explain a concept, a quiz on a specific topic or show some analytics.",
      s"Hello $namePart, ready to learn something new today?",
      s"Hey there $namePart! Let's dive into some programming concepts. what's on your mind?",
      s"Hi $namePart! I'm here to help you with programming concepts. What would you like to explore?",
      s"Greetings $namePart! Want to quiz yourself or dive into a concept?",
      s"Yo $namePart, what's up? Let's get started with some coding knowledge!"
    )
    greetingTemplates(Random.nextInt(greetingTemplates.length))
  }

  def parseInput(input: String, knownKeywords: List[String] = allKeywordsForParsing): List[String] = {
  val normalizedInput = input.replaceAll("[^a-zA-Z0-9\\-_ +]", " ")
                            .replaceAll("\\s+", " ")
                            .toLowerCase
                            .trim

  val sortedCanonicalConceptKeywords = conceptKeywords.distinct.sortBy(-_.length)

  // Helper to check if a phrase is a "whole word/phrase" match in text
  // This version uses a simple regex for word boundaries for clarity and robustness
  // If you strictly want to avoid java.util.regex, we can use the manual boundary check.
  def isWholeWordOrPhraseMatch(text: String, phrase: String): Boolean = {
    // Escape special regex characters in the phrase, then add word boundaries
    val pattern = s"\\b${java.util.regex.Pattern.quote(phrase)}\\b".r
    pattern.findFirstIn(text).isDefined
  }
  
  // Alternative isWholeWordOrPhraseMatch without java.util.regex (more manual)
  def isWholeWordOrPhraseMatchManual(text: String, phrase: String): Boolean = {
    var found = false
    var index = text.indexOf(phrase)
    while (index != -1 && !found) {
      val charBeforeOK = if (index == 0) true else !text(index - 1).isLetterOrDigit
      val charAfterOK = if (index + phrase.length == text.length) true else !text(index + phrase.length).isLetterOrDigit
      if (charBeforeOK && charAfterOK) {
        found = true
      }
      index = text.indexOf(phrase, index + 1)
    }
    found
  }


  val (foundCanonicalConcepts, textAfterConceptExtraction) =
    sortedCanonicalConceptKeywords.foldLeft((Set.empty[String], normalizedInput)) {
      case ((accConcepts, currentText), conceptKey) =>
        // For very short keys (like "c", "ai"), or any key you want to be strict with,
        // always use a whole word match. For longer keys, simple contains might be
        // acceptable if they are less likely to be substrings of action words.
        // However, for consistency and to fix your "ai" in "explain" issue,
        // using isWholeWordOrPhraseMatch for ALL concepts is safer.

        if (isWholeWordOrPhraseMatchManual(currentText, conceptKey)) { // Using the manual version
        // if (isWholeWordOrPhraseMatch(currentText, conceptKey)) { // Or using the regex version
          val canonical = variationToCanonical.getOrElse(conceptKey, conceptKey) // Should already be canonical if from conceptKeywords
          (accConcepts + canonical, currentText.replace(conceptKey, " ")) // Replace to avoid re-matching parts
        } else {
          (accConcepts, currentText)
        }
    }

  val singleWordActionTokens = textAfterConceptExtraction
    .split(" ")
    .filter(_.nonEmpty)
    .flatMap { token =>
      // At this stage, primarily looking for action keywords (explain, quiz)
      // or single-word concept aliases that were not part of multi-word phrases.
      variationToCanonical.get(token) match {
        case Some(canonical) if conceptKeywords.contains(canonical) => Some(canonical)
        case Some(otherCanonical) if knownKeywords.contains(otherCanonical) => Some(otherCanonical) 
        case None => if (knownKeywords.contains(token)) Some(token) else None 
      }
    }
    .toSet

  (foundCanonicalConcepts ++ singleWordActionTokens).toList.distinct
}

  type IntentInfo = (String, List[String], Option[String], Option[String])

  def determineIntent(tokens: List[String], rawInput: String): IntentInfo = {
    val lowerTokens = tokens.map(_.toLowerCase)
    val lowerRawInput = rawInput.toLowerCase

    val isGreeting = lowerTokens.exists(greetingKeywords.contains)
    val isQuiz = lowerTokens.exists(quizKeywords.contains)
    val isExplain = lowerTokens.exists(explainKeywords.contains)
    val isFarewell = lowerTokens.exists(farewellKeywords.contains)
    val isAnalytics = lowerTokens.exists(analyticsKeywords.contains)

    val foundConcepts = lowerTokens.filter(conceptKeywords.contains)
    val foundLanguagesOrTopics = lowerTokens.filter(t => languageKeywords.contains(t) || conceptKeywords.contains(t))
                                      .map(t => variationToCanonical.getOrElse(t, t))
    val mainTopic = foundLanguagesOrTopics.headOption

    var extractedName: Option[String] = None
    val nameKeywordsForDetection = List("name is", "my name is", "call me", "i am", "i'm", "name","called", "call", "name to") // Internal list
    nameKeywordsForDetection.foreach { keyword =>
      if (extractedName.isEmpty && lowerRawInput.contains(keyword + " ")) { // Check for space after keyword
        val startIndex = lowerRawInput.indexOf(keyword) + keyword.length
        val potentialName = lowerRawInput.substring(startIndex).trim.split(" ").headOption.filter(_.nonEmpty)
        potentialName.foreach { name =>
          if (!allKeywordsForParsing.contains(name.toLowerCase) || name.length > 1) { // Avoid single letter keywords as names
               extractedName = Some(name.split(" ").head.capitalize) // Take first word after "is", capitalize
          }
        }
      }
    }

    val (baseIntent, baseConcepts, baseLangOpt) = {
        if (isQuiz) ("quiz", foundConcepts.distinct, mainTopic)
        else if (isExplain) ("explain", foundConcepts.distinct, mainTopic)
        else if (isAnalytics) ("analytics", List(), None)
        else if (isGreeting) ("greeting", List(), None)
        else if (isFarewell) ("farewell", List(), None)
        else if (mainTopic.isDefined && !extractedName.isDefined) ("explain", foundConcepts.distinct, mainTopic)
        else ("unknown", List(), None)
    }

    if (extractedName.isDefined && (baseIntent == "unknown" || baseIntent == "greeting")) {
        ("set_name", List.empty, None, extractedName)
    } else {
        (baseIntent, baseConcepts, baseLangOpt, extractedName)
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


  // data loading
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
              val explanation1 = columns(1).trim.stripPrefix("\"").stripSuffix("\"")
              val explanation2 = columns(2).trim.stripPrefix("\"").stripSuffix("\"")
              val example = columns(3).trim.stripPrefix("\"").stripSuffix("\"")
              val note = columns(4).trim.stripPrefix("\"").stripSuffix("\"")
              if (concept.nonEmpty) Some(concept -> (explanation1, explanation2, example, note))
              else None
            } else {
              None
            }
          }.recover {
            case e: Exception =>
                None
          }.toOption.flatten
        }.toMap
      }.getOrElse {
        println(s"Failed to load concept explanations from $filePath. Using an empty map.")
        Map.empty[String, ExplanationData]
      }
    }
      
  def loadQuizBankFromFile(filePath: String = questionsPath): QuizBank = {
      val file = new java.io.File(filePath)
      // ... (your debug prints for path checking are still good) ...
    
      Try {
        val reader = CSVReader.open(file, "UTF-8") // Open with the library
        val allLinesWithHeader = reader.allWithHeaders() // Reads all lines and uses header for map keys
        reader.close()
    
        // println(s"--- DEBUG: Read ${allLinesWithHeader.length} data rows (with headers) from questions.csv ---")
    
        allLinesWithHeader.flatMap { rowMap => // rowMap is Map[String, String] (Header -> Value)
          Try {
            // Access columns by header name (case-sensitive, ensure they match your CSV)
            val csvConcept = rowMap("Concept").trim.toLowerCase
            val canonicalConcept = variationToCanonical.getOrElse(csvConcept, csvConcept)
            val questionText = rowMap("Question").trim
            val choices = List(
              rowMap("Option One").trim,
              rowMap("Option Two").trim,
              rowMap("Option Three").trim,
              rowMap("Option Four").trim
            )
            val correctAnswer = rowMap("Correct Answer").trim
            val generalNote = rowMap("Note").trim
            val questionExplanation = rowMap("Explanation").trim
    
            Some(canonicalConcept -> (questionText, choices, correctAnswer, generalNote, questionExplanation))
          }.recover {
            case e: NoSuchElementException => // If a header is missing in a row
              println(s"--- DEBUG: Missing header in row. Headers: ${rowMap.keys.mkString(", ")}. Error: ${e.getMessage} ---")
              None
            case e: Exception =>
              println(s"--- DEBUG: EXCEPTION while processing CSV row map: ${rowMap}. Error: ${e.getClass.getSimpleName} - ${e.getMessage} ---")
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

  // Quiz Functions
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
    s"\nQuestion ${questionNumber}: $questionText\n$choicesString\nYour answer (letter or full text): "
  }

  def evaluateQuizAnswer(userAnswerRaw: String, choices: List[String], correctAnswerText: String): Option[Boolean] = {
    val userAnswerProcessed = userAnswerRaw.trim.toLowerCase
    
    val chosenAnswerTextOpt = userAnswerProcessed match { // Renamed to chosenAnswerTextOpt for clarity
      case singleChar if singleChar.length == 1 && "abcd".contains(singleChar) =>
        val choiceIndex = "abcd".indexOf(singleChar)
        if (choiceIndex >= 0 && choiceIndex < choices.length) Some(choices(choiceIndex).toLowerCase)
        else None
      case fullText => Some(fullText)
    }

    chosenAnswerTextOpt.map { chosenAnsText =>
      def levenshteinDistance(userAnswer: String, correctAnswer: String): Int = {
        val s1 = userAnswer.toLowerCase.trim
        val s2 = correctAnswer.toLowerCase.trim
        
        val memo = scala.collection.mutable.Map[(Int, Int), Int]()
        
        def compute(i: Int, j: Int): Int = {
          memo.get((i, j)) match {
            case Some(result) => result
            case None =>
              val result = 
                if (i == 0) j  
                else if (j == 0) i
                else {
                  val substitutionCost = if (s1(i-1) == s2(j-1)) 0 else 1
                  val deletion = compute(i-1, j) + 1
                  val insertion = compute(i, j-1) + 1
                  val substitution = compute(i-1, j-1) + substitutionCost
                  
                  math.min(math.min(deletion, insertion), substitution)
                }
                
              memo((i, j)) = result
              result
          }
        }
        compute(s1.length, s2.length)
      }
      def isSimilar(s1: String, s2: String, threshold: Double = 0.2): Boolean = {
        if (s1.trim.isEmpty || s2.trim.isEmpty) return false
        val distance = levenshteinDistance(s1, s2)
        val maxLength = math.max(s1.length, s2.length)
        if (maxLength == 0) true else (distance.toDouble / maxLength) <= threshold
      }
      chosenAnsText == correctAnswerText.toLowerCase || isSimilar(chosenAnsText, correctAnswerText.toLowerCase)

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

  //Immutable Interaction Analytics Dashboard
  def persistentLogInteraction(userInput: String, chatbotResponse: String, context: Option[String] = None): Unit = {
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
    
      // Change this line to check if the response CONTAINS "quiz finished!"
      val quizEndResponses = log.filter(_._3.toLowerCase.contains("quiz finished!")) 
    
      if (quizEndResponses.isEmpty) return "No completed quiz sessions found in logs for performance analysis."
    
      val scores = quizEndResponses.flatMap { case (_, _, response) =>
        // Regex to find "You scored X/Y"
        val scorePattern = "you scored (\\d+)/(\\d+)".r // Made regex case-insensitive friendly for matching
        scorePattern.findFirstMatchIn(response.toLowerCase).map { m => // Match against lowercase response
          (Try(m.group(1).toInt).getOrElse(0), Try(m.group(2).toInt).getOrElse(0))
        }
      }
    
      if (scores.isEmpty) return "Could not parse scores from completed quiz sessions."
    
      val totalCorrect = scores.map(_._1).sum
      val totalAttempted = scores.map(_._2).sum
      val numQuizzes = scores.length
      val avgAccuracy = if (totalAttempted > 0) (totalCorrect.toDouble / totalAttempted * 100).toInt else 0
    
      s"Overall Quiz Performance:\n" +
      s"Total Quizzes Completed (parsed): $numQuizzes\n" + // Changed from "Taken" for clarity
      s"Total Questions Answered in these quizzes: $totalAttempted\n" +
      s"Total Correct Answers in these quizzes: $totalCorrect\n" +
      s"Average Accuracy across these quizzes: $avgAccuracy%\n"
    }

  //User Preferences
  def storeUserPreferences(key: String, value: String, currentPreferences: UserPreferences): UserPreferences = {
    currentPreferences + (key -> value)
  }

  def getUserPreferences(key: String, preferences: UserPreferences): Option[String] = {
    preferences.get(key)
  }

  //Main Chatbot Logic
  def handleUserInput(
    rawInput: String,
    chatbotState: ChatbotState,
    explanations: Map[String, ExplanationData],
    quizBank: QuizBank
  ): (String, ChatbotState) = {
    val (currentLog, currentSeqNum, currentQuizSessOpt, currentSessionPrefs, lastActionCtx, currentUserNameOpt) = chatbotState // UNPACK 6 elements
    val input = rawInput.trim.toLowerCase

    val activeUserNameForLog = currentUserNameOpt.getOrElse("Guest")
    persistentLogInteraction(input, "N/A - User Input Processing", Some(s"User: $activeUserNameForLog | RAW_INPUT"))

    def setSessionUsername(nameToSet: String, currentPrefs: UserPreferences): (String, UserPreferences) = {
      val capitalizedName = nameToSet.capitalize
      val oldNameOpt = currentPrefs.get("session_username")
      val newPrefs = currentPrefs + ("session_username" -> capitalizedName)

      val response = oldNameOpt match {
          case Some(oldName) if oldName.equalsIgnoreCase(capitalizedName) =>
              s"Still $capitalizedName, got it!"
          case Some(oldName) =>
              s"Okay, I'll call you $capitalizedName from now on for this session (instead of $oldName)!"
          case None =>
              s"Nice to meet you, $capitalizedName! I'll remember that for our chat."
      }
      (response, newPrefs)
    }

    if (input.contains("remember my name") || input.contains("what is my name") || input.contains("who am i")||
        input.contains("what's my name") || input.contains("tell me my name") || input.contains("say my name")) {
      currentSessionPrefs.get("session_username") match {
        case Some(name) => // No need to check for "Guest" as it's still a remembered name for the session
          val r = s"Yes, I have your name as $name for this session! What can I help you with?"
          val (l,s) = logInteraction(rawInput,r, currentLog, currentSeqNum)
          return (r, (l,s, currentQuizSessOpt, currentSessionPrefs, None, currentUserNameOpt)) // Return 6-tuple
        case None => // This case is less likely if we always start with a username in prefs
          val r = "I don't seem to have a name for you in this session. You can tell me by saying 'my name is [your name]'."
          val (l,s) = logInteraction(rawInput,r, currentLog, currentSeqNum)
          return (r, (l,s, currentQuizSessOpt, currentSessionPrefs, None, currentUserNameOpt)) // Return 6-tuple
      }
    }

    def startNewQuiz(topic: String, numQuestions: Int = 5): (String, ChatbotState) = {
      selectQuizQuestions(topic, numQuestions, quizBank) match {
        case Some(selectedQuestions) if selectedQuestions.nonEmpty =>
          val displayQuestionNum = 1
          val firstQuestionText = presentQuizQuestion(selectedQuestions.head, displayQuestionNum)
          val newQuizSession: QuizSessionState = (topic, selectedQuestions, List.empty[Boolean], 0, List.empty[QuizQuestion])
          val response = s"Okay, let's start a quiz on $topic!\nHere's your first question:\n$firstQuestionText"
          val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
          (response, (updatedLog, nextSeq, Some(newQuizSession), currentSessionPrefs, Some("quiz_started" -> topic), currentUserNameOpt)) // Return 6-tuple
        case _ =>
          val availableTopics = Random.shuffle(quizBank.keys.toList.filter(_.nonEmpty)).take(5).mkString(", ")
          val response = s"Sorry, I couldn't find enough questions for '$topic'. Try one of these: $availableTopics, or another concept."
          val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
          (response, (updatedLog, nextSeq, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
      }
    }

    currentQuizSessOpt match {
      case Some((topic: String, sessionQuestions: List[QuizQuestion], answersSoFar: List[Boolean], currentQuestionIdx: Int, missedSoFar: List[QuizQuestion])) =>
        val displayQuestionNum = currentQuestionIdx + 1
        val currentQuestionOpt: Option[QuizQuestion] =
          if (currentQuestionIdx >= 0 && currentQuestionIdx < sessionQuestions.length) Try(sessionQuestions(currentQuestionIdx)).toOption else None

        currentQuestionOpt match {
          case Some(currentQuestionTuple: QuizQuestion) =>
            val (qText, qChoices, qCorrectAnswer, qNote, qExplanation) = currentQuestionTuple
            if (input == "hint") {
              val response = if (qNote.nonEmpty) s"Hint: $qNote\nYour answer (to question $displayQuestionNum): " else s"No specific hint for this question.\nYour answer (to question $displayQuestionNum): "
              val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
              (response, (updatedLog, nextSeq, currentQuizSessOpt, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // 6-tuple
            }else if(input.toLowerCase == "clear") {
                val response = "Quiz exited. What would you like to do next?"
                val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
                (response, (updatedLog, nextSeq, None, currentSessionPrefs, Some("quiz_exited" -> topic), currentUserNameOpt)) // 6-tuple
            } else {
              evaluateQuizAnswer(input, qChoices, qCorrectAnswer) match {
                case Some(isCorrect: Boolean) =>
                  val feedback = {
                    val expl = if (qExplanation.nonEmpty) qExplanation else "No specific explanation or note provided."
                    if (isCorrect) s"Correct!."
                    else s"Not quite. The correct answer was: $qCorrectAnswer.\nExplanation: $expl\n"
                  }
                  val updatedAnswers = answersSoFar :+ isCorrect
                  val updatedMissed = if (!isCorrect) missedSoFar :+ currentQuestionTuple else missedSoFar
                  val nextQuestionIdx = currentQuestionIdx + 1
                  if (nextQuestionIdx >= sessionQuestions.length) {
                    val summary = summarizeQuizResults(updatedAnswers)
                    val missedExplanations = updatedMissed.map { case (qt, _, ca, note, qe) =>
                      val notee = if (note.nonEmpty) note else "No specific note provided."
                      s"- $notee"
                    }.mkString("\n")
                    val missedHeader = if (updatedMissed.nonEmpty) "\n\nBut these notes in your consideration:\n" else "\nGreat job!"
                    val response = s"$feedback\n$summary$missedHeader$missedExplanations\n\n${suggestionPrompts(Random.nextInt(suggestionPrompts.length))}"
                    val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
                    (response, (updatedLog, nextSeq, None, currentSessionPrefs, Some("quiz_ended" -> topic), currentUserNameOpt)) // 6-tuple
                  } else {
                    val nextQuestionTuple = sessionQuestions(nextQuestionIdx)
                    val nextQuestionText = presentQuizQuestion(nextQuestionTuple, displayQuestionNum + 1)
                    val response = s"$feedback\n$nextQuestionText"
                    val newQuizSession = (topic, sessionQuestions, updatedAnswers, nextQuestionIdx, updatedMissed)
                    val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
                    (response, (updatedLog, nextSeq, Some(newQuizSession), currentSessionPrefs, Some("in_quiz" -> topic), currentUserNameOpt)) // 6-tuple
                  }
                case None =>
                  val response = "Hmm, I didn't quite get that answer. Please use 'a', 'b', 'c', 'd' or the full text.\n" + presentQuizQuestion(currentQuestionTuple, displayQuestionNum)
                  val (updatedLog, nextSeq) = logInteraction(rawInput, response, currentLog, currentSeqNum)
                  (response, (updatedLog, nextSeq, currentQuizSessOpt, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // 6-tuple
              }
            }
          case None =>
            val r = "Error: I lost track of the current question. Ending quiz. What would you like to do next?"
            val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
            (r, (l, s, None, currentSessionPrefs, Some("quiz_error" -> topic), currentUserNameOpt)) // 6-tuple
          }

      case None => // No quiz in progress
        val followUpKeywords = List("further","more", "details", "clarify", "example", "use case", "show me more", "tell me more", "another way", "different angle","dont understand")
        if (followUpKeywords.exists(fk => input.contains(fk))) {
          lastActionCtx match {
            case Some(("explained", conceptName)) =>
              explanations.get(conceptName) match {
                case Some((expl1, expl2, exampleText, note)) =>
                  val responseText = if (input.contains("example") || input.contains("use case")) {
                  if (exampleText.nonEmpty) exampleTemplates(Random.nextInt(exampleTemplates.length)).replace("{concept}", conceptName).replace("{response}", exampleText)
                    else s"Sorry, I don't have a specific example for $conceptName right now. Did you want more details on the explanation?"
                  } else {
                    if (expl2.nonEmpty) followUpTemplates(Random.nextInt(followUpTemplates.length)).replace("{concept}", conceptName).replace("{response}", expl2)
                    else if (expl1.nonEmpty && note.nonEmpty) s"I've shared the main points and a tip on $conceptName. What else can I clarify, or would an example help?"
                    else if (expl1.nonEmpty) s"I've shared the main points on $conceptName. Maybe an example would be helpful if available?"
                    else s"I don't have more details on $conceptName at the moment. Perhaps another concept?"
                  }
                  val prompt = clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", conceptName)
                  val fullResponse = s"$responseText\n$prompt"
                  val (newLog, newSeqNum) = logInteraction(rawInput, fullResponse, currentLog, currentSeqNum)
                  return (fullResponse, (newLog, newSeqNum, None, currentSessionPrefs, Some("explained" -> conceptName), currentUserNameOpt)) // Return 6-tuple
                case None =>
                  val r = "I seem to have forgotten what we were discussing. What topic are you interested in?"
                  val (l,s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
                  return (r, (l,s, None, currentSessionPrefs, None, currentUserNameOpt)) // Return 6-tuple, clear context
              }
            case _ =>
          }
        }

        val tokens = parseInput(rawInput)
        val (intent, concepts, langOrConceptOpt, identifiedNameOpt) = determineIntent(tokens, rawInput)

        val (responseString, newChatbotStateTuple) = intent match {
          case "set_name" => // ADDED THIS CASE BACK
            identifiedNameOpt match {
              case Some(name) =>
                val (nameSetResponse, updatedPrefs) = setSessionUsername(name, currentSessionPrefs)
                val (l, s) = logInteraction(rawInput, nameSetResponse, currentLog, currentSeqNum)
                (nameSetResponse, (l, s, None, updatedPrefs, None, Some(name.capitalize))) // Return 6-tuple, new user name
              case None =>
                val r = "I think you tried to tell me your name, but I missed it. Try 'my name is [name]'."
                val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
                (r, (l, s, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
            }
          case "greeting" =>
            val nameToGreet = currentSessionPrefs.get("session_username") // Use stored name
            val r = greetUser(nameToGreet) // Pass name to greetUser
            val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
            (r, (l, s, None, currentSessionPrefs, None, currentUserNameOpt)) // Return 6-tuple
          case "farewell" =>
            val r = "Goodbye! Happy learning!"
            val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
            (r, (l, s, None, currentSessionPrefs, None, currentUserNameOpt)) // Return 6-tuple
          case "quiz" =>
            langOrConceptOpt match {
              case Some(topic) =>
                val (quizStartMsg, stateAfterQuizStart) = startNewQuiz(topic)
                val (_, _, _, prefsFromQuizStart, _, userNameFromQuizStart) = stateAfterQuizStart // unpack to get new prefs
                val finalPrefs = storeUserPreferences("last_quiz_topic", topic, prefsFromQuizStart) // store in session prefs
                (quizStartMsg, stateAfterQuizStart.copy(_4 = finalPrefs)) // copy to update the prefs in state
              case None =>
                val r = s"Please specify a topic for the quiz (e.g., 'quiz python'). Available: ${Random.shuffle(quizBank.keys.toList.filter(_.nonEmpty)).take(5).mkString(", ")}"
                val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
                (r, (l, s, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
            }
          case "explain" =>
            langOrConceptOpt.orElse(concepts.headOption) match {
              case Some(conceptToExplain) =>
                val canonicalConcept = variationToCanonical.getOrElse(conceptToExplain.toLowerCase, conceptToExplain.toLowerCase)
                val explanationText = generateExplanationResponse(canonicalConcept, explanations)
                val prompt = if (explanations.contains(canonicalConcept)) {
                  clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", canonicalConcept)
                } else ""
                val r = s"$explanationText\n$prompt"
                val updatedPrefs = storeUserPreferences("last_explained_concept", canonicalConcept, currentSessionPrefs)
                val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
                (r, (l, s, None, updatedPrefs, Some("explained" -> canonicalConcept), currentUserNameOpt)) // Return 6-tuple
              case None =>
                val r = "Please specify a concept to explain (e.g., 'explain oop')."
                val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
                (r, (l, s, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
            }
          case "analytics" =>
            val interactionAnalysis = analyzeInteractions(currentLog)
            val quizAnalysis = analyzeQuizPerformance(currentLog)
            val r = s"$interactionAnalysis\n\n$quizAnalysis"
            val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
            (r, (l, s, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
          case "unknown" =>
            val r = "I'm not sure how to help with that. " + suggestionPrompts(Random.nextInt(suggestionPrompts.length))
            val (l, s) = logInteraction(rawInput, r, currentLog, currentSeqNum)
            (r, (l, s, None, currentSessionPrefs, lastActionCtx, currentUserNameOpt)) // Return 6-tuple
        }
        (responseString, newChatbotStateTuple)
    }
  }


  def runChatbot(
      initialExplanations: Map[String, ExplanationData],
      initialQuizBank: QuizBank
    ): Unit = {
      println("Welcome to the Scala Functional Chatbot!")
      
      val initialBotStateFromHelper = runInitialState() // Get initial state
      val initialGreeting = greetUser(initialBotStateFromHelper._6) // Use current user from state for greeting
      println(initialGreeting) // Print potentially personalized greeting

      // Log the actual greeting that was printed
      val (logAfterActualGreeting, seqAfterActualGreeting) = 
          logInteraction("system_start", initialGreeting, initialBotStateFromHelper._1, initialBotStateFromHelper._2)

      val initialBotStateForLoop: ChatbotState = 
          initialBotStateFromHelper.copy(_1 = logAfterActualGreeting, _2 = seqAfterActualGreeting)


      @scala.annotation.tailrec
      def loop(currentState: ChatbotState): Unit = {
        print("\n> ")
        Try(StdIn.readLine()) match {
          case scala.util.Success(userInputStringOrNull) =>
            val userInput = Option(userInputStringOrNull).map(_.trim).getOrElse("exit")
            val (_, _, _, currentPrefsInState, _, currentNameInStateOpt) = currentState // For logging

            if (farewellKeywords.contains(userInput.toLowerCase)) {
              val name = currentNameInStateOpt.getOrElse("learner")
              val farewellMsg = s"Goodbye $name! Happy learning!"
              println(farewellMsg)
              persistentLogInteraction(userInput, farewellMsg, Some(s"User: $name"))
            } else {
              val (response, nextState) = handleUserInput(userInput, currentState, initialExplanations, initialQuizBank)
              println(response)
              if (!farewellKeywords.contains(userInput.toLowerCase) && response != "Goodbye! Happy learning!" && !response.startsWith("Goodbye ")) {
                loop(nextState)
              } else {
                if (response != "Goodbye! Happy learning!" && !response.startsWith("Goodbye ")) { // Log if handleUserInput didn't already say goodbye
                    persistentLogInteraction(userInput, response, Some(s"User: ${currentNameInStateOpt.getOrElse("Guest")}"))
                } else if (response.startsWith("Goodbye ")){ // handleUserInput produced a goodbye
                    persistentLogInteraction(userInput, response, Some(s"User: ${nextState._6.getOrElse("Guest")}"))
                }
              }
            }
          case scala.util.Failure(ex) =>
            println(s"Error reading input: ${ex.getMessage}. Exiting.")
            persistentLogInteraction("INPUT_ERROR", s"Chatbot session ended due to input error: ${ex.getMessage}", None)
        }
      }
      loop(initialBotStateForLoop)
    }


  def runInitialState(): ChatbotState = {
    val initialGreeting = greetUser(None)
    val (initialLog, initialSeqNum) = logInteraction("system_start", initialGreeting, List.empty, 1)
    
    val initialPrefs = Map("session_username" -> "Guest") 
    (initialLog, initialSeqNum, None, initialPrefs, None, Some("Guest")) 
  }
}
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2609)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.isSelfSym(SymDenotations.scala:715)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:330)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1636)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1638)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1669)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1671)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:454)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1677)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1636)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1638)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1675)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$13(ExtractSemanticDB.scala:391)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:386)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:348)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1636)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1638)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1669)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1724)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:354)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$11(ExtractSemanticDB.scala:377)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:377)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1728)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1642)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:351)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$1(ExtractSemanticDB.scala:315)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:315)
	dotty.tools.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:36)
	dotty.tools.pc.ScalaPresentationCompiler.semanticdbTextDocument$$anonfun$1(ScalaPresentationCompiler.scala:242)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner