package com.github.canisartorus.prospectorjournal;

import java.util.List;

import gregapi.data.OP;
import gregapi.data.TC;
import gregapi.util.CR;
import gregapi.util.ST;
import net.minecraft.item.ItemStack;

//@author Alexander James

public class Items {
	public static ItemStack noteBook;

	static void RegisterItems() {
		new gregapi.item.multiitem.MultiItemRandom(ProspectorJournal.MOD_ID, "ca.prospectorjournal.notebook") {
			@Override
			public void addItems() {
				Items.noteBook = addItem(0, "Prospector's Journal", "Cross-referencing all the rocks.", JournalBehaviour.INSTANCE, TC.PERFODIO.get(2), TC.COGNITO.get(4), TC.ORDO.get(1));
//				Items.noteBook = ST.make(this, 1, 0);
				
				CR.shaped(Items.noteBook, CR.DEF, "PRP", "RDR", "PRP", 'D', OP.dye, 'P', ST.make(net.minecraft.init.Items.paper, 1, 0), 'R', OP.rockGt);
				
				gregapi.data.CS.BooksGT.BOOK_REGISTER.add(ST.make(ProspectorJournal.MOD_DATA, "ca.prospectorjournal.notebook", 1, 0), (byte)53);
			}
		};
		
	}

	static void RegisterRecipes() {
		CR.shaped(Items.noteBook, CR.DEF, "PRP", "RDR", "PRP", 'D', OP.dye, 'P', ST.make(net.minecraft.init.Items.paper, 1, 0), 'R', OP.rockGt);
	}
}
