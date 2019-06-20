package slider2;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

public class Slider2 extends JFrame
{
	private JSlider slider;
	private JLabel label1;
	private JPanel panel1;
	private JPanel panel2;
	private int sliderPreviousValue = 24;
	private double label1Width = 100;
	private int frameMoved = 0;
	
	public Slider2()
	{
		initComponents();
	}
	
	private void initComponents()
	{
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Changing Font Size With a Slider"); 
        this.setBounds(0, 0, 640, 400);
        this.setPreferredSize(new Dimension(640, 400));
        this.setLocationRelativeTo(null);
        
        slider = new JSlider(10, 130, 24);
        slider.setPreferredSize(new Dimension(400, 50));
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(2);
        slider.setSnapToTicks(false);
        slider.setPaintLabels(true);
        
        label1 = new JLabel("To jest czcionka o rozmiarze " + slider.getValue());
        // or "This text has font of size - " + slider.getValue()
        label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), slider.getValue()));
        
        slider.addChangeListener(new ChangeListener()
        		{	@Override
					public void stateChanged(ChangeEvent arg0)
					{
        				if (slider.getValue() - sliderPreviousValue > 1 || slider.getValue() - sliderPreviousValue < -1)	
        						slider.setValue(sliderPreviousValue);
        				else
        					sliderChange();
					}        			
        		});
        
        panel1 = new JPanel();
        panel1.add(slider);
        panel2 = new JPanel();
        panel2.add(label1);
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel1, BorderLayout.NORTH);
        this.getContentPane().add(panel2, BorderLayout.SOUTH);
        
        this.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e)
			{
				frameResize();
				sliderChange();		// This enables slider movement (increasing the font) right after enlarging the frame.
			}
			@Override
			public void componentMoved(ComponentEvent e) {
				switch(frameMoved)
				{
				case 0:
					frameMoved = 1;
					break;
				case 1:
					frameMoved = 2;		// Initially when run, the frame moves twice. This is to make sure the text changes...
					break;				// ...when the USER moves the frame and not earlier.
				case 2:
					label1.setText("Don't do that. I'm getting dizzy..");
					break;
				}
			}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
        
        pack();
	}
	
	public static void main(String[] args)
	{
		new Slider2().setVisible(true);
	}

	private void sliderChange()
	{	//	The value below (a bit more than the label's width) is to have a margin so that the text does not get too close to the frame.
		label1Width = label1.getWidth() * 1.09;

		if (label1Width >= panel2.getWidth() * 1.0 && slider.getValue() >= sliderPreviousValue)
		{	//	^ when label gets too wide;					^ when slider moved to the right enlarging the font;
			label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), slider.getValue()));
			label1.setText("To jest czcionka o rozmiarze " + slider.getValue());
			slider.setExtent(slider.getMaximum() - slider.getValue());
		}	//	^ This is to prevent further movement to the right.
		else
		{
			label1.setFont(new Font(label1.getFont().getName(), label1.getFont().getStyle(), slider.getValue()));
			label1.setText("To jest czcionka o rozmiarze " + slider.getValue());
			slider.setExtent(0);	// This magically enables slider's movement again after blocking above.
			frameResize();		// This enables shrinking the frame right after making the text smaller.
		}
		
		sliderPreviousValue = slider.getValue();
	}
	
	private void frameResize()
	{
		Dimension frameMinSize = new Dimension
				(this.getGraphicsConfiguration().getDevice().getDisplayMode().getWidth() / 10 * 3,
				 this.getGraphicsConfiguration().getDevice().getDisplayMode().getHeight() / 10 * 4);
		label1Width = label1.getWidth() * 1.04;
		if (label1Width <= frameMinSize.getWidth())
			this.setMinimumSize(frameMinSize);
		else
			this.setMinimumSize(new Dimension((int)label1Width, frameMinSize.height));
		panel1.setBorder(BorderFactory.createEmptyBorder(this.getHeight() / 7, 0, 0, 0));
		panel2.setBorder(BorderFactory.createEmptyBorder(0, 0, this.getHeight() / 7, 0));
	}
		//	^ If the label (with a margin) is smaller than a portion of the screen determined in the 'frameMinSize'...
		//	...set the frame's min. size to that variable's value. If the label is larger set the frame's min. size...
		//	...to the label's width (with a margin).
		//	Set Border makes sure both the slider and the label stay away from the frame in Y axis always keeping...
		//	...the same distance in percentage.
}























