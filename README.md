# Civilization Simulator (civ)
A Civilization Simulator coded in Python (pygame) and Java.

## Description

Originally programmed in RBLX Lua, this zero-player game features random terrain generation and AI empires that fight with each other to become the dominant empire in a hex-based world. The game has been carefully balanced as to let no one empire become dominant and stop the game (Roman Empire scenario) or many tiny empires, if you can call them such, that are essentially city-states in constant flux (Holy Roman Empire scenario). Instead, empires vary in size, power, and lasting on the global scale (even without empire-specific modifiers, yet!).

This game is programmed in Python3, using pygame, but being phased over to Java for better performance and a better reflection of the object-oriented nature of this game. I'm also slightly terrified of what I've done with the Python version and it needs a serious overhaul but I'm kinda afraid to touch it.

## Features

..* Random terrain generation using OpenSimplex noise
..* Expansion, growth, and economy
..* Diplomacy through war and peace deals
..* Stability (but mostly lack thereof)
..* Rebellion and civil war scenarios

## What do Empires do?

..* Spawn in territories without any government and expand to fill the anarchy
..* Grow an economy that makes them more able to fight in conflicts
..* Manage stability in an expanding empire which can pose internal threats
..* Rebel against an existing empire for independence or civil war
..* Fight in wars with other empires (that they think they can win against)
..* Launch liberation wars occasionally when an empire annexes a smaller empire
..* Create peace deals to minimize border gore
..* Tactical combat that also minimizes border gore, relying on economy and neighboring tiles
..* Sea invasions that exist because eternal island nations off the main continent are boring
..* Systematic empire collapse creating power vacuums, successor nations, but probably both
..* More features coming soon

# Installation
Clone this repo and navigate to the respective language folder you want to run this program off.

## Python3 with pygame

1. Navigate to the Python folder
2. Make the necessary imports:

```bash
pip install pygame
pip install noise
```

3. Run the program:

```bash
python3 civ.py
```

## Java

1. Navigate to the Java folder
2. No imports are necessary other than the standard Java libraries:

```bash
java Main
```