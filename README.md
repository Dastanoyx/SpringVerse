# Springverse Architecture

## 1. Layered Architecture

The application follows a **layered architecture** with distinct layers for separation of concerns, making the application maintainable, testable, and scalable.

| Layer                  | Purpose                                                           |
|------------------------|-------------------------------------------------------------------|
| **Controller Layer**   | Handles HTTP requests and responses. Exposes endpoints for the client. |
| **Service Layer**      | Implements business logic. Handles external API calls and processes data. |
| **Persistence Layer**  | Manages database interactions (if required in the future).       |
| **Exception Layer**    | Centralized exception handling for graceful error reporting.     |
| **Configuration Layer**| Configures external services, application properties, and security. |
| **Utility Layer**      | Provides reusable helper methods and common functionalities.     |

---

## 2. Components

### **Controller Layer**
- **`AIController`**:
    - Exposes REST APIs for AI-powered functionalities.
    - Validates incoming requests and delegates processing to service classes.

#### Endpoints:
- `/api/v1/ai/sentiment` → Analyze sentiment.
- `/api/v1/ai/image` → Generate images.
- `/api/v1/ai/qa` → Answer questions based on context.
- `/api/v1/ai/summarize` → Summarize text.
- `/api/v1/ai/tts` → Convert text to speech.

---

### **Service Layer**
- Services extend the `BaseService` for consistent API request preparation.
- Implements specific logic for calling Hugging Face APIs:
    - **`SentimentAnalysisService`**: Processes text sentiment analysis.
    - **`ImageGenerationService`**: Generates images based on prompts.
    - **`QuestionAnsweringService`**: Fetches answers to context-based questions.
    - **`TextSummarizationService`**: Summarizes blocks of text.
    - **`TextToSpeechService`**: Converts text input into speech.

---

### **Base Service**
- `BaseService`: Abstract class providing common functionality for API requests.
    - Prepares WebClient requests with base URLs and authorization headers.
    - Centralizes reusable logic for external API communication.

---

### **Exception Layer**
- **GlobalExceptionHandler**:
    - Centralized handling of exceptions across the application.
    - Converts exceptions into meaningful HTTP responses with appropriate status codes.
    - Handles custom exceptions like:
        - `AIServiceException`: Issues specific to AI services.
        - `InvalidInputException`: Handles client-side errors (e.g., missing inputs).
        - `ServiceProcessingException`: Covers issues during data processing.

---

### **Configuration Layer**
- **SecurityConfig**:
    - Configures Spring Security with HTTP Basic Authentication.
    - Protects all `/api/v1/ai/**` endpoints.
- **Application Configuration**:
    - Reads application properties (`application.yml`) for:
        - Hugging Face API key.
        - Server port.
        - Security credentials.

---

### **Utility Layer**
- **Helper Utilities** (if needed):
    - JSON parsing (e.g., `ObjectMapper` usage).
    - Logging helpers.
    - Retry mechanisms and circuit breakers (via Resilience4j or Spring Retry).

---

## 3. Data Flow

### Example: **Question Answering API (`/api/v1/ai/qa`)**

1. **Client Request**:
    - A client sends a POST request with `question` and `context` in the body.

   ```json
   {
       "question": "What is Spring Boot?",
       "context": "Spring Boot is an open-source framework for building microservices."
   }
    ```
2. **Controller Layer**:

   #### AIController:

    - Validates the input.
    - Delegates processing to `QuestionAnsweringService`.


3. **Service Layer**:

   #### QuestionAnsweringServiceImpl:

    - Calls the Hugging Face API using `BaseService`.
    - Parses the JSON response.
    - Extracts the answer and returns it.


4. **External API Interaction:**:
    - The Hugging Face endpoint processes the request and returns a response:

   ```json
   {
    "score": 0.6409,
    "start": 15,
    "end": 66,
    "answer": "an open-source framework for building microservices"
    }
    ```
5. **Response to Client:**:
    - The controller formats the processed result:

   ```json
   {
    "answer": "an open-source framework for building microservices",
    "score": 0.6409
    }
    ```
---

## 4. Technologies and Tools

| **Aspect**           | **Technology/Tool**         |
|-----------------------|-----------------------------|
| **Framework**         | Spring Boot                |
| **REST Communication**| Spring WebFlux (WebClient) |
| **Security**          | Spring Security            |
| **Configuration**     | YAML (application.yml)     |
| **Exception Handling**| @RestControllerAdvice      |
| **External APIs**     | Hugging Face Inference API |
| **Serialization**     | Jackson (ObjectMapper)     |
| **Testing**           | JUnit, MockMvc             |

---

## 5. Deployment and Scalability

### Deployment

- Deployed on a local server (currently `localhost:8080`).
- Could be containerized using Docker for easy deployment.

### Scalability Enhancements

- **Rate Limiting**:
    - Protect endpoints using rate-limiting libraries like `Bucket4j`.

- **Circuit Breaker**:
    - Use `Resilience4j` to gracefully degrade when Hugging Face APIs are unavailable.

- **Load Balancing**:
    - Add a load balancer if deploying in a distributed environment.

- **Caching**:
    - Use a cache (e.g., Redis) to store frequently requested results.

---

## 6. Summary Diagram

### Architecture Diagram

```plaintext
+-------------------+       +--------------------------+
|   Client (API)    | ----> |  AIController (REST API) |
+-------------------+       +--------------------------+
        |
        v
+-----------------------------------+
|         Service Layer             |
|-----------------------------------|
| - SentimentAnalysisService        |
| - ImageGenerationService          |
| - QuestionAnsweringService        |
| - TextSummarizationService        |
| - TextToSpeechService             |
+-----------------------------------+
        |
        v
+-----------------------------------+
|            BaseService            |
|-----------------------------------|
| Handles external API requests     |
+-----------------------------------+
        |
        v
+-----------------------------------+
|     External APIs (Hugging Face)  |
+-----------------------------------+
```
---

# Springverse: Empowering AI-driven APIs 🚀

Springverse is a modern, AI-powered Spring Boot application offering sentiment analysis, text summarization, question answering, image generation, and text-to-speech capabilities. Built with robust technologies like Spring WebFlux, Spring Security, and integrated with Hugging Face APIs, it’s designed to be scalable, secure, and ready for deployment.

Happy to share this exciting project! If you have suggestions, ideas, or want to collaborate, feel free to send me a message. Let’s innovate together! 💡✨
