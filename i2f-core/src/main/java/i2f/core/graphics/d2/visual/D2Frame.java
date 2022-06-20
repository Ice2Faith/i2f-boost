package i2f.core.graphics.d2.visual;

import lombok.Data;

import javax.swing.*;

/**
 * @author ltb
 * @date 2022/6/20 9:45
 * @desc
 */
@Data
public class D2Frame extends JFrame {
    protected int width;
    protected int height;
    protected D2Canvas canvas;

    public D2Frame(int width,int height){
        this.width=width;
        this.height=height;

        setBounds(0,0,width,height);

        setResizable(false);
        setTitle("D2Frame");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initComponents();
    }

    protected void initComponents(){
        canvas=new D2Canvas(width,height);
        canvas.setSize(width,height);
        getContentPane().add(canvas);

    }

    public void refresh(){
        this.canvas.repaint();
    }


}
