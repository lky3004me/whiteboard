import kr.ac.konkuk.ccslab.cm.event.CMDummyEvent;
import kr.ac.konkuk.ccslab.cm.event.CMEvent;
import kr.ac.konkuk.ccslab.cm.event.CMSessionEvent;
import kr.ac.konkuk.ccslab.cm.info.CMConfigurationInfo;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Vector;

import static java.lang.Math.abs;

///////////////////////////////////////////////////////////////////////////////////////////////////
//그림 정보 클래스
///////////////////////////////////////////////////////////////////////////////////////////////////
class DrawInfo {
    private int x; // 처음 클릭 좌표
    private int y;
    private int x1; //나중 클릭 좌표s
    private int y1;
    private String type; //도형 모양 PEN, LINE, CIR, REC
    private Color color; //색깔 RGB
    private int color_R;
    private int color_G;
    private int color_B;
    private boolean fill;// 색 채우기 여부
    private int thickness; //선두께
    private boolean nowDrawing = false;

    public DrawInfo(String type, int x, int y, int x1, int y1,
                    int color_R, int color_G, int color_B,
                    boolean fill, int thickness,boolean nowDrawing) {
        this.type = type;
        this.x = x; this.y = y; this.x1 = x1; this.y1 = y1;
        this.color_R = color_R;this.color_G = color_G;this.color_B = color_B;
        this.color = new Color(color_R,color_G,color_B);
        this.fill = fill; this.thickness = thickness; this.nowDrawing = nowDrawing;
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
        return STR."\{type}#\{x}#\{y}#\{x1}#\{y1}#\{color_R}#\{color_G}#\{color_B}#\{fill}#\{thickness}#\{nowDrawing}";
    }
    static class DrawFrame extends JPanel implements MouseListener, MouseMotionListener, ItemListener, ActionListener {
        //그림판
        private Vector vclist = new Vector<Vector>();
        private Vector vc = new Vector(); //좌표 정보 저장하는 벡터
        public CMClientStub m_clientStub = null;
        public CMServerStub m_serverStub = null;
        public boolean isClient = false; //클라이언트: true 서버: false
        private int x; private int y; private int x1; private int y1;
        public Graphics Graphics_buffer; //더블버퍼링
        public Image Img_buffer;
        private String nowType = "pen";
        private Color nowColor = Color.black;
        private boolean nowFill = false;
        private int nowThickness = 1; //선두께
        private boolean nowDrawing = false;
        private DrawInfo tmpinfo = null;
        private DrawInfo clearInfo = new DrawInfo("clear",0,0,0,0,0,0,0,false,0,false);

        public DrawFrame(CMClientStub m_clientStub){
            //super("클라이언트");
            this.m_clientStub = m_clientStub;
            isClient = true;
            vc.add(clearInfo);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.setSize(500,500);
            //setLocationRelativeTo(null);
            this.setVisible(true);
        }
        public DrawFrame(CMServerStub m_serverStub){
            //super("서버");
            this.m_serverStub = m_serverStub;
            isClient = false;
            vc.add(clearInfo);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.setSize(500,500);


            //setLocationRelativeTo(null);
            this.setVisible(true);
        }//
        //@Override
        public void paint(Graphics g){
            //더블 버퍼링을 위한 설정
            Img_buffer = createImage(getWidth(),getHeight());
            Graphics_buffer = Img_buffer.getGraphics();
            update(g);
        }
        //@Override
        public void update(Graphics g){
            //벡터에 저장된 그림 전부 그림
            Graphics_buffer.clearRect(0, 0, 500, 500); // 백지화
            for (int i = 0; i < vc.size(); i++) {
                DrawInfo info = (DrawInfo) vc.elementAt(i);
                if (info.type.equals("pen")) {
                    Graphics_buffer.setColor(info.color);
                    ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                    Graphics_buffer.drawLine(info.getX(), info.getY(), info.getX(), info.getY());
                }
                else if (info.type.equals("line")) {
                    Graphics_buffer.setColor(info.color);
                    ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                    Graphics_buffer.drawLine(info.getX(), info.getY(), info.getX1(), info.getY1());
                }
                else if (info.type.equals("cir")){
                    if(info.fill){
                        Graphics_buffer.setColor(info.color);
                        ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                        Graphics_buffer.fillOval(info.getX(), info.getY(), (info.getX1()-info.getX()), (info.getY1()-info.getY()));

                    }else{
                        Graphics_buffer.setColor(info.color);
                        ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                        Graphics_buffer.drawOval(info.getX(), info.getY(), (info.getX1()-info.getX()), (info.getY1()-info.getY()));
                    }
                }
                else if (info.type.equals("rec")) {
                    if(info.fill){
                        Graphics_buffer.setColor(info.color);
                        ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                        Graphics_buffer.fillRect(info.getX(), info.getY(), (info.getX1()-info.getX()), (info.getY1()-info.getY()));
                    }else{
                        Graphics_buffer.setColor(info.color);
                        ((Graphics2D) Graphics_buffer).setStroke(new BasicStroke(info.thickness, BasicStroke.CAP_ROUND, 0));
                        Graphics_buffer.drawRect(info.getX(), info.getY(), (info.getX1()-info.getX()), (info.getY1()-info.getY()));
                    }
                }
                else{

                }
                if(info.nowDrawing){
                    vc.remove(i);
                    i--;
                }
            }
            g.drawImage(Img_buffer, 0, 0, this);
            //repaint();


        }
        public void setMode(String mode){
            nowType = mode;
        }
        public String getMode(){
            return nowType;
        }
        public void setNowFill(boolean b){ nowFill = b; }
        public boolean getNowFill(){ return nowFill; }
        //색깔 설정
        public void setNowColor(Color c){ nowColor = c;}

        //굵기 설정
        public void setNowThickness(int t){nowThickness = t;}

        public Vector getVc(){
            return vc;
        }
        public void addVc(){
            vclist.add(new Vector());
        }
        @Override
        public void actionPerformed(ActionEvent e) {

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
                tmpinfo = null;
                x1 = e.getX();
                y1 = e.getY();
                DrawInfo di;
                if(nowType.equals("pen")){
                    nowDrawing = false;
                    di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    sendDrawInfo(di);
                    x = x1;  //끝난 지점에서 다시 그려져야하므로
                    y = y1;
                }
                if(nowType.equals("line")){
                    nowDrawing = false;
                    di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    sendDrawInfo(di);
                }
                if(nowType.equals("cir")||nowType.equals("rec")){
                    nowDrawing = false;
                    if(x<x1&&y<y1)
                        di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else if (x>x1&&y<y1)
                        di = new DrawInfo(nowType, x1, y, x, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else if (x<x1&&y>y1)
                        di = new DrawInfo(nowType, x, y1, x1, y,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else
                        di = new DrawInfo(nowType, x1, y1, x, y,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    sendDrawInfo(di);
                }
                this.repaint();

            }
        }

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
                DrawInfo di;
                if(nowType.equals("pen")){
                    nowDrawing = false;
                    di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    sendDrawInfo(di);
                    x = x1;  //끝난 지점에서 다시 그려져야하므로
                    y = y1;
                }
                if(nowType.equals("line")){
                    nowDrawing = true;
                    di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    tmpinfo = di;
                    sendDrawInfo(di);
                }
                if(nowType.equals("cir")||nowType.equals("rec")){
                    nowDrawing = true;
                    if(x<x1&&y<y1)
                        di = new DrawInfo(nowType, x, y, x1, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else if (x>x1&&y<y1)
                        di = new DrawInfo(nowType, x1, y, x, y1,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else if (x<x1&&y>y1)
                        di = new DrawInfo(nowType, x, y1, x1, y,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    else
                        di = new DrawInfo(nowType, x1, y1, x, y,nowColor.getRed(), nowColor.getGreen(), nowColor.getBlue(), nowFill, nowThickness, nowDrawing);
                    sendDrawInfo(di);
                }
                this.repaint();
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }


        public void sendDrawInfo(DrawInfo info){
            if(isClient){
                String strInput = info.getInfo();
                CMDummyEvent due = new CMDummyEvent();
                System.out.println(strInput);
                due.setDummyInfo(strInput);
                m_clientStub.send(due, m_clientStub.getDefaultServerName());
            }
        }
        public void receiveDrawInfo(String str){
            String[] strArr = str.split("#");
            DrawInfo strDrawInfo = new DrawInfo(strArr[0],Integer.parseInt(strArr[1]),Integer.parseInt(strArr[2]),Integer.parseInt(strArr[3]),Integer.parseInt(strArr[4]),
                    Integer.parseInt(strArr[5]),Integer.parseInt(strArr[6]),Integer.parseInt(strArr[7]),Boolean.parseBoolean(strArr[8]),Integer.parseInt(strArr[9]),Boolean.parseBoolean(strArr[10]));
            if(strDrawInfo.type.equals("clear")){
                vc.clear();
            }
            vc.add(strDrawInfo);
            if(!isClient){
                CMDummyEvent due = new CMDummyEvent();
                due.setDummyInfo(str);
                //m_serverStub.cast(due, se.getSessionName(),se.getCurrentGroupName());
            }
            this.repaint();
        }
    }

}
