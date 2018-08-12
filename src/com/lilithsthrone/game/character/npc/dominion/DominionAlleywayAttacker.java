package com.lilithsthrone.game.character.npc.dominion;

import java.util.ArrayList;
import java.time.Month;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.lilithsthrone.game.Season;
import com.lilithsthrone.game.Weather;
import com.lilithsthrone.game.character.CharacterImportSetting;
import com.lilithsthrone.game.character.CharacterUtils;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.attributes.Attribute;
import com.lilithsthrone.game.character.attributes.CorruptionLevel;
import com.lilithsthrone.game.character.fetishes.Fetish;
import com.lilithsthrone.game.character.gender.Gender;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.character.npc.misc.GenericSexualPartner;
import com.lilithsthrone.game.character.persona.Name;
import com.lilithsthrone.game.character.persona.Occupation;
import com.lilithsthrone.game.character.race.RaceStage;
import com.lilithsthrone.game.character.race.RacialBody;
import com.lilithsthrone.game.character.race.Subspecies;
import com.lilithsthrone.game.dialogue.DialogueFlagValue;
import com.lilithsthrone.game.dialogue.DialogueNodeOld;
import com.lilithsthrone.game.dialogue.npcDialogue.SlaveDialogue;
import com.lilithsthrone.game.dialogue.npcDialogue.dominion.AlleywayAttackerDialogue;
import com.lilithsthrone.game.dialogue.npcDialogue.dominion.AlleywayAttackerDialogueCompanions;
import com.lilithsthrone.game.dialogue.places.dominion.PlayerAlleywaySlavery;
import com.lilithsthrone.game.dialogue.npcDialogue.dominion.AlleywayProstituteDialogue;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.utils.CharactersPresentDialogue;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.game.inventory.CharacterInventory;
import com.lilithsthrone.game.inventory.item.AbstractItem;
import com.lilithsthrone.game.slavery.playerSlavery.PlayerSlaveryEvent;
import com.lilithsthrone.game.slavery.playerSlavery.events.EventsSlaveryAlleyway;
import com.lilithsthrone.game.slavery.playerSlavery.rules.RulesSlaveryAlleyway;
import com.lilithsthrone.game.slavery.playerSlavery.rules.RulesSlaveryDefault;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Util;
import com.lilithsthrone.utils.Vector2i;
import com.lilithsthrone.utils.Util.ListValue;
import com.lilithsthrone.world.WorldType;
import com.lilithsthrone.world.places.PlaceType;

/**
 * @since 0.1.66
 * @version 0.2.6
 * @author Innoxia
 */
public class DominionAlleywayAttacker extends NPC {

	public DominionAlleywayAttacker() {
		this(Gender.F_V_B_FEMALE, false);
	}
	
	public DominionAlleywayAttacker(Gender gender) {
		this(gender, false);
	}
	
	public DominionAlleywayAttacker(boolean isImported) {
		this(Gender.F_V_B_FEMALE, isImported);
	}
	
	public DominionAlleywayAttacker(Gender gender, boolean isImported) {
		super(null, "",
				Util.random.nextInt(28)+18, Util.randomItemFrom(Month.values()), 1+Util.random.nextInt(25),
				3, gender, RacialBody.DOG_MORPH, RaceStage.GREATER,
				new CharacterInventory(10), WorldType.DOMINION, PlaceType.DOMINION_BACK_ALLEYS, false);

		if(!isImported) {
	
			this.setWorldLocation(Main.game.getPlayer().getWorldLocation());
			this.setLocation(new Vector2i(Main.game.getPlayer().getLocation().getX(), Main.game.getPlayer().getLocation().getY()));
			
			boolean canalSpecies = false;
			PlaceType pt = Main.game.getActiveWorld().getCell(location).getPlace().getPlaceType();
			if(pt == PlaceType.DOMINION_ALLEYS_CANAL_CROSSING
					|| pt == PlaceType.DOMINION_CANAL
					|| pt == PlaceType.DOMINION_CANAL_END) {
				canalSpecies = true;
			}
			
			// Set random level from 1 to 3:
			setLevel(Util.random.nextInt(3) + 1);
			
			// RACE & NAME:
			
			Map<Subspecies, Integer> availableRaces = new HashMap<>();
			for(Subspecies s : Subspecies.values()) {
				switch(s) {
					// No spawn chance:
					case ANGEL:
					case BAT_MORPH:
					case DEMON:
					case HARPY:
					case HARPY_RAVEN:
					case HARPY_BALD_EAGLE:
					case HUMAN:
					case IMP:
					case IMP_ALPHA:
					case FOX_ASCENDANT:
					case FOX_ASCENDANT_FENNEC:
					case ELEMENTAL_AIR:
					case ELEMENTAL_ARCANE:
					case ELEMENTAL_EARTH:
					case ELEMENTAL_FIRE:
					case ELEMENTAL_WATER:
						break;
						
					// Canals spawn only:
					case ALLIGATOR_MORPH:
						addToSubspeciesMap(canalSpecies?20:0, gender, s, availableRaces);
						break;
					case SLIME:
					case SLIME_ALLIGATOR:
					case SLIME_ANGEL:
					case SLIME_CAT:
					case SLIME_CAT_LYNX:
					case SLIME_CAT_LEOPARD_SNOW:
					case SLIME_CAT_LEOPARD:
					case SLIME_CAT_LION:
					case SLIME_CAT_TIGER:
					case SLIME_CAT_CHEETAH:
					case SLIME_CAT_CARACAL:
					case SLIME_COW:
					case SLIME_DEMON:
					case SLIME_DOG:
					case SLIME_DOG_DOBERMANN:
					case SLIME_DOG_BORDER_COLLIE:
					case SLIME_FOX:
					case SLIME_FOX_FENNEC:
					case SLIME_HARPY:
					case SLIME_HARPY_RAVEN:
					case SLIME_HARPY_BALD_EAGLE:
					case SLIME_HORSE:
					case SLIME_IMP:
					case SLIME_REINDEER:
					case SLIME_SQUIRREL:
					case SLIME_BAT:
					case SLIME_RAT:
					case SLIME_WOLF:
					case SLIME_RABBIT:
						addToSubspeciesMap(canalSpecies?2:0, gender, s, availableRaces);
						break;
					case RAT_MORPH:
						addToSubspeciesMap(canalSpecies?10:0, gender, s, availableRaces);
						break;
						
					// Special spawns:
					case REINDEER_MORPH:
						if(Main.game.getSeason()==Season.WINTER && Main.game.getDialogueFlags().hasFlag(DialogueFlagValue.hasSnowedThisWinter)) {
							addToSubspeciesMap(canalSpecies?1:10, gender, s, availableRaces);
						}
						break;
						
					// Regular spawns:
					case CAT_MORPH:
						addToSubspeciesMap(canalSpecies?5:20, gender, s, availableRaces);
						break;
					case CAT_MORPH_LYNX:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_LEOPARD_SNOW:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_LEOPARD:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_LION:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_TIGER:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_CHEETAH:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case CAT_MORPH_CARACAL:
						addToSubspeciesMap(canalSpecies?2:5, gender, s, availableRaces);
						break;
					case COW_MORPH:
						addToSubspeciesMap(canalSpecies?1:10, gender, s, availableRaces);
						break;
					case DOG_MORPH:
						addToSubspeciesMap(canalSpecies?3:12, gender, s, availableRaces);
						break;
					case DOG_MORPH_DOBERMANN:
						addToSubspeciesMap(canalSpecies?1:4, gender, s, availableRaces);
						break;
					case DOG_MORPH_BORDER_COLLIE:
						addToSubspeciesMap(canalSpecies?1:4, gender, s, availableRaces);
						break;
					case FOX_MORPH:
						addToSubspeciesMap(canalSpecies?1:10, gender, s, availableRaces);
						break;
					case FOX_MORPH_FENNEC:
						addToSubspeciesMap(5, gender, s, availableRaces);
						break;
					case HORSE_MORPH:
						addToSubspeciesMap(canalSpecies?4:16, gender, s, availableRaces);
						break;
					case HORSE_MORPH_ZEBRA:
						addToSubspeciesMap(canalSpecies?1:4, gender, s, availableRaces);
						break;
					case SQUIRREL_MORPH:
						addToSubspeciesMap(canalSpecies?1:10, gender, s, availableRaces);
						break;
					case WOLF_MORPH:
						addToSubspeciesMap(canalSpecies?5:20, gender, s, availableRaces);
						break;
					case RABBIT_MORPH:
						addToSubspeciesMap(canalSpecies?1:3, gender, s, availableRaces);
						break;
					case RABBIT_MORPH_LOP:
						addToSubspeciesMap(canalSpecies?1:3, gender, s, availableRaces);
						break;
				}
			}
			
			this.setBodyFromSubspeciesPreference(gender, availableRaces);
			
			setSexualOrientation(RacialBody.valueOfRace(getRace()).getSexualOrientation(gender));
	
			setName(Name.getRandomTriplet(this.getRace()));
			this.setPlayerKnowsName(false);
			setDescription(UtilText.parse(this,
					"[npc.Name] is a resident of Dominion, who, for reasons of [npc.her] own, prowls the back alleys in search of victims to prey upon."));
			
			// PERSONALITY & BACKGROUND:
			
			CharacterUtils.setHistoryAndPersonality(this, true);
			if(Main.game.getCurrentWeather()==Weather.MAGIC_STORM) {
				this.setHistory(Occupation.NPC_MUGGER);
			}
			
			// ADDING FETISHES:
			
			CharacterUtils.addFetishes(this);
			
			// BODY RANDOMISATION:
			
			CharacterUtils.randomiseBody(this);
			
			// INVENTORY:
			
			resetInventory();
			inventory.setMoney(10 + Util.random.nextInt(getLevel()*10) + 1);
			CharacterUtils.generateItemsInInventory(this);
	
			CharacterUtils.equipClothing(this, true, false);
			CharacterUtils.applyMakeup(this, true);
			
			// Set starting attributes based on the character's race
			for (Attribute a : RacialBody.valueOfRace(this.getRace()).getAttributeModifiers().keySet()) {
				attributes.put(a, RacialBody.valueOfRace(this.getRace()).getAttributeModifiers().get(a).getMinimum() + RacialBody.valueOfRace(this.getRace()).getAttributeModifiers().get(a).getRandomVariance());
			}
			
			setMana(getAttributeValue(Attribute.MANA_MAXIMUM));
			setHealth(getAttributeValue(Attribute.HEALTH_MAXIMUM));
		}

		this.setEnslavementDialogue(SlaveDialogue.DEFAULT_ENSLAVEMENT_DIALOGUE);
	}
	
	@Override
	public void loadFromXML(Element parentElement, Document doc, CharacterImportSetting... settings) {
		loadNPCVariablesFromXML(this, null, parentElement, doc, settings);
	}
	
	@Override
	public boolean isUnique() {
		return false;
	}
	
	@Override
	public void hourlyUpdate() {
		if(this.getHistory()==Occupation.NPC_PROSTITUTE && this.getLocationPlace().getPlaceType()==PlaceType.ANGELS_KISS_BEDROOM) {
			// Remove client:
			List<NPC> charactersPresent = Main.game.getCharactersPresent(this.getWorldLocation(), this.getLocation());
			if(charactersPresent.size()>1) {
				for(NPC npc : charactersPresent) {
					if(npc instanceof GenericSexualPartner) {
	//					System.out.println("partner removed for "+slave.getName());
						Main.game.banishNPC(npc);
					}
				}
				
			} else if(Math.random()<0.33f) { // Add client:
				GenericSexualPartner partner;
				
				if(Math.random()<0.25f) {
					partner = new GenericSexualPartner(Gender.F_P_V_B_FUTANARI, this.getWorldLocation(), this.getLocation(), false);
				} else {
					partner = new GenericSexualPartner(Gender.M_P_MALE, this.getWorldLocation(), this.getLocation(), false);
				}
				try {
					Main.game.addNPC(partner, false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public String getDescription() {
		if(this.getHistory()==Occupation.NPC_PROSTITUTE) {
			if(this.isSlave()) {
				return (UtilText.parse(this,
						"[npc.NamePos] days of whoring [npc.herself] out in the back alleys of Dominion are now over. Having run afoul of the law, [npc.sheIs] now a slave, and is no more than [npc.her] owner's property."));
			} else {
				return (UtilText.parse(this,
						"[npc.Name] is a prostitute who whores [npc.herself] out in the backalleys of Dominion."));
			}
			
		} else {
			if(this.isSlave()) {
				return (UtilText.parse(this,
						"[npc.NamePos] days of prowling the back alleys of Dominion and mugging innocent travellers are now over. Having run afoul of the law, [npc.sheIs] now a slave, and is no more than [npc.her] owner's property."));
			} else {
				return (UtilText.parse(this,
						"[npc.Name] is a resident of Dominion, who prowls the back alleys in search of innocent travellers to mug and rape."));
			}
		}
	}
	
	@Override
	public void endSex() {
		if(!isSlave()) {
			setPendingClothingDressing(true);
		}
	}

	@Override
	public boolean isClothingStealable() {
		return true;
	}
	
	@Override
	public boolean isAbleToBeImpregnated() {
		return true;
	}
	
	@Override
	public void changeFurryLevel(){
	}
	
	@Override
	public DialogueNodeOld getEncounterDialogue() {
		PlaceType pt = Main.game.getActiveWorld().getCell(location).getPlace().getPlaceType();
		
		if(pt == PlaceType.DOMINION_BACK_ALLEYS
				|| pt == PlaceType.DOMINION_CANAL
				|| pt == PlaceType.DOMINION_ALLEYS_CANAL_CROSSING
				|| pt == PlaceType.DOMINION_CANAL_END) {
			
			if(this.getHistory()==Occupation.NPC_PROSTITUTE) {
				this.setPlayerKnowsName(true);
				return AlleywayProstituteDialogue.ALLEY_PROSTITUTE;
				
			} else {
				if(Main.game.getPlayer().getCompanions().isEmpty()) {
					return AlleywayAttackerDialogue.ALLEY_ATTACK;
				} else {
					return AlleywayAttackerDialogueCompanions.ALLEY_ATTACK;
				}
			}
			
		} else {
			return AlleywayAttackerDialogue.STORM_ATTACK;
		}
	}

	// Combat:

	@Override
	public Response endCombat(boolean applyEffects, boolean victory) {
		if(this.getHistory()==Occupation.NPC_PROSTITUTE) {
			if (victory) {
				return new Response("", "", AlleywayProstituteDialogue.AFTER_COMBAT_VICTORY);
			} else {
				return new Response ("", "", AlleywayProstituteDialogue.AFTER_COMBAT_DEFEAT);
			}
		} else {
			if(Main.game.getPlayer().getCompanions().isEmpty()) {
				if (victory) {
					return new Response("", "", AlleywayAttackerDialogue.AFTER_COMBAT_VICTORY);
				} else {
					return new Response ("", "", AlleywayAttackerDialogue.AFTER_COMBAT_DEFEAT);
				}
			} else {
				if (victory) {
					return new Response("", "", AlleywayAttackerDialogueCompanions.AFTER_COMBAT_VICTORY);
				} else {
					return new Response ("", "", AlleywayAttackerDialogueCompanions.AFTER_COMBAT_DEFEAT);
				}
			}
		}
	}
	
	@Override
	public String getItemUseEffects(AbstractItem item, GameCharacter user, GameCharacter target){
		return getItemUseEffectsAllowingUse(item, user, target);
	}
	
	/**
	 * Automatically enters the scene that triggers when player passes out from their collar or gets brought back by enforcers.
	 * 
	 * It should handle the movement to a new location as well.
	 */
	@Override
	public void triggerSlaveRetrivalDialogue()
	{
		Main.game.getPlayer().setLocation(this.worldLocation, this.location, false);
		
		// Warning: We are doing dangerous shit there. Since slavery system is supposed to be an overbearing one, it's safe to do it here, but to other contributors, here's a note - don't do it without a good reason!
		// To explain - this function below sets the dialogue node.
		// It can force it in the middle of anything, potentially sequence breaking a quest dialogue.
		// Using it outside of these tight constraints should be avoided.
		// (Note for Inno: I'm just leaving this as a warning for future contributors :3)
		Main.game.setContent(new Response("", "", PlayerAlleywaySlavery.ALLEYWAY_SLAVE_RECOVERED));		
	}
	
	@Override
	public Set<PlayerSlaveryEvent> getSlaveryEvents()
	{
		Set<PlayerSlaveryEvent> returnable = new HashSet<>();
		
		// Punishments
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_PUNISHMENT_TF_POTION);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_PUNISHMENT_KINK_POTION);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_PUNISHMENT_BEGGING);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_PUNISHMENT_MUG);
		
		// Rule changes
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_RULE_DAILY_TRIBUTE);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_RULE_NAKED);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_RULE_OUTSIDE_FREEDOM);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_RULE_DEGRADING_NAME_CHANGE);
		
		// "Quests"
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_QUEST_BRIBE);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_QUEST_SUPPLY_RUN);
		
		// Special events
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_EVENT_ARCANE_STORM_SEX);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_EVENT_NORMAL_SEX);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_EVENT_SUBMISSIVE_SEX);
		returnable.add(EventsSlaveryAlleyway.ALLEYWAY_SLAVE_EVENT_INSPECTION);
		
		return returnable;
	}
	
	@Override
	public Response getTalkResponse(int index)
	{
		PlayerCharacter player = Main.game.getPlayer();
		if(player.getOwner() == this)
		{
			if(index == 1 && this.getRule(RulesSlaveryDefault.RULE_DAILY_TRIBUTE_MONEY) != null)
			{
				if(player.getMoney() <= 0)
				{
					return new Response("Pay up", "You don't have any money to pay your master with!", null);
				}
				else if(this.getRule(RulesSlaveryDefault.RULE_DAILY_TRIBUTE_MONEY).getCashRequirement() <= 0)
				{
					return new Response("Pay up", "You've paid off your tribute for today.", null);
				}
				
				int toPay = Math.min(this.getRule(RulesSlaveryDefault.RULE_DAILY_TRIBUTE_MONEY).getCashRequirement(), player.getMoney());
				return new Response("Pay up "+UtilText.formatAsMoney(toPay), "Pay "+UtilText.formatAsMoney(toPay)+" to your master to hopefully fullfill the tribute for today", CharactersPresentDialogue.MENU)	{
					@Override
					public void effects()
					{
						player.incrementMoney(-toPay);
						getRule(RulesSlaveryDefault.RULE_DAILY_TRIBUTE_MONEY).modifyCashRequirement(toPay*-1);
					}
				};
			}
			else if(index == 2)
			{
				if(player.getMoney() < 50000)
				{
					return new Response("Buy Freedom", "You don't think whatever you have is enough to buy yourself freedom! You need "+UtilText.formatAsMoney(50000), null);
				}
				else
				{
					return new Response("Buy Freedom "+UtilText.formatAsMoney(50000), "Pay "+UtilText.formatAsMoney(50000)+" to your master to pay off yourself and buy yourself freedom.", PlayerAlleywaySlavery.BUY_FREEDOM)	{
						@Override
						public void effects()
						{
							player.incrementMoney(-50000);
						}
					};
				}
			}
			else if(index == 3)
			{
				ArrayList<Fetish> applicableFetishes = Util.newArrayListOfValues(new ListValue<>(Fetish.FETISH_SUBMISSIVE), new ListValue<>(Fetish.FETISH_SLAVE));
				CorruptionLevel applicableCorruptionLevel = Fetish.FETISH_SUBMISSIVE.getAssociatedCorruptionLevel();
				
				if(player.getObedienceValue() < 0f &&  !player.hasFetish(Fetish.FETISH_SUBMISSIVE) &&  !player.hasFetish(Fetish.FETISH_MASOCHIST))
				{
					return new Response("Beg for Punishment", "You can't even <i>begin</i> to think about that! That's too demeaning for you.", null);
				}
				else
				{
					return new Response("Beg for Punishment", "You do feel that begging for punishment sounds fun...", PlayerAlleywaySlavery.BEGGED_FOR_PUNISHMENT,
							applicableFetishes,
							applicableCorruptionLevel,
							null,
							null,
							null){
						@Override
						public void effects()
						{
							Main.game.getTextEndStringBuilder().append(player.getOwner().incrementAffection(player, 5f));
							Main.game.getTextEndStringBuilder().append(player.incrementObedience(5f));
						}
					};
				}
			}
			else if(index == 4)
			{
				if(Main.game.getPlayer().getOwner().getRule(RulesSlaveryAlleyway.RULE_ALLEYWAY_SUPPLY_RUN) != null)
				{
					if(!RulesSlaveryAlleyway.RULE_ALLEYWAY_SUPPLY_RUN.canCompleteRule())
					{
						return new Response("Give "+RulesSlaveryAlleyway.RULE_ALLEYWAY_SUPPLY_RUN.getTargetName(), "You can't give the item to your master because you don't have it.", null);
					}
					else
					{
						return new Response("Give "+RulesSlaveryAlleyway.RULE_ALLEYWAY_SUPPLY_RUN.getTargetName(), "Give the item that your master asked of you to them.", CharactersPresentDialogue.MENU){
							@Override
							public void effects()
							{
								RulesSlaveryAlleyway.RULE_ALLEYWAY_SUPPLY_RUN.completeRule();
							}
						};
					}
				}
			}
		}
		return null;
  }
}
