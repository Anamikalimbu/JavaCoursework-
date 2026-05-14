/**
 * PersonalPlan class extends AIModel and represents a personal subscription plan for individual users with a monthly prompt quota.
 * 
 * @author Anamika Limbu.
 * ID: NP05CP4A250018
 */
public class PersonalPlan extends AIModel 
{
    private int promptsRemaining;
    
    /**
     * Constructor for the PersonalPlan class, initializes a new AIModel with specifices details.
     * @param modelName         The name of the AI Model.
     * @param price             The price in NRs per 1 Lakh tokens.
     * @param parameterCount    The number of model parameters in billions. 
     * @param contextWindow     The context window size of the model.
     * @param initialPrompts    The initial number of prompts remaining.
     */
    public PersonalPlan(String modelName, double price, int parameterCount, int contextWindow, int initialPrompts){
        super(modelName, price, parameterCount, contextWindow);
        if (initialPrompts < 0) {
        throw new IllegalArgumentException("Initial prompts cannot be negative.");
    }
        this.promptsRemaining = initialPrompts;
    }
    
    /**
     * Retrieves the number of prompts remaining.
     * 
     * @return the remaining prompts count.
     */
    public int getPromptsRemaining(){
        return this.promptsRemaining;
    }
    
    /**
     * Allow user to purchase extra prompts and add them to the monthly quota.
     * 
     * @param amount The number of prompt to purchase.
     * @return A message indicating success or error.
     */
    public String buyPrompts(int amount) {
        if (amount < 0) {
            return "ERROR: User must enter a positive value or user must upgrade to pro plan.";
        }
        promptsRemaining += amount;
        return "Success: " + amount + " prompts added. "+ 
        "\nRemaining prompts: " + promptsRemaining;
    }

    /**
     * Simulates entering a prompt and reduces the quota if sufficient prompts are available.
     * 
     * @param promptText the text of the prompt enter by the user.
     * @param tokens the output tokens user expected.
     * @return A message with prompt details and token usage, or a quota-reached message
     */
    public String enterPrompt(String promptText, int expectedOutputLength) {
        if (promptsRemaining <= 0) {
            return "ERROR: Monthly plan has been reached.";
        }
        int tokens = calculateTokens(promptText);
        promptsRemaining--;
        return String.format(
            "Prompt submitted successfully.\n" +
            "  Prompt text  : %s\n" +
            "  Tokens used  : %d\n" +
            "  Prompts left : %d",
            promptText, tokens, promptsRemaining);
    }

    /**
     *Returns a formatted String displaying all PersonalPlan details.
     * 
     * @return a String with all AIModel attributes plus the remaining prompts
     */
    public String displayInfo() {
        return String.format(
            "[ Personal Plan ]\n" +
            super.displayInfo() + "\n" +
            "  Prompts Left   : %d",
            promptsRemaining);
    }
}