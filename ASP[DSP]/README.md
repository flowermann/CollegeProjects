This is a homework assignment I've done for the class "Data Structures and Algorithms". The following is a rough translation of the projects specifications. Also, the file "cm240685d.cpp is the project. 
Im not sure what i was doing back when i was writing this in January 2026, but i probably had some problems with the IDE, so i had multiple versions of the same code, more or less. 

# Smart Building Sensor Network Simulator

An implementation of a Smart Building Sensor System model built in C++ using Graph Data Structures. Developed as part of the Algorithms and Data Structures course at the Faculty of Electrical Engineering, University of Belgrade (ETF).

---

## Problem Overview

Modern smart buildings rely on networks of diverse sensors (temperature, smoke, motion, etc.) communicating with each other. This project models such a system using a directed weighted graph, where:
* Vertices (Nodes): Represent sensors, characterized by a unique ID and operational speed (v in [1, 100]).
* Edges: Represent direct communication links.
* Edge Weights: Represent signal/packet transmission times in milliseconds.

### Link Probability & Weight Calculation
The existence of a link between two sensors is determined pseudo-randomly based on a user-defined threshold p in [1/n, ln(n)/n], where n is the total number of sensors. 

For each pair of sensors (i, j), if u < p (where u is in [0, 1)), a directed edge is formed. The weight (latency) is calculated as:
t[i, j] = d[i, j] + 100 - min(v[i], v[j])
where d[i, j] in [0.5, 30.0] represents the pseudo-random distance between sensors.

---

## Key Features

### 1. Pseudo-Random Number Generators (PRNG)
Custom, non-STL implementations of PRNG engines:
* Linear Congruential Generator (LCG)
* Blum Blum Shub (BBS) with parity bit extraction

### 2. Graph Representations
Custom implementations of graph storage structures (without Standard Template Library / STL usage):
* Adjacency Matrix
* Linked Adjacency List
* Sequential / Linearized Adjacency List

### 3. Graph Operations & Management
* Dynamic Sensor Generation: Interactive creation of n sensors with random characteristics and link matrices.
* Manual Sensor Insertion / Deletion: Dynamically add a new sensor with fresh generated distances/probabilities, or remove an existing sensor along with all its incoming/outgoing edges.
* Console Visualization: Print graph representations and status matrices.
* Memory Management: Explicit dynamic memory deallocation to prevent memory leaks.

### 4. Network Analysis Algorithms
* System Status Check (BFS & DFS): Traverse reachable sensors starting from a specified node. Neighbors are visited in ascending order of their sensor IDs.
* Shortest Path & Latency (Dijkstra's Algorithm): Calculate the fastest signal path and overall latency between two given sensors.
* Network Centrality & Eccentricity: Determine the eccentricity of each sensor node and identify the Central Sensor (center of the graph) with minimum eccentricity.

---

## Tech Stack & Implementation Rules

* Language: C++
* STL Restrictions: Built entirely without Standard Template Library (STL) containers (e.g., std::vector, std::list, std::map) or external libraries to demonstrate manual memory management and low-level data structure design.
* Iterative Logic: Algorithms are implemented iteratively without recursion to optimize system resource usage.

---

## Interactive CLI Interface

The program provides a simple, loop-driven command-line interface (CLI) allowing users to execute operations sequentially:

================ SMART BUILDING SENSOR SYSTEM ================
1. Generate Sensor Network (PRNG)
2. Add Sensor Manually
3. Remove Sensor by ID
4. Display Graph Representation
5. Run BFS/DFS Status Check
6. Find Shortest Signal Path (Min Latency)
7. Calculate Node Eccentricities & Center Sensor
8. Clear Network / Free Memory
0. Exit
==============================================================

---

## License & Academic Context
* Institution: Faculty of Electrical Engineering, University of Belgrade (ETF)
* Course: Algorithms and Data Structures (13E112ASP)
* Academic Year: 2025/2026
