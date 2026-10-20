package com.example.model

data class SaveCharacter(
    val id: String,
    val name: String,
    val className: String,
    val soulLevel: Int,
    val soulsCount: Long,
    val humanityCount: Int,
    val currentArea: String,
    val playtimeHours: Float,
    val primaryWeapon: String,
    val shield: String,
    val armorSet: String,
    val vitality: Int,
    val attunement: Int,
    val endurance: Int,
    val strength: Int,
    val dexterity: Int,
    val resistance: Int,
    val intelligence: Int,
    val faith: Int,
    val currentEquipWeight: Float,
    val maxEquipWeight: Float
) {
    val equipLoadRatio: Float
        get() = if (maxEquipWeight > 0f) currentEquipWeight / maxEquipWeight else 0f

    val rollType: String
        get() = when {
            equipLoadRatio <= 0.25f -> "Fast Roll (< 25% Load)"
            equipLoadRatio <= 0.50f -> "Mid Roll (< 50% Load)"
            else -> "Fat Roll (> 50% Load)"
        }
}

object SampleSaves {
    val CHARACTERS = listOf(
        SaveCharacter(
            id = "slot_1",
            name = "Chosen Undead",
            className = "Warrior",
            soulLevel = 42,
            soulsCount = 28450,
            humanityCount = 14,
            currentArea = "Anor Londo (Main Cathedral)",
            playtimeHours = 34.5f,
            primaryWeapon = "Claymore +10",
            shield = "Grass Crest Shield",
            armorSet = "Elite Knight Armor",
            vitality = 25,
            attunement = 10,
            endurance = 28,
            strength = 27,
            dexterity = 24,
            resistance = 11,
            intelligence = 9,
            faith = 9,
            currentEquipWeight = 26.4f,
            maxEquipWeight = 68.0f
        ),
        SaveCharacter(
            id = "slot_2",
            name = "Solaire Disciple",
            className = "Knight",
            soulLevel = 65,
            soulsCount = 112000,
            humanityCount = 28,
            currentArea = "Kiln of the First Flame",
            playtimeHours = 58.2f,
            primaryWeapon = "Sunlight Straight Sword +15",
            shield = "Sunlight Shield",
            armorSet = "Iron Armor of the Sun",
            vitality = 32,
            attunement = 14,
            endurance = 35,
            strength = 30,
            dexterity = 18,
            resistance = 10,
            intelligence = 9,
            faith = 30,
            currentEquipWeight = 34.0f,
            maxEquipWeight = 75.0f
        ),
        SaveCharacter(
            id = "slot_3",
            name = "Chaos Witch",
            className = "Pyromancer",
            soulLevel = 22,
            soulsCount = 4320,
            humanityCount = 6,
            currentArea = "Blighttown (Swamp Bonfire)",
            playtimeHours = 12.8f,
            primaryWeapon = "Reinforced Club +5 (Fire)",
            shield = "Heater Shield",
            armorSet = "Gold-Hemmed Black Cloak",
            vitality = 18,
            attunement = 16,
            endurance = 20,
            strength = 14,
            dexterity = 12,
            resistance = 12,
            intelligence = 10,
            faith = 8,
            currentEquipWeight = 11.2f,
            maxEquipWeight = 60.0f
        )
    )
}
