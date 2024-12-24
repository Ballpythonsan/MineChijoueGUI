package ballpythonsan.minechijouegui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class MineChijoueGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MineChijoueGUI frame = new MineChijoueGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public MineChijoueGUI() {
		setResizable(false);
		setTitle("地上絵作成支援GUI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 893, 467);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 10, 384, 408);
		contentPane.add(tabbedPane);
		
		// 基本設定タブ
		JPanel setting = new JPanel();
		setting.setBackground(Color.GRAY);
		setting.setBounds(31, 29, 365, 374);
		// contentPane.add(setting);
		tabbedPane.addTab("メイン設定", null, setting, "地上絵にしたい画像の選択、ログ、作成実行");
		
		JTextPane textPane_2 = new JTextPane();
		textPane_2.setText("地上絵にしたい画像を選択してください");
		textPane_2.setForeground(Color.WHITE);
		textPane_2.setBackground(Color.DARK_GRAY);
		
		// ファイルパス入力フィールド
		JTextField inputImagePath = new JTextField();
		inputImagePath.setColumns(10);

		// ↑のフィールドに入力される
		JButton FileChooser = new JButton("ファイルの選択");
		FileChooser.setVerticalAlignment(SwingConstants.BOTTOM);
		FileChooser.setBackground(Color.WHITE);
		FileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser("./");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("画像ファイル", "png", "jpg", "jpeg", "bmp", "tiff");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				while (true) {
					int returnVal = fileChooser.showOpenDialog(FileChooser);
					if (returnVal != JFileChooser.APPROVE_OPTION) {
						return;
					}
					File file = fileChooser.getSelectedFile();
					inputImagePath.setText(file.getPath());
					return;
					// if (file.getName().endsWith("png") || file.getName().endsWith("jpeg") || file.getName().endsWith("jpg") || file.getName().endsWith("bmp")) {

					// }
				}
			}
		});
		// ファイル選択ウィンドウの見た目を変えるやつ
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		
		// ログの出力画面
		JScrollPane scrollPane = new JScrollPane();
		JTextArea consoleLog = new JTextArea();
		consoleLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
		scrollPane.setViewportView(consoleLog);
		consoleLog.setEditable(false);
		
		// 実行ボタン(内部でchijoue.jarを叩いている)
		JButton execute = new JButton("実行");
		execute.setToolTipText("この設定で地上絵を作る");
		execute.setBackground(Color.WHITE);
		execute.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String inputPath = inputImagePath.getText();
					if (inputPath.endsWith("png") ||
							inputPath.endsWith("jpg") ||
							inputPath.endsWith("jpeg") ||
							inputPath.endsWith("bmp") ||
							inputPath.endsWith("tiff")){
						ProcessBuilder pb = new ProcessBuilder("java", "-jar", "./chijoue.jar", inputPath);
						pb.redirectErrorStream(true);
						Process process = pb.start();
						
						// 標準出力の読み取り
			            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			            String line;
			            while ((line = reader.readLine()) != null) {
			            	consoleLog.append(line + "\n");
			            }
			            
			            // エラー出力の読み取り（必要に応じて）
			            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			            String errorLine;
			            while ((errorLine = errorReader.readLine()) != null) {
			            	consoleLog.append("ERROR: " + errorLine + "\n");
			            }

			            consoleLog.append("wait process...\n");
						int exitCode = process.waitFor();
						consoleLog.append("Process finished with exit code: " + exitCode + "\n\n");
					}
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
				return;
			}
		});
		
		GroupLayout gl_setting = new GroupLayout(setting);
		gl_setting.setHorizontalGroup(
			gl_setting.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_setting.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_setting.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_setting.createSequentialGroup()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_setting.createSequentialGroup()
							.addComponent(textPane_2, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
							.addGap(100))
						.addGroup(Alignment.TRAILING, gl_setting.createSequentialGroup()
							.addComponent(inputImagePath, GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(FileChooser, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_setting.createSequentialGroup()
							.addComponent(execute, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
							.addContainerGap())))
		);
		gl_setting.setVerticalGroup(
			gl_setting.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_setting.createSequentialGroup()
					.addContainerGap()
					.addComponent(textPane_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addGroup(gl_setting.createParallelGroup(Alignment.BASELINE)
						.addComponent(inputImagePath, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
						.addComponent(FileChooser, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(execute, GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
					.addGap(11))
		);
		setting.setLayout(gl_setting);
		
		// 代替ブロック設定タブ
		JPanel substitute = new JPanel();
		substitute.setBackground(Color.GRAY);
		tabbedPane.addTab("代替設定", null, substitute, "代替ブロックの設定");
		
		JTextPane textPane = new JTextPane();
		textPane.setText("代替ブロックの設定");
		textPane.setForeground(Color.WHITE);
		textPane.setBackground(Color.DARK_GRAY);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		DefaultTableModel tableModel = new DefaultTableModel(new Object[][] {
            { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" },
            { "", "" }, { "", "" }, { "", "" }, { "", "" }, { "", "" }
        }, new Object[] { "Column 1", "Column 2" });
		
		JTable substituteTable = new JTable(tableModel);
		scrollPane_1.setViewportView(substituteTable);
		
		tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int lastRow = tableModel.getRowCount() - 1;
                    boolean isLastRowFilled = true;

                    // 最後の行がすべて埋まっているか確認
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        if (tableModel.getValueAt(lastRow, col) == null ||
                            tableModel.getValueAt(lastRow, col).toString().trim().isEmpty()) {
                            isLastRowFilled = false;
                            break;
                        }
                    }

                    // 最後の行が埋まっていたら新しい行を追加
                    if (isLastRowFilled) {
                        tableModel.addRow(new Object[] { "", "" });
                    }
                }
            }
        });
		
		GroupLayout gl_substitute = new GroupLayout(substitute);
		gl_substitute.setHorizontalGroup(
			gl_substitute.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_substitute.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_substitute.createParallelGroup(Alignment.LEADING)
						.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 223, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 355, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_substitute.setVerticalGroup(
			gl_substitute.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_substitute.createSequentialGroup()
					.addContainerGap()
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		substitute.setLayout(gl_substitute);
		
	}
}
