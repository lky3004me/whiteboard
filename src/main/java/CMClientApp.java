
import com.mysql.fabric.Server;
import kr.ac.konkuk.ccslab.cm.entity.CMServer;
import kr.ac.konkuk.ccslab.cm.entity.CMUser;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInteractionInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Vector;
///////////////////////////////////////////////////////////////////////////////////////////////////
//클라이언트 메인
///////////////////////////////////////////////////////////////////////////////////////////////////

public class CMClientApp implements MouseListener,MouseMotionListener{
    private static CMClientStub m_clientStub;
    private CMClientEventHandler m_eventHandler;

    private DrawInfo.DrawFrame drawboard;


    public CMClientApp()
    {
        m_clientStub = new CMClientStub();
        drawboard = new DrawInfo.DrawFrame(m_clientStub);
        m_eventHandler = new CMClientEventHandler(m_clientStub,drawboard);
    }
    public CMClientStub getClientStub()
    {
        return m_clientStub;
    }
    public CMClientEventHandler getClientEventHandler()
    {
        return m_eventHandler;
    }



        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CMClientApp client = new CMClientApp();
        CMClientStub clientStub = client.getClientStub();
        CMClientEventHandler eventHandler = client.getClientEventHandler();

        boolean ret = false;
// initialize CM
        clientStub.setAppEventHandler((CMAppEventHandler) eventHandler);
        ret = clientStub.startCM();
        if(ret)
            System.out.println("init success");
        else {
            System.err.println("init error!");
            return;
        }
// login CM server
        System.out.println("user name: ccslab");
        System.out.println("password: ccslab");
        ret = clientStub.loginCM("ccslab", "ccslab");
        if(ret)
            System.out.println("successfully sent the login request.");
        else {
            System.err.println("failed the login request!");
            return;
        }
// wait before executing next API
        //System.out.println("Press enter to execute next API:");
        //scanner.nextLine();
/*
        CMInteractionInfo interInfo = m_clientStub.getCMInfo().getInteractionInfo();
        CMUser myself = interInfo.getMyself();
        System.out.println("====== test CMDummyEvent in current group");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("input message: ");
        String strInput = null;
        try {
            strInput = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CMDummyEvent due = new CMDummyEvent();
        due.setHandlerSession(myself.getCurrentSession());
        due.setHandlerGroup(myself.getCurrentGroup());
        due.setDummyInfo(strInput);
        m_clientStub.cast(due, myself.getCurrentSession(), myself.getCurrentGroup());
        //m_clientStub.send(due, m_clientStub.getDefaultServerName());
        due = null;
        */

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
    public void mouseDragged(MouseEvent e) {
        Vector vc = drawboard.getVc();

        for (int i = 0; i<vc.size();i++){
            String strInput = vc.toString();
            CMDummyEvent due = new CMDummyEvent();
            System.out.println(strInput);
            due.setDummyInfo(strInput);
            m_clientStub.send(due, m_clientStub.getDefaultServerName());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
