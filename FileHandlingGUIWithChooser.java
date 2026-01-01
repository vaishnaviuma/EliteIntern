import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class FileHandlingGUIWithChooser extends JFrame implements ActionListener {

    JTextArea textArea;
    JButton openBtn, writeBtn, readBtn, modifyBtn, clearBtn;
    File selectedFile;

    FileHandlingGUIWithChooser() {

        setTitle("File Handling Utility");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Text area
        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Buttons
        openBtn = new JButton("Choose File");
        writeBtn = new JButton("Write File");
        readBtn = new JButton("Read File");
        modifyBtn = new JButton("Modify File");
        clearBtn = new JButton("Clear");

        JPanel panel = new JPanel();
        panel.add(openBtn);
        panel.add(writeBtn);
        panel.add(readBtn);
        panel.add(modifyBtn);
        panel.add(clearBtn);

        openBtn.addActionListener(this);
        writeBtn.addActionListener(this);
        readBtn.addActionListener(this);
        modifyBtn.addActionListener(this);
        clearBtn.addActionListener(this);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == openBtn) {
            chooseFile();
        } else if (e.getSource() == writeBtn) {
            writeFile();
        } else if (e.getSource() == readBtn) {
            readFile();
        } else if (e.getSource() == modifyBtn) {
            modifyFile();
        } else if (e.getSource() == clearBtn) {
            textArea.setText("");
        }
    }

    // Choose file using file chooser
    void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            setTitle("File Handling Utility - " + selectedFile.getName());
            JOptionPane.showMessageDialog(this, "File Selected Successfully");
        }
    }

    // Write (overwrite) file
    void writeFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please choose a file first!");
            return;
        }

        try (FileWriter fw = new FileWriter(selectedFile)) {
            fw.write(textArea.getText());
            JOptionPane.showMessageDialog(this, "File written successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error writing file!");
        }
    }

    // Read file
    void readFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please choose a file first!");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            textArea.setText("");
            String line;
            while ((line = br.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file!");
        }
    }

    // Modify (append) file
    void modifyFile() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please choose a file first!");
            return;
        }

        try (FileWriter fw = new FileWriter(selectedFile, true)) {
            fw.write("\n" + textArea.getText());
            JOptionPane.showMessageDialog(this, "File modified successfully!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error modifying file!");
        }
    }

    public static void main(String[] args) {
        new FileHandlingGUIWithChooser();
    }
}
