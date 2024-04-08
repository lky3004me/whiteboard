
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.manager.CMDBManager;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;
///////////////////////////////////////////////////////////////////////////////////////////////////
//서버 이벤트 핸들러
///////////////////////////////////////////////////////////////////////////////////////////////////
public class CMServerEventHandler implements CMAppEventHandler {
    private CMServerStub m_serverStub;
    public DrawInfo.DrawFrame m_drawboard;
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
