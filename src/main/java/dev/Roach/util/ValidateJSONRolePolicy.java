package dev.Roach.util;

import dev.Roach.datamodel.JSONRolePolicy;
import dev.Roach.mapper.JSONRolePolicyMapper;

public class ValidateJSONRolePolicy {
    public boolean validateJSON(String json) {
        JSONRolePolicy jsonRolePolicy = JSONRolePolicyMapper.deserializeJSON(json);
        return !jsonRolePolicy.getPolicyDocument().getStatement().get(0).getResource().equals("*");
    }
}
