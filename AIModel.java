/**
 * Abstract parent class representing a basic AIModel.
 * It serves as the parent class for PersonalPlan and ProPlan
 * @author Anamika Limbu
 * ID: NP05CP4A250018
 */
public abstract class AIModel 
{
    private String modelName;
    private double price;
    private int parameterCount;
    private int contextWindow;

    /**
     * Constructor for the AIModel class, initializes an AIModel object.
     * @param modelName       The name of the AI Model.
     * @param price           The price in NRs per 1 Lakh tokens.
     * @param parameterCount  The number of model parameters in billions. 
     * @param contextWindow   The context window size of the model.
     */
    public AIModel(String modelName, double price, int parameterCount, int contextWindow){
        this.modelName = modelName;
        this.price = price;
        this.parameterCount = parameterCount;
        this.contextWindow = contextWindow;
    }

    /**
     * Retrieves the modelName of AIModel.
     * 
     * @return The name of the AI model.
     */
    public String getModelName(){
        return this.modelName;
    }
    
    /**
     * Retrieves the price of AIModel.
     * 
     * @return The price in Nepali Rupees.
     */
    public double getPrice(){
        return this.price;
    }
    
    /**
     * Retrieves the parameter count.
     * 
     * @return The parameter count.
     */
    public int getParameterCount(){
        return this.parameterCount;
    }
    
    /**
     * Retrieves the context window.
     * 
     * @return The context window size.
     */
    public int getContextWindow(){
        return this.contextWindow;
    }
    
    /**
     * calculateTokens – shared logic used by both plan subclasses.
     * Estimates the token count for a given prompt string.
     * Approximation: 1 token ≈ 4 characters (common industry heuristic).
     *
     * @param prompt the text entered by the user
     * @return estimated token count (minimum 1)
     */
    public int calculateTokens(String prompt) {
        if (prompt == null || prompt.isEmpty()) return 1;
        return (int) Math.ceil(prompt.length() / 4.0);
    }
    
    /**
     * enterPrompt – each subclass applies its own quota / validation rules.
     *
     * @param promptText  the text the user wants to send
     * @param expectedOutputLength ignored here; reserved for subclass use
     * @return a result message shown in the GUI output area
     */
    public abstract String enterPrompt(String promptText, int expectedOutputLength);
    
    /** 
     * displayInfo – formats its own details.
     * @return String representation of the AIModel
     */
    public String displayInfo() {
        return String.format(
            "  Model          : %s\n" +
            "  Price          : NRs %.2f\n" +
            "  Parameters     : %d B\n" +
            "  Context Window : %d tokens",
            modelName, price, parameterCount, contextWindow);
    }
}