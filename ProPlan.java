/**
 * ProPlan class extends AIModel and represents a pro subscription plan for teams.
 *
 * @author Anamika Limbu
 * ID: NP05CP4A250018
 */
public class ProPlan extends AIModel 
{
    private int availableSlots;
     /**
     * Constructor for the ProPlan class, initializes a new AIModel with specifices details.
     * @param modelName       The name of the AI Model.
     * @param price           The price in NRs per 1 Lakh tokens.
     * @param parameterCount  The number of model parameters in billions. 
     * @param contextWindow   The context window size of the model.
     * @param initialSlots  The initial number of available team slots.
     */
    public ProPlan(String modelName, double price, int parameterCount, int contextWindow,int initialSlots){
        super(modelName, price, parameterCount, contextWindow);
        if (initialSlots < 0) {
            throw new IllegalArgumentException("Slots cannot be negative.");
        }
        this.availableSlots = initialSlots;
    }
    
    /**
     * Retrieves the number of available slots.
     * @return the available team member slots.
     */
    public int getAvailableSlots(){
        return availableSlots;
    }
    
    /**
     * Add the team member if slots are available, decrease the available team slots accordingly.
     * 
     * @param name The name of the team member added.
     * @return A message indicating success or no slots available.
     */
    public String addTeamMember(String name) {
        if (availableSlots <= 0) {
            return "ERROR: No available slots. Cannot add team member '" + name + "'.";
        }
        availableSlots--;
        return "Team member '" + name + "' added. Slots remaining: " + availableSlots;
    }

    /**
     * Removes a team member, increase the available slots.
     * 
     * @param name The name of the team member to remove.
     * @return A message confirming removal and updated slots.
     */
    public String removeTeamMember(String name) {
        availableSlots++;
        return "Team member '" + name + "' removed. Slots available: " + availableSlots;
    }
    
    /**
     * enterPrompt – validates only the context window (prompts are unlimited).
     *
     * Rules:
     *  1. Estimated token count must be ≤ contextWindow
     *  2. No quota is decremented (Pro plan is unlimited)
     *
     * @param promptText  the text the user wishes to send
     * @param expectedOutputLength not used for ProPlan (reserved parameter)
     * @return a descriptive result message
     */
    public String enterPrompt(String promptText, int expectedOutputLength) {
        int tokens = calculateTokens(promptText);
        if (tokens > getContextWindow()) {
            return String.format(
                "ERROR: Prompt too long. Estimated tokens: %d | Context window: %d",
                tokens, getContextWindow());
        }

        return String.format(
            "Prompt submitted successfully (unlimited plan).\n" +
            "  Model        : %s\n" +
            "  Tokens used  : %d / %d\n" +
            "  Quota        : Unlimited",
            getModelName(), tokens, getContextWindow());
    }


     /**
     * Returns a formatted String displaying all ProPlan details.
     * 
     * @return String with all AIModel attributes, available slots, and team members
     */
    public String displayInfo() {
        return String.format(
            "[ Pro Plan ]\n" +
            super.displayInfo() + "\n" +
            "  Available Slots: %d",
            availableSlots);
    }
}