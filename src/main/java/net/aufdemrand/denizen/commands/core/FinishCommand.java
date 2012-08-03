package net.aufdemrand.denizen.commands.core;

import java.util.logging.Level;

import org.bukkit.entity.Player;

import net.aufdemrand.denizen.commands.AbstractCommand;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.citizensnpcs.command.exception.CommandException;

/**
 * Sets a Script as 'FINISHED'. Scripts can be finished multiple times.
 * This can also be checked against with the FINISHED requirement.
 *  
 * @author Jeremy Schroeder
 */

public class FinishCommand extends AbstractCommand {

	/* 

	/* Arguments: [] - Required, () - Optional 
	 * None.
	 * 
	 * Modifiers: 
	 * ('SCRIPT:[Script Name]') Changes the script from the triggering script to the one specified.
	 * 
	 * Example usages:
	 */

	@Override
	public boolean execute(ScriptEntry theCommand) throws CommandException {

		String theScript = theCommand.getScript();
		
		/* Get arguments */
		if (theCommand.arguments() != null) {
			for (String thisArgument : theCommand.arguments()) {

				if (plugin.debugMode) plugin.getLogger().log(Level.INFO, "Processing command " + theCommand.getCommand() + " argument: " + thisArgument);

				/* Change the script to a specified one */
				if (thisArgument.contains("SCRIPT:")) 
					theScript = thisArgument.split(":", 2)[1];

				else {
					if (plugin.debugMode) plugin.getLogger().log(Level.INFO, "Unable to match argument!");
				}
			
			}
		}
		
		int finishes = plugin.getAssignments().getInt("Players." + theCommand.getPlayer().getName() + "." + theScript + "." + "Completed", 0);

		finishes++;	
		
		plugin.getSaves().set("Players." + theCommand.getPlayer().getName() + "." + theScript + "." + "Completed", finishes);
		plugin.saveSaves();

		return true;
	}

	
	/* 
	 * GetScriptComplete/GetScriptFail
	 *
	 * Requires the Player and the Script.
	 * Reads the config.yml to find if the player has completed or failed the specified script.
	 *
	 */

	public boolean getScriptCompletes(Player thePlayer, String theScript, String theAmount, boolean negativeRequirement) {

		boolean outcome = false;

		/*
		 * (-)FINISHED (#) [Name of Script]
		 */

			if (Character.isDigit(theAmount.charAt(0))) theScript = theScript.split(" ", 2)[1];
			else theAmount = "1";

			if (plugin.getSaves().getString("Players." + thePlayer.getName() + "." + theScript + "." + "Completed") != null) { 
				if (plugin.getSaves().getInt("Players." + thePlayer.getName() + "." + theScript + "." + "Completed", 0) >= Integer.valueOf(theAmount)) outcome = true;
			}

		if (negativeRequirement != outcome) return true;

		return false;
	}




}