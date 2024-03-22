package i2f.workflow.test;

import i2f.workflow.WorkFlow;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ice2Faith
 * @date 2024/3/22 16:36
 * @desc
 */
public class TestFlow {
    public static void main(String[] args) throws Exception {
        WorkFlow.flow()
                .add("0","中国",new TestTask(), Arrays.asList())
                .add("0-1","福建",new TestTask(),Arrays.asList("0"))
                .add("0-2","浙江",new TestTask(),Arrays.asList("0"))
                .add("0-3","广东",new TestTask(),Arrays.asList("0"))
                .add("0-4","上海",new TestTask(),Arrays.asList("0"))
                .add("0-1-1","福州",new TestTask(),Arrays.asList("0-1"))
                .add("0-1-2","厦门",new TestTask(),Arrays.asList("0-1"))
                .add("0-1-1-1","马尾",new TestTask(),Arrays.asList("0-1-1"))
                .add("0-1-1&0-1-2","福建地市",new TestTask(),Arrays.asList("0-1-1","0-1-2"))
                .add("0-1&0-2&0-3&0-4","中国省份",new TestTask(),Arrays.asList("0-1","0-2","0-3","0-4"))
                .add("(0-1-1&0-1-2)&(0-1&0-2&0-3&0-4)","主要",new TestTask(),Arrays.asList("0-1-1&0-1-2","0-1&0-2&0-3&0-4","0-1-1-1","0-4"))
                .done()
                .pool(Executors.newCachedThreadPool())
                .run()
                .await();

    }
}
