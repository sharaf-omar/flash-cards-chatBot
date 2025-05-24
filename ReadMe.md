# Chatbot Engine and Chatbot GUI

An educational chatbot system built using Scala and ScalaFX, designed to help students, developers, and enthusiasts learn programming concepts through interactive explanations, quizzes, and performance analytics.

## 📚 Project Overview

This project includes two core components:

- **ChatbotEngine**: Handles logic for parsing input, delivering explanations, conducting quizzes, and analyzing performance.
- **ChatbotGUI**: Provides a user-friendly graphical interface built with ScalaFX for engaging with the engine.

---

## 🚀 Motivation

The chatbot aims to support programming education by:
- Offering concise explanations of key programming topics.
- Engaging users with quizzes to reinforce understanding.
- Providing performance analytics for tracking progress.

Topics covered include OOP, data structures, and programming languages like Python and Scala.

---

## 🧠 Design and Architecture

### ✅ Functional Programming Principles

- **Immutable Structures**: Used throughout (e.g., `ChatbotState`, `QuizQuestion`, `ExplanationData`).
- **Pure Functions**: Clear separation of concerns with modular, reusable code.
- **Higher-Order Functions**: Leveraged for data processing (`map`, `filter`, `foldLeft`, etc.).
- **Option Types**: Used for robust handling of missing/invalid data.

---

### 📊 Explanation and Quiz Data Design

- **Explanations**: Stored in `Map[String, ExplanationData]`, where each `ExplanationData` contains:
  - Two explanations
  - One example
  - One note

- **Quiz Questions**: Stored in `QuizBank` (`Map[String, List[QuizQuestion]]`), each `QuizQuestion` includes:
  - Question
  - Four options
  - Correct answer
  - Note
  - Explanation

- **Storage**: Data is loaded from `explanations.csv` and `questions.csv`.

---

### 🧩 Modular Components

| Component | Responsibility |
|----------|----------------|
| `parseInput` | Normalizes input |
| `determineIntent` | Identifies user intent |
| `generateExplanationResponse` | Handles explanation requests |
| `selectQuizQuestions` | Fetches questions |
| `presentQuizQuestion` | Formats questions |
| `evaluateQuizAnswer` | Interprets and evaluates answers |
| `summarizeQuizResults` | Reports quiz scores |
| `analyzeQuizPerformance` | Logs performance |

---

## 💡 Features

### 🔍 Flexible Input Handling

- Synonym recognition using canonical mapping (e.g., "regex" → "regular expression").
- Supports both letter (a–d) and free-form quiz answers using Levenshtein distance.

### 🖼️ GUI Features

- Built with ScalaFX.
- Components:
  - `TextArea` for conversation
  - `TextField` for input
  - Buttons for:
    - Sending messages
    - Toggling light/dark theme
    - Clearing chat
- Styled with CSS-like properties and icons.

---

## 🔧 Implementation Highlights

### 📘 Explanation Workflow

```scala
generateExplanationResponse(
  concept: String,
  explanations: Map[String, ExplanationData],
  lastActionCtx: LastActionContext
): Option[String]

## To run follow these steps

1. cd into the preferred version be it CLI or GUI (make sure you're not in any sub folders and in the main folder for any version)
2. run the following command sbt  "sbt clean compile run"


### Prerequisites
*   Java Development Kit (JDK) 11 or higher (JDK 17 preferred).
*   SBT (Scala Build Tool) installed.
*   Scala 3 (e.g., 3.3.3) - SBT will manage this based on `build.sbt`.
*   scalaFX