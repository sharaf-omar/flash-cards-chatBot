error id: file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT%202/CTRL%20ALT%20CHATBOT/src/chatbot.scala:[13740..13741) in Input.VirtualFile("file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT%202/CTRL%20ALT%20CHATBOT/src/chatbot.scala", "import java.io._
import scala.io.Source
import scala.io.StdIn._
import scala.util.Random
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


val baseDir = "data/" 
val explanationsPath = s"${baseDir}explanations.csv"
val questionsPath = s"${baseDir}questions.csv" 
val userInputsPath = s"${baseDir}user_inputs.txt"

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

val greetingKeywords = List("hi", "hello", "hey", "greetings", "start")
val quizKeywords = List("quiz", "test", "questions", "practice")
val explainKeywords = List("explain", "what", "how", "why", "describe", "tell")
val farewellKeywords = List("exit", "bye", "goodbye", "quit")
val languageKeywords = List("c++", "cpp", "javascript", "js", "python", "py", "scala", "java", "c", "clang", "go", "golang")

val conceptKeywords = List(
    "algorithm", "scala", "class", "arrays", "variables", "dsa", "oop", "loops", "inheritance", "polymorphism", 
    "closure", "list", "array", "interface", "function", "pointer", "dictionary", "abstraction", "virtual function", "module", "prototype", 
    "structure", "lambda function", "encapsulation", "template", "set", "linked list", "exception handling", "list comprehension", "overloading", 
    "callback", "generator", "abstract class", "stack", "tuple", "promise", "destructor", "method overriding", "queue", "file i/o", 
    "arrow function", "stl", "dynamic memory allocation", "regular expression", "map", "graph", "filter", "exception", "constructor", 
    "binary tree", "class template", "global variable", "polymorphic method", "heap", "string formatting", "friend function", "overloaded constructor", 
    "hash table", "spread operator", "multithreading", "avl tree", "deque", "tree", "zip function", "trie", 
    "lambda expression", "abstract class", "binary search", "dataframe", "annotation", "unit testing", "type inference", "numpy array", 
    "garbage collection", "sorting", "api", "disjoint set", "enum", "kruskal's", "metaclass", "fenwick tree", "class method", "huffman coding", 
    "multiprocessing", "reflection", "bellman ford", "hash function", "segment tree", "floyd warshall", "lru cache", "websocket", "immutability", 
    "dataclasses", "bigint", "fold expression", "memory management", "divide and conquer", "pattern matching", "sealed class", "backtracking", 
    "bit manipulation", "server-side rendering", "dynamic programming", "refactoring", "scalability", "memory leak", "code reusability", "microservices", 
    "iterator", "debugging", "data compression", "data encryption", "graphql", "data visualization", "rest api", "data cleaning", 
    "automation", "authorization", "logic programming", "accessibility", "seo optimization", "code validation", "machine learning", 
    "reinforcement learning", "data security", "framework", "greedy algorithms", "recurrence relations", "big o notation", "relational databases", 
    "sql", "tables", "primary keys", "foreign keys", "indexes", "normalization", "denormalization", "joins", "inner join", "left join", "right join", 
    "full join", "queries", "select statement", "insert statement", "update statement", "delete statement", "transactions", "nosql", 
    "graph databases", "mongodb", "html", "css", "javascript", "dom", "ajax", "json", "xml", "apis", 
    "rest", "soap", "http methods", "status codes", "frontend", "backend", "frameworks", "react", "angular", "vue js", "node js", "express js", 
    "django", "flask", "cookies", "sessions", "websockets", "sdlc", "agile methodology", "scrum", "waterfall model", "version control", "git", 
    "github", "commits", "branches", "merging", "pull requests", "continuous integration", "continuous deployment", "testing", "procedural programming", 
    "declarative programming", "imperative programming", "concurrent programming", "parallel programming", "tcp ip", "http", "https", "dns", 
    "ip address", "ports", "sockets", "firewalls", "load balancers", "encryption", "hashing", "unsupervised learning", "classification", "regression", 
    "clustering", "deep learning", "neural network", "natural language processing", "computer vision", "try catch blocks", "pointers", "go routines", 
    "golang", "ides", "text editors", "virtual machines", "containers", "docker", "kubernetes", "cloud computing", "aws", "azure", "functors", 
    "pure functions", "function composition", "partial application", "tail recursion", "memoization", "closures", "currying", "higher order functions", 
    "time complexity", "space complexity","c++"
)

def generateKeywordVariations(concepts: List[String]): Map[String, List[String]] = {
  concepts.map { concept =>
    val canonical = concept.toLowerCase 
    val lowercase = concept.toLowerCase
    var variations = List(lowercase)

    if (!lowercase.endsWith("s") && !lowercase.contains(" ")) {
      variations = (lowercase + "s") :: variations
    }

    if (lowercase.endsWith("s") && !lowercase.contains(" ")) {
      variations = lowercase.stripSuffix("s") :: variations
    }

    val specialCases = Map(
      "oop" -> List("oop", "object oriented programming", "object-oriented", "object oriented"),
      "dsa" -> List("dsa", "data structures", "algorithms", "data structures and algorithms"),
      "cpp" -> List("cpp", "c++", "c plus plus", "cplusplus"),
      "javascript" -> List("javascript", "js", "ecmascript"),
      "python" -> List("python", "py"),
      "scala" -> List("scala"),
      "java" -> List("java"),
      "c" -> List("c", "clang", "c programming", "c lang", "ansi c"),
      "function" -> List("function", "functions", "func"),
      "regular expression" -> List("regular expression", "regex", "regexp"),
      "file i/o" -> List("file i/o", "file input/output", "file operations"),
      "stl" -> List("stl", "standard template library"),
      "abstract class" -> List("abstract class", "abstract classes"),
      "tcp ip" -> List("tcpip", "tcp ip"),
      "aws" -> List("aws", "amazon web services"),
      "golang" -> List("go", "golang", "go lang")
    )

    variations = specialCases.getOrElse(canonical, Nil) ++ variations

    if (concept.contains(" ")) {
      variations = lowercase.replace(" ", "") :: lowercase.replace("-", " ") :: variations
    }

    canonical -> variations.distinct.map(_.toLowerCase)
  }.toMap
}

val keywordVariations = generateKeywordVariations(conceptKeywords)

val variationToCanonical = keywordVariations.flatMap { 
  case (canonical, variations) => variations.map(variation => variation -> canonical)
}.toMap

val keywords = greetingKeywords ++ quizKeywords ++ explainKeywords ++ farewellKeywords ++ keywordVariations.keys.toList

def parseInput(input: String, keywordsList: List[String]): List[String] = {
  val normalizedInput = input.replaceAll("[^a-zA-Z0-9-_ ]", "")
                            .replaceAll("\\s+", " ")
                            .toLowerCase
                            .trim
  
  val fullPhraseMatches = conceptKeywords
    .filter(concept => normalizedInput.contains(concept.toLowerCase))
    .sortBy(-_.length)

  val orderedVariations = variationToCanonical.keys.toList.sortBy(-_.length)
  val (matchedKeywords, remainingText) = orderedVariations.foldLeft((Set.empty[String], normalizedInput)) {
    case ((matches, text), variation) =>
      if (text.contains(variation)) {
        val canonical = variationToCanonical(variation)
        (matches + canonical, text.replace(variation, " "))
      } else {
        (matches, text)
      }
  }

  val singleWordMatches = remainingText
    .split(" ")
    .filter(_.nonEmpty)
    .flatMap { word =>
      variationToCanonical.get(word).orElse {
        if (keywordsList.contains(word)) Some(word) else None
      }
    }
    .toSet
  
  (matchedKeywords ++ singleWordMatches).toList
}

def processIntent(
  tokens: List[String],
  greetingKeywords: List[String],
  quizKeywords: List[String],
  explainKeywords: List[String],
  farewellKeywords: List[String],
  conceptKeywords: List[String]
): (String, List[String], Option[String]) = {
  val isGreeting = tokens.intersect(greetingKeywords).nonEmpty
  val isQuiz = tokens.intersect(quizKeywords).nonEmpty
  val isExplain = tokens.intersect(explainKeywords).nonEmpty
  val isFarewell = tokens.intersect(farewellKeywords).nonEmpty
  val concepts = tokens.intersect(conceptKeywords)
  val language = tokens.intersect(languageKeywords).headOption

  (isExplain,isQuiz,concepts.nonEmpty) match
    case (_, true, _) if tokens.contains("cpp") => ("quiz", List("cpp"), None)
    case (_, true, true)  => ("quiz", List(), Some(concepts.head))
    case (_, true, _)     => ("quiz", List(), language)
    case (true, _, true)  => ("explain", concepts, language)
    case (true, _, _)     => ("explain", List(), language)
    case (_, _, true)     => ("explain", concepts, language)
    case _ => (isGreeting,isFarewell) match
      case (true, false) => ("greeting", List(), None)
      case (false, true) => ("farewell", List(), None)
      case _ => ("unknown", List(), None) 
  
}

case class ExplanationData(
  explanation: String,
  explanation2: String,
  example: String,
  note: String
)

def loadExplanations(filePath: String = explanationsPath): Map[(String, String), ExplanationData] = {
  try {
    Source
      .fromFile(filePath)
      .getLines()
      .drop(1)
      .map(_.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1))
      .collect {
        case Array(lang, concept, explanation, explanation2, example, note) if lang.nonEmpty && concept.nonEmpty =>
          ((concept.trim.toLowerCase, lang.trim.toLowerCase), ExplanationData(
            explanation.trim.stripPrefix("\"").stripSuffix("\""),
            explanation2.trim.stripPrefix("\"").stripSuffix("\""),
            example.trim.stripPrefix("\"").stripSuffix("\""),
            note.trim.stripPrefix("\"").stripSuffix("\"")
          ))
      }
      .toMap
  } catch {
    case e: Exception =>
      println(s"Failed to load explanations: ${e.getMessage}. Using an empty map.")
      Map.empty[(String, String), ExplanationData]
  }
}


def logUserInput(
  input: String,
  concept: Option[String] = None,
  question: Option[String] = None,
  filePath: String = userInputsPath
): Unit = {
  val timestamp = LocalDateTime.now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  val logEntry = (concept, question) match {
    case (Some(c), Some(q)) =>
      s"[$timestamp] Concept: $c, Question: $q, Answer: $input\n"
    case _ =>
      s"[$timestamp] Input: $input\n"
  }
  try {
    val writer = new PrintWriter(new FileOutputStream(new File(filePath), true))
    try {
      writer.write(logEntry)
    } finally { 
      writer.close()
    }
  } catch {
    case e: Exception =>
      println(s"Failed to log input: ${e.getMessage}")
  }
}

def getAvailableQuizTopics(): List[String] = {
  val allLines = try {
    Source.fromFile(questionsPath).getLines().toList
  } catch {
    case e: Exception =>
      println(s"Failed to load topics: ${e.getMessage}")
      return List.empty
  }
  val topics = allLines.tail
    .map(_.takeWhile(_ != ','))
    .distinct
  println(s"[DEBUG] All detected topics: ${topics.take(10)}...")
  Random.shuffle(topics).take(5)
}

case class QuestionData(
  question: String,
  choices: List[String],
  correctAnswer: String,
  generalNote: String
)

def 9QuestionsByConcept(
  concept: String,
  numQuestions: Int,
  filePath: String = questionsPath
): List[QuestionData] = {
  val canonicalConcept = variationToCanonical.getOrElse(concept.toLowerCase, concept.toLowerCase)

  try {
    val source = Source.fromFile(filePath)
    val questions = source.getLines().drop(1).flatMap { line =>
      try {
        val columns = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)
        val csvConcept = columns(0).trim.toLowerCase
        val csvCanonical = variationToCanonical.getOrElse(csvConcept, csvConcept)
        if (csvCanonical.equalsIgnoreCase(canonicalConcept)) {
          Some(QuestionData(
            columns(1).trim.stripPrefix("\"").stripSuffix("\""),
            List(columns(2), columns(3), columns(4), columns(5))
              .map(_.trim.stripPrefix("\"").stripSuffix("\"")),
            columns(6).trim.stripPrefix("\"").stripSuffix("\""),
            columns(7).trim.stripPrefix("\"").stripSuffix("\"")
          ))
        } else None
      } catch {
        case e: Exception =>
          println(s"Error parsing row in $filePath: ${e.getMessage}")
          None
      }
    }.toList
    source.close()
    Random.shuffle(questions).take(math.min(numQuestions, questions.size))
  } catch {
    case e: Exception =>
      println(s"Failed to load questions: ${e.getMessage}")
      List.empty
  }
}

def quiz(
  concepts: List[String],
  numQuestions: Int = 5,
  questionFile: String = questionsPath,
  logFile: String = userInputsPath
): String = {
  if (concepts.isEmpty) {
    return "No concepts provided for the quiz. Try 'quiz python' or 'quiz encapsulation'."
  }

  println(s"[DEBUG] Quiz requested for concepts: ${concepts.mkString(",")}") // Add this line
  println(s"[DEBUG] Canonical concepts: ${concepts.map(c => variationToCanonical.getOrElse(c.toLowerCase, c.toLowerCase))}")
  val processedConcepts = concepts.map { 
    case c if c.equalsIgnoreCase("C++") || c.equalsIgnoreCase("cpp") => "C++"
    case c => c
  }
    

  val totalQuestions = math.min(numQuestions, 10)
  val questionsPerConcept = math.max(1, totalQuestions / concepts.length)
  val remainingQuestions = totalQuestions - (questionsPerConcept * concepts.length)

  val allQuestions = concepts.zipWithIndex.flatMap { case (concept, idx) =>
    val num = if (idx < remainingQuestions) questionsPerConcept + 1 else questionsPerConcept
    loadQuestionsByConcept(concept, num)
  }.take(totalQuestions)

  if (allQuestions.isEmpty) {
    val availableTopics = getAvailableQuizTopics()
    return s"No questions found for ${concepts.mkString(", ")}. Available topics include: ${availableTopics.take(5).mkString(", ")}  and more." 
  }

  println(s"Starting quiz with ${allQuestions.length} questions!")
  val (correctCount, totalCount) = allQuestions.foldLeft((0, 0)) { case ((correct, total), QuestionData(question, choices, correctAnswer, generalNote)) =>
    val concept = concepts.find(c => question.toLowerCase.contains(c.toLowerCase)).getOrElse("unknown")

    println(s"\nQuestion ${total + 1}: $question")
    choices.zipWithIndex.foreach { case (opt, idx) =>
      println(s"${(idx + 'a').toChar}. $opt")
    }
    print("Your answer (letter or full text): ")

    val userAnswerRaw = readLine().trim
    if(userAnswerRaw.toLowerCase == "exit") {
      println("Goodbye! Happy learning!")
      sys.exit()}
    val userAnswer = userAnswerRaw.toLowerCase match {
      case a if a.length == 1 && "abcd".contains(a) =>
        choices("abcd".indexOf(a))
      case a => a
    }

    logUserInput(userAnswerRaw, Some(concept), Some(question), logFile)

    val isCorrect = userAnswer.toLowerCase == correctAnswer.toLowerCase
    println(if (isCorrect) "Correct!" else s"Wrong. The correct answer is: $correctAnswer")

    (correct + (if (isCorrect) 1 else 0), total + 1)
  }

  s"You scored $correctCount/$totalCount!" + "\n\n" +
  suggestionPrompts(Random.nextInt(suggestionPrompts.length))
  
}

def greet(): String = {
  val greetingTemplates = List(
    "Hello, ready to learn something new today?",
    "Hey there! Let's dive into some programming concepts—what's on your mind?",
    "Hi! I'm here to help you with programming concepts. What would you like to explore?",
    "Greetings, learner! Want to quiz yourself or dive into a concept?",
    "Yo, what's up? Let's get started with some coding knowledge!"
  )
  greetingTemplates(Random.nextInt(greetingTemplates.length)) + "\n" +
  suggestionPrompts(Random.nextInt(suggestionPrompts.length))
}

def explanationFlow(
  concept: String,
  languageOpt: Option[String],
  explanations: Map[(String, String), ExplanationData],
  logFile: String = userInputsPath
): String = {
  val normalizedConcept = variationToCanonical.getOrElse(concept.toLowerCase, concept.toLowerCase)
  val explanationDataOpt = languageOpt.flatMap { lang =>
    explanations.get((normalizedConcept, lang.toLowerCase))
  }.orElse {
    explanations.keys
      .find { case (c, _) => c == normalizedConcept }
      .map(explanations(_))
  }

  explanationDataOpt match {
    case None =>
      val availableTopics = getAvailableQuizTopics()
      s"Sorry, I don't have an explanation for '$concept'. Try another concept like 'encapsulation' or 'inheritance'. Available topics include: ${availableTopics.mkString(", ")}."
    case Some(data) =>
      val template = responseTemplates(Random.nextInt(responseTemplates.length))
      val initialExplanation = template
        .replace("{concept}", concept)
        .replace("{response}", data.explanation)

      println(initialExplanation)
      if (data.note.nonEmpty) println(s"Tip: ${data.note}")
      println(clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", concept))

      @scala.annotation.tailrec
      def explanationLoop(state: String): String = {
        print("\n> ")
        val input = readLine().trim.toLowerCase
        logUserInput(input, Some(concept))

        if (farewellKeywords.contains(input)) {
          "Goodbye! Happy learning!"
        } else if (input.contains("yes") || input.contains("more") || input.contains("clarify") || input.contains("explain")) {
          val followUpTemplate = followUpTemplates(Random.nextInt(followUpTemplates.length))
          val followUpExplanation = followUpTemplate
            .replace("{concept}", concept)
            .replace("{response}", data.explanation2)
          println(followUpExplanation)
          println(clarificationPrompts(Random.nextInt(clarificationPrompts.length)).replace("{concept}", concept))
          explanationLoop("explained")
        } else if (input.contains("example") || input.contains("use case") || input.contains("show")) {
          val exampleTemplate = exampleTemplates(Random.nextInt(exampleTemplates.length))
          val exampleText = exampleTemplate
            .replace("{concept}", concept)
            .replace("{response}", data.example)
          println(exampleText)
          println("Would you like more clarification on this concept, or perhaps try a quiz on it?")
          explanationLoop("example_shown")
        } else if (input.contains("quiz") || input.contains("test")) {
          s"Alright, let’s start a quiz on $concept! Type 'next' to begin."
        } else {
          "Cool, glad I could help! " + suggestionPrompts(Random.nextInt(suggestionPrompts.length))
        }
      }

      explanationLoop("initial")
  }
}

def handleIntent(
  intentTuple: (String, List[String], Option[String]),
  explanations: Map[(String, String), ExplanationData]
): (String, Option[String]) = {
  val (intent, concepts, languageOpt) = intentTuple

  intent match {
    case "greeting" =>
      (greet(), None)

    case "farewell" =>
      ("Goodbye! Happy learning!", None)

    case "quiz" =>
      val normalizedLang = languageOpt.map(lang => variationToCanonical.getOrElse(lang.toLowerCase, lang))
      normalizedLang match {
        case Some(lang) => (s"quiz:$lang", Some(lang))
        case None => 
          val topics = getAvailableQuizTopics()
          (s"Please specify a topic for the quiz, like 'quiz python' or 'quiz inheritance'.\n" +
           s"Available topics include: ${topics.take(5).mkString(", ")}...", None)
      }

    case "explain" =>
      if (concepts.isEmpty) {
        ("Please specify a concept to explain, like 'explain encapsulation'.", None)
      } else {
        val concept = concepts.head
        (s"explain:$concept", Some(concept))
      }

    case "unknown" =>
      ("Oops, I didn’t quite get that. Try 'hi', 'quiz <topic>', or 'explain <concept>'. " +
       suggestionPrompts(Random.nextInt(suggestionPrompts.length)), None)
  }
}

def processUserInput(
  input: String,
  explanations: Map[(String, String), ExplanationData],
  state: Option[String],
  logFile: String = userInputsPath
): (String, Option[String]) = {
  logUserInput(input)

  if (farewellKeywords.contains(input.toLowerCase)) {
    ("Goodbye! Happy learning!", None)
  } else if (input.trim.toLowerCase == "clear") {
    ("State is cleared! Ready to learn more. Try 'quiz' or 'explain <concept>'.", None)
  } else {
    state match {
      case Some(s) if s.startsWith("quiz:") && input.trim.toLowerCase == "next" =>
        val topic = s.stripPrefix("quiz:")
        val concepts = List(topic)
        (quiz(concepts), None)

      case Some(s) if s.startsWith("explain:") =>
        val concept = s.stripPrefix("explain:")
        val lang = Option(variationToCanonical.getOrElse(concept.toLowerCase, concept)).filter(languageKeywords.contains)
        (explanationFlow(concept, lang, explanations), None)

      case _ =>
        val tokens = parseInput(input, keywords)
        val intent = processIntent(tokens, greetingKeywords, quizKeywords, explainKeywords, farewellKeywords, conceptKeywords)
        val (response, newState) = handleIntent(intent, explanations)

        if (response.startsWith("quiz:")) {
          val topic = response.stripPrefix("quiz:")
          val concepts = List(topic)
          (quiz(concepts), None)

        } else if (response.startsWith("explain:")) {
          val concept = response.stripPrefix("explain:")
          val lang = Option(variationToCanonical.getOrElse(concept.toLowerCase, concept)).filter(languageKeywords.contains)
          (explanationFlow(concept, lang, explanations), None)

        } else {
          (response, newState)
        }
    }
  }
}

def runChatbot(explanations: Map[(String, String), ExplanationData]): Unit = {
  println("Welcome to the Programming Concepts Flash-Cards Chatbot!")
  println("Type 'hi' to greet, 'quiz <language>' to start a quiz, 'explain <concept>' to learn, or 'exit' to quit.")

  @scala.annotation.tailrec
  def loop(state: Option[String]): Unit = {
    print("\n> ")
    val input = try {
      readLine()
    } catch {
      case e: Exception =>
        println(s"Error reading input: ${e.getMessage}. Please try again.")
        return
    }
    val (response, newState) = processUserInput(
      input,
      explanations,
      state
    )

    println(response)
    if (response != "Goodbye! Happy learning!") {
      loop(newState)
    }
  }

  loop(None)
}

@main def main(): Unit = {
  val explanations = loadExplanations()
  runChatbot(explanations)
}")
file:///C:/Users/dell/OneDrive%20-%20Egypt%20University%20of%20Informatics/Desktop/CTRL%20ALT%20CHATBOT%202/CTRL%20ALT%20CHATBOT/src/chatbot.scala:289: error: expected identifier; obtained intlit
def 9QuestionsByConcept(
    ^
#### Short summary: 

expected identifier; obtained intlit