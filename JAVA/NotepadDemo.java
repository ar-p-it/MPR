import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.FileStore;

class Notepad extends Frame implements ActionListener{

    JTabbedPane jtb;
    TextArea retrievedTextArea;
    Notepad(String title){
        super(title);
        setLayout(new BorderLayout());
        setSize(800,600);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we){
                dispose();
            }
        });

       jtb = new JTabbedPane(JTabbedPane.TOP);
       jtb.add("Untitled Tab", new NewTab());
       add(jtb);

        MenuBar mbar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu viewMenu = new Menu("View");

        MenuItem save = new MenuItem("Save");
        MenuItem NewT = new MenuItem("New Tab");
        MenuItem NewW = new MenuItem("New Window");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem open = new MenuItem("Open");
        MenuItem closeTab = new MenuItem("Close Tab");
        MenuItem closeWindow = new MenuItem("Close Window");

        MenuItem find = new MenuItem("Find");
        MenuItem replace = new MenuItem("Replace");

        MenuItem zoomIn = new MenuItem("Zoom In");
        MenuItem zoomOut = new MenuItem("Zoom Out");

        fileMenu.add(NewT).addActionListener(this);
        fileMenu.add(NewW).addActionListener(this);
        fileMenu.add(save).addActionListener(this);
        fileMenu.add(saveAs).addActionListener(this);
        fileMenu.addSeparator();
        fileMenu.add(open).addActionListener(this);
        fileMenu.add(closeTab).addActionListener(this);
        fileMenu.add(closeWindow).addActionListener(this);

        editMenu.addSeparator();
        editMenu.add(find).addActionListener(this);
        editMenu.add(replace).addActionListener(this);

        viewMenu.add(zoomIn).addActionListener(this);
        viewMenu.add(zoomOut).addActionListener(this);

        mbar.add(fileMenu);
        mbar.add(editMenu);
        mbar.add(viewMenu);
        setMenuBar(mbar);



    }

    public static String encryptText(String Message){
        char[] ch = Message.toCharArray();
        int shift = 4;
            for(int i=0; i<ch.length; i++){
                if(Character.isLetter(ch[i])){
                    if(ch[i] >= 'a' && ch[i] <= 'z')
                    ch[i] = (char)((ch[i] - 'a' + shift + 26) % 26 + 'a');
                    else
                    ch[i] = (char)((ch[i] - 'A' + shift + 26) % 26 + 'A');
                }
            }
        String encryptedText = new String(ch);
        return encryptedText;
    }

    public static String decryptText(String Message){
        char[] ch = Message.toCharArray();
        int shift = 4;
            for(int i=0; i<ch.length; i++){
                if(Character.isLetter(ch[i])){
                    if(ch[i] >= 'a' && ch[i] <= 'z')
                    ch[i] = (char)((ch[i] - 'a' - shift + 26) % 26 + 'a');
                    else
                    ch[i] = (char)((ch[i] - 'A' - shift + 26) % 26 + 'A');
                }
            }
        String decryptedText = new String(ch);
        return decryptedText;
    }
    @Override
    public void actionPerformed(ActionEvent ae){
        String source = ae.getActionCommand();
        boolean isMyExtension = false;
        switch(source) {
            case "New Tab":
                jtb.add("Untitled Tab", new NewTab());
                break;

            case "New Window":
                new Notepad("Notepad");
                break;

            case "Save":
                FileDialog fd = new FileDialog(this, "Save File", FileDialog.SAVE);
                String directory, fileName;
                if(jtb.getTitleAt(jtb.getSelectedIndex()) == "Untitled Tab"){
                    fd.setVisible(true);
                    if(fd.getDirectory() == null || fd.getFile() == null){return;}
                    directory = fd.getDirectory();
                    fileName = fd.getFile();
                    if(fileName.endsWith(".gg")){
                        isMyExtension = true;
                    }
                    int index = jtb.getSelectedIndex();
                    jtb.setTitleAt(index,fileName);

                    File f = new File(directory+fileName);
                    Component selectedComponent = jtb.getSelectedComponent();
                    NewTab newTab = (NewTab) selectedComponent;
                    newTab.setLocation(directory+fileName);
                    retrievedTextArea = newTab.getTextArea();
                    String text = retrievedTextArea.getText();

                    try{
                        FileOutputStream fOverWrite = new FileOutputStream(f);
                            if(isMyExtension){
                            text = encryptText(text);
                            isMyExtension = false;
                        }
                        fOverWrite.write(text.getBytes());
                        fOverWrite.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                
            }
               else
                {
                    
                    try{
                        Component selectedComponent = jtb.getSelectedComponent();
                        NewTab newTab = (NewTab) selectedComponent;
                        retrievedTextArea = newTab.getTextArea();
                        String location = newTab.getLocationString();
                        
                        
                        File f = new File(location);
                        if(!f.exists()){
                            int index = jtb.getSelectedIndex();
                            jtb.setTitleAt(index,"Untitled Tab");
                            fd.setVisible(true);
                            if(fd.getDirectory() == null || fd.getFile() == null){return;}
                            directory = fd.getDirectory();
                            fileName = fd.getFile();
                            if(fileName.endsWith(".gg")){
                                isMyExtension = true;
                            }
                            int Index = jtb.getSelectedIndex();
                            jtb.setTitleAt(Index,fileName);
        
                            File f1 = new File(directory+fileName);
                            Component SelectedComponent = jtb.getSelectedComponent();
                            NewTab newAgain = (NewTab) SelectedComponent;
                            newAgain.setLocation(directory+fileName);
                            retrievedTextArea = newAgain.getTextArea();
                            String text = retrievedTextArea.getText();
        
                            try {
                                FileOutputStream fOverWrite = new FileOutputStream(f1);
                                if(isMyExtension){
                                    text = encryptText(text);
                                    isMyExtension = false;
                                }
                                fOverWrite.write(text.getBytes());
                                fOverWrite.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                            
                            
                        }

                        if(location.endsWith(".gg")){
                            isMyExtension = true;
                        }
                        FileOutputStream fout = new FileOutputStream(f);
                        
                        String lines = retrievedTextArea.getText();
                        if(isMyExtension){
                            lines = encryptText(lines);
                            isMyExtension = false;
                            
                        }
                    
                        fout.write(lines.getBytes());
                        fout.close();
                        
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }
                    System.out.println("File saved");
                        
                        
                    
                
                }
                break;
            case "Save As":
                FileDialog reSaveFd = new FileDialog(this, "Save File", FileDialog.SAVE);
                String newDirectory, newFileName;
                    reSaveFd.setVisible(true);
                    if(reSaveFd.getDirectory() == null || reSaveFd.getFile() == null){return;}
                    newDirectory = reSaveFd.getDirectory();
                    newFileName = reSaveFd.getFile();
                    if(newFileName.endsWith(".gg")){
                        isMyExtension = true;
                    }
                    int index = jtb.getSelectedIndex();
                    jtb.setTitleAt(index,newFileName);
                    File f = new File(newDirectory+newFileName);
                    Component selectedComponent = jtb.getSelectedComponent();
                    NewTab newTab = (NewTab) selectedComponent;
                    newTab.setLocation(newDirectory+newFileName);
                    retrievedTextArea = newTab.getTextArea();
                    String text = retrievedTextArea.getText();
                    try {
                        FileOutputStream fOverWrite = new FileOutputStream(f);
                        if(isMyExtension){
                            text = encryptText(text);
                            isMyExtension = false;
                            }
                        fOverWrite.write(text.getBytes());
                        fOverWrite.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                break;

            case "Open":
                    FileDialog openFd = new FileDialog(this, "Open File", FileDialog.LOAD);
                    String openedDirectory, openedFileName;
                    openFd.setVisible(true);
                    if(openFd.getDirectory() == null || openFd.getFile() == null){
                        return;
                    }
                    else{
                        String data = "";
                        openedDirectory = openFd.getDirectory();
                        openedFileName = openFd.getFile();
                        if(openedFileName.endsWith(".gg")){
                            isMyExtension = true;
                        }
                        File openedFile = new File(openedDirectory+openedFileName);
                        try {
                            FileInputStream fis = new FileInputStream(openedFile);
                            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                            String line = "";
                            while((line = br.readLine())!= null){
                                data = data+line+"\n";
                            }
                            br.close();
                            fis.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        NewTab newTab2 = new NewTab();
                        jtb.addTab(openedFileName, newTab2);
                        newTab2.setLocation(openedDirectory+openedFileName);
                        if(isMyExtension){
                            data = decryptText(data);
                            isMyExtension = false;
                        }
                        newTab2.ta.setText(data);  
                        newTab2.requestFocus();                
                    }
                    break;

            case "Close Tab":
                if(jtb.getTitleAt(jtb.getSelectedIndex()) != "Untitled Tab"){
                    NewTab closeTab = (NewTab)jtb.getSelectedComponent();
                    TextArea retrievedTextArea = closeTab.getTextArea();
                    String location = closeTab.getLocationString();
                    if(location.endsWith(".gg")){
                        isMyExtension = true;
                    }
                    File closingF = new File(location);

                    try{
                    FileOutputStream fout = new FileOutputStream(closingF);
                    String lines = retrievedTextArea.getText();
                    if(isMyExtension){
                        lines = encryptText(lines);
                        isMyExtension = false;
                    }
                
                    fout.write(lines.getBytes());
                    fout.close();
                    jtb.removeTabAt(jtb.getSelectedIndex());
                    }
                    catch(FileNotFoundException fe){
                        fe.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    FileDialog closingFd = new FileDialog(this, "Save File", FileDialog.SAVE);
                    if(jtb.getTabCount() == 1){
                        int closingIndex = jtb.getSelectedIndex();
                        jtb.removeTabAt(closingIndex);
                        this.dispose();
                        return;
                    }
                    
                    closingFd.setVisible(true);
                    if(closingFd.getDirectory() == null || closingFd.getFile() == null){return;}
                    String closingDirectory = closingFd.getDirectory();
                    String closingFileName = closingFd.getFile();
                    if(closingFileName.endsWith(".gg")){
                        isMyExtension = true;
                    }
                    int closingIndex = jtb.getSelectedIndex();
                    jtb.setTitleAt(closingIndex,closingFileName);

                    File closingFile = new File(closingDirectory+closingFileName);
                    Component closingSelectedComponent = jtb.getSelectedComponent();
                    NewTab closingTab = (NewTab)closingSelectedComponent;
                    closingTab.setLocation(closingDirectory+closingFileName);
                    TextArea closingTextArea = closingTab.getTextArea();
                    String closingText = closingTextArea.getText();

                    try {
                        FileOutputStream fOverWrite = new FileOutputStream(closingFile);
                        if(isMyExtension){
                            closingText = encryptText(closingText);
                            isMyExtension = false;
                        }
                        fOverWrite.write(closingText.getBytes());
                        fOverWrite.close();
                        jtb.removeTabAt(closingIndex);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                
                    
                }
                    break;
            
            case "Close Window":
                    this.dispose();
                break;

            case "Find":
                NewTab findTab = (NewTab)jtb.getSelectedComponent();
                TextArea findArea = findTab.getTextArea();
                MyDialog dnew = new MyDialog(this, "Find", findArea);
                dnew.setVisible(true);
                break;
            case "Replace":
                NewTab replaceTab = (NewTab)jtb.getSelectedComponent();
                TextArea replacArea = replaceTab.getTextArea();
                MyDialogReplace drnew = new MyDialogReplace(this, "Replace", replacArea);
                drnew.setVisible(true);

                break;

            case "Zoom In":
                NewTab zoomTab = (NewTab)jtb.getSelectedComponent();
                zoomTab.zoomIn();
                break;

            case "Zoom Out":
                NewTab zoomOutTab = (NewTab)jtb.getSelectedComponent();
                zoomOutTab.zoomOut();
                break;

            default:
                break;
        }
    }

    class NewTab extends JPanel{
        TextArea ta = new TextArea();
        float zoomLevel = 1.0f;
        String location;
        public NewTab(){
        ta.setSize(this.WIDTH, this.HEIGHT);
        setLayout(new BorderLayout());
        add(ta, BorderLayout.CENTER);
       }

       public TextArea getTextArea(){
        return ta;
       }

       public void setLocation(String location){
            this.location = location;
       }
       
       public String getLocationString(){
            return this.location;
       }

       public void zoomIn(){
            this.zoomLevel += 0.1;
            this.ta.setFont(ta.getFont().deriveFont(zoomLevel * ta.getFont().getSize()));
           
       }

       public void zoomOut(){
        
            this.zoomLevel -= 0.1;
            this.ta.setFont(ta.getFont().deriveFont(zoomLevel * ta.getFont().getSize()));
         
        
       }

      


    }

    class MyDialog extends Dialog implements ActionListener, FocusListener{
        TextField tf ;
        String pattern;
        String data;
        int position = 0;
        TextArea ta;
        Label l1;
        MyDialog(Frame parent, String title, TextArea ta){
            super(parent, title, false);
            setLayout(new FlowLayout());
            setSize(700,100);
            tf = new TextField(10);
            
            this.ta = ta;
            data = this.ta.getText();
            add(new Label("Enter the text to find"), FlowLayout.LEFT);
            l1 = new Label("                                                   ");
            add(tf, FlowLayout.LEFT);
            Button f = new Button("Find");
            f.addActionListener(this);
            f.addFocusListener(this);
            add(f, FlowLayout.CENTER);
            add(l1, FlowLayout.RIGHT);


            addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    dispose();
                }
            });


        } 
        
        public String getSeaerchText(){
            return tf.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(tf.getText().length()!=0 && data.length()!=0){
                this.pattern = tf.getText();
                data = data.toLowerCase();
                data = data.replace("\r\n", "\n");
                String backupPattern = this.pattern;
                pattern = pattern.toLowerCase();
                
                int location = data.indexOf(pattern, position);
                if(position != data.length()){
                int currentPosition = position;
                
                if(currentPosition!=0 && location == -1){
                    position = 0;
                    location = data.indexOf(pattern, position);
                    this.ta.select(location, location + pattern.length());
                    this.ta.requestFocus();
                    this.l1.setText(" \" "+backupPattern+" \" is present at index "+location);

                    position = location + pattern.length() - 1;
                    return;
                }
                if(currentPosition == 0 && location == -1){
                    this.l1.setText(" \" "+backupPattern+" \" was not found");

                    return;
                }
                else{
                    this.ta.select(location, location + pattern.length());
                    this.ta.requestFocus();
                    this.l1.setText(" \" "+backupPattern+" \" is present at index "+location);

                position = location + pattern.length() - 1;
                }
            }
                
            }
           


        }

        @Override
        public void focusGained(FocusEvent e) {
            this.data = this.ta.getText();
        }

        @Override
        public void focusLost(FocusEvent e) {
            
        }
    }
    

    
    class MyDialogReplace extends Dialog implements ActionListener, FocusListener{
        TextField tf ;
        Button f;
        TextArea ta;
        TextField repField;
        Button replaceNext;    
        Button repAll;
        String pattern;
        String data;
        int position = 0;
        Label l1;
        MyDialogReplace(Frame parent, String title, TextArea ta){
            super(parent, title, false);
            setLayout(new FlowLayout());
            setSize(700,100);
            tf = new TextField(10);
            
            this.ta = ta;
            data = this.ta.getText();
            replaceNext = new Button("Replace");
            repAll = new Button("Replace All");
            repField = new TextField(10);

            add(tf, FlowLayout.LEFT);
            f = new Button("Find");
            replaceNext.addActionListener(this);
            repAll.addActionListener(this);
            add(repField);
        add(replaceNext);
        add(repAll);
            f.addActionListener(this);
            replaceNext.addFocusListener(this);
            add(f, FlowLayout.CENTER);



            addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    dispose();
                }
            });


        } 
        
        public String getSeaerchText(){
            return tf.getText();
        }

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == f){
            if(tf.getText().length()!=0 && data.length()!=0){
                this.pattern = tf.getText();
                data = ta.getText();
                data = data.toLowerCase();
                data = data.replace("\r\n", "\n");
                String backupPattern = this.pattern;
                pattern = pattern.toLowerCase();
                int location = data.indexOf(pattern, position);
                if (location != -1) {
                    ta.select(location, location + pattern.length());
                    ta.requestFocus();
                    position = location + pattern.length();
                } else {
                    position = 0;
                    location = data.indexOf(pattern, position);
                    if (location != -1){
                        ta.select(location, location + pattern.length());
                        ta.requestFocus();
                        position = location + pattern.length();
                    } else {
                        position = 0;
                        ta.select(0, 0);
                    }
                
            }
        }
    }
           
        if(e.getSource() == repAll){
            if(repField.getText().length() == 0 || tf.getText().length() == 0){
                return;
            }
            else{
                String data = ta.getText();
                String backupData = data;
                data = data.toLowerCase();
                String pattern = tf.getText();
                pattern = pattern.toLowerCase();
                String repPattern = repField.getText();
                String backupRepPattern = repPattern;
                repPattern = repPattern.toLowerCase();
                if(data.indexOf(pattern, 0) == -1){
                    return;
                }
                StringBuilder newData = new StringBuilder();
                int start = 0;

                while(true){
                    int index = data.indexOf(pattern, start);
                    if(index == -1){
                        newData.append(backupData.substring(start));
                        break;
                    }

                    newData.append(backupData.substring(start, index));
                    newData.append(backupRepPattern);
                    start = index + pattern.length();
                }

                ta.setText(newData.toString());

                

            }
        }

        if(e.getSource() == replaceNext){
            if(repField.getText().length() == 0 || tf.getText().length() == 0){
                return;
            }
            else{
                int start = ta.getSelectionStart();
                int end = ta.getSelectionEnd();
                if(start == -1 || start == end || end < start){
                    return;
                }
                String newData = "";
                newData = data.substring(0, start);
                newData = newData + repField.getText();
                newData = newData + data.substring(end, data.length());
                ta.setText(newData);
        }
            
        }

        }

        @Override
        public void focusGained(FocusEvent e) {
            this.data = this.ta.getText();
        }

        @Override
        public void focusLost(FocusEvent e) {
            
        }
    }

}

public class NotepadDemo{
    public static void main(String[] args){
        new Notepad("Notepad");
    }
}