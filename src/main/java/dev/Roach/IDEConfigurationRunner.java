package dev.Roach;

import dev.Roach.exceptions.JSONValidationException;
import dev.Roach.util.ValidateJSONRolePolicyUtil;

import java.io.IOException;

public class DefaultMain {
    //Absolute Path to you JSON file
    private static final String DEFAULT_JSON_PATH = "E:\\ProjektyIntelliJ\\Java_Remitly_Internship_2024\\src\\main\\resources\\policyWithoutSingleAsteriks.json";
    //Json string that u can always change, and it will work if u keep it formatted as AWS::IAM::Role Policy
    private static final String DEFAULT_JSON_STRING = """
            {
                "PolicyName": "ComprehensivePolicy",
                "PolicyDocument": {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Sid": "FullAccess",
                            "Effect": "Allow",
                            "Action": ["ec2:StartInstances", "ec2:StopInstances"],
                            "Resource": "*",
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

    public static void main(String[] args) throws JSONValidationException, IOException {

        ValidateJSONRolePolicyUtil validateJSONRolePolicyUtil = new ValidateJSONRolePolicyUtil();

        System.out.println(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(DEFAULT_JSON_PATH));

        System.out.println(validateJSONRolePolicyUtil.validateJSONFromStringAndFile(DEFAULT_JSON_STRING));

    }
}
