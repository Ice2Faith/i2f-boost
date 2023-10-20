package i2f.extension.guarder;

import i2f.core.cache.ICache;
import lombok.Data;

/**
 * @author Ice2Faith
 * @date 2023/8/21 9:34
 * @desc
 */
@Data
public class LoginGuarder extends ResourcesFailureGuarder {
    public LoginGuarder() {
        super("login");
    }

    public LoginGuarder(String resourcesType) {
        super(resourcesType);
    }

    public LoginGuarder(String resourcesType, ICache<String, Object> cache) {
        super(resourcesType, cache);
    }

    public LoginGuarder(String resourcesType, ICache<String, Object> cache, boolean enableResourcesGuarder, boolean enableIpGuarder) {
        super(resourcesType, cache, enableResourcesGuarder, enableIpGuarder);
    }
}
