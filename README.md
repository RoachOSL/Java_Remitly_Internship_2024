# Validate AWS IAM Role Policy

This project implements a method to verify input JSON data against AWS IAM Role Policy definitions, as defined by AWS. It evaluates the `Resource` field within the JSON and returns `false` if it contains a single asterisk (*), and `true` otherwise. This can help ensure your AWS IAM policies meet your specified security standards.

## Getting Started

These instructions will guide you on how to set up and run the project on your local machine for development and testing purposes.

### Prerequisites

- Java JDK 21.0.1 (Installation guide: [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html))
- Gradle (Installation guide: [Gradle](https://gradle.org/install/))

### Building the Project

1. Clone the repository to your local machine.
2. Navigate to the project directory where the `build.gradle` file is located.
3. Run the following command to build the project including all dependencies:

    ```bash
    ./gradlew shadowJar
    ```

   This command generates an executable JAR file `Validate_AWS-1.0.jar` in the `build/libs` directory.

### Running the Application

You can run the application in two ways:

1. **Using the Command Line:**
   After building the project, use one of the following commands to run the JAR file:

    ```bash
    java -jar build/libs/Validate_AWS-1.0.jar "{Your JSON String formatted according to AWS::IAM::Role Policy}"
    ```

   or

    ```bash
    java -jar build/libs/Validate_AWS-1.0.jar "/absolute/path/to/json/file.json"
    ```

   For example:

    ```bash
    java -jar build/libs/Validate_AWS-1.0.jar "{\"PolicyName\": \"ExamplePolicy\", \"PolicyDocument\": {\"Version\": \"2012-10-17\", \"Statement\": [{\"Sid\": \"Stmt1\", \"Effect\": \"Allow\", \"Action\": [\"ec2:StartInstances\", \"ec2:StopInstances\"], \"Resource\": \"*\"}]}}"
    ```

2. **Running in an IDE:**
   Alternatively, run the project directly within an IDE like IntelliJ IDEA:

   - Open the project.
   - Navigate to `IDEConfigurationRunner.java` in the `dev.Roach` package.
   - Right-click and select `Run 'IDEConfigurationRunner.main()'`.

### Testing

Run the following command in the main project folder to execute unit tests:

```bash
./gradlew test
