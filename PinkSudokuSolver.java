import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PinkSudokuSolver extends JFrame {
    private JTextField[][] cells = new JTextField[9][9];
    private JButton solveButton = new JButton("Solve Puzzle");
    private JButton clearButton = new JButton("Clear Board");
    private JPanel sudokuPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    
    // Pink theme colors
    private Color backgroundColor = new Color(255, 240, 245); // Light pink
    private Color gridColor = new Color(219, 112, 147);       // Medium pink
    private Color solvedTextColor = new Color(199, 21, 133);  // Dark pink
    private Color buttonColor = new Color(255, 182, 193);     // Light pink
    private Color buttonTextColor = new Color(199, 21, 133);  // Dark pink
    private Font cellFont = new Font("Comic Sans MS", Font.PLAIN, 20);
    private Font buttonFont = new Font("Comic Sans MS", Font.BOLD, 16);
    
    public PinkSudokuSolver() {
        setTitle("✨ Pink Sudoku Solver ✨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        
        sudokuPanel.setLayout(new GridLayout(9, 9, 1, 1));
        sudokuPanel.setBackground(gridColor);
        sudokuPanel.setBorder(BorderFactory.createLineBorder(gridColor, 3));
        
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = new JTextField();
                cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                cells[row][col].setFont(cellFont);
                
                // Add thicker borders to separate 3x3 boxes
                javax.swing.border.Border border = BorderFactory.createLineBorder(gridColor, 1);
                if (row % 3 == 0 && col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 3, 1, 1, gridColor);
                } else if (row % 3 == 0) {
                    border = BorderFactory.createMatteBorder(3, 1, 1, 1, gridColor);
                } else if (col % 3 == 0) {
                    border = BorderFactory.createMatteBorder(1, 3, 1, 1, gridColor);
                }
                
                cells[row][col].setBorder(border);
                cells[row][col].setBackground(Color.WHITE);
                
                sudokuPanel.add(cells[row][col]);
            }
        }
        
        
        solveButton.setFont(buttonFont);
        solveButton.setBackground(buttonColor);
        solveButton.setForeground(buttonTextColor);
        solveButton.setFocusPainted(false);
        
        clearButton.setFont(buttonFont);
        clearButton.setBackground(buttonColor);
        clearButton.setForeground(buttonTextColor);
        clearButton.setFocusPainted(false);
        
        
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearBoard();
            }
        });
        
        
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(solveButton);
        buttonPanel.add(clearButton);
        
        
        JLabel titleLabel = new JLabel("💖 Pink Sudoku Solver 💖", JLabel.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
        titleLabel.setForeground(new Color(219, 112, 147));
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(sudokuPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        
        add(mainPanel);
        
        // Center on screen
        setLocationRelativeTo(null);
    }
    
    private void solveSudoku() {
        int[][] board = new int[9][9];
        
        // Read values from fields
        try {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String value = cells[row][col].getText().trim();
                    if (value.isEmpty()) {
                        board[row][col] = 0;
                    } else {
                        board[row][col] = Integer.parseInt(value);
                        // Validate input (1-9)
                        if (board[row][col] < 1 || board[row][col] > 9) {
                            JOptionPane.showMessageDialog(this, 
                                "Please enter numbers between 1 and 9 only!", 
                                "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter numbers only!", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Attempt to solve the puzzle
        if (solve(board)) {
            
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    cells[row][col].setText(String.valueOf(board[row][col]));
                    cells[row][col].setForeground(solvedTextColor);
                    cells[row][col].setFont(cellFont.deriveFont(Font.BOLD));
                }
            }
            
            // Add a cute success message
            JOptionPane.showMessageDialog(this, 
                "✨ Puzzle solved successfully! ✨", 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "This puzzle cannot be solved. Please check your input.", 
                "Unsolvable Puzzle", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private boolean solve(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Find an empty cell
                if (board[row][col] == 0) {
                    // Try placing digits 1-9
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            
                            board[row][col] = num;
                            
                            
                            if (solve(board)) {
                                return true;
                            }
                            
                            
                            board[row][col] = 0;
                        }
                    }
                    
                    return false;
                }
            }
        }
       
        return true;
    }
    
    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        
        // Check column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        
        // Check 3x3 box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private void clearBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col].setText("");
                cells[row][col].setForeground(Color.BLACK);
                cells[row][col].setFont(cellFont.deriveFont(Font.PLAIN));
            }
        }
    }
    
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Start the application
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PinkSudokuSolver().setVisible(true);
            }
        });
    }
}
