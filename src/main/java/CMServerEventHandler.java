
import kr.ac.konkuk.ccslab.cm.event.CMDataEvent;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import java.util.Vector;

///////////////////////////////////////////////////////////////////////////////////////////////////
//서버 이벤트 핸들러
///////////////////////////////////////////////////////////////////////////////////////////////////
public class CMServerEventHandler implements CMAppEventHandler {
    private CMServerStub m_serverStub;
    public DrawInfo.DrawFrame m_drawboard;

    //클라이언트 유저 목록
    private Vector<String> userlist = new Vector();
    public CMServerEventHandler(CMServerStub serverStub, DrawInfo.DrawFrame drawboard)
    {
        m_serverStub = serverStub;
        m_drawboard = drawboard;
    }
    @Override
    public void processEvent(CMEvent cme) {
        switch(cme.getType())
        {
            case CMInfo.CM_DUMMY_EVENT:
                processDummyEvent(cme);
                break;
            case CMInfo.CM_SESSION_EVENT:
                processSessionEvent(cme);
            default:
                return;
        }
    }
    private void processSessionEvent(CMEvent cme)
    {
        CMSessionEvent se = (CMSessionEvent) cme;
        //CMDummyEvent due1 = new CMDummyEvent();
        CMDummyEvent due2 = new CMDummyEvent();
        //due1.setDummyInfo("다른 사용자가 입장하였습니다.");
        due2.setDummyInfo("다른 사용자가 종료하였습니다.");
        switch(se.getID())
        {
            case CMSessionEvent.LOGIN:
                System.out.println("["+se.getUserName()+"] requests login.");
                //userlist.add(se.getUserName());
                //m_serverStub.cast(due1, null, null);
                break;
            case CMSessionEvent.LOGOUT:
                m_serverStub.cast(due2, null, null);
                System.out.println("leaveSession");
            default:
                return;
        }
    }
    private void processDummyEvent(CMEvent cme) {
        CMDummyEvent due = (CMDummyEvent) cme;
        System.out.println("dummy msg: "+due.getDummyInfo());
        m_drawboard.receiveDrawInfo(due.getDummyInfo());
        return;
    }
}