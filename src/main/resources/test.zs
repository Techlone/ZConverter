import mods.gregtech.Assembler;

recipes.remove(<minecraft:stick>);
recipes.addShaped(<gregtech:gt.metaitem.01:32600> * 4, [[<minecraft:log:*>, <ore:ingot*>], [null, <ProjRed|Illumination:projectred.illumination.cagelamp2>]]);

for item in <ore:drawerBasic>.items {
	Assembler.addRecipe(<StorageDrawers:upgradeTemplate>, item, <minecraft:piston>, 1200, 16);
}
