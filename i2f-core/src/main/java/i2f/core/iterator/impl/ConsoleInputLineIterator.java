package i2f.core.iterator.impl;

import java.util.Scanner;

public class ConsoleInputLineIterator extends AbsCollectIterator<String> {

    private String endingFlag = "#";

    Scanner scanner = new Scanner(System.in);

    public ConsoleInputLineIterator(String endingFlag) {
        this.endingFlag = endingFlag;
    }

    @Override
    protected boolean isEnding() {
        String line = scanner.nextLine();
        boolean rs = line.equals(endingFlag);
        if (!rs) {
            collect(line);
        }
        return rs;
    }

}
