# Mob Gems
Mob Gems is a **Fabric** mod for Minecraft **1.16.5** based around capturing mobs in diamonds to harness their powers!

- With the Capture enchantment you can trap mobs in mob gems
- Bracelets allows you to gain abilities (good or bad) based on mob gems
- Gem Cages utilise mob gems to create area effects

**Note:** This mod supports **Curios API** (https://www.curseforge.com/minecraft/mc-mods/curios-fabric), which allows you to wear bracelets and chestpieces at the same time!

## Capturing mobs
Trapping the souls of mobs in mob gems requires you to enchant a weapon with Capture. It is a rare enchantment that can't be found in the enchantment table, 
but it can be acquired through all other usual methods (villagers, loot). In addition, it also has a very high likelihood of appearing in **stronghold libraries**.
There are three levels available, each level with a higher chance of capturing a mob when killed with the enchanted weapon. To obtain a mob gem, 
make sure to have a **diamond in your off-hand**, which will be turned into a mob gem if you manage to capture a mob's soul.

**Note:** Capture can not be combined with Mending or Looting.

## Using mob gems

### Bracelets
Bracelets grant the wearer certain effects or abillities that represent a mob's characteristics. These can be either good or bad depending on the mob and the type of bracelet.
Bracelets can be worn on the chest, in your main or off-hand or in the Curios Bracelet slot. Only one bracelet can be active at a time. You can also right click a mob with a bracelet to have them equip it.

- **Golden Bracelets** augment a mob's characteristic, granting the wearer an effect that mimics a mob's behaviour, abilities, etc...
- **Iron Bracelets** diminish a mob's characteristic, granting the wearer an opposite effect of a mob's characteristic

<details>
  <summary>See crafting recipes</summary>
  
  ![alt text](https://github.com/JrmWrm/mob-gems/blob/master/recipes/golden_bracelet.png "Golden Bracelet recipe")
  ![alt text](https://github.com/JrmWrm/mob-gems/blob/master/recipes/iron_bracelet.png "Iron Bracelet recipe")
  
</details>

### Gem Cage
Gem Cages are magical blocks that utilize a mob's soul for useful effects on the area or adjacent blocks. Gem cages require a mob gem to function, 
but also a new resource for power: **soul powder**. Crafted from blaze powder and soul sand/soil this magical fuel will power the gem cage and allow it to extract 
effects from the trapped soul of a mob.

<details>
  <summary>See crafting recipes</summary>
  
  ![alt text](https://github.com/JrmWrm/mob-gems/blob/master/recipes/gem_cage.png "Gem Cage recipe")
  ![alt text](https://github.com/JrmWrm/mob-gems/blob/master/recipes/soul_powder.png "Soul Powder recipe")
  
</details>

## Mob Gem effects
I recommend figuring out all the different effects on your own, prt of the fun is in finding out! However some can be hard to deduce or 
maybe you like knowing what you're getting yourself into. No worries, all effects have been listed below.

**Note:** Only the Mob Gems listed here have been implemented as of yet. Others will be coming in the future.

<details>
  <summary>See effects</summary>
  
  | Mob           | Golden Bracelet | Iron Bracelet  | Gem Cage     |
  | ------------- | --------------- | -------------- | ------------ |
  | Zombie (& variants) | Eating any food will give the wearer hunger | The wearer can eat rotten flesh without getting the hunger effect | All villagers in range will be turned into zombie villagers |
  | Skeleton (& variants) | Bows will behave like they have infinity | Wolfs won't get angered at you | Projectiles are warded off in the area |
  | Creeper | When hostile mobs are near and the wearer is not on full health, they will ignite. Run away before you explode! | The wearer gains the new Blast Resistence status effect | All mobs in range can be ignited with flint and steel |
  | Iron Golem | The wearer will have extra max health | The wearer will have less max health | All hostile mobs in range will be pushed away |
  | Cow | All status effects will be removed while wearing | All status effects will slowly increase in strenght (max 5) | All mobs in range can now be milked |
  | Bee | The wearer will polinate (bonemeal) blocks while walking | The wearer will decay organic blocks while walking. Flowers have a small chance of turning into wither roses | All crops and saplings in range will grow faster |
  | Chicken | The wearer will recieve the slow falling effect | The wearer will be heavier: less jump height & more fall damage | All mobs in range will now lay eggs |
  
</details>



