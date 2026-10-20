package com.example.model

data class TouchHudButton(
    val id: String,
    val label: String,
    val actionDescription: String,
    val shortcutKey: String,
    val offsetXPercent: Float, // 0f to 100f
    val offsetYPercent: Float, // 0f to 100f
    val diameterDp: Int = 60,
    val isPrimary: Boolean = false,
    val category: String = "Action" // "Attack", "Action", "D-Pad", "Trigger"
)

data class HudPreferences(
    val scaleMultiplier: Float = 1.0f,
    val opacity: Float = 0.75f,
    val hapticFeedbackEnabled: Boolean = true,
    val showJoystickLeft: Boolean = true,
    val showJoystickRight: Boolean = true,
    val showTriggers: Boolean = true,
    val showDpad: Boolean = true
)

object DefaultHudLayout {
    val BUTTONS = listOf(
        // Right side Action & Combat cluster
        TouchHudButton(
            id = "btn_r1",
            label = "R1",
            actionDescription = "Right Weapon Light Attack",
            shortcutKey = "RB",
            offsetXPercent = 88f,
            offsetYPercent = 45f,
            diameterDp = 58,
            isPrimary = true,
            category = "Attack"
        ),
        TouchHudButton(
            id = "btn_r2",
            label = "R2",
            actionDescription = "Right Weapon Heavy Attack / Jump Attack",
            shortcutKey = "RT",
            offsetXPercent = 88f,
            offsetYPercent = 30f,
            diameterDp = 58,
            isPrimary = false,
            category = "Attack"
        ),
        TouchHudButton(
            id = "btn_l1",
            label = "L1",
            actionDescription = "Left Hand Guard / Shield Block",
            shortcutKey = "LB",
            offsetXPercent = 12f,
            offsetYPercent = 30f,
            diameterDp = 58,
            isPrimary = true,
            category = "Trigger"
        ),
        TouchHudButton(
            id = "btn_l2",
            label = "L2",
            actionDescription = "Parry / Shield Bash / Weapon Skill",
            shortcutKey = "LT",
            offsetXPercent = 12f,
            offsetYPercent = 45f,
            diameterDp = 58,
            isPrimary = false,
            category = "Trigger"
        ),
        TouchHudButton(
            id = "btn_b",
            label = "B",
            actionDescription = "Roll / Dodge / Backstep / Sprint",
            shortcutKey = "B",
            offsetXPercent = 92f,
            offsetYPercent = 70f,
            diameterDp = 64,
            isPrimary = true,
            category = "Action"
        ),
        TouchHudButton(
            id = "btn_a",
            label = "A",
            actionDescription = "Interact / Confirm / Loot",
            shortcutKey = "A",
            offsetXPercent = 82f,
            offsetYPercent = 82f,
            diameterDp = 56,
            isPrimary = true,
            category = "Action"
        ),
        TouchHudButton(
            id = "btn_x",
            label = "X",
            actionDescription = "Use Estus Flask / Equipped Item",
            shortcutKey = "X",
            offsetXPercent = 74f,
            offsetYPercent = 68f,
            diameterDp = 56,
            isPrimary = true,
            category = "Action"
        ),
        TouchHudButton(
            id = "btn_y",
            label = "Y",
            actionDescription = "Two-Handed Stance / Hold for Left 2H",
            shortcutKey = "Y",
            offsetXPercent = 82f,
            offsetYPercent = 56f,
            diameterDp = 56,
            isPrimary = false,
            category = "Action"
        ),
        TouchHudButton(
            id = "btn_lock",
            label = "R3",
            actionDescription = "Lock-on Enemy Target / Reset Camera",
            shortcutKey = "R3",
            offsetXPercent = 65f,
            offsetYPercent = 82f,
            diameterDp = 50,
            isPrimary = false,
            category = "Action"
        ),
        // D-Pad Switchers
        TouchHudButton(
            id = "dpad_up",
            label = "▲",
            actionDescription = "Cycle Attuned Spell / Pyromancy / Miracle",
            shortcutKey = "D-Up",
            offsetXPercent = 25f,
            offsetYPercent = 56f,
            diameterDp = 48,
            isPrimary = false,
            category = "D-Pad"
        ),
        TouchHudButton(
            id = "dpad_down",
            label = "▼",
            actionDescription = "Cycle Equipped Belt Item (Estus)",
            shortcutKey = "D-Down",
            offsetXPercent = 25f,
            offsetYPercent = 78f,
            diameterDp = 48,
            isPrimary = false,
            category = "D-Pad"
        ),
        TouchHudButton(
            id = "dpad_left",
            label = "◄",
            actionDescription = "Switch Left Hand Weapon / Shield",
            shortcutKey = "D-Left",
            offsetXPercent = 18f,
            offsetYPercent = 67f,
            diameterDp = 48,
            isPrimary = false,
            category = "D-Pad"
        ),
        TouchHudButton(
            id = "dpad_right",
            label = "►",
            actionDescription = "Switch Right Hand Weapon",
            shortcutKey = "D-Right",
            offsetXPercent = 32f,
            offsetYPercent = 67f,
            diameterDp = 48,
            isPrimary = false,
            category = "D-Pad"
        )
    )
}
