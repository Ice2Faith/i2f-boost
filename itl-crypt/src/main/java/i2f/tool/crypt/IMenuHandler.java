package i2f.tool.crypt;

public interface IMenuHandler {
    String name();

    void execute(String[] args) throws Exception;
}
