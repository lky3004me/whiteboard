import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.event.handler.CMEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import java.io.*;
///////////////////////////////////////////////////////////////////////////////////////////////////
//클라이언트 이벤트핸들러
///////////////////////////////////////////////////////////////////////////////////////////////////
public class CMClientEventHandler implements CMAppEventHandler {
    public CMClientStub m_clientStub;
    public DrawInfo.DrawFrame m_drawboard;
    public CMClientEventHandler (CMClientStub stub, DrawInfo.DrawFrame drawboard)
    {
        m_clientStub = stub;
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
    private void processDummyEvent(CMEvent cme)
    {
        CMDummyEvent due = (CMDummyEvent) cme;
        System.out.println("dummy msg: "+due.getDummyInfo());
        m_drawboard.receiveDrawInfo(due.getDummyInfo());
        return;
    }
}
