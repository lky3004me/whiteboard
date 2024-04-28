
import kr.ac.konkuk.ccslab.cm.manager.CMConfigurator;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
///////////////////////////////////////////////////////////////////////////////////////////////////
//서버 메인
///////////////////////////////////////////////////////////////////////////////////////////////////
public class CMServerApp extends JFrame{
    public CMServerStub m_serverStub;
    public CMServerEventHandler m_eventHandler;
    public DrawInfo.DrawFrame drawboard;
    public TopMenu menu;

    public CMServerApp()
    {
        super("서버");
        this.setSize(800,600);
        m_serverStub = new CMServerStub();
        setLocationRelativeTo(null);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        drawboard = new DrawInfo.DrawFrame(m_serverStub);
        menu = new TopMenu(m_serverStub,drawboard);
        this.add(menu, BorderLayout.NORTH);
        this.add(drawboard, BorderLayout.CENTER);
        m_eventHandler = new CMServerEventHandler(m_serverStub,drawboard);
        this.setVisible(true);
    }
    public CMServerStub getServerStub()
    {
        return m_serverStub;
    }
    public CMServerEventHandler getServerEventHandler()
    {
        return m_eventHandler;
    }
    public static void main(String[] args) {
        CMServerApp server = new CMServerApp();
        CMServerStub cmStub = server.getServerStub();
        cmStub.setAppEventHandler(server.getServerEventHandler());
        cmStub.startCM();
    }
}
