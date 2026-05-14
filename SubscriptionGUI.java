import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * SubscriptionGUI class manages the graphical user interface for the AIModel system.
 * It allows users to add, update and manage AI plans.
 *
 *
 * @author Anamika Limbu
 * ID: NP05CP4A250018
 */
public class SubscriptionGUI {

    private JFrame mainFrame;
    private final ArrayList<AIModel> plans = new ArrayList<>();

    // Personal Plan fields
    private JTextField ppModelName, ppPrice, ppParams, ppContext, ppPrompts;
    private JTextField ppOpId, ppPromptText, ppOutputLines;

    // Pro Plan fields 
    private JTextField prModelName, prPrice, prParams, prContext, prSlots;
    private JTextField prOpId, prTeamMemberName, prPromptText, prOutputLines;

    // Output console 
    private JTextArea taOut;

    // Palette 
    private static final Color C_WHITE        = Color.WHITE;
    private static final Color C_OFF_WHITE    = new Color(248, 248, 248);
    private static final Color C_SURFACE      = new Color(242, 242, 242);
    private static final Color C_BORDER       = new Color(210, 210, 210);
    private static final Color C_BORDER_DARK  = new Color(170, 170, 170);
    private static final Color C_TEXT         = new Color(30,  30,  30);
    private static final Color C_TEXT_MUTED   = new Color(100, 100, 100);
    private static final Color C_ACCENT       = new Color(220, 235, 255);
    private static final Color C_ACCENT_HVR   = new Color(200, 220, 250);
    private static final Color C_BTN_HOVER    = new Color(235, 235, 235);
    private static final Color C_PLACEHOLDER  = new Color(180, 180, 180);

    //Fonts
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  16);
    private static final Font F_HDR   = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font F_SEC   = new Font("Segoe UI", Font.PLAIN, 10);
    private static final Font F_LBL   = new Font("Poppins",  Font.BOLD,  12);
    private static final Font F_IN    = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font F_BTN   = new Font("Rubik",    Font.BOLD,  13);
    private static final Font F_OUT   = new Font("Consolas", Font.PLAIN, 12);

    public SubscriptionGUI() {
        mainFrame = new JFrame("AI Subscription Management System");
        mainFrame.setSize(1200, 760);
        mainFrame.setLayout(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(C_WHITE);

        buildTitleBar();
        buildPersonalColumn();
        buildOutputColumn();
        buildProColumn();

        mainFrame.setVisible(true);
    }

    private void buildTitleBar() {
        JLabel title = new JLabel("AI Subscription Management System");
        title.setBounds(20, 8, 700, 28);
        title.setFont(F_TITLE);
        title.setForeground(C_TEXT);
        mainFrame.add(title);

        JLabel sub = new JLabel("Personal Plan  ·  Pro Plan");
        sub.setBounds(20, 34, 400, 18);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(C_TEXT_MUTED);
        mainFrame.add(sub);

        JSeparator sep = new JSeparator();
        sep.setBounds(0, 56, 1200, 2);
        sep.setForeground(C_BORDER);
        mainFrame.add(sep);
    }

    private void buildPersonalColumn() {
        int x = 0, colW = 370, y = 58;

        // Column header
        JLabel hdr = colHeader("Personal Plan");
        hdr.setBounds(x, y, colW, 36);
        mainFrame.add(hdr);
        y += 38;

        // Plan details section 
        y = sectionLabel("Plan details", x + 10, y, colW - 20) + 6;

        ppModelName = labeledField(mainFrame, "Model name",        "e.g. GPT",     x + 10, y, colW - 20); y += 32;
        ppPrice     = labeledField(mainFrame, "Price (NRs/1L tk)", "e.g. 250.00",  x + 10, y, colW - 20); y += 32;
        ppParams    = labeledField(mainFrame, "Parameters (B)",    "e.g. 175",     x + 10, y, colW - 20); y += 32;
        ppContext   = labeledField(mainFrame, "Context window",    "e.g. 128000",  x + 10, y, colW - 20); y += 32;
        ppPrompts   = labeledField(mainFrame, "Initial prompts",   "e.g. 100",     x + 10, y, colW - 20); y += 36;

        JButton btnCreate = accentBtn("Create personal plan");
        btnCreate.setBounds(x + 10, y, colW - 20, 30);
        btnCreate.addActionListener(e -> createPersonalPlan());
        mainFrame.add(btnCreate);
        y += 40;

        //  Operations section
        y = sectionLabel("Operations", x + 10, y, colW - 20) + 6;

        ppOpId = labeledField(mainFrame, "Plan index", "0-based index", x + 10, y, colW - 20); y += 32;

        int half = (colW - 25) / 2;
        JButton btnDisplay = plainBtn("Display info");
        btnDisplay.setBounds(x + 10, y, half, 28);
        btnDisplay.addActionListener(e -> displayInfo(ppOpId, "Personal"));
        mainFrame.add(btnDisplay);

        JButton btnBuy = plainBtn("Buy prompts");
        btnBuy.setBounds(x + 10 + half + 5, y, half, 28);
        btnBuy.addActionListener(e -> buyPrompts(ppOpId));
        mainFrame.add(btnBuy); y += 34;

        ppPromptText  = labeledField(mainFrame, "Prompt text",  "Enter your prompt",   x + 10, y, colW - 20); y += 32;
        ppOutputLines = labeledField(mainFrame, "Output lines", "n/a for Personal",    x + 10, y, colW - 20); y += 36;

        JButton btnPrompt = plainBtn("Enter prompt");
        btnPrompt.setBounds(x + 10, y, colW - 20, 28);
        btnPrompt.addActionListener(e -> enterPrompt(ppOpId, ppPromptText, ppOutputLines, "Personal"));
        mainFrame.add(btnPrompt); y += 34;

        JButton btnType = plainBtn("Check plan type");
        btnType.setBounds(x + 10, y, colW - 20, 28);
        btnType.addActionListener(e -> checkPlanType(ppOpId));
        mainFrame.add(btnType); y += 40;

        // Queries section 
        y = sectionLabel("Queries", x + 10, y, colW - 20) + 6;

        JButton btnAll = plainBtn("Display all plans");
        btnAll.setBounds(x + 10, y, colW - 20, 28);
        btnAll.addActionListener(e -> displayAll());
        mainFrame.add(btnAll); y += 34;

        JButton btnClear = plainBtn("Clear fields");
        btnClear.setBounds(x + 10, y, colW - 20, 28);
        btnClear.addActionListener(e -> clearPersonal());
        mainFrame.add(btnClear); y += 40;

        // File handling section
        y = sectionLabel("File Handling", x + 10, y, colW - 20) + 6;

        JButton btnSave = plainBtn("Save data");
        btnSave.setBounds(x + 10, y, (colW - 25) / 2, 28);
        btnSave.addActionListener(e -> saveData());
        mainFrame.add(btnSave);

        JButton btnLoad = plainBtn("Load data");
        btnLoad.setBounds(x + 10 + (colW - 25) / 2 + 5, y, (colW - 25) / 2, 28);
        btnLoad.addActionListener(e -> loadData());
        mainFrame.add(btnLoad);

        // Vertical separator
        JSeparator vs = new JSeparator(JSeparator.VERTICAL);
        vs.setBounds(colW, 58, 1, 700);
        vs.setForeground(C_BORDER);
        mainFrame.add(vs);
    }

    private void buildOutputColumn() {
        int x = 371, colW = 458, y = 58;

        JLabel hdr = colHeader("Output console");
        hdr.setBounds(x, y, colW, 36);
        mainFrame.add(hdr);
        y += 38;

        int btnH = 36;
        int spH  = 760 - y - btnH - 28;

        taOut = new JTextArea("Welcome!\n");
        taOut.setEditable(false);
        taOut.setBackground(C_WHITE);
        taOut.setForeground(C_TEXT);
        taOut.setFont(F_OUT);
        taOut.setLineWrap(true);
        taOut.setWrapStyleWord(true);
        taOut.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane sp = new JScrollPane(taOut);
        sp.setBounds(x, y, colW, spH);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER, 1));
        mainFrame.add(sp);

        JButton btnClr = new JButton("Clear output");
        btnClr.setFont(F_BTN);
        btnClr.setForeground(C_TEXT_MUTED);
        btnClr.setBackground(C_SURFACE);
        btnClr.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, C_BORDER));
        btnClr.setFocusPainted(false);
        btnClr.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClr.setBounds(x, y + spH, colW, btnH);
        btnClr.addActionListener(e -> taOut.setText(""));
        btnClr.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnClr.setBackground(C_BTN_HOVER); btnClr.setForeground(C_TEXT); }
            public void mouseExited (MouseEvent e) { btnClr.setBackground(C_SURFACE);   btnClr.setForeground(C_TEXT_MUTED); }
        });
        mainFrame.add(btnClr);

        // Vertical separator
        JSeparator vs = new JSeparator(JSeparator.VERTICAL);
        vs.setBounds(x + colW, 58, 1, 700);
        vs.setForeground(C_BORDER);
        mainFrame.add(vs);
    }

    private void buildProColumn() {
        int x = 830, colW = 360, y = 58;

        JLabel hdr = colHeader("Pro Plan");
        hdr.setBounds(x, y, colW, 36);
        mainFrame.add(hdr);
        y += 38;

        //Plan details section
        y = sectionLabel("Plan details", x + 10, y, colW - 20) + 6;

        prModelName = labeledField(mainFrame, "Model name",        "e.g. GPT Pro",  x + 10, y, colW - 20); y += 32;
        prPrice     = labeledField(mainFrame, "Price (NRs/1L tk)", "e.g. 800.00",   x + 10, y, colW - 20); y += 32;
        prParams    = labeledField(mainFrame, "Parameters (B)",    "e.g. 200",      x + 10, y, colW - 20); y += 32;
        prContext   = labeledField(mainFrame, "Context window",    "e.g. 200000",   x + 10, y, colW - 20); y += 32;
        prSlots     = labeledField(mainFrame, "Initial slots",     "e.g. 10",       x + 10, y, colW - 20); y += 36;

        JButton btnCreate = accentBtn("Create Pro plan");
        btnCreate.setBounds(x + 10, y, colW - 20, 30);
        btnCreate.addActionListener(e -> createProPlan());
        mainFrame.add(btnCreate);
        y += 40;

        // Operations section 
        y = sectionLabel("Operations", x + 10, y, colW - 20) + 6;

        prOpId = labeledField(mainFrame, "Plan index", "0-based index", x + 10, y, colW - 20); y += 32;

        JButton btnDisplay = plainBtn("Display info");
        btnDisplay.setBounds(x + 10, y, colW - 20, 28);
        btnDisplay.addActionListener(e -> displayInfo(prOpId, "Pro"));
        mainFrame.add(btnDisplay); y += 34;

        prTeamMemberName = labeledField(mainFrame, "Team member", "e.g. Anu", x + 10, y, colW - 20); y += 32;

        JButton btnAdd = plainBtn("Add team member");
        btnAdd.setBounds(x + 10, y, (colW - 25) / 2, 28);
        btnAdd.addActionListener(e -> addTeamMember(prOpId));
        mainFrame.add(btnAdd);

        JButton btnRem = plainBtn("Remove team member");
        btnRem.setBounds(x + 10 + (colW - 25) / 2 + 5, y, (colW - 25) / 2, 28);
        btnRem.addActionListener(e -> removeTeamMember(prOpId));
        mainFrame.add(btnRem); y += 34;

        prPromptText  = labeledField(mainFrame, "Prompt text",  "Enter your prompt", x + 10, y, colW - 20); y += 32;
        prOutputLines = labeledField(mainFrame, "Output lines", "e.g. 10",           x + 10, y, colW - 20); y += 36;

        int halfP = (colW - 25) / 2;
        JButton btnPrompt = plainBtn("Enter prompt");
        btnPrompt.setBounds(x + 10, y, halfP, 28);
        btnPrompt.addActionListener(e -> enterPrompt(prOpId, prPromptText, prOutputLines, "Pro"));
        mainFrame.add(btnPrompt);

        JButton btnType = plainBtn("Check plan type");
        btnType.setBounds(x + 10 + halfP + 5, y, halfP, 28);
        btnType.addActionListener(e -> checkPlanType(prOpId));
        mainFrame.add(btnType); y += 40;

        // Queries section 
        y = sectionLabel("Queries", x + 10, y, colW - 20) + 6;

        // "Display all plans" and "Clear fields"
        int halfQ = (colW - 25) / 2;
        JButton btnAll = plainBtn("Display all plans");
        btnAll.setBounds(x + 10, y, halfQ, 28);
        btnAll.addActionListener(e -> displayAll());
        mainFrame.add(btnAll);

        JButton btnClear = plainBtn("Clear fields");
        btnClear.setBounds(x + 10 + halfQ + 5, y, halfQ, 28);
        btnClear.addActionListener(e -> clearPro());
        mainFrame.add(btnClear); y += 40;

        // File handling section
        y = sectionLabel("File Handling", x + 10, y, colW - 20) + 6;

        JButton btnSave = plainBtn("Save data");
        btnSave.setBounds(x + 10, y, (colW - 25) / 2, 28);
        btnSave.addActionListener(e -> saveData());
        mainFrame.add(btnSave);

        JButton btnLoad = plainBtn("Load data");
        btnLoad.setBounds(x + 10 + (colW - 25) / 2 + 5, y, (colW - 25) / 2, 28);
        btnLoad.addActionListener(e -> loadData());
        mainFrame.add(btnLoad);
    }

    /** Coloured header label for each column */
    private JLabel colHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_HDR);
        l.setForeground(C_TEXT);
        l.setBackground(C_SURFACE);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER),
                BorderFactory.createEmptyBorder(0, 14, 0, 0)));
        l.setHorizontalAlignment(SwingConstants.LEFT);
        l.setVerticalAlignment(SwingConstants.CENTER);
        return l;
    }

    /** Small section divider label; returns the bottom y coordinate */
    private int sectionLabel(String text, int x, int y, int w) {
        JLabel l = new JLabel(text.toUpperCase());
        l.setFont(F_SEC);
        l.setForeground(C_TEXT_MUTED);
        l.setBounds(x, y, w, 16);
        l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER));
        mainFrame.add(l);
        return y + 18;
    }

    /**
     * Creates a label and textfield row directly onto the given container using setBounds.
     * Returns the JTextField so the caller can keep a reference.
     */
    private JTextField labeledField(JFrame frame, String label, String placeholder, int x, int y, int w) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(F_LBL);
        lbl.setForeground(C_TEXT_MUTED);
        lbl.setBounds(x, y, 130, 26);
        frame.add(lbl);

        JTextField tf = new JTextField();
        tf.setFont(F_IN);
        tf.setBackground(C_WHITE);
        tf.setCaretColor(C_TEXT);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        tf.setBounds(x + 132, y, w - 132, 26);
        tf.setToolTipText(placeholder);

        // Placeholder behaviour
        tf.setText(placeholder);
        tf.setForeground(C_PLACEHOLDER);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getForeground().equals(C_PLACEHOLDER)) { tf.setText(""); tf.setForeground(C_TEXT); }
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER_DARK, 1),
                        BorderFactory.createEmptyBorder(3, 6, 3, 6)));
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().trim().isEmpty()) { tf.setText(placeholder); tf.setForeground(C_PLACEHOLDER); }
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER, 1),
                        BorderFactory.createEmptyBorder(3, 6, 3, 6)));
            }
        });
        frame.add(tf);
        return tf;
    }

    private JButton plainBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setForeground(C_TEXT);
        b.setBackground(C_WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(C_BTN_HOVER);
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER_DARK, 1),
                        BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(C_WHITE);
                b.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER, 1),
                        BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            }
        });
        return b;
    }

    private JButton accentBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(F_BTN);
        b.setForeground(C_TEXT);
        b.setBackground(C_ACCENT);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER_DARK, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(C_ACCENT_HVR); }
            public void mouseExited (MouseEvent e) { b.setBackground(C_ACCENT); }
        });
        return b;
    }

    
    private void createPersonalPlan() {
        java.util.List<String> errors = new ArrayList<>();
        String name = getFieldValue(ppModelName);
        if (name.isEmpty()) errors.add("Model name cannot be empty.");

        double price = 0;
        if (getFieldValue(ppPrice).isEmpty()) { errors.add("Price cannot be empty."); }
        else { try { price = Double.parseDouble(getFieldValue(ppPrice)); } catch (NumberFormatException ex) { errors.add("Price must be a valid number."); } }

        int params = 0;
        if (getFieldValue(ppParams).isEmpty()) { errors.add("Parameters cannot be empty."); }
        else { try { params = Integer.parseInt(getFieldValue(ppParams)); } catch (NumberFormatException ex) { errors.add("Parameters must be a valid integer."); } }

        int context = 0;
        if (getFieldValue(ppContext).isEmpty()) { errors.add("Context window cannot be empty."); }
        else { try { context = Integer.parseInt(getFieldValue(ppContext)); } catch (NumberFormatException ex) { errors.add("Context window must be a valid integer."); } }

        int prompts = 0;
        if (getFieldValue(ppPrompts).isEmpty()) { errors.add("Initial prompts cannot be empty."); }
        else { try { prompts = Integer.parseInt(getFieldValue(ppPrompts)); } catch (NumberFormatException ex) { errors.add("Initial prompts must be a valid integer."); } }

        if (!errors.isEmpty()) { showFeedbackModal("Validation Error — Personal Plan", errors, false); return; }

        PersonalPlan p = new PersonalPlan(name, price, params, context, prompts);
        plans.add(p);
        int idx = plans.size() - 1;
        out("Personal plan created  [index " + idx + "]\n" + p.displayInfo());
        clearPersonal();
        showFeedbackModal("Personal Plan Created",
                java.util.Collections.singletonList("Plan saved at index " + idx + ".\nModel: " + name), true);
    }

    private void createProPlan() {
        java.util.List<String> errors = new ArrayList<>();
        String name = getFieldValue(prModelName);
        if (name.isEmpty()) errors.add("Model name cannot be empty.");

        double price = 0;
        if (getFieldValue(prPrice).isEmpty()) { errors.add("Price cannot be empty."); }
        else { try { price = Double.parseDouble(getFieldValue(prPrice)); } catch (NumberFormatException ex) { errors.add("Price must be a valid number."); } }

        int params = 0;
        if (getFieldValue(prParams).isEmpty()) { errors.add("Parameters cannot be empty."); }
        else { try { params = Integer.parseInt(getFieldValue(prParams)); } catch (NumberFormatException ex) { errors.add("Parameters must be a valid integer."); } }

        int context = 0;
        if (getFieldValue(prContext).isEmpty()) { errors.add("Context window cannot be empty."); }
        else { try { context = Integer.parseInt(getFieldValue(prContext)); } catch (NumberFormatException ex) { errors.add("Context window must be a valid integer."); } }

        int slots = 0;
        if (getFieldValue(prSlots).isEmpty()) { errors.add("Initial slots cannot be empty."); }
        else { try { slots = Integer.parseInt(getFieldValue(prSlots)); } catch (NumberFormatException ex) { errors.add("Initial slots must be a valid integer."); } }

        if (!errors.isEmpty()) { showFeedbackModal("Validation Error — Pro Plan", errors, false); return; }

        ProPlan p = new ProPlan(name, price, params, context, slots);
        plans.add(p);
        int idx = plans.size() - 1;
        out("Pro plan created  [index " + idx + "]\n" + p.displayInfo());
        clearPro();
        showFeedbackModal("Pro Plan Created",
                java.util.Collections.singletonList("Plan saved at index " + idx + ".\nModel: " + name), true);
    }

    private void displayInfo(JTextField idFld, String type) {
        AIModel m = getPlan(idFld, type);
        if (m == null) return;
        out(m.displayInfo());
    }

    private void buyPrompts(JTextField idFld) {
        AIModel m = getPlan(idFld, "Personal");
        if (m == null) return;
        PersonalPlan pp = (PersonalPlan) m;
        String input = JOptionPane.showInputDialog(mainFrame,
                "Model: " + pp.getModelName() + "\nNumber of prompts to buy:", "Buy Prompts", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.trim().isEmpty()) return;
        try {
            int amount = Integer.parseInt(input.trim());
            out("[index " + getIndex(idFld) + "] " + pp.buyPrompts(amount));
        } catch (NumberFormatException ex) {
            showFeedbackModal("Invalid Input", java.util.Collections.singletonList("Please enter a whole number."), false);
        } catch (IllegalArgumentException ex) {
            showFeedbackModal("Operation Error", java.util.Collections.singletonList(ex.getMessage()), false);
        }
    }

    private void addTeamMember(JTextField idFld) {
        AIModel m = getPlan(idFld, "Any");
        if (m == null) return;
        if (m instanceof ProPlan) {
            ProPlan pr = (ProPlan) m;
            try {
                String name = req(prTeamMemberName, "Team member name");
                out("[index " + getIndex(idFld) + "] " + pr.addTeamMember(name));
            } catch (IllegalArgumentException ex) {
                showFeedbackModal("Operation Error", java.util.Collections.singletonList(ex.getMessage()), false);
            }
        } else {
            showFeedbackModal("Operation Error",
                    java.util.Collections.singletonList("Team collaboration is only available for Pro Plan subscriptions."), false);
        }
    }

    private void removeTeamMember(JTextField idFld) {
        AIModel m = getPlan(idFld, "Any");
        if (m == null) return;
        if (m instanceof ProPlan) {
            ProPlan pr = (ProPlan) m;
            try {
                String name = req(prTeamMemberName, "Team member name");
                out("[index " + getIndex(idFld) + "] " + pr.removeTeamMember(name));
            } catch (IllegalArgumentException ex) {
                showFeedbackModal("Operation Error", java.util.Collections.singletonList(ex.getMessage()), false);
            }
        } else {
            showFeedbackModal("Operation Error",
                    java.util.Collections.singletonList("Team collaboration is only available for Pro Plan subscriptions."), false);
        }
    }

    private void enterPrompt(JTextField idFld, JTextField promptFld,
                             JTextField outputFld, String type) {
        AIModel m = getPlan(idFld, "Any");
        if (m == null) return;

        try {
            String promptText = req(promptFld, "Prompt text");

            String outputRaw  = getFieldValue(outputFld);
            int    outputLines = 1; 
            if (!outputRaw.isEmpty()) {
                try {
                    outputLines = Integer.parseInt(outputRaw);
                    if (outputLines < 1) outputLines = 1;
                } catch (NumberFormatException ex) {
                    showFeedbackModal("Invalid Input",
                            java.util.Collections.singletonList(
                                    "Output lines must be a whole number. Defaulting to 1."), false);
                }
            }

            if (m instanceof PersonalPlan) {
                PersonalPlan pp = (PersonalPlan) m;
                out("[index " + getIndex(idFld) + "] " + pp.enterPrompt(promptText, outputLines));

            } else if (m instanceof ProPlan) {
                ProPlan pr = (ProPlan) m;
                // Pro Plan — unlimited access, output lines optional (defaults to 1)
                out("[index " + getIndex(idFld) + "] [Pro Plan – Unlimited Access]\n"
                        + pr.enterPrompt(promptText, outputLines));
            }
        } catch (IllegalArgumentException ex) {
            showFeedbackModal("Operation Error",
                    java.util.Collections.singletonList(ex.getMessage()), false);
        }
    }

    private void displayAll() {
        if (plans.isEmpty()) { out("No plans created yet."); return; }
        StringBuilder sb = new StringBuilder("All plans  (" + plans.size() + ")\n");
        sb.append("══════════════════════════════════\n");
        for (int i = 0; i < plans.size(); i++) {
            sb.append("Index ").append(i).append("  [")
              .append(plans.get(i) instanceof PersonalPlan ? "Personal" : "Pro").append("]\n");
            sb.append(plans.get(i).displayInfo()).append("\n");
            sb.append("──────────────────────────────────\n");
        }
        out(sb.toString());
    }

    private void checkPlanType(JTextField idFld) {
        int idx = getIndex(idFld);
        if (idx == -1) {
            showFeedbackModal("Invalid Index",
                    java.util.Collections.singletonList("Plan index must be a valid integer."), false); return;
        }
        if (plans.isEmpty()) {
            showFeedbackModal("No Plans Found",
                    java.util.Collections.singletonList("No plans have been created yet.\nCreate a Personal or Pro plan first."), false); return;
        }
        if (idx < 0 || idx >= plans.size()) {
            showFeedbackModal("Index Out of Range",
                    java.util.Arrays.asList("No plan exists at index " + idx + ".",
                            "Valid range: 0 – " + (plans.size() - 1) + "."), false); return;
        }

        AIModel m = plans.get(idx);
        boolean isPersonal = m instanceof PersonalPlan;
        boolean isPro      = m instanceof ProPlan;
        String typeLabel   = isPersonal ? "Personal Plan" : (isPro ? "Pro Plan" : "Unknown Plan");
        String className   = m.getClass().getSimpleName();
        String modelName   = m.getModelName();
        String priceStr    = String.format("%.2f", m.getPrice());

        java.util.List<String> availableOps = new ArrayList<>();
        availableOps.add("Display info");
        availableOps.add("Enter prompt");
        if (isPersonal) availableOps.add("Buy prompts");
        if (isPro)      { availableOps.add("Add team member"); availableOps.add("Remove team member"); }

        out("checkPlanType  [index " + idx + "]\n"
                + "  Class   : " + className + "\n"
                + "  Type    : " + typeLabel  + "\n"
                + "  Model   : " + modelName  + "\n"
                + "  Personal: " + (isPersonal ? "yes" : "no") + "\n"
                + "  Pro     : " + (isPro      ? "yes" : "no"));

        StringBuilder sb = new StringBuilder();
        sb.append("Class: ").append(className).append("\n");
        sb.append("Model: ").append(modelName).append("\n");
        sb.append("Price: NRs ").append(priceStr).append(" / 1L tokens\n\n");
        sb.append("instanceof checks:\n");
        sb.append("PersonalPlan -> ").append(isPersonal).append("\n");
        sb.append("ProPlan -> ").append(isPro).append("\n");
        sb.append("AIModel -> true\n\nAvailable operations:\n");
        for (String op : availableOps) sb.append("• ").append(op).append("\n");
        JOptionPane.showMessageDialog(mainFrame, sb.toString(),
                "Check Plan Type — Index " + idx, JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearPersonal() {
        for (JTextField f : new JTextField[]{ ppModelName, ppPrice, ppParams, ppContext, ppPrompts, ppOpId, ppPromptText, ppOutputLines })
            resetField(f);
        out("Personal Plan fields cleared.");
    }

    private void clearPro() {
        for (JTextField f : new JTextField[]{ prModelName, prPrice, prParams, prContext, prSlots, prOpId, prTeamMemberName, prPromptText, prOutputLines })
            resetField(f);
        out("Pro Plan fields cleared.");
    }

    private void saveData() {
        File mainFile = new File("plans.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(mainFile, true))) {
            for (AIModel plan : plans) {
                if (plan instanceof PersonalPlan) {
                    PersonalPlan pp = (PersonalPlan) plan;
                    bw.write("Personal," + pp.getModelName() + "," + pp.getPrice() + ","
                            + pp.getParameterCount() + "," + pp.getContextWindow() + ","
                            + pp.getPromptsRemaining());
                } else if (plan instanceof ProPlan) {
                    ProPlan pr = (ProPlan) plan;
                    bw.write("Pro," + pr.getModelName() + "," + pr.getPrice() + ","
                            + pr.getParameterCount() + "," + pr.getContextWindow() + ","
                            + pr.getAvailableSlots());
                }
                bw.newLine();
            }

            showFeedbackModal("Data Saved",
                    java.util.Collections.singletonList("Data successfully saved to plans.txt."), true);
            out("Data saved to plans.txt.");

        } catch (IOException e) {
            showFeedbackModal("Save Error",
                    java.util.Collections.singletonList("Error saving data: " + e.getMessage()), false);
            out("⚠  Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        File file = new File("plans.txt");
        if (!file.exists()) {
            showFeedbackModal("Load Error",
                    java.util.Collections.singletonList("No saved data found (plans.txt missing)."), false); return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            plans.clear();
            StringBuilder content = new StringBuilder("\n--- Loaded File Content ---\n");
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String type     = parts[0];
                    String name     = parts[1];
                    double price    = Double.parseDouble(parts[2]);
                    int    params   = Integer.parseInt(parts[3]);
                    int    context  = Integer.parseInt(parts[4]);
                    int    specific = Integer.parseInt(parts[5]);
                    if (type.equals("Personal")) plans.add(new PersonalPlan(name, price, params, context, specific));
                    else if (type.equals("Pro")) plans.add(new ProPlan(name, price, params, context, specific));
                }
            }
            showFeedbackModal("Data Loaded",
                    java.util.Collections.singletonList("Data successfully loaded from plans.txt."), true);
            out(content + "Data loaded from plans.txt. Total plans: " + plans.size());
            clearPersonal();
            clearPro();
        } catch (IOException | NumberFormatException e) {
            showFeedbackModal("Load Error",
                    java.util.Collections.singletonList("Error loading data: " + e.getMessage()), false);
            out("⚠  Error loading data: " + e.getMessage());
        }
        
    }

    private void showFeedbackModal(String title, java.util.List<String> lines, boolean success) {
        int type = success ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(mainFrame, String.join("\n", lines), title, type);
    }

    private String getFieldValue(JTextField f) {
        if (f.getForeground().equals(C_PLACEHOLDER)) return "";
        return f.getText().trim();
    }

    private int getIndex(JTextField f) {
        try { return Integer.parseInt(f.getText().trim()); }
        catch (NumberFormatException ex) { return -1; }
    }

    private AIModel getPlan(JTextField idFld, String type) {
        int idx = getIndex(idFld);
        if (idx == -1) {
            showFeedbackModal("Invalid Index",
                    java.util.Collections.singletonList("Plan index must be a valid integer."), false); return null;
        }
        if (idx < 0 || idx >= plans.size()) {
            showFeedbackModal("Index Not Found",
                    java.util.Collections.singletonList("No plan exists at index " + idx + ".\nValid range: 0 – " + (plans.size() - 1) + "."), false); return null;
        }
        AIModel m = plans.get(idx);
        if ("Personal".equals(type) && !(m instanceof PersonalPlan)) {
            showFeedbackModal("Wrong Plan Type",
                    java.util.Arrays.asList("Index " + idx + " holds a " + (m instanceof ProPlan ? "Pro Plan" : "Unknown Plan") + ",",
                            "but this operation requires a Personal Plan."), false); return null;
        }
        if ("Pro".equals(type) && !(m instanceof ProPlan)) {
            showFeedbackModal("Wrong Plan Type",
                    java.util.Arrays.asList("Index " + idx + " holds a " + (m instanceof PersonalPlan ? "Personal Plan" : "Unknown Plan") + ",",
                            "but this operation requires a Pro Plan."), false); return null;
        }
        return m;
    }

    private void resetField(JTextField f) {
        f.setText("");
        f.dispatchEvent(new FocusEvent(f, FocusEvent.FOCUS_LOST));
    }

    private String req(JTextField f, String name) {
        String v = getFieldValue(f);
        if (v.isEmpty()) throw new IllegalArgumentException(name + " cannot be empty.");
        return v;
    }

    private void out(String msg) {
        taOut.append(msg + "\n\n");
        taOut.setCaretPosition(taOut.getDocument().getLength());
    }
    
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        new SubscriptionGUI();
    }
}