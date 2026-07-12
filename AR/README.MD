This code was written in a special, custom assembler app, coupled with a simulator of a computer system to follow whats happening to the memory locations and registers after assembling said code in the app. Unfortunately, due to
copyright issues (i am not sure if im allowed to share said programs, as the college owns them as intellectual property), i cannot share the apps here so the program is a bit uncheckable. But it works! I promise :)

            The following is a rough translations of the project requirements and specifications file, which was originally written in Serbian. Its prone to mistakes, which i will hopefully correct soon.

# Homework Assignment: Computer Architecture

## Section A (Variant A1)

* **Main Program Location:** Start at address `4000h`.


* **Interrupt Vector (IV) Table & IVTP Configuration:** Initialize the IV table and the `ivtp` register according to the layout below (where `IVTP = 0100h`):



| Entry Number | Content (Address) | Device |
| --- | --- | --- |
| 5 | `0500h` | KP1.1 |
| 4 | `1000h` | KP1.2 |
| 2 | `1500h` | KP2.1 |
| 3 | `2000h` | DMA1.1 |
| 1 | `2500h` | DMA1.2 |
| 0 | `3000h` | DMA1.4 |

* **Task Description:** Load arrays `A` and `B` into memory starting at addresses `5000h` and `6000h`, respectively. Both arrays contain `8h` elements. Load array `A` from **KP1.1** using the *interrupt mechanism*, and load array `B` from **KP2.1** by polling its status flags (*polling / ready bit checking*). The data transfer from both peripherals must be performed concurrently.



---

## Section B (Variant B0)

* **Subroutine Task:** Write a subroutine `int sumAll(int* arr1, int* arr2, int n)` that calculates the sum of elements from two separate arrays and returns the final result in register `R0`. The parameters `arr1` and `arr2` are pointers to the arrays to be summed, and `n` specifies the number of elements to process.


* **Execution Flow:** After arrays `A` and `B` are fully loaded, call the `sumAll` function to sum all of their elements. Following the function call, store the resulting value from `R0` into memory location `9999h`.


* **DMA Task:** Additionally, copy array `B` into memory starting from address `6100h` using the **DMA1.4** controller running in **burst mode**.



---

## Section C (Variant C2)

* **Task Description:** Upon completing Section B, transmit array `A` to the **KP1.2** peripheral using the *interrupt mechanism*.


* **DMA Task:** Send the value currently stored at memory location `9999h` to the **DMA1.2** controller configured to run in **burst mode**.



---

## Technical Documentation & Registers

### Standard Peripherals

| Peripheral | Control | Status | Entry | Data |
| --- | --- | --- | --- | --- |
| **KP1 (KP1.1)** | F100 | F101 | F102 | F103 |
| **KP1.2** | F140 | F141 | F142 | F143 |
| **KP2 (KP2.1)** | F200 | F201 | F202 | F203 |

#### Peripheral Control Register Bit Layout:

```
15                                                               0
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+----+
| / | / | / | / | / | / | / | / | / | / | / | / |lvl|enb|i/o|strt|
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+----+

```

* **start (strt):** Value `1` turns the peripheral ON.


* **input/output (i/o):** Value `1` designates an input device, `0` designates an output device.


* **level (lvl):** Must be set to `1` if the device operates using interrupts (if the `enable` bit is `1`), otherwise it must be set to `0`.



#### Peripheral Status Register Bit Layout:

The lowest bit of the status register represents the **ready** status.

```
15                                                               0
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+----+
| / | / | / | / | / | / | / | / | / | / | / | / | / | / | / |rdy |
+---+---+---+---+---+---+---+---+---+---+---+---+---+---+---+----+

```

---

### Direct Memory Access (DMA) Controllers

| Controller | Control | Entry | Count | AR1 | AR2 |
| --- | --- | --- | --- | --- | --- |
| **DMA1.2** | F040 | F042 | F044 | F045 | F046 |
| **DMA1.4** | F0C0 | F0C2 | F0C4 | F0C5 | F0C6 |

* **AR1:** Source memory address register.


* **AR2:** Destination memory address register.


* **Count:** Number of data elements to be transferred.



#### DMA Control Register Bit Layout:

```
15                                                               0
+---+---+---+---+---+---+---+---+-+-+-+-+-+-+-+------------------+
| / | / | / | / | / | / | / | / |b|c|d|i|m|l|s|e|      i/o       |
+---+---+---+---+---+---+---+---+-+-+-+-+-+-+-+------------------+

```

*(Where: b = burst/cycle_stealing, c = cycle_stealing, d = dec, i = inc, m = mem, l = level, s = start, e = enable, i/o = input/output)*

* **start:** Value `1` turns the DMA controller ON.


* **input/output (i/o):** Value `1` defines an input operation (data transferred from a peripheral to memory), `0` defines an output operation (data transferred from memory to a peripheral).


* **mem:** If performing a standard peripheral-to-memory or memory-to-peripheral transfer, this bit must be `0`. Setting `mem = 1` activates a direct **Memory-to-Memory** transfer.


* **level (l):** Must be set to `1` if the DMA operates using interrupts (`enable` bit is set to `1`).


* **burst / cycle_stealing (b/c):** Value `1` enables **burst mode** (block transfer), whereas value `0` runs data transfers in single **cycle-stealing** bus cycles.


* **update (i/d):** Configuration bits signify whether the controller runs with or without dynamically updating the source address register (**AR1**). The destination address register (**AR2**) is **always updated**.


* **inc/dec:** Dictates whether the active registers increment (`0`) or decrement (`1`) during modifications.



---

## Important Implementation Notes

* Implement your assignment using the **SPasm** assembler and the **SPECSeo** simulator matching the guidelines detailed across laboratory exercise manuals.


* The structure of your `VEZBA.ASM` assembly source file initiates with a mandatory hardware block isolated between `! inicijalizacija simulacije` and `! kraj inicijalizacije` comment wrappers. **Do not modify the statements located between these initialization comments.**


* Standard peripherals (excluding DMAs) configured via interrupts cannot be safely deactivated at the exact millisecond they process their final data payload. They must instead be disabled upon their subsequent pending interrupt request.


* Write your subroutines below the main program block (directly under the `halt` execution instruction statement) by explicitly identifying them using code labels representing their respective function names.


* Pass all parameters to subroutines **via the stack in the reverse order** of their declaration signature. The caller context holds sole responsibility for resetting/cleaning arguments off the stack upon subroutine return (the caller cleans the stack, not the callee).


* Data types defined as `int` are **16 bits wide**. All memory pointers span **16 bits wide**; the smallest individually addressable units across the target architecture are **16 bits wide**.


* The execution stack grows dynamically downward from **higher memory address locations toward lower locations**. The stack pointer register (`SP`) points to the memory slot holding the last active pushed element.
