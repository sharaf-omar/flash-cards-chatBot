error id: 
file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:
	 -explanationsPath.
	 -explanationsPath#
	 -explanationsPath().
	 -scala/Predef.explanationsPath.
	 -scala/Predef.explanationsPath#
	 -scala/Predef.explanationsPath().
offset: 1222
uri: file:///C:/Users/dr_sh/OneDrive/Desktop/CTRL%20ALT%20CHATBOT/CTRL%20ALT%20CHATBOT/src/chatbot.scala
text:
```scala
import scala.io.{Source, StdIn}
import scala.util.{Try, Success, Failure, Random}
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// (Concept, Explanation1, Explanation2, Example, Note)
type ExplanationTuple = (String, String, String, String, String)

// (Concept, Question, Choices, CorrectAnswer, NoteForWrong)
type QuestionTuple = (String, String, List[String], String, String)

// Format required by spec: (QuestionText, Choices, CorrectAnswer)
type QuizQuestionSpec = (String, List[String], String)

// Represents application state: Option[ (StateTypeString, StateData) ]
type ChatState = Option[(String, Any)]

// Represents user intent: (IntentTypeString, IntentData)
type UserIntent = (String, Any)



val baseDir = Try(Paths.get("../data/")).getOrElse(Paths.get("data/")) // Adjust path as needed
val explanationsPath: String = baseDir.resolve("explanations.csv").toString
val questionsPath: String = baseDir.resolve("quiz.csv").toString
val logPath: String = baseDir.resolve("interaction_log.txt").toString
val maxQuizQuestions = 10
}
val baseDir = "../data/" 
val expla@@nationsPath = s"${baseDir}explanations.csv"
val questionsPath = s"${baseDir}questions.csv" 
val logPath = s"${baseDir}user_inputs.txt"
    
// Loads explanations from CSV into an immutable Map[String, ExplanationTuple]
def loadExplanations(filePath: String = Config.explanationsPath): Try[Map[String, ExplanationTuple]] = Try {
Using(Source.fromFile(filePath)) { source => // Using ensures source is closed
    source.getLines().drop(1).flatMap { line =>
    val cols = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)
                    .map(_.trim.stripPrefix("\"").stripSuffix("\""))
    if (cols.length >= 5 && cols(0).nonEmpty) {
        val concept = cols(0).toLowerCase
        Some(concept -> (concept, cols(1), cols(2), cols(3), cols(4)))
    } else {
        println(s"Warning: Skipping malformed line in explanations: $line")
        None
    }
    }.toMap
}
}.flatten // Handle potential exception during Using block as well

// Loads ALL questions into a List[QuestionTuple]
def loadAllQuestions(filePath: String = Config.questionsPath): Try[List[QuestionTuple]] = Try {
    Using(Source.fromFile(filePath)) { source =>
        source.getLines().drop(1).flatMap { line =>
            val cols = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)
                        .map(_.trim.stripPrefix("\"").stripSuffix("\""))
            if (cols.length >= 8 && cols(0).nonEmpty && cols(1).nonEmpty && cols(6).nonEmpty) {
                val choices = List(cols(2), cols(3), cols(4), cols(5)).filter(_.nonEmpty)
                if (choices.length >= 2) { // Need at least 2 choices
                val concept = cols(0).toLowerCase
                    Some((concept, cols(1), choices, cols(6), cols(7)))
                } else {
                    println(s"Warning: Skipping question with insufficient choices: $line")
                    None
                }
            } else {
                println(s"Warning: Skipping malformed line in questions: $line")
                None
            }
        }.toList // Convert iterator to list within Using block
    }
}.flatten

// Helper to manage resource closing automatically
private def Using[A <: { def close(): Unit }, B](resource: A)(block: A => B): Try[B] = {
Try(block(resource)). Płytkarz
    Try(resource.close()) // Close even if block fails
    // Propagate the original exception from block if it occurred
}
}
}


// --- Keyword Definitions & NLP ---
object Keywords {
// Concept keywords - canonical form (lowercase)
val conceptKeywords: Set[String] = Set(
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
"graph databases", "mongodb", "html", "css", "javascript", "dom", "ajax", "json", "xml", "apis",
"rest", "soap", "http methods", "status codes", "frontend", "backend", "frameworks", "react", "angular", "vue js", "node js", "express js",
"django", "flask", "cookies", "sessions", "websockets", "sdlc", "agile methodology", "scrum", "waterfall model", "version control", "git",
"github", "commits", "branches", "merging", "pull requests", "continuous integration", "continuous deployment", "testing", "procedural programming",
"declarative programming", "imperative programming", "concurrent programming", "parallel programming", "tcp ip", "http", "https", "dns",
"ip address", "ports", "sockets", "firewalls", "load balancers", "encryption", "hashing", "unsupervised learning", "classification", "regression",
"clustering", "deep learning", "neural network", "natural language processing", "computer vision", "try catch blocks", "pointers", "go routines",
"golang", "ides", "text editors", "virtual machines", "containers", "docker", "kubernetes", "cloud computing", "aws", "azure", "functors",
"pure functions", "function composition", "partial application", "tail recursion", "memoization", "closures", "currying", "higher order functions",
"time complexity", "space complexity"
)
val languageKeywords: Set[String] = Set("c++", "javascript", "js", "python", "py", "scala", "java", "c", "go", "golang")
val greetingKeywords: Set[String] = Set("hi", "hello", "hey", "greetings", "start", "yo", "wassup", "howdy")
val quizKeywords: Set[String] = Set("quiz", "test", "questions", "practice", "exam", "challenge")
val explainKeywords: Set[String] = Set("explain", "what", "describe", "tell", "define", "how", "why", "concept")
val farewellKeywords: Set[String] = Set("exit", "bye", "goodbye", "quit", "later", "stop")
val clarificationKeywords: Set[String] = Set("more", "clarify", "elaborate", "again", "further", "deeper", "huh", "yes") // Added 'yes' for simpler flow
val exampleKeywords: Set[String] = Set("example", "show", "instance", "use case", "practical")
val helpKeywords: Set[String] = Set("help", "hint", "clue", "suggest", "idea")
val abortKeywords: Set[String] = Set("abort", "cancel", "nevermind", "clear", "stop") // 'stop' can overlap, context matters

val keywordVariations: Map[String, String] = {
val baseConcepts = conceptKeywords ++ languageKeywords
baseConcepts.flatMap { concept =>
    var variations = Set(concept)
    if (!concept.endsWith("s") && concept.length > 2) variations += concept + "s"
    if (concept.endsWith("s") && concept.length > 3) variations += concept.stripSuffix("s")
    val aliases = concept match {
    case "python" => Set("py")
    case "javascript" => Set("js", "ecmascript")
    case "oop" => Set("object oriented programming", "object-oriented")
    case "dsa" => Set("data structures", "algorithms", "data structures and algorithms")
    case "c++" => Set("cpp", "cplusplus")
    case "golang" => Set("go", "go lang")
    case "c" => Set("clang")
    case "file i/o" => Set("file input output", "file operations")
    case "regular expression" => Set("regex", "regexp")
    case "tcp ip" => Set("tcpip")
    case "aws" => Set("amazon web services")
    case "rest api" => Set("restful api")
    case "big o notation" => Set("big o")
    case "abstract class" => Set("abstract classes")
    case "function" => Set("func", "method", "subroutine")
    case _ => Set.empty[String]
    }
    variations ++= aliases
    if (concept.contains(" ")) variations += concept.replace(" ", "")
    variations.map(_ -> concept)
}.toMap
}

val allKeywords: Set[String] = greetingKeywords ++ quizKeywords ++ explainKeywords ++
                            farewellKeywords ++ conceptKeywords ++ languageKeywords ++
                            clarificationKeywords ++ exampleKeywords ++ helpKeywords ++
                            abortKeywords ++ Set("a", "b", "c", "d") // Add single letters as potential answer keywords

/** Spec Function: parseInput(input: String): List[String] */
def parseInput(input: String): List[String] = {
val lowerInput = input.toLowerCase.trim
if (lowerInput.isEmpty) return List.empty

val sortedVariations = keywordVariations.keys.toList.sortBy(-_.length)
var remainingInput = " " + lowerInput + " "
val foundCanonicalConcepts = scala.collection.mutable.ListBuffer[String]()

sortedVariations.foreach { variation =>
    val paddedVariation = " " + variation + " "
    if (remainingInput.contains(paddedVariation)) {
        keywordVariations.get(variation).foreach { canonical =>
            foundCanonicalConcepts += canonical
            remainingInput = remainingInput.replace(paddedVariation, " ")
        }
    }
}
    val singleWordTokens = remainingInput.split("\\s+").filter(_.nonEmpty).toList
    val foundSingleKeywords = singleWordTokens.flatMap { token =>
    if (conceptKeywords.contains(token) || languageKeywords.contains(token) || allKeywords.contains(token)) Some(token)
    else keywordVariations.get(token) // Check if remaining token is a variation itself
    }.toSet

(foundCanonicalConcepts.toList ++ foundSingleKeywords).distinct
}
}


// --- Core Chatbot Logic (Pure Functions where possible) ---
object ChatbotLogic {

val random = new Random()

// --- Specifications Implementation ---

/** Spec Function: greetUser(): String */
def greetUser(): String = {
val templates = List(
    "Hello! Ready to dive into some programming concepts? Ask me to explain something or start a quiz!",
    "Hey there! What programming topic is on your mind today? (e.g., 'explain oop', 'quiz python')",
    "Hi! I'm your friendly programming concept chatbot. How can I help you learn today?",
    "Greetings! Want to test your knowledge with a quiz or get an explanation on a concept?"
)
templates(random.nextInt(templates.length)) + "\n" + suggestNextAction()
}

/** Spec Function: handleUserInput interpreted as determineIntent
* Returns UserIntent: (IntentString, Data) tuple
*/
def determineIntent(tokens: List[String], state: ChatState): UserIntent = {
    if (tokens.isEmpty) return ("unknown", None)

    val hasGreeting = tokens.exists(Keywords.greetingKeywords.contains)
    val hasFarewell = tokens.exists(Keywords.farewellKeywords.contains)
    val hasQuiz = tokens.exists(Keywords.quizKeywords.contains)
    val hasExplain = tokens.exists(Keywords.explainKeywords.contains)
    val hasClarify = tokens.exists(Keywords.clarificationKeywords.contains)
    val hasExample = tokens.exists(Keywords.exampleKeywords.contains)
    val hasHelp = tokens.exists(Keywords.helpKeywords.contains)
    val hasAbort = tokens.exists(Keywords.abortKeywords.contains)

    val concepts = tokens.filter(t => Keywords.conceptKeywords.contains(t) || Keywords.languageKeywords.contains(t)).distinct
    val languageHint = concepts.find(Keywords.languageKeywords.contains)
    val numberHint = tokens.flatMap(t => Try(t.toInt).toOption).headOption.filter(_ <= Config.maxQuizQuestions)

    state match {
    // --- Current State: Explaining ---
    case Some(("explaining", data)) =>
        val (currentConcept, _, _, _, stage) = data.asInstanceOf[(String, ExplanationTuple, String)]
        if (hasFarewell) ("farewell", None)
        else if (hasAbort) ("abort", None)
        else if (hasClarify && stage != "followup") ("clarify", currentConcept) // Avoid clarifying twice
        else if (hasExample && stage != "example") ("example", currentConcept)  // Avoid example twice
        else if (hasQuiz) ("quiz", (concepts.headOption.getOrElse(currentConcept) :: Nil, None, None)) // Quiz mentioned concept or current
        else if (hasExplain && concepts.nonEmpty) ("explain", (concepts, languageHint)) // Ask new topic
        else if (concepts.nonEmpty && !concepts.contains(currentConcept)) ("explain", (concepts, languageHint)) // Implicitly ask new topic
        else if (hasHelp) ("help", None)
        else ("unknown", None) // Default if in explanation state

    // --- Current State: Quiz ---
    case Some(("quiz", data)) =>
            val (topic, _, _, _, _) = data.asInstanceOf[(String, List[QuestionTuple], Int, Int, List[(QuestionTuple, String)])]
            if (hasFarewell) ("farewell", None)
            else if (hasAbort) ("abort", None)
            else if (hasHelp) ("help", None) // Ask for hint during quiz
            else if (tokens.length == 1 && "abcd".contains(tokens.head)) ("answer", tokens.head) // Letter choice
            else if (tokens.nonEmpty && tokens.forall(t => !Keywords.allKeywords.contains(t))) ("answer", tokens.mkString(" ")) // Assume free text if no keywords
            else if (hasQuiz && concepts.nonEmpty) ("abort", None) // Requesting new quiz -> abort current
            else if (hasExplain && concepts.nonEmpty) ("abort", None) // Requesting explanation -> abort current
            else ("unknown", None) // Confusing input during quiz

    // --- Current State: Idle ---
    case None => // Represents Idle state
            if (hasFarewell) ("farewell", None)
            else if (hasAbort) ("abort", None) // Abort is like clearing intention
            else if (hasQuiz && concepts.nonEmpty) ("quiz", (concepts, numberHint, languageHint))
            else if (hasQuiz) ("help", None) // "quiz" without topic -> Suggest topics
            else if (hasExplain && concepts.nonEmpty) ("explain", (concepts, languageHint))
            else if (hasExplain) ("help", None) // "explain" without topic -> Ask for concept
            else if (concepts.nonEmpty) ("explain", (concepts, languageHint)) // Implicitly ask about concept
            else if (hasGreeting) ("greet", None)
            else if (hasHelp) ("help", None)
            else ("unknown", None)
}
}

/** Spec Function: selectQuizQuestions(topic: String): List[QuizQuestionSpec]
 *  Takes number of questions and List[QuestionTuple].
 *  Returns Try[List[QuizQuestionSpec]]
 */
def selectQuizQuestions(
    topic: String,
    numQuestions: Int,
    allQuestions: List[QuestionTuple]
): Try[List[QuizQuestionSpec]] = Try {

    val canonicalTopic = Keywords.keywordVariations.getOrElse(topic.toLowerCase, topic.toLowerCase)

    val relevantQuestions = allQuestions.filter { qData =>
        val (qConcept, _, _, _, _) = qData
        val questionCanonicalConcept = Keywords.keywordVariations.getOrElse(qConcept.toLowerCase, qConcept.toLowerCase)
        questionCanonicalConcept == canonicalTopic
    }

    if (relevantQuestions.isEmpty) {
    Failure(new NoSuchElementException(s"No questions found for topic: $topic (canonical: $canonicalTopic)"))
    } else {
    val selectedData = random.shuffle(relevantQuestions).take(numQuestions)
    if (selectedData.isEmpty) {
        Failure(new NoSuchElementException(s"No questions found after filtering for topic: $topic"))
    } else {
        // Convert to the specified tuple format (question, choices, answer)
        val resultList = selectedData.map { case (_, qText, choices, correctAns, _) => (qText, choices, correctAns) }
        Success(resultList)
    }
    }
}.flatten

// Helper to get full QuestionTuple data for a topic
def getQuizDataForTopic(topic: String, numQuestions: Int, allQuestions: List[QuestionTuple]): List[QuestionTuple] = {
    val canonicalTopic = Keywords.keywordVariations.getOrElse(topic.toLowerCase, topic.toLowerCase)
    val relevantQuestions = allQuestions.filter { qData =>
        val (qConcept, _, _, _, _) = qData
        val questionCanonicalConcept = Keywords.keywordVariations.getOrElse(qConcept.toLowerCase, qConcept.toLowerCase)
        questionCanonicalConcept == canonicalTopic
    }
    random.shuffle(relevantQuestions).take(numQuestions)
}


/** Spec Function: presentQuizQuestion(question: QuizQuestionSpec): String */
def presentQuizQuestion(questionSpec: QuizQuestionSpec, questionNumber: Int, totalQuestions: Int): String = {
val (questionText, choices, _) = questionSpec
val optionsString = choices.zipWithIndex.map { case (opt, idx) =>
    s"${(idx + 'a').toChar}. $opt"
}.mkString("\n")
s"\nQuestion ${questionNumber}/${totalQuestions}: $questionText\n$optionsString\nYour answer (a, b, c, ... or full text): "
}

// Internal helper using QuestionTuple for better access to 'note'
def formatQuizQuestion(questionTuple: QuestionTuple, questionNumber: Int, totalQuestions: Int): String = {
    val (_, questionText, choices, _, _) = questionTuple
    val optionsString = choices.zipWithIndex.map { case (opt, idx) =>
        s"${(idx + 'a').toChar}. $opt"
    }.mkString("\n")
    s"\nQuestion ${questionNumber}/${totalQuestions}: $questionText\n$optionsString\nYour answer (a, b, c, ... or type 'hint', 'abort', 'exit'): "
}

/** Spec Function: evaluateQuizAnswer(userAnswer: String, correctAnswer: String): Boolean
 *  Extended slightly to use choices for letter matching.
 */
def evaluateQuizAnswer(userAnswer: String, correctAnswer: String, choices: List[String]): Boolean = {
val userAnswerLower = userAnswer.toLowerCase.trim
val correctAnswerLower = correctAnswer.toLowerCase.trim

val letterChoiceMatch = if (userAnswerLower.length == 1 && "abcdefghijklmnopqrstuvwxyz".contains(userAnswerLower)) {
    val index = userAnswerLower.head - 'a'
    choices.lift(index).map(_.toLowerCase.trim == correctAnswerLower)
} else {
    None
}
letterChoiceMatch.getOrElse {
    userAnswerLower == correctAnswerLower || isSimilar(userAnswerLower, correctAnswerLower)
}
}

private def levenshteinDistance(s1: String, s2: String): Int = { // Identical to previous version
val memo = scala.collection.mutable.Map[(Int, Int), Int]()
def compute(i: Int, j: Int): Int = memo.getOrElseUpdate((i, j), {
    if (i == 0) j else if (j == 0) i else {
    val cost = if (s1(i - 1) == s2(j - 1)) 0 else 1
    Seq(compute(i - 1, j) + 1, compute(i, j - 1) + 1, compute(i - 1, j - 1) + cost).min
    }
})
compute(s1.length, s2.length)
}

private def isSimilar(userAnswer: String, correctAnswer: String): Boolean = { // Identical to previous version
if (userAnswer.isEmpty || correctAnswer.isEmpty) return false
val userNum = Try(userAnswer.toDouble).toOption
val correctNum = Try(correctAnswer.toDouble).toOption
if (userNum.isDefined && correctNum.isDefined) return userNum == correctNum
val distance = levenshteinDistance(userAnswer, correctAnswer)
val length = math.max(userAnswer.length, correctAnswer.length)
if (length == 0) return true
val threshold = if (length <= 4) 0.3 else if (length <= 10) 0.25 else 0.2
(distance.toDouble / length) <= threshold
}

/** Spec Function: summarizeQuizResults(answers: List[Boolean]): String */
def summarizeQuizResults(answers: List[Boolean]): String = {
val correctCount = answers.count(_ == true)
val totalCount = answers.length
if (totalCount == 0) {
    "No questions were answered in the quiz."
} else {
    val score = (correctCount.toDouble / totalCount * 100).toInt
    s"Quiz finished! You scored $correctCount out of $totalCount ($score%)."
}
}

// Internal helper for richer summary using quiz state data tuple
// Input: (askedCount: Int, correctCount: Int, incorrectAnswers: List[(QuestionTuple, String)])
def generateQuizSummary(quizResultData: (Int, Int, List[(QuestionTuple, String)])): String = {
    val (totalCount, correctCount, incorrectAnswers) = quizResultData
    if (totalCount == 0) {
        "Quiz aborted before any questions were answered."
    } else {
        val score = Try( (correctCount.toDouble / totalCount * 100).toInt ).getOrElse(0)
        val summary = s"Quiz finished! You scored $correctCount out of $totalCount ($score%)."

        val wrongAnswerExplanations = incorrectAnswers.map { case (qTuple, userAnswer) =>
                val (_, qText, _, correctAns, note) = qTuple
                s"  - For \"$qText\":\n     Your answer: '$userAnswer'. Correct answer: '$correctAns'.\n     Note: ${if (note.nonEmpty) note else "No specific note."}"
        }

        if (wrongAnswerExplanations.nonEmpty) {
            summary + "\n\nHere are the explanations for the questions you missed:\n" + wrongAnswerExplanations.mkString("\n") +
            "\n\n" + suggestNextAction()
        } else if (totalCount > 0 && correctCount == totalCount) {
                summary + "\nGreat job! You got all questions right!" + "\n\n" + suggestNextAction()
        } else {
                summary + "\n\n" + suggestNextAction() // Handle cases like 0/0
        }
    }
}

/** Spec Function: generateResponse - implemented as helpers */
// Input stateData: (currentConcept: String, explanationTuple: ExplanationTuple, stage: String)
def generateExplanationResponse(stateData: (String, ExplanationTuple, String)): String = {
val (concept, explanationTuple, stage) = stateData
val (_, exp1, exp2, example, note) = explanationTuple
val responseText = stage match {
    case "initial" =>
    val templates = List(
        "Alright, let's talk about {concept}: {response}",
        "Here's the lowdown on {concept}: {response}"
    )
    val base = templates(random.nextInt(templates.length)).replace("{concept}", concept).replace("{response}", exp1)
    val noteText = if (note.nonEmpty) s"\nQuick Tip: $note" else ""
    base + noteText + "\n\nDoes that make sense? You can ask for 'more', an 'example', or try a 'quiz' on it."
    case "followup" =>
        val templates = List(
        "Okay, let's try explaining {concept} another way: {response}",
        "Still a bit fuzzy? Here's a different perspective on {concept}: {response}"
        )
        templates(random.nextInt(templates.length)).replace("{concept}", concept).replace("{response}", exp2) +
        "\n\nClearer now? Ask for an 'example' or another concept!"
    case "example" =>
        val templates = List(
        "Here’s a practical example of {concept}: {response}",
        "To see {concept} in action, consider this: {response}"
        )
        templates(random.nextInt(templates.length)).replace("{concept}", concept).replace("{response}", example) +
        "\n\nGot it? What would you like to do next?"
    case _ => "Unexpected explanation stage." // Error case
}
responseText
}

def generateHelpResponse(state: ChatState): String = state match {
    case None => suggestNextAction() // Idle state
    case Some(("explaining", data)) =>
        val (concept, _, stage) = data.asInstanceOf[(String, ExplanationTuple, String)]
        s"We are discussing '$concept' (currently at stage: $stage). You can ask for 'more', an 'example', try a 'quiz $concept', ask about another topic, or type 'abort'."
    case Some(("quiz", data)) =>
        val (topic, questions, asked, _, _) = data.asInstanceOf[(String, List[QuestionTuple], Int, Int, List[(QuestionTuple, String)])]
        val total = questions.length + asked
        s"You are on question ${asked+1}/$total of the '$topic' quiz. You can answer (a, b, ... or text), ask for a 'hint' (if available), 'abort' the quiz, or 'exit'."
    case _ => suggestNextAction() // Fallback
}

def suggestNextAction(): String = {
    val suggestions = List(
    "What would you like to do next? Try 'explain inheritance', 'quiz java', or 'exit'.",
    "Need ideas? You could ask me to 'explain polymorphism' or start a 'quiz python'.",
    "I can 'explain' concepts, 'quiz' you on topics, or just say 'bye'!"
    )
    suggestions(random.nextInt(suggestions.length))
}

def generateFarewellResponse(): String = {
    val templates = List("Goodbye! Happy learning!", "See you later! Keep practicing!", "Farewell! Hope that was helpful.")
    templates(random.nextInt(templates.length))
}

def generateUnknownResponse(): String = {
    val templates = List(
        "Hmm, I didn't quite understand that. Could you rephrase?",
        "Sorry, I'm not sure what you mean. Try asking differently?",
        "I'm still learning! Can you try asking in a different way?"
    )
    templates(random.nextInt(templates.length)) + " " + suggestNextAction()
}

def generateAbortResponse(prevState: ChatState): String = prevState match {
    case Some(("explaining", data)) =>
        val (concept, _, _) = data.asInstanceOf[(String, ExplanationTuple, String)]
        s"Okay, stopping the explanation of '$concept'. " + suggestNextAction()
    case Some(("quiz", data)) =>
        val (topic, _, _, _, _) = data.asInstanceOf[(String, List[QuestionTuple], Int, Int, List[(QuestionTuple, String)])]
        s"Okay, quiz on '$topic' aborted. " + suggestNextAction()
    case None => "Okay. " + suggestNextAction() // Was idle
    case _ => "Okay. " + suggestNextAction() // Fallback
}

// --- Analytics / Logging (Impure - Side Effects) ---

/** Spec Function: logInteraction(userInput: String, chatbotResponse: String): Unit */
def logInteraction(userInput: String, chatbotResponse: String, logPath: String = Config.logPath): Try[Unit] = Try {
    val timestamp = LocalDateTime.now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    val logEntry = s"[$timestamp] USER: $userInput\n[$timestamp] BOT:  $chatbotResponse\n---\n"
    // Ensure directory exists (simple approach)
    Try(Files.createDirectories(Paths.get(logPath).getParent))
    Files.write(Paths.get(logPath), logEntry.getBytes("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)
    ()
}.recoverWith { case e: IOException => println(s"Warning: Failed to write to log file $logPath - ${e.getMessage}"); Failure(e) }

// Spec Functions: getInteractionLog, analyzeInteractions, analyzeQuizPerformance would require file reading/parsing. Omitted for brevity based on focus.

// --- User Preferences (Impure - Side Effects) ---
private val userPreferences = scala.collection.mutable.Map[String, String]() // Simple in-memory store

/** Spec Function: storeUserPreferences(preference: String): Unit
 *  Assuming preference is a key-value pair string like "topic=python"
 */
def storeUserPreferences(preference: String): Unit = {
    preference.split("=", 2) match {
        case Array(key, value) =>
        println(s"[Debug] Storing preference: $key -> $value")
        userPreferences.put(key.trim, value.trim)
        case _ => println(s"[Debug] Could not parse preference to store: $preference")
    }
}

/** Spec Function: getUserPreferences(): Option[String]
 *  Assuming we retrieve by key.
 */
def getUserPreferences(key: String): Option[String] = {
    userPreferences.get(key)
}
}


// --- Main Application Entry Point ---
object ChatbotApp {

// Load data once at the start
val loadedExplanations: Map[String, ExplanationTuple] = DataLoader.loadExplanations() match {
case Success(data) => println(s"Successfully loaded ${data.size} explanations."); data
case Failure(e) => println(s"FATAL: Failed to load explanations: ${e.getMessage}. Exiting."); sys.exit(1)
}

val loadedQuestions: List[QuestionTuple] = DataLoader.loadAllQuestions() match {
    case Success(data) => println(s"Successfully loaded ${data.size} questions."); data
    case Failure(e) => println(s"WARNING: Failed to load questions: ${e.getMessage}. Quiz functionality limited."); List.empty
}

/** The main interaction loop. */
@scala.annotation.tailrec
def runChatbotLoop(currentState: ChatState): Unit = {
    // 1. Get User Input
    print("> ")
    val userInput = Try(StdIn.readLine()).getOrElse("")

    // 2. Parse Input
    val tokens = Keywords.parseInput(userInput)

    // 3. Determine Intent
    val intent = ChatbotLogic.determineIntent(tokens, currentState)

    // 4. Process Intent and Update State
    val (response, nextState) = processIntent(intent, currentState, userInput)

    // 5. Produce Output
    println(s"\n🤖 $response\n")

    // 6. Log Interaction
    ChatbotLogic.logInteraction(userInput, response).recover { case e => /* ignore logging failure */ }

    // 7. Loop or Exit
    if (intent._1 != "farewell") { // Check intent string directly
        runChatbotLoop(nextState)
    } else {
        println("\nChatbot session ended.")
    }
}


/** Processes the user's intent based on the current state.
 *  Returns (ChatbotResponseString, NextChatState)
 */
def processIntent(intent: UserIntent, currentState: ChatState, userInput: String): (String, ChatState) = {
val (intentType, intentData) = intent

(intentType, currentState) match {
    // --- Global Intents ---
    case ("farewell", _) => (ChatbotLogic.generateFarewellResponse(), None) // Exit signal is implicit now
    case ("greet", _) => (ChatbotLogic.greetUser(), None) // Reset to Idle (None)
    case ("abort", state) => (ChatbotLogic.generateAbortResponse(state), None) // Go back to Idle
    case ("help", state) => (ChatbotLogic.generateHelpResponse(state), state) // Stay in current state

    // --- Idle State Logic (currentState is None) ---
    case ("explain", None) =>
    val (concepts, _) = intentData.asInstanceOf[(List[String], Option[String])]
    if (concepts.nonEmpty) {
        val concept = concepts.head
        loadedExplanations.get(concept.toLowerCase) match {
            case Some(explTuple) =>
                val initialStateData = (concept, explTuple, "initial") // (concept, data, stage)
                val response = ChatbotLogic.generateExplanationResponse(initialStateData)
                (response, Some(("explaining", initialStateData)))
            case None =>
                (s"Sorry, I don't have an explanation for '$concept'. Try another. ${ChatbotLogic.suggestNextAction()}", None)
        }
    } else { // explain without concept
        (ChatbotLogic.generateHelpResponse(None), None) // Ask for topic
    }

    case ("quiz", None) =>
    val (concepts, numOpt, _) = intentData.asInstanceOf[(List[String], Option[Int], Option[String])]
        if (concepts.nonEmpty) {
            val concept = concepts.head
            val numQuestions = numOpt.getOrElse(5).min(Config.maxQuizQuestions)
            val questions = ChatbotLogic.getQuizDataForTopic(concept, numQuestions, loadedQuestions)
            if (questions.nonEmpty) {
                // State: (topic, remainingQuestions, askedCount, correctCount, incorrectList)
                val initialQuizStateData = (concept, questions.tail, 0, 0, List.empty[(QuestionTuple, String)])
                val response = ChatbotLogic.formatQuizQuestion(questions.head, 1, questions.length)
                (s"Okay, starting a ${questions.length} question quiz on '$concept'!\n" + response, Some(("quiz", initialQuizStateData)))
            } else {
                (s"Sorry, I couldn't find any questions for '$concept'. ${ChatbotLogic.suggestNextAction()}", None)
            }
        } else { // quiz without concept
            (ChatbotLogic.generateHelpResponse(None), None) // Ask for topic
        }

    // --- Explaining State Logic ---
    case ("clarify", Some(("explaining", stateData))) =>
        val (concept, explTuple, stage) = stateData.asInstanceOf[(String, ExplanationTuple, String)]
        if (stage != "followup") { // Check if already provided
            val newStateData = (concept, explTuple, "followup")
            (ChatbotLogic.generateExplanationResponse(newStateData), Some(("explaining", newStateData)))
        } else {
            ("I've already provided the follow-up explanation. Maybe try an 'example'?", currentState) // Stay in state
        }

    case ("example", Some(("explaining", stateData))) =>
        val (concept, explTuple, stage) = stateData.asInstanceOf[(String, ExplanationTuple, String)]
        if (stage != "example") { // Check if already provided
            val newStateData = (concept, explTuple, "example")
            (ChatbotLogic.generateExplanationResponse(newStateData), Some(("explaining", newStateData)))
        } else {
            ("I've already given the example for this concept. Ready for a 'quiz' or another topic?", currentState)
        }

    case ("quiz", Some(("explaining", stateData))) =>
        val (currentConcept, _, _) = stateData.asInstanceOf[(String, ExplanationTuple, String)]
        // Extract potential concept from quiz intent, default to current explanation concept
        val (quizConcepts, numOpt, langHint) = intentData.asInstanceOf[(List[String], Option[Int], Option[String])]
        val conceptToQuiz = quizConcepts.headOption.getOrElse(currentConcept)
        // Delegate to Idle handler for quiz start
        processIntent(("quiz", (List(conceptToQuiz), numOpt, langHint)), None, userInput)

    case ("explain", Some(("explaining", _))) =>
        // Asking for new explanation while explaining -> delegate to Idle handler
        processIntent(("explain", intentData), None, userInput)


    // --- Quiz State Logic ---
    case ("answer", Some(("quiz", stateData))) =>
        val userAnswer = intentData.asInstanceOf[String]
        val (topic, remainingQs, asked, correct, incorrectList) = stateData.asInstanceOf[(String, List[QuestionTuple], Int, Int, List[(QuestionTuple, String)])]

        remainingQs match {
            case currentQuestionTuple :: tail =>
                val (_, _, choices, correctAnswer, _) = currentQuestionTuple
                val isCorrect = ChatbotLogic.evaluateQuizAnswer(userAnswer, correctAnswer, choices)
                val feedback = if (isCorrect) "Correct!" else s"Incorrect. The answer was: $correctAnswer"
                val newIncorrect = if (isCorrect) incorrectList else incorrectList :+ (currentQuestionTuple, userAnswer)
                val newCorrectCount = if (isCorrect) correct + 1 else correct
                val newAskedCount = asked + 1

                tail match {
                case nextQ :: restOfTail =>
                    val totalQs = remainingQs.length + asked
                    val nextQNum = newAskedCount + 1
                    val response = feedback + "\n" + ChatbotLogic.formatQuizQuestion(nextQ, nextQNum, totalQs)
                    val newStateData = (topic, restOfTail, newAskedCount, newCorrectCount, newIncorrect)
                    (response, Some(("quiz", newStateData)))
                case Nil => // Last question answered
                    val quizSummaryData = (newAskedCount, newCorrectCount, newIncorrect)
                    (feedback + "\n\n" + ChatbotLogic.generateQuizSummary(quizSummaryData), None) // Go back to Idle
                }
            case Nil => // Should not happen if state is managed correctly, but handle defensively
                println("[Error] Answer intent received but no questions left in state.")
                (ChatbotLogic.generateUnknownResponse(), None) // Reset state
        }

    case ("help", Some(("quiz", stateData))) => // Hint request
        val (_, questions, _, _, _) = stateData.asInstanceOf[(String, List[QuestionTuple], Int, Int, List[(QuestionTuple, String)])]
        questions.headOption match {
            case Some(qTuple) =>
                val (_, _, _, _, note) = qTuple
                val hint = if (note.nonEmpty) s"Hint: $note" else "Sorry, no specific hint available for this question."
                (hint, currentState) // Stay in quiz state
            case None => ("No question currently active.", currentState) // Should not happen
        }

    // --- Fallback for Unknown/Unhandled ---
    case ("unknown", state) => (ChatbotLogic.generateUnknownResponse(), state) // Stay in current state
    case (_, state) => // Catch-all for intents not explicitly handled in the current state
    (ChatbotLogic.generateUnknownResponse() + " (I can't do that right now)", state)
}
}


@main def main(): Unit = {
println("Initializing Chatbot (Simple Function Version)...")
if (loadedExplanations.isEmpty) {
    println("Cannot run chatbot without explanation data.")
} else {
    println("\n" + ChatbotLogic.greetUser())
    Try(runChatbotLoop(None)).recover { // Start the loop in Idle state (None)
        case e: Exception => println(s"\nAn unexpected error occurred: ${e.getMessage}")
    }
}
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 