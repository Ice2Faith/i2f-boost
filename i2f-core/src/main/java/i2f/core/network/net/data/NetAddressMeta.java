package i2f.core.network.net.data;

import i2f.core.network.net.INetAddressMeta;

/**
 * @author ltb
 * @date 2022/3/26 18:50
 * @desc
 */
public class NetAddressMeta implements INetAddressMeta {
    protected String host;
    protected int port;
    public NetAddressMeta(){

    }
    public NetAddressMeta setHost(String host){
        this.host=host;
        return this;
    }
    public NetAddressMeta setPort(int port){
        this.port=port;
        return this;
    }
    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
