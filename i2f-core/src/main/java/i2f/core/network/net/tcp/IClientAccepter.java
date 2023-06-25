package i2f.core.network.net.tcp;

import i2f.core.annotations.remark.Author;

import java.net.Socket;

@Author("i2f")
public interface IClientAccepter {
    void onClientArrive(Socket sock);
    void onServerClose();
}
