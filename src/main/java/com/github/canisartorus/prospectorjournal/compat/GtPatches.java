package com.github.canisartorus.prospectorjournal.compat;

import com.github.canisartorus.prospectorjournal.ConfigHandler;

import static gregapi.data.CS.W;

import gregapi.data.CS.ArmorsGT;
import gregapi.data.MD;
import gregapi.util.ST;

public class GtPatches {

	public static void onInit() {
		if( ConfigHandler.patchHazMat) {
			if(MD.GC_ADV_ROCKETRY.mLoaded) {
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceHelmet", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceBoots", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceChest", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceLeggings", 1, W));

				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceHelmet", 1, W));
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceBoots", 1, W));
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceChest", 1, W));
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceLeggings", 1, W));

				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceHelmet", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceBoots", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceChest", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceLeggings", 1, W));

				ArmorsGT.HAZMATS_FROST.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceHelmet", 1, W));
				ArmorsGT.HAZMATS_FROST.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceBoots", 1, W));
				ArmorsGT.HAZMATS_FROST.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceChest", 1, W));
				ArmorsGT.HAZMATS_FROST.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceLeggings", 1, W));

				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceHelmet", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceBoots", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceChest", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.GC_ADV_ROCKETRY, "item.spaceLeggings", 1, W));

			}
			if(MD.ARS.mLoaded) {
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.ARS, "water_orbs", 1, W));

				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ARS, "fire_ears", 1, W));

				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.ARS, "enderBoots", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.ARS, "earth_armor", 1, W));
			}

			if(MD.ATUM.mLoaded) {
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.ATUM, "item.mummyHelmet", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.ATUM, "item.mummyChest", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.ATUM, "item.mummyLegs", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.ATUM, "item.mummyBoots", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.ATUM, "item.isisEmbrace", 1, W));

				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.ATUM, "item.horusFlight", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.ATUM, "item.nutsAgility", 1, W));

				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ATUM, "item.mummyHelmet", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ATUM, "item.mummyChest", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ATUM, "item.mummyLegs", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ATUM, "item.mummyBoots", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.ATUM, "item.sekmetsWrath", 1, W));

				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.ATUM, "item.rasGlory", 1, W));
			}

			if(MD.ERE.mLoaded) {
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.ERE, "waterStriders", 1, W));
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.ERE, "compoundGoggles", 1, W));
				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.ERE, "reinCompoundGoggles", 1, W));

				ArmorsGT.HAZMATS_INSECTS.add(ST.make(MD.ERE, "spiderTShirt", 1, W));
			}

			if(MD.TC.mLoaded) {
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TC, "BootsTraveller", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TC, "ItemLeggingsFortress", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TC, "ItemChestplateFortress", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TC, "ItemHelmetFortress", 1, W));

				ArmorsGT.HAZMATS_CHEM.add(ST.make(MD.TC, "BootsTraveller", 1, W));

				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemBootsVoid", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemLeggingsVoidFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemChestplateVoidFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemHelmetVoidFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "BootsTraveller", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemLeggingsFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemChestplateFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "ItemHelmetFortress", 1, W));
				ArmorsGT.HAZMATS_GAS.add(ST.make(MD.TC, "HoverHarness", 1, W));

				ArmorsGT.HAZMATS_FROST.add(ST.make(MD.TC, "BootsTraveller", 1, W));

				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.TC, "ItemBootsVoid", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.TC, "ItemLeggingsVoidFortress", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.TC, "ItemChestplateVoidFortress", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(MD.TC, "ItemHelmetVoidFortress", 1, W));
			}

			if(MD.TF.mLoaded) {
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TF, "plateNaga", 1, W));
				ArmorsGT.HAZMATS_BIO.add(ST.make(MD.TF, "legsNaga", 1, W));

				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.TF, "fieryBoots", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.TF, "fieryLegs", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.TF, "fieryPlate", 1, W));
				ArmorsGT.HAZMATS_HEAT.add(ST.make(MD.TF, "fieryHelm", 1, W));

				ArmorsGT.HAZMATS_LIGHTNING.add(ST.make(MD.TF, "ironwoodHelm", 1, W));
				ArmorsGT.HAZMATS_LIGHTNING.add(ST.make(MD.TF, "ironwoodPlate", 1, W));
				ArmorsGT.HAZMATS_LIGHTNING.add(ST.make(MD.TF, "ironwoodLegs", 1, W));
				ArmorsGT.HAZMATS_LIGHTNING.add(ST.make(MD.TF, "ironwoodBoots", 1, W));
			}

			if(OtherMods.NUCC.mLoaded) {
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "dUBoots", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "dULegs", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "dUChest", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "dUHelm", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "toughBoots", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "toughLegs", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "toughChest", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "toughHelm", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "boronBoots", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "boronLegs", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "boronChest", 1, W));
				ArmorsGT.HAZMATS_RADIOACTIVE.add(ST.make(OtherMods.NUCC, "boronHelm", 1, W));
			}
		}

	}

}
