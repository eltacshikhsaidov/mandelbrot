import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

class MandelbrotDraw extends JPanel {
    private static int maxIter = 180;
    private double[] scala = {28, 28};
    private double[] position = {0, 0};

    public double[] getPosition() { return position; }

    public void setPosition(double[] position) { this.position = position; }

    public void paint(Graphics g) {
        super.paint(g);
        Dimension d = getSize();
        int x, y, iterations;
        double reale = 0, imaginary = 0;
        double cr, ci;
        double tr = 0, ti = 0;
        double mod = 0;
        int color_r, color_g, color_b;
        float mu = 0;
        BufferedImage I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (x = 0; x < d.width; x++) {
            for (y = 0; y < d.height; y++) {
                cr = ((double) x - position[0]) / scala[0];
                ci = -((double) y - position[1]) / scala[1];

                reale = 0;
                imaginary = 0;
                for (iterations = 0; iterations < maxIter; iterations++) {
                    tr = reale * reale - imaginary * imaginary + cr;
                    ti = 2 * reale * imaginary + ci;
                    reale = tr;
                    imaginary = ti;
                    mod = reale * reale + imaginary * imaginary;
                    if (mod > 4)
                        break;
                }


                mu = (float) (iterations - (Math.log(Math.log(Math.sqrt(mod)))) / Math.log(2.0));
                mu *= 10;
                I.setRGB(x, y, (int) mu);

            }

        }
        g.drawImage(I, 0, 0, this);

    }

    public double[] getScala() {
        return scala;
    }

    public void setScala(double[] scala) {
        this.scala = scala;
    }
}

class Finestra extends JFrame {
    double startX = 0;
    double startY = 0;

    public Finestra() {
        this.setLayout(new BorderLayout());
        this.setBounds(300, 300, 300, 300);
        final MandelbrotDraw mandelbrotdraw = new MandelbrotDraw();
        final double[] arrPos = new double[2];
        Dimension d = getSize();
        arrPos[0] = d.width / 2;
        arrPos[1] = d.height / 2;
        mandelbrotdraw.setPosition(arrPos);
        repaint();
        this.add(mandelbrotdraw, BorderLayout.CENTER);

        MouseWheelListener mW = new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent arg0) {
                int scroll = arg0.getWheelRotation();
                double[] scala = mandelbrotdraw.getScala();
                scala[0] += scroll;
                scala[1] += scroll;
                mandelbrotdraw.setScala(scala);
                repaint();
            }

        };

        MouseInputAdapter mI = new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                startX = p.x;
                startY = p.y;

            }

            public void mouseDragged(MouseEvent e) {
                Point p = e.getPoint();
                int curX = p.x;
                int curY = p.y;

                arrPos[0] += (curX - startX);
                arrPos[1] += (curY - startY);

                mandelbrotdraw.setPosition(arrPos);
                repaint();

                startX = curX;
                startY = curY;
            }
        };
        this.addMouseListener(mI);
        this.addMouseMotionListener(mI);
        this.addMouseWheelListener(mW);

        ActionListener tim = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                double[] scala = mandelbrotdraw.getScala();
                scala[0] += 3;
                scala[1] += 3;
                mandelbrotdraw.setScala(scala);
                repaint();
            }

        };

        Timer t = new Timer(1, tim);
        t.start();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

public class MandelBrot {
    public static void main(String[] args) {
        Finestra m = new Finestra();
    }
}
