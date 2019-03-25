package com.canisartorus.prospectorjournal.lib;

// @Author Alexander James

public class RockMatter {
	public int dim, ore = 0, cx, cz;
	public int x, z;
	public short y, multiple = 1;
	public boolean sample = true;
	
	public RockMatter(int ore, int dim, int x, int y, int z, boolean rock=true) {
		this.dim = dim;
		this.x = x;
		this.y = (short) y;
		this.z = z;
		this.ore = ore;
		this.sample = rock;
		this.cx = x / 16;
		this.cz = z / 16;
	}

	public RockMatter(net.minecraft.block.Block Ore, int dim, int x, int y, int z) {
		String sName = Ore.getUnlocalizedName();
			if(sName.equalIgnoreCase("gt.multitileentity.rock")) {	//is actually a gregapi.tileentity.notick.TileEntityBase03MultiTileEntities <- net.minecraft.tileentity.TileEntity
				// this.sample = true;
				this.ore = Ore.getDrops(0, false)[0].getMeta();	//XXX check method
				this.y = (short) y;
			} else if(sName.endsWith(".bedrock"){
				this.sample = false;
				this.y = 0;
				this.multiple = 4096;
				this.ore = Ore.getMeta();
			} else if (sName.startsWith("gt.meta.ore.normal.")){
				this.sample = false;
				this.y = (short) y;
				this.ore = Ore.getMeta();
			} else if (sName.startsWith("gt.flower") {
				// this.sample = true;
				this.y = 10;
				switch(sName) {
					case():	//TODO
						this.ore = 000;
						break;
					default:
						this.ore= 0;
						// TODO
				}
			}
		}
		this.dim = dim;
		this.x = x;
		this.z = z;
	}

}