
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
            case CMInfo.CM_DATA_EVENT:

            CMDataEvent de = (CMDataEvent) cme;

            if(de.getID() == CMDataEvent.REMOVE_USER){
                String removedUser = de.getUserName();
                //System.out.println("[SYSTEM] "+removedUser + "님이 나갔습니다.");
            }
            if(de.getID() == CMDataEvent.NEW_USER){
                String newUser = de.getUserName();
                //System.out.println("[SYSTEM] "+newUser + "님이 들어오셨습니다.");
            }
            break;
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
        CMDummyEvent due1 = new CMDummyEvent();
        CMDummyEvent due2 = new CMDummyEvent();
        //due1.setDummyInfo("[system]#다른 사용자가 입장하였습니다.");
        //due2.setDummyInfo("[system]#다른 사용자가 종료하였습니다.");
        switch(se.getID())
        {
            case CMSessionEvent.LOGIN:
                due1.setDummyInfo("[system]#["+se.getUserName()+"]님이 입장하였습니다.\n");
                System.out.println(due1.getDummyInfo());
                m_serverStub.cast(due1, null, null);
                //userlist.add(se.getUserName());
                break;
            case CMSessionEvent.LOGOUT:
                due1.setDummyInfo("[system]#["+se.getUserName()+"]님이 떠났습니다.\n");
                System.out.println(due1.getDummyInfo());
                m_serverStub.cast(due1, null, null);
                break;
            default:
                return;
        }
    }
    private void processDummyEvent(CMEvent cme) {
        CMDummyEvent due = (CMDummyEvent) cme;

        String[] strArr = due.getDummyInfo().split("#");
        if(strArr[0].equals("[system]")){
            System.out.println(strArr[0]+" "+strArr[1]);
        }else{
            m_drawboard.receiveDrawInfo(due.getDummyInfo());
        }
        return;
    }
}