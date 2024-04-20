package util;

import dev.Roach.exceptions.JSONValidationException;
import dev.Roach.util.ValidateJSONRolePolicyUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ValidateJsonRolePolicyUtilTest {

    private ValidateJSONRolePolicyUtil validateJSONRolePolicyUtil;

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        validateJSONRolePolicyUtil = new ValidateJSONRolePolicyUtil();
        tempFile = Files.createTempFile("test", ".json");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void validateJsonRolePolicyUtilShouldHandleFileInputAsFilePath() throws IOException, JSONValidationException {
        String jsonContent = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "FileAccess",
                                "Effect": "Allow",
                                "Action": ["s3:ListBucket"],
                                "Resource": "arn:aws:s3:::example_bucket/*"
                            }
                        ]
                    }
                }
                """;
        Files.writeString(tempFile, jsonContent);

        assertTrue(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(tempFile.toString()),
                "Should handle file path input and return true for valid JSON without an asterisk");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseForFileWithSingleAsterisk() throws IOException, JSONValidationException {

        String jsonContent = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "ExampleStatement",
                                "Effect": "Allow",
                                "Action": ["s3:GetObject"],
                                "Resource": "*"
                            }
                        ]
                    }
                }
                """;
        Files.writeString(tempFile, jsonContent);

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(tempFile.toString()),
                "Should return false when file path leads to JSON with a single asterisk in Resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldHandleEmptyJSON() {
        String emptyJSON = "{}";
        Assertions.assertThrows(JSONValidationException.class, () -> {
            validateJSONRolePolicyUtil.validateJSONFromStringAndFile(emptyJSON);
        }, "Should throw an exception for empty JSON input");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWithMixedResourceTypes() throws JSONValidationException, IOException {
        String mixedResourceJson = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "MixedResourceTypeAccess",
                                "Effect": "Allow",
                                "Action": ["s3:GetObject", "s3:PutObject"],
                                "Resource": ["arn:aws:s3:::example_bucket/some/path", "*", "arn:aws:s3:::example_bucket/another/path"]
                            }
                        ]
                    }
                }
                """;
        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(mixedResourceJson),
                "Should return false if mixed resource types include an asterisk");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsterisk() throws JSONValidationException, IOException {

        String testJSON = """
                {
                    "PolicyName": "root",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "IamListAccess",
                                "Effect": "Allow",
                                "Action": [
                                    "iam:ListRoles",
                                    "iam:ListUsers"
                                ],
                                "Resource": "*"
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(testJSON), "Should return false if one asterisk in resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnTrueWhenThereIsNoSingleAsterisk() throws JSONValidationException, IOException {

        String comprehensiveTestJSON = """
                {
                    "PolicyName": "ComprehensivePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "FullAccess",
                                "Effect": "Allow",
                                "Action": ["ec2:StartInstances", "ec2:StopInstances"],
                                "Resource": ["arn:aws:ec2:::instance/*"],
                                "Condition": {
                                    "StringLike": {"aws:username": "user_*"}
                                },
                                "Principal": {
                                    "AWS": "arn:aws:iam::123456789012:root"
                                },
                                "NotPrincipal": {
                                    "AWS": "arn:aws:iam::123456789012:some-service-role"
                                }
                            }
                        ]
                    }
                }
                """;

        assertTrue(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(comprehensiveTestJSON), "Should return true if there is none asterisk in resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldThrowExceptionWhenThereIsCorruptedJson() {

        String corruptedJson = """
                {
                    "PolicyName": "ComprehensivePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement":
                            {
                                "Sid": "FullAccess",
                                "Effect": "Allow",
                                "Action": ["ec2:StartInstances", "ec2:StopInstances"],
                                "Resource": "fafa",
                                "Condition": {
                                    "StringLike": {"aws:username": "user_*"}
                                },
                                "Principal":
                                    "AWS": "arn:aws:iam::123456789012:root"
                                },
                                "NotPrincipal": {
                                    "AWS": "arn:aws:iam::123456789012:some-service-role"
                                }
                            }
                }
                """;
        JSONValidationException thrown = assertThrows(
                JSONValidationException.class,
                () -> validateJSONRolePolicyUtil.validateJSONFromStringAndFile(corruptedJson),
                "Expected validateJSON() to throw JSONValidationException, but it did not"
        );


        assertTrue(thrown.getMessage().contains("An error occurred during JSON deserialization"),
                "Unexpected exception message: " + thrown.getMessage());


    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenAsteriskIsWithinResourceArray() throws JSONValidationException, IOException {
        String jsonWithAsteriskInResourceArray = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "MixedResourceAccess",
                                "Effect": "Allow",
                                "Action": ["s3:GetObject", "s3:PutObject"],
                                "Resource": [
                                    "arn:aws:s3:::example_bucket/some/path",
                                    "arn:aws:s3:::example_bucket/another/path",
                                    "*",
                                    "arn:aws:s3:::example_bucket/yet/another/path"
                                ]
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithAsteriskInResourceArray),
                "Should return false if an asterisk is present anywhere in the Resource array");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithMultipleStatements() throws JSONValidationException, IOException {

        String jsonMultipleStatements = """
                {
                    "PolicyName": "root",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "IamListAccess",
                                "Effect": "Allow",
                                "Action": [
                                    "iam:ListRoles",
                                    "iam:ListUsers"
                                ],
                                "Resource": "arn:aws:iam::123456789012:role/S3Access"
                            },
                            {
                                "Sid": "IamListAccessWithAsterisk",
                                "Effect": "Allow",
                                "Action": [
                                    "iam:ListRoles",
                                    "iam:ListUsers"
                                ],
                                "Resource": "*"
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonMultipleStatements),
                "Should return false if one asterisk in resource even with multiple statements");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithArrayOfAction() throws JSONValidationException, IOException {

        String jsonWithArrayOfAction = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "ExampleStatement1",
                                "Effect": "Allow",
                                "Action": [
                                    "s3:GetObject",
                                    "s3:PutObject"
                                ],
                                "Resource": "*"
                            }
                        ]
                    }
                }
                """;


        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithArrayOfAction), "Should return false if one asterisk in resource with array of actions");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithNotAction() throws JSONValidationException, IOException {


        String jsonWithNotAction = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "ExampleStatement2",
                                "Effect": "Deny",
                                "NotAction": [
                                    "iam:ChangePassword"
                                ],
                                "Resource": "*"
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithNotAction), "Should return false if one asterisk in resource with NotAction");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnTrueWithNotResource() throws JSONValidationException, IOException {

        String jsonWithNotResource = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "ExcludeSensitiveRoles",
                                "Effect": "Deny",
                                "Action": "iam:DeleteRole",
                                "NotResource": [
                                    "arn:aws:iam::123456789012:role/BaseRole",
                                    "arn:aws:iam::123456789012:role/SecureRole"
                                ]
                            }
                        ]
                    }
                }
                """;

        assertTrue(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithNotResource), "Should return true if there is no resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithConditionMap() throws JSONValidationException, IOException {

        String jsonWithCondition = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "AllowRemoveMfaOnlyIfRecentMfa",
                                "Effect": "Allow",
                                "Action": [
                                    "iam:DeactivateMFADevice"
                                ],
                                "Resource": "*",
                                "Condition": {
                                    "NumericLessThanEquals": {
                                        "aws:MultiFactorAuthAge": "3600"
                                    }
                                }
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithCondition), "Should return false with condition when there is one asterisk in resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithPrincipalString() throws JSONValidationException, IOException {

        String jsonWithPrincipalString = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "SpecificUserAccess",
                                "Effect": "Allow",
                                "Action": [
                                    "s3:ListBucket"
                                ],
                                "Resource": "*",
                                "Principal": "arn:aws:iam::123456789012:user/specific-user"
                            }
                        ]
                    }
                }
                """;
        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithPrincipalString), "Should return false with String principal if there is one asterisk in resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithPrincipalList() throws JSONValidationException, IOException {

        String jsonWithNotPrincipalList = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "DenyExceptForTheseUsers",
                                "Effect": "Deny",
                                "Action": [
                                    "ec2:TerminateInstances"
                                ],
                                "Resource": "*",
                                "NotPrincipal": [
                                    "arn:aws:iam::123456789012:user/exception-user1",
                                    "arn:aws:iam::123456789012:user/exception-user2"
                                ]
                            }
                        ]
                    }
                }
                """;
        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithNotPrincipalList), "Should return false with List principal if there is one asterisk in resource");
    }

    @Test
    void validateJsonRolePolicyUtilShouldReturnFalseWhenSingleAsteriskWithPrincipalMap() throws JSONValidationException, IOException {

        String jsonWithPrincipalMap = """
                {
                    "PolicyName": "examplePolicy",
                    "PolicyDocument": {
                        "Version": "2012-10-17",
                        "Statement": [
                            {
                                "Sid": "FederatedUserAccess",
                                "Effect": "Allow",
                                "Action": [
                                    "dynamodb:GetItem"
                                ],
                                "Resource": "*",
                                "Principal": {
                                    "Federated": "www.example.com"
                                }
                            }
                        ]
                    }
                }
                """;

        assertFalse(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(jsonWithPrincipalMap), "Should return false with Map principal if there is one asterisk in resource");
    }


}
