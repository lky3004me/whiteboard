import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Vector;
///////////////////////////////////////////////////////////////////////////////////////////////////
//그림 정보 클래스
///////////////////////////////////////////////////////////////////////////////////////////////////
class DrawInfo {
    private int x; // 처음 클릭 좌표
    private int y;
    private int x1; //나중 클릭 좌표
    private int y1;
    private String type; //도형 모양 PEN, LINE, CIR, REC
    private Color color; //색깔 RGB
    private int color_R;
    private int color_G;
    private int color_B;
    private boolean fill;// 색 채우기 여부
    private int thickness; //선두께
    public DrawInfo(String type, int x, int y, int x1, int y1,
                    int color_R, int color_G, int color_B,
                    boolean fill, int thickness) {
        this.type = type;
        this.x = x; this.y = y; this.x1 = x1; this.y1 = y1;
        this.color_R = color_R;this.color_G = color_G;this.color_B = color_B;
        this.color = new Color(color_R,color_G,color_B);
        this.fill = fill; this.thickness = thickness;
    }
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }
    public void setX1(int x1){ this.x1 = x1;}
    public void setY1(int y1){ this.y1 = y1; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getX1(){ return x1; }
    public int getY1(){ return y1; }

    public String getInfo(){
        //통신으로 보낼 문자열
        return STR."\{type}#\{x}#\{y}#\{x1}#\{y1}#\{color_R}#\{color_G}#\{color_B}#\{fill}#\{thickness}";
    }
    static class DrawFrame extends Frame implements MouseListener, MouseMotionListener, ItemListener, ActionListener {
        //그림판
        private Vector vc = new Vector(); //좌표 정보 저장하는 벡터
        private Boolean firstDrawing = true;
        public CMClientStub m_clientStub = null;
        public CMServerStub m_serverStub = null;
        public boolean isClient = false; //클라이언트: true 서버: false
        private int x; private int y; private int x1; private int y1;

        public JButton exitBtn = new JButton("나가기");
        public JPanel btnPanel = new JPanel();

        public DrawFrame(CMClientStub m_clientStub){
            super("클라이언트");
            this.m_clientStub = m_clientStub;
            isClient = true;

            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.setSize(500,500);


            //나가기 버튼
            //버튼에 이벤트 핸들러 등록, 상단에 버튼 추가
            exitBtn.addActionListener(this);
            btnPanel.add(exitBtn);
            this.add(btnPanel, BorderLayout.NORTH);

            setLocationRelativeTo(null);
            this.setVisible(true);
        }
        public DrawFrame(CMServerStub m_serverStub){
            super("서버");
            this.m_serverStub = m_serverStub;
            isClient = false;

            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.setSize(500,500);

            //나가기 버튼
            //버튼에 이벤트 핸들러 등록, 상단에 버튼 추가
            exitBtn.addActionListener(this);
            btnPanel.add(exitBtn);
            this.add(btnPanel, BorderLayout.NORTH);

            setLocationRelativeTo(null);
            this.setVisible(true);
        }//
        public void paint(Graphics g){

            //벡터에 저장된 그림 전부 그림
            for (int i = 0; i < vc.size(); i++) {
                DrawInfo info = (DrawInfo) vc.elementAt(i);
                g.setColor(info.color);
                ((Graphics2D) g).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                g.drawLine(info.getX(), info.getY(), info.getX1(), info.getY1());
            }
            firstDrawing = false;

        }

        public Vector getVc(){
            return vc;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == exitBtn){
                //m_clientStub.terminateCM();
                System.exit(0);
           }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(isClient) {
                x = e.getX();
                y = e.getY();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(isClient) {
                x1 = e.getX();
                y1 = e.getY();
                DrawInfo di = new DrawInfo("PEN", x, y, x1, y1, 0, 0, 0, false, 3);
                vc.add(di);
                this.repaint();
            }
        }

        //나가기 버튼 깜박거림 해결을 위해
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(isClient) {
                x1 = e.getX();
                y1 = e.getY();
                Color c = new Color(0, 0, 0);
                DrawInfo di = new DrawInfo("PEN", x, y, x1, y1, 0, 0, 0, false, 3);
                vc.add(di); //그리기 정보 객체를 벡터에 저장한다.
                x = x1;  //끝난 지점에서 다시 그려져야하므로
                y = y1;
                this.repaint();
                sendDrawInfo();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
        public void sendDrawInfo(){
            if(isClient){
                for (int i = 0; i<vc.size();i++){
                    DrawInfo info = (DrawInfo) vc.elementAt(i);
                    String strInput = info.getInfo();
                    CMDummyEvent due = new CMDummyEvent();
                    System.out.println(strInput);
                    due.setDummyInfo(strInput);
                    m_clientStub.send(due, m_clientStub.getDefaultServerName());
                }
            }
        }
        public void receiveDrawInfo(String str){

            String[] strArr = str.split("#");
            DrawInfo strDrawInfo = new DrawInfo(strArr[0],Integer.parseInt(strArr[1]),Integer.parseInt(strArr[2]),Integer.parseInt(strArr[3]),Integer.parseInt(strArr[4]),
                    Integer.parseInt(strArr[5]),Integer.parseInt(strArr[6]),Integer.parseInt(strArr[7]),Boolean.parseBoolean(strArr[8]),Integer.parseInt(strArr[9]));
            vc.add(strDrawInfo);
            this.repaint();

        }
    }

}
