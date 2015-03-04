package quicklyst;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class QLGUI extends JFrame{
    private static final String MESSAGE_NAME = "Quicklyst";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int INNER_WIDTH = 783;
    private static final int INNER_HEIGHT = 557;
    private static final int PADDING_LEFT = 10;
    private static final int PADDING_TOP = 10;
    
    private static final int TASKLIST_OFFSET_X = PADDING_LEFT;
    private static final int TASKLIST_OFFSET_Y = PADDING_TOP;
    private static final int TASKLIST_WIDTH = 390;
    private static final int TASKLIST_HEIGHT = 500;
    
    private static final int COMMAND_OFFSET_X = PADDING_LEFT;
    private static final int COMMAND_OFFSET_Y = 2*PADDING_TOP+TASKLIST_HEIGHT;
    private static final int COMMAND_WIDTH = INNER_WIDTH-2*PADDING_LEFT;
    private static final int COMMAND_HEIGHT = INNER_HEIGHT-3*PADDING_TOP-TASKLIST_HEIGHT;
    
    private final static int OVERVIEW_OFFSET_X = 2*PADDING_LEFT+TASKLIST_WIDTH;
    private final static int OVERVIEW_OFFSET_Y = PADDING_TOP;
    private final static int OVERVIEW_WIDTH = INNER_WIDTH-TASKLIST_WIDTH-3*PADDING_LEFT;
    private final static int OVERVIEW_HEIGHT = (TASKLIST_HEIGHT-PADDING_TOP)/2;
    
    private static final int FEEDBACK_OFFSET_X = 2*PADDING_LEFT+TASKLIST_WIDTH;
    private static final int FEEDBACK_OFFSET_Y = (TASKLIST_HEIGHT-PADDING_TOP)/2+2*PADDING_TOP;
    private static final int FEEDBACK_WIDTH = INNER_WIDTH-TASKLIST_WIDTH-3*PADDING_LEFT;
    private static final int FEEDBACK_HEIGHT = (TASKLIST_HEIGHT-PADDING_TOP)/2;
    
    private JPanel taskList;
    private JPanel overview;
    private JTextArea feedback;
    private JTextField command;
    
    public QLGUI() {
        super(MESSAGE_NAME);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        
        taskList = new JPanel();
        taskList.setLayout(new GridBagLayout());
        
        overview = new JPanel();
        overview.setLayout(new BoxLayout(overview, BoxLayout.PAGE_AXIS));
        
        feedback = new JTextArea();
        
        JPanel temp = new JPanel(new BorderLayout());
        temp.add(taskList, BorderLayout.NORTH);
        JScrollPane taskListScroll = new JScrollPane(temp);
        add(taskListScroll);
        
        JScrollPane feedbackScroll = new JScrollPane(feedback);
        add(feedbackScroll);
        overview.setBackground(Color.BLUE);
        add(overview);
       
        command = new JTextField();
        
        command.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
                StringBuilder fb = new StringBuilder();
                List<Task> tasks = QLLogic.executeCommand(command.getText(), fb);
                if (!fb.toString().isEmpty()) {
                    feedback.append(fb.toString() + "\r\n");
                }
                taskList.removeAll();
                int i = 1;
                for (Task t : tasks)
                {
                    
                    JPanel p = new JPanel();
                    p.setLayout(new GridBagLayout());
                    p.setBorder(new LineBorder(Color.BLACK));
                    
                    JPanel c = new JPanel();
                    c.setBackground(Color.RED);
                    JLabel name = new JLabel(t.getName());
                    JLabel index = new JLabel("#" + i);
                    
                    JLabel date = new JLabel("");
                    if ((t.getDueDate() != null) && (t.getStartDate() != null)) {
                        date.setText(t.getStartDateString() + " - " + t.getDueDateString());
                    }
                    else if (t.getDueDate() != null) {
                        date.setText("due " + t.getDueDateString());
                    } else if (t.getStartDate() != null) {
                        date.setText("start " + t.getStartDateString());
                    }
                        
                    
                    JLabel priority = new JLabel(((Character)t.getPriority()).toString());
                    
                    
                    
                    GridBagConstraints con = new GridBagConstraints();
                    con.insets = new Insets(5, 5, 5, 5);
                    con.gridheight = 2;
                    con.gridwidth = 1;
                    con.gridx = 0;
                    con.gridy = 0;
                    con.fill = GridBagConstraints.VERTICAL;
                    p.add(c, con);
                    
                    con.gridheight = 1;
                    con.gridwidth = 1;
                    con.gridx = 1;
                    con.gridy = 0;
                    con.weightx = 1;
                    con.fill = GridBagConstraints.HORIZONTAL;
                    con.anchor = GridBagConstraints.WEST;
                    p.add(name, con);
                    
                    con.weightx = 0;
                    con.fill = GridBagConstraints.NONE;
                    con.anchor = GridBagConstraints.EAST;
                    con.gridheight = 1;
                    con.gridwidth = 1;
                    con.gridx = 2;
                    con.gridy = 0;
                    p.add(index, con);
                    
                    con.anchor = GridBagConstraints.WEST;
                    con.gridheight = 1;
                    con.gridwidth = 1;
                    con.gridx = 1;
                    con.gridy = 1;
                    p.add(date, con);
                    
                    con.anchor = GridBagConstraints.EAST;
                    con.gridheight = 1;
                    con.gridwidth = 1;
                    con.gridx = 2;
                    con.gridy = 1;
                    p.add(priority, con);
                    
                    
                    con.weightx = 1;
                    con.anchor = GridBagConstraints.NORTHEAST;
                    con.fill = GridBagConstraints.HORIZONTAL;
                    con.gridheight = 1;
                    con.gridwidth = 1;
                    con.gridx = 0;
                    con.gridy = i-1;
                    taskList.add(p, con);
                    i++;
                }
                taskList.revalidate();
                taskList.repaint();
            }
          }
        );
        
        
        command.setToolTipText("Field 1 hover");
        add(command);
        
        Insets insets = getInsets();
        taskListScroll.setBounds(TASKLIST_OFFSET_X+insets.left, TASKLIST_OFFSET_Y+insets.top,
                                 TASKLIST_WIDTH, TASKLIST_HEIGHT);
        command.setBounds(COMMAND_OFFSET_X+insets.left, COMMAND_OFFSET_Y+insets.top, 
                          COMMAND_WIDTH, COMMAND_HEIGHT);
        overview.setBounds(OVERVIEW_OFFSET_X+insets.left, OVERVIEW_OFFSET_Y+insets.top, 
                           OVERVIEW_WIDTH, OVERVIEW_HEIGHT);
        feedbackScroll.setBounds(FEEDBACK_OFFSET_X+insets.left, FEEDBACK_OFFSET_Y+insets.top,
                                 FEEDBACK_WIDTH, FEEDBACK_HEIGHT);

        setVisible(true);
        
        command.getActionListeners()[0].actionPerformed(null);
        feedback.setText("");
    }
    public static void main(String[] args) {
        QLLogic.setup("save.json");
        QLGUI g = new QLGUI();
        
    }
   
}
