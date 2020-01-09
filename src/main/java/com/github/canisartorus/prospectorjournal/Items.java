package com.github.canisartorus.prospectorjournal;

import java.util.List;

import gregapi.data.ANY;
import gregapi.data.CS;
import gregapi.data.MD;
import gregapi.data.MT;
import gregapi.data.OD;
import gregapi.data.OP;
import gregapi.data.TC;
import gregapi.oredict.OreDictMaterial;
import gregapi.util.CR;
import gregapi.util.ST;
import gregapi.util.UT;
import net.minecraft.item.ItemStack;

//@author Alexander James

public class Items {
	public static final String CTAB_BOXES = "Prospector Collection Boxes";
	public static ItemStack noteBook;
	static gregapi.item.multiitem.MultiItem mBoxes;

	public static void RegisterItems() {
		// invent a Block for the TileEntities to Have-A
//		MultiTileEntityBlock.getOrCreate(ProspectorJournal.MOD_ID, "machine", gregapi.block.MaterialMachines.instance, Block.soundTypeMetal, CS.TOOL_pickaxe, -2, -1, 5, false, false);
//		MultiTileEntityBlock.getOrCreate(ProspectorJournal.MOD_ID, "wood", Material.wood, Block.soundTypeWood, CS.TOOL_axe, -2, -1, 5, false, false);
		
		// makes a bunch of items to the same ID and name
		new gregapi.item.multiitem.MultiItemRandom(ProspectorJournal.MOD_ID, "ca.prospectorjournal.notebook") {
			@Override
			public void addItems() {
				Items.noteBook = addItem(0, "Prospector's Journal", "Cross-referencing all the rocks.", JournalBehaviour.INSTANCE, TC.PERFODIO.get(2), TC.COGNITO.get(4), TC.ORDO.get(1));
//				Items.noteBook = ST.make(this, 1, 0);
				
//				CR.shaped(Items.noteBook, CR.DEF, "PRP", "RDR", "PRP", 'D', OP.dye, 'P', ST.make(net.minecraft.init.Items.paper, 1, 0), 'R', OP.rockGt);
				
//XXX				gregapi.data.CS.BooksGT.BOOK_REGISTER.add(Items.noteBook, (byte)53);
			}
		};
		
		// No Problem, you can add single Items too, if you just need those.
		// Assets go into "/assets/insert_your_modid_here/textures/items/$name/..."
		// The Textures themselves are just the IDs you insert down there. So "0.png" for the Tiny Pile of Examplium Dust.
		mBoxes = new MultiSampleBoxItem(ProspectorJournal.MOD_ID, "ca.prospectorjournal.samplebag") {@Override public void addItems() {
			addItem();
		}	};
//		net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new BoxBehaviour(mBoxes));
	}

	public static void RegisterRecipes() {
		CR.shaped(Items.noteBook, CR.DEF, "PRP", "RDR", "PRP", 'D', OP.dye, 'P', ST.make(net.minecraft.init.Items.paper, 1, 0), 'R', OP.rockGt);
		
		/*
		gregapi.block.multitileentity.MultiTileEntityRegistry caMTEReg = gregapi.block.multitileentity.MultiTileEntityRegistry.getRegistry("ca.pj.multitileentity");
		// Block Textures have to be at: "/assets/gregtech/textures/blocks/machines/basicmachines/$NBT_TEXTURE/..." Yes that is not a Typo, it is actually the GregTech Mod-ID in that path. I noticed that flaw way too late to fix it. And look at how GT has the Textures for its Oven for Details.
		MultiTileEntityBlock tBox = MultiTileEntityBlock.getOrCreate(ProspectorJournal.MOD_ID, "machine", gregapi.block.MaterialMachines.instance, Block.soundTypeMetal, CS.TOOL_pickaxe, -2, -1, 5, false, false);
		MultiTileEntityBlock tCrate	= MultiTileEntityBlock.getOrCreate(ProspectorJournal.MOD_ID, "wood", Material.wood, Block.soundTypeWood, CS.TOOL_axe, -2, -1, 5, false, false);
		MultiTileEntityBlock tBag	= MultiTileEntityBlock.getOrCreate(ProspectorJournal.MOD_ID, "cloth", Material.cloth, Block.soundTypeCloth, CS.TOOL_axe, -1, -1, 5, false, false);
				
		caMTEReg.add("Rock Sample Box (Wood)", CTAB_BOXES, 		0, 0, SampleCrateTile.class,	0, 1, tCrate,
				UT.NBT.make(CS.NBT_MATERIAL, ANY.WoodNormal, CS.NBT_COLOR, UT.Code.getRGBaInt(MT.Wood.fRGBaSolid), CS.NBT_HARDNESS, 3.0f, CS.NBT_RESISTANCE, 3.0f, CS.NBT_FLAMMABILITY, 100, CS.NBT_TEXTURE, "woodchest", CS.INV_SIZE, 8),
				" PS", "P P", " P ", 'P', OD.plankAnyWood, 'S', OP.stick.dat(ANY.WoodNormal));
		makeWoodBox(MT.WoodSealed,		1, 3.0f, 12, true, tCrate);
		caMTEReg.add("Rock Sample Box (SkyRoot)", CTAB_BOXES,	2, 0, SampleCrateTile.class,	MT.Skyroot.mToolQuality, 1, tCrate,
				UT.NBT.make(CS.NBT_MATERIAL, MT.Skyroot, CS.NBT_COLOR, UT.Code.getRGBaInt(MT.SkyRoot.fRGBaSolid), CS.NBT_HARDNESS, 4.0f, CS.NBT_RESISTANCE, 4.0f, CS.NBT_FLAMMABILITY, 100, CS.NBT_TEXTURE, "woodchest", CS.INV_SIZE, 12),
				" PS", "P P", " P ", 'P', OD.plankSkyroot, 'S', OP.stick.dat(MT.Skyroot));
		caMTEReg.add("Rock Sample Box (Weedwood)", CTAB_BOXES, 		3, 0, SampleCrateTile.class,	0, 1, tCrate,
				UT.NBT.make(CS.NBT_MATERIAL, MT.Weedwood, CS.NBT_COLOR, UT.Code.getRGBaInt(MT.Weedwood.fRGBaSolid), CS.NBT_HARDNESS, 4.0f, CS.NBT_RESISTANCE, 4.0f, CS.NBT_TEXTURE, "woodchest", CS.INV_SIZE, 8),
				" PS", "P P", " P ", 'P', OD.plankWeedwood, 'S', OP.stick.dat(MT.Weedwood));
		makeWoodBox(MT.Livingwood,		4, 3.0f, 12, true, tCrate);
		makeWoodBox(MT.Dreamwood,		5, 5.0f, 16, false, tCrate);
		makeWoodBox(MT.Shimmerwood,		6, 5.0f, 16, false, tCrate);
		makeWoodBox(MT.IronWood,		7, 4.0f, 12, false, tCrate);
		makeWoodBox(MT.Greatwood,		8, 3.0f, 12, true, tCrate);
		makeWoodBox(MT.Silverwood,		9, 5.0f, 16, false, tCrate);
		makeBoxes(	MT.Pb, 				10, 4.0f, 8, tBox);
		makeBoxes(	MT.Bi,				11, 4.0f, 12, tBox);
		makeBoxes(	MT.Sb,				12, 4.0f, 12, tBox);
		makeBoxes(	MT.Ni, 				13, 4.0f, 16, tBox);
		makeBoxes(	MT.Bronze,			14, 7.0f, 16, tBox);
		makeBoxes(	MT.FakeOsmium,		15, 2.0f, 16, tBox);
		makeBoxes(	MT.Al,				16, 2.0f, 20, tBox);
		makeBoxes(	MT.Brass,			17, 2.5f, 20, tBox);
		makeBoxes(	MT.TinAlloy,		18, 3.0f, 20, tBox);
		makeBoxes(	MT.Co,				19, 4.0f, 20, tBox);
		makeBoxes(	MT.Ardite,			20, 2.0f, 20, tBox);
		makeBoxes(	ANY.Iron,			21, 3.5f, 24, tBox);
		makeBoxes(	MT.Ge,				22, 4.0f, 24, tBox);
		makeBoxes(	MT.Invar,			23, 4.0f, 24, tBox);
		makeBoxes(	MT.Steel,			24, 6.0f, 24, tBox);
		makeBoxes(	MT.HSLA,			25, 6.0f, 26, tBox);
		makeBoxes(	MT.Au,				26, 2.0f, 26, tBox);
		makeBoxes(	MT.Ag,				27, 2.0f, 26, tBox);
		makeBoxes(	MT.Mn,				28, 6.0f, 26, tBox);
		makeBoxes(	MT.Manyullyn,		29, 3.0f, 26, tBox);
		makeBoxes(	MT.Knightmetal,		30, 7.0f, 28, tBox);
		makeBoxes(	MT.SteelGalvanized,	31, 6.0f, 28, tBox);
		makeBoxes(	MT.Meteorite,		32, 7.0f, 28, tBox);
		makeBoxes(	MT.MeteoricSteel,	33, 8.0f, 30, tBox);
		makeBoxes(	MT.GildedIron,		34, 6.0f, 30, tBox);
		makeBoxes(	MT.Mo,				35, 6.0f, 30, tBox);
		makeBoxes(	MT.Symorite,		36, 4.0f, 32, tBox);
		makeBoxes(	MT.Electrum,		37, 2.0f, 32, tBox);
		makeBoxes(	MT.StainlessSteel,	38, 5.0f, 32, tBox);
		makeBoxes(	MT.Thaumium,		39, 9.0f, 32, tBox);
		makeBoxes(	MT.Manasteel,		40, 9.0f, 32, tBox);
		makeBoxes(	MT.Ti,				41, 9.0f, 34, tBox);
		makeBoxes(	MT.Cr,				42, 4.0f, 36, tBox);
		makeBoxes(	MT.Pt,				43, 2.0f, 38, tBox);
		makeBoxes(	MT.Octine,			44, 8.0f, 38, tBox);
		makeBoxes(	MT.Desh,			45, 15.0f, 40, tBox);
		makeBoxes(	MT.Terrasteel,		46, 15.0f, 40, tBox);
		makeBoxes(	MT.TungstenSteel,	47, 12.5f, 44, tBox);
		makeBoxes(	MT.TungstenCarbide,	48, 12.5f, 44, tBox);
		makeBoxes(	MT.DuraniumAlloy,	49, 20.0f, 44, tBox);
		makeBoxes(	MT.Draconium,		50, 50.0f, 44, tBox);
		makeBoxes(	MT.Ultimet,			51, 12.5f, 44, tBox);
		makeBoxes(	ANY.W,				52, 10.0f, 48, tBox);
		makeBoxes(	MT.Ir,				53, 15.0f, 48, tBox);
		makeBoxes(	MT.Os,				54, 3.0f, 48, tBox);
		makeBoxes(	MT.VoidMetal,		55, 30.0f, 48, tBox);
		makeBoxes(	MT.ElvenElementium,	56, 30.0f, 48, tBox);
		makeBoxes(	MT.TritaniumAlloy,	57, 30.0f, 48, tBox);
		makeBoxes(	MT.Ad,				58, 100.0f, 48, tBox);
		makeBoxes(MT.Bedrock_HSLA_Alloy,59, 100.0f, 48, tBox);
		makeBoxes(MT.DraconiumAwakened, 60, 100.0f, 48, tBox);
		makeBoxes(	MT.Infinity,		61, 100.0f, 48, tBox);
		makeBoxes(	MT.Plastic,			62, 3.0f, 16, tCrate);
		caMTEReg.add("Rock Sample Bag (leather)", CTAB_BOXES, 63, 0, SampleCrateTile.class, 0, 1, tBag, 
				gregapi.util.UT.NBT.make(CS.NBT_MATERIAL, MT.Leather, CS.NBT_HARDNESS, 0.5f, CS.NBT_RESISTANCE, 2.0f, CS.NBT_COLOR, UT.Code.getRGBInt(MT.Leather.fRGBaSolid), CS.NBT_TEXTURE, "woodchest", CS.NBT_INV_SIZE, 6, CS.NBT_FLAMMABILITY, 100),
				" LS", "LbL", " L ", 'L', OD.itemLeather, 'S', OP.stick.dat(ANY.Wood));
		caMTEReg.add("Rock Sample Bag (fabric)", CTAB_BOXES, 64, 0, SampleCrateTile.class, 0, 1, tBag, 
				gregapi.util.UT.NBT.make(CS.NBT_MATERIAL, MT.Empty, CS.NBT_HARDNESS, 0.5f, CS.NBT_RESISTANCE, 1.0f, CS.NBT_COLOR, UT.Code.getRGBInt(MT.White.fRGBaSolid), CS.NBT_TEXTURE, "woodchest", CS.NBT_INV_SIZE, 6, CS.NBT_FLAMMABILITY, 100));
				*/
	}
	/*
	private static void makeBoxes(OreDictMaterial odm, int iId, float fHard, int iSize, MultiTileEntityBlock tBox) {
//		if(odm.contains(gregapi.data.TD.ItemGenerator.CONTAINERS) && odm.mID > 0) {
			caMTEReg.add("Rock Sample Box ("+odm.getLocal()+")", CTAB_BOXES, iId, 0, SampleCrateTile.class, odm.mToolQuality, 1, tBox, 
					gregapi.util.UT.NBT.make(CS.NBT_MATERIAL, odm, CS.NBT_HARDNESS, fHard, CS.NBT_RESISTANCE, fHard, CS.NBT_COLOR, UT.Code.getRGBInt(odm.fRGBaSolid), CS.NBT_TEXTURE, "metalchest", CS.NBT_INV_SIZE, iSize),
					" PS", "PhP", " P ", 'P', OP.plate.dat(odm), 'S', OP.stick.dat(odm));
//		}
	}
	
	private static void makeWoodBox(OreDictMaterial odm, int iId, float fHard, int iSize, boolean bFlam, MultiTileEntityBlock tCrate) {
		caMTEReg.add("Rock Sample Box ("+odm.getLocal()+")", CTAB_BOXES, iId, 0, SampleCrateTile.class,	odm.mToolQuality, 1, tCrate,
				bFlam ? UT.NBT.make(CS.NBT_MATERIAL, odm, CS.NBT_COLOR, UT.Code.getRGBaInt(odm.fRGBaSolid), CS.NBT_HARDNESS, fHard, CS.NBT_RESISTANCE, fHard, CS.NBT_FLAMMABILITY, 100, CS.NBT_TEXTURE, "woodchest", CS.INV_SIZE, iSize)
						: UT.NBT.make(CS.NBT_MATERIAL, odm, CS.NBT_COLOR, UT.Code.getRGBaInt(odm.fRGBaSolid), CS.NBT_HARDNESS, fHard, CS.NBT_RESISTANCE, fHard, CS.NBT_TEXTURE, "woodchest", CS.INV_SIZE, iSize),
				" PS", "P P", " P ", 'P', OP.plate.dat(odm), 'S', OP.stick.dat(odm));
	}
	*/
}
