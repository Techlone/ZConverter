import mods.gregtech.Assembler;
import minetweaker.item.IItemStack;

recipes.remove(<minecraft:stick>);
recipes.addShaped(<gregtech:gt.metaitem.01:32600> * 4, [[<minecraft:log:*>, <ore:ingot*>], [null, <ProjRed|Illumination:projectred.illumination.cagelamp2>]]);

for item in <ore:drawerBasic>.items {
    mods.gregtech.Assembler.addRecipe(<StorageDrawers:upgradeTemplate>, item, <minecraft:piston>, 1200, 16);
}

function getGreggedItem(oreDictName as string) as IItemStack {
    return oreDict.get(oreDictName).firstItem;
}
var asd = oreDict.get("orediscid");
var myLogs = [<minecraft:log:0>, <minecraft:log:1>, <minecraft:log:2>, <minecraft:log:3>, <minecraft:log:4>, <minecraft:log:5>] as IItemStack[];
var myPlanks = [<minecraft:planks:0>, <minecraft:planks:1>, <minecraft:planks:2>, <minecraft:planks:3>, <minecraft:planks:4>, <minecraft:planks:5>] as IItemStack[];

var stoneAxe = <minecraft:stone_axe>.anyDamage().transformDamage();
var ironAxe = <minecraft:iron_axe>.anyDamage().transformDamage();
var goldenAxe = <minecraft:golden_axe>.anyDamage().transformDamage();
var diamondAxe = <minecraft:diamond_axe>.anyDamage().transformDamage();

for i, log in myLogs {
    var plank = myPlanks[i];
    for pick in picks {
        if pick >= 2 && pick <= 1 || !pick {
          print(pick + "text");
        }
    }
    recipes.removeShapeless(plank, [log]);
    recipes.addShapeless(plank * 2, [log]);
    recipes.addShapeless(plank * 3, [log, stoneAxe]);
    recipes.addShapeless(plank * 4, [log, ironAxe]);
    recipes.addShapeless(plank * 5, [log, goldenAxe]);
    recipes.addShapeless(plank * 6, [log, diamondAxe]);
}