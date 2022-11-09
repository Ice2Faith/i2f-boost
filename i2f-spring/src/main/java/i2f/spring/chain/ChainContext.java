package i2f.spring.chain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author ltb
 * @date 2022/10/27 9:16
 * @desc
 */
@Data
@NoArgsConstructor
public class ChainContext {
    protected Set<String> includesResolver;
    protected Set<String> excludesResolver;
}
