package dev.Roach;

import dev.Roach.util.ValidateJSONRolePolicy;

public class Main {
    public static void main(String[] args) {

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

        ValidateJSONRolePolicy validateJSONRolePolicy = new ValidateJSONRolePolicy();

        System.out.println(validateJSONRolePolicy.validateJSON(testJSON));

    }
}
