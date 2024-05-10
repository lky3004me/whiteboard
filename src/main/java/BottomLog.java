import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import javax.swing.*;
import java.awt.*;

public class BottomLog  extends JPanel {
    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane = new JScrollPane(textArea);
    public BottomLog() {
        this.setSize(800, 100);
        textArea = new JTextArea(3,50);
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        setBackground(Color.WHITE);
        setVisible(true);
    }
}
