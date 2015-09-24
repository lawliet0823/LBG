import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

/**
 * Created by L on 2015/1/15.
 */


public class GUI extends JFrame {

    private int codeBookSize = 128;
    private int dimension = 2;
    private double epsilon = 0.08;
    private int decisionSearch = 1;
    private int output;
    private String path = "";

    public GUI() {
        super("Vector Quantification");
        this.setSize(800, 500);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        final Container content = getContentPane();
        content.setLayout(null);

//        final JPanel imagePanel = new JPanel();
//        imagePanel.setBounds(72, 100, 256, 256);
//        //imagePanel.setBackground(Color.green);
//        content.add(imagePanel);

        // codebook size setting
        JLabel sizeLabel = new JLabel("1.Please choose the codebook size: ");
        sizeLabel.setBounds(410, 10, 280, 20);
        sizeLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(sizeLabel);

        ButtonGroup sizeGroup = new ButtonGroup();
        final JCheckBox checkbox128 = new JCheckBox("128");
        checkbox128.setBounds(420, 40, 50, 20);
        checkbox128.setSelected(true);
        checkbox128.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                codeBookSize = 128;
            }
        });
        content.add(checkbox128);

        final JCheckBox checkbox256 = new JCheckBox("256");
        checkbox256.setBounds(490, 40, 50, 20);
        checkbox256.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                codeBookSize = 256;
            }
        });
        content.add(checkbox256);

        sizeGroup.add(checkbox128);
        sizeGroup.add(checkbox256);

        // dimension setting
        JLabel dimLabel = new JLabel("2.Please choose the dimension: ");
        dimLabel.setBounds(410, 70, 280, 20);
        dimLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(dimLabel);

        ButtonGroup dimGroup = new ButtonGroup();

        JCheckBox checkBox2 = new JCheckBox("2x2");
        checkBox2.setBounds(420, 100, 50, 20);
        checkBox2.setSelected(true);
        checkBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dimension = 2;
            }
        });
        content.add(checkBox2);

        JCheckBox checkBox4 = new JCheckBox("4x4");
        checkBox4.setBounds(490, 100, 50, 20);
        checkBox4.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dimension = 4;
            }
        });
        content.add(checkBox4);

        JCheckBox checkBox8 = new JCheckBox("8x8");
        checkBox8.setBounds(560, 100, 50, 20);
        checkBox8.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                dimension = 8;
            }
        });
        content.add(checkBox8);

        dimGroup.add(checkBox2);
        dimGroup.add(checkBox4);
        dimGroup.add(checkBox8);

        // epsilon setting
        JLabel epsilonLabel = new JLabel("3.Please specifyεfor GLA: (e.g. 0.1) ");
        epsilonLabel.setBounds(410, 130, 280, 20);
        epsilonLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(epsilonLabel);

        JTextField epsilonText = new JTextField("0.01");
        epsilonText.setBounds(420, 160, 100, 20);
        content.add(epsilonText);

        // search setting
        JLabel searchLabel = new JLabel("4.Please choose the search method: ");
        searchLabel.setBounds(410, 190, 280, 20);
        searchLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(searchLabel);

        ButtonGroup searchGroup = new ButtonGroup();

        JCheckBox treeCheckBox = new JCheckBox("Tree Search");
        treeCheckBox.setBounds(420, 220, 100, 20);
        treeCheckBox.setSelected(true);
        treeCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                decisionSearch = 1;
            }
        });
        content.add(treeCheckBox);

        JCheckBox fullCheckBox = new JCheckBox("Full Search");
        fullCheckBox.setBounds(540, 220, 100, 20);
        fullCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                decisionSearch = 2;
            }
        });
        content.add(fullCheckBox);

        JCheckBox mCheckBox = new JCheckBox("M Search");
        mCheckBox.setBounds(660, 220, 100, 20);
        mCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                decisionSearch = 3;
            }
        });
        content.add(mCheckBox);

        searchGroup.add(treeCheckBox);
        searchGroup.add(fullCheckBox);
        searchGroup.add(mCheckBox);

        // file setting
        JLabel fileLabel = new JLabel("5.Please choose the file: ");
        fileLabel.setBounds(410, 250, 280, 20);
        fileLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(fileLabel);

        JButton selectButton = new JButton("Select");
        selectButton.setBounds(420, 280, 80, 20);
        content.add(selectButton);

        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("." + File.separator + "src" + File.separator + "target");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setDialogTitle("Choose Data Path");
                if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(null)) {
                    path = fileChooser.getSelectedFile().getAbsolutePath();
                    System.out.print(path);
                }
            }
        });


        // output setting
        JLabel jpgLabel = new JLabel("6.Do you want to get jpg as your output?");
        jpgLabel.setBounds(410, 310, 300, 20);
        jpgLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(jpgLabel);

        ButtonGroup jpgGroup = new ButtonGroup();

        JCheckBox yesCheckBox = new JCheckBox("Yes");
        yesCheckBox.setBounds(420, 340, 100, 20);
        yesCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                output = 1;
            }
        });
        content.add(yesCheckBox);

        JCheckBox noCheckBox = new JCheckBox("No");
        noCheckBox.setBounds(530, 340, 100, 20);
        noCheckBox.setSelected(true);
        noCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                output = 0;
            }
        });
        content.add(noCheckBox);
        jpgGroup.add(yesCheckBox);
        jpgGroup.add(noCheckBox);

        // decision setting
        JLabel decisionLabel = new JLabel("7.Please choose: ");
        decisionLabel.setBounds(410, 370, 280, 20);
        decisionLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        content.add(decisionLabel);

        JButton encodeButton = new JButton("Encode");
        encodeButton.setBounds(420, 410, 80, 20);
        content.add(encodeButton);

        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (path.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please select image!!!", "Alert", JOptionPane.ERROR_MESSAGE);
                } else {
                    Execution execution = new Execution(codeBookSize, dimension, epsilon, decisionSearch, output, path);
                    try {
                        execution.encode();
                    } catch (Exception e1) {
                        System.out.println("Encode Error");
                    }
                }
            }
        });

        JButton decodeButton = new JButton("Decode");
        decodeButton.setBounds(520, 410, 80, 20);
        content.add(decodeButton);

        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Execution execution = new Execution(codeBookSize, dimension, epsilon, decisionSearch, output, path);
                try {
                    execution.decode();
                } catch (Exception e1) {
                    System.out.println("Decode Error");
                }
            }
        });
        this.setVisible(true);
    }


}

