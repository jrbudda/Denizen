package net.aufdemrand.denizen.activities.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.aufdemrand.denizen.activities.AbstractActivity;
import net.aufdemrand.denizen.npc.DenizenNPC;
import net.citizensnpcs.api.ai.Goal;

public class TaskActivity extends AbstractActivity {

	private Map<DenizenNPC, List<TaskGoal>> taskMap = new HashMap<DenizenNPC, List<TaskGoal>>();


	public void addGoal(DenizenNPC npc, String[] arguments, int priority) {
		aH.echoDebug("Adding TASK Activity.");

		int delay = 60;
		int repeats = -1;
		String script = null;
		int duration = 50;

		for (String thisArgument : arguments) {
			if (thisArgument.toUpperCase().contains("DELAY:")) {
				try { delay = Integer.valueOf(thisArgument.split(":", 2)[1]); }
				catch (NumberFormatException e) { aH.echoError("...bad argument '%s'!", thisArgument); }
			}

			else if (thisArgument.toUpperCase().contains("REPEATS:")) {
				try { repeats = Integer.valueOf(thisArgument.split(":", 2)[1]); }
				catch (NumberFormatException e) { aH.echoError("...bad argument '%s'!", thisArgument); }
			}

			else if (thisArgument.toUpperCase().contains("DURATION:")) {
				try { duration = Integer.valueOf(thisArgument.split(":", 2)[1]); }
				catch (NumberFormatException e) { aH.echoError("...bad argument '%s'!", thisArgument); }
			}

			else if (thisArgument.toUpperCase().contains("SCRIPT:")) {
				script = aH.getStringModifier(thisArgument);
			}

		}


		List<TaskGoal> taskGoals = new ArrayList<TaskGoal>();

		if (taskMap.containsKey(npc)) 
			taskGoals = taskMap.get(npc);

		taskGoals.add(0, new TaskGoal(npc, delay, duration, script, repeats, this));

		taskMap.put(npc, taskGoals);
		npc.getCitizensEntity().getDefaultGoalController().addGoal(taskMap.get(npc).get(0), priority);	
	}



	public void removeGoal(DenizenNPC npc, boolean verbose) {
		if (taskMap.containsKey(npc)) {
			for (Goal goal : taskMap.get(npc))
				npc.getCitizensEntity().getDefaultGoalController().removeGoal(goal);
			taskMap.remove(npc);
			if (verbose) plugin.getLogger().info("Removed Wander Activity from NPC.");
		} 
		else if (verbose) plugin.getLogger().info("NPC does not have this activity...");
	}

}
