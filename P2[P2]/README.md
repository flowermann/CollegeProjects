Homework I've done for the class Programming 2, written in C. Unlike programming 1, I do actually have the project specifications for this one, but also unlike Programming 1... I've only done the first homework
with the basic C syntax. The reasoning behind this was simple: The college and students wwere in protest of the government the entire year, and after the protests ended, we had three years to wrap up the 
entire school year, both semesters, so unfortunately i did not get to finish the other one on time, which is a shame. Nonetheless, the same applies here as it does to Programming 1: these are codes 
i do not take too seriously, and are very simple. The only reason im uploading them is out of archiving purposes, and as such, these codes arent that important. Still, i felt i should post them, so 
heres a rough translation of the homework assignment file. 

# Programming 2 - Preparatory Tasks for Homework 1

Write a C program to perform data processing according to the task description. The main program must:
1. Call functions that read all required input data from the standard input following the specified format;
2. Call appropriate functions to perform the requested processing;
3. Call appropriate functions to print all resulting outputs.

Ensure proper program decomposition into subprograms based on requirements and the structure above. Additional subprograms may be introduced if necessary. The student should define function names and required parameters. Pass only necessary data to functions. Subprograms must not access global/main variables directly, but only through arguments and return values.

Use dynamic memory allocation for data structures (arrays or matrices). If the specified dimensions are invalid, terminate execution cleanly without printing anything. Choose data types according to task requirements, or use generic types where algorithms permit. In case of memory allocation failure, print `MEM_GRESKA` and terminate execution cleanly. Deallocate all dynamic memory prior to program termination.

Read input according to expected data types. All input is provided via standard input according to specifications. Assume input formats are valid unless explicitly stated otherwise. Valid matrix/array dimensions are integers greater than 0. Return 0 upon completion in all valid execution flows. Floating-point numbers in output should be rounded to two decimal places.

---

## Task 0: Mobile Network Impulse Billing

Write a program that calculates telephone impulses used in a small company's mobile network based on a sequence of call logs. An impulse represents a billing period measured in seconds. Employees are identified by phone numbers (integers), where the last two digits uniquely identify the employee (up to 100 employees).

Input sequence:
1. Total number of calls;
2. Array of phone numbers (one line, space-separated);
3. Array of call durations in seconds (next line, space-separated);
4. Impulse length in seconds (billing period).

An impulse is charged if at least one second of the billing period has started.

Requirements:
1. Read call array length. Verify length > 0; terminate cleanly (return 0) if invalid. Read phone numbers and durations.
2. Verify phone numbers fall within range [0-99] and durations are positive (> 0). Terminate cleanly (return 0) if invalid.
3. Echo input phone numbers and call durations on separate lines.
4. Calculate total impulses per employee and for the entire company.
5. Print total company impulses on a new line, followed by per-employee impulse usage for employees with > 0 impulses (sorted by employee ID ascending).

---

## Task 1: Checkout Lane Assignment

Write a program that assigns incoming customers to available checkout registers. Customers are represented by the number of items they carry and are processed in arrival order to the first available checkout register. Item scanning time is uniform across all registers.

Requirements:
1. Read number of customers. Verify count > 0; terminate cleanly (return 0) if invalid.
2. Read items per customer. Verify item counts are positive (> 0); terminate cleanly (return 0) if invalid.
3. Read number of registers. Verify register count > 0; terminate cleanly (return 0) if invalid.
4. Assign customers to registers according to queue balance criteria and print final register assignments (one line per register, space-separated customer item counts).

---

## Task 2: Sudoku Solver Validation

Write a program to validate a Sudoku puzzle solution on an $N^2 \times N^2$ grid, subdivided into $N \times N$ regions containing integers in range $[1, N^2]$.

Validation Criteria:
1. Each cell contains an integer within range $[1, N^2]$.
2. Every row, column, and $N \times N$ region contains unique numbers.

Error Messages:
* Out-of-range cell values: `LOS OPSEG`
* Non-unique row values: `LOSE KOLONE` or `LOSI REDOVI`
* Non-unique region values: `LOSI REGIONI`
* Valid solution: `VALIDNO RESENJE`

Requirements:
1. Read dimension factor $N$. Verify $N > 0$; terminate cleanly if invalid.
2. Call function to read grid matrix.
3. Call function to validate cell ranges; terminate with corresponding error message if invalid.
4. Call function to print input grid.
5. Call function to validate uniqueness constraints and print resulting validation message.

---

## Task 3: Conway's Game of Life

Write a program to simulate Conway's Game of Life on a grid of cells represented by `O` (alive) and `X` (dead).

State Rules:
1. Live cell with 2 or 3 live neighbors remains alive (`O`).
2. Dead cell with exactly 3 live neighbors becomes alive (`O`).
3. All other cells become or remain dead (`X`).

Requirements:
1. Read grid dimensions. Verify dimensions > 0; terminate cleanly (return 0) if invalid.
2. Read initial cell grid.
3. Print initial cell grid under header `INITIAL`.
4. Read number of iterations. Verify iterations > 0; terminate cleanly (return 0) if invalid.
5. Compute and print grid state for each step under headers `ITERATION0`, `ITERATION1`, etc.
