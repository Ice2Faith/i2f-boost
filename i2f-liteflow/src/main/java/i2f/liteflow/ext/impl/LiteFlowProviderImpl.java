package i2f.liteflow.ext.impl;

import i2f.liteflow.ext.LiteFlowProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/6/7 18:54
 * @desc
 */
@Service
public class LiteFlowProviderImpl implements LiteFlowProvider {

    @Override
    public Set<String> getPersonOrgans(String oper) {
        return new HashSet<>(Arrays.asList("hr"));
    }
}
