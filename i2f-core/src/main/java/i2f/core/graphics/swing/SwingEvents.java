package i2f.core.graphics.swing;

import i2f.core.annotations.remark.Ref;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;

/**
 * @author ltb
 * @date 2022/6/20 10:52
 * @desc
 */
public class SwingEvents {
    /**
     * 滚轮向上
     * @param e
     * @return
     */
    public static boolean wheelUp(MouseWheelEvent e){
        if(e.getWheelRotation()==1){ // 上滚
            return true;
        }
        return false;
    }

    /**
     * 滚轮向下
     * @param e
     * @return
     */
    public static boolean wheelDown(MouseWheelEvent e){
        if(e.getWheelRotation()==-1) {//下滚
            return true;
        }
        return false;
    }

    /**
     * 是否有效滚轮
     * @param e
     * @return
     */
    public static boolean wheelValid(MouseWheelEvent e){
        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL){
            return true;
        }
        return false;
    }

    /**
     * 是否是指定按键
     * @param e
     * @param keyCode
     * @return
     */
    public static boolean isKey(KeyEvent e,@Ref("KeyEvent.VK_") int keyCode){
        return e.getKeyCode()==keyCode;
    }

    /**
     * 是否同时按下Alt+指定键
     * @param e
     * @param keyCode
     * @return
     */
    public static boolean isAltKey(KeyEvent e,@Ref("KeyEvent.VK_") int keyCode){
        return e.isAltDown() && isKey(e,keyCode);
    }

    /**
     * 是否同时按下Ctrl+指定键
     * @param e
     * @param keyCode
     * @return
     */
    public static boolean isCtrlKey(KeyEvent e,@Ref("KeyEvent.VK_") int keyCode){
        return e.isControlDown() && isKey(e,keyCode);
    }

    /**
     * 是否同时按下Shift+指定键
     * @param e
     * @param keyCode
     * @return
     */
    public static boolean isShiftKey(KeyEvent e,@Ref("KeyEvent.VK_") int keyCode){
        return e.isShiftDown() && isKey(e,keyCode);
    }

}
