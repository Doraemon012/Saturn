
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.text.*;

//fonts
// keyadapter - abstract chass for receiving keyboard events
// action listner - evernt handler class, define what should be done when an user performs certain operation 
// keylistner - listener interface for receiving keyboard events (keystrokes)

public class IDE extends KeyAdapter implements ActionListener, KeyListener {
    JSplitPane splitPane; 

    static Color Key_H = Color.BLUE;
    static String python_name = "";

    static String javac_name = "";

    static String java_name = "";

    static String cpp_name = "";

    static String c_name = "";

    // Default Font Size for text
    int fsize = 17;

    static String current_file_path = "";

    String all_terminal_text = "";

    static String exit_code;

    private static JTextPane area;
    private JTextPane terminal;

    private JScrollPane scpane;
    private JScrollPane scpanet;

    String text = "";
    static String current_language = "None";

    // Creating Frame and setting up the title
    JFrame f = new JFrame("Saturn IDE");

    JTextField title;
    Font newFont;
    JPanel bottomPanel;
    JLabel detailsOfFile;
    JPanel runPanel;
    Button runX;
    JList fontFamilyList, fontStyleList, fontSizeList;
    JScrollPane sb;
    JMenuBar menuBar;
    JMenu file, edit, format, theme;
    JMenuItem newdoc, open, save, print, exit, copy, paste, cut, selectall, fontfamily, fontstyle, fontsize, run, light,
            dark;

    // Defining List of Fonts for Text
    String fontFamilyValues[] = { "Dyuthi", "Chilanka", "DejaVu Sans", "FreeMono", "FreeSerif", "Nimbus Sans",
            "URW Gothic",
            "Tlwg Mono", "Trimmana", "Ubuntu Mono", "Ubuntu" };

    // Defining List of Font Size for Text
    String fontSizeValues[] = { "5", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60", "65", "70" };
    int[] stylevalue = { Font.PLAIN, Font.BOLD, Font.ITALIC };

    // Defining List of Font Styles for Text
    String[] fontStyleValues = { "PLAIN", "BOLD", "ITALIC" };

    String fontFamily, fontSize, fontStyle;

    int fstyle;
    int cl;
    int linecount;
    JScrollPane sp;
    Color text_color_dark = new Color(255, 255, 255);
    Color background_color_dark = new Color(0, 0, 0);
    Color terminal_color_dark = new Color(20, 20, 20);

    Color line_no_Color_dark = new Color(39, 55, 77);
    Color line_no_Color_light = new Color(221, 230, 237);

    Color text_color_light = new Color(0, 0, 0);
    Color background_color_light = new Color(255, 255, 255);
    Color terminal_color_light = new Color(220, 220, 220);

    JTextArea lines;

    public IDE() {
        // Calling initUI() method to initiliaze UI
        initUI();
        // Calling addActionEvents() method to add events
        addActionEvents();

    }

    /**
     * The actionPerformed method handles various actions performed by the user,
     * such as opening,
     * saving, printing, copying, pasting, cutting, selecting all, changing font
     * family, style, and
     * size, running code, and changing the theme.
     * 
     * @param ae The parameter "ae" is of type ActionEvent. It represents the event
     *           that occurred, such
     *           as a button click or menu item selection.
     */

    public void actionPerformed(ActionEvent ae) {
        // if new option is choosen
        if (ae.getActionCommand().equals("New")) {
            // Setting Text as empty by default
            area.setText("");

        } // if open option is chosen
        else if (ae.getActionCommand().equals("Open")) {
            // Setting current by default directory "C" folder
            JFileChooser chooser = new JFileChooser("C:/");
            chooser.setAcceptAllFileFilterUsed(false);
            // Allowing only text (.txt) files extension to open
            FileNameExtensionFilter txt = new FileNameExtensionFilter(".txt files", "txt");
            chooser.addChoosableFileFilter(txt);
            FileNameExtensionFilter py = new FileNameExtensionFilter(".py files", "py");
            chooser.addChoosableFileFilter(py);
            FileNameExtensionFilter cpp_files = new FileNameExtensionFilter(".cpp files", "cpp");
            chooser.addChoosableFileFilter(cpp_files);
            FileNameExtensionFilter js_files = new FileNameExtensionFilter(".js files", "js");
            chooser.addChoosableFileFilter(js_files);
            FileNameExtensionFilter c_files = new FileNameExtensionFilter(".c files", "c");
            chooser.addChoosableFileFilter(c_files);
            FileNameExtensionFilter java_files = new FileNameExtensionFilter(".java files", "java");
            chooser.addChoosableFileFilter(java_files);
            int result = chooser.showOpenDialog(f);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String filename = file.getName();
                current_language = filename.substring(filename.lastIndexOf("."), filename.length());
                current_file_path = file.getAbsolutePath();
                detailsOfFile.setText("Path: " + current_file_path + " Lang: " + current_language);
                System.out.println(current_language + " aaraha hai");

                try {

                    // Reading the file
                    FileReader reader = new FileReader(file);

                    BufferedReader br = new BufferedReader(reader);
                    area.read(br, null);
                    // Closing the file after reading
                    // Clearing the memory
                    br.close();
                    area.requestFocus();
                    readFileAndHighlight(area, current_file_path, current_language);
                    lines.setText(getText());

                } catch (Exception e) {
                    System.out.print(e);
                }
                f.setTitle(current_file_path + " - Saturn IDE");
            }
        } // if save option is choosen
        else if (ae.getActionCommand().equals("Save")) {
            if (current_file_path == "") {
                final JFileChooser SaveAs = new JFileChooser();
                JFileChooser chooser = new JFileChooser("C:/");
                chooser.setAcceptAllFileFilterUsed(false);
                // Allowing only text (.txt) files extension to open
                FileNameExtensionFilter txt = new FileNameExtensionFilter(".txt files", "txt");
                SaveAs.addChoosableFileFilter(txt);
                FileNameExtensionFilter py = new FileNameExtensionFilter(".py files", "py");
                SaveAs.addChoosableFileFilter(py);
                FileNameExtensionFilter cpp_files = new FileNameExtensionFilter(".cpp files", "cpp");
                SaveAs.addChoosableFileFilter(cpp_files);
                FileNameExtensionFilter js_files = new FileNameExtensionFilter(".js files", "js");
                SaveAs.addChoosableFileFilter(js_files);
                FileNameExtensionFilter c_files = new FileNameExtensionFilter(".c files", "c");
                SaveAs.addChoosableFileFilter(c_files);
                FileNameExtensionFilter java_files = new FileNameExtensionFilter(".java files", "java");
                SaveAs.addChoosableFileFilter(java_files);
                SaveAs.setApproveButtonText("Save");
                // Opening the dialog and asking from user where to save the file.
                int actionDialog = SaveAs.showOpenDialog(f);
                if (actionDialog != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                ///////////////////////////////////////////////////////////////////
                String ext = "";

                String extension = SaveAs.getFileFilter().getDescription();

                if (extension.equals(".py files")) {
                    ext = ".py";
                } else if (extension.equals(".txt files")) {
                    ext = ".txt";
                } else if (extension.equals(".cpp files")) {
                    ext = ".cpp";
                } else if (extension.equals(".c files")) {
                    ext = ".c";
                } else if (extension.equals(".js files")) {
                    ext = ".js";
                } else if (extension.equals(".java files")) {
                    ext = ".java";

                }
                current_file_path = SaveAs.getSelectedFile().getAbsolutePath() + ext;
                current_language = ext;
                detailsOfFile.setText("Path: " + current_file_path + " Lang: " + current_language);

                System.out.println(current_file_path);
                System.out.println(extension);

                f.setTitle(current_file_path + " - Saturn IDE");

                File fileName = new File(SaveAs.getSelectedFile() + ext);
                BufferedWriter outFile = null;
                try {
                    outFile = new BufferedWriter(new FileWriter(fileName));
                    area.write(outFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                lines.setText(getText());

            } else if (current_file_path != "") {
                File fileName = new File(current_file_path);
                BufferedWriter outFile = null;
                try {
                    outFile = new BufferedWriter(new FileWriter(fileName));
                    area.write(outFile);

                    // readFileAndHighlight(area, current_file_path, current_language);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                lines.setText(getText());

            }
        } // if print option is choosen
        else if (ae.getActionCommand().equals("Print")) {
            try {
                // printer dialog will open
                area.print();
            } catch (Exception e) {
            }
        } // if exit option is choosen
        else if (ae.getActionCommand().equals("Exit")) {
            // Destroying/Closing the frame/window
            f.dispose();
        } // if copy option is choosen
        else if (ae.getActionCommand().equals("Copy")) {
            // Getting Selected Selected Text
            text = area.getSelectedText();
        } // if paste option is choosen
        else if (ae.getActionCommand().equals("Paste")) {
            area.paste();
            // area.insertComponent(copy);(text, area.getCaretPosition()); 
        } // if cut option is choosen
        else if (ae.getActionCommand().equals("Cut")) {
            text = area.getSelectedText();
            // area.replaceRange("", area.getSelectionStart(), area.getSelectionEnd()); //
        } // if select all option is choosen
        else if (ae.getActionCommand().equals("Select All")) {
            // Selecting all text
            area.selectAll();
        } // if font family change option is choosen
        else if (ae.getActionCommand().equals("Font Family")) {
            // Setting up Font Family
            JOptionPane.showConfirmDialog(null, fontFamilyList, "Choose Font Family", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            fontFamily = String.valueOf(fontFamilyList.getSelectedValue());
            newFont = new Font(fontFamily, fstyle, fsize);
            area.setFont(newFont);
            lines.setFont(newFont);
            terminal.setFont(newFont);
            lines.setText(getText());

        } // if font style change option is choosen
        else if (ae.getActionCommand().equals("Font Style")) {
            // Setting up Font Style
            JOptionPane.showConfirmDialog(null, fontStyleList, "Choose Font Style", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            fstyle = stylevalue[fontStyleList.getSelectedIndex()];
            newFont = new Font(fontFamily, fstyle, fsize);
            area.setFont(newFont);
            lines.setFont(newFont);
            terminal.setFont(newFont);
            lines.setText(getText());

        } // if font size change option is choosen
        else if (ae.getActionCommand().equals("Font Size")) {
            // Setting up Font Size
            JOptionPane.showConfirmDialog(null, fontSizeList, "Choose Font Size", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
            fontSize = String.valueOf(fontSizeList.getSelectedValue());
            fsize = Integer.parseInt(fontSize);
            newFont = new Font(fontFamily, fstyle, fsize);
            area.setFont(newFont);
            lines.setFont(newFont);
            terminal.setFont(newFont);
            lines.setText(getText());

        } else if (ae.getActionCommand().equals("Run")) {
            if (current_file_path == "") {
                final JFileChooser SaveAs = new JFileChooser();
                JFileChooser chooser = new JFileChooser("C:/");
                chooser.setAcceptAllFileFilterUsed(false);
                // Allowing only text (.txt) files extension to open
                FileNameExtensionFilter txt = new FileNameExtensionFilter(".txt files", "txt");
                SaveAs.addChoosableFileFilter(txt);
                FileNameExtensionFilter py = new FileNameExtensionFilter(".py files", "py");
                SaveAs.addChoosableFileFilter(py);
                FileNameExtensionFilter cpp_files = new FileNameExtensionFilter(".cpp files", "cpp");
                SaveAs.addChoosableFileFilter(cpp_files);
                FileNameExtensionFilter js_files = new FileNameExtensionFilter(".js files", "js");
                SaveAs.addChoosableFileFilter(js_files);
                FileNameExtensionFilter c_files = new FileNameExtensionFilter(".c files", "c");
                SaveAs.addChoosableFileFilter(c_files);
                FileNameExtensionFilter java_files = new FileNameExtensionFilter(".java files", "java");
                SaveAs.addChoosableFileFilter(java_files);
                SaveAs.setApproveButtonText("Save");
                // Opening the dialog and asking from user where to save the file.
                int actionDialog = SaveAs.showOpenDialog(f);
                if (actionDialog != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                ///////////////////////////////////////////////////////////////////
                String ext = "";

                String extension = SaveAs.getFileFilter().getDescription();

                if (extension.equals(".py files")) {
                    ext = ".py";
                } else if (extension.equals(".txt files")) {
                    ext = ".txt";
                } else if (extension.equals(".cpp files")) {
                    ext = ".cpp";
                } else if (extension.equals(".c files")) {
                    ext = ".c";
                } else if (extension.equals(".js files")) {
                    ext = ".js";
                } else if (extension.equals(".java files")) {
                    ext = ".java";

                }
                current_file_path = SaveAs.getSelectedFile().getAbsolutePath() + ext;
                current_language = ext;
                detailsOfFile.setText("Path: " + current_file_path + " Lang: " + current_language);

                System.out.println(current_file_path);
                System.out.println(extension);
                f.setTitle(current_file_path + " - Saturn IDE");

                File fileName = new File(SaveAs.getSelectedFile() + ext);
                BufferedWriter outFile = null;
                try {
                    outFile = new BufferedWriter(new FileWriter(fileName));
                    area.write(outFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    // readFileAndHighlight(area, current_file_path, extension);

                } catch (Exception e) {
                    System.out.print(e);
                }
                lines.setText(getText());

            } else if (current_file_path != "") {
                File fileName = new File(current_file_path);
                BufferedWriter outFile = null;
                try {
                    outFile = new BufferedWriter(new FileWriter(fileName));
                    area.write(outFile);

                    // readFileAndHighlight(area, current_file_path, current_language);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                lines.setText(getText());

            }
            // Setting up Font Size
            System.out.println(current_language);
            if (current_language.equals(".py")) {
                if (python_name == "") {
                    python_name = getPythonInterpreterPath();
                    String out = runCommand(python_name + " " + current_file_path);
                    all_terminal_text += out + "\n" + exit_code + "\n";
                    terminal.setText(all_terminal_text);
                } else {
                    String out = runCommand(python_name + " " + current_file_path);
                    all_terminal_text += out + "\n" + exit_code + "\n";
                    terminal.setText(all_terminal_text);
                }
                try {
                    // readFileAndHighlight(area, current_file_path, ".py");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (current_language.equals(".java")) {

                if (javac_name == "" && java_name == "") {
                    javac_name = getJavacInterpreterPath();
                    java_name = getJavaInterpreterPath();
                    System.out.println(java_name);
                    System.out.println(javac_name);
                    String out = runCommand(javac_name + " " + current_file_path);
                    String out2 = runJavaFileCommand(java_name, current_file_path);
                    System.out.println(out + " " + out2);
                    all_terminal_text += out + "\n" + out2 + "\n" + exit_code + "\n";
                    terminal.setText(all_terminal_text);
                } else {
                    System.out.println(java_name);
                    System.out.println(javac_name);
                    String out = runCommand(javac_name + " " + current_file_path);
                    String out2 = runJavaFileCommand(java_name, current_file_path);
                    System.out.println(out + " " + out2);
                    all_terminal_text += out + "\n" + out2 + "\n" + exit_code + "\n";
                    terminal.setText(all_terminal_text);

                }
                try {
                    readFileAndHighlight(area, current_file_path, ".java");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (current_language.equals(".cpp")) {
                if (cpp_name == "") {
                    cpp_name = getCppInterpreterPath();
                    String out = runCppFileCommand(cpp_name, current_file_path);

                    all_terminal_text += out + "\n";
                    terminal.setText(all_terminal_text);
                } else {
                    String out = runCppFileCommand(cpp_name, current_file_path);

                    all_terminal_text += out + "\n";
                    terminal.setText(all_terminal_text);
                }
                try {
                    readFileAndHighlight(area, current_file_path, ".cpp");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (current_language.equals(".c")) {
                if (c_name == "") {
                    c_name = getCInterpreterPath();
                    String out = runCFileCommand(c_name, current_file_path);

                    all_terminal_text += out + "\n";
                    terminal.setText(all_terminal_text);
                } else {
                    String out = runCFileCommand(c_name, current_file_path);

                    all_terminal_text += out + "\n";
                    terminal.setText(all_terminal_text);
                }
                try {
                    readFileAndHighlight(area, current_file_path, ".c");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (current_language.equals(".js")) {
                try {
                    readFileAndHighlight(area, current_file_path, ".js");
                    cantRunLanguageAlert("js");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (current_language.equals(".txt")) {
                cantRunLanguageAlert(".txt");
            }

        } else if (ae.getActionCommand().equals("Dark")) {
            darkTheme();
            // System.out.println("trying to highlight", area.gates);

            if (current_language != "") {
                // highlight(area, area.getStyledDocument(), current_language);
            }

        } else if (ae.getActionCommand().equals("Light")) {
            lightTheme();
            if (current_language != "") {
                highlight(area, area.getStyledDocument(), current_language);

            }
        }
    }

    /**
     * The function `runCommand` executes a command in the command line and returns
     * the output as a
     * string.
     * 
     * @param command The "command" parameter is a string that represents the
     *                command you want to
     *                execute in the command line. It can be any valid command that
     *                you would normally run in the
     *                command prompt or terminal.
     * @return The method is returning a String, which is the output of the command
     *         that was executed.
     */
    public static String runCommand(String command) {
        String out = "";
        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec(command);

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                out += line + "\n";
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            exit_code = "Exited with error code : " + exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * The `initUI()` function initializes the user interface for a Java
     * application, including
     * creating menus, text areas, scroll panes, and setting up event listeners.
     */
    public void initUI() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // do this
        detailsOfFile = new JLabel();

        bottomPanel = new JPanel();

        runPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JPanel panel = new JPanel(new GridLayout(2, 1));

        // Creating Menubar
        menuBar = new JMenuBar();

        // Creating Menu "File"
        file = new JMenu("File");

        // Creating MenuItem "New"
        newdoc = new JMenuItem("New");

        // Assigning shortcut "Cntrl + N" for "New" Menu Item
        newdoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Open"
        open = new JMenuItem("Open");
        // Assigning shortcut "Cntrl + O" for "Open" Menu Item
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));

        run = new JMenuItem("Run");

        // Assigning shortcut "Cntrl + O" for "Open" Menu Item
        run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Save"
        save = new JMenuItem("Save");

        // Assigning shortcut "Cntrl + S" for "Save" Menu Item
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Print"
        print = new JMenuItem("Print");

        // Assigning shortcut "Cntl + P" for "Print" Menu Item
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Exit"
        exit = new JMenuItem("Exit");

        // Assigning shortcut "ESC" for "Exit" Menu Item
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));

        // Creating Menu "Edit"
        edit = new JMenu("Edit");

        // Creating MenuItem "Copy"
        copy = new JMenuItem("Copy");

        // Assigning shortcut "Cntrl + C" for "Copy" Menu Item
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Paste"
        paste = new JMenuItem("Paste");

        // Assigning shortcut "Cntrl + V" for "Paste" Menu Item
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Cut"
        cut = new JMenuItem("Cut");

        // Assigning shortcut "Cntrl + X" for "Cut" Menu Item
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));

        // Creating MenuItem "Select All"
        selectall = new JMenuItem("Select All");

        // Assigning shortcut "Cntrl + A" for "Select All" Menu Item
        selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

        // Creating Menu "Format"
        format = new JMenu("Format");

        // Creating MenuItem "Font Family"
        fontfamily = new JMenuItem("Font Family");

        // Creating MenuItem "Font Style"
        fontstyle = new JMenuItem("Font Style");

        // Creating MenuItem "Font Size"
        fontsize = new JMenuItem("Font Size");

        // Creating List of Font Family and assigning the list values
        fontFamilyList = new JList(fontFamilyValues);

        // Creating List of Font Styles and assigning the list values
        fontStyleList = new JList(fontStyleValues);

        // Creating List of Font Size and assigning the list values
        fontSizeList = new JList(fontSizeValues);

        theme = new JMenu("Theme");
        light = new JMenuItem("Light");
        dark = new JMenuItem("Dark");

        // Allowing user to select only one option
        fontFamilyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontStyleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // JScrollPane jsp = new JScrollPane();

        // TextArea / Editor of Notepad
        area = new JTextPane();

        lines = new JTextArea("1");
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);

        area.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = area.getDocument().getLength();
                Element root = area.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
        });

        // Default font will be sam_serif and default font style will be plain and
        // default style will be 20.
        area.setFont(new Font("FreeMono", Font.BOLD, 20));
        lines.setFont(new Font("FreeMono", Font.BOLD, 20));

        // Sets the line-wrapping policy of the text area no line wap
        // Sets the word-wrapping policy of the text area
        // area.setWrapStyleWord(true);

        // Creating Scrollables around textarea
        scpane = new JScrollPane();
        scpane.getViewport().add(area);
        scpane.setRowHeaderView(lines);
        panel.add(scpane);
        // //Creating border for scrollpane
        // scpane.setBorder(BorderFactory.createEmptyBorder());

        // Adding menubar in frame
        f.setJMenuBar(menuBar);

        // Adding all menus in menubars
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(format);
        menuBar.add(theme);

        file.add(newdoc);
        file.add(open);
        file.add(run);
        file.add(save);
        file.add(print);
        file.add(exit);

        edit.add(copy);
        edit.add(paste);
        edit.add(cut);
        edit.add(selectall);

        format.add(fontfamily);
        format.add(fontstyle);
        format.add(fontsize);

        theme.add(light);
        theme.add(dark);

        terminal = new JTextPane();

        // //Default font will be sam_serif and default font style will be plain and
        // default style will be 20.
        terminal.setFont(new Font("FreeMono", Font.BOLD, 15));

        // //Sets the line-wrapping policy of the text area

        scpanet = new JScrollPane(terminal);

        // Creating border for scrollpane
        // scpanet.setBorder(BorderFactory.createEmptyBorder());

        Color color = new Color(220, 220, 220);
        terminal.setBackground(color);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scpane, scpanet);
        splitPane.setResizeWeight(0.8); // Set initial resize weight (80% for top and 20% for bottom)
        // panel.add(scpanet);
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        splitPane.setBorder(null);
        bottomPanel.add(detailsOfFile);
        runX = new Button("▶️");
        runX.setForeground(new Color(0, 153, 51));

        runX.setFont(new Font("Arial", Font.PLAIN, 15));
        runPanel.add(runX);

        runX.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(current_language);
                if (current_file_path == "") {
                    final JFileChooser SaveAs = new JFileChooser();
                    JFileChooser chooser = new JFileChooser("C:/");
                    chooser.setAcceptAllFileFilterUsed(false);
                    // Allowing only text (.txt) files extension to open
                    FileNameExtensionFilter txt = new FileNameExtensionFilter(".txt files", "txt");
                    SaveAs.addChoosableFileFilter(txt);
                    FileNameExtensionFilter py = new FileNameExtensionFilter(".py files", "py");
                    SaveAs.addChoosableFileFilter(py);
                    FileNameExtensionFilter cpp_files = new FileNameExtensionFilter(".cpp files", "cpp");
                    SaveAs.addChoosableFileFilter(cpp_files);
                    FileNameExtensionFilter js_files = new FileNameExtensionFilter(".js files", "js");
                    SaveAs.addChoosableFileFilter(js_files);
                    FileNameExtensionFilter c_files = new FileNameExtensionFilter(".c files", "c");
                    SaveAs.addChoosableFileFilter(c_files);
                    FileNameExtensionFilter java_files = new FileNameExtensionFilter(".java files", "java");
                    SaveAs.addChoosableFileFilter(java_files);
                    SaveAs.setApproveButtonText("Save");
                    // Opening the dialog and asking from user where to save the file.
                    int actionDialog = SaveAs.showOpenDialog(f);
                    if (actionDialog != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    ///////////////////////////////////////////////////////////////////
                    String ext = "";

                    String extension = SaveAs.getFileFilter().getDescription();

                    if (extension.equals(".py files")) {
                        ext = ".py";
                    } else if (extension.equals(".txt files")) {
                        ext = ".txt";
                    } else if (extension.equals(".cpp files")) {
                        ext = ".cpp";
                    } else if (extension.equals(".c files")) {
                        ext = ".c";
                    } else if (extension.equals(".js files")) {
                        ext = ".js";
                    } else if (extension.equals(".java files")) {
                        ext = ".java";

                    }
                    current_file_path = SaveAs.getSelectedFile().getAbsolutePath() + ext;
                    current_language = ext;
                    detailsOfFile.setText("Path: " + current_file_path + " Lang: " + current_language);

                    System.out.println(current_file_path);
                    System.out.println(extension);
                    f.setTitle(current_file_path + " - Saturn IDE");

                    File fileName = new File(SaveAs.getSelectedFile() + ext);
                    BufferedWriter outFile = null;
                    try {
                        outFile = new BufferedWriter(new FileWriter(fileName));
                        area.write(outFile);

                    } catch (IOException ew) {
                        ew.printStackTrace();
                    }
                    try {

                        // readFileAndHighlight(area, current_file_path, extension);

                    } catch (Exception es) {
                        System.out.print(es);
                    }
                    lines.setText(getText());

                } else if (current_file_path != "") {
                    File fileName = new File(current_file_path);
                    BufferedWriter outFile = null;
                    try {
                        outFile = new BufferedWriter(new FileWriter(fileName));
                        area.write(outFile);

                        // readFileAndHighlight(area, current_file_path, current_language);

                    } catch (Exception ea) {
                        ea.printStackTrace();
                    }
                    lines.setText(getText());

                }
                if (current_language.equals(".py")) {
                    try {
                        readFileAndHighlight(area, current_file_path, ".py");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if (python_name == "") {
                        python_name = getPythonInterpreterPath();
                        String out = runCommand(python_name + " " + current_file_path);
                        all_terminal_text += out + "\n" + exit_code + "\n";
                        terminal.setText(all_terminal_text);

                    } else {
                        String out = runCommand(python_name + " " + current_file_path);
                        all_terminal_text += out + "\n" + exit_code + "\n";
                        terminal.setText(all_terminal_text);
                    }

                } else if (current_language.equals(".java")) {
                    try {
                        readFileAndHighlight(area, current_file_path, ".java");
                    } catch (Exception e5) {
                        e5.printStackTrace();
                    }
                    if (javac_name == "" && java_name == "") {
                        javac_name = getJavacInterpreterPath();
                        java_name = getJavaInterpreterPath();
                        System.out.println(java_name);
                        System.out.println(javac_name);
                        String out = runCommand(javac_name + " " + current_file_path);
                        String out2 = runJavaFileCommand(java_name, current_file_path);
                        System.out.println(out + " " + out2);
                        all_terminal_text += out + "\n" + out2 + "\n" + exit_code + "\n";
                        terminal.setText(all_terminal_text);
                    } else {
                        System.out.println(java_name);
                        System.out.println(javac_name);
                        String out = runCommand(javac_name + " " + current_file_path);
                        String out2 = runJavaFileCommand(java_name, current_file_path);
                        System.out.println(out + " " + out2);
                        all_terminal_text += out + "\n" + out2 + "\n" + exit_code + "\n";
                        terminal.setText(all_terminal_text);

                    }

                } else if (current_language.equals(".cpp")) {
                    try {
                        readFileAndHighlight(area, current_file_path, ".cpp");
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    if (cpp_name == "") {
                        cpp_name = getCppInterpreterPath();
                        String out = runCppFileCommand(cpp_name, current_file_path);

                        all_terminal_text += out + "\n";
                        terminal.setText(all_terminal_text);
                    } else {
                        String out = runCppFileCommand(cpp_name, current_file_path);

                        all_terminal_text += out + "\n";
                        terminal.setText(all_terminal_text);
                    }

                } else if (current_language.equals(".c")) {
                    try {
                        readFileAndHighlight(area, current_file_path, ".c");
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    if (c_name == "") {
                        c_name = getCInterpreterPath();
                        String out = runCFileCommand(c_name, current_file_path);

                        all_terminal_text += out + "\n";
                        terminal.setText(all_terminal_text);
                    } else {
                        String out = runCFileCommand(c_name, current_file_path);

                        all_terminal_text += out + "\n";
                        terminal.setText(all_terminal_text);
                    }

                } else if (current_language.equals(".js")) {
                    try {
                        readFileAndHighlight(area, current_file_path, ".js");
                        cantRunLanguageAlert("js");

                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }

                } else if (current_language.equals(".txt")) {
                    cantRunLanguageAlert(".txt");
                }
            }
        });

        // Setting up the size of frame
        f.setSize(980, 480);

        // Setting up the layout of frame
        f.setLayout(new BorderLayout());
        f.add(splitPane, BorderLayout.CENTER);

        // Adding panels in frame
        // f.add(panel);
        // f.add(, BorderLayout.CENTER);

        f.add(bottomPanel, BorderLayout.SOUTH);
        f.add(runPanel, BorderLayout.NORTH);

        // Setting Frame visible to user
        f.setLocationRelativeTo(null);

        f.setVisible(true);
    }

    /**
     * The darkTheme() function sets the background and foreground colors of various
     * components to
     * create a dark theme.
     */
    public void darkTheme() {
        terminal.setBackground(terminal_color_dark);
        terminal.setForeground(text_color_dark);

        area.setBackground(background_color_dark);
        area.setForeground(text_color_dark);
        lines.setBackground(line_no_Color_dark); // yeh vars hai ln:82
        lines.setForeground(text_color_dark);
        Key_H = Color.CYAN;
        runX.setBackground(line_no_Color_dark);
        area.setCaretColor(Color.WHITE); // Set the custom caret color

        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(line_no_Color_dark);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        splitPane.setBorder(null);
        runPanel.setBackground(line_no_Color_dark);
        bottomPanel.setBackground(line_no_Color_dark);
        detailsOfFile.setForeground(Color.WHITE);
    }

    /**
     * The lightTheme() function sets various colors and backgrounds to create a
     * light theme for a Java
     * application.
     */
    public void lightTheme() {
        area.setCaretColor(Color.BLACK); // Set the custom caret color
        terminal.setBackground(terminal_color_light);
        terminal.setForeground(text_color_light);
        runX.setBackground(line_no_Color_light);

        area.setBackground(background_color_light);
        area.setForeground(text_color_light);

        lines.setBackground(line_no_Color_light);
        lines.setForeground(text_color_light);
        Key_H = Color.BLUE;

        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {
                    }

                    @Override
                    public void paint(Graphics g) {
                        g.setColor(Color.WHITE);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
        splitPane.setBorder(null);
        runPanel.setBackground(line_no_Color_light);
        bottomPanel.setBackground(Color.WHITE);
        detailsOfFile.setForeground(Color.BLACK);

    }

    /**
     * The function registers action listeners to various buttons.
     */
    public void addActionEvents() {
        // registering action listener to buttons
        newdoc.addActionListener(this);
        save.addActionListener(this);
        print.addActionListener(this);
        exit.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        cut.addActionListener(this);
        selectall.addActionListener(this);
        open.addActionListener(this);
        run.addActionListener(this);
        light.addActionListener(this);
        dark.addActionListener(this);

        fontfamily.addActionListener(this);
        fontsize.addActionListener(this);
        fontstyle.addActionListener(this);

    }

    /**
     * The main function sets up a document listener for a text area, which triggers
     * syntax
     * highlighting when text is inserted or removed.
     * 
     * @param ar The parameter "ar" in the main method is an array of strings. It is
     *           commonly used to
     *           pass command line arguments to the program when it is executed.
     */
    public static void main(String ar[]) {
        IDE tnp = new IDE();
        area.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSyntaxHighlighting();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSyntaxHighlighting();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components do not fire these events
            }

            private void updateSyntaxHighlighting() {
                SwingUtilities.invokeLater(() -> {
                    // StyledDocument updatedDoc = area.getStyledDocument();
                    try {
                        if (current_file_path != "" && current_language != "None") {
                            readFileAndHighlight(area, current_file_path, current_language);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    public static String getPythonInterpreterPath() {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        if (isWindows == true) {
            python_name = "python";
            return "python";
        } else {

            try {
                // Execute the command
                Process process = Runtime.getRuntime().exec("python -c import sys; print(sys.version_info.major)");

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Wait for the process to complete
                int exitCode = process.waitFor();
                exit_code = "Exited with error code : " + exitCode;
                python_name = "python";

            } catch (IOException | InterruptedException e) {
                try {
                    // Execute the command
                    Process process = Runtime.getRuntime().exec("python2 -c import sys; print(sys.version_info.major)");

                    // Read the output of the command
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                    // Wait for the process to complete
                    int exitCode = process.waitFor();
                    exit_code = "Exited with error code : " + exitCode;
                    python_name = "python2";

                } catch (IOException | InterruptedException e1) {
                    try {
                        // Execute the command
                        Process process = Runtime.getRuntime()
                                .exec("python3 -c import sys; print(sys.version_info.major)");

                        // Read the output of the command
                        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                        // Wait for the process to complete
                        int exitCode = process.waitFor();
                        exit_code = "Exited with error code : " + exitCode;
                        python_name = "python3";
                    } catch (IOException | InterruptedException e2) {
                        e2.printStackTrace();
                        showNotInstalledAlert("Python");

                    }
                }
            }

            return python_name;
        }

    }

    /**
     * The function `getJavacInterpreterPath()` returns the path of the `javac`
     * interpreter if it is
     * installed, otherwise it shows an alert indicating that Java is not installed.
     * 
     * @return The method is returning the value of the variable "javac_name".
     */
    public static String getJavacInterpreterPath() {

        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec("javac --version");
            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // Wait for the process to complete
            int exitCode = process.waitFor();
            exit_code = "Exited with error code : " + exitCode;
            javac_name = "javac";

        } catch (IOException | InterruptedException e) {
            showNotInstalledAlert("Java");
            javac_name = "";

        }
        return javac_name;
    }

    /**
     * The function `getJavaInterpreterPath()` returns the path of the Java
     * interpreter if it is
     * installed, otherwise it returns an empty string.
     * 
     * @return The method is returning the value of the variable "java_name".
     */
    public static String getJavaInterpreterPath() {
        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec("java --version");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Wait for the process to complete
            int exitCode = process.waitFor();
            exit_code = "Exited with error code : " + exitCode;
            java_name = "java";
            System.out.println("installed hai");
        } catch (IOException | InterruptedException e1) {
            java_name = "";
        }

        return java_name;
    }

    /**
     * The function displays an error message indicating that a specific programming
     * language cannot be
     * run.
     * 
     * @param lang The lang parameter is a string that represents the programming
     *             language that the
     *             user is trying to run.
     */
    // The above code is defining a method called "showNotInstalledAlert" that takes
    // a parameter "lang"
    // of type String. The purpose of this method is not clear from the provided
    // code snippet.
    public static void showNotInstalledAlert(String lang) {
        // JOptionPane.showMessageDialog(null, lang + " " + " was not found on your
        // system. Install it to run the file .");
        JOptionPane.showMessageDialog(
                null,
                lang + " was not found on your system. Install it to run the file.",
                "Language Not Found",
                JOptionPane.ERROR_MESSAGE);

    }

    /**
     * The function displays an error message indicating that a specific programming
     * language cannot be
     * run.
     * 
     * @param lang The lang parameter is a string that represents the programming
     *             language that the
     *             user is trying to run.
     */
    public static void cantRunLanguageAlert(String lang) {
        // JOptionPane.showMessageDialog(null, lang + " " + " was not found on your
        // system. Install it to run the file .");
        JOptionPane.showMessageDialog(
                null,
                "Cant run ." + lang + " files yet.",
                "Can't run !",
                JOptionPane.ERROR_MESSAGE);

    }

    /**
     * The function `runJavaFileCommand` executes a Java file using the provided
     * Java compiler command
     * and returns the output of the execution.
     * 
     * @param javaF     The `javaF` parameter is a string that represents the
     *                  command to run the Java file.
     *                  It should include the path to the Java executable and any
     *                  additional arguments or options.
     * @param file_path The `file_path` parameter is the path to the Java file that
     *                  you want to run.
     * @return The method `runJavaFileCommand` returns a `String` which contains the
     *         output of the
     *         executed Java file.
     */
    public static String runJavaFileCommand(String javaF, String file_path) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String out = "";
        try {
            String cd_folder = extractContainingFolder(file_path);
            String file_ok = extractFileName(file_path);

            if (isWindows) {
                String comm = "cmd.exe /c " + "cd " + cd_folder + " && " + javaF + " " + file_ok;
                Process process = Runtime.getRuntime().exec(comm);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    out += line + "\n";
                }
                // Wait for the process to complete
                int exitCode = process.waitFor();
                exit_code = "Exited with error code : " + exitCode;
            } else {
                // "/bin/bash -c cd " + cd_folder + " ; " +
                Process process1 = Runtime.getRuntime().exec("/bin/bash -c cd " + cd_folder);

                Process process = Runtime.getRuntime().exec(javaF + " " + file_ok);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    out += line + "\n";
                }
                // Wait for the process to complete
                int exitCode = process.waitFor();
                exit_code = "Exited with error code : " + exitCode;
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return out;
    }

    /**
     * The function extracts the containing folder of a given file path.
     * 
     * @param filePath The `filePath` parameter is a string that represents the path
     *                 to a file.
     * @return The method is returning the path of the parent directory of the given
     *         file path.
     */
    public static String extractContainingFolder(String filePath) {
        // Create a Path object from the file path
        Path path = Paths.get(filePath);

        // Get the parent directory of the file
        Path parent = path.getParent();

        // Convert the parent directory to a String
        String containingFolder = parent != null ? parent.toString() : "";

        return containingFolder;
    }

    /**
     * The function extracts the file name from a given file path and removes the
     * file extension if
     * present.
     * 
     * @param filePath A string representing the file path.
     * @return The method is returning the file name without the file extension.
     */
    public static String extractFileName(String filePath) {
        // Create a Path object from the file path
        Path path = Paths.get(filePath);

        // Get the file name from the path
        String fileName = path.getFileName().toString();

        // return fileName;
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        } else {
            return fileName; // No extension found
        }
    }

    /**
     * The function `getCppInterpreterPath()` checks if the C++ (g++) interpreter is
     * installed and
     * returns the path to the interpreter if it is.
     * 
     * @return The method is returning the value of the variable "cpp_name".
     */
    public static String getCppInterpreterPath() {
        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec("g++ --version");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Wait for the process to complete
            int exitCode = process.waitFor();
            exit_code = "Exited with error code : " + exitCode;
            cpp_name = "g++";
            System.out.println("installed hai");
        } catch (IOException | InterruptedException e1) {
            showNotInstalledAlert("C++ (g++)");
        }

        return cpp_name;
    }

    /**
     * The getCInterpreterPath() function checks if the C compiler (gcc) is
     * installed and returns the
     * name of the compiler.
     * 
     * @return The method is returning the value of the variable "c_name".
     */
    // The above code is defining a Java method called "runCppFileCommand" that
    // takes two parameters:
    // "cppF" and "file_path". The method returns a String.
    public static String runCppFileCommand(String cppF, String file_path) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String out = "";
        try {
            String cd_folder = extractContainingFolder(file_path);
            String file_ok = extractFileName(file_path);

            if (isWindows) {
                // run with cmd.exe
                Process process1 = Runtime.getRuntime().exec("cmd.exe /c " + "cd " + cd_folder);
                Process process_compile = Runtime.getRuntime().exec(cppF + " " + file_ok + ".cpp" + "-o " + file_ok);
                Process process_run = Runtime.getRuntime().exec("./ " + file_ok);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process_compile.getInputStream()));
                String line1;
                while ((line1 = reader.readLine()) != null) {
                    out += line1 + "\n";
                }
                // Wait for the process to complete
                int exitCode = process_compile.waitFor();

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process_run.getInputStream()));
                String line2;
                while ((line2 = reader.readLine()) != null) {
                    out += line2 + "\n";
                }
                // Wait for the process to complete
                int exitCode2 = process_compile.waitFor();

                exit_code = "Complied with error code : " + exitCode + "\n" + "Exited with error code : " + exitCode2;

            } else {
                // "/bin/bash -c cd " + cd_folder + " ; " +
                Process process1 = Runtime.getRuntime().exec("/bin/bash -c cd " + cd_folder);
                Process process_compile = Runtime.getRuntime().exec(cppF + " " + file_ok + ".cpp " + "-o " + file_ok);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process_compile.getInputStream()));
                String line1;
                String compileLine;
                // while ((compileLine = compileReader.readLine()) != null) {
                // System.out.println(compileLine);
                // }

                while ((line1 = reader.readLine()) != null) {
                    out += line1 + "\n";
                }
                // Wait for the process to complete
                int exitCode = process_compile.waitFor();

                Process process_run = Runtime.getRuntime().exec("/bin/bash -c ./" + file_ok);

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process_run.getInputStream()));
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    out += line2 + "\n";
                }
                // Wait for the process to complete
                int exitCode2 = process_compile.waitFor();

                exit_code = "Complied with error code : " + exitCode + "\n" + "Exited with error code : " + exitCode2;
                System.out.println(out + " " + cpp_name);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return out + "\n" + exit_code;
    }

    /**
     * The getCInterpreterPath() function checks if the C compiler (gcc) is
     * installed and returns the
     * name of the compiler.
     * 
     * @return The method is returning the value of the variable "c_name".
     */
    public static String getCInterpreterPath() {
        try {
            // Execute the command
            Process process = Runtime.getRuntime().exec("gcc --version");

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Wait for the process to complete
            int exitCode = process.waitFor();
            exit_code = "Exited with error code : " + exitCode;
            c_name = "gcc";
            System.out.println("installed hai");
        } catch (IOException | InterruptedException e1) {
            showNotInstalledAlert("C (gcc)");
        }

        return c_name;
    }

    /**
     * The function `runCFileCommand` compiles and runs a C file using the specified
     * compiler command
     * and returns the output and exit code.
     * 
     * @param cF        The parameter `cF` is the command to compile the C file. It
     *                  is used to compile the C
     *                  file into an executable. The `file_path` parameter is the
     *                  path to the C file that needs to be
     *                  compiled and executed.
     * @param file_path The `file_path` parameter is the path to the C file that you
     *                  want to compile
     *                  and run.
     * @return The method is returning a string that contains the output of the
     *         command executed and
     *         the exit code of the process.
     */
    public static String runCFileCommand(String cF, String file_path) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        String out = "";
        try {
            String cd_folder = extractContainingFolder(file_path);
            String file_ok = extractFileName(file_path);

            if (isWindows) {
                // run with cmd.exe
                Process process1 = Runtime.getRuntime().exec("cmd.exe /c " + "cd " + cd_folder);
                Process process_compile = Runtime.getRuntime().exec(cF + " " + file_ok + ".c" + "-o " + file_ok);
                Process process_run = Runtime.getRuntime().exec("./ " + file_ok);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process_compile.getInputStream()));
                String line1;
                while ((line1 = reader.readLine()) != null) {
                    out += line1 + "\n";
                }
                // Wait for the process to complete
                int exitCode = process_compile.waitFor();

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process_run.getInputStream()));
                String line2;
                while ((line2 = reader.readLine()) != null) {
                    out += line2 + "\n";
                }
                // Wait for the process to complete
                int exitCode2 = process_compile.waitFor();

                exit_code = "Complied with error code : " + exitCode + "\n" + "Exited with error code : " + exitCode2;

            } else {
                // "/bin/bash -c cd " + cd_folder + " ; " +
                Process process1 = Runtime.getRuntime().exec("/bin/bash -c cd " + cd_folder);
                Process process_compile = Runtime.getRuntime().exec(cF + " " + file_ok + ".c " + "-o " + file_ok);

                // Read the output of the command
                BufferedReader reader = new BufferedReader(new InputStreamReader(process_compile.getInputStream()));
                String line1;
                String compileLine;
                // while ((compileLine = compileReader.readLine()) != null) {
                // System.out.println(compileLine);
                // }

                while ((line1 = reader.readLine()) != null) {
                    out += line1 + "\n";
                }
                // Wait for the process to complete
                int exitCode = process_compile.waitFor();

                Process process_run = Runtime.getRuntime().exec("/bin/bash -c ./" + file_ok);

                BufferedReader reader2 = new BufferedReader(new InputStreamReader(process_run.getInputStream()));
                String line2;
                while ((line2 = reader2.readLine()) != null) {
                    out += line2 + "\n";
                }
                // Wait for the process to complete
                int exitCode2 = process_compile.waitFor();

                exit_code = "Complied with error code : " + exitCode + "\n" + "Exited with error code : " + exitCode2;
                System.out.println(out + " " + cpp_name);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return out + "\n" + exit_code;
    }

    /**
     * The `highlight` function applies syntax highlighting styles to a `JTextPane`
     * based on the
     * specified programming language.
     * 
     * @param textPane A JTextPane component where the text is displayed.
     * @param doc      The `doc` parameter is a `StyledDocument` object that
     *                 represents the document of the
     *                 `JTextPane` component. It is used to apply the syntax
     *                 highlighting styles to the text in the
     *                 `JTextPane`.
     * @param lang     The `lang` parameter is a string that represents the
     *                 programming language for which
     *                 you want to apply syntax highlighting. It can have the
     *                 following values:
     */
    public static void highlight(JTextPane textPane, StyledDocument doc, String lang) {
        String text = textPane.getText();

        // Define syntax highlighting styles for Java
        Style defaultStyle = textPane.getStyle(StyleContext.DEFAULT_STYLE);
        Style keywordStyle = textPane.addStyle("Keyword", defaultStyle);
        StyleConstants.setForeground(keywordStyle, Key_H);

        Style commentStyle = textPane.addStyle("Comment", defaultStyle);
        StyleConstants.setForeground(commentStyle, Color.GREEN);

        Style stringStyle = textPane.addStyle("String", defaultStyle);
        StyleConstants.setForeground(stringStyle, Color.RED);

        Style dtypeStyle = textPane.addStyle("DType", defaultStyle);
        StyleConstants.setForeground(dtypeStyle, Color.PINK);

        // Apply styles to keywords
        if (lang == ".java") {
            String[] keywords = { "abstract", "boolean", "break", "class", "extends", "for", "if", "new", "return",
                    "while", "public", "private", "static", "void", "int", "double", "import", "true", "false" };
            String[] dtypes = { "byte", "short", "int", "long", "float", "double", "char", "boolean", "String", };

            for (String keyword : keywords) {
                highlightWords(doc, text, keyword, keywordStyle);
            }

            for (String dtype : dtypes) {
                highlightWords(doc, text, dtype, dtypeStyle);
            }

            // Highlight comments
            highlightComments(doc, text, commentStyle);

            // Highlight strings
            highlightStrings(doc, text, stringStyle);
        } else if (lang == ".py") {
            String[] keywords = {
                    "False", "None", "True", "and", "as", "assert", "break", "class", "continue",
                    "def", "del", "elif", "else", "except", "finally", "for", "from", "global",
                    "if", "import", "in", "is", "lambda", "nonlocal", "not", "or", "pass", "raise",
                    "return", "try", "while", "with", "yield", "print"
            };

            String[] dtypes = {
                    "int", "float", "complex", "str", "list", "tuple", "dict", "set", "bool",
                    "bytes", "bytearray", "memoryview"
            };

            for (String keyword : keywords) {
                highlightWords(doc, text, keyword, keywordStyle);
            }

            for (String dtype : dtypes) {
                highlightWords(doc, text, dtype, dtypeStyle);
            }

            // Highlight comments
            highlightComments(doc, text, commentStyle);

            // Highlight strings
            highlightStrings(doc, text, stringStyle);
        } else if (lang == ".cpp") {
            String[] keywords = {
                    "alignas", "alignof", "and", "and_eq", "asm", "auto", "bitand", "bitor",
                    "bool", "break", "case", "catch", "char", "char16_t", "char32_t", "class",
                    "compl", "const", "constexpr", "const_cast", "continue", "decltype", "default",
                    "delete", "do", "double", "dynamic_cast", "else", "enum", "explicit", "export",
                    "extern", "false", "float", "for", "friend", "goto", "if", "inline", "int",
                    "long", "mutable", "namespace", "new", "noexcept", "not", "not_eq", "nullptr",
                    "operator", "or", "or_eq", "private", "protected", "public", "register",
                    "reinterpret_cast", "return", "short", "signed", "sizeof", "static",
                    "static_assert", "static_cast", "struct", "switch", "template", "this", "thread_local",
                    "throw", "true", "try", "typedef", "typeid", "typename", "union", "unsigned",
                    "using", "virtual", "void", "volatile", "wchar_t", "while", "xor", "xor_eq", "#include"
            };

            String[] dtypes = {
                    "int", "float", "double", "char", "bool", "void",
                    "short", "long", "long long", "unsigned int", "unsigned short", "unsigned long",
                    "wchar_t", "char16_t", "char32_t", "string", "wstring", "size_t",
                    "nullptr_t", "auto", "decltype", "void*", "const", "volatile",
                    "enum", "class", "struct", "union", "typedef", "decltype",
                    "std::vector", "std::list", "std::map", "std::set", "std::queue", "std::stack",
                    "std::pair", "std::tuple", "std::array", "std::string", "std::wstring"
            };

            for (String keyword : keywords) {
                highlightWords(doc, text, keyword, keywordStyle);
            }

            for (String dtype : dtypes) {
                highlightWords(doc, text, dtype, dtypeStyle);
            }

            // Highlight comments
            highlightComments(doc, text, commentStyle);

            // Highlight strings
            highlightStrings(doc, text, stringStyle);
        } else if (lang == ".c") {
            String[] keywords = {
                    "auto", "break", "else", "switch",
                    "case", "enum", "register", "typedef",
                    "extern", "return", "union",
                    "const", "unsigned", "continue", "for", "signed", "void",
                    "default", "goto", "sizeof", "volatile",
                    "do", "if", "static", "while", "#include"
            };
            String[] dtypes = {
                    "int", "float", "double", "char", "_Bool",
                    "short", "long", "_Bool", "struct"
            };

            for (String keyword : keywords) {
                highlightWords(doc, text, keyword, keywordStyle);
            }

            for (String dtype : dtypes) {
                highlightWords(doc, text, dtype, dtypeStyle);
            }

            // Highlight comments
            highlightComments(doc, text, commentStyle);

            // Highlight strings
            highlightStrings(doc, text, stringStyle);
        } else if (lang == ".js") {
            String[] keywords = { "break", "do", "in", "typeof",
                    "case", "else", "instanceof", "var",
                    "catch", "export", "new", "void",
                    "class", "extends", "return", "while",
                    "const", "finally", "super", "with",
                    "continue", "for", "switch", "yield",
                    "debugger", "function", "this",
                    "default", "if", "throw",
                    "delete", "import", "try", "let" };
            String[] dtypes = { "true", "false", "null" };

            for (String keyword : keywords) {
                highlightWords(doc, text, keyword, keywordStyle);
            }

            for (String dtype : dtypes) {
                highlightWords(doc, text, dtype, dtypeStyle);
            }

            // Highlight comments
            highlightComments(doc, text, commentStyle);

            // Highlight strings
            highlightStrings(doc, text, stringStyle);
        }
    }

    /**
     * The function `highlightWords` takes a `StyledDocument`, a `String` of text, a
     * `String` word, and
     * a `Style` and highlights all occurrences of the word in the text with the
     * given style.
     * 
     * @param doc   The "doc" parameter is a StyledDocument object, which represents
     *              the document that
     *              you want to apply the highlighting to. It allows you to
     *              manipulate the text and styles of the
     *              document.
     * @param text  The text parameter is the string in which you want to highlight
     *              specific words.
     * @param word  The "word" parameter is a string that represents the word you
     *              want to highlight in
     *              the text.
     * @param style The "style" parameter is of type Style and represents the style
     *              to be applied to
     *              the highlighted words. It can be used to specify attributes such
     *              as font color, font size, font
     *              style, etc.
     */
    private static void highlightWords(StyledDocument doc, String text, String word, Style style) {
        int pos = 0;

        while ((pos = text.indexOf(word, pos)) >= 0) {
            setTextColor(doc, pos, word.length(), style);
            pos += word.length();
        }
    }

    /**
     * The function `highlightWords` takes a `StyledDocument`, a `String` of text, a
     * `String` word, and
     * a `Style` and highlights all occurrences of the word in the text with the
     * given style.
     * 
     * @param doc   The "doc" parameter is a StyledDocument object, which represents
     *              the document that
     *              you want to apply the highlighting to. It allows you to
     *              manipulate the text and styles of the
     *              document.
     * @param text  The text parameter is the string in which you want to highlight
     *              specific words.
     * @param word  The "word" parameter is a string that represents the word you
     *              want to highlight in
     *              the text.
     * @param style The "style" parameter is of type Style and represents the style
     *              to be applied to
     *              the highlighted words. It can be used to specify attributes such
     *              as font color, font size, font
     *              style, etc.
     */
    private static void highlightComments(StyledDocument doc, String text, Style style) {
        int pos = 0;
        String singleLineComment = "//";
        String multiLineCommentStart = "/*";
        String multiLineCommentEnd = "*/";

        while ((pos = text.indexOf(singleLineComment, pos)) >= 0) {
            int end = text.indexOf("\n", pos);
            if (end == -1) {
                end = text.length();
            }
            setTextColor(doc, pos, end - pos, style);
            pos = end;
        }

        pos = 0;
        while ((pos = text.indexOf(multiLineCommentStart, pos)) >= 0) {
            int end = text.indexOf(multiLineCommentEnd, pos);
            if (end == -1) {
                end = text.length();
            }
            setTextColor(doc, pos, end - pos + 2, style);
            pos = end;
        }
    }

    /**
     * The function `highlightComments` takes a `StyledDocument`, a `String`, and a
     * `Style` as input,
     * and highlights single-line and multi-line comments in the text using the
     * specified style.
     * 
     * @param doc   The `doc` parameter is a `StyledDocument` object, which
     *              represents the document that
     *              contains the text to be highlighted.
     * @param text  The `text` parameter is the string that contains the code to be
     *              highlighted.
     * @param style The "style" parameter is the style to be applied to the
     *              highlighted comments. It is
     *              of type "Style" and is used to specify the font, color, and
     *              other formatting properties for the
     *              comments.
     */
    // The above code is defining a method called "highlightStrings" that takes in a
    // StyledDocument, a
    // String, and a Style as parameters. This method is used to highlight specific
    // strings within the
    // StyledDocument using the given Style.
    private static void highlightStrings(StyledDocument doc, String text, Style style) {
        int pos = 0;

        while ((pos = text.indexOf("\"", pos)) >= 0) {
            int end = text.indexOf("\"", pos + 1);
            if (end == -1) {
                end = text.length();
            }
            setTextColor(doc, pos, end - pos + 1, style);
            pos = end + 1;
        }
    }

    /**
     * The function sets the text color of a specified range in a styled document.
     * 
     * @param doc    The "doc" parameter is a StyledDocument object, which
     *               represents a document that
     *               supports styled text. It can be used to apply different styles
     *               to different parts of the text.
     * @param start  The start parameter is an integer that represents the starting
     *               position of the text
     *               in the document where you want to apply the text color. It is
     *               the index of the first character
     *               in the text range.
     * @param length The "length" parameter in the "setTextColor" method represents
     *               the number of
     *               characters in the document that you want to apply the specified
     *               text color to.
     * @param style  The "style" parameter is an instance of the Style class, which
     *               represents the text
     *               style to be applied to the specified range of text. It can
     *               include attributes such as font
     *               family, font size, font color, and more.
     */
    public static void setTextColor(StyledDocument doc, int start, int length, Style style) {
        doc.setCharacterAttributes(start, length, style, false);
    }

    /**
     * The function reads a file, inserts its contents into a JTextPane, and then
     * highlights the syntax
     * based on the file extension.
     * 
     * @param textPane The textPane parameter is a JTextPane object, which is a
     *                 component that can
     *                 display styled text. It is used to display the contents of
     *                 the file being read and highlighted.
     * @param filePath The `filePath` parameter is the path to the file that you
     *                 want to read and
     *                 highlight in the `JTextPane`. It should be a string
     *                 representing the file's location on your
     *                 computer.
     * @param ext      The "ext" parameter is a string that represents the file
     *                 extension of the file being
     *                 read. It is used to determine which syntax highlighting rules
     *                 to apply to the text in the
     *                 JTextPane.
     */
    private static void readFileAndHighlight(JTextPane textPane, String filePath, String ext)

            throws IOException, BadLocationException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StyledDocument doc = textPane.getStyledDocument();

        String line;
        while ((line = reader.readLine()) != null) {
            // doc.insertString(doc.getLength(), line + "\n", null);
        }

        reader.close();
        System.out.println("File loaded successfully.");
        if (ext == ".java") {
            highlight(textPane, doc, ".java");
        } else if (ext == ".py") {
            highlight(textPane, doc, ".py");
        } else if (ext == ".cpp") {
            highlight(textPane, doc, ".cpp");
        } else if (ext == ".c") {
            highlight(textPane, doc, ".c");
        } else if (ext == ".js") {
            highlight(textPane, doc, ".js");
        }
    }

    /**
     * The getText() function returns a string containing line numbers up to the
     * current caret position
     * in a JTextArea.
     * 
     * @return The method is returning a string that contains line numbers starting
     *         from 1 up to the
     *         line number of the current caret position in a JTextArea. Each line
     *         number is followed by a line
     *         separator.
     */
    public String getText() {
        int caretPosition = area.getDocument().getLength();
        Element root = area.getDocument().getDefaultRootElement();
        String text = "1" + System.getProperty("line.separator");
        for (int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
            text += i + System.getProperty("line.separator");
        }
        return text;
    }

    /**
     * The function sets the text color of a specified range in a styled document.
     * 
     * @param doc    The "doc" parameter is a StyledDocument object, which
     *               represents a document that
     *               supports styled text. It can be used to apply different styles
     *               to different parts of the text.
     * @param start  The start parameter is an integer that represents the starting
     *               position of the text
     *               in the document where you want to apply the text color. It is
     *               the index of the first character
     *               in the text range.
     * @param length The "length" parameter in the "setTextColor" method represents
     *               the number of
     *               characters in the document that you want to apply the specified
     *               text color to.
     * @param style  The "style" parameter is an instance of the Style class, which
     *               represents the text
     *               style to be applied to the specified range of text. It can
     *               include attributes such as font
     *               family, font size, font color, and more.
     */
}
