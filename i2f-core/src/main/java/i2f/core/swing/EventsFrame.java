package i2f.core.swing;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author ltb
 * @date 2022/6/20 10:28
 * @desc
 */
public class EventsFrame extends JFrame
        implements
        MouseWheelListener, MouseMotionListener, MouseListener,
        KeyListener,
        WindowListener,WindowStateListener,WindowFocusListener,
        InputMethodListener{

    protected MouseWheelListener mouseWheelListener;
    protected MouseMotionListener mouseMotionListener;
    protected MouseListener mouseListener;
    protected KeyListener keyListener;
    protected WindowListener windowListener;
    protected WindowStateListener windowStateListener;
    protected WindowFocusListener windowFocusListener;
    protected InputMethodListener inputMethodListener;

    public void create(){
        initComponents();
        registryListeners();
    }

    public void initComponents(){

    }

    public void registryListeners(){
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        addWindowListener(this);
        addWindowStateListener(this);
        addWindowFocusListener(this);
        addInputMethodListener(this);
    }

    @Override
    public void inputMethodTextChanged(InputMethodEvent event) {
        if(inputMethodListener!=null){
            inputMethodListener.inputMethodTextChanged(event);
        }
    }

    @Override
    public void caretPositionChanged(InputMethodEvent event) {
        if(inputMethodListener!=null){
            inputMethodListener.caretPositionChanged(event);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(keyListener!=null){
            keyListener.keyTyped(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(keyListener!=null){
            keyListener.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(keyListener!=null){
            keyListener.keyReleased(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(mouseListener!=null){
            mouseListener.mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(mouseListener!=null){
            mouseListener.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(mouseListener!=null){
            mouseListener.mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(mouseListener!=null){
            mouseListener.mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if(mouseListener!=null){
            mouseListener.mouseExited(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(mouseMotionListener!=null){
            mouseMotionListener.mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(mouseMotionListener!=null){
            mouseMotionListener.mouseMoved(e);
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(mouseWheelListener!=null){
            mouseWheelListener.mouseWheelMoved(e);
        }
    }

    @Override
    public void windowGainedFocus(WindowEvent e) {
        if(windowFocusListener!=null){
            windowFocusListener.windowGainedFocus(e);
        }
    }

    @Override
    public void windowLostFocus(WindowEvent e) {
        if(windowFocusListener!=null){
            windowFocusListener.windowLostFocus(e);
        }
    }

    @Override
    public void windowStateChanged(WindowEvent e) {
        if(windowStateListener!=null){
            windowStateListener.windowStateChanged(e);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowOpened(e);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowClosing(e);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowClosed(e);
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowIconified(e);
        }
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowDeiconified(e);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowActivated(e);
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        if(windowListener!=null){
            windowListener.windowDeactivated(e);
        }
    }

}
