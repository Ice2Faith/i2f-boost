package i2f.workflow;

/**
 * @author Ice2Faith
 * @date 2024/3/22 15:57
 * @desc
 */
public interface FlowTask {
    boolean trigger(FlowNode node);

    void run(FlowNode node) throws Throwable;
}
