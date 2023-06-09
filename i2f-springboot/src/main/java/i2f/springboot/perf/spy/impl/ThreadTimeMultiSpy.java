package i2f.springboot.perf.spy.impl;

import i2f.springboot.perf.data.PerfIndex;
import i2f.springboot.perf.spy.PerfMultiSpy;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/6/8 9:20
 * @desc
 */
@Component
public class ThreadTimeMultiSpy implements PerfMultiSpy {
    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new LinkedList<>();
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] ids = bean.getAllThreadIds();
        int newCount = 0;
        int runnableCount = 0;
        int terminatedCount = 0;
        int blockedCount = 0;
        int waitingCount = 0;
        int timedWaitingCount = 0;
        int suspendedCount = 0;
        int inNativeCount = 0;
        for (long id : ids) {
            ThreadInfo thread = bean.getThreadInfo(id);
            String threadName = thread.getThreadName() + ":" + thread.getThreadId();
            Thread.State state = thread.getThreadState();
            int stateVal = 0;
            if (state == Thread.State.NEW) {
                stateVal = 0;
                newCount++;
            } else if (state == Thread.State.RUNNABLE) {
                stateVal = 4;
                runnableCount++;
            } else if (state == Thread.State.TERMINATED) {
                stateVal = -1;
                terminatedCount++;
            } else if (state == Thread.State.BLOCKED) {
                stateVal = 1;
                blockedCount++;
            } else if (state == Thread.State.WAITING) {
                stateVal = 3;
                waitingCount++;
            } else if (state == Thread.State.TIMED_WAITING) {
                stateVal = 2;
                timedWaitingCount++;
            }
            if (thread.isSuspended()) {
                suspendedCount++;
            }
            if (thread.isInNative()) {
                inNativeCount++;
            }
            String stateDesc = "(0:New,1:Blocked,2:TimedWaiting,3:Waiting,4:Runnable,-1:Terminated)";
            ret.add(new PerfIndex("Thread:single:state:" + threadName + stateDesc, stateVal));
        }
        ret.add(new PerfIndex("Thread:count:new", newCount));
        ret.add(new PerfIndex("Thread:count:runnable", runnableCount));
        ret.add(new PerfIndex("Thread:count:terminated", terminatedCount));
        ret.add(new PerfIndex("Thread:count:blocked", blockedCount));
        ret.add(new PerfIndex("Thread:count:waiting", waitingCount));
        ret.add(new PerfIndex("Thread:count:timedWaiting", timedWaitingCount));
        ret.add(new PerfIndex("Thread:count:suspended", suspendedCount));
        ret.add(new PerfIndex("Thread:count:native", inNativeCount));
        return ret;
    }
}
