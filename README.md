# Validate AWS IAM Role Policy

This project implements a method to verify input JSON data against AWS IAM Role Policy definitions, as defined by AWS. It evaluates the `Resource` field within the JSON and returns `false` if it contains a single asterisk (*), and `true` otherwise. This helps ensure your AWS IAM policies meet your specified security standards.

## Getting Started

These instructions will guide you on how to set up and run the project on your local machine for development and testing purposes.

### Prerequisites

- Java JDK 21.0.1 (Installation guide: [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html))
- Gradle (Installation guide: [Gradle](https://gradle.org/install/))

### Building the Project

1. Clone the repository to your local machine.
2. Navigate to the project directory in the terminal where the `build.gradle` file is located. The default directory is `your_path/Java_Remitly_Internship_2024`.
3. Run the following command to build the project, including all dependencies:

    ```bash
    ./gradlew shadowJar
    ```

   This command generates an executable JAR file `Validate_AWS-1.0.jar` in the `build/libs` directory.

### Running the Application

You can run the application in two ways:

1. **Using the Command Line:**
   After building the project, navigate to the `build/libs` directory and execute:

    ```bash
    java -jar Validate_AWS-1.0.jar "{Your JSON String formatted according to AWS::IAM::Role Policy}"
    ```

   or, if you have a JSON file:

    ```bash
    java -jar Validate_AWS-1.0.jar "/absolute/path/to/json/file.json"
    ```

   For example:

    ```bash
    java -jar Validate_AWS-1.0.jar "{\"PolicyName\": \"ExamplePolicy\", \"PolicyDocument\": {\"Version\": \"2012-10-17\", \"Statement\": [{\"Sid\": \"Stmt1\", \"Effect\": \"Allow\", \"Action\": [\"ec2:StartInstances\", \"ec2:StopInstances\"], \"Resource\": \"*\"}]}}"
    ```

2. **Running in an IDE:**
   If you prefer using an IDE such as IntelliJ IDEA:

   - Open the project.
   - Navigate to `IDEConfigurationRunner.java` in the `dev.Roach` package.
   - Right-click and choose `Run 'IDEConfigurationRunner.main()'`.

**Troubleshooting:**
If you encounter issues with running the project or there are dependency errors in the code, try invalidating the IntelliJ IDEA caches:
- Go to `File` > `Invalidate Caches...`
- Select `Invalidate and Restart`.

This should resolve issues related to the project setup in the IDE.

### Testing

Execute unit tests with the following command in the main project folder:

```bash
./gradlew test
