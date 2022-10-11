package i2f.spring.chain;

import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2022/9/20 15:15
 * @desc 链式执行器
 * 1.将本执行器挂载到其他执行器上，监听其他执行器的next事件
 * 2.当其他执行器执行next之后，将会把挂载到上面的执行器全部执行，调用task方法
 * 例如：
 * A
 * B attach A
 * C attach A
 * D attach B
 * ------------------
 * --A
 * |--B
 * |--D
 * |--C
 * 则A.next将会触发B.task和C.task的执行
 * 如果B.task调用了B.next
 * 则D.task也会被调用
 */
public interface IChainResolver {
    /**
     * 挂在到哪些执行器后面运行
     * 返回值就是挂载的执行器，当返回值中的这些执行器执行调用next之后，将会调用本注册器执行task方法的内容
     *
     * @return 执行器的类
     */
    Set<Class<? extends IChainResolver>> attach();

    /**
     * 执行这个处理器，并返回下一个处理器的参数
     *
     * @param parent 来源执行器，可能为空（被直接调用）
     * @param action 触发链式的动作或目的
     * @param params 参数，可能为空，不传递参数
     * @return
     */
    void task(IChainResolver parent, Object action, Object params);

    /**
     * 执行这个处理器
     *
     * @param action 触发链式的动作或目的
     * @param params 参数，可能为空，取决于本执行器的task返回值
     */
    void next(Object action, Object params, boolean async, boolean await, ExecutorService pool);

}
