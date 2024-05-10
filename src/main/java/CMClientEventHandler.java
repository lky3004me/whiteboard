import kr.ac.konkuk.ccslab.cm.event.CMDataEvent;
import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.event.handler.CMAppEventHandler;
import kr.ac.konkuk.ccslab.cm.event.handler.CMEventHandler;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.info.CMInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import java.io.*;
///////////////////////////////////////////////////////////////////////////////////////////////////
//클라이언트 이벤트핸들러
///////////////////////////////////////////////////////////////////////////////////////////////////
public class CMClientEventHandler implements CMAppEventHandler {
    public CMClientStub m_clientStub;
    public DrawInfo.DrawFrame m_drawboard;

    public BottomLog m_bottomLog;
    public CMClientEventHandler (CMClientStub stub, DrawInfo.DrawFrame drawboard, BottomLog bottomLog)
    {
        m_clientStub = stub;
        m_drawboard = drawboard;
        m_bottomLog = bottomLog;
    }

    public void processSessionEvent(CMEvent cme){
        CMConfigurationInfo confInfo = m_clientStub.getCMInfo().getConfigurationInfo();
        CMSessionEvent se = (CMSessionEvent) cme;
        switch (se.getID()){
            case CMSessionEvent.LEAVE_SESSION:
                System.out.println("["+se.getUserName()+"] leaves a session("+se.getSessionName()+").");
                break;
            case CMSessionEvent.LOGOUT:
                System.out.println("["+se.getUserName()+"] logs out.");
                break;
            default:
                return;
        }
    }
    @Override
    public void processEvent(CMEvent cme) {
        //사용자가 나갔을 때 이벤트
//        switch(cme.getID()){
//            case CMInfo.CM_DATA_EVENT:
//                if(cme instanceof CMDataEvent){
//                    CMDataEvent de = (CMDataEvent) cme;
//                    if(de.getID() == CMDataEvent.REMOVE_USER){
//                        String removedUser = de.getUserName();
//                        //이제 이걸 다른 사용자들에게 전달
//                        System.out.println(removedUser + "님이 나갔습니다.");
//                    }
//                }
//        }
        switch(cme.getType())
        {
            case CMInfo.CM_DATA_EVENT:

                CMDataEvent de = (CMDataEvent) cme;

                if(de.getID() == CMDataEvent.REMOVE_USER){
                    String removedUser = de.getUserName();
                    //이제 이걸 다른 사용자들에게 전달
                    System.out.println("[SYSTEM] "+removedUser + "님이 나갔습니다.");
                    m_bottomLog.textArea.append("[SYSTEM] "+removedUser + "님이 나갔습니다.\n");
                }
                if(de.getID() == CMDataEvent.NEW_USER){
                    String newUser = de.getUserName();
                    //이제 이걸 다른 사용자들에게 전달
                    System.out.println("[SYSTEM] "+newUser + "님이 들어오셨습니다.");
                    m_bottomLog.textArea.append("[SYSTEM] "+newUser + "님이 들어오셨습니다.\n");
                }
                int pos = m_bottomLog.textArea.getText().length();
                m_bottomLog.textArea.setCaretPosition(pos);
                m_bottomLog.textArea.requestFocus();
                break;
            case CMInfo.CM_SESSION_EVENT:
                processSessionEvent(cme);
                break;
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
        String[] strArr = due.getDummyInfo().split("#");

        if(strArr[0].equals("[system]")){
            System.out.println(strArr[0]+" "+strArr[1]);
        }else{
            m_drawboard.receiveDrawInfo(due.getDummyInfo());
        }
        return;
    }

}