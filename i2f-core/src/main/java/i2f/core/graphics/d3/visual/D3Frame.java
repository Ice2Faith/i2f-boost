package i2f.core.graphics.d3.visual;

import i2f.core.graphics.d3.D3Painter;
import i2f.core.graphics.d3.projection.ID3Projection;
import i2f.core.graphics.d3.transform.impl.*;
import i2f.core.graphics.math.Calc;
import lombok.Data;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author ltb
 * @date 2022/6/19 16:52
 * @desc 操作指南
 * Alt+X/Y/Z+滚轮 进行绕X/Y/Z轴旋转
 * Ctrl+X/Y/Z+滚轮 进行X/Y/Z坐标移动
 * Shit+X/Y/Z+滚轮 单独对X/Y/Z轴进行缩放
 * 单独滚轮 等比例缩放
 * Ctrl+空格，重置所有变换
 */
@Data
public class D3Frame extends JFrame
        implements MouseWheelListener,
        MouseMotionListener,
        MouseListener,
        KeyListener {
  protected D3Canvas canvas;
  protected int width;
  protected int height;
  protected ID3Projection d3proj;

  protected OnDraw drawer;

  protected SpinTransform spin=new SpinTransform(false,0,0,0);
  protected MoveTransform mov=new MoveTransform(false,0,0,0);
  protected ScaleTransform scale=new ScaleTransform(false,1,1,1);
  protected MiscutAxisTransform miscus=new MiscutAxisTransform(false,0,0,0,0,0,0);
  protected ReflectAxisTransform reflecAxis=new ReflectAxisTransform(false,false,false,false);
  protected ReflectFlatTransform reflectFlat=new ReflectFlatTransform(false,false,false,false);

  protected boolean isDownX=false;
  protected boolean isDownY=false;
  protected boolean isDownZ=false;
  protected boolean isDownCtrl=false;
  protected boolean isDownShift=false;
  protected boolean isDownAlt=false;

  public D3Frame(int width,int height,OnDraw drawer,ID3Projection d3proj){
    this.width=width;
    this.height=height;
    this.d3proj=d3proj;
    this.drawer=drawer;

    setBounds(0,0,width,height);

    setResizable(false);
    setTitle("D3Frame");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    initComponents();
  }

  protected void initComponents(){
    canvas=new D3Canvas(new D3Painter(width,height,d3proj));
    canvas.setSize(width,height);
    getContentPane().add(canvas);

    canvas.painter.transforms.clear();
    canvas.painter.transforms.add(spin);
    canvas.painter.transforms.add(mov);
    canvas.painter.transforms.add(scale);
    canvas.painter.transforms.add(miscus);
    canvas.painter.transforms.add(reflecAxis);
    canvas.painter.transforms.add(reflectFlat);

    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addMouseListener(this);
    addKeyListener(this);

  }


  public void refresh(){
    if(drawer!=null){
      drawer.draw(this.canvas.painter,this.canvas,this);
    }
    this.canvas.repaint();
  }


  public  interface OnDraw{
    void draw(D3Painter painter,D3Canvas canvas,D3Frame frame);
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {

  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL){
      return;
    }
    boolean isAction=false;
    if(isDownAlt) { // Alt+X/Y/Z进行旋转
      if (isDownX) {
        if(e.getWheelRotation()==1){ // 上滚
          spin.sx+= Calc.angle2radian(5);
          spin.sx=Calc.regularRadian(spin.sx);
        }else if(e.getWheelRotation()==-1) {//下滚
          spin.sx-= Calc.angle2radian(5);
          spin.sx=Calc.regularRadian(spin.sx);
        }
        isAction=true;
      }
      if (isDownY) {
        if(e.getWheelRotation()==1){ // 上滚
          spin.sy+= Calc.angle2radian(5);
          spin.sy=Calc.regularRadian(spin.sy);
        }else if(e.getWheelRotation()==-1) {//下滚
          spin.sy-= Calc.angle2radian(5);
          spin.sy=Calc.regularRadian(spin.sy);
        }
        isAction=true;
      }
      if (isDownZ) {
        if(e.getWheelRotation()==1){ // 上滚
          spin.sz+= Calc.angle2radian(5);
          spin.sz=Calc.regularRadian(spin.sz);
        }else if(e.getWheelRotation()==-1) {//下滚
          spin.sz-= Calc.angle2radian(5);
          spin.sz=Calc.regularRadian(spin.sz);
        }
        isAction=true;
      }
    }
    if(isDownCtrl){ // Ctrl+X/Y/Z进行移动
      if (isDownX) {
        if(e.getWheelRotation()==1){ // 上滚
          mov.mx+= 5;
        }else if(e.getWheelRotation()==-1) {//下滚
          mov.mx-= 5;
        }
        isAction=true;
      }
      if (isDownY) {
        if(e.getWheelRotation()==1){ // 上滚
          mov.my+= 5;
        }else if(e.getWheelRotation()==-1) {//下滚
          mov.my-= 5;
        }
        isAction=true;
      }
      if (isDownZ) {
        if(e.getWheelRotation()==1){ // 上滚
          mov.mz+= 5;
        }else if(e.getWheelRotation()==-1) {//下滚
          mov.mz-= 5;
        }
        isAction=true;
      }
    }
    if(isDownShift){ // Shift+X/Y/Z进行单独缩放
      if (isDownX) {
        if(e.getWheelRotation()==1){ // 上滚
          scale.sx*= 1.01;
        }else if(e.getWheelRotation()==-1) {//下滚
          scale.sx*= 0.99;
        }
        isAction=true;
      }
      if (isDownY) {
        if(e.getWheelRotation()==1){ // 上滚
          scale.sy*= 1.01;
        }else if(e.getWheelRotation()==-1) {//下滚
          scale.sy*= 0.99;
        }
        isAction=true;
      }
      if (isDownZ) {
        if(e.getWheelRotation()==1){ // 上滚
          scale.sz*= 1.01;
        }else if(e.getWheelRotation()==-1) {//下滚
          scale.sz*= 0.99;
        }
        isAction=true;
      }
    }
    // 其他情况，则默认等比例缩放
    if(!isAction){
      if(e.getWheelRotation()==1){ // 上滚
        scale.sx*= 1.01;
        scale.sy*= 1.01;
        scale.sz*= 1.01;
      }else if(e.getWheelRotation()==-1) {//下滚
        scale.sx*= 0.99;
        scale.sy*= 0.99;
        scale.sz*= 0.99;
      }
      isAction=true;
    }

    if(isAction){
      refresh();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode()==KeyEvent.VK_X){
      isDownX=true;
    }
    if(e.getKeyCode()==KeyEvent.VK_Y){
      isDownY=true;
    }
    if(e.getKeyCode()==KeyEvent.VK_Z){
      isDownZ=true;
    }
    isDownCtrl=e.isControlDown();
    isDownShift=e.isShiftDown();
    isDownAlt=e.isAltDown();
  }

  @Override
  public void keyReleased(KeyEvent e) {
    if(e.getKeyCode()==KeyEvent.VK_X){
      isDownX=false;
    }
    if(e.getKeyCode()==KeyEvent.VK_Y){
      isDownY=false;
    }
    if(e.getKeyCode()==KeyEvent.VK_Z){
      isDownZ=false;
    }
    if(isDownCtrl) {
      if (e.getKeyCode() == KeyEvent.VK_SPACE) {
        spin.sx=0;
        spin.sy=0;
        spin.sz=0;

        mov.mx=0;
        mov.my=0;
        mov.mz=0;

        scale.sx=1;
        scale.sy=1;
        scale.sz=1;

        miscus.xdy=0;
        miscus.xgz=0;
        miscus.ybx=0;
        miscus.yhz=0;
        miscus.zcx=0;
        miscus.zfy=0;

        reflecAxis.rx=false;
        reflecAxis.ry=false;
        reflecAxis.rz=false;

        reflectFlat.xoy=false;
        reflectFlat.yoz=false;
        reflectFlat.xoz=false;

        refresh();
      }
    }
    isDownCtrl=e.isControlDown();
    isDownShift=e.isShiftDown();
    isDownAlt=e.isAltDown();
  }
}
