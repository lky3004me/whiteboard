import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import kr.ac.konkuk.ccslab.cm.stub.CMClientStub;
import kr.ac.konkuk.ccslab.cm.stub.CMServerStub;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Random;


public class TopMenu extends JPanel implements ActionListener {

    public JButton exitBtn = new JButton("나가기");
    public JButton penBtn = new JButton("펜");
    public JButton lineBtn = new JButton("직선");
    public JButton cirBtn = new JButton("원");
    public JButton recBtn = new JButton("사각형");
    public JButton textBtn = new JButton("텍스트");
    public JButton changeBtn = new JButton("수정하기");
    public JButton colorBtn = new JButton("색깔 선택");
    public JButton thickBtn = new JButton("굵기:1");
    public JButton fillBtn = new JButton("채우기 ○");
    public JButton saveBtn = new JButton("저장");
    public JButton loadBtn = new JButton("불러오기");


    public JPanel btnPanel = new JPanel();
    public JPanel colorPalette = new JPanel();
    public CMClientStub m_clientStub = null;
    public CMServerStub m_serverStub = null;
    private  boolean isClient;
    private int thickness = 1;
    private DrawInfo.DrawFrame drawboard;

    JColorChooser chooser = new JColorChooser();

    public TopMenu(CMClientStub m_clientStub, DrawInfo.DrawFrame drawboard){
        this.m_clientStub = m_clientStub;
        this.drawboard = drawboard;
        isClient=true;
        this.setSize(900,200);

        FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
        this.setLayout(flow);
        //add(new JLabel("메뉴"));
        allbtnUnselect(true);
        //나가기 버튼
        //버튼에 이벤트 핸들러 등록, 상단에 버튼 추가
        exitBtn.addActionListener(this);
        btnPanel.add(exitBtn);
        penBtn.addActionListener(this);
        btnPanel.add(penBtn);

        lineBtn.addActionListener(this);
        btnPanel.add(lineBtn);

        cirBtn.addActionListener(this);
        btnPanel.add(cirBtn);

        recBtn.addActionListener(this);
        btnPanel.add(recBtn);

        textBtn.addActionListener(this);
        btnPanel.add(textBtn);

        colorBtn.addActionListener(this);
        Border colborder = new LineBorder(drawboard.getlockColor(),2);
        changeBtn.setPreferredSize(new Dimension(80,30));
        changeBtn.setBorder(colborder);
        btnPanel.add(colorBtn);

        thickBtn.addActionListener(this);
        btnPanel.add(thickBtn);

        fillBtn.addActionListener(this);
        btnPanel.add(fillBtn);

        changeBtn.addActionListener(this);
        btnPanel.add(changeBtn);

        saveBtn.addActionListener(this);
        btnPanel.add(saveBtn);

        loadBtn.addActionListener(this);
        btnPanel.add(loadBtn);

        add(btnPanel);
        setBackground(Color.WHITE);
        setVisible(true);
    }
    public TopMenu(CMServerStub m_serverStub, DrawInfo.DrawFrame drawboard){
        this.m_serverStub = m_serverStub;
        this.drawboard = drawboard;
        isClient=false;
        this.setSize(900,200);
        FlowLayout flow = new FlowLayout(FlowLayout.LEFT);
        this.setLayout(flow);
        //add(new JLabel("메뉴"));

        //나가기 버튼
        //버튼에 이벤트 핸들러 등록, 상단에 버튼 추가
        exitBtn.addActionListener(this);
        btnPanel.add(exitBtn);
        add(btnPanel);

        setBackground(Color.WHITE);
        setVisible(true);
    }
    public void allbtnUnselect(boolean chan){
        penBtn.setFont(new Font("default",Font.PLAIN,12));
        lineBtn.setFont(new Font("default",Font.PLAIN,12));
        cirBtn.setFont(new Font("default",Font.PLAIN,12));
        recBtn.setFont(new Font("default",Font.PLAIN,12));
        textBtn.setFont(new Font("default",Font.PLAIN,12));
        colorBtn.setFont(new Font("default",Font.PLAIN,12));
        thickBtn.setFont(new Font("default",Font.PLAIN,12));
        fillBtn.setFont(new Font("default",Font.PLAIN,12));
        if(chan){
            changeBtn.setFont(new Font("default",Font.PLAIN,12));
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn){
            //terminate를 제외하면 접속 해제를 알리는 기능이 존재
            //m_clientStub.leaveSession();
            //termiante는 모든 연결을 해제
            //그러나 나갔다는 기록을 알리는게 없어서 주의 필요
            //나갔는지의 상태는 99p CMInfo.CM_login
            //server와 client 둘만 있을 때, server와 client 누가 눌렀는지 식별함

            if(isClient && m_clientStub != null){
                if(drawboard.getMode().equals("change")) {
                    drawboard.unselectChange();
                }
                //CMDummyEvent due = new CMDummyEvent();
                //due.setDummyInfo("[system]#["+se.getUserName()+"] 님이 세션에 입장하였습니다.");
                //m_clientStub.cast()
                //m_clientStub.leaveSession();
                //m_clientStub.logoutCM();
                //m_clientStub.disconnectFromServer();
                //m_clientStub.terminateCM();
                boolean ret = false;

                ret = m_clientStub.logoutCM();
                if(ret)
                    System.out.println("successfully sent the log out request.");
                else {
                    System.err.println("failed the log out request!");
                    return;
                }
                ret = false;
                //m_clientStub.terminateCM();
            }
            else if(!isClient && m_serverStub != null){
                m_serverStub.terminateCM();
            }
            System.exit(0);
        }
        else if(e.getSource() == penBtn){
            allbtnUnselect(true);
            penBtn.setFont(new Font("default",Font.BOLD,12));
            drawboard.setMode("pen");
        }
        else if(e.getSource() == lineBtn){
            allbtnUnselect(true);
            lineBtn.setFont(new Font("default",Font.BOLD,12));
            drawboard.setMode("line");
        }
        else if(e.getSource() == cirBtn){
            allbtnUnselect(true);
            cirBtn.setFont(new Font("default",Font.BOLD,12));
            drawboard.setMode("cir");
        }
        else if(e.getSource() == recBtn){
            allbtnUnselect(true);
            recBtn.setFont(new Font("default",Font.BOLD,12));
            drawboard.setMode("rec");
        }
        else if(e.getSource() == textBtn){
            String text;
            System.out.println(drawboard.getSelected());
            if(drawboard.getSelected() && drawboard.getMode().equals("change")) {
                allbtnUnselect(false);
                textBtn.setFont(new Font("default",Font.BOLD,12));
                text = JOptionPane.showInputDialog("수정할 텍스트를 입력하세요");
                drawboard.setTextcontent(text);
                drawboard.changeDrawInfo(0,0,0,1);
            }
            else {
                allbtnUnselect(true);
                textBtn.setFont(new Font("default",Font.BOLD,12));
                drawboard.setMode("text");
                text = JOptionPane.showInputDialog("텍스트를 입력하세요");
                drawboard.setTextcontent(text);
            }
            /*
            String text = JOptionPane.showInputDialog("텍스트를 입력하세요");
            drawboard.setTextcontent(text);*/
        }
        else if(e.getSource() == changeBtn){
            allbtnUnselect(false);
            changeBtn.setFont(new Font("default",Font.BOLD,12));
            drawboard.setMode("change");
        }else if(e.getSource() == colorBtn) {
            Color selectedColor;
            if(drawboard.getMode().equals("change")){
                colorBtn.setFont(new Font("default",Font.BOLD,12));
                selectedColor = chooser.showDialog(null, "Color palette", Color.BLACK);

                if (selectedColor != null) {
                    drawboard.setChangeColor(selectedColor);
                    drawboard.setNowColor(selectedColor);
                }
                drawboard.changeDrawInfo(1,0,0,0);
            }
            else{
                colorBtn.setFont(new Font("default",Font.BOLD,12));
                selectedColor = chooser.showDialog(null, "Color palette", Color.BLACK);

                if (selectedColor != null) {
                    drawboard.setNowColor(selectedColor);
                }
            }

            Border border = new LineBorder(selectedColor,2);
            colorBtn.setPreferredSize(new Dimension(80,30));
            colorBtn.setBorder(border);

        }else if (e.getSource() == thickBtn){
            thickness *= 2;

            if (thickness == 64) {
                thickness = 1;
            }

            thickBtn.setText("굵기:" + thickness);
            if(drawboard.getMode().equals("change")) {
                thickBtn.setFont(new Font("default",Font.BOLD,12));
                drawboard.setChangeThickness(thickness);
                drawboard.setNowThickness(thickness);
                drawboard.changeDrawInfo(0,1,0,0);
            }
            else {
                thickBtn.setFont(new Font("default",Font.BOLD,12));
                drawboard.setNowThickness(thickness);
            }
        }else if(e.getSource() == fillBtn){
            Color selectedColor = drawboard.getNowColor();
            if(fillBtn.getText().equals("채우기 ○")) {
                fillBtn.setText("채우기 ●");
                //selectedColor = chooser.showDialog(null, "Color palette", Color.BLACK);
                if(drawboard.getMode().equals("cir") || drawboard.getMode().equals("rec")){
                    drawboard.setNowColor(selectedColor);
                }
            }
            else{
                fillBtn.setText("채우기 ○");
                if(drawboard.getMode().equals("cir") || drawboard.getMode().equals("rec")){
                    drawboard.setNowColor(selectedColor);
                }
            }
            boolean tmp = drawboard.getNowFill();

            if(drawboard.getMode().equals("change")){
                fillBtn.setFont(new Font("default",Font.BOLD,12));
                drawboard.setChangeFill(!tmp);
                drawboard.setNowFill(!tmp);
                drawboard.changeDrawInfo(0,0,1,0);
            }else{
                fillBtn.setFont(new Font("default",Font.BOLD,12));
                drawboard.setNowFill(!tmp);
            }

        } else if (e.getSource() == saveBtn) {
            Vector vc = drawboard.getVc();

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            try{
                String json = mapper.writeValueAsString(vc);

                File file = new File("saveFile.json");
                mapper.writeValue(file, vc);

                System.out.println("vc converted to JSON and saved to saveFile.json");
            }catch (Exception excep){
                excep.printStackTrace();
            }
        } else if (e.getSource() == loadBtn) {
            Graphics g;
            try{
                File file = new File("saveFile.json");

                ObjectMapper mapper = new ObjectMapper();

                LinkedHashMap[] mapArray = mapper.readValue(file, LinkedHashMap[].class);

                Vector<DrawInfo> vc = new Vector<>();

                for(LinkedHashMap map : mapArray){
                    DrawInfo drawInfo = drawboard.convertToDrawInfo(map);
                    vc.add(drawInfo);
                }

                drawboard.load(vc);
                System.out.println("vc from saveFile.json");
            }catch (Exception excep){
                excep.printStackTrace();
            }
        }
    }
}
